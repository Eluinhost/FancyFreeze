package gg.uhc.fancyfreeze;

import gg.uhc.fancyfreeze.api.nms.FakePotionApplier;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class FreezePotionsApplier {

    protected final FakePotionApplier applier;
    protected final List<PotionEffect> freezeEffects;

    public FreezePotionsApplier(FakePotionApplier applier, List<PotionEffect> freezeEffects) {
        this.applier = applier;
        this.freezeEffects = freezeEffects;
    }

    void addPotions(Player player) {
        for (PotionEffect effect : freezeEffects) {
            applier.addFakePotionEffect(player, effect);
        }
    }

    void removePotions(Player player) {
        for (PotionEffect effect : freezeEffects) {
            applier.removeFakePotionEffect(player, effect);
        }
    }
}
