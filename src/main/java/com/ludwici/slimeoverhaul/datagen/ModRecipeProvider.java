package com.ludwici.slimeoverhaul.datagen;

import com.ludwici.crumbslib.api.CrumbSupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.*;
import static com.ludwici.slimeoverhaul.Content.*;


public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(arg, completableFuture);
    }

    @Override
    protected void buildRecipes(RecipeOutput arg) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LEAD, 2)
                .pattern("~~ ")
                .pattern("~0 ")
                .pattern("  ~")
                .define('~', Items.STRING)
                .define('0', Tags.Items.SLIME_BALLS)
                .unlockedBy("has_slime_balls", has(Tags.Items.SLIME_BALLS))
                .save(arg, ResourceLocation.fromNamespaceAndPath(MODID, "lead_from_slime_balls"));

        slimeBlockRecipe(AIR_SLIME_BLOCK, AIR_SLIME_BALL, arg);
        slimeBlockRecipe(WATER_SLIME_BLOCK, WATER_SLIME_BALL, arg);
        slimeBlockRecipe(EARTH_SLIME_BLOCK, EARTH_SLIME_BALL, arg);
        slimeBlockRecipe(FIRE_SLIME_BLOCK, FIRE_SLIME_BALL, arg);

        slimeBallRecipe(AIR_SLIME_BALL, AIR_SLIME_BLOCK, arg);
        slimeBallRecipe(WATER_SLIME_BALL, WATER_SLIME_BLOCK, arg);
        slimeBallRecipe(EARTH_SLIME_BALL, EARTH_SLIME_BLOCK, arg);
        slimeBallRecipe(FIRE_SLIME_BALL, FIRE_SLIME_BLOCK, arg);

        bannerPatternRecipe(PATTERN_AIR_SLIME_SIGN, AIR_SLIME_BLOCK, arg);
        bannerPatternRecipe(PATTERN_WATER_SLIME_SIGN, WATER_SLIME_BLOCK, arg);
        bannerPatternRecipe(PATTERN_EARTH_SLIME_SIGN, EARTH_SLIME_BLOCK, arg);
        bannerPatternRecipe(PATTERN_FIRE_SLIME_SIGN, FIRE_SLIME_BLOCK, arg);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CLEANSING_BRUSH.get(), 1)
                .pattern("   ")
                .pattern("XYZ")
                .pattern("   ")
                .define('X', Items.LAPIS_LAZULI)
                .define('Y', Items.BRUSH)
                .define('Z', Items.AMETHYST_SHARD)
                .unlockedBy(getHasName(Items.BRUSH), has(Items.BRUSH))
                .save(arg);
        ;
    }

    private void slimeBlockRecipe(CrumbSupplier<Block> result, CrumbSupplier<Item> from, RecipeOutput arg) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, result.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', from.get())
                .unlockedBy(getHasName(from.get()), has(from.get()))
                .save(arg);
    }

    private void slimeBallRecipe(CrumbSupplier<Item> result, CrumbSupplier<Block> from, RecipeOutput arg) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get(), 9)
                .requires(from.get())
                .unlockedBy(getHasName(from.get()), has(from.get()))
                .save(arg);
    }

    private void bannerPatternRecipe(CrumbSupplier<BannerPatternItem> bannerPattern, CrumbSupplier<Block> from, RecipeOutput arg) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, bannerPattern.get(), 1)
                .requires(from.get())
                .requires(Items.PAPER)
                .unlockedBy("has_" + BuiltInRegistries.BLOCK.getKey(from.get()).getPath(), has(from.get()))
                .save(arg);
    }
}
