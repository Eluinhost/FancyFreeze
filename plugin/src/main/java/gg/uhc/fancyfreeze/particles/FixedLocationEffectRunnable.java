package gg.uhc.fancyfreeze.particles;

import gg.uhc.fancyfreeze.api.CustomParticleEffect;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class FixedLocationEffectRunnable extends BukkitRunnable {

    protected final CustomParticleEffect particleEffect;
    protected final Location location;

    public FixedLocationEffectRunnable(CustomParticleEffect particleEffect, Location location) {
        this.particleEffect = particleEffect;
        this.location = location;
    }

    @Override
    public void run() {
        particleEffect.playAtLocation(location);
    }
}
