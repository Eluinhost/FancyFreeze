package gg.uhc.fancyfreeze.particles;

import gg.uhc.fancyfreeze.api.CustomParticleEffect;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.ref.WeakReference;

public class EntityLocationEffectRunnable extends BukkitRunnable {

    protected final CustomParticleEffect particleEffect;
    protected final WeakReference<Entity> reference;

    public EntityLocationEffectRunnable(CustomParticleEffect particleEffect, Entity entity) {
        this.particleEffect = particleEffect;
        this.reference = new WeakReference<>(entity);
    }

    @Override
    public void run() {
        Entity entity = reference.get();

        if (entity == null || entity.isDead()) return;

        particleEffect.playAtLocation(entity.getLocation());
    }
}
