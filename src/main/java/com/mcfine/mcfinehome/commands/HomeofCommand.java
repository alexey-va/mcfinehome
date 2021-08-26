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

        if(sender instanceof Player){
            Player p = (Player)sender;
            if(args.length==1){
                Home hm = HomeStorage.getHomeByName(args[0].toLowerCase(Locale.ROOT).trim(),"Main");
                if(Objects.isNull(hm)){
                    hm = HomeStorage.getAnyHomeByName(args[0].trim());
                    if(Objects.isNull(hm)) {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cУ игрока нет дома"));
                    } else {
                        if(hm.isPubl() || hm.isInvited(p.getName())){
                            try {
                                if(Teleporter.tryTp(p, hm.getLocation())) {
                                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eВы телепортировались в дом: &6" + hm.getHomeName() + " &eигрока: &6" + hm.getPlayerName()));
                                }
                            } catch(Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                } else{
                    if(hm.isPubl() || hm.isInvited(p.getName())){
                        try {
                            if(Teleporter.tryTp(p, hm.getLocation())) {
                                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eВы телепортировались в дом: &6" + hm.getHomeName() + " &eигрока: &6" + hm.getPlayerName()));
                            }
                        } catch(Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } else if(args.length==2){
                Home hm = HomeStorage.getHomeByName(args[0].toLowerCase(Locale.ROOT).trim(),args[1].toLowerCase(Locale.ROOT).trim());
                if(Objects.isNull(hm)){
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cУ игрока нет такого дома"));
                } else{
                    if(hm.isPubl() || hm.isInvited(p.getName())){
                        try {
                            Teleporter.tryTp(p, hm.getLocation());
                            p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eВы телепортировались в дом: &6"+hm.getHomeName()+" &eигрока: &6"+hm.getPlayerName()));
                        } catch(Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }

        return false;
    }
}
