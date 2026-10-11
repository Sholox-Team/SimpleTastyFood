package org.sholox.simpletastyfood.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;

public class CustomFoodValuesSyncer {
    private record FoodValuesPacket(Map<String, Float> values) {
        public static final StreamCodec<ByteBuf, FoodValuesPacket> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.map(
                                HashMap::new,
                                ByteBufCodecs.STRING_UTF8,
                                ByteBufCodecs.FLOAT
                        ),
                        FoodValuesPacket::values,
                        FoodValuesPacket::new
                );
    }

    public void sendCustomValues(ServerPlayer player) {
//        Map<ResourceLocation, Float> customValues = CustomFoodValuesLoader.getCustomValues();
//        FoodValuesPacket packet = new FoodValuesPacket(customValues);

//        PacketDistributor.sendToPlayer(player, packet);
    }
}
