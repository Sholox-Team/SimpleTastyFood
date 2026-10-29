package org.sholox.simpletastyfood.event;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.sholox.simpletastyfood.ClientConfig;
import org.sholox.simpletastyfood.Config;
import org.sholox.simpletastyfood.Simpletastyfood;
import org.sholox.simpletastyfood.api.TooltipRenderEvent;

// Props to AppleSkin
@EventBusSubscriber(modid = Simpletastyfood.MOD_ID, value = Dist.CLIENT)
public class TooltipRenderer {
    public static void register(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(HealthTooltip.class, HealthTooltipRenderer::new);
    }

    @SubscribeEvent
    public static void onTooltipRender(RenderTooltipEvent.GatherComponents event) {
        if (!ClientConfig.displayBonusHealthTooltip || event.isCanceled()) return;

        Player player = Minecraft.getInstance().player;

        ItemStack itemStack = event.getItemStack();

        FoodProperties foodProperties = itemStack.getFoodProperties(player);
        if (foodProperties == null) return;

        // Render tooltip
        ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(itemStack.getItem());

        TooltipRenderEvent tooltipRenderEvent = new TooltipRenderEvent(resourceLocation);
        NeoForge.EVENT_BUS.post(tooltipRenderEvent);
        if (tooltipRenderEvent.isCanceled()) return;

        HealthTooltip foodTooltip = new HealthTooltip(resourceLocation, foodProperties, player);
        event.getTooltipElements().add(Either.right(foodTooltip));
    }
}
