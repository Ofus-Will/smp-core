package org.ofus.core.feature.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class CraftingRecipes {

    private CraftingRecipes() {
    }

    public static List<Recipe> create(JavaPlugin plugin) {
        return List.of();
    }

    private static ShapedRecipe shaped(JavaPlugin plugin, String key, ItemStack result, String... shape) {
        ShapedRecipe recipe = new ShapedRecipe(key(plugin, key), result);
        recipe.shape(shape);
        return recipe;
    }

    private static ShapelessRecipe shapeless(JavaPlugin plugin, String key, ItemStack result) {
        return new ShapelessRecipe(key(plugin, key), result);
    }

    private static ItemStack item(Material material) {
        return new ItemStack(material);
    }

    private static ItemStack item(Material material, int amount) {
        return new ItemStack(material, amount);
    }

    private static NamespacedKey key(JavaPlugin plugin, String key) {
        return new NamespacedKey(plugin, key);
    }
}