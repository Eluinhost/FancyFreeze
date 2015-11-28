/*
 * Project: FancyFreeze
 * Class: gg.uhc.fancyfreeze.nms.v1_8_R2.FakePotionApplier
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

package gg.uhc.fancyfreeze.nms.v1_8_R2;

import net.minecraft.server.v1_8_R2.MobEffect;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityEffect;
import net.minecraft.server.v1_8_R2.PacketPlayOutRemoveEntityEffect;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

public class FakePotionApplier implements gg.uhc.fancyfreeze.api.nms.FakePotionApplier {
    @Override
    public void addFakePotionEffect(Player player, PotionEffect effect) {
        MobEffect e = effectFromPotionEffect(effect);
        removeEffect(player, e);
        addEffect(player, e);
    }

    @Override
    public void removeFakePotionEffect(Player player, PotionEffect effect) {
        MobEffect e = effectFromPotionEffect(effect);
        removeEffect(player, e);

        // readd the existing ones
        Collection<PotionEffect> active = player.getActivePotionEffects();

        for (PotionEffect pe : active) {
            if (pe.getType().equals(effect.getType())) {
                addFakePotionEffect(player, pe);
            }
        }
    }

    protected MobEffect effectFromPotionEffect(PotionEffect effect) {
        return new MobEffect(
                effect.getType().getId(),
                effect.getDuration(),
                effect.getAmplifier(),
                effect.isAmbient(),
                effect.hasParticles()
        );
    }

    protected void addEffect(Player player, MobEffect effect) {
        PacketPlayOutEntityEffect removeEffect = new PacketPlayOutEntityEffect(player.getEntityId(), effect);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(removeEffect);
    }

    protected void removeEffect(Player player, MobEffect effect) {
        PacketPlayOutRemoveEntityEffect removeEffect = new PacketPlayOutRemoveEntityEffect(player.getEntityId(), effect);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(removeEffect);
    }
}
