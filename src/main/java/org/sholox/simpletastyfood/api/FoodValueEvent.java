package org.sholox.simpletastyfood.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.bus.api.Event;

public class FoodValueEvent extends Event {
    public FoodValueEvent(ResourceLocation resourceLocation, FoodProperties foodProperties) {
        this.resourceLocation = resourceLocation;
        this.foodProperties = foodProperties;
    }

    public final ResourceLocation resourceLocation;
    public FoodProperties foodProperties;
}
