package org.sholox.simpletastyfood.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.sholox.simpletastyfood.Simpletastyfood;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Simpletastyfood.MOD_ID);

    public static final FoodProperties SOUP_PROPERTIES = new FoodProperties.Builder()
            .nutrition(0)
            .saturationModifier(0.0f)
            .usingConvertsTo(Items.BOWL)
            .build();

    public static final DeferredItem<Item> SOUP = ITEMS
            .register("soup",
                    () -> new Item(new Item.Properties().food(SOUP_PROPERTIES))
            );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
