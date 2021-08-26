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
        if(sender instanceof Player) {
            Player p =(Player)sender;
            if (args.length == 1) {
                List<String> suggest = new ArrayList<>();

                suggest.add("set");
                suggest.add("invite");
                suggest.add("uninvite");
                suggest.add("invited");
                suggest.add("delete");
                suggest.add("help");
                suggest.add("public");
                suggest.add("private");
                Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                Bukkit.getServer().getOnlinePlayers().toArray(players);
                for (Player pl : players) {
                    try {
                        if ((HomeStorage.getAnyHomeByName(pl.getName())).getInvitedNames().contains(p.getName())) {
                            suggest.add(pl.getName());
                        }
                    } catch (NullPointerException ex){

                    }
                }
                return suggest;
            } else if (args.length == 2 && args[0].toLowerCase(Locale.ROOT).trim().equals("uninvite")) {
                List<String> suggest;
                try {
                    suggest = Objects.requireNonNull(HomeStorage.getAnyHomeByName(p.getName())).getInvitedNames();
                } catch(NullPointerException ex){
                    return null;
                }
                return suggest;
            } else if(args.length==2 && args[0].toLowerCase(Locale.ROOT).trim().equals("delete")){
                List<String> suggest = new ArrayList<>();
                ArrayList<Home> homes;
                homes=HomeStorage.getHomeListName(p.getName());
                if(Objects.nonNull(homes)){
                    for(Home hm:homes){
                        suggest.add(hm.getHomeName());
                    }
                    return suggest;
                }
            }
        }

        return null;
    }
}
