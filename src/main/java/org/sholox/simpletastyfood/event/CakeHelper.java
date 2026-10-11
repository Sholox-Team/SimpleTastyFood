package org.sholox.simpletastyfood.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import org.sholox.simpletastyfood.Simpletastyfood;
import org.sholox.simpletastyfood.core.FoodValueCalculator;
import org.sholox.simpletastyfood.core.FoodValueCore;
import org.sholox.simpletastyfood.data.CustomFoodValuesLoader;
import org.sholox.simpletastyfood.data.FoodValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@EventBusSubscriber(modid = Simpletastyfood.MOD_ID)
public class CakeHelper {
    // Note: This has flawed logic. We only check if the cake was eaten, not if player's foodData changed.
    // In theory this means that multiple players can get the buffs from the same 'eat' event.
    // We can track changes in player's FoodData, but it can also change from exhaustionLevel and not properly track.
    // However, i dont care.
    // Also other fix could be adding mixin for the default CakeBlock class, but it also would mean that other mod's cakes wont work.
    // Вибач мені будь ласка
    @SubscribeEvent
    public static void playerUsedItemOnBlockEvent(UseItemOnBlockEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) {
            return;
        }

        BlockPos blockPos = event.getPos();
        BlockState blockState = event.getLevel().getBlockState(blockPos);
        Integer blockBites = blockBites(blockState);

        if (blockBites == null || event.getUsePhase() != UseItemOnBlockEvent.UsePhase.BLOCK) {
            return;
        }

        event.getLevel().getServer().execute(() -> {
            BlockState newBlockState = event.getLevel().getBlockState(blockPos);
            Integer newBlockBites = blockBites(newBlockState);

            // Continue only after bites increased in count OR the block changed to AIR, so cake was fully eaten
            if (!((newBlockBites != null && newBlockBites > blockBites) || newBlockState.getBlock() == Blocks.AIR)) {
                return;
            }

            eat(blockState, player);
        });
    }

    private static void eat(BlockState blockState, ServerPlayer player) {
        FoodValue foodValue = getBlockFoodValue(blockState);
        FoodValueCore.onFoodEaten(foodValue, player);
    }

    private static FoodValue getBlockFoodValue(BlockState blockState) {
        var resource = BuiltInRegistries.BLOCK.getKey(blockState.getBlock());
        var defaultBites = 7;
        var defaultFoodProperties = vanillaCakeBlockFoodProperties();

        return FoodValueCalculator.foodValue(resource, defaultFoodProperties, defaultBites);
    }

    private static FoodProperties vanillaCakeBlockFoodProperties() {
        return new FoodProperties(2 * 7, 0.1F * 7, true, 1,
                Optional.empty(), List.of());
    }

    // We do all of this, because BlockStateProperties.BITES is not the same between mods
    private static Integer blockBites(BlockState blockState) {
        var properties = blockState.getValues();

        var value = properties.entrySet().stream()
                .filter(entry -> entry.getKey().getName().equals("bites"))
                .map(Map.Entry::getValue)
                .findFirst();

        if (value.isPresent() && value.get() instanceof Integer integer) {
            return integer;
        }

        return null;
    }
}