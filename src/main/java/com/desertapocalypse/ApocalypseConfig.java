package com.desertapocalypse;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ApocalypseConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue MIN_START_DISTANCE;
    public static final ForgeConfigSpec.IntValue MAX_START_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue START_RADIUS;
    public static final ForgeConfigSpec.DoubleValue BASE_SPEED;
    public static final ForgeConfigSpec.DoubleValue DAILY_ACCELERATION;
    public static final ForgeConfigSpec.DoubleValue MAX_SPEED;
    public static final ForgeConfigSpec.IntValue SAMPLES_PER_TICK;
    public static final ForgeConfigSpec.IntValue SAMPLE_RANGE;

    public static final ForgeConfigSpec.BooleanValue THIRST_ENABLED;
    public static final ForgeConfigSpec.IntValue THIRST_DRAIN_SECONDS;
    public static final ForgeConfigSpec.BooleanValue GIVE_STARTER_KIT;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.push("apocalypse");
        MIN_START_DISTANCE = b.comment("Minimum distance (blocks) from the first player to the Heart of the Desert")
                .defineInRange("minStartDistance", 10, 1, 500);
        MAX_START_DISTANCE = b.comment("Maximum distance (blocks) from the first player to the Heart of the Desert")
                .defineInRange("maxStartDistance", 100, 1, 1000);
        START_RADIUS = b.comment("Radius of the desert at the very start")
                .defineInRange("startRadius", 4.0D, 0.0D, 500.0D);
        BASE_SPEED = b.comment("Spread speed on day 1 in blocks per SECOND (0.03 = ~1.8 blocks per minute)")
                .defineInRange("baseSpeed", 0.03D, 0.0D, 100.0D);
        DAILY_ACCELERATION = b.comment("Each Minecraft day the speed is multiplied by (1 + this value). 0.12 = +12% per day, compounding")
                .defineInRange("dailyAcceleration", 0.12D, 0.0D, 5.0D);
        MAX_SPEED = b.comment("Upper limit of spread speed in blocks per second")
                .defineInRange("maxSpeed", 12.0D, 0.0D, 1000.0D);
        SAMPLES_PER_TICK = b.comment("How many random columns per player per tick get converted (higher = faster visual change, more lag)")
                .defineInRange("samplesPerTick", 40, 1, 1000);
        SAMPLE_RANGE = b.comment("Horizontal range around each player where blocks are converted")
                .defineInRange("sampleRange", 48, 8, 128);
        b.pop();

        b.push("thirst");
        THIRST_ENABLED = b.comment("Enable the thirst system").define("enabled", true);
        THIRST_DRAIN_SECONDS = b.comment("Seconds for a full thirst bar to drain while standing calmly outside the desert")
                .defineInRange("drainSeconds", 900, 60, 36000);
        GIVE_STARTER_KIT = b.comment("Give every new player a Desert Compass and 2 water bottles")
                .define("starterKit", true);
        b.pop();

        SPEC = b.build();
    }

    private ApocalypseConfig() {}
}
