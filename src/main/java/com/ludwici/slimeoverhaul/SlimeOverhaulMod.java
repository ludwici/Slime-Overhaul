package com.ludwici.slimeoverhaul;

import com.ludwici.crumbslib.api.*;
import com.ludwici.slimeoverhaul.entity.client.BaseSlimeRenderer;
import com.ludwici.slimeoverhaul.entity.custom.BaseSlime;
import com.ludwici.slimeoverhaul.entity.custom.elementals.EarthSlime;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.List;

import static com.ludwici.slimeoverhaul.Content.*;

@Mod(SlimeOverhaulMod.MODID)
public class SlimeOverhaulMod {
    public static final String MODID = "slimeoverhaul";
    private static Minecraft minecraft;

    public SlimeOverhaulMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        ModHelper.init(modEventBus, MinecraftForge.EVENT_BUS, MODID);
        EntityHelper.initBus();
        ItemHelper.initBus();
        BlockHelper.initBus();
        PotionHelper.initBus();
        MobEffectHelper.initBus();
        CreativeTabHelper.initBus();

        EventHelper.addEventRenderers(event -> {
            event.registerEntityRenderer(AIR_SLIME.get(), BaseSlimeRenderer::new);
            event.registerEntityRenderer(WATER_SLIME.get(), BaseSlimeRenderer::new);
            event.registerEntityRenderer(EARTH_SLIME.get(), BaseSlimeRenderer::new);
            event.registerEntityRenderer(FLAME_SLIME.get(), BaseSlimeRenderer::new);
        });

        EventHelper.addAttributeEvent(event -> {
            event.put(AIR_SLIME.get(), BaseSlime.createMobAttributes().build());
            event.put(WATER_SLIME.get(), BaseSlime.createMobAttributes().build());
            event.put(EARTH_SLIME.get(), EarthSlime.createMobAttributes().build());
            event.put(FLAME_SLIME.get(), BaseSlime.createMobAttributes().build());
        });

        modEventBus.addListener(Content::registerPotions);
        modEventBus.addListener(Content::spawns);

        MinecraftForge.EVENT_BUS.addListener(SlimeOverhaulMod::onKnock);
        MinecraftForge.EVENT_BUS.addListener(SlimeOverhaulMod::onTooltip);
        MinecraftForge.EVENT_BUS.addListener(SlimeOverhaulMod::onShieldBlock);
        MinecraftForge.EVENT_BUS.addListener(SlimeOverhaulMod::onInv);
        MinecraftForge.EVENT_BUS.addListener(SlimeOverhaulMod::onTrade);
        MinecraftForge.EVENT_BUS.addListener(Content::registerAnvilEvent);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinecraftForge.EVENT_BUS.addListener(SlimeOverhaulMod::handleKeyBindings);
        }

        Content.init();
    }

    private static void onTrade(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.CARTOGRAPHER) {
            var trades = event.getTrades();
            trades.get(2).add((trader, random) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8),
                    new ItemStack(PATTERN_SLIME.get(), 1),
                    12, 3, 0.05f
            ));
        }
    }

    private static void onKnock(LivingKnockBackEvent event) {
        if (event.getEntity().hasEffect(KNOCK_BACK_EFFECT.get())) {
            event.setCanceled(true);
        }
    }

    private static void onTooltip(ItemTooltipEvent event) {
        ItemStack item = event.getItemStack();
        if (item.is(Tags.Items.TOOLS_SHIELDS)) {
            List<Component> tooltipComponents = event.getToolTip();
            CompoundTag tag = item.getTag();
            if (tag != null && tag.contains("bounce")) {
                int bounce = tag.getInt("bounce");
                tooltipComponents.add(Component.translatable("slimeoverhaul.earth_power", bounce).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    private static void onShieldBlock(ShieldBlockEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack itemStack = entity.getItemInHand(entity.getUsedItemHand());

        if (itemStack.is(Tags.Items.TOOLS_SHIELDS)) {
            checkShieldBounce(itemStack, event, entity);
        }
    }

    private static void onInv(AnvilRepairEvent event) {
        ItemStack item = event.getOutput();
        if (item.is(Tags.Items.TOOLS_SHIELDS)) {
            CompoundTag tag = item.getTag();
            if (tag != null && tag.contains("bounce")) {
                var player = event.getEntity();
                if (player instanceof ServerPlayer serverPlayer) {
                    var advancement = AdvancementHelper.getAdvancement(serverPlayer, "slimeoverhaul/earth_power");
                    if (advancement != null) {
                        serverPlayer.getAdvancements().award(advancement, "bounce_shield");
                    }
                }
            }
        }
    }

    private static void checkShieldBounce(ItemStack shield, ShieldBlockEvent event, LivingEntity entity) {
        CompoundTag tag = shield.getTag();

        if (tag != null) {
            if (tag.contains("bounce")) {
                Player player = (Player) entity;

                int bounce = tag.getInt("bounce");
                if (player != null && player.isCreative()) {
                    return;
                }
                bounce--;
                DamageSource damageSource = event.getDamageSource();
                if (damageSource.getDirectEntity() instanceof Projectile projectile) {

                } else {
                    if (!damageSource.isIndirect()) {
                        var target = damageSource.getDirectEntity();

                        DamageSource newDamage = new DamageSource(damageSource.typeHolder(), player, player);

                        target.hurt(newDamage, event.getOriginalBlockedDamage());
                    }
                }

                if (bounce > 0) {
                    tag.putInt("bounce", bounce);
                } else {
                    tag.remove("bounce");
                    if (player != null) {
                        player.sendSystemMessage(Component.translatable("slimeoverhaul.lost_earth_power"));
                    }
                }
            }
        }
    }

    private static boolean canDoubleJump = true;

    private static void handleKeyBindings(InputEvent.Key event) {
        if (minecraft == null) {
            minecraft = Minecraft.getInstance();
        }

        if (minecraft.player == null) {
            return;
        }

        if (event.getAction() == InputConstants.PRESS) {
            if (event.getKey() == minecraft.options.keyJump.getKey().getValue()) {
                if (canDoubleJump) {
                    var player = minecraft.player;
                    if (!player.isCreative() && !player.isSpectator() && !player.onGround() && !player.onClimbable() && !player.isInWaterOrBubble() && !ElytraItem.isFlyEnabled(player.getItemBySlot(EquipmentSlot.CHEST)) && !player.isPassenger()) {
                        if (player.hasEffect(DOUBLE_JUMP_EFFECT.get())) {
                            minecraft.player.jumpFromGround();
                            canDoubleJump = false;
                        }
                    }
                }
            }
        }

        if (minecraft.player.onGround()) {
            canDoubleJump = true;
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
