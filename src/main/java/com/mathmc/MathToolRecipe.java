package com.mathmc;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

final class MathToolRecipe extends SpecialCraftingRecipe {
    MathToolRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        if (inventory.getWidth() < 2 || inventory.getHeight() < 3) {
            return false;
        }
        ItemStack aStack = getStackAt(inventory, 0, 0);
        ItemStack bStack = getStackAt(inventory, 1, 0);
        ItemStack cStack = getStackAt(inventory, 0, 2);
        if (!MathNumbers.isNumberedLog(aStack) || !MathNumbers.isNumberedLog(bStack) || !MathNumbers.isNumberedLog(cStack)) {
            return false;
        }
        for (int y = 0; y < inventory.getHeight(); y++) {
            for (int x = 0; x < inventory.getWidth(); x++) {
                if ((x == 0 && y == 0) || (x == 1 && y == 0) || (x == 0 && y == 2)) {
                    continue;
                }
                if (!getStackAt(inventory, x, y).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack craft(CraftingInventory inventory, DynamicRegistryManager registryManager) {
        int a = MathNumbers.getLogNumber(getStackAt(inventory, 0, 0));
        int b = MathNumbers.getLogNumber(getStackAt(inventory, 1, 0));
        int c = MathNumbers.getLogNumber(getStackAt(inventory, 0, 2));
        int result = a * b + c;
        Item tool = MathToolNumbers.findToolForNumber(result);
        if (tool == Items.AIR) {
            return ItemStack.EMPTY;
        }
        int targetNumber = MathToolNumbers.getToolNumber(tool);
        ItemStack output = new ItemStack(tool);
        MathNumbers.applyToolNumber(output, targetNumber);
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 3;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return ItemStack.EMPTY;
    }

    @Override
    public net.minecraft.recipe.RecipeSerializer<?> getSerializer() {
        return MathMcMod.MATH_TOOL_RECIPE_SERIALIZER;
    }

    private ItemStack getStackAt(CraftingInventory inventory, int x, int y) {
        return inventory.getStack(x + y * inventory.getWidth());
    }
}
