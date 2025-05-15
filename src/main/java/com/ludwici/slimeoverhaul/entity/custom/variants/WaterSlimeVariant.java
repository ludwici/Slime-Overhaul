package com.ludwici.slimeoverhaul.entity.custom.variants;

import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Comparator;

public enum WaterSlimeVariant implements StringRepresentable {
    RIVER(0, "river"),
    OCEAN(1, "ocean");

    private static final WaterSlimeVariant[] BY_ID = Arrays.stream(values()).sorted(
            Comparator.comparingInt(WaterSlimeVariant::getId)
    ).toArray(WaterSlimeVariant[]::new);

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
        return BY_ID[id % BY_ID.length];
    }
}
