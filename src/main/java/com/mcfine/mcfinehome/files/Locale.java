package com.mcfine.mcfinehome.files;

import com.mcfine.mcfinehome.McfineHome;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Locale {


    private static FileConfiguration lang;
    private static File langFile;

    public static void setup(String language) {
        langFile = new File(Bukkit.getServer().getPluginManager().getPlugin("McfineHome").getDataFolder(), "messages_" + language.toLowerCase());

        if (!langFile.exists()) {
            try {
                langFile.createNewFile();
            } catch (Exception ex) {

            }
        }
        lang = YamlConfiguration.loadConfiguration(langFile);
    }

    public static FileConfiguration get(){
        return lang;
    }

    public static void save(){
        try {
            lang.save(langFile);
        } catch(IOException ex){
            McfineHome.getPlugin().getLogger().info("Was not able to save messages file");
        }
    }
}
