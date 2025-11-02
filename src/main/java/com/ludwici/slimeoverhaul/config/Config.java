package com.ludwici.slimeoverhaul.config;


import net.minecraftforge.common.ForgeConfigSpec;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue SPAWN_AIR_SLIMES = BUILDER.define("allowAirSlimes", true);
    public static final ForgeConfigSpec.BooleanValue SPAWN_WATER_SLIMES = BUILDER.define("allowWaterSlimes", true);
    public static final ForgeConfigSpec.BooleanValue SPAWN_EARTH_SLIMES = BUILDER.define("allowEarthSlimes", true);
    public static final ForgeConfigSpec.BooleanValue SPAWN_FLAME_SLIMES = BUILDER.define("allowFlameSlimes", true);

    public static final ForgeConfigSpec.ConfigValue<Integer> WATER_SLIME_FIND_FIRE = BUILDER
            .translation(MODID + ".configuration.waterSlimeFindFireTick")
            .define("waterSlimeFindFireTick", 20);

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
