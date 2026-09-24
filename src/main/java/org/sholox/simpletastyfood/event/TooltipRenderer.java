package org.sholox.simpletastyfood.event;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import org.sholox.simpletastyfood.Simpletastyfood;

// Props to AppleSkin
@EventBusSubscriber(modid = Simpletastyfood.MOD_ID, value = Dist.CLIENT)
public class TooltipRenderer {
    public static void register(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(HealthTooltip.class, HealthTooltipRenderer::new);
    }

    @SubscribeEvent
    public static void onTooltipRender(RenderTooltipEvent.GatherComponents event) {
        if (event.isCanceled()) return;

        Player player = Minecraft.getInstance().player;

        ItemStack itemStack = event.getItemStack();

        FoodProperties foodProperties = itemStack.getFoodProperties(player);
        if (foodProperties == null) return;

        // Render tooltip
        ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(itemStack.getItem());

        HealthTooltip foodTooltip = new HealthTooltip(resourceLocation, foodProperties, player);
        event.getTooltipElements().add(Either.right(foodTooltip));
    }
}
