package com.ludwici.slimeoverhaul.datagen;

import com.ludwici.slimeoverhaul.datamap.UpgradeCost;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

import static com.ludwici.slimeoverhaul.Content.*;

public class ModDataMapProvider extends DataMapProvider {
    public ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        super.gather(provider);
        builder(UPGRADE_COST_DATA)
                .add(FIRE_TEMPLATE.getHolder(), new UpgradeCost(FIRE_SLIME_BALL.get().getDefaultInstance()), false)
        ;
    }
}
