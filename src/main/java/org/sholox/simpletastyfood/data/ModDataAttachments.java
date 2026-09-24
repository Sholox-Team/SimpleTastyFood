package org.sholox.simpletastyfood.data;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.sholox.simpletastyfood.Config;
import org.sholox.simpletastyfood.Simpletastyfood;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ModDataAttachments {
    // Create the DeferredRegister for attachment types
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister
            .create(NeoForgeRegistries.ATTACHMENT_TYPES, Simpletastyfood.MOD_ID);

    private static final StreamCodec<ByteBuf, List<Float>> codec =
            ByteBufCodecs.FLOAT.apply(ByteBufCodecs.list());

    // Serialization via codec
    public static final Supplier<AttachmentType<List<Float>>> FOOD_VALUES = ATTACHMENT_TYPES
            .register("food_values", () -> AttachmentType.builder(() -> Collections.nCopies(Config.foodStackSize, 0f))
                    .serialize(Codec.FLOAT.listOf())
                    .sync(codec)
                    .copyOnDeath()
                    .build());

    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
