package org.ofus.core.feature.recipe;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.ofus.core.feature.PluginFeature;

import java.util.ArrayList;
import java.util.List;

public class RecipeFeature implements PluginFeature {

    private final JavaPlugin plugin;
    private final List<Keyed> registeredRecipes = new ArrayList<>();

    public RecipeFeature(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enable() {
        register(CraftingRecipes.create(plugin));
        register(FurnaceRecipes.create(plugin));
    }

    @Override
    public void disable() {
        for (Keyed recipe : registeredRecipes) {
            Bukkit.removeRecipe(recipe.getKey());
        }

        registeredRecipes.clear();
    }

    private void register(List<? extends Recipe> recipes) {
        for (Recipe recipe : recipes) {
            if (!(recipe instanceof Keyed keyed)) continue;

            Bukkit.removeRecipe(keyed.getKey());
            if (Bukkit.addRecipe(recipe)) {
                registeredRecipes.add(keyed);
            }
        }
    }
}