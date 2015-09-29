package gg.uhc.fancyfreeze.nms.v1_8_R3;

import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEffect;
import net.minecraft.server.v1_8_R3.PacketPlayOutRemoveEntityEffect;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
