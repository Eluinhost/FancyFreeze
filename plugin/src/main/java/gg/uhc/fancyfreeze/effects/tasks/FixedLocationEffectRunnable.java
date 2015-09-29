package gg.uhc.fancyfreeze.effects.tasks;

import gg.uhc.fancyfreeze.api.CustomEffect;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class FixedLocationEffectRunnable extends BukkitRunnable {

    protected final CustomEffect particleEffect;
    protected final Location location;

    public FixedLocationEffectRunnable(CustomEffect particleEffect, Location location) {
        this.particleEffect = particleEffect;
        this.location = location;
    }

    @Override
    public void run() {
        particleEffect.playAtLocation(location);
    }
}
