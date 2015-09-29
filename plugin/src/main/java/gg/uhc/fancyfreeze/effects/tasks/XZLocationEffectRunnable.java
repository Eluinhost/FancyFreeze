package gg.uhc.fancyfreeze.effects.tasks;

import gg.uhc.fancyfreeze.api.CustomEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.ref.WeakReference;

public class XZLocationEffectRunnable extends BukkitRunnable {

    protected final WeakReference<Entity> reference;
    protected final CustomEffect particleEffect;
    protected final Location location;

    public XZLocationEffectRunnable(CustomEffect particleEffect, Entity entity, Location location) {
        this.particleEffect = particleEffect;
        this.reference = new WeakReference<>(entity);
        this.location = location;
    }

    @Override
    public void run() {
        Entity entity = reference.get();

        Location at = location.clone();
        if (entity != null) {
            at.setY(entity.getLocation().getY());
        }

        particleEffect.playAtLocation(at);
    }
}
