package com.ludwici.slimeoverhaul.compat.emi;

import com.ludwici.slimeoverhaul.Content;
import com.ludwici.slimeoverhaul.data.PyrocideLevel;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

import static com.ludwici.slimeoverhaul.Content.*;
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

        registry.addCategory(VanillaEmiRecipeCategories.SMITHING);
        registry.addWorkstation(VanillaEmiRecipeCategories.SMITHING, EmiStack.of(Blocks.SMITHING_TABLE));

        SmithingTableEmiRecipe recipe = new SmithingTableEmiRecipe(
                EmiStack.of(FIRE_TEMPLATE.get()),
                EmiIngredient.of(PYROCIDE_UPGRADABLE_ITEMS_TAG),
                EmiIngredient.of(PYROCIDE_UPGRADE_MATERIALS_TAG),
                ResourceLocation.fromNamespaceAndPath(MODID, "/pyrocide_upgrade")
        );
        registry.addRecipe(recipe);
    }
}
