# smp-core

A small core plugin for a Minecraft survival multiplayer server.

`smp-core` bundles the quality-of-life features I want on my SMP into one Paper plugin: homes, graves, pets, item tools, holograms, storage helpers, and small world interactions.

## Features

### Homes

- `/sethome [name]` saves your current location.
- `/home [name]` teleports to a saved home.
- `/delhome [name]` or `/deletehome [name]` deletes a saved home.
- Home names are validated and limited by `homes.max`.

### Graves

- Creates a grave when a player dies with items or experience.
- Stores the player's drops and experience in a grave GUI.
- Only the grave owner can open their grave.
- When the grave is emptied, the grave block is removed and stored experience is returned.

### Pets

- `/pets` or `/pet` opens a GUI of known pets.
- Left click a pet to teleport to it.
- Right click a pet to teleport it to you.
- Known pets are saved so unloaded pets can be found again once their chunk is loaded.
- Tamed wolves can automatically target nearby hostile mobs.
- Pet kills can drop XP like player kills.
- Pets heal on kill, capped at max health.
- Pet kills send the owner an action bar message.
- Loaded tamed pets show mounted holograms with their name and health bar.
- If a pet has no custom name, its hologram uses the owner fallback, such as `Billy's wolf`.

### Holograms

- `/hologram create <id> <lines>` creates a saved text hologram.
- `/hologram delete <id>` removes a saved hologram.
- `/hologram list` lists saved holograms.
- `/hologram move <id>` moves a hologram to your view position.
- `/hologram update <id> <lines>` updates hologram text.
- Separate lines are split with `//`.
- Holograms are saved to `holograms.yml`.

### Damage Holograms

- Shows temporary floating damage numbers when players or tamed pets damage entities.
- Damage text rises, fades out, and removes itself automatically.

### Inventory Helpers

- `/quickstack` or `/qs` moves matching inventory items into nearby chests.
- Chest sorting lets players sort chest contents through the chest interaction feature.

### Items

- `/rename <name>` renames the item in your hand.
- `/lore clear` clears item lore.
- `/lore setline <line> <text>` sets a lore line.
- `/lore removeline <line>` removes a lore line.

### World And Player Commands

- `/spawn` teleports to the current world's spawn.
- `/world` lists worlds.
- `/world <world>` teleports to a world spawn.
- `/gmc` switches to creative mode.
- `/gms` switches to survival mode.

### Double Doors

- Opening one door in a double-door pair opens the connected door too.
- Closing one door closes the connected door too.
- Iron doors are ignored.

### Recipes

- Includes a recipe feature area for adding custom crafting and furnace recipes.

## Configuration

```yaml
features:
  homes: true
  graves: true
  holograms: true
  damage-holograms: true
  double-doors: true
  chest-sort: true
  quick-stack: true
  pets: true
  recipes: true

homes:
  max: 5

quick-stack:
  radius: 5

pets:
  auto-attack-radius: 12
  hologram-radius: 32
  heal-on-kill: 4.0

items:
  max-rename-length: 64
  max-lore-line-length: 128
```

## Permissions

| Permission | Default | Description |
| --- | --- | --- |
| `core.homes` | Everyone | Use `/home`, `/sethome`, and `/delhome`. |
| `core.quickstack` | Everyone | Use `/quickstack` and `/qs`. |
| `core.pets` | Everyone | Use `/pets` and `/pet`. |
| `core.spawn` | Everyone | Use `/spawn`. |
| `core.rename` | Configured by command registration | Use `/rename`. |
| `core.lore` | OP | Use `/lore`. |
| `core.holograms` | OP | Manage holograms. |
| `core.world` | OP | Use `/world`. |
| `core.gamemode` | OP | Use `/gmc` and `/gms`. |

## Building

Build the plugin jar with Gradle:

```powershell
.\gradlew.bat jar
```

The jar is named `Core.jar`.

If `gradle.properties` contains `targetDirectory`, the jar task writes directly to that folder. Example:

```properties
targetDirectory=C:\\Users\\billy\\Desktop\\Minecraft\\Lucie SMP\\plugins
```