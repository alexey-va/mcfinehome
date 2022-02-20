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

public class TabCompletion implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 1) {
                List<String> suggest = new ArrayList<>();
                if("set".contains(args[0])) suggest.add("set");
                if("invite".contains(args[0])) suggest.add("invite");
                if("uninvite".contains(args[0])) suggest.add("uninvite");
                if("invites".contains(args[0])) suggest.add("invites");
                if("delete".contains(args[0])) suggest.add("delete");
                if("help".contains(args[0])) suggest.add("help");
                if("public".contains(args[0])) suggest.add("public");
                if("private".contains(args[0])) suggest.add("private");
                //if("list".contains(args[0])) suggest.add("list");
                //suggest.add("rename");
                Map<String, ArrayList<Home>> tmp = HomeStorage.getHomeSet();
                Collection<ArrayList<Home>> pHomeList = tmp.values();
                ArrayList<String> pls = new ArrayList<>();

                for (ArrayList<Home> array : pHomeList) {
                    if (array == null) continue;
                    for (Home home : array) {
                        if (home == null) continue;
                        if ((home.isInvited(p.getName()) || home.isPubl() || p.hasPermission("mcfinehome.admin")) && (home.getPlayerName().contains(args[0]))) {
                            suggest.add(home.getPlayerName());
                        }
                    }
                }
                return suggest;
            } else if (args.length == 2 && args[0].toLowerCase(Locale.ROOT).trim().equals("uninvite")) {
                List<String> suggest = null;
                Home main = HomeStorage.getHome(p.getName(), "Main");
                if (main != null) {
                    suggest = main.getInvitedNames();
                }
                return suggest;
            } /*else if (args.length == 2 && (args[0].trim().equalsIgnoreCase("delete") || args[0].trim().equalsIgnoreCase("rename")
                    || args[0].trim().equalsIgnoreCase("public") || args[0].trim().equalsIgnoreCase("private"))) {
                List<String> suggest = new ArrayList<>();
                ArrayList<String> homes = HomeStorage.getHomeNamesList(p.getName());
                return homes;
            }*/
        }

        return null;
    }
}
