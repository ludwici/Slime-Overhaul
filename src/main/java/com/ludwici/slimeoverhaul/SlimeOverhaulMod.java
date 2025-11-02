package com.ludwici.slimeoverhaul;

import com.ludwici.crumbslib.api.*;
import com.ludwici.slimeoverhaul.config.Config;
import com.ludwici.slimeoverhaul.entity.client.BaseSlimeRenderer;
import com.ludwici.slimeoverhaul.entity.custom.BaseSlime;
import com.ludwici.slimeoverhaul.entity.custom.elementals.EarthSlime;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.player.AnvilRepairEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;

import static com.ludwici.slimeoverhaul.Content.*;

@Mod(SlimeOverhaulMod.MODID)
public class SlimeOverhaulMod {
    public static final String MODID = "slimeoverhaul";
    private static Minecraft minecraft;

    public SlimeOverhaulMod(IEventBus modEventBus, ModContainer modContainer) {
        ModHelper.init(modEventBus, NeoForge.EVENT_BUS, MODID);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        EntityHelper.initBus();
        ItemHelper.initBus();
        BlockHelper.initBus();
        PotionHelper.initBus();
        MobEffectHelper.initBus();
        CreativeTabHelper.initBus();
        StructureHelper.initBus();
        BlockEntityHelper.initBus();

        modEventBus.addListener(Content::spawns);

        EventHelper.addBrewingRecipeEvent(Content::registerPotions);
        EventHelper.addAnvilUpdateEvent(Content::registerAnvilEvent);
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

        Content.init();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            NeoForge.EVENT_BUS.addListener(SlimeOverhaulMod::handleKeyBindings);
        }

        NeoForge.EVENT_BUS.addListener(SlimeOverhaulMod::onKnock);
        NeoForge.EVENT_BUS.addListener(SlimeOverhaulMod::onInv);
        NeoForge.EVENT_BUS.addListener(SlimeOverhaulMod::onTooltip);
        NeoForge.EVENT_BUS.addListener(SlimeOverhaulMod::onShieldBlock);
        NeoForge.EVENT_BUS.addListener(SlimeOverhaulMod::onTrade);
    }

    private static void onTrade(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.CARTOGRAPHER) {
            var trades = event.getTrades();
            trades.get(2).add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 8),
                    new ItemStack(PATTERN_SLIME.get(), 1),
                    12, 3, 0.05f
            ));
        }
    }

    private static void onTooltip(ItemTooltipEvent event) {
        ItemStack item = event.getItemStack();
        if (isShieldItem(item)) {
            List<Component> tooltipComponents = event.getToolTip();
            CustomData data = item.get(DataComponents.CUSTOM_DATA);
            if (data != null) {
                CompoundTag tag = data.copyTag();
                int bounce = tag.getInt("bounce");
                tooltipComponents.add(Component.translatable("slimeoverhaul.earth_power", bounce).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    private static void onInv(AnvilRepairEvent event) {
        ItemStack item = event.getOutput();
        if (isShieldItem(item)) {
            CustomData data = item.get(DataComponents.CUSTOM_DATA);
            if (data != null) {
                CompoundTag tag = data.copyTag();
                if (tag.contains("bounce")) {
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
    }

    private static void onShieldBlock(LivingShieldBlockEvent event) {
        LivingEntity entity = event.getEntity();

        ItemStack shield = entity.getItemInHand(entity.getUsedItemHand());

        if (isShieldItem(shield)) {
            checkShieldBounce(shield, event, entity);
        }
    }

    private static void checkShieldBounce(ItemStack shield, LivingShieldBlockEvent event, LivingEntity entity) {
        CustomData data = shield.get(DataComponents.CUSTOM_DATA);
        if (data != null) {
            CompoundTag tag = data.copyTag();
            if (tag.contains("bounce")) {
                Player player = (Player) entity;

                int bounce = tag.getInt("bounce");
                if (player != null && player.isCreative()) {
                    return;
                }
                bounce--;
                DamageContainer container = event.getDamageContainer();

                DamageSource damageSource = container.getSource();
                if (damageSource.getDirectEntity() instanceof Projectile projectile) {

                } else {
                    if (damageSource.isDirect()) {
                        var target = damageSource.getDirectEntity();

                        DamageSource newDamage = new DamageSource(damageSource.typeHolder(), player, player);
                        target.hurt(newDamage, container.getOriginalDamage());
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

                shield.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            }
        }
    }

    private static void onKnock(LivingKnockBackEvent event) {
        if (event.getEntity().hasEffect(KNOCK_BACK_EFFECT.getHolder())) {
            event.setCanceled(true);
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
                        if (player.hasEffect(DOUBLE_JUMP_EFFECT.getHolder())) {
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

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemProperties.register(
                    CLEANSING_BRUSH.get(),
                    ResourceLocation.withDefaultNamespace("brushing"),
                    ((stack, level, entity, seed) -> entity != null && entity.getUseItem() == stack
                    ? (float) (entity.getUseItemRemainingTicks() % 10) / 10.0F : 0.0F)
            );
        }
    }
}
