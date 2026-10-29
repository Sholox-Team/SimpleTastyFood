package org.sholox.simpletastyfood.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.sholox.simpletastyfood.Config;
import org.sholox.simpletastyfood.Simpletastyfood;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ModDataAttachments {
    // Create the DeferredRegister for attachment types
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister
            .create(NeoForgeRegistries.ATTACHMENT_TYPES, Simpletastyfood.MOD_ID);

    private static final StreamCodec<ByteBuf, FoodValue> FOOD_VALUE_STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.FLOAT,
                    FoodValue::foodValue,
                    ByteBufCodecs.VAR_INT,
                    FoodValue::parts,
                    FoodValue::new
            );

    private static final StreamCodec<ByteBuf, List<FoodValue>> FOOD_VALUES_STREAM_CODEC =
            FOOD_VALUE_STREAM_CODEC.apply(
                    ByteBufCodecs.collection(ArrayList::new)
            );

    // Serialization via codec
    public static final Supplier<AttachmentType<List<FoodValue>>> FOOD_VALUES = ATTACHMENT_TYPES
            .register("food_values", () -> AttachmentType.builder(() -> Collections.nCopies(Config.foodStackSize, FoodValue.empty()))
                    .serialize(FoodValue.CODEC.listOf())
                    .sync(FOOD_VALUES_STREAM_CODEC)
                    .copyOnDeath()
                    .build());

    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
