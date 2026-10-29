package org.sholox.simpletastyfood.data;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.sholox.simpletastyfood.Simpletastyfood;
import org.sholox.simpletastyfood.event.EventListener;

@EventBusSubscriber(modid = Simpletastyfood.MOD_ID)
public class CustomFoodValuesSyncer {
    public static void sendCustomValues(ServerPlayer player) {
        var customValues = CustomFoodValuesLoader.getCustomValuesString();
        var packet = new CustomFoodValuesPayload(customValues);

        PacketDistributor.sendToPlayer(player, packet);
    }

    public static void handleDataOnMain(final CustomFoodValuesPayload data, final IPayloadContext context) {
        var json = data.json();

        CustomFoodValuesLoader.saveCustomValuesString(json);
        EventListener.manualSetupFoodConfig();
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.1");
        registrar.playToClient(
                CustomFoodValuesPayload.TYPE,
                CustomFoodValuesPayload.STREAM_CODEC,
                CustomFoodValuesSyncer::handleDataOnMain
        );
    }

    @SubscribeEvent
    public static void onPlayerLogin(final PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            // Not remote server, no need to send values
            if (serverPlayer.connection.getConnection().isMemoryConnection()) { return; }

            sendCustomValues(serverPlayer);
        }
    }
}
