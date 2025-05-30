package com.ludwici.slimeoverhaul.datagen;

import com.ludwici.crumbslib.api.CrumbSupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

import static com.ludwici.slimeoverhaul.Content.*;
import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

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

        basicItem(AIR_SLIME_BALL.get());
        basicItem(WATER_SLIME_BALL.get());
        basicItem(EARTH_SLIME_BALL.get());
        basicItem(FLAME_SLIME_BALL.get());

        basicItem(WATER_SLIME_BUCKET.get());
        basicItem(FLAME_SLIME_BUCKET.get());

        bannerPatternItem(PATTERN_SLIME);
        bannerPatternItem(PATTERN_AIR_SLIME_SIGN);
        bannerPatternItem(PATTERN_WATER_SLIME_SIGN);
        bannerPatternItem(PATTERN_EARTH_SLIME_SIGN);
        bannerPatternItem(PATTERN_FIRE_SLIME_SIGN);
    }

    public ItemModelBuilder spawnEggItem(Item item) {
        return spawnEggItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    public ItemModelBuilder spawnEggItem(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }

    private <T extends Item> void bannerPatternItem(CrumbSupplier<T> item) {
        withExistingParent(getName(item), mcLoc("item/creeper_banner_pattern"));
    }

    private <T extends Item> String getName(CrumbSupplier<T> item) {
        return item.getId().getPath();
    }
}
