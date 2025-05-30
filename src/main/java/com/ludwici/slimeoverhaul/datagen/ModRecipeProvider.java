package com.ludwici.slimeoverhaul.datagen;

import com.ludwici.crumbslib.api.CrumbSupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

import static com.ludwici.slimeoverhaul.Content.*;
import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LEAD, 2)
                .pattern("~~ ")
                .pattern("~0 ")
                .pattern("  ~")
                .define('~', Items.STRING)
                .define('0', Tags.Items.SLIMEBALLS)
                .unlockedBy("has_slime_balls", has(Tags.Items.SLIMEBALLS))
                .save(pWriter, ResourceLocation.fromNamespaceAndPath(MODID, "lead_from_slime_balls"));

        slimeBlockRecipe(AIR_SLIME_BLOCK, AIR_SLIME_BALL, pWriter);
        slimeBlockRecipe(WATER_SLIME_BLOCK, WATER_SLIME_BALL, pWriter);
        slimeBlockRecipe(EARTH_SLIME_BLOCK, EARTH_SLIME_BALL, pWriter);
        slimeBlockRecipe(FLAME_SLIME_BLOCK, FLAME_SLIME_BALL, pWriter);

        slimeBallRecipe(AIR_SLIME_BALL, AIR_SLIME_BLOCK, pWriter);
        slimeBallRecipe(WATER_SLIME_BALL, WATER_SLIME_BLOCK, pWriter);
        slimeBallRecipe(EARTH_SLIME_BALL, EARTH_SLIME_BLOCK, pWriter);
        slimeBallRecipe(FLAME_SLIME_BALL, FLAME_SLIME_BLOCK, pWriter);

        bannerPatternRecipe(PATTERN_AIR_SLIME_SIGN, AIR_SLIME_BLOCK, pWriter);
        bannerPatternRecipe(PATTERN_WATER_SLIME_SIGN, WATER_SLIME_BLOCK, pWriter);
        bannerPatternRecipe(PATTERN_EARTH_SLIME_SIGN, EARTH_SLIME_BLOCK, pWriter);
        bannerPatternRecipe(PATTERN_FIRE_SLIME_SIGN, FLAME_SLIME_BLOCK, pWriter);
    }

    private void slimeBlockRecipe(CrumbSupplier<Block> result, CrumbSupplier<Item> from, Consumer<FinishedRecipe> arg) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, result.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', from.get())
                .unlockedBy(getHasName(from.get()), has(from.get()))
                .save(arg);
    }

    private void slimeBallRecipe(CrumbSupplier<Item> result, CrumbSupplier<Block> from, Consumer<FinishedRecipe> arg) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get(), 9)
                .requires(from.get())
                .unlockedBy(getHasName(from.get()), has(from.get()))
                .save(arg);
    }

    private void bannerPatternRecipe(CrumbSupplier<BannerPatternItem> bannerPattern, CrumbSupplier<Block> from, Consumer<FinishedRecipe> arg) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, bannerPattern.get(), 1)
                .requires(from.get())
                .requires(Items.PAPER)
                .unlockedBy("has_" + BuiltInRegistries.BLOCK.getKey(from.get()).getPath(), has(from.get()))
                .save(arg);
    }
}
