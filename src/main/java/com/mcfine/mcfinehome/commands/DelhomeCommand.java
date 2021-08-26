package com.mcfine.mcfinehome.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class DelhomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player){

            for(int i=0, j=args.length;i<j;i++){
                args[i]=args[i].toLowerCase(Locale.ROOT).trim();
            }

            HomeCommand.delHome((Player)sender,args);
        }

        return false;
    }
}
