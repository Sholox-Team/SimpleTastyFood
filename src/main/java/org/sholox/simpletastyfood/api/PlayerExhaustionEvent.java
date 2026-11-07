package org.sholox.simpletastyfood.api;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class PlayerExhaustionEvent extends Event implements ICancellableEvent {
    public PlayerExhaustionEvent(ServerPlayer player, Double playerAdditionalModifier, double defaultModifier, float result) {
        this.player = player;
        this.playerAdditionalModifier = playerAdditionalModifier;
        this.defaultModifier = defaultModifier;
        this.result = result;
    }

    public final ServerPlayer player;
    public final Double playerAdditionalModifier;
    public final double defaultModifier;

    public float result;
}