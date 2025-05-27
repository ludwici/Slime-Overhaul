package com.ludwici.slimeoverhaul.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

import static com.ludwici.slimeoverhaul.Content.EARTH_SLIME_BALL;
import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(MODID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory vanillaRecipeFactory = registration.getVanillaRecipeFactory();

        ItemStack result = new ItemStack(Items.SHIELD);
        CompoundTag tag = new CompoundTag();
        tag.putInt("bounce", 32);
        result.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

        IJeiAnvilRecipe recipe = vanillaRecipeFactory.createAnvilRecipe(new ItemStack(Items.SHIELD), List.of(new ItemStack(EARTH_SLIME_BALL.getHolder())), List.of(result), ResourceLocation.fromNamespaceAndPath(MODID, ""));
        registration.addRecipes(RecipeTypes.ANVIL, List.of(recipe));
    }
}
