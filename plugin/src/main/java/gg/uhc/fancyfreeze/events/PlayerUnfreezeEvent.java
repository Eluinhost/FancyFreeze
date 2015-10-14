package gg.uhc.fancyfreeze.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerUnfreezeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    protected final Player player;

    public PlayerUnfreezeEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
