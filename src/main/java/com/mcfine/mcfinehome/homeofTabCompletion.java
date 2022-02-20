package com.mcfine.mcfinehome;

import com.mcfine.mcfinehome.data.Home;
import com.mcfine.mcfinehome.utils.HomeStorage;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class homeofTabCompletion implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 1) {
                List<String> suggest = new ArrayList<>();
                Map<String, ArrayList<Home>> tmp = HomeStorage.getHomeSet();
                Collection<ArrayList<Home>> pHomeList = tmp.values();
                ArrayList<String> pls = new ArrayList<>();

                for (ArrayList<Home> array : pHomeList) {
                    if (array == null) continue;
                    for (Home home : array) {
                        if (home == null) continue;
                        if ((home.isInvited(p.getName()) || home.isPubl() || p.hasPermission("mcfinehome.admin"))&& (home.getPlayerName().contains(args[0]))) {
                            suggest.add(home.getPlayerName());
                        }
                    }
                }
                return suggest;
            }
        }

        return null;
    }
}
