package gg.uhc.fancyfreeze.effects.tasks;

import gg.uhc.fancyfreeze.api.CustomEffect;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.ref.WeakReference;

public class EntityLocationEffectRunnable extends BukkitRunnable {

    protected final CustomEffect particleEffect;
    protected final WeakReference<Entity> reference;

    public EntityLocationEffectRunnable(CustomEffect particleEffect, Entity entity) {
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
