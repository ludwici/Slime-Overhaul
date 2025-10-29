package com.ludwici.slimeoverhaul.datagen;

import com.ludwici.crumbslib.api.CrumbSupplier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.*;
import static com.ludwici.slimeoverhaul.Content.*;


public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        spawnEggItem(AIR_SLIME_EGG.get());
        spawnEggItem(WATER_SLIME_EGG.get());
        spawnEggItem(EARTH_SLIME_EGG.get());
        spawnEggItem(FLAME_SLIME_EGG.get());

        basicItem(AIR_SLIME_BALL);
        basicItem(WATER_SLIME_BALL);
        basicItem(EARTH_SLIME_BALL);
        basicItem(FIRE_SLIME_BALL);

        basicItem(WATER_SLIME_BUCKET);
        basicItem(FIRE_SLIME_BUCKET);

        basicItem(ANCIENT_FIRE_FRAGMENTS);

        bannerPatternItem(PATTERN_SLIME);
        bannerPatternItem(PATTERN_AIR_SLIME_SIGN);
        bannerPatternItem(PATTERN_WATER_SLIME_SIGN);
        bannerPatternItem(PATTERN_EARTH_SLIME_SIGN);
        bannerPatternItem(PATTERN_FIRE_SLIME_SIGN);
    }

    private <T extends Item> void bannerPatternItem(CrumbSupplier<T> item) {
        withExistingParent(getName(item), mcLoc("item/creeper_banner_pattern"));
    }

    public <I extends Item> void basicItem(CrumbSupplier<I> item) {
        basicItem(item.get());
    }

    private <T extends Item> String getName(CrumbSupplier<T> item) {
        return item.getId().getPath();
    }

}
