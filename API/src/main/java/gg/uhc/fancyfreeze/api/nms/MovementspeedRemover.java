package gg.uhc.fancyfreeze.api.nms;

import org.bukkit.entity.LivingEntity;

public interface MovementspeedRemover {
    void applyReduction(LivingEntity entity);
    void removeReduction(LivingEntity entity);
}
