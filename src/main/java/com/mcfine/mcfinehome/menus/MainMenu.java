package com.mcfine.mcfinehome.menus;

import com.mcfine.mcfinehome.McfineHome;
import com.mcfine.mcfinehome.commands.HomeCommand;
import com.mcfine.mcfinehome.data.Home;
import com.mcfine.mcfinehome.utils.HomeStorage;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import me.kodysimpson.simpapi.heads.SkullCreator;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.MenuManager;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MainMenu extends Menu {
    public MainMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return ColorTranslator.translateColorCodes(McfineHome.getPlugin().getConfig().getString("menu-name"));
    }

    @Override
    public int getSlots() {
        return McfineHome.getPlugin().getConfig().getInt("row-amount") * 9;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        Player p = playerMenuUtility.getOwner();
        switch (e.getCurrentItem().getType()) {
            case PAPER:
                HomeCommand.printWhoInvitedMe(p);
                break;
            case RED_TERRACOTTA:
                p.performCommand("home");
                break;
            case PLAYER_HEAD:
                String action = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(McfineHome.getPlugin(), "menuAction"), PersistentDataType.STRING);
                if (action.equals("close")) {
                    p.closeInventory();
                }
                break;
            case BLUE_TERRACOTTA:
                ClickType click = e.getClick();
                String homeName = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(McfineHome.getPlugin(), "homeName"), PersistentDataType.STRING);
                if (click.isLeftClick()) {
                    p.performCommand("home " + homeName);
                } else if (click.isRightClick()) {
                    playerMenuUtility.setData("chosenHome", homeName);
                    MenuManager.openMenu(HomeMenu.class, playerMenuUtility.getOwner());
                }
                break;
        }

    }

    @Override
    public void setMenuItems() {
        int rows = McfineHome.getPlugin().getConfig().getInt("row-amount");
        ItemStack invited = makeItem(Material.PAPER, ColorTranslator.translateColorCodes("&6&lПриглашения"));
        ItemStack filler = makeItem(Material.GRAY_STAINED_GLASS_PANE, " ");
        FILLER_GLASS = filler;
        this.setFillerGlass();
        inventory.setItem(4, invited);
        Player p = playerMenuUtility.getOwner();
        ArrayList<Home> homes = HomeStorage.getHomeList(p.getName());
        ArrayList<String> homeNames = HomeStorage.getHomeNamesList(p.getName());
        int loc = 10;
        if (!Objects.isNull(homes)) {
            Home main = HomeStorage.getHome(p.getName(), "Main");
            if (!Objects.isNull(main)) {
                homes.remove(main);
                homes.add(0, main);
            }
            for (Home hm : homes) {
                ItemStack homeItem = makeItem(Material.BLUE_TERRACOTTA, ColorTranslator.translateColorCodes("&6&l" + hm.getHomeName()),
                        ColorTranslator.translateColorCodes(" &7▪ &eКоординаты: &6X:" + (int)hm.getX() + "  Y:" + (int)hm.getY() + "  Z:" + (int)hm.getZ()),
                        hm.isPubl() ? ColorTranslator.translateColorCodes(" &7▪ &eПубличный: &6Да") : ColorTranslator.translateColorCodes(" &7▪ &eПубличный: &6Нет"));
                ItemMeta meta = homeItem.getItemMeta();
                meta.getPersistentDataContainer().set(new NamespacedKey(McfineHome.getPlugin(), "homeName"), PersistentDataType.STRING, hm.getHomeName());
                homeItem.setItemMeta(meta);
                if (hm.getHomeName().equals("Main")) homeItem.setType(Material.RED_TERRACOTTA);
                inventory.setItem(loc, homeItem);
                loc++;
                if (loc == 17 || loc == 26) loc += 2;
                if (loc == 18 || loc == 27) loc++;
                if (loc >= 9 * (rows - 1) - 2) break;
            }
        }

        for (int i = 10 + homeNames.size(); i < (rows - 1) * 9 - 1; i++) {
            if ((i + 1) % 9 == 0 || (i + 1) % 9 == 1) continue;
            ItemStack noHome = makeItem(Material.GREEN_STAINED_GLASS_PANE, ColorTranslator.translateColorCodes(" "));
            inventory.setItem(i, noHome);
        }
        ItemStack close = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTJkN2E3NTFlYjA3MWUwOGRiYmM5NWJjNWQ5ZDY2ZTVmNTFkYzY3MTI2NDBhZDJkZmEwM2RlZmJiNjhhN2YzYSJ9fX0=");
        close.getItemMeta().setLocalizedName(ColorTranslator.translateColorCodes("&9&lВыход"));
        ItemMeta meta = close.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(McfineHome.getPlugin(), "menuAction"), PersistentDataType.STRING, "close");
        close.setItemMeta(meta);
        inventory.setItem(rows * 9 - 5, close);


    }
}
