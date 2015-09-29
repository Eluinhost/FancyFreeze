package gg.uhc.fancyfreeze.effects.wrappers;

import gg.uhc.fancyfreeze.api.CustomEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CombinationEffect implements CustomEffect {

    protected final CustomEffect[] effects;

    public CombinationEffect(CustomEffect... effects) {
        this.effects = effects;
    }

    @Override
    public void playAtLocation(Location location, Player... players) {
        for (CustomEffect effect : effects) {
            effect.playAtLocation(location, players);
        }
    }
}
