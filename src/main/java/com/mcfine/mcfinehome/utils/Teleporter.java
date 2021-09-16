package com.mcfine.mcfinehome.utils;

import com.mcfine.mcfinehome.McfineHome;
import com.mcfine.mcfinehome.data.Home;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Teleporter {

    public static int tryTpAny(Player p){
        ArrayList<Home> homeList = HomeStorage.getHomeList(p.getName());
        if(homeList == null || homeList.size()==0) return 0;
        else{

            Home main = null;
            for(Home home : homeList){
                if(home.getHomeName().equalsIgnoreCase("main")){
                    main = home;
                    break;
                }
            }
            if(main != null) {
                if (isSafe(main.getLocation())) {
                    p.teleportAsync(main.getLocation());
                    p.sendMessage(ColorTranslator.translateColorCodes("Телепортация к главному дому"));
                    return 1;
                }
            }

            for(Home home : homeList){
                if(isSafe(home.getLocation())){
                    p.teleportAsync(home.getLocation());
                    p.sendMessage(ColorTranslator.translateColorCodes("Телепортация к "+home.getHomeName()));
                    return 1;
                }
            }
        }
        return -1;
    }

    public static boolean tryTp(Player p, Location loc){
        if(!isSafe(loc)){
            p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cНебезопасная локация"));
            return false;
        }
        try {
            p.teleportAsync(loc);
            return true;
        } catch(Exception ex){
            p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cПроизошла ошибка"));
        }

        return false;
    }

    public static boolean isSafe(Location location){
        try {
            Block feet = location.getBlock();
            if (feet.getType().isSolid() && feet.getLocation().add(0, 1, 0).getBlock().getType().isSolid()) {
                return false; // not transparent (will suffocate)
            }
            Block head = feet.getRelative(BlockFace.UP);
            if (head.getType().isSolid()) {
                return false; // not transparent (will suffocate)
            }
            Block ground = feet.getRelative(BlockFace.DOWN);
            if(ground.getType().equals(Material.LAVA)||feet.getType().equals(Material.LAVA)||head.getType().equals(Material.LAVA)||head.getType().equals(Material.WATER)){
                return false;
            }
            if(ground.getType().isSolid()){
                return true;
            } else if(ground.getLocation().add(0,-1,0).getBlock().getType().isSolid()){
                return true;
            } else if(!ground.getLocation().add(0,-1,0).getBlock().getType().equals(Material.LAVA) && ground.getLocation().add(0,-2,0).getBlock().getType().isSolid()){
                return true;
            }
            // returns if the ground is solid or not.
            return ground.getType().isSolid();
        } catch (Exception err) {
            McfineHome.getPlugin().getLogger().info("Not safe");
        }
        return false;
    }

}
