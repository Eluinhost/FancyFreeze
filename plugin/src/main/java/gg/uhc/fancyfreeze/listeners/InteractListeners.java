package gg.uhc.fancyfreeze.listeners;

import gg.uhc.fancyfreeze.api.Freezer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListeners implements Listener {

    protected final Freezer freezer;

    public InteractListeners(Freezer freezer) {
        this.freezer = freezer;
    }

    @EventHandler(ignoreCancelled = true)
    public void on(PlayerInteractEvent event) {
        if (freezer.isCurrentlyFrozen(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void on(PlayerInteractEntityEvent event) {
        if (freezer.isCurrentlyFrozen(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
