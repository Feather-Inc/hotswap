package com.juanmuscaria.hotswap.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class HRecipeProvider extends RecipeProvider {
    public HRecipeProvider(PackOutput out) {
        super(out);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {

    }
}
