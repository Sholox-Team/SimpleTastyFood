package org.sholox.simpletastyfood.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.sholox.simpletastyfood.Config;
import org.sholox.simpletastyfood.core.FoodValueCalculator;
import org.sholox.simpletastyfood.core.FoodValueCore;

import javax.annotation.Nullable;

public class HealthTooltip implements TooltipComponent {
    String bonusHealthText;

    HealthTooltip(ResourceLocation resourceLocation, FoodProperties foodProperties, Player player) {
        float foodValue = FoodValueCalculator.foodValue(resourceLocation, foodProperties);

        var bonusHealth = foodValue / FoodValueCore.valueForHealth;
        bonusHealth = clampBonusHealth(bonusHealth, player);

        float roundedBonusHealth = (FoodValueCore.roundToTwo(bonusHealth) / 2.0f);

        if (Config.roundHp) {
            System.out.println("Test  " + "|" + bonusHealth + "|" + (int) roundedBonusHealth);

            this.bonusHealthText = "x" + (int) roundedBonusHealth;
        } else {
            boolean roundsToHalf = Math.ceil(bonusHealth) % 2 != 0;

            this.bonusHealthText = "x" + (int) roundedBonusHealth + (roundsToHalf ? ".5" : ".0");
        }
    }

    static private float clampBonusHealth(float value, Player player) {
        if (!Config.clampMaxHealth) return value;

        var playerMinHealth = getPlayerMinHealth(player);
        if (playerMinHealth == null) return value;

        if (playerMinHealth + value > Config.maxHealth) {
            return Config.maxHealth - playerMinHealth;
        }

        return value;
    }

    @Nullable
    static private Float getPlayerMinHealth(Player player) {
        var maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth == null) return null;

        return (float) maxHealth.getBaseValue();
    }
}
