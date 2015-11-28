/*
 * Project: FancyFreeze
 * Class: gg.uhc.fancyfreeze.commands.FreezeCommand
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

package gg.uhc.fancyfreeze.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import gg.uhc.fancyfreeze.api.Freezer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FreezeCommand implements TabExecutor {

    protected static final Set<String> ARGUMENTS = ImmutableSet.of("toggle", "on", "off");

    protected static final String USAGE = ChatColor.RED + "Usage: /ff playerName [on|off]";
    protected static final String FROZEN = ChatColor.AQUA + "Player %s is now %s";
    protected static final String PLAYER_FROZEN = ChatColor.AQUA + "You have been %s";

    protected final Freezer freezer;

    public FreezeCommand(Freezer freezer) {
        this.freezer = freezer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(USAGE);
            return true;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        UUID uuid = player.getUniqueId();

        String type = "toggle";

        if (args.length > 1) {
            type = args[1].toLowerCase();
        }

        boolean frozen;
        switch (type) {
            case "toggle":
                frozen = freezer.toggleAlwaysFreeze(uuid);
                break;
            case "on":
                frozen = true;
                freezer.alwaysFreeze(uuid);
                break;
            case "off":
                frozen = false;
                freezer.removeAlwaysFreeze(uuid);
                break;
            default:
                sender.sendMessage(USAGE);
                return true;
        }

        sender.sendMessage(String.format(FROZEN, args[0], frozen ? "frozen" : "unfrozen"));

        if (player.isOnline()) {
            player.getPlayer().sendMessage(String.format(PLAYER_FROZEN, frozen ? "frozen" : "unfrozen"));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 2) {
            return ImmutableList.of();
        }

        if (args.length == 2) {
            List<String> results = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[1], ARGUMENTS, results);
            return results;
        }

        List<String> results = Lists.newArrayList();
        StringUtil.copyPartialMatches(args[0], FunctionUtil.getNamesOfAllOnline(), results);
        Collections.sort(results);

        return results;
    }
}
