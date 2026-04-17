package com.ludwici.slimeoverhaul.datagen;

import com.ludwici.crumbslib.api.BlockHelper;
import com.ludwici.crumbslib.api.CrumbSupplier;
import com.ludwici.slimeoverhaul.block.SlimeCoatBlock;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

import static com.ludwici.slimeoverhaul.Content.*;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider arg2) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), arg2);
    }

    @Override
    protected void generate() {
        dropSelf(AIR_SLIME_BLOCK.get());
        dropSelf(WATER_SLIME_BLOCK.get());
        dropSelf(EARTH_SLIME_BLOCK.get());
        dropSelf(FIRE_SLIME_BLOCK.get());

        dropSelf(ANCIENT_AIR_SLIMY_BLOCK.get());
        dropSelf(ANCIENT_WATER_SLIMY_BLOCK.get());
        dropSelf(ANCIENT_EARTH_SLIMY_BLOCK.get());
        dropSelf(ANCIENT_FIRE_SLIMY_BLOCK.get());

        slimeCoat(AIR_SLIME_COAT);
        slimeCoat(WATER_SLIME_COAT);
        slimeCoat(EARTH_SLIME_COAT);
        slimeCoat(FIRE_SLIME_COAT);

        dropOther(AIR_CRYSTALLIZED_SLIME_BLOCK, AIR_CRYSTALLIZED_DUST);
        dropOther(WATER_CRYSTALLIZED_SLIME_BLOCK, WATER_CRYSTALLIZED_DUST);
        dropOther(EARTH_CRYSTALLIZED_SLIME_BLOCK, EARTH_CRYSTALLIZED_DUST);

        createCrystallizedSlimeDrops(SMALL_PYROCIDE_BLOCK, PYROCIDE_DUST, ConstantValue.exactly(1));
        createCrystallizedSlimeDrops(MEDIUM_PYROCIDE_BLOCK, PYROCIDE_DUST, UniformGenerator.between(2, 3));
        createCrystallizedSlimeDrops(LARGE_PYROCIDE_BLOCK, PYROCIDE_DUST, UniformGenerator.between(3, 5));

        dropOther(FIRE_THICKENER_CAULDRON_BLOCK.get(), Blocks.CAULDRON);
    }

    public void dropOther(CrumbSupplier<Block> from, CrumbSupplier<Item> loot) {
        dropOther(from.get(), loot.get());
    }

    public void createCrystallizedSlimeDrops(CrumbSupplier<Block> from, CrumbSupplier<Item> loot, NumberProvider countValue) {
        createCrystallizedSlimeDrops(from.get(), loot.get(), countValue);
    }

    protected void createCrystallizedSlimeDrops(Block block, Item drop, NumberProvider countValue) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        add(block, this.createSilkTouchDispatchTable(
                block,
                this.applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(drop)
                                .apply(SetItemCountFunction.setCount(countValue))
                                .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
                )
        ));
    }

//    public void crystallizedSlime(Block from, Item loot) {
//        this.add(from, lt -> LootTable.lootTable()
//                .withPool(LootPool.lootPool()
//                        .add(AlternativesEntry.alternatives(
//                                AlternativesEntry.alternatives(
//                                        CrystallizedSlimeBlock.STAGE.getPossibleValues(),
//                                        value -> LootItem.lootTableItem(loot).when(
//                                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(from)
//                                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CrystallizedSlimeBlock.STAGE, value))
//                                        )
//                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, value)))
//                                )
//                        ))
//                )
//        );
//    }

//    public void crystallizedSlime(CrumbSupplier<Block> from, CrumbSupplier<Item> loot) {
//        crystallizedSlime(from.get(), loot.get());
//    }

    public void slimeCoat(Block block, Item item) {
        this.add(block, lt -> LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(AlternativesEntry.alternatives(
                                AlternativesEntry.alternatives(
                                        SlimeCoatBlock.FACE_COUNT.getPossibleValues(),
                                        value -> LootItem.lootTableItem(item)
                                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlimeCoatBlock.FACE_COUNT, value))
                                                )
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(value)))

                                )
                        ))
                )
        );
    }

    public void slimeCoat(Block block) {
        slimeCoat(block, block.asItem());
    }

    public <T extends Block> void slimeCoat(CrumbSupplier<T> block) {
        slimeCoat(block.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockHelper.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
