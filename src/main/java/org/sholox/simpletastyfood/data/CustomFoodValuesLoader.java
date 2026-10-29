package org.sholox.simpletastyfood.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLPaths;
import org.sholox.simpletastyfood.Simpletastyfood;
import org.sholox.simpletastyfood.core.FoodValueCore;

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

    public static void load() {
        Path path = getCustomValuesPath();

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
        // We convert provided custom foodValue, so provided value is considered as bonus hp, instead of some random value
        var valueForHealth = FoodValueCore.valueForHealth;

        var foodValueMap = raw.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> ResourceLocation.parse(entry.getKey()),
                        entry -> {
                            JsonElement value = entry.getValue();

                            // Old format key:float
                            if (value.isJsonPrimitive()) {
                                return new FoodValue(value.getAsFloat() * valueForHealth, 1);
                            }

                            // New format: key: { "foodValue": 4.0, "parts": 2 }
                            FoodValue foodValue = GSON.fromJson(value, FoodValue.class);

                            return new FoodValue(foodValue.foodValue() * valueForHealth, foodValue.parts());
                        }
                ));
        return  foodValueMap;
    }

    public static String getCustomValuesString() {
        Path path = getCustomValuesPath();

        if (!Files.exists(path)) {
            createEmptyJson(path);
            System.out.println("SimpleTastyFood. Creating empty json for custom values.");

            return null;
        }

        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveCustomValuesString(String string) {
        Path path = getCustomValuesPath();

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(path)) {
                writer.write(string);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create empty food values file", e);
        }
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

    private static Path getCustomValuesPath() {
        return FMLPaths.CONFIGDIR.get()
                .resolve("stf")
                .resolve(Simpletastyfood.MOD_ID + "_custom_values.json");
    }
}
