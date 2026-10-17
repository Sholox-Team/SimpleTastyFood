package org.sholox.simpletastyfood.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.neoforge.common.NeoForge;
import org.sholox.simpletastyfood.Config;
import org.sholox.simpletastyfood.Simpletastyfood;
import org.sholox.simpletastyfood.api.BonusHealthEvent;
import org.sholox.simpletastyfood.api.FoodValueEvent;
import org.sholox.simpletastyfood.api.FoodValueValueEvent;
import org.sholox.simpletastyfood.data.FoodValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FoodValueCalculator {
    public static Map<ResourceLocation, FoodValue> customFoodValues = new HashMap<ResourceLocation, FoodValue>();

    public static FoodValue foodValue(ResourceLocation resourceLocation, FoodProperties foodProperties) {
        return foodValue(resourceLocation, foodProperties, 1);
    }

    public static FoodValue foodValue(ResourceLocation resourceLocation, FoodProperties foodProperties, Integer parts) {
        FoodValueEvent foodValueEvent = new FoodValueEvent(resourceLocation, foodProperties, parts);
        NeoForge.EVENT_BUS.post(foodValueEvent);

        // If event changed food properties OR parts, then return updated value
        if (foodValueEvent.foodProperties != foodProperties || foodValueEvent.parts != parts) {
            var foodValue = calculateFoodValue(foodValueEvent.foodProperties);

            return new FoodValue(foodValue, parts);
        }

        FoodValue customFoodValue = customFoodValues.get(resourceLocation);
        if (customFoodValue != null) {
            return customFoodValue;
        }

        return new FoodValue(calculateFoodValue(foodProperties), parts);
    }

    private static float calculateFoodValue(FoodProperties foodProperties) {
        var defaultValue = (foodProperties.nutrition() + foodProperties.saturation()) / 2f;

        FoodValueValueEvent foodValueValueEvent = new FoodValueValueEvent(foodProperties, defaultValue);
        NeoForge.EVENT_BUS.post(foodValueValueEvent);

        return foodValueValueEvent.value;
    }

    public static Float getMaxFoodValue(Float[] values) {
        switch (Config.foodFiltering) {
            case "PERCENTILE":
                // We return the 95th percentile food, instead of best one
                int index = (int) (values.length * 0.95f);
                return values[index];
            default: // NONE and default
                return Arrays.stream(values).max(Float::compare).orElseGet(() -> {
                    System.out.println(Simpletastyfood.MOD_ID + ": Found no food with the maximum value");
                    return 11f; // Default result for vanilla minecraft
                });

        }
    }
}
