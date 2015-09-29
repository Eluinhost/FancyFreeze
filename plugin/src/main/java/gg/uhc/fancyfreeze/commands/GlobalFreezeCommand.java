package gg.uhc.fancyfreeze.commands;

import gg.uhc.fancyfreeze.api.Freezer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GlobalFreezeCommand implements CommandExecutor {

    protected static final String USAGE = ChatColor.RED + "Usage: /ffg [on|off]";
    protected static final String FROZEN = ChatColor.AQUA + "All players are now %s";

    protected final Freezer freezer;

    public GlobalFreezeCommand(Freezer freezer) {
        this.freezer = freezer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        String type = "toggle";

        if (args.length > 0) {
            type = args[0].toLowerCase();
        }

        boolean frozen;
        switch (type) {
            case "toggle":
                frozen = freezer.toggleGlobalFreeze();
                break;
            case "on":
                frozen = true;
                freezer.enableGlobalFreeze();
                break;
            case "off":
                frozen = false;
                freezer.disableGlobalFreeze();
                break;
            default:
                sender.sendMessage(USAGE);
                return true;
        }

        Bukkit.broadcastMessage(String.format(FROZEN, frozen ? "frozen" : "unfrozen"));
        return true;
    }
}
