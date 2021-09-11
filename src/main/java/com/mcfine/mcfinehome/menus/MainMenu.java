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
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
        return McfineHome.getPlugin().getConfig().getInt("row-amount")*9;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {
        Player p = playerMenuUtility.getOwner();
        switch(e.getCurrentItem().getType()){
            case PAPER:
                HomeCommand.printInvitedmeNames(p);
                break;
            case RED_TERRACOTTA:
                p.performCommand("home");
                break;
            case PLAYER_HEAD:
                p.closeInventory();
                break;
            case BLUE_TERRACOTTA:
                Home hm = HomeStorage.getHomeByName(p.getName(),e.getCurrentItem().getItemMeta().getLocalizedName());
                if(Objects.isNull(hm)) p.sendMessage(ColorTranslator.translateColorCodes("&c&lТут ошибка сообщите админу"));
                else{
                    p.performCommand("home "+hm.getHomeName());
                }
                break;
        }

    }

    @Override
    public void setMenuItems() {
        int rows = McfineHome.getPlugin().getConfig().getInt("row-amount");
        ItemStack invited = makeItem(Material.PAPER, ColorTranslator.translateColorCodes("&6&lПриглашения"));
        ItemStack filler = makeItem(Material.GRAY_STAINED_GLASS_PANE," ");
        FILLER_GLASS=filler;
        this.setFillerGlass();
        inventory.setItem(4, invited);
        Player p =playerMenuUtility.getOwner();
        ArrayList<Home> homes = HomeStorage.getHomeListName(p.getName());
        ArrayList<String> homeNames = HomeStorage.getHomeNamesList(p.getName());
        int loc=10;
        Home main = HomeStorage.getHomeByName(p.getName(),"Main");
        if(Objects.nonNull(main)) {
            ItemStack homeMain = makeItem(Material.RED_TERRACOTTA, ColorTranslator.translateColorCodes("&6&lГлавный дом"),
                    "  &7-  &6&lЭто ваш главный дом", ColorTranslator.translateColorCodes("  &f-  &eКоординаты: &6X:" + main.getX() + "  Y:" + main.getY() + "  Z:" + main.getZ()),
                    main.isPubl() ? ColorTranslator.translateColorCodes("  &f-  &eПубличный: &6Да") : ColorTranslator.translateColorCodes("  &f-  &eПубличный: &6Нет"));
                inventory.setItem(10, homeMain);
                loc++;
        }
        for(Home hm:homes){
            if(!hm.getHomeName().equals("Main")){
                ItemStack homeItem = makeItem(Material.BLUE_TERRACOTTA,ColorTranslator.translateColorCodes("&6&l"+hm.getHomeName()),
                        ColorTranslator.translateColorCodes("  &f-  &eКоординаты: &6X:"+hm.getX()+"  Y:"+hm.getY()+"  Z:"+hm.getZ()),
                        hm.isPubl() ? ColorTranslator.translateColorCodes("  &f-  &eПубличный: &6Да") : ColorTranslator.translateColorCodes("  &f-  &eПубличный: &6Нет"));
                inventory.setItem(loc,homeItem);
                loc++;
                if(loc==17 || loc==26) loc+=2;
                if(loc==18 || loc==27) loc++;
                if(loc>=9*(rows-1)-2) break;
            }
        }




        for(int i=10+homeNames.size();i<(rows-1)*9-1;i++){
            if((i+1)%9==0 || (i+1)%9==1) continue;
            ItemStack noHome = makeItem(Material.GREEN_STAINED_GLASS_PANE,ColorTranslator.translateColorCodes(" "));
            inventory.setItem(i,noHome);
        }
        ItemStack close = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTJkN2E3NTFlYjA3MWUwOGRiYmM5NWJjNWQ5ZDY2ZTVmNTFkYzY3MTI2NDBhZDJkZmEwM2RlZmJiNjhhN2YzYSJ9fX0=");
        close.getItemMeta().setLocalizedName(ColorTranslator.translateColorCodes("&9&lВыход"));
        inventory.setItem(rows*9-5,close);


    }
}
