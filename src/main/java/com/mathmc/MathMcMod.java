package com.mathmc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.tooltip.v1.ItemTooltipCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class MathMcMod implements ModInitializer {
    public static final String MOD_ID = "mathmc";
    public static final SpecialRecipeSerializer<MathToolRecipe> MATH_TOOL_RECIPE_SERIALIZER =
            new SpecialRecipeSerializer<>(MathToolRecipe::new);

    @Override
    public void onInitialize() {
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(MOD_ID, "math_tool"), MATH_TOOL_RECIPE_SERIALIZER);

        ServerLifecycleEvents.SERVER_STARTED.register(MathToolNumbers::initialize);

        PlayerBlockBreakEvents.BEFORE.register(this::handleLogBreak);

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (MathNumbers.isNumberedLog(stack)) {
                int value = MathNumbers.getLogNumber(stack);
                lines.add(Text.literal("Log Number: " + value));
            } else if (MathToolNumbers.isKnownTool(stack.getItem())) {
                int value = MathToolNumbers.getToolNumber(stack.getItem());
                if (value > 0) {
                    lines.add(Text.literal("Target Number: " + value));
                }
            }
        });
    }

    private boolean handleLogBreak(World world, net.minecraft.entity.player.PlayerEntity player, BlockPos pos, BlockState state, net.minecraft.block.entity.BlockEntity blockEntity) {
        if (world.isClient || !state.isIn(BlockTags.LOGS)) {
            return true;
        }
        Block block = state.getBlock();
        ItemStack stack = new ItemStack(block.asItem());
        int number = world.getRandom().nextBetween(1, 10);
        MathNumbers.applyLogNumber(stack, number);
        Block.dropStack(world, pos, stack);
        world.removeBlock(pos, false);
        return false;
    }
}
