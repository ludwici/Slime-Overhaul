package com.ludwici.slimeoverhaul.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;

public final class PyrocideLevel {
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 4;
    public static final PyrocideLevel DEFAULT = new PyrocideLevel(MIN_LEVEL, 0);

    private final int level;
    private final float damage;

    public PyrocideLevel(int level, float damage) {
        this.level = Mth.clamp(level, MIN_LEVEL, MAX_LEVEL);
        this.damage = Float.isFinite(damage) ? Mth.clamp(damage, 0, Float.MAX_VALUE) : 0;
    }

    public PyrocideLevel() {
        this(MIN_LEVEL, 0);
    }

    public int getLevel() {
        return level;
    }

    public float getDamage() {
        return damage;
    }

    public float getDamageModifier() {
        return switch (level) {
            case 2 -> 0.25F;
            case 3 -> 0.35F;
            case 4 -> 0.4F;
            default -> 0.15F;
        };
    }

    public PyrocideLevel reset() {
        return DEFAULT;
    }

    public PyrocideLevel resetDamage() {
        return new PyrocideLevel(level, 0);
    }

    public PyrocideLevel levelUp() {
        return new PyrocideLevel(level + 1, damage);
    }

    public PyrocideLevel addDamage(float value) {
        if (!Float.isFinite(value)) {
            return this;
        }

        return new PyrocideLevel(level, damage + value);
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PyrocideLevel other)) {
            return false;
        }

        return level == other.level && Float.compare(damage, other.damage) == 0;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(level);
        result = 31 * result + Float.hashCode(damage);
        return result;
    }
}
