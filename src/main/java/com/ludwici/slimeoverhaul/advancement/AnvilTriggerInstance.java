package com.ludwici.slimeoverhaul.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;

import java.util.Optional;

public record AnvilTriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
    public static final Codec<AnvilTriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(AnvilTriggerInstance::player)
    ).apply(instance, AnvilTriggerInstance::new));

    public boolean matches() {
        return true;
    }
}
