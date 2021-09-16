package com.mcfine.mcfinehome.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcfine.mcfinehome.McfineHome;
import com.mcfine.mcfinehome.data.Home;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.io.*;
import java.util.*;

public class HomeStorage {

    private static Map<String, ArrayList<Home>> homeSet = new HashMap<>();

    public static boolean createHome(Player player, String homeName) {
        if (homeSet.containsKey(player.getName().toLowerCase())) {
            if (!containsHome(player.getName().toLowerCase(), homeName)) {
                homeSet.get(player.getName().toLowerCase()).add(new Home(player, homeName));
                return true;
            } else {
                return false;
            }

        } else {
            ArrayList<Home> homeList = new ArrayList<>();
            homeList.add(new Home(player, homeName));
            homeSet.put(player.getName().toLowerCase(), homeList);
            return true;
        }

    }

    public static boolean putHome(String playerName, Home home) {
        if (homeSet.containsKey(playerName.toLowerCase())) {
            if (!containsHome(playerName, home.getHomeName())) {
                homeSet.get(playerName.toLowerCase()).add(home);
                return true;
            } else {
                return false;
            }

        } else {
            ArrayList<Home> homeList = new ArrayList<>();
            homeList.add(home);
            homeSet.put(playerName.toLowerCase(), homeList);
            return true;
        }

    }

    public static boolean containsHome(String playerName, String homeName) {
        if (homeSet.containsKey(playerName.toLowerCase())) {
            for (Home home : homeSet.get(playerName.toLowerCase())) {
                if (home.getHomeName().equalsIgnoreCase(homeName)) return true;
            }
        } else {
            return false;
        }
        return false;
    }

    public static Home getHome(String playerName, String homeName) {
        if (homeSet.containsKey(playerName.toLowerCase())) {
            for (Home home : homeSet.get(playerName.toLowerCase())) {
                if (home.getHomeName().equalsIgnoreCase(homeName)) return home;
            }
        } else {
            return null;
        }
        return null;
    }

    public static int getHomeAmount(String playerName) {
        if (!homeSet.containsKey(playerName.toLowerCase())) return 0;
        else return homeSet.get(playerName.toLowerCase()).size();
    }

    public static ArrayList<Home> getHomeList(String playerName) {
        if (!homeSet.containsKey(playerName.toLowerCase())) return null;
        else return homeSet.get(playerName.toLowerCase());
    }

    public static ArrayList<String> getHomeNamesList(String playerName){
        if (!homeSet.containsKey(playerName.toLowerCase())) return null;
        else{
            ArrayList<String> names = new ArrayList<>();
            for(Home home : homeSet.get(playerName.toLowerCase())){
                names.add(home.getHomeName());
            }
            return names;
        }
    }

    public static boolean deleteHome(String playerName, String homeName) {
        if (!homeSet.containsKey(playerName.toLowerCase())) return false;
        else {
            boolean isRemoved = false;
            for (Home home : homeSet.get(playerName.toLowerCase())) {
                if (home.getHomeName().equalsIgnoreCase(homeName)) {
                    homeSet.get(playerName.toLowerCase()).remove(home);
                    isRemoved = true;
                }
            }
            return isRemoved;
        }
    }

    public static boolean replaceHome(String playerName, Home newHome, String homeName) {
        if (!homeSet.containsKey(playerName)) return false;
        else {
            for (Home home : homeSet.get(playerName.toLowerCase())) {
                if (home.getHomeName().equals(homeName.toLowerCase())) {
                    homeSet.get(playerName.toLowerCase()).remove(home);
                    homeSet.get(playerName.toLowerCase()).add(newHome);
                    return true;
                }
            }
        }
        return false;
    }


    public static Map<String, ArrayList<Home>> getHomeSet() {
        return homeSet;
    }


    public static void saveHome() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(McfineHome.getPlugin().getDataFolder().getAbsolutePath() + "/homes.json");

        file.getParentFile().mkdir();
        file.createNewFile();
        Writer writer = new FileWriter(file, false);
        gson.toJson(homeSet, writer);
        writer.flush();
        writer.close();
    }


    public static void loadHome() throws IOException {
        Gson gson = new Gson();
        File file = new File(McfineHome.getPlugin().getDataFolder().getAbsolutePath() + "/homes.json");
        if (file.exists()) {
            Reader reader = new FileReader(file);

            Map<String, ArrayList<Home>> map = new HashMap<String, ArrayList<Home>>();
            map = (Map<String, ArrayList<Home>>) gson.fromJson(reader, map.getClass());
            homeSet = map;
            McfineHome.getPlugin().getLogger().info("Data uploaded");
        }
    }

}
