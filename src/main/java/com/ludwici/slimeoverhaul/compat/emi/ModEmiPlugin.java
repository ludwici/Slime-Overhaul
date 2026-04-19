package com.ludwici.slimeoverhaul.compat.emi;

import com.ludwici.slimeoverhaul.Content;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

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

        List<ItemStack> shields = BuiltInRegistries.ITEM.stream().map(ItemStack::new).filter(Content::isShieldItem).toList();
        shields.forEach(s -> {
            ItemStack r = new ItemStack(s.getItem());
            CompoundTag tag = new CompoundTag();
            tag.putInt("bounce", 32);
            r.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

            AnvilEmiRecipe recipe = new AnvilEmiRecipe(
                    EmiStack.of(s),
                    EmiStack.of(EARTH_SLIME_BALL.get()),
                    EmiStack.of(r),
                    ResourceLocation.fromNamespaceAndPath(MODID, "/earth_power_shield")
            );

            registry.addRecipe(recipe);
        });

        registry.removeRecipes(emiRecipe -> emiRecipe.getId().getPath().endsWith("_fake_recipe"));
    }
}
