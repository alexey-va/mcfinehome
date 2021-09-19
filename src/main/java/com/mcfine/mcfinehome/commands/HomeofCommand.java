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

import java.util.Locale;
import java.util.Objects;

public class HomeofCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length >= 1 && args[0].trim().equalsIgnoreCase(p.getName())) {
                p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7▪ &cЭто же вы!"));
                return false;
            }
            if (args.length == 1) {
                Home hm = HomeStorage.getHome(args[0].trim(), "Main");
                if (Objects.isNull(hm)) {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7▪ &cУ игрока нет дома"));
                } else {
                    if (hm.isPubl() || hm.isInvited(p.getName())) {
                        try {
                            if (Teleporter.tryTp(p, hm.getLocation())) {
                                p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7▪ &eВы телепортировались в дом: &6" + hm.getHomeName() + " &eигрока: &6" + hm.getPlayerName()));
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7▪ &eВы не приглашены в этот дом"));
                    }
                }
            } else if (args.length == 2) {
                Home hm = HomeStorage.getHome(args[0].trim(), args[1].trim());
                if (Objects.isNull(hm)) {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7▪ &cУ игрока нет такого дома"));
                } else {
                    if (hm.isPubl() || hm.isInvited(p.getName())) {
                        try {
                            Teleporter.tryTp(p, hm.getLocation());
                            p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7▪ &eВы телепортировались в дом: &6" + hm.getHomeName() + " &eигрока: &6" + hm.getPlayerName()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }

        return false;
    }
}
