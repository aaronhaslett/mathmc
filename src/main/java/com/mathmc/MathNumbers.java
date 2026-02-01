package com.mathmc;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;

final class MathNumbers {
    static final String LOG_NUMBER_KEY = "MathNumber";
    static final String TOOL_NUMBER_KEY = "MathTarget";

    private MathNumbers() {
    }

    static boolean isNumberedLog(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem blockItem)) {
            return false;
        }
        Block block = blockItem.getBlock();
        if (!block.getDefaultState().isIn(BlockTags.LOGS)) {
            return false;
        }
        NbtCompound nbt = stack.getNbt();
        return nbt != null && nbt.contains(LOG_NUMBER_KEY);
    }

    static int getLogNumber(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt == null ? 0 : nbt.getInt(LOG_NUMBER_KEY);
    }

    static void applyLogNumber(ItemStack stack, int number) {
        stack.getOrCreateNbt().putInt(LOG_NUMBER_KEY, number);
    }

    static void applyToolNumber(ItemStack stack, int number) {
        stack.getOrCreateNbt().putInt(TOOL_NUMBER_KEY, number);
    }
}
