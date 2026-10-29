package org.sholox.simpletastyfood;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = Simpletastyfood.MOD_ID)
public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue DISPLAY_BONUS_HEALTH_TOOLTIP = BUILDER
            .comment("Displays bonus health tooltip")
            .define("displayBonusHealthTooltip", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean displayBonusHealthTooltip;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event instanceof ModConfigEvent.Unloading) { return; }

        displayBonusHealthTooltip = DISPLAY_BONUS_HEALTH_TOOLTIP.get();
    }
}
