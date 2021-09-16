package com.mcfine.mcfinehome.menus;

import com.mcfine.mcfinehome.McfineHome;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.heads.SkullCreator;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class HomeMenu extends Menu {
    public HomeMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Настройка: "+playerMenuUtility.getData("chosenMenu");
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) throws MenuManagerNotSetupException, MenuManagerException {

    }

    @Override
    public void setMenuItems() {
        ItemStack exit = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTJkN2E3NTFlYjA3MWUwOGRiYmM5NWJjNWQ5ZDY2ZTVmNTFkYzY3MTI2NDBhZDJkZmEwM2RlZmJiNjhhN2YzYSJ9fX0=");
        exit.getItemMeta().setLocalizedName("&8&lВыход");
        ItemStack back = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjllYTFkODYyNDdmNGFmMzUxZWQxODY2YmNhNmEzMDQwYTA2YzY4MTc3Yzc4ZTQyMzE2YTEwOThlNjBmYjdkMyJ9fX0=");
        back.getItemMeta().setLocalizedName("&8&lНазад");
        ItemStack filler = makeItem(Material.GRAY_STAINED_GLASS_PANE," ");
        for(int i=0;i<10;i++){
            inventory.setItem(i,filler);
        }
        inventory.setItem(17,filler);
        inventory.setItem(18,filler);
        inventory.setItem(26,filler);
        inventory.setItem(27,filler);
        inventory.setItem(35,filler);
        for(int i=37;i<45;i++) {
            inventory.setItem(i, filler);
        }
        inventory.setItem(36,back);
        inventory.setItem(40,exit);
    }
}
