package org.sholox.simpletastyfood.mixin;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LocalPlayer.class)
public interface PlayerMixin {
    @Accessor("flashOnSetHealth")
    void setFlashOnSetHealth(boolean value);
}
