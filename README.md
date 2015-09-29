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

Stepping outside of the boundary will teleport the player back to the initial freeze location with some more particle
effects. All particle effects are only visible to the player that is frozen.

A player that is frozen will also be sent 'fake' potion effects depending on configuration: slowness, jump boost, 
blindness and mining fatigue. When they are unfrozen these potion effects are removed and any overwritten potion effects 
are sent back to the player.

Players with `uhc.fancyfreeze.antifreeze` (default op) will be unaffected by freeze commands

# Commands

## `/ff <name> [on|off]`

Freeze/unfreeze a specific player

`<name>` - the player to be frozen/unfrozen

`[on|off]` - optional, if not supplied will toggle.

When a player is frozen with this method they will not be unfrozen with global unfreeze `/ffg` and will only be unfrozen
via this command

Permission: `uhc.fancyfreeze.command.player` default OP

## `/ffg [on|off]`

Sets global freeze on/off. If on/off is not supplied it will toggle instead. All players are frozen + any new logins.

Permission: `uhc.fancyfreeze.command.global` default OP

# Configuration

```yaml
remove jumping: true
remove movement speed: false
add mining fatigue: true
add blindness: false
use particles: true
max distance: 5
```

`remove jumping` whether to send fake jump potion effect to disable jumping

`remove movement speed` whether to send fake slow potion effect and will have their movement speed set to 0 server side

`add mining fatigue` whether to send fake mining fatigue potion effect. NOTE block breaking is already cancelled, this 
is a client visual thing

`add blindness` whether to send fake blindness effect

`use particles` whether to render particles to players

`max distance` the distance before warping back to freeze location + how far out to render particles