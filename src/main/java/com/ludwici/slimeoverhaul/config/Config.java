package com.ludwici.slimeoverhaul.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SPAWN_AIR_SLIMES = BUILDER.define("allowAirSlimes", true);
    public static final ModConfigSpec.BooleanValue SPAWN_WATER_SLIMES = BUILDER.define("allowWaterSlimes", true);
    public static final ModConfigSpec.BooleanValue SPAWN_EARTH_SLIMES = BUILDER.define("allowEarthSlimes", true);
    public static final ModConfigSpec.BooleanValue SPAWN_FLAME_SLIMES = BUILDER.define("allowFlameSlimes", true);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
