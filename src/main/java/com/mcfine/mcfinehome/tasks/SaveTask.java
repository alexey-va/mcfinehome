package com.mcfine.mcfinehome.tasks;

import com.mcfine.mcfinehome.McfineHome;
import com.mcfine.mcfinehome.utils.HomeStorage;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class SaveTask extends BukkitRunnable {

    @SuppressWarnings("unused")
    final McfineHome plugin;

    public SaveTask(McfineHome plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        try {
            HomeStorage.saveHome();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
