package com.ludwici.slimeoverhaul.event;

import com.ludwici.crumbslib.api.AdvancementHelper;
import com.ludwici.crumbslib.api.CrumbSupplier;
import com.ludwici.slimeoverhaul.data.PyrocideLevel;
import com.ludwici.slimeoverhaul.effect.FieryFuryEffect;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import static com.ludwici.slimeoverhaul.Content.*;

public final class GameplayEvents {
    private GameplayEvents() {
    }

    public static void init() {
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamage, finalDamage, blocked) -> {
            if (!blocked && finalDamage > 0) {
                onLivingDamagePost(source, baseDamage);
            }
        });

        AttackEntityCallback.EVENT.register((player, level, hand, target, hitResult) -> {
            if (!level.isClientSide() && isCriticalHit(player)) {
                onCrit(player);
            }
            return InteractionResult.PASS;
        });
    }

    private static boolean isCriticalHit(Player player) {
        return player.fallDistance > 0.0F && !player.onGround() && !player.onClimbable()
                && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS)
                && !player.isPassenger() && !player.isSprinting();
    }

    private static void onCrit(Player player) {
        if (player.hasEffect(FIRE_FATIGUE.getHolder())) {
            return;
        }

        ItemStack weapon = player.getWeaponItem();
        PyrocideLevel level = weapon.get(PYROCIDE_LEVEL.get());
        if (level != null) {
            weapon.set(PYROCIDE_LEVEL.get(), level.levelUp());
        }
    }

    public static float modifyIncomingDamage(DamageSource source, float damage) {
        Entity attacker = source.getEntity();
        if (!(attacker instanceof LivingEntity livingEntity) || livingEntity.level().isClientSide()) {
            return damage;
        }

        if (livingEntity.hasEffect(FIRE_FATIGUE.getHolder())) {
            return damage;
        }

        ItemStack weapon = livingEntity.getWeaponItem();
        PyrocideLevel pyrocideLevel = weapon.get(PYROCIDE_LEVEL.get());
        if (pyrocideLevel == null) {
            return damage;
        }

        CrumbSupplier<MobEffect> effect = FieryFuryEffect.getEffect(livingEntity);
        if (livingEntity.getRandom().nextFloat() <= 0.5F) {
            if (effect == null) {
                livingEntity.addEffect(new MobEffectInstance(FIERY_FURY_EFFECT_I.getHolder(), 1200));
                weapon.set(PYROCIDE_LEVEL.get(), pyrocideLevel.resetDamage());
            } else {
                CrumbSupplier<MobEffect> next = FieryFuryEffect.getNext(livingEntity);
                if (next != null) {
                    livingEntity.removeEffect(effect.getHolder());
                    livingEntity.addEffect(new MobEffectInstance(next.getHolder(), 1200));
                }
            }
            return damage;
        }

        if (pyrocideLevel.getDamage() != 0.0F && FieryFuryEffect.hasEffect(livingEntity)) {
            weapon.set(PYROCIDE_LEVEL.get(), new PyrocideLevel());
            FieryFuryEffect.removeEffect(livingEntity);
            livingEntity.addEffect(new MobEffectInstance(FIRE_FATIGUE.getHolder(), 3600));
            return damage + pyrocideLevel.getDamage();
        }

        return damage;
    }

    private static void onLivingDamagePost(DamageSource source, float originalDamage) {
        Entity attacker = source.getEntity();
        if (!(attacker instanceof LivingEntity livingEntity)) {
            return;
        }
        if (livingEntity.hasEffect(FIRE_FATIGUE.getHolder()) && !FieryFuryEffect.hasEffect(livingEntity)) {
            return;
        }

        ItemStack weapon = livingEntity.getWeaponItem();
        PyrocideLevel progress = weapon.get(PYROCIDE_LEVEL.get());
        if (progress != null) {
            weapon.set(PYROCIDE_LEVEL.get(), progress.addDamage(originalDamage * progress.getDamageModifier()));
        }
    }

    public static void onShieldBlock(LivingEntity entity, DamageSource source, float originalDamage) {
        if (entity.level().isClientSide()) {
            return;
        }
        ItemStack shield = entity.getItemInHand(entity.getUsedItemHand());
        if (!isShieldItem(shield) || (entity instanceof Player player && player.isCreative())) {
            return;
        }

        CustomData data = shield.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return;
        }
        CompoundTag tag = data.copyTag();
        if (!tag.contains("bounce")) {
            return;
        }
        int bounce = tag.getInt("bounce");
        if (bounce <= 0) {
            tag.remove("bounce");
            shield.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            return;
        }
        if (!source.is(DamageTypes.THORNS) && source.isDirect() && source.getDirectEntity() instanceof LivingEntity attacker && attacker != entity) {
            attacker.hurt(entity.damageSources().thorns(entity), originalDamage);
        }

        bounce--;
        if (bounce > 0) {
            tag.putInt("bounce", bounce);
        } else {
            tag.remove("bounce");
            if (entity instanceof Player player) {
                player.sendSystemMessage(Component.translatable("slimeoverhaul.lost_earth_power"));
            }
        }
        shield.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static void onAnvilTake(Player player, ItemStack output) {
        if (!(player instanceof ServerPlayer serverPlayer) || !isShieldItem(output)) {
            return;
        }
        CustomData data = output.get(DataComponents.CUSTOM_DATA);
        if (data == null || !data.copyTag().contains("bounce")) {
            return;
        }
        var advancement = AdvancementHelper.getAdvancement(serverPlayer, "slimeoverhaul/earth_power");
        if (advancement != null) {
            serverPlayer.getAdvancements().award(advancement, "bounce_shield");
        }
    }
}
