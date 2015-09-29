package gg.uhc.fancyfreeze.listeners;

import gg.uhc.fancyfreeze.api.Freezer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PotionListener implements Listener {

    protected final Freezer freezer;

    /**
     * Cancels potion effects on frozen players playing around with freeze effects
     */
    public PotionListener(Freezer freezer) {
        this.freezer = freezer;
    }

    @EventHandler(ignoreCancelled = true)
    public void on(PotionSplashEvent event) {
        for (LivingEntity entity : event.getAffectedEntities()) {
            if (!(entity instanceof Player)) continue;

            if (freezer.isCurrentlyFrozen(entity.getUniqueId())) {
                event.setIntensity(entity, 0);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void on(PlayerItemConsumeEvent event) {
        if (freezer.isCurrentlyFrozen(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
