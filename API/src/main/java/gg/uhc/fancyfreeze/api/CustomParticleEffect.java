package gg.uhc.fancyfreeze.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface CustomParticleEffect {
    void playAtLocation(Location location, Player... players);
}
