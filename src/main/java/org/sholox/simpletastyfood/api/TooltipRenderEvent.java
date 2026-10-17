package org.sholox.simpletastyfood.api;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class TooltipRenderEvent extends Event implements ICancellableEvent {
    public TooltipRenderEvent(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public ResourceLocation resourceLocation;
}
