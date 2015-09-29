package gg.uhc.fancyfreeze.nms.v1_8_R3;

import net.minecraft.server.v1_8_R3.AttributeModifier;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public class MovementspeedRemover implements gg.uhc.fancyfreeze.api.nms.MovementspeedRemover {

    // a is setSaved(Boolean)
    protected static final AttributeModifier FREEZE_MODIFIER = new AttributeModifier("Frozen entity", -100, 2).a(false);

    @Override
    public void applyReduction(LivingEntity entity) {
        ((CraftLivingEntity) entity).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).b(FREEZE_MODIFIER);
    }

    @Override
    public void removeReduction(LivingEntity entity) {
        ((CraftLivingEntity) entity).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).c(FREEZE_MODIFIER);
    }
}
