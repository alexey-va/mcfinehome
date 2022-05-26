package com.mcfine.mcfinehome.commands;

import com.mcfine.mcfinehome.data.Home;
import com.mcfine.mcfinehome.utils.HomeStorage;
import com.mcfine.mcfinehome.utils.Teleporter;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class TohomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("mcfinehome.home")) {
                p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7» &cУ вас нет доступа к данной команде!"));
                return false;
            }
            ArrayList<Home> homeList = HomeStorage.getHomeList(p.getName());
            if (Objects.isNull(homeList) || homeList.size() == 0) {
                p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7» &cУ вас нет дома!"));
                return false;
            }
            int result = Teleporter.tryTpAny(p);
            if (result == -1) {
                p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7» &cВаш дом не безопасен! Введите &7/home confirm &cдля телепортации!"));
            } else if (result == 0) {
                p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7» &cУ вас нет дома!"));
            }
        }
        return true;
    }
}
