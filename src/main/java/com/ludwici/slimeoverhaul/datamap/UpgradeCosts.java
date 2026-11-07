package com.ludwici.slimeoverhaul.datamap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static com.ludwici.slimeoverhaul.Content.FIRE_SLIME_BALL;
import static com.ludwici.slimeoverhaul.Content.FIRE_TEMPLATE;
import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public final class UpgradeCosts extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
    private static volatile Map<ResourceLocation, UpgradeCost> costs = Map.of();

    public UpgradeCosts() {
        super(new Gson(), "slimeoverhaul/upgrade_costs");
    }

    @Override
    public ResourceLocation getFabricId() {
        return ResourceLocation.fromNamespaceAndPath(MODID, "upgrade_costs");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, ProfilerFiller profiler) {
        Map<ResourceLocation, UpgradeCost> parsed = new HashMap<>();
        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            JsonObject source = entry.getValue().getAsJsonObject().getAsJsonObject("itemStack");
            if (source == null || !source.has("id")) {
                throw new IllegalArgumentException("Missing itemStack.id in upgrade cost " + entry.getKey());
            }

            ResourceLocation id = ResourceLocation.parse(source.get("id").getAsString());
            Item item = BuiltInRegistries.ITEM.get(id);
            if (!BuiltInRegistries.ITEM.containsKey(id)) {
                throw new IllegalArgumentException("Unknown upgrade material " + id);
            }
            int count = source.has("count") ? source.get("count").getAsInt() : 1;
            if (count < 1 || count > 99) {
                throw new IllegalArgumentException("Invalid upgrade material count for " + entry.getKey());
            }
            parsed.put(entry.getKey(), new UpgradeCost(new ItemStack(item, count)));
        }
        costs = Map.copyOf(parsed);
    }

    public static UpgradeCost get(ItemStack template) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(template.getItem());
        UpgradeCost configured = costs.get(id);
        if (configured != null) {
            return configured;
        }
        if (template.is(FIRE_TEMPLATE.get())) {
            return new UpgradeCost(new ItemStack(FIRE_SLIME_BALL.get()));
        }
        return null;
    }
}
