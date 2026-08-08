# smp-core

A Paper plugin that provides  quality-of-life features for a survival multiplayer server.

## Features

### Homes

- `/sethome [name]` saves the player's current location.
- `/home [name]` teleports to a saved home.
- `/home` lists homes when the player has more than one saved home.
- `/delhome [name]` deletes a saved home.
- `/deletehome [name]` is an alias for `/delhome`.
- Home names are validated by the home manager and the maximum number of homes is controlled by `homes.max`.

### Teleport Requests

- `/tpa <player>` asks to teleport to another online player.
- `/tpaccept` accepts the latest pending request.
- `/tpyes` is an alias for `/tpaccept`.
- `/tpdeny` denies the latest pending request.
- `/tpno` is an alias for `/tpdeny`.
- Requests are only available when `features.teleport-requests` is enabled.

### Graves

- Creates a player-head grave when a player dies with drops or experience.
- Stores the player's dropped items and experience in a grave GUI.
- Cancels the normal item and XP drops after the grave is created.
- Only the grave owner can open the grave.
- Right-clicking or attempting to break the grave opens the grave GUI.
- When the grave inventory is emptied, the grave block is removed and stored experience is returned.
- Graves are persisted through the grave repository.

### Pets

- `/pets` opens a GUI of known tamed pets.
- `/pet` is an alias for `/pets`.
- Left-click a pet in the GUI to teleport to it.
- Right-click a pet in the GUI to teleport it to you.
- Tamed pets are remembered when chunks load.
- Shift-right-click a horse to inspect health, speed, jump height, and raw stat values.
- Low-health tamed pets teleport back to their owner when they take damage.
- Tamed wolves can automatically target nearby hostile mobs, excluding creepers.
- Pet kills can drop XP like player kills, heal the pet, and show the owner an action bar message.
- Loaded tamed pets can show holograms with name and health information.

### Holograms

- `/hologram create <id> <lines>` creates a saved text hologram at the player's view location.
- `/hologram add <id> <lines>` is an alias for create.
- `/hologram delete <id>` removes a saved hologram.
- `/hologram remove <id>` is an alias for delete.
- `/hologram list` lists saved holograms.
- `/hologram move <id>` moves a hologram to the player's view location.
- `/hologram tp <id>` and `/hologram teleport <id>` are aliases for move.
- `/hologram update <id> <lines>` updates hologram text.
- `/hologram edit`, `/hologram setlines`, and `/hologram lines` are aliases for update.
- Separate hologram lines are split with `//`.
- Holograms are saved by the hologram repository.

### Damage Holograms

- Shows temporary floating damage numbers when players or tamed pets damage entities.
- Damage text rises, fades out, and removes itself automatically.
- Controlled by `features.damage-holograms`.

### Inventory Helpers

- `/quickstack` moves matching inventory items into nearby chests.
- `/qs` is an alias for `/quickstack`.
- Quick stack only moves items into chests that already contain a similar item.
- Nearby chests are searched within `quick-stack.radius`.
- Sneak-left-clicking a chest sorts its contents by item type.

### Items

- `/rename <name>` renames the item in the player's main hand.
- `/lore clear` clears lore on the item in the player's main hand.
- `/lore setline <line> <text>` sets a lore line, with line numbers starting at 1.
- `/lore removeline <line>` removes a lore line.
- Rename and lore text lengths are limited by the `items` config values.

### World And Player Commands

- `/spawn` teleports to the current world's spawn.
- `/world` lists loaded worlds.
- `/world <world>` teleports to that world's spawn.
- `/gmc` switches the player to creative mode.
- `/gms` switches the player to survival mode.

### Double Doors

- Opening one door in a double-door pair opens the connected door too.
- Closing one door closes the connected door too.
- Iron doors are ignored.

### Recipes

- Registers custom recipes when `features.recipes` is enabled.
- Current furnace recipe: rotten flesh smelts into leather.
- The crafting recipe feature is present but currently registers no custom crafting recipes.

## Configuration

Default `config.yml`:

```yaml
features:
  homes: true
  graves: true
  holograms: true
  damage-holograms: true
  double-doors: true
  chest-sort: true
  quick-stack: true
  teleport-requests: true
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

Notes:

- Numeric config values are clamped to safe minimums in `Settings`.
- Feature flags are read on plugin startup.
- Disable a feature by setting its `features.<name>` value to `false`.

## Permissions

| Permission | Default | Covers |
| --- | --- | --- |
| `core.homes` | Everyone | `/home`, `/sethome`, `/delhome`, `/deletehome` |
| `core.tpa` | Everyone | `/tpa`, `/tpaccept`, `/tpyes`, `/tpdeny`, `/tpno` |
| `core.quickstack` | Everyone | `/quickstack`, `/qs` |
| `core.pets` | Everyone | `/pets`, `/pet` |
| `core.spawn` | Everyone | `/spawn` |
| `core.rename` | OP | `/rename` |
| `core.lore` | OP | `/lore` |
| `core.holograms` | OP | `/hologram`, `/holograms` |
| `core.world` | OP | `/world` |
| `core.gamemode` | OP | `/gmc`, `/gms` |

## Building

If `gradle.properties` contains `targetDirectory`, the `jar` task writes directly to that folder.

Example:

```properties
targetDirectory=C:\\Users\\john\\Desktop\\Minecraft\\SMP\\plugins
```
