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

    private static final ModConfigSpec.IntValue FOOD_STACK_SIZE = BUILDER.comment("Size of last eaten food stack. " +
            "Smaller size - faster health changes for the new food eaten").defineInRange("foodStackSize", 4, 1,
            Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue MIN_HEALTH =
            BUILDER.comment("Minimum health of a player.").defineInRange("minHealth", 10, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue MAX_HEALTH =
            BUILDER.comment("Maximum health of a player.").defineInRange("maxHealth", 20, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.BooleanValue SET_MIN_HEALTH = BUILDER.comment("Changes the player's maximum hp" +
            " to the value, provided at minHealth.", "Disabling it makes minHealth only be used for bonus hp " +
            "calculations.", "WARNING: Disabling it doesn't return the previous max hp! If no other mod changes hp, " +
            "first set new minHp, then disable this property.").define("setMinHealth", true);

    private static final ModConfigSpec.BooleanValue CLAMP_MAX_HEALTH = BUILDER.comment("If false and filtering makes " +
            "some food better than the 'best' food, it can go beyond the defined 'max' hp.", "Otherwise it will clamp" +
            " this value to the max hp value.").define("clampMaxHealth", true);
    private static final ModConfigSpec.BooleanValue ROUND_HP = BUILDER.comment("Rounds hp, so there can't be max hp " +
            "with half heart empty.").define("roundHp", true);

    private static final ModConfigSpec.ConfigValue<String> FOOD_FILTERING = BUILDER.comment("What filtering logic is " +
            "applied to get the 'best' food available.", "NONE - gets the best food and calculates hp gain from it, " +
            "no filtering applied.", "PERCENTILE - gets the 95th percentile best food, ignoring one of a kind food.")
            .defineInList("foodFiltering", "PERCENTILE",
                    Arrays.asList("NONE", "PERCENTILE"));

    private static final ModConfigSpec.BooleanValue SCAN_FOOD = BUILDER.comment("Scans food items during startup, to " +
            "generate food values.",
            "If false, mod will generate relative food values only for the specified foods.").define("scanFood", true);

    // a list of strings that are treated as resource locations for items
//    private static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER.comment("A list
//    of " +
//            "items to log on common setup.").defineListAllowEmpty("items", List.of("minecraft:iron_ingot"),
//            Config::validateItemName);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int foodStackSize;

    public static int minHealth;
    public static int maxHealth;

    public static boolean setMinHealth;

    public static boolean clampMaxHealth;
    public static boolean roundHp;
    public static boolean scanFood;

    public static String foodFiltering;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        foodStackSize = FOOD_STACK_SIZE.get();

        minHealth = MIN_HEALTH.get();
        maxHealth = MAX_HEALTH.get();
        setMinHealth = SET_MIN_HEALTH.get();

        foodFiltering = FOOD_FILTERING.get();

        clampMaxHealth = CLAMP_MAX_HEALTH.get();
        roundHp = ROUND_HP.get();

        scanFood = SCAN_FOOD.get();
        // convert the list of strings into a set of items
//        items = ITEM_STRINGS.get().stream().map(itemName -> BuiltInRegistries.ITEM.get(ResourceLocation.parse
//        (itemName))).collect(Collectors.toSet());
    }
}
