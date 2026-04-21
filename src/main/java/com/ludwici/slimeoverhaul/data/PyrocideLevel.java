package com.ludwici.slimeoverhaul.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;

import java.util.Objects;

public class PyrocideLevel {
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 4;
    public static final PyrocideLevel DEFAULT = new PyrocideLevel(MIN_LEVEL, 0);

    private int level;
    private float damage;

    public PyrocideLevel(int level, float damage) {
        this.level = level;
        this.damage = damage;
    }

    public PyrocideLevel() {
        this.level = MIN_LEVEL;
        this.damage = 0;
    }

    public int getLevel() {
        return level;
    }

    public float getDamage() {
        return damage;
    }

    public void reset() {
        level = MIN_LEVEL;
        resetDamage();
    }

    public float getDamageModifier() {
        return switch (level) {
            case 2 -> 0.25F;
            case 3 -> 0.35F;
            case 4 -> 0.4F;
            default -> 0.15F;
        };
    }

    public void resetDamage() {
        damage = 0;
    }

    public static final Codec<PyrocideLevel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("level").forGetter(PyrocideLevel::getLevel),
            Codec.FLOAT.fieldOf("damage").forGetter(PyrocideLevel::getDamage)
    ).apply(instance, PyrocideLevel::new));

    public static final StreamCodec<ByteBuf, PyrocideLevel> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PyrocideLevel::getLevel,
            ByteBufCodecs.FLOAT, PyrocideLevel::getDamage,
            PyrocideLevel::new
    );

    public void levelUp() {
        level = Mth.clamp(level + 1, MIN_LEVEL, MAX_LEVEL);
    }

    public void addDamage(float value) {
        damage = Mth.clamp(damage + value, 0, Float.MAX_VALUE);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PyrocideLevel pyrocideLevel)) return false;

        return level == pyrocideLevel.getLevel()
                && damage == pyrocideLevel.getDamage()
                ;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(level);
        result = 31 * result + Float.hashCode(damage);
        return result;
    }
}
