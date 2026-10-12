package org.sholox.simpletastyfood.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.bus.api.Event;

public class FoodValueEvent extends Event {
    public FoodValueEvent(ResourceLocation resourceLocation, FoodProperties foodProperties, int parts) {
        this.resourceLocation = resourceLocation;
        this.foodProperties = foodProperties;
        this.parts = parts;
    }

    public final ResourceLocation resourceLocation;
    public FoodProperties foodProperties;
    public int parts;
}
