/*
 * Project: FancyFreeze
 * Class: gg.uhc.fancyfreeze.listeners.PotionListener
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
