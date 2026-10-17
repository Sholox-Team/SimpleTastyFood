package org.sholox.simpletastyfood.api;

import net.minecraft.world.food.FoodProperties;
import net.neoforged.bus.api.Event;

public class FoodValueValueEvent extends Event {
    public FoodValueValueEvent(FoodProperties foodProperties, float value) {
        this.foodProperties = foodProperties;
        this.value = value;
    }

    public FoodProperties foodProperties;
    public float value;
}
