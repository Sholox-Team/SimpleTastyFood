package org.sholox.simpletastyfood.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;

public class HealthTooltipRenderer implements ClientTooltipComponent {
    private final HealthTooltip healthTooltip;

    HealthTooltipRenderer(HealthTooltip healthTooltip)
    {
        this.healthTooltip = healthTooltip;
    }

    private static final ResourceLocation HEALTH_TEXTURE = ResourceLocation.withDefaultNamespace("hud/heart/full");

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public int getWidth(Font font) {
        int healthIconWidth = 9;
        int textWidth = (int) (font.width(healthTooltip.bonusHealthText) * 0.75f);

        return healthIconWidth + textWidth + 3;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int offsetX = x;

        // Render Image
        guiGraphics.blitSprite(HealthTooltipRenderer.HEALTH_TEXTURE, offsetX, y, 9, 9);

        // Render Text
        offsetX += 9;
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(offsetX, y, 0);
        poseStack.scale(0.75f, 0.75f, 0.75f);
        guiGraphics.drawString(font, healthTooltip.bonusHealthText, 2, 2, 0xFFAAAAAA);
        poseStack.popPose();

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // reset to drawHoveringText state
        RenderSystem.disableDepthTest();
    }
}
