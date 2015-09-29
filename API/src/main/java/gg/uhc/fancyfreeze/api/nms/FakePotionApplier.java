package gg.uhc.fancyfreeze.api.nms;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public interface FakePotionApplier {
    void addFakePotionEffect(Player player, PotionEffect effect);
    void removeFakePotionEffect(Player player, PotionEffect effect);
}