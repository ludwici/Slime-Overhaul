package com.ludwici.slimeoverhaul.datagen;

import com.ludwici.slimeoverhaul.world.item.ModTrades;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

@EventBusSubscriber(modid = MODID)
public class DataGenerators {
    
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.VILLAGER_TRADE, ModTrades::bootstrap)
            ;

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        event.createDatapackRegistryObjects(BUILDER);

        event.createProvider(ModModelProvider::new);
        event.createProvider(((output, lookupProvider) -> new LootTableProvider(
            output, Set.of(), List.of(
                    new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK),
                    new LootTableProvider.SubProviderEntry(ModEntityLootTableProvider::new, LootContextParamSets.ENTITY)
        ),
            lookupProvider
        )));

        event.createProvider(ModRecipeProvider.Runner::new);
        event.createProvider(ModBlockTagProvider::new);
        event.createProvider(ModItemTagProvider::new);
        event.createProvider(ModEntityTagProvider::new);
        event.createProvider(ModBiomeTagProvider::new);
        event.createProvider(ModVillageTradesTagProvider::new);
    }

//    @SubscribeEvent
//    public static void gatherServerData(GatherDataEvent.Server event) {
//        DataGenerator generator = event.getGenerator();
//        PackOutput packOutput = generator.getPackOutput();
////        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
//        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
//
////        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupProvider));
//
////        BlockTagsProvider blockTagsProvider = new ModBlockTagProvider(packOutput, lookupProvider, existingFileHelper);
////        generator.addProvider(event.includeServer(), blockTagsProvider);
////        generator.addProvider(event.includeServer(), new ModItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
////        generator.addProvider(event.includeServer(), new ModEntityTagProvider(packOutput, lookupProvider, existingFileHelper));
////        generator.addProvider(event.includeServer(), new ModBiomeTagProvider(packOutput, lookupProvider, existingFileHelper));
////        generator.addProvider(event.includeServer(), new ModWorldGenProvider(packOutput, lookupProvider));
//
//    }
}
