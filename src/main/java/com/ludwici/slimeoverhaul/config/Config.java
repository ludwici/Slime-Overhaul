package com.ludwici.slimeoverhaul.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public final class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Bool SPAWN_AIR_SLIMES = new Bool(true);
    public static final Bool SPAWN_WATER_SLIMES = new Bool(true);
    public static final Bool SPAWN_EARTH_SLIMES = new Bool(true);
    public static final Bool SPAWN_FLAME_SLIMES = new Bool(true);
    public static final IntegerValue WATER_SLIME_FIND_FIRE = new IntegerValue(20);
    public static final Bool WATER_SLIME_CAN_TRANSFORM_SAND = new Bool(true);

    private Config() {
    }

    public static void load() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("slimeoverhaul.json");
        try {
            if (Files.exists(path)) {
                JsonObject json = GSON.fromJson(Files.readString(path), JsonObject.class);
                if (json != null) {
                    readBool(json, "allowAirSlimes", SPAWN_AIR_SLIMES);
                    readBool(json, "allowWaterSlimes", SPAWN_WATER_SLIMES);
                    readBool(json, "allowEarthSlimes", SPAWN_EARTH_SLIMES);
                    readBool(json, "allowFlameSlimes", SPAWN_FLAME_SLIMES);
                    readBool(json, "water_slime_can_transform_sand", WATER_SLIME_CAN_TRANSFORM_SAND);
                    if (json.has("waterSlimeFindFireTick")) {
                        WATER_SLIME_FIND_FIRE.value = Math.max(1, json.get("waterSlimeFindFireTick").getAsInt());
                    }
                }
            } else {
                JsonObject defaults = new JsonObject();
                defaults.addProperty("allowAirSlimes", SPAWN_AIR_SLIMES.get());
                defaults.addProperty("allowWaterSlimes", SPAWN_WATER_SLIMES.get());
                defaults.addProperty("allowEarthSlimes", SPAWN_EARTH_SLIMES.get());
                defaults.addProperty("allowFlameSlimes", SPAWN_FLAME_SLIMES.get());
                defaults.addProperty("waterSlimeFindFireTick", WATER_SLIME_FIND_FIRE.get());
                defaults.addProperty("water_slime_can_transform_sand", WATER_SLIME_CAN_TRANSFORM_SAND.get());
                Files.createDirectories(path.getParent());
                Files.writeString(path, GSON.toJson(defaults));
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load SlimeOverhaul config", e);
        }
    }

    private static void readBool(JsonObject json, String key, Bool value) {
        if (json.has(key)) {
            value.value = json.get(key).getAsBoolean();
        }
    }

    public static final class Bool {
        private boolean value;

        private Bool(boolean value) {
            this.value = value;
        }

        public boolean get() {
            return value;
        }

        public boolean isFalse() {
            return !value;
        }

        public boolean isTrue() {
            return value;
        }
    }

    public static final class IntegerValue {
        private int value;

        private IntegerValue(int value) {
            this.value = value;
        }

        public int get() {
            return value;
        }
    }
}
