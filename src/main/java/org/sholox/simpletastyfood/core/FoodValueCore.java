package org.sholox.simpletastyfood.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import org.apache.commons.lang3.ArrayUtils;
import org.sholox.simpletastyfood.Config;
import org.sholox.simpletastyfood.Simpletastyfood;
import org.sholox.simpletastyfood.data.CustomFoodValuesLoader;
import org.sholox.simpletastyfood.data.FoodValue;
import org.sholox.simpletastyfood.data.ModDataAttachments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class FoodValueCore {
    // props to Baubley Heart Canisters
    public static final ResourceLocation HEALTH_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(Simpletastyfood.MOD_ID, "extra_health");

    public static float valueForHealth = 1f;

    public static void onFoodEaten(ServerPlayer player, ResourceLocation resourceLocation,
                                   FoodProperties foodProperties) {
        FoodValue foodValue = FoodValueCalculator.foodValue(resourceLocation, foodProperties);
        saveEatenFoodValue(player, foodValue);

        updatePlayerMaxHealth(player);
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.resetSentInfo();
        }
    }

    // Player max health
    public static void updatePlayerMaxHealth(Player player) {
        float bonusHealth = calculateFoodValueBonusHealth(player);

        AttributeInstance healthAttribute = player.getAttribute(Attributes.MAX_HEALTH);

        if (healthAttribute == null) return;
        healthAttribute.removeModifier(HEALTH_MODIFIER_ID);

        // Calculate health addition
        float healthDiff = Config.maxHealth - Config.minHealth;
        if (Config.clampMaxHealth && bonusHealth > healthDiff) {
            bonusHealth = healthDiff ;
        }

        if (Config.roundHp) {
            bonusHealth = roundToTwo(bonusHealth);
        }

        // Set max health modifier if bonus health > 0
        float futureMaxHealth = (float) (healthAttribute.getValue() + bonusHealth);
        if (bonusHealth <= 0) return;

        var playerCurrentHealth = player.getHealth();

        var healthModifier = new AttributeModifier(HEALTH_MODIFIER_ID, bonusHealth, AttributeModifier.Operation.ADD_VALUE);
        healthAttribute.addPermanentModifier(healthModifier);

        // Manually set health, because by default it updates with percentages, we need absolute values
        if (futureMaxHealth < playerCurrentHealth) {
            player.setHealth(futureMaxHealth);
        } else {
            player.setHealth(playerCurrentHealth);
        }
    }

    private static float calculateFoodValueBonusHealth(Player player) {
        ArrayList<FoodValue> foodValues = FoodValue.get(player);

        System.out.println("Test " + foodValues);

        float sum = (float) foodValues.stream()
                .mapToDouble(value -> value.weightedValue() / valueForHealth)
                .sum();

        return sum/foodValues.size();
    }

    // Player food values
    private static void saveEatenFoodValue(Player player, FoodValue foodValue) {
        ArrayList<FoodValue> foodValues = FoodValue.get(player);
        foodValues.add(foodValue);

        while (foodValues.size() > Config.foodStackSize) {
            foodValues.removeFirst();
        }

        FoodValue.setFoodValues(player, foodValues);
    }

    // Food to health properties
    public static void updateFoodToHealthProperties(Map<ResourceLocation, FoodProperties> allFoodProperties) {
        Float[] allFoodValues = allFoodProperties.entrySet().stream()
                .map(entry -> FoodValueCalculator.foodValue(entry.getKey(), entry.getValue()).foodValue())
                .toArray(Float[]::new);

        allFoodValues = ArrayUtils.addAll(allFoodValues, CustomFoodValuesLoader.getCustomValuesFlat());

        Arrays.sort(allFoodValues);

        Float maxFoodValue = FoodValueCalculator.getMaxFoodValue(allFoodValues);

        float hpDifference = Config.maxHealth - Config.minHealth;
        valueForHealth = maxFoodValue / hpDifference;
    }

    public static float roundToTwo(float value) {
        return (Math.round(value / 2.0f) * 2.0f);
    }
}
