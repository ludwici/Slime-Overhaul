package com.ludwici.slimeoverhaul.datagen;

import com.ludwici.crumbslib.api.CrumbSupplier;
import com.ludwici.crumbslib.api.EntityHelper;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.SlimePredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.Stream;

import static com.ludwici.slimeoverhaul.Content.*;


public class ModEntityLootTableProvider extends EntityLootSubProvider {
    protected ModEntityLootTableProvider(HolderLookup.Provider arg2) {
        super(FeatureFlags.DEFAULT_FLAGS, arg2);
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return EntityHelper.ENTITY_TYPES.getEntries().stream().map(e -> (EntityType<?>) e.value());
    }

    @Override
    public void generate() {
        slimeLoot(AIR_SLIME, AIR_SLIME_BALL);
        slimeLoot(WATER_SLIME, WATER_SLIME_BALL);
        slimeLoot(EARTH_SLIME, EARTH_SLIME_BALL);
        slimeLoot(FLAME_SLIME, FIRE_SLIME_BALL);
    }

    private <T extends Entity> void slimeLoot(CrumbSupplier<EntityType<T>> entity, CrumbSupplier<Item> loot) {
        add(entity.get(), LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        LootItem.lootTableItem(loot.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0.0F, 1.0f)))
                                )
                                .when(
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder.entity().subPredicate(SlimePredicate.sized(MinMaxBounds.Ints.exactly(1)))
                                        )
                                )
                ));
    }
}
