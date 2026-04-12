package com.ludwici.slimeoverhaul.entity.custom.elementals;

import com.ludwici.slimeoverhaul.config.Config;
import com.ludwici.slimeoverhaul.entity.custom.BaseSlime;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;

public class EarthSlime extends BaseSlime {
    public EarthSlime(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.getAvailableGoals().removeIf(g -> g.getGoal() instanceof SlimeFloatGoal);
    }

    @Override
    public ParticleOptions getParticleType() {
        return ParticleTypes.ASH;
    }

    @Override
    public String getSlimeType() {
        return "earth";
    }

    @Override
    public void setSize(int size, boolean updateHealth) {
        super.setSize(size, updateHealth);
        int j = Mth.clamp(size, 1, 127);
        double health = j * j * 2 + j * 2;
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)(0.2F + 0.1F * (float)j) * 0.7F);
        if (updateHealth) {
            this.setHealth(this.getMaxHealth());
        }
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return BaseSlime.createMobAttributes()
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8);
    }

    public static boolean checkSpawnRules(EntityType<EarthSlime> type, LevelAccessor level, EntitySpawnReason mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        if (Config.SPAWN_EARTH_SLIMES.isFalse()) {
            return false;
        }

        if (level.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }

        Holder<Biome> biome = level.getBiome(blockPos);
        if (biome.is(Tags.Biomes.IS_COLD)) {
            return false;
        }

        if (biome.is(Tags.Biomes.IS_BADLANDS)) {
            return biome.is(Biomes.WOODED_BADLANDS);
        }

        return true;
    }
}
