/*
 * Project: FancyFreeze
 * Class: gg.uhc.fancyfreeze.commands.GlobalFreezeCommand
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
import gg.uhc.fancyfreeze.announcer.GlobalAnnouncer;
import gg.uhc.fancyfreeze.api.Freezer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import java.util.List;
import java.util.Set;

public class GlobalFreezeCommand implements TabExecutor {

    protected static final Set<String> ARGUMENTS = ImmutableSet.of("toggle", "on", "off");

    protected static final String USAGE = ChatColor.RED + "Usage: /ffg [on|off]";

    protected final GlobalAnnouncer announcer;
    protected final Freezer freezer;

    public GlobalFreezeCommand(GlobalAnnouncer announcer, Freezer freezer) {
        this.announcer = announcer;
        this.freezer = freezer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        String type = "toggle";

        if (args.length > 0) {
            type = args[0].toLowerCase();
        }

        switch (type) {
            case "toggle":
                freezer.toggleGlobalFreeze();
                break;
            case "on":
                freezer.enableGlobalFreeze();
                break;
            case "off":
                freezer.disableGlobalFreeze();
                break;
            default:
                sender.sendMessage(USAGE);
                return true;
        }

        announcer.announceState();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 1) {
            return ImmutableList.of();
        }

        List<String> results = Lists.newArrayList();
        StringUtil.copyPartialMatches(args[0], ARGUMENTS, results);
        return results;
    }
}
