package com.mcfine.mcfinehome;

import com.mcfine.mcfinehome.commands.DelhomeCommand;
import com.mcfine.mcfinehome.commands.HomeCommand;
import com.mcfine.mcfinehome.commands.HomeofCommand;
import com.mcfine.mcfinehome.commands.SethomeCommand;
import com.mcfine.mcfinehome.data.Home;
import com.mcfine.mcfinehome.tasks.SaveTask;
import com.mcfine.mcfinehome.utils.HomeStorage;
import me.kodysimpson.simpapi.colors.ColorTranslator;
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

        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand());
        Objects.requireNonNull(getCommand("home")).setTabCompleter(new TabCompletion());
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SethomeCommand());
        Objects.requireNonNull(getCommand("delhome")).setExecutor(new DelhomeCommand());
        Objects.requireNonNull(getCommand("home-of")).setExecutor(new HomeofCommand());
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
        if (getConfig().getBoolean("bedSetsHome")) {
            if (getConfig().getBoolean("onlyBedSetsHome")) {
                bybed = true;
                p.performCommand("home set");
            } else {
                Home home = HomeStorage.getHomeByUID(p.getUniqueId().toString(), "bed");
                if (Objects.nonNull(home)) {
                    HomeStorage.replaceHome(p.getUniqueId().toString(), new Home(loc.getX(), loc.getY(), loc.getZ(),
                            p.getLocation().getYaw(), p.getLocation().getPitch(), "bed", p.getUniqueId().toString(), p.getName(),
                            new ArrayList<>(), loc.getWorld().getName(), false), "bed");
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Дом привязанный к кровати перемещен. Используйте &9/home bed"));
                } else {
                    HomeStorage.putHome(new Home(loc.getX(), loc.getY(), loc.getZ(),
                            p.getLocation().getYaw(), p.getLocation().getPitch(), "bed", p.getUniqueId().toString(), p.getName(),
                            new ArrayList<>(), loc.getWorld().getName(), false));
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Дом привязанный к кровати установлен. Используйте &9/home bed"));
                }
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent ev) {
        Player p = ev.getPlayer();
        Home hm = HomeStorage.getHomeByUID(p.getUniqueId().toString(), "Main");
        if (Objects.nonNull(hm)) {
            p.setBedSpawnLocation(hm.getLocation());
            ev.setRespawnLocation(hm.getLocation());
            return;
        }
        hm = HomeStorage.getHomeByUID(p.getUniqueId().toString(), "bed");
        if (Objects.nonNull(hm)) {
            p.setBedSpawnLocation(hm.getLocation());
            ev.setRespawnLocation(hm.getLocation());
            return;
        }
        hm = HomeStorage.getAnyHomeByUID(p.getUniqueId().toString());
        if (Objects.nonNull(hm)) {
            p.setBedSpawnLocation(hm.getLocation());
            ev.setRespawnLocation(hm.getLocation());
            return;
        }
    }

}