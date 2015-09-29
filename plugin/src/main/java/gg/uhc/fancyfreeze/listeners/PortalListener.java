package gg.uhc.fancyfreeze.listeners;

import gg.uhc.fancyfreeze.api.Freezer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PortalListener implements Listener {

    protected final Freezer freezer;

    /**
     * Cancels dimension transportation when frozen
     */
    public PortalListener(Freezer freezer) {
        this.freezer = freezer;
    }

    @EventHandler(ignoreCancelled = true)
    public void on(PlayerPortalEvent event) {
        if (freezer.isCurrentlyFrozen(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
