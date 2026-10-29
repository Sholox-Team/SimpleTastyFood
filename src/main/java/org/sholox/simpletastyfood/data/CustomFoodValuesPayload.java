package org.sholox.simpletastyfood.data;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.sholox.simpletastyfood.Simpletastyfood;

public record CustomFoodValuesPayload(String json) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CustomFoodValuesPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Simpletastyfood.MOD_ID,
            "custom_food_values"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CustomFoodValuesPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    CustomFoodValuesPayload::json,
                    CustomFoodValuesPayload::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
