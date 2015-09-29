package gg.uhc.fancyfreeze;

import gg.uhc.fancyfreeze.api.nms.MovementspeedRemover;
import org.bukkit.entity.LivingEntity;

public class DummyMovementspeedRemover implements MovementspeedRemover {
    @Override
    public void applyReduction(LivingEntity entity) {}

    @Override
    public void removeReduction(LivingEntity entity) {}
}
