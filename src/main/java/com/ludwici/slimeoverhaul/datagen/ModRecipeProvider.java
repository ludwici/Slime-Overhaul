package com.ludwici.slimeoverhaul.datagen;

import com.ludwici.crumbslib.api.CrumbSupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
//import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
//import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.*;
import static com.ludwici.slimeoverhaul.Content.*;


public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.MISC, Items.LEAD, 2)
                .pattern("~~ ")
                .pattern("~0 ")
                .pattern("  ~")
                .define('~', Items.STRING)
                .define('0', Tags.Items.SLIME_BALLS)
                .unlockedBy("has_slime_balls", has(Tags.Items.SLIME_BALLS))
                .save(output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(MODID, "lead_from_slime_balls")));

        slimeBlockRecipe(AIR_SLIME_BLOCK, AIR_SLIME_BALL);
        slimeBlockRecipe(WATER_SLIME_BLOCK, WATER_SLIME_BALL);
        slimeBlockRecipe(EARTH_SLIME_BLOCK, EARTH_SLIME_BALL);
        slimeBlockRecipe(FIRE_SLIME_BLOCK, FIRE_SLIME_BALL);

        slimeBallRecipe(AIR_SLIME_BALL, AIR_SLIME_BLOCK);
        slimeBallRecipe(WATER_SLIME_BALL, WATER_SLIME_BLOCK);
        slimeBallRecipe(EARTH_SLIME_BALL, EARTH_SLIME_BLOCK);
        slimeBallRecipe(FIRE_SLIME_BALL, FIRE_SLIME_BLOCK);

//        bannerPatternRecipe(PATTERN_AIR_SLIME_SIGN, AIR_SLIME_BLOCK, arg);
//        bannerPatternRecipe(PATTERN_WATER_SLIME_SIGN, WATER_SLIME_BLOCK, arg);
//        bannerPatternRecipe(PATTERN_EARTH_SLIME_SIGN, EARTH_SLIME_BLOCK, arg);
//        bannerPatternRecipe(PATTERN_FIRE_SLIME_SIGN, FIRE_SLIME_BLOCK, arg);

//        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CLEANSING_BRUSH.get(), 1)
//                .pattern("   ")
//                .pattern("XYZ")
//                .pattern("   ")
//                .define('X', Items.LAPIS_LAZULI)
//                .define('Y', Items.BRUSH)
//                .define('Z', Items.AMETHYST_SHARD)
//                .unlockedBy(getHasName(Items.BRUSH), has(Items.BRUSH))
//                .save(arg)
//        ;
    }

    private void slimeBlockRecipe(CrumbSupplier<Block> result, CrumbSupplier<Item> from) {
        shaped(RecipeCategory.REDSTONE, result.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', from.get())
                .unlockedBy(getHasName(from.get()), has(from.get()))
                .save(output);
    }

    private void slimeBallRecipe(CrumbSupplier<Item> result, CrumbSupplier<Block> from) {
        shapeless(RecipeCategory.MISC, result.get(), 9)
                .requires(from.get())
                .unlockedBy(getHasName(from.get()), has(from.get()))
                .save(output);
    }

//    private void bannerPatternRecipe(CrumbSupplier<BannerPatternItem> bannerPattern, CrumbSupplier<Block> from, RecipeOutput arg) {
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, bannerPattern.get(), 1)
//                .requires(from.get())
//                .requires(Items.PAPER)
//                .unlockedBy("has_" + BuiltInRegistries.BLOCK.getKey(from.get()).getPath(), has(from.get()))
//                .save(arg);
//    }

    public static class Runner extends RecipeProvider.Runner {
        protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes : " + MODID;
        }
    }
}
