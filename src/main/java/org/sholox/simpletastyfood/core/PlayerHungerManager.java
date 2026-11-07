package org.sholox.simpletastyfood.core;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.sholox.simpletastyfood.Config;
import org.sholox.simpletastyfood.Simpletastyfood;
import org.sholox.simpletastyfood.api.PlayerExhaustionEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = Simpletastyfood.MOD_ID)
public class PlayerHungerManager {
    // Additional multiplier on top of the config one.
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<UUID, Double> additionalMultipliers = new HashMap<>();

    public static void applyExhaustion(ServerPlayer player) {
        var additionalMultiplier = additionalMultipliers.getOrDefault(player.getUUID(), 1.0);

        // 1 hunger depletion every minute
        var exhaustionLevel = (float) (Config.passiveHungerMultiplier / 300.0);
        exhaustionLevel *= additionalMultiplier;

        PlayerExhaustionEvent event = new PlayerExhaustionEvent(player, additionalMultiplier, Config.passiveHungerMultiplier, exhaustionLevel);
        if (event.isCanceled()) return;

        player.getFoodData().addExhaustion(event.result);
    }

    @SubscribeEvent
    public static void playerTickEvent(EntityTickEvent.Post event) {
        if (Config.passiveHungerMultiplier <= 0.0) return;

        Entity entity = event.getEntity();

        if (!(entity instanceof ServerPlayer player && player.isAlive())) return;

        if (player.isCreative() || player.isSpectator()) return;
        if (player.level().getDifficulty() == Difficulty.PEACEFUL) return;

        applyExhaustion(player);
    }
}
