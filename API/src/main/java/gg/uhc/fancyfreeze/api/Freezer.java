package gg.uhc.fancyfreeze.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Set;
import java.util.UUID;

public interface Freezer extends Listener {

    boolean toggleAlwaysFreeze(UUID player);
    void alwaysFreeze(UUID player);
    void removeAlwaysFreeze(UUID player);
    boolean isAlwaysFrozen(UUID player);
    void clearAlwaysFreezeList();

    boolean toggleGlobalFreeze();
    void enableGlobalFreeze();
    void disableGlobalFreeze();
    boolean isGloballyFrozen();

    boolean isCurrentlyFrozen(UUID player);

    Set<Player> getOnlineFrozenPlayers();
}