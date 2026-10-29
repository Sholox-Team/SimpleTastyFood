package org.sholox.simpletastyfood.event;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.sholox.simpletastyfood.Config;
import org.sholox.simpletastyfood.core.FoodValueCalculator;
import org.sholox.simpletastyfood.core.FoodValueCore;
import org.sholox.simpletastyfood.core.PlayerHungerManager;
import org.sholox.simpletastyfood.data.CustomFoodValuesLoader;
import org.sholox.simpletastyfood.Simpletastyfood;
import org.sholox.simpletastyfood.data.FoodValue;
import org.sholox.simpletastyfood.item.ModItems;
import org.sholox.simpletastyfood.mixin.PlayerMixin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = Simpletastyfood.MOD_ID)
public class EventListener {
    @SubscribeEvent
    public static void playerJoined(PlayerEvent.PlayerLoggedInEvent playerEvent) {
        Player player = playerEvent.getEntity();

        // Set player minHealth
        if (Config.setMinHealth) {
            var attribute = player.getAttributes();
            if (attribute != null) {
                attribute.getInstance(Attributes.MAX_HEALTH).setBaseValue(Config.minHealth);
            }
        }

        PlayerHungerManager.test(player);

        if (player instanceof ServerPlayer serverPlayer) {
            FoodValueCore.updatePlayerMaxHealth(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void playerSpawned(PlayerEvent.PlayerRespawnEvent playerEvent) {
        Player player = playerEvent.getEntity();
        FoodValueCore.updatePlayerMaxHealth(player);
    }

    @SubscribeEvent
    public static void playerEatenFood(LivingEntityUseItemEvent.Finish useItemEvent) {
        ItemStack item = useItemEvent.getItem();
        ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(item.getItem());

        FoodProperties foodProperties = item.getFoodProperties(useItemEvent.getEntity());

        if (useItemEvent.getEntity() instanceof Player player && foodProperties != null) {
            if (player instanceof ServerPlayer serverPlayer) {
                FoodValue foodValue = FoodValueCalculator.foodValue(resourceLocation, foodProperties);
                FoodValueCore.onFoodEaten(foodValue, serverPlayer);
            } else if (player instanceof LocalPlayer localPlayer) {
                ((PlayerMixin) localPlayer).setFlashOnSetHealth(false);
            }
        }
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.SOUP);
        }
    }

    @SubscribeEvent
    public static void setupFoodConfig(ServerStartingEvent event) {
        manualSetupFoodConfig();

        CustomFoodValuesLoader.load();
        FoodValueCalculator.customFoodValues = CustomFoodValuesLoader.getCustomValues();
    }

    public static void manualSetupFoodConfig() {
        Map<ResourceLocation, FoodProperties> allFoodProperties = new HashMap<>(){};

        if (Config.scanFood) {
            allFoodProperties = BuiltInRegistries.ITEM.stream()
                    .map(item -> {
                        ResourceLocation itemLocation = BuiltInRegistries.ITEM.getKey(item);
                        FoodProperties baseProperties = item.getDefaultInstance().getFoodProperties(null);

                        if (baseProperties == null) {
                            return null;
                        }

                        return Map.entry(itemLocation, baseProperties);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        FoodValueCore.updateFoodToHealthProperties(allFoodProperties);
    }
}