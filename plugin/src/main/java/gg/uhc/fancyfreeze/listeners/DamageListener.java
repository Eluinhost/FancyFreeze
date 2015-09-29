package gg.uhc.fancyfreeze.listeners;

import gg.uhc.fancyfreeze.api.Freezer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class DamageListener implements Listener {

    protected final Freezer freezer;

    /**
     * Cancels all player damage (given and taken) when frozen
     */
    public DamageListener(Freezer freezer) {
        this.freezer = freezer;
    }

    @EventHandler(ignoreCancelled = true)
    public void on(EntityDamageEvent event) {

        Entity damaged = event.getEntity();

        if (damaged instanceof Player && freezer.isCurrentlyFrozen(damaged.getUniqueId())) {
            event.setCancelled(true);
        }

        if (event instanceof EntityDamageByEntityEvent) {
            Entity entity = ((EntityDamageByEntityEvent) event).getDamager();

            if (entity instanceof Player && freezer.isCurrentlyFrozen(entity.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void on(EntityShootBowEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && freezer.isCurrentlyFrozen(entity.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
