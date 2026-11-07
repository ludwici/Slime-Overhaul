package com.ludwici.slimeoverhaul;

import com.ludwici.crumbslib.api.*;
import com.ludwici.crumbslib.api.world.feature.FeatureHelper;
import com.ludwici.slimeoverhaul.block.FireThickenerCauldronBlock;
import com.ludwici.slimeoverhaul.config.Config;
import com.ludwici.slimeoverhaul.entity.custom.BaseSlime;
import com.ludwici.slimeoverhaul.entity.custom.elementals.EarthSlime;
import com.ludwici.slimeoverhaul.event.SlimyBlockExecute;
import com.ludwici.slimeoverhaul.event.GameplayEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.GenerationStep;

import static com.ludwici.slimeoverhaul.Content.*;

public final class SlimeOverhaulMod implements ModInitializer {
    public static final String MODID = "slimeoverhaul";

    @Override
    public void onInitialize() {
        ModHelper.init(MODID);
        Config.load();

        DataComponentsHelper.initBus();
        EntityHelper.initBus();
        ItemHelper.initBus();
        BlockHelper.initBus();
        PotionHelper.initBus();
        MobEffectHelper.initBus();
        CreativeTabHelper.initBus();
        StructureHelper.initBus();
        BlockEntityHelper.initBus();
        FeatureHelper.initBus();

        Content.init();
        Content.spawns();
        GameplayEvents.init();

        EventHelper.addAttributeEvent(attributes -> {
            attributes.put(AIR_SLIME.get(), BaseSlime.createMobAttributes().build());
            attributes.put(WATER_SLIME.get(), BaseSlime.createMobAttributes().build());
            attributes.put(EARTH_SLIME.get(), EarthSlime.createMobAttributes().build());
            attributes.put(FLAME_SLIME.get(), BaseSlime.createMobAttributes().build());
        });

        EventHelper.addBrewingRecipeEvent(Content::registerPotions);
        EventHelper.addAnvilUpdateEvent(Content::registerAnvilEvent);

        FireThickenerCauldronBlock.THICKENER.map().put(PYROCIDE_DUST.get(), FireThickenerCauldronBlock.FILL_PYDOCIDE_DUST);
        CauldronInteraction.EMPTY.map().put(FIRE_SLIME_BALL.get(), FireThickenerCauldronBlock.FILL_FIRE_SLIME);

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 3, trades -> trades.add((trader, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, 8), new ItemStack(PATTERN_SLIME.get(), 1), 12, 3, 0.05f)));

        BiomeModifications.addFeature(context -> context.hasTag(BiomeTags.IS_OVERWORLD), GenerationStep.Decoration.LOCAL_MODIFICATIONS, com.ludwici.slimeoverhaul.world.ModPacedFeatures.PYROCIDE_GEODE_PLACED_KEY);
    }

    public static void onSlimyBlockExecute(SlimyBlockExecute event) {
        var blockEntity = event.getBlockEntity();
        var block = event.getBlock();
        var player = event.getPlayer();
        var behaviour = block.blockBehaviourList.get(player.level().random.nextInt(block.blockBehaviourList.size()));

        if (block.canApplyEffect(player, behaviour)) {
            behaviour.applyEffect(player, blockEntity);
        } else {
            event.setCanceled(true);
        }
    }
}
