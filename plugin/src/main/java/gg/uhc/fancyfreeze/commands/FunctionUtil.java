package gg.uhc.fancyfreeze.commands;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class FunctionUtil {

    public static final Function<OfflinePlayer, String> GET_PLAYER_NAME = new Function<OfflinePlayer, String>() {
        @Override
        public String apply(OfflinePlayer input) {
            return input == null ? null : input.getName();
        }
    };

    public static Iterable<String> getNamesOfAllOnline() {
        return Iterables.transform(Bukkit.getOnlinePlayers(), GET_PLAYER_NAME);
    }
}
