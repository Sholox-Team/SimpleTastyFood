package org.sholox.simpletastyfood.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.neoforge.common.NeoForge;
import org.sholox.simpletastyfood.Config;
import org.sholox.simpletastyfood.Simpletastyfood;
import org.sholox.simpletastyfood.api.FoodValueEvent;
import org.sholox.simpletastyfood.data.FoodValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FoodValueCalculator {
    public static Map<ResourceLocation, Float> customFoodValues = new HashMap<ResourceLocation, Float>();

    public static FoodValue foodValue(ResourceLocation resourceLocation, FoodProperties foodProperties) {
        FoodValueEvent foodValueEvent = new FoodValueEvent(resourceLocation, foodProperties);
        NeoForge.EVENT_BUS.post(foodValueEvent);

        // If event changed food properties, then return updated value
        if (foodValueEvent.foodProperties != foodProperties) {
            var foodValue = calculateFoodValue(foodValueEvent.foodProperties);

            return new FoodValue(foodValue, 1);
        }

        Float customFoodValue = customFoodValues.get(resourceLocation);
        if (customFoodValue != null) {
            return new FoodValue(customFoodValue, 1);
        }

        return new FoodValue(calculateFoodValue(foodProperties), 1);
    }

    private static float calculateFoodValue(FoodProperties foodProperties) {
        return (foodProperties.nutrition() + foodProperties.saturation()) / 2f;
    }

    public static Float getMaxFoodValue(Float[] values) {
        switch (Config.foodFiltering) {
            case "PERCENTILE":
                // We return the 95th percentile food, instead of best one
                int index = (int) (values.length * 0.95f);
                return values[index];
            default: // NONE and default
                return Arrays.stream(values)
                        .max(Float::compare)
                        .orElseGet(() -> {
                            System.out.println(Simpletastyfood.MOD_ID + ": Found no food with the maximum value");
                            return 11f; // Default result for vanilla minecraft
                        });

        }
    }
}
