package com.mathmc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

final class MathToolNumbers {
    private static final String STATE_ID = MathMcMod.MOD_ID + ":tool_numbers";
    private static final List<Item> TOOLS = List.of(
            Items.WOODEN_PICKAXE,
            Items.WOODEN_AXE,
            Items.WOODEN_SHOVEL,
            Items.WOODEN_HOE,
            Items.WOODEN_SWORD
    );

    private static final Map<Item, Integer> TOOL_NUMBERS = new HashMap<>();

    private MathToolNumbers() {
    }

    static void initialize(MinecraftServer server) {
        PersistentStateManager stateManager = server.getOverworld().getPersistentStateManager();
        ToolNumberState state = stateManager.getOrCreate(ToolNumberState::new, ToolNumberState::fromNbt, STATE_ID);
        Random random = Random.create(server.getOverworld().getSeed());
        for (Item tool : TOOLS) {
            Identifier id = Registries.ITEM.getId(tool);
            if (!state.numbers.containsKey(id.toString())) {
                state.numbers.put(id.toString(), random.nextBetween(11, 100));
            }
        }
        state.markDirty();
        TOOL_NUMBERS.clear();
        state.numbers.forEach((key, value) -> {
            Item item = Registries.ITEM.get(new Identifier(key));
            if (item != Items.AIR) {
                TOOL_NUMBERS.put(item, value);
            }
        });
    }

    static int getToolNumber(Item item) {
        return TOOL_NUMBERS.getOrDefault(item, 0);
    }

    static Item findToolForNumber(int number) {
        for (Map.Entry<Item, Integer> entry : TOOL_NUMBERS.entrySet()) {
            if (entry.getValue() == number) {
                return entry.getKey();
            }
        }
        return Items.AIR;
    }

    static boolean isKnownTool(Item item) {
        return TOOL_NUMBERS.containsKey(item);
    }

    private static final class ToolNumberState extends PersistentState {
        private final Map<String, Integer> numbers = new HashMap<>();

        static ToolNumberState fromNbt(NbtCompound nbt) {
            ToolNumberState state = new ToolNumberState();
            for (String key : nbt.getKeys()) {
                state.numbers.put(key, nbt.getInt(key));
            }
            return state;
        }

        @Override
        public NbtCompound writeNbt(NbtCompound nbt) {
            for (Map.Entry<String, Integer> entry : numbers.entrySet()) {
                nbt.putInt(entry.getKey(), entry.getValue());
            }
            return nbt;
        }
    }
}
