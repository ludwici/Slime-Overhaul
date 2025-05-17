package com.ludwici.slimeoverhaul.entity.custom.elementals;

import com.ludwici.slimeoverhaul.entity.custom.BaseSlime;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

public class ThunderSlime extends BaseSlime {
    public ThunderSlime(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public String getSlimeType() {
        return "thunder";
    }

    @Override
    public ParticleOptions getParticleType() {
        return ParticleTypes.ELECTRIC_SPARK;
    }
}
