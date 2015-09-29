package gg.uhc.fancyfreeze;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import gg.uhc.fancyfreeze.api.CustomParticleEffect;
import gg.uhc.fancyfreeze.api.Freezer;
import gg.uhc.fancyfreeze.api.nms.FakePotionApplier;
import gg.uhc.fancyfreeze.api.nms.MovementspeedRemover;
import gg.uhc.fancyfreeze.particles.FixedLocationEffectRunnable;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DefaultFreezer implements Freezer {

    protected static final String METADATA_KEY = "freeze location";

    protected final Plugin plugin;
    protected final FakePotionApplier potionApplier;
    protected final CustomParticleEffect freezeEffect;
    protected final CustomParticleEffect warpEffect;
    protected final MovementspeedRemover movementspeedRemover;

    protected final Set<UUID> alwaysFrozen = Sets.newHashSet();
    protected final Map<UUID, FixedLocationEffectRunnable> particleSpawners = Maps.newHashMap();
    protected final List<PotionEffect> freezeEffects;
    protected final double maxDistanceSq;

    protected boolean globallyFrozen = false;

    public DefaultFreezer(Plugin plugin, FakePotionApplier potionApplier, MovementspeedRemover movementspeedRemover, CustomParticleEffect freezeEffect, CustomParticleEffect warpEffect, double maxDistance) {
        this.plugin = plugin;
        this.movementspeedRemover = movementspeedRemover;
        this.freezeEffect = freezeEffect;
        this.potionApplier = potionApplier;
        this.warpEffect = warpEffect;
        this.maxDistanceSq = maxDistance * maxDistance;

        freezeEffects = ImmutableList.<PotionEffect>builder()
                .add(new PotionEffect(PotionEffectType.JUMP, Short.MAX_VALUE, -Byte.MAX_VALUE))
                .add(new PotionEffect(PotionEffectType.SLOW, Short.MAX_VALUE, Byte.MAX_VALUE))
                .add(new PotionEffect(PotionEffectType.SLOW_DIGGING, Short.MAX_VALUE, 5))
                .build();

        // start timer that keeps people in their boundaries
        new LocationResetterTask().runTaskTimer(plugin, 0, 40);
    }

    @Override
    public boolean toggleAlwaysFreeze(UUID player) {
        if (isAlwaysFrozen(player)) {
            removeAlwaysFreeze(player);
            return false;
        } else {
            alwaysFreeze(player);
            return true;
        }
    }

    @Override
    public void alwaysFreeze(UUID player) {
        if (!alwaysFrozen.add(player)) return;

        // if it's global freeze atm skip freezing them
        // as they already have the right modifiers
        if (!isGloballyFrozen()) {
            Player p = Bukkit.getPlayer(player);
            if (p != null) freezePlayer(p);
        }
    }

    @Override
    public void removeAlwaysFreeze(UUID player) {
        if (!alwaysFrozen.remove(player)) return;

        // only remove effects if not in global freeze
        if (!isGloballyFrozen()) {
            Player p = Bukkit.getPlayer(player);
            if (p != null) unfreezePlayer(p);
        }
    }

    @Override
    public boolean isAlwaysFrozen(UUID player) {
        return alwaysFrozen.contains(player);
    }

    @Override
    public void clearAlwaysFreezeList() {
        for (UUID uuid : Sets.newHashSet(alwaysFrozen)) {
            removeAlwaysFreeze(uuid);
        }
    }

    @Override
    public boolean toggleGlobalFreeze() {
        if (isGloballyFrozen()) {
            disableGlobalFreeze();
            return false;
        } else {
            enableGlobalFreeze();
            return true;
        }
    }

    @Override
    public void enableGlobalFreeze() {
        if (globallyFrozen) return;

        globallyFrozen = true;

        for (Player player : Bukkit.getOnlinePlayers()) {
            // skip always frozen as they are already frozen
            // and we want to avoid multiple modifier problems
            if (!isAlwaysFrozen(player.getUniqueId())) {
                freezePlayer(player);
            }
        }
    }

    @Override
    public void disableGlobalFreeze() {
        if (!globallyFrozen) return;

        globallyFrozen = false;

        for (Player player : Bukkit.getOnlinePlayers()) {
            // skip always frozen so they remain frozen
            if (!isAlwaysFrozen(player.getUniqueId())) {
                unfreezePlayer(player);
            }
        }
    }

    @Override
    public boolean isGloballyFrozen() {
        return globallyFrozen;
    }

    @Override
    public boolean isCurrentlyFrozen(UUID player) {
        return isGloballyFrozen() || isAlwaysFrozen(player);
    }

    @Override
    public Set<Player> getOnlineFrozenPlayers() {
        if (globallyFrozen) {
            return Sets.newHashSet(Bukkit.getOnlinePlayers());
        }

        Set<Player> online = Sets.newHashSet();
        for (UUID frozen : alwaysFrozen) {
            Player player = Bukkit.getPlayer(frozen);
            if (player != null) online.add(player);
        }

        return online;
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        if (globallyFrozen || alwaysFrozen.contains(event.getPlayer().getUniqueId())) {
            freezePlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        stopParticleSpawning(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(PlayerTeleportEvent event) {
        if (!isCurrentlyFrozen(event.getPlayer().getUniqueId())) return;

        Player player = event.getPlayer();

        switch (event.getCause()) {
            case PLUGIN:
                // skip if it was to the same location as currently set, stops
                // resetting particles/metadata on border knockback
                if (event.getTo().equals(getFreezeLocation(player).get())) return;
            case COMMAND:
            case UNKNOWN:
                // set new location and reset particles
                setFreezeLocation(player, event.getTo());
                startParticleSpawning(player.getUniqueId(), event.getTo());
        }
    }

    protected Optional<Location> getFreezeLocation(Player player) {
        List<MetadataValue> meta = player.getMetadata(METADATA_KEY);

        if (meta.size() == 0) return Optional.absent();

        for (MetadataValue value : meta) {
            if (value.getOwningPlugin().equals(plugin)) return Optional.fromNullable((Location) value.value());
        }

        return Optional.absent();
    }

    protected void setFreezeLocation(Player player, Location location) {
        player.setMetadata(METADATA_KEY, new FixedMetadataValue(plugin, location));
    }

    protected void removeFreezeLocation(Player player) {
        player.removeMetadata(METADATA_KEY, plugin);
    }

    protected void freezePlayer(Player player) {
        setFreezeLocation(player, player.getLocation());
        movementspeedRemover.applyReduction(player);

        for (PotionEffect effect : freezeEffects) {
            potionApplier.addFakePotionEffect(player, effect);
        }

        startParticleSpawning(player.getUniqueId(), player.getLocation());
    }

    protected void unfreezePlayer(Player player) {
        removeFreezeLocation(player);
        movementspeedRemover.removeReduction(player);

        for (PotionEffect effect : freezeEffects) {
            potionApplier.removeFakePotionEffect(player, effect);
        }

        stopParticleSpawning(player.getUniqueId());
    }

    protected void warpPlayerBack(Player player, Location location) {
        player.teleport(location.clone().setDirection(player.getLocation().getDirection()));
        player.sendMessage(ChatColor.RED + "Stay within the boundary");
        warpEffect.playAtLocation(player.getLocation(), player);
    }

    protected void startParticleSpawning(UUID uuid, Location centre) {
        stopParticleSpawning(uuid);

        FixedLocationEffectRunnable spawner = new FixedLocationEffectRunnable(freezeEffect, centre);
        spawner.runTaskTimerAsynchronously(plugin, 0, 20);

        particleSpawners.put(uuid, spawner);
    }

    protected void stopParticleSpawning(UUID uuid) {
        FixedLocationEffectRunnable runnable = particleSpawners.remove(uuid);

        if (runnable != null) {
            runnable.cancel();
        }
    }

    class LocationResetterTask extends BukkitRunnable {
        @Override
        public void run() {
            for (Player player : getOnlineFrozenPlayers()) {
                List<MetadataValue> data = player.getMetadata(METADATA_KEY);

                if (data.size() == 0) continue;

                Object locationObject = data.get(0).value();

                if (!(locationObject instanceof Location)) continue;

                Location location = (Location) locationObject;
                Location playerLocation = player.getLocation();

                UUID locationWorldUUID = location.getWorld().getUID();
                UUID playerWorldUUID = playerLocation.getWorld().getUID();

                double dX = location.getX() - playerLocation.getX();
                double dZ = location.getZ() - playerLocation.getZ();

                double XZDistanceSq = (dX * dX) + (dZ * dZ);

                if (!locationWorldUUID.equals(playerWorldUUID) || XZDistanceSq > maxDistanceSq) {
                    warpPlayerBack(player, location);
                }
            }
        }
    }
}
