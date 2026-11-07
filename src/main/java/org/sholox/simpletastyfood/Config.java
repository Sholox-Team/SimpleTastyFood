package org.sholox.simpletastyfood;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = Simpletastyfood.MOD_ID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue FOOD_STACK_SIZE =
            BUILDER.comment("Size of last eaten food stack. " + "Smaller size - faster health changes for the new " + "food eaten").defineInRange("foodStackSize", 4, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue MIN_HEALTH =
            BUILDER.comment("Minimum health of a player. Used in " + "calculations and can set player's minimum " +
                    "health.").defineInRange("minHealth", 20, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue MAX_HEALTH =
            BUILDER.comment("Maximum health of a player. Used in " + "calculations.").defineInRange("maxHealth", 40,
                    1, Integer.MAX_VALUE);

    private static final ModConfigSpec.BooleanValue SET_MIN_HEALTH =
            BUILDER.comment("Changes the player's maximum " + "hp" + " to the value, provided at minHealth.",
                    "Disabling it makes minHealth only be used for bonus hp " + "calculations.", "WARNING: Disabling "
                            + "it " + "doesn't return the previous max hp! If no other mod changes hp, " + "first set" +
                            " " + "new minHp, then " + "disable this property.").define("setMinHealth", false);

    private static final ModConfigSpec.BooleanValue CLAMP_MAX_HEALTH = BUILDER.comment("If false and filtering makes "
            + "some food better than the 'best' food, it can go beyond the defined 'max' hp.",
            "Otherwise it will " + "clamp" + " this value to the max hp value.").define("clampMaxHealth", true);
    private static final ModConfigSpec.BooleanValue ROUND_HP =
            BUILDER.comment("Rounds hp, so there can't be max hp " + "with half heart empty.").define("roundHp", true);

    private static final ModConfigSpec.DoubleValue NUTRITION_MULTIPLIER = BUILDER.comment("Multiplier of a nutrition "
            + "property of the food, to determine final food value. Custom values skip this.").defineInRange(
                    "nutritionMultiplier", 1.0, 0.0, Double.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue SATURATION_MULTIPLIER = BUILDER.comment("Multiplier of a " +
            "saturation property of the food, to determine final food value. Custom values skip this.").defineInRange("saturationMultiplier", 1.0, 0.0, Double.MAX_VALUE);

    private static final ModConfigSpec.ConfigValue<String> FOOD_FILTERING = BUILDER.comment("What filtering logic is "
            + "applied to get the 'best' food available.", "NONE - gets the best food and calculates hp gain from it,"
            + " " + "no filtering applied.", "PERCENTILE - gets the 95th percentile best food, ignoring one of a " +
            "kind" + " " + "food.").defineInList("foodFiltering", "PERCENTILE", Arrays.asList("NONE", "PERCENTILE"));

    private static final ModConfigSpec.DoubleValue PASSIVE_HUNGER_MULTIPLIER =
            BUILDER.comment("Multiplier applied " + "to" + " passive hunger depletion. Value of 1 will deplete 1 " +
                    "point of hunger every minute. Other mods can" + " set " + "additional multiplier on top of this " +
                    "one.", "Set 0 to disable. Peaceful disables this.").defineInRange("passiveHungerMultiplier", 0.0
                    , 0.0, Double.MAX_VALUE);

    private static final ModConfigSpec.BooleanValue SCAN_FOOD = BUILDER.comment("Scans food items during startup, to "
            + "map food values for other calculations.",
            "If false, mod will generate relative food values only for " + "the custom food values provided in " +
                    "simpletastyfood_custom_values.json.").define("scanFood", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int foodStackSize;

    public static int minHealth;
    public static int maxHealth;

    public static double nutritionMultiplier;
    public static double saturationMultiplier;

    public static boolean setMinHealth;

    public static boolean clampMaxHealth;
    public static boolean roundHp;
    public static boolean scanFood;

    public static double passiveHungerMultiplier;

    public static String foodFiltering;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event instanceof ModConfigEvent.Unloading) return;

        foodStackSize = FOOD_STACK_SIZE.get();

        minHealth = MIN_HEALTH.get();
        maxHealth = MAX_HEALTH.get();

        nutritionMultiplier = NUTRITION_MULTIPLIER.get();
        saturationMultiplier = SATURATION_MULTIPLIER.get();

        setMinHealth = SET_MIN_HEALTH.get();

        foodFiltering = FOOD_FILTERING.get();

        clampMaxHealth = CLAMP_MAX_HEALTH.get();
        roundHp = ROUND_HP.get();

        passiveHungerMultiplier = PASSIVE_HUNGER_MULTIPLIER.get();

        scanFood = SCAN_FOOD.get();
    }
}
