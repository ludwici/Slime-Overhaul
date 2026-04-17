package com.ludwici.slimeoverhaul.datagen;

import com.ludwici.crumbslib.api.CrumbSupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
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

        basicItem(ANCIENT_AIR_FRAGMENTS);
        basicItem(ANCIENT_WATER_FRAGMENTS);
        basicItem(ANCIENT_EARTH_FRAGMENTS);
        basicItem(ANCIENT_FIRE_FRAGMENTS);

        fromBlock(AIR_SLIME_COAT.get());
        fromBlock(WATER_SLIME_COAT.get());
        fromBlock(EARTH_SLIME_COAT.get());
        fromBlock(FIRE_SLIME_COAT.get());

        bannerPatternItem(PATTERN_SLIME);
        bannerPatternItem(PATTERN_AIR_SLIME_SIGN);
        bannerPatternItem(PATTERN_WATER_SLIME_SIGN);
        bannerPatternItem(PATTERN_EARTH_SLIME_SIGN);
        bannerPatternItem(PATTERN_FIRE_SLIME_SIGN);

        basicItem(AIR_CRYSTALLIZED_DUST);
        basicItem(WATER_CRYSTALLIZED_DUST);
        basicItem(EARTH_CRYSTALLIZED_DUST);
        basicItem(PYROCIDE_DUST);

        basicItem(BLANK_FIRE_TEMPLATE);
        basicItem(FIRE_TEMPLATE);
    }

    private <T extends Item> void bannerPatternItem(CrumbSupplier<T> item) {
        withExistingParent(getName(item), mcLoc("item/creeper_banner_pattern"));
    }

    public <I extends Item> void basicItem(CrumbSupplier<I> item) {
        basicItem(item.get());
    }

    public void fromBlock(Block block) {
        ResourceLocation res = BuiltInRegistries.ITEM.getKey(block.asItem());
        getBuilder(block.asItem().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(res.getNamespace(), "block/" + res.getPath()));

    }

    public void fromBlock(CrumbSupplier<Block> block) {
        fromBlock(block.get());
    }

    private <T extends Item> String getName(CrumbSupplier<T> item) {
        return item.getId().getPath();
    }

}
