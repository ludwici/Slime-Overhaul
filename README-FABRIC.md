# SlimeOverhaul — Fabric 1.21.1

This branch is the Fabric port of SlimeOverhaul 1.1.0. The NeoForge branch `1.21.1` remains unchanged.

## Requirements

- Minecraft 1.21.1
- Java 21
- Fabric Loader 0.19.5
- Fabric API 0.116.17+1.21.1
- CrumbsLib for Fabric 1.21.1 (branch `ludwici/CrumbsLib:1.21.1-fabric`, version `1.21.1-1.0.76`)

## Build from source

Because CrumbsLib is in a private repository, you need access to both projects.

1. Check out the `1.21.1-fabric` branch of CrumbsLib.
2. Run `./gradlew publishToMavenLocal` in CrumbsLib.
3. Check out the `1.21.1-fabric` branch of SlimeOverhaul.
4. Run `./gradlew clean build` in SlimeOverhaul.

The compiled Fabric mod JAR is located in `build/libs`. GitHub Actions performs the same build, using the repository secret `CRUMBSLIB_READ_TOKEN`.

## Configuration

The Fabric configuration is stored in `config/slimeoverhaul.json`. It is created with defaults on first launch. After editing the JSON file, restart the game or server.

## Upgrade materials

Upgrade costs are data-pack driven on Fabric:

`data/<namespace>/slimeoverhaul/upgrade_costs/<template_item_path>.json`

For example, the shipped Fire Template entry is at `data/slimeoverhaul/slimeoverhaul/upgrade_costs/fire_template.json`.

```json
{
  "itemStack": {
    "id": "slimeoverhaul:fire_slime_ball",
    "count": 1
  }
}
```

Upgrade cost files reload with server data packs. A default Fire Template material is used as fallback.

## Migration notes

- Fabric `ModInitializer` handles content registration, attributes, brewing recipes, trader offers and biome features.
- Combat and shield effects have Fabric event listeners and vanilla mixins in place of NeoForge events.
- Existing NeoForge biome modifiers and data map generation files are excluded; Fabric registers geodes with Fabric API and loads its own upgrade-cost resources.
- The committed generated recipes, tags, models and loot tables are included in the Fabric build.
- JEI and EMI use optional Fabric API integrations.

The Fabric CI workflow builds the mod, attempts a dedicated server startup check, and uploads the compiled JAR when all required checks pass.
