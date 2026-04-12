package com.ludwici.slimeoverhaul.entity.custom.variants;

import com.mojang.serialization.Codec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.IntFunction;

public enum WaterSlimeVariant implements StringRepresentable {
    RIVER(0, "river"),
    OCEAN(1, "ocean");

    public static final WaterSlimeVariant DEFAULT = RIVER;
    private static final IntFunction<WaterSlimeVariant> BY_ID = ByIdMap.continuous(WaterSlimeVariant::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

    public static final Codec<WaterSlimeVariant> CODEC = StringRepresentable.fromEnum(WaterSlimeVariant::values);
    public static final Codec<WaterSlimeVariant> LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, WaterSlimeVariant::getId);

    private final int id;
    private final String name;

    WaterSlimeVariant(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public static WaterSlimeVariant byId(int id) {
        return BY_ID.apply(id);
    }
}
