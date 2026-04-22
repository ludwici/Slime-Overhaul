package com.ludwici.slimeoverhaul.compat.emi;

import com.ludwici.slimeoverhaul.data.PyrocideLevel;
import com.ludwici.slimeoverhaul.datamap.UpgradeCost;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Random;

import static com.ludwici.slimeoverhaul.Content.PYROCIDE_LEVEL;
import static com.ludwici.slimeoverhaul.Content.UPGRADE_COST_DATA;

public class SmithingTableEmiRecipe extends BasicEmiRecipe {
    private final Random RANDOM = new Random();
    private final int uniq = RANDOM.nextInt();

    public SmithingTableEmiRecipe(EmiIngredient template, EmiIngredient input, EmiIngredient addition, ResourceLocation id) {
        super(VanillaEmiRecipeCategories.SMITHING, id, 112, 18);
        inputs.add(template);
        inputs.add(input);
        outputs.add(EmiStack.of(Items.IRON_SWORD, DataComponentPatch.builder().set(PYROCIDE_LEVEL.get(), new PyrocideLevel()).build()));
        catalysts.add(addition);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 62, 1);

        ItemStack template = inputs.getFirst().getEmiStacks().getFirst().getItemStack();
        Holder<Item> holder = template.getItemHolder();
        UpgradeCost data = holder.getData(UPGRADE_COST_DATA);
        if (data == null) {
            return;
        }

        widgets.addSlot(inputs.getFirst(), 0, 0);
        widgets.addGeneratedSlot(random -> getStack(random, 0), uniq, 18, 0);
        widgets.addSlot(EmiStack.of(data.itemStack()), 36, 0);
        widgets.addGeneratedSlot(random -> getStack(random, 1), uniq, 94, 0).recipeContext(this);
    }

    private EmiStack getStack(Random r, int i) {
        List<EmiStack> inputItems = inputs.get(1).getEmiStacks();
        int size = inputItems.size();
        int ran = r.nextInt(size);
        EmiStack input = inputItems.get(ran);

        var s = input.getItemStack().copy();
        s.set(PYROCIDE_LEVEL.get(), new PyrocideLevel());
        return new EmiStack[] {
                input,
                EmiStack.of(s)
        }[i];
    }
}
