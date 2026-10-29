package org.sholox.simpletastyfood.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public record FoodValue(float foodValue, int parts) {
    public FoodValue {
        if (parts < 1) {
            throw new IllegalArgumentException("parts must be >= 1");
        }
    }

    public static FoodValue empty() {
        return new FoodValue(0f, 1);
    }

    public float weightedValue() {
        return foodValue / parts;
    }

    public static ArrayList<FoodValue> get(Player player) {
        return new ArrayList<>(player.getData(ModDataAttachments.FOOD_VALUES));
    }

    public static void setFoodValues(Player player, ArrayList<FoodValue> foodValues) {
        player.setData(ModDataAttachments.FOOD_VALUES, foodValues);
    }

    private static final Codec<FoodValue> DEFAULT_CODEC = RecordCodecBuilder.<FoodValue>create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("foodValue").forGetter(FoodValue::foodValue),
                    Codec.INT.optionalFieldOf("parts", 1).forGetter(FoodValue::parts)
            ).apply(instance, FoodValue::new)
    );

    public static final Codec<FoodValue> CODEC = Codec
            .either(Codec.FLOAT, FoodValue.DEFAULT_CODEC)
            .xmap(either -> either.map(
                    value -> new FoodValue(value, 1),
                    value -> value), Either::right
            );
}
