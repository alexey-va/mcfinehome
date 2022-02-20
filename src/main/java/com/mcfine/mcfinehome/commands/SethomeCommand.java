package com.mcfine.mcfinehome.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class SethomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            String[] args2 = new String[args.length+1];
            args2[0] = "set";
            for (int i = 0, argsLength = args.length; i < argsLength; i++) {
                args2[i+1]=args[i].toLowerCase(Locale.ROOT).trim();
            }

            HomeCommand.setHome((Player)sender,args2);
        }
        return false;
    }

}