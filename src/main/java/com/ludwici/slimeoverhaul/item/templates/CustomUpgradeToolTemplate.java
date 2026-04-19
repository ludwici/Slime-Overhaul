package com.ludwici.slimeoverhaul.item.templates;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class CustomUpgradeToolTemplate extends SmithingTemplateItem {
    private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;

    private static final ResourceLocation EMPTY_SLOT_PICKAXE = ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe");
    private static final ResourceLocation EMPTY_SLOT_SWORD = ResourceLocation.withDefaultNamespace("item/empty_slot_sword");
    private static final ResourceLocation EMPTY_SLOT_AXE = ResourceLocation.withDefaultNamespace("item/empty_slot_axe");
    private static final ResourceLocation EMPTY_SLOT_HOE = ResourceLocation.withDefaultNamespace("item/empty_slot_hoe");
    private static final ResourceLocation EMPTY_SLOT_SHOVEL = ResourceLocation.withDefaultNamespace("item/empty_slot_shovel");

    private static final ResourceLocation EMPTY_SLOT_INGOT = ResourceLocation.withDefaultNamespace("item/empty_slot_ingot");

    public CustomUpgradeToolTemplate(String id, List<String> upgradeItems) {
        super(
                getAppliesTo(id),
                getIngredients(id),
                getUpgradeDescription(id),
                getBaseSlotDescription(id),
                getAdditionsSlotDescription(id),
                createUpgradeIconList(upgradeItems),
                createUpgradeMaterialList()
        );
    }

    private static List<ResourceLocation> createUpgradeIconList(List<String> upgradeItems) {
//        List<ResourceLocation> ret = upgradeItems.stream().map(s -> ResourceLocation.withDefaultNamespace("item/empty_slot_" + s)).toList();
        return upgradeItems.stream().map(s -> ResourceLocation.withDefaultNamespace("item/empty_slot_" + s)).toList();
//        return List.of(
//                EMPTY_SLOT_SWORD,
//                EMPTY_SLOT_PICKAXE,
//                EMPTY_SLOT_AXE,
//                EMPTY_SLOT_HOE,
//                EMPTY_SLOT_SHOVEL
//        );
    }

    private static List<ResourceLocation> createUpgradeMaterialList() {
        return List.of(EMPTY_SLOT_INGOT);
    }

    private static Component getUpgradeDescription(String path) {
        return Component.translatable(
                Util.makeDescriptionId("upgrade", ResourceLocation.fromNamespaceAndPath(MODID, path))
        ).withStyle(TITLE_FORMAT);
    }

    private static Component getAppliesTo(String path) {
        return Component.translatable(
                Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(MODID, path + ".applies_to"))
        ).withStyle(DESCRIPTION_FORMAT);
    }

    private static Component getIngredients(String path) {
        return Component.translatable(
                Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(MODID, path + ".ingredients"))
        ).withStyle(DESCRIPTION_FORMAT);
    }

    private static Component getBaseSlotDescription(String path) {
        return Component.translatable(
                Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(MODID, path + ".base_slot_description"))
        );
    }

    private static Component getAdditionsSlotDescription(String path) {
        return Component.translatable(
                Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(MODID, path + ".additions_slot_description"))
        );
    }
}
