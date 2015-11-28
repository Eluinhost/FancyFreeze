package gg.uhc.fancyfreeze.announcer;

import gg.uhc.fancyfreeze.api.Freezer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

public class DefaultAnnouncer implements GlobalAnnouncer {
    protected static final String FORMAT = ChatColor.AQUA + "All players are now %s";

    protected final Freezer freezer;

    public DefaultAnnouncer(Freezer freezer) {
        this.freezer = freezer;
    }

    @Override
    public void announceState() {
        Bukkit.broadcastMessage(String.format(FORMAT, freezer.isGloballyFrozen() ? "frozen" : "unfrozen"));
    }
}
