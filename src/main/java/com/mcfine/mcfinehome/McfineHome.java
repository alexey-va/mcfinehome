package com.mcfine.mcfinehome;

import com.mcfine.mcfinehome.commands.*;
import com.mcfine.mcfinehome.data.Home;
import com.mcfine.mcfinehome.tasks.SaveTask;
import com.mcfine.mcfinehome.utils.HomeStorage;
import com.mcfine.mcfinehome.utils.Teleporter;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.menu.MenuManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public final class McfineHome extends JavaPlugin implements Listener {

    public static boolean bybed = false;
    private static McfineHome plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;


        getConfig().options().copyDefaults();
        saveDefaultConfig();

        try {
            HomeStorage.loadHome();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MenuManager.setup(getServer(), this);
        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand());
        Objects.requireNonNull(getCommand("tohome")).setExecutor(new TohomeCommand());
        Objects.requireNonNull(getCommand("home")).setTabCompleter(new TabCompletion());
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SethomeCommand());
        Objects.requireNonNull(getCommand("delhome")).setExecutor(new DelhomeCommand());
        Objects.requireNonNull(getCommand("home-of")).setExecutor(new HomeofCommand());
        getCommand("home-of").setTabCompleter(new homeofTabCompletion());
        //if(getConfig().getInt("maxNumberOfHomes")>2) {
        //getCommand("homes").setExecutor(new HomesCommand());
        //}
        getServer().getPluginManager().registerEvents(this, this);
        new SaveTask(plugin).runTaskTimerAsynchronously(plugin, 1200L, 3600L);
        getLogger().info("Plugin started");
    }

    public static McfineHome getPlugin() {
        return plugin;
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            HomeStorage.saveHome();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onBedClicking(PlayerBedEnterEvent event) {
        Location loc = event.getBed().getLocation();
        Player p = event.getPlayer();
        Home home = HomeStorage.getHome(p.getName(), "Main");
        if (Objects.nonNull(home)) {
        } else {
            HomeStorage.putHome(p.getName().toLowerCase(), new Home(loc.getX(), loc.getY(), loc.getZ(),
                    p.getLocation().getYaw(), p.getLocation().getPitch(), "Main", p.getUniqueId().toString(), p.getName(),
                    new ArrayList<>(), loc.getWorld().getName(), false));
            p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7» &6Ваш дом установлен. Используйте &9/home"));
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent ev) {
        Player p = ev.getPlayer();

        ArrayList<Home> homeList = HomeStorage.getHomeList(p.getName());
        if (homeList == null) {
            return;
        }
        if (homeList.size() == 0) {
            //p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7» &cУ вас нет дома &6:("));
        } else {
            Home main = null;
            Home bed = null;
            Home any = null;
            for (Home home : homeList) {
                if (home != null) {
                    if (home.getHomeName().equalsIgnoreCase("main")) main = home;
                    else if (home.getHomeName().equalsIgnoreCase("bed")) bed = home;
                }
            }
            if (main != null) {
                if (Teleporter.isSafe(main.getLocation())) {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7» &2Вы возродились у себя дома"));
                    ev.setRespawnLocation(main.getLocation());
                    return;
                } else {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7» &cВаш дом небезопасен!"));
                }
            }
            if (bed != null) {
                if (Teleporter.isSafe(bed.getLocation())) {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7» &2Вы возродились у себя дома"));
                    ev.setRespawnLocation(bed.getLocation());
                    return;
                } else {
                    //p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7▪ &cВаш дом небезопасен"));
                }
            }
            if (any != null) {
                for (Home home : homeList) {
                    if (Teleporter.isSafe(home.getLocation())) {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Дом &7» &2Вы возродились у себя дома"));
                        ev.setRespawnLocation(home.getLocation());
                        return;
                    } else {
                        //p.sendMessage(ColorTranslator.translateColorCodes("Ваш дом "+home.getHomeName()+" кровати небезопасен"));
                    }
                }
            }
        }

    }

}
