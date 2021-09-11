package com.mcfine.mcfinehome.commands;

import com.mcfine.mcfinehome.menus.MainMenu;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            try {
                MenuManager.openMenu(MainMenu.class,p);
            } catch (MenuManagerException e) {
                e.printStackTrace();
            } catch (MenuManagerNotSetupException e) {
                e.printStackTrace();
                p.sendMessage("Error, сообщите админу");
            }
        }
        return false;
    }
}
