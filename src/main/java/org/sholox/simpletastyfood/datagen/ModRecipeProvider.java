package org.sholox.simpletastyfood.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.sholox.simpletastyfood.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput p_248933_, CompletableFuture<HolderLookup.Provider> p_323846_) {
        super(p_248933_, p_323846_);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.SOUP.get())
                .requires(Items.BOWL)
                .requires(Items.BREAD)
                .requires(Items.WATER_BUCKET)
                .unlockedBy("has_bread", has(Items.BREAD))
                .save(recipeOutput);
    }
}
