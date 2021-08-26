package com.mcfine.mcfinehome.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcfine.mcfinehome.McfineHome;
import com.mcfine.mcfinehome.data.Home;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class HomeStorage {

    private static ArrayList<Home> homeSet = new ArrayList<>();

    public static boolean createHome(Player p, String homeName){

        Home tmp = getHomeByUID(p.getUniqueId().toString(), homeName);
        if(Objects.nonNull(tmp)) return false;

        Home home = new Home(p, homeName);
        homeSet.add(home);

        return true;


    }

    public static void putHome(Home home){
        if(!homeSet.contains(home)) {
            homeSet.add(home);
        }
    }


    public static Home getHomeByName(String name,String homeName){
        for(Home home:homeSet){
                if (home.getPlayerName().equalsIgnoreCase(name) && home.getHomeName().equalsIgnoreCase(homeName)) {
                    return home;
                }
        }
        McfineHome.getPlugin().getLogger().info("[Home] Home was not found by player name: "+name);
        return null;
    }

    public static int getHomeNumber(String uid){
        int n=0;
        for(Home home:homeSet){
            if(home.getPlayerUid().equals(uid)){
                n++;
            }
        }
        return n;
    }

    public static ArrayList<Home> getHomeList(String uid){
        ArrayList<Home> list= new ArrayList<>();
        for(Home hm:homeSet){
            if(hm.getPlayerUid().equals(uid)) {
                list.add(hm);
            }
        }
        if(list.size()==0){
            return null;
        }
        return list;
    }

    public static ArrayList<Home> getHomeListName(String name){
        ArrayList<Home> list= new ArrayList<>();
        for(Home hm:homeSet){
            if(hm.getPlayerName().equalsIgnoreCase(name)) {
                list.add(hm);
            }
        }
        if(list.size()==0){
            return null;
        }
        return list;
    }


    public static Home getHomeByUID(String uid, String homeName){
        for(Home home:homeSet){
                if (home.getPlayerUid().equalsIgnoreCase(uid) && home.getHomeName().equalsIgnoreCase(homeName)) {
                    return home;
                }
        }
        McfineHome.getPlugin().getLogger().info("[Home] No home was not found by UID: "+uid);
        return null;
    }

    public static Home getAnyHomeByUID(String uid){
        for(Home home:homeSet){
            if (home.getPlayerUid().equalsIgnoreCase(uid)) {
                return home;
            }
        }
        McfineHome.getPlugin().getLogger().info("[Home] No home was not found by UID: "+uid);
        return null;
    }

    public static Home getAnyHomeByName(String name){
        for(Home home:homeSet){
            if (home.getPlayerName().equalsIgnoreCase(name)) {
                return home;
            }
        }
        return null;
    }


    public static void deleteHome(String uid, String homeName){
        for(Home home:homeSet){
            if(home.getPlayerUid().equalsIgnoreCase(uid) && home.getHomeName().equalsIgnoreCase(homeName)){
                homeSet.remove(home);
                break;
            }
        }
        McfineHome.getPlugin().getLogger().info("[Home] Error deleting home by UID: "+uid);
    }

    public static void deleteHomeName(String name, String homeName){
        for(Home home:homeSet){
            if(home.getPlayerName().equalsIgnoreCase(name) && home.getHomeName().equalsIgnoreCase(homeName)){
                homeSet.remove(home);
                break;
            }
        }
        McfineHome.getPlugin().getLogger().info("[Home] Error deleting home by NAME: "+name);
    }


    public static void replaceHome(String uid, Home newHome, String homeName){
        for(Home home:homeSet){
            if(home.getPlayerUid().equalsIgnoreCase(uid) && home.getHomeName().equalsIgnoreCase(homeName)){
                home.setHomeName(newHome.getHomeName());
                home.setInvitedNames(newHome.getInvitedNames());
                home.setPitch(newHome.getPitch());
                home.setYaw(newHome.getYaw());
                home.setWorld(newHome.getWorld());
                home.setX(newHome.getX());
                home.setY(newHome.getY());
                home.setZ(newHome.getZ());
                home.setPubl(newHome.isPubl());
                break;
            }
        }


    }

    public static void replaceHomeName(String name, Home newHome, String homeName){
        for(Home home:homeSet){
            if(home.getPlayerName().equalsIgnoreCase(name) && home.getHomeName().equalsIgnoreCase(homeName)){
                home.setHomeName(newHome.getHomeName());
                home.setInvitedNames(newHome.getInvitedNames());
                home.setPitch(newHome.getPitch());
                home.setYaw(newHome.getYaw());
                home.setWorld(newHome.getWorld());
                home.setX(newHome.getX());
                home.setY(newHome.getY());
                home.setZ(newHome.getZ());
                home.setPubl(newHome.isPubl());
                break;
            }
        }


    }


    public static void saveHome() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(McfineHome.getPlugin().getDataFolder().getAbsolutePath()+"/homes.json");

        file.getParentFile().mkdir();
        file.createNewFile();
        Writer writer = new FileWriter(file, false);
        gson.toJson(homeSet,writer);
        writer.flush();
        writer.close();
        McfineHome.getPlugin().getLogger().info("[Home] Data saved");
    }


    public static void loadHome() throws IOException{
        Gson gson = new Gson();
        File file = new File(McfineHome.getPlugin().getDataFolder().getAbsolutePath()+"/homes.json");
        if(file.exists()){
            Reader reader = new FileReader(file);
            Home[] n =gson.fromJson(reader, Home[].class);
            homeSet = new ArrayList<>(Arrays.asList(n));
            McfineHome.getPlugin().getLogger().info("[Home] Data uploaded");
        }
    }

}
