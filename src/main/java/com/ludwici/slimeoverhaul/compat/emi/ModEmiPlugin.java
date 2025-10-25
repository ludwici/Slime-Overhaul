package com.ludwici.slimeoverhaul.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.common.Tags;

import static com.ludwici.slimeoverhaul.Content.EARTH_SLIME_BALL;
import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

@EmiEntrypoint
public class ModEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(VanillaEmiRecipeCategories.ANVIL_REPAIRING);
        registry.addWorkstation(VanillaEmiRecipeCategories.ANVIL_REPAIRING, EmiStack.of(Items.ANVIL));
        registry.addWorkstation(VanillaEmiRecipeCategories.ANVIL_REPAIRING, EmiStack.of(Items.CHIPPED_ANVIL));
        registry.addWorkstation(VanillaEmiRecipeCategories.ANVIL_REPAIRING, EmiStack.of(Items.DAMAGED_ANVIL));

        ItemStack result = new ItemStack(Items.SHIELD);

        CompoundTag tag = new CompoundTag();
        tag.putInt("bounce", 32);
        result.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));


        AnvilEmiRecipe recipe = new AnvilEmiRecipe(
                EmiIngredient.of(Tags.Items.TOOLS_SHIELD),
                EmiStack.of(EARTH_SLIME_BALL.get()),
                EmiStack.of(result),
                ResourceLocation.fromNamespaceAndPath(MODID, "/earth_power_shield")
        );

        registry.addRecipe(recipe);
    }
}
