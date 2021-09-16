package com.mcfine.mcfinehome.commands;

import com.mcfine.mcfinehome.McfineHome;
import com.mcfine.mcfinehome.data.Home;
import com.mcfine.mcfinehome.utils.HomeStorage;
import com.mcfine.mcfinehome.utils.Teleporter;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("mcfinehome.home")) {
                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &4У вас нет доступа к данной команде!"));
                return false;
            }

            if (args.length == 0) {
                ArrayList<Home> homeList = HomeStorage.getHomeList(p.getName());
                if (Objects.isNull(homeList) || homeList.size() == 0) {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cУ вас нет дома!"));
                    return false;
                }
                int result = Teleporter.tryTpAny(p);
                if (result == -1) {
                    p.sendMessage(ColorTranslator.translateColorCodes("У вас нет безопасных домов"));
                } else if (result == 0) {
                    p.sendMessage(ColorTranslator.translateColorCodes("У вас нет домов"));
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("set")) {
                    setHome(p, args);
                    return false;
                }
                if (args[0].equalsIgnoreCase("list")) {
                    listHomes(p, args);
                    return false;
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    delHome(p, args);
                    return false;
                }
                if (args[0].equalsIgnoreCase("help")) {
                    helpShow(p);
                    return false;
                }
                if (args[0].equalsIgnoreCase("public")) {
                    makeHomePublic(p, true, "Main");
                    return false;
                }
                if (args[0].equalsIgnoreCase("private")) {
                    makeHomePublic(p, false, "Main");
                    return false;
                }
                if (args[0].equalsIgnoreCase("invitedme")) {
                    printWhoInvitedMe(p);
                    return false;
                }

                ArrayList<Home> homeList = HomeStorage.getHomeList(p.getName());
                if (HomeStorage.containsHome(p.getName(), args[0].trim())) {
                    Home home = HomeStorage.getHome(p.getName(), args[0].trim());
                    Teleporter.tryTp(p, home.getLocation());
                    return false;
                }

                Home home = HomeStorage.getHome(args[0].trim(), "Main");
                if (Objects.isNull(home)) {
                    p.sendMessage(ColorTranslator.translateColorCodes("No such home"));
                    return false;
                }
                if (home.isPubl() || home.isInvited(p.getName())) {
                    if (Teleporter.tryTp(p, home.getLocation())) {
                        if (home.getHomeName().equalsIgnoreCase("Main")) {
                            p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eВы телепортировались в дом &6" + home.getPlayerName()));
                            return false;
                        }
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eВы телепортировались в дом: &6" + home.getHomeName() + " &eигрока: &6" + home.getPlayerName()));
                    }
                } else {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cВас не пригласили к &6" + home.getPlayerName()));
                }
                return false;

            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("set")) {
                    setHome(p, args);
                    return false;
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    delHome(p, args);
                    return false;
                }
                if (args[0].equalsIgnoreCase("invite")) {
                    inviteHome(p, args);
                    return false;
                }
                if (args[0].equalsIgnoreCase("uninvite")) {
                    uninviteHome(p, args);
                    return false;
                }
                if (args[0].equalsIgnoreCase("public")) {
                    makeHomePublic(p, true, args[1].trim());
                    return false;
                }
                if (args[0].equalsIgnoreCase("private")) {
                    makeHomePublic(p, false, args[1].trim());
                    return false;
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("rename")) {
                    renameHome(p.getName(), args[1].trim(), args[2].trim());
                    return false;
                }
            } else {
                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cКоманда неизвестна"));
            }


        }

        return false;
    }

    public static void delHome(Player p, String[] args) {
        if (args.length == 1) {
            if (HomeStorage.deleteHome(p.getName(), "Main"))
                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eДом удален"));
            else p.sendMessage(ColorTranslator.translateColorCodes("У вас нет главного дома"));
        } else if (args.length == 2) {
            Home home = HomeStorage.getHome(p.getName(), args[1]);
            if (HomeStorage.deleteHome(p.getName(), args[1]))
                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eДом &9" + args[1] + " &eудален"));
            else p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cНе верное имя дома"));
        } else {
            p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cСлишком много аргументов"));
        }
    }

    public static void setHome(Player p, String[] args) {
        if (args.length == 1) {
            if (McfineHome.getPlugin().getConfig().getBoolean("onlyBedSetsHome")) {
                if (McfineHome.bybed) McfineHome.bybed = false;
                else
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Дом можно установить только поспав в кровати!"));
            }

            if (HomeStorage.containsHome(p.getName(), "Main")) {
                Home main = HomeStorage.getHome(p.getName(), "Main");
                Home home = new Home(p, "Main");
                home.setInvitedNames(main.getInvitedNames());
                HomeStorage.replaceHome(p.getName(), home, "Main");
                p.setBedSpawnLocation(home.getLocation());
                p.setCompassTarget(home.getLocation());
                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Главный дом перемещен"));
            } else {
                if (HomeStorage.createHome(p, "Main")) {
                    Home main = HomeStorage.getHome(p.getName(), "Main");
                    if (main != null) {
                        p.setBedSpawnLocation(main.getLocation());
                        p.setCompassTarget(main.getLocation());
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &aТочка главного дома поставлена"));
                    } else {
                        p.sendMessage("Ошибка в установке дома");
                    }
                } else {
                    p.sendMessage("Ошибка в установке дома");
                }
            }
        } else if (args.length == 2) {
            int max = 2;
            for (int i = Math.max(McfineHome.getPlugin().getConfig().getInt("maxNumberOfHomes"), 32); i >= 0; i--) {
                if (p.hasPermission("mcfine.home." + i)) {
                    max = i;
                    break;
                }
            }
            if (HomeStorage.getHomeAmount(p.getName()) < max) {
                if (args[1].equals("invite") || args[1].equals("set") || args[1].equals("delete") || args[1].equals("invited")
                        || args[1].equals("uninvite") || args[1].equals("private") || args[1].equals("public")
                        || args[1].equals("help") || args[1].equals("rename")) {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cЭто имя зарезервированно! Используйте другое"));
                    return;
                }
                if (HomeStorage.containsHome(p.getName(), args[1].trim())) {
                    Home home = new Home(p, args[1].trim());
                    home.setInvitedNames(HomeStorage.getHome(p.getName(), args[1].trim()).getInvitedNames());
                    HomeStorage.replaceHome(p.getName(), home, args[1].trim());
                    p.setBedSpawnLocation(home.getLocation());
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Дом перемещен"));
                } else {
                    if (HomeStorage.createHome(p, args[1].trim())) {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &aТочка дома поставлена"));
                    } else {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cОшибка! Обратитесь к администрации"));
                    }
                }
            } else {
                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cУ вас максимальное число домов. Удалите один командой /home delete <имя>"));
            }
        }
    }

    public static void printWhoInvitedMe(Player p) {
        Map<String, ArrayList<Home>> tmp = HomeStorage.getHomeSet();
        Collection<ArrayList<Home>> pHomeList = tmp.values();
        StringBuilder homesBuilder = new StringBuilder("&9Home &7▪ &eВы приглашены к: &6");
        boolean any = false;
        ArrayList<String> players = new ArrayList<>();

        for (ArrayList<Home> array : pHomeList) {
            if (array == null) continue;
            boolean anyCurrent = false;
            StringBuilder currentBuilder = new StringBuilder(array.get(0).getPlayerName() + ": ");
            for (Home home : array) {
                if (home == null) continue;
                if (home.isInvited(p.getName())) {
                    currentBuilder.append(home.getHomeName() + ", ");
                    anyCurrent = true;
                }
            }
            if (anyCurrent) {
                String currentPlayer = currentBuilder.toString();
                currentPlayer = currentPlayer.substring(0, currentPlayer.length() - 4);
                homesBuilder.append(currentPlayer);
                any = true;
            }
        }

        if (any) {
            String result = homesBuilder.toString();
            p.sendMessage(ColorTranslator.translateColorCodes(result));
        } else {
            p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cВас еще никто не пригласил"));
        }
    }

    public static void renameHome(String playerName, String oldHomeName, String newHomeName) {
        Home home = HomeStorage.getHome(playerName, oldHomeName);
        Player player = McfineHome.getPlugin().getServer().getPlayer(playerName);
        if (home == null) {
            player.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cУ вас нет дома с таким именем!"));
        } else {
            home.setHomeName(newHomeName);
            HomeStorage.replaceHome(playerName, home, oldHomeName);
            player.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &aДом успешно переименован из &e" + oldHomeName + "&a в &e" + newHomeName));
        }
    }

    public static void inviteHome(Player p, String[] args) {
        if (args.length == 2) {
            if (args[1].trim().equalsIgnoreCase(p.getName()))
                p.sendMessage(ColorTranslator.translateColorCodes("Это ваш ник"));
            if (HomeStorage.containsHome(p.getName(), "Main")) {
                Home main = HomeStorage.getHome(p.getName(), "Main");
                if (main.invitePlayer(args[1].trim())) {
                    Player invitedPlayer = McfineHome.getPlugin().getServer().getPlayer(args[1].trim());
                    if (invitedPlayer != null) {
                        invitedPlayer.sendMessage(ColorTranslator.translateColorCodes("Вы были приглашены к " + p.getName() + " в главный дом"));
                    }
                } else {
                    p.sendMessage(ColorTranslator.translateColorCodes("Игрок уже приглашен"));
                }
            } else {
                p.sendMessage(ColorTranslator.translateColorCodes("У вас нет главного дома"));
            }
        } else if (args.length == 3) {
            if (args[2].trim().equalsIgnoreCase(p.getName()))
                p.sendMessage(ColorTranslator.translateColorCodes("Это ваш ник"));
            if (HomeStorage.containsHome(p.getName(), args[1].trim())) {
                Home home = HomeStorage.getHome(p.getName(), args[1].trim());
                if (home.invitePlayer(args[2].trim())) {
                    Player invitedPlayer = McfineHome.getPlugin().getServer().getPlayer(args[2].trim());
                    if (invitedPlayer != null) {
                        invitedPlayer.sendMessage(ColorTranslator.translateColorCodes("Вы были приглашены к " + p.getName() + " в дом " + home.getHomeName()));
                    }
                } else {
                    p.sendMessage(ColorTranslator.translateColorCodes("Игрок уже приглашен"));
                }
            } else {
                p.sendMessage(ColorTranslator.translateColorCodes("У вас нет такого дома"));
            }
        } else {
            p.sendMessage(ColorTranslator.translateColorCodes("Слишком много аргументов"));
        }
    }

    public static void uninviteHome(Player p, String[] args) {
        if (args.length == 2) {
            if (args[1].trim().equalsIgnoreCase(p.getName()))
                p.sendMessage(ColorTranslator.translateColorCodes("Это ваш ник"));
            if (HomeStorage.containsHome(p.getName(), "Main")) {
                Home main = HomeStorage.getHome(p.getName(), "Main");
                if (main.uninvitePlayer(args[1].trim())) {
                    Player uninvitedPlayer = McfineHome.getPlugin().getServer().getPlayer(args[1].trim());
                    if (uninvitedPlayer != null) {
                        uninvitedPlayer.sendMessage(ColorTranslator.translateColorCodes("Вы больше не приглашены к " + p.getName() + " в главный дом"));
                    }
                } else {
                    p.sendMessage(ColorTranslator.translateColorCodes("Игрок и так не приглашен к вам"));
                }
            } else {
                p.sendMessage(ColorTranslator.translateColorCodes("У вас нет главного дома"));
            }
        } else if (args.length == 3) {
            if (args[2].trim().equalsIgnoreCase(p.getName()))
                p.sendMessage(ColorTranslator.translateColorCodes("Это ваш ник"));
            if (HomeStorage.containsHome(p.getName(), args[1].trim())) {
                Home home = HomeStorage.getHome(p.getName(), args[1].trim());
                if (home.uninvitePlayer(args[2].trim())) {
                    Player uninvitedPlayer = McfineHome.getPlugin().getServer().getPlayer(args[2].trim());
                    if (uninvitedPlayer != null) {
                        uninvitedPlayer.sendMessage(ColorTranslator.translateColorCodes("Вы больше не приглашены к " + p.getName() + " в дом " + home.getHomeName()));
                    }
                } else {
                    p.sendMessage(ColorTranslator.translateColorCodes("Игрок и так не приглашен к вам"));
                }
            } else {
                p.sendMessage(ColorTranslator.translateColorCodes("У вас нет такого дома"));
            }
        } else {
            p.sendMessage(ColorTranslator.translateColorCodes("Слишком много аргументов"));
        }
    }

    public static void helpShow(Player p) {
        p.sendMessage(ColorTranslator.translateColorCodes("&8&l&m------------------------------------------"));
        p.sendMessage(ColorTranslator.translateColorCodes("&9/home                   &6-  &6Телепортироваться домой"));
        p.sendMessage(ColorTranslator.translateColorCodes("&9/home set              &6-  &6Установить точку дома"));
        p.sendMessage(ColorTranslator.translateColorCodes("&9/home invite [Ник]     &6-  &6Пригласить домой"));
        p.sendMessage(ColorTranslator.translateColorCodes("&9/home uninvite [Ник]  &6-  &6Удалить из приглашенных"));
        p.sendMessage(ColorTranslator.translateColorCodes("&9/home [Ник]             &6-  &6Телепортироваться в дом игрока"));
        p.sendMessage(ColorTranslator.translateColorCodes("&9/home delete           &6-  &6Удалить точку дома"));
        p.sendMessage(ColorTranslator.translateColorCodes("&9/home invited          &6-  &6Посмотреть список приглашенных"));
        p.sendMessage(ColorTranslator.translateColorCodes("&9/home public           &6-  &6Сделать дом общедоступным"));
        p.sendMessage(ColorTranslator.translateColorCodes("&9/home private         &6-  &6Сделать дом приватным"));
        p.sendMessage(ColorTranslator.translateColorCodes("&9/home help             &6-  &6Получить информацию о плагине"));
        p.sendMessage(ColorTranslator.translateColorCodes("&8&l&m------------------------------------------"));
    }

    public static void makeHomePublic(Player player, boolean publ, String homeName) {
        if (HomeStorage.containsHome(player.getName(), homeName)) {
            Home home = HomeStorage.getHome(player.getName(), homeName);
            if (home.setPublic(publ)) {
                HomeStorage.replaceHome(player.getName(), home, home.getHomeName());
                if (publ) {
                    player.sendMessage(ColorTranslator.translateColorCodes("Дом сделан публичным"));
                } else {
                    player.sendMessage(ColorTranslator.translateColorCodes("Дом сделан приватным"));
                }
            } else {
                if (publ) {
                    player.sendMessage(ColorTranslator.translateColorCodes("Дом и так публичный"));
                } else {
                    player.sendMessage(ColorTranslator.translateColorCodes("Дом и так приватный"));
                }
            }
        } else {
            player.sendMessage(ColorTranslator.translateColorCodes("У вас нет такого дома"));
        }
    }

    public static void listHomes(Player p, String[] args) {
        if (args.length == 1) {
            ArrayList<String> homeNames = HomeStorage.getHomeNamesList(p.getName());
            if (homeNames == null) {
                p.sendMessage(ColorTranslator.translateColorCodes("No homes found"));
            } else if (homeNames.size() == 0) {
                p.sendMessage(ColorTranslator.translateColorCodes("No homes found"));
            }
            StringBuilder builder = new StringBuilder(ColorTranslator.translateColorCodes("&6Ваши дома: &e"));
            for (String str : homeNames) {
                builder.append(str+"&6, &e");
            }
            String list = builder.toString();
            list = list.substring(0,list.length()-4);
            p.sendMessage(ColorTranslator.translateColorCodes(list));
        } else{
            p.sendMessage(ColorTranslator.translateColorCodes("Too many args"));
        }
    }


}
