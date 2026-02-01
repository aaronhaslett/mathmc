# MathMC (POC)

MathMC is a proof-of-concept Fabric mod that turns Minecraft into an arithmetic training game. Wood logs are assigned random numbers between **1–10**, and each wooden tool type is assigned a random target number between **11–100** on world creation. Craft the correct tool by finding three log numbers **A**, **B**, and **C** such that:

```
A * B + C = N
```

Example: if the wooden pickaxe target is **50**, a valid crafting layout uses **7**, **7**, and **1** because `7 * 7 + 1 = 50`.

## Gameplay POC Rules

- **Numbered logs**: break any log to receive a log item tagged with a random number (1–10). The number appears in the tooltip.
- **Tool targets**: wooden tools (pickaxe, axe, shovel, hoe, sword) each have a world-specific target number (11–100). The target number appears in the tooltip.
- **Crafting layout** (3x3 grid):

```
[A][B][ ]
[ ][ ][ ]
[C][ ][ ]
```

If `A * B + C` equals a tool target, the crafting output becomes that tool.

## Build (minimal CLI)

### Prerequisites

- **JDK 17**
- **Gradle** (the build script calls the local `gradle` executable)

### Build

```bash
./scripts/build.sh
```

The compiled mod jar will be in `build/libs/`.

## Install

1. Install **Minecraft 1.20.1** and the **Fabric Loader**.
2. Install the **Fabric API** mod.
3. Copy the built `mathmc-<version>.jar` from `build/libs/` into your Minecraft `mods/` folder.

## Notes

- This is a POC implementation; it focuses on the math-driven crafting loop and uses default log drops for numbers.
- The tool target numbers are stored per-world, so each new world generates new targets.
