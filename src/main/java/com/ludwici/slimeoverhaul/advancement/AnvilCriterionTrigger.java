package com.ludwici.slimeoverhaul.advancement;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

public class AnvilCriterionTrigger extends SimpleCriterionTrigger<AnvilTriggerInstance> {

    public void trigger(ServerPlayer player) {
        this.trigger(player, AnvilTriggerInstance::matches);
    }

    @Override
    public Codec<AnvilTriggerInstance> codec() {
        return AnvilTriggerInstance.CODEC;
    }
}
