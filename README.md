FancyFreeze
===========

Fancy freeze commands.  

A frozen player cannot:

- Interact with blocks/entities
- Deal or receive damage
- Use portals
- Drink or be splashed by potions

The player will be surrounded by fancy particles:

[Preview](http://gfycat.com/VictoriousBelovedAndalusianhorse)

Stepping outside of the boundary will teleport the player back to the initial freeze location.

A player that is frozen will also be sent 'fake' potion effects: slowness, jump boost and mining fatigue. When they are
unfrozen these potion effects are removed and any overwritten potion effects are sent back to the player.

# Commands

## `/ff <name> [on|off]`

Freeze/unfreeze a specific player

`<name>` - the player to be frozen/unfrozen

`[on|off]` - optional, if not supplied will toggle.

When a player is frozen with this method they will not be unfrozen with global unfreeze `/ffg` and will only be unfrozen
via this command

Permission: `uhc.freeze.command.player` default OP

## `/ffg [on|off]`

Sets global freeze on/off. If on/off is not supplied it will toggle instead. All players are frozen + any new logins.

Permission: `uhc.freeze.command.global` default OP