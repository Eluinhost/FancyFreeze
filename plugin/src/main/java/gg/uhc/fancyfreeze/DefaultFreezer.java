/*
 * Project: FancyFreeze
 * Class: gg.uhc.fancyfreeze.DefaultFreezer
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Graham Howden <graham_howden1 at yahoo.co.uk>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package gg.uhc.fancyfreeze;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import gg.uhc.fancyfreeze.api.CustomEffect;
import gg.uhc.fancyfreeze.api.Freezer;
import gg.uhc.fancyfreeze.api.nms.MovementspeedRemover;
import gg.uhc.fancyfreeze.events.GlobalFreezeEvent;
import gg.uhc.fancyfreeze.events.GlobalUnFreezeEvent;
import gg.uhc.fancyfreeze.events.PlayerFreezeEvent;
import gg.uhc.fancyfreeze.events.PlayerUnfreezeEvent;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DefaultFreezer implements Freezer {

    public static final String ANTIFREEZE = "uhc.fancyfreeze.antifreeze";

    protected static final String METADATA_KEY = "freeze location";
    protected static final String STAY_IN_BORDER = ChatColor.RED + "Stay close to your freeze position!";
    protected static final double TAU = 2 * Math.PI;

    protected final Plugin plugin;
    protected final FreezePotionsApplier potionApplier;
    protected final CustomEffect freezeEffect;
    protected final CustomEffect warpEffect;
    protected final MovementspeedRemover movementspeedRemover;

    protected final Set<UUID> alwaysFrozen = Sets.newHashSet();
    protected final Map<UUID, BukkitRunnable> particleSpawners = Maps.newHashMap();
    protected final double maxDistanceSq;

    protected boolean globallyFrozen = false;

    public DefaultFreezer(Plugin plugin, FreezePotionsApplier potionApplier, MovementspeedRemover movementspeedRemover, CustomEffect freezeEffect, CustomEffect warpEffect, double maxDistance) {
        this.plugin = plugin;
        this.movementspeedRemover = movementspeedRemover;
        this.freezeEffect = freezeEffect;
        this.potionApplier = potionApplier;
        this.warpEffect = warpEffect;
        this.maxDistanceSq = maxDistance * maxDistance;

        // start timer that keeps people in their boundaries
        new LocationResetterTask().runTaskTimer(plugin, 0, 20);
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
        if (hasAntifreezePermission(player)) return;

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

        Bukkit.getPluginManager().callEvent(new GlobalFreezeEvent());
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

        Bukkit.getPluginManager().callEvent(new GlobalUnFreezeEvent());
    }

    @Override
    public boolean isGloballyFrozen() {
        return globallyFrozen;
    }

    @Override
    public boolean isCurrentlyFrozen(UUID player) {
        return (isGloballyFrozen() || isAlwaysFrozen(player)) && !hasAntifreezePermission(player);
    }

    @Override
    public boolean hasAntifreezePermission(UUID player) {
        return Bukkit.getPlayer(player).hasPermission(ANTIFREEZE);
    }

    @Override
    public Set<Player> getOnlineFrozenPlayers() {
        if (globallyFrozen) {
            return Sets.newHashSet(Bukkit.getOnlinePlayers());
        }

        Set<Player> online = Sets.newHashSet();
        for (UUID frozen : alwaysFrozen) {
            Player player = Bukkit.getPlayer(frozen);
            if (player != null && !player.hasPermission(ANTIFREEZE)) online.add(player);
        }

        return online;
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        if (isCurrentlyFrozen(event.getPlayer().getUniqueId())) {
            freezePlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        stopParticleSpawning(event.getPlayer());
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
                startParticleSpawning(player, event.getTo());
                potionApplier.addPotions(player);
        }
    }

    protected Optional<Location> getFreezeLocation(Player player) {
        List<MetadataValue> meta = player.getMetadata(METADATA_KEY);

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
        if (player.hasPermission(ANTIFREEZE)) return;

        setFreezeLocation(player, player.getLocation());
        movementspeedRemover.applyReduction(player);
        potionApplier.addPotions(player);
        startParticleSpawning(player, player.getLocation());

        Bukkit.getPluginManager().callEvent(new PlayerFreezeEvent(player));
    }

    protected void unfreezePlayer(Player player) {
        removeFreezeLocation(player);
        movementspeedRemover.removeReduction(player);
        potionApplier.removePotions(player);
        stopParticleSpawning(player);

        Bukkit.getPluginManager().callEvent(new PlayerUnfreezeEvent(player));
    }

    protected void warpPlayerBack(Player player, Location location) {
        warpEffect.playAtLocation(player.getEyeLocation(), player);
        Location newLocation = location.clone();

        Location playerLocation = player.getLocation();
        double dX = playerLocation.getX() - location.getX();
        double dZ = playerLocation.getZ() - location.getZ();
        double theta = Math.atan2(-dX, dZ);

        newLocation.setYaw((float)Math.toDegrees((theta + TAU) % TAU));
        newLocation.setPitch(playerLocation.getPitch());

        player.teleport(newLocation);
        player.sendMessage(STAY_IN_BORDER);
    }

    protected void startParticleSpawning(Player player, Location centre) {
        stopParticleSpawning(player);

        BukkitRunnable spawner = new PlayerYTrackedParticles(freezeEffect, player, centre);
        spawner.runTaskTimerAsynchronously(plugin, 0, 20);

        particleSpawners.put(player.getUniqueId(), spawner);
    }

    protected void stopParticleSpawning(Player player) {
        BukkitRunnable runnable = particleSpawners.remove(player.getUniqueId());

        if (runnable != null) {
            runnable.cancel();
        }
    }

    class LocationResetterTask extends BukkitRunnable {
        @Override
        public void run() {
            for (Player player : getOnlineFrozenPlayers()) {
                Optional<Location> locationOptional = getFreezeLocation(player);

                if (!locationOptional.isPresent()) continue;

                Location location = locationOptional.get();
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

    static class PlayerYTrackedParticles extends BukkitRunnable {

        protected final CustomEffect particleEffect;
        protected final Player player;
        protected final Location location;

        public PlayerYTrackedParticles(CustomEffect particleEffect, Player player, Location location) {
            this.particleEffect = particleEffect;
            this.player = player;
            this.location = location;
        }

        @Override
        public void run() {
            Location at = location.clone();
            at.setY(player.getLocation().getY());

            particleEffect.playAtLocation(at, player);
        }
    }
}
