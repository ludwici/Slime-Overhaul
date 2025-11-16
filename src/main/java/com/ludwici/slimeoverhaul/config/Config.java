package com.ludwici.slimeoverhaul.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SPAWN_AIR_SLIMES = BUILDER.define("allowAirSlimes", true);
    public static final ModConfigSpec.BooleanValue SPAWN_WATER_SLIMES = BUILDER.define("allowWaterSlimes", true);
    public static final ModConfigSpec.BooleanValue SPAWN_EARTH_SLIMES = BUILDER.define("allowEarthSlimes", true);
    public static final ModConfigSpec.BooleanValue SPAWN_FLAME_SLIMES = BUILDER.define("allowFlameSlimes", true);

    public static final ModConfigSpec.ConfigValue<Integer> WATER_SLIME_FIND_FIRE = BUILDER
            .translation(MODID + ".configuration.waterSlimeFindFireTick")
            .define("waterSlimeFindFireTick", 20);

    public static final ModConfigSpec.BooleanValue WATER_SLIME_CAN_TRANSFORM_SAND = BUILDER.define("water_slime_can_transform_sand", true);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
