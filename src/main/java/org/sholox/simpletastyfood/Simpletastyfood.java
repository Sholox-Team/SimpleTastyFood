package org.sholox.simpletastyfood;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.sholox.simpletastyfood.data.ModDataAttachments;
import org.sholox.simpletastyfood.event.TooltipRenderer;
import org.sholox.simpletastyfood.item.ModItems;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Simpletastyfood.MOD_ID)
public class Simpletastyfood {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "simpletastyfood";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Simpletastyfood(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::onRegisterClientTooltipComponentFactories);

        ModItems.register(modEventBus);
        ModDataAttachments.register(modEventBus);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
        modContainer.registerConfig(ModConfig.Type.STARTUP, ClientConfig.SPEC);
    }

    private void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        TooltipRenderer.register(event);
    }
}
