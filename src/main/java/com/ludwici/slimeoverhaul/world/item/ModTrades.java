package com.ludwici.slimeoverhaul.world.item;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.List;
import java.util.Optional;

import static com.ludwici.slimeoverhaul.Content.PATTERN_SLIME;
import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class ModTrades {

    public static final ResourceKey<VillagerTrade> CARTOGRAPHER_2_EMERALD_PATTERN_SLIME = registerKey("cartographer/2/emerald_pattern_slime");

    private static ResourceKey<VillagerTrade> registerKey(String name) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, Identifier.fromNamespaceAndPath(MODID, name));
    }

    public static void bootstrap(BootstrapContext<VillagerTrade> context) {
        register(context, CARTOGRAPHER_2_EMERALD_PATTERN_SLIME, new VillagerTrade(new TradeCost(Items.EMERALD, 8), new ItemStackTemplate(PATTERN_SLIME.get(), 1), 12, 3, 0.05F, Optional.empty(), List.of()));
    }

    public static Holder.Reference<VillagerTrade> register(BootstrapContext<VillagerTrade> context, ResourceKey<VillagerTrade> resourceKey, VillagerTrade villagerTrade) {
        return context.register(resourceKey, villagerTrade);
    }
}
