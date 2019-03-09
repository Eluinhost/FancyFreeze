/*
 * Project: FancyFreeze
 * Class: MovementspeedRemover
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

package gg.uhc.fancyfreeze.nms.v1_12_R1;

import net.minecraft.server.v1_12_R1.AttributeModifier;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
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
