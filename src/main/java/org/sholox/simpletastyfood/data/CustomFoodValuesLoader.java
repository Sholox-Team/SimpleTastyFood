package org.sholox.simpletastyfood.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import org.sholox.simpletastyfood.Simpletastyfood;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomFoodValuesLoader {
    private static Map<ResourceLocation, FoodValue> customValues = Map.of();

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static void load(Path directory) {
        Path path = directory
                .resolve("config")
                .resolve("stf")
                .resolve(Simpletastyfood.MOD_ID + "_custom_values.json");

        if (!Files.exists(path)) {
            createEmptyJson(path);
            System.out.println("Custom values. Empty json");

            return;
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            customValues = readCustomValues(reader);

            System.out.println("Custom values. " + customValues);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load food values", e);
        }
    }

    private static Map<ResourceLocation, FoodValue> readCustomValues(Reader reader) {
        Type type = new TypeToken<Map<String, JsonElement>>() {}.getType();

        Map<String, JsonElement> raw = GSON.fromJson(reader, type);

        return raw.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> ResourceLocation.parse(entry.getKey()),
                        entry -> {
                            JsonElement value = entry.getValue();

                            // Old format key:float
                            if (value.isJsonPrimitive()) {
                                return new FoodValue(value.getAsFloat(), 1);
                            }

                            // New format: key: { "foodValue": 4.0, "parts": 2 }
                            return GSON.fromJson(value, FoodValue.class);
                        }
                ));
    }

    public static Map<ResourceLocation, FoodValue> getCustomValues() {
        return customValues;
    }

    public static Float[] getCustomValuesFlat() {
        return customValues.values()
                .stream()
                .map(FoodValue::foodValue)
                .toArray(Float[]::new);
    }

    private static void createEmptyJson(Path path) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(Collections.emptyMap(), writer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create empty food values file", e);
        }
    }
}
