package org.ofus.core.feature.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class FurnaceRecipes {

    private static final float DEFAULT_EXPERIENCE = 0.35f;
    private static final int DEFAULT_COOK_TIME = 200;

    private FurnaceRecipes() {
    }

    public static List<Recipe> create(JavaPlugin plugin) {
        return List.of(
                rottenFleshToLeather(plugin)
        );
    }

    private static FurnaceRecipe furnace(JavaPlugin plugin, String key, Material input, Material output) {
        return furnace(plugin, key, input, new ItemStack(output), DEFAULT_EXPERIENCE, DEFAULT_COOK_TIME);
    }

    private static FurnaceRecipe furnace(JavaPlugin plugin, String key, Material input, ItemStack output, float experience, int cookTime) {
        return new FurnaceRecipe(
                key(plugin, key),
                output,
                new RecipeChoice.MaterialChoice(input),
                experience,
                cookTime
        );
    }

    private static FurnaceRecipe rottenFleshToLeather(JavaPlugin plugin) {
        return furnace(plugin, "rotten_flesh_to_leather", Material.ROTTEN_FLESH, Material.LEATHER);
    }

    private static NamespacedKey key(JavaPlugin plugin, String key) {
        return new NamespacedKey(plugin, key);
    }
}