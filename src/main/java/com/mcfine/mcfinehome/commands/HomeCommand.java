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

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("mcfinehome.home")) {
                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &4У вас нет доступа к данной команде!"));
                return false;
            }
            String[] args2 = new String[args.length];
            for (int i = 0, argsLength = args.length; i < argsLength; i++) {
                String s = args[i];
                args2[i] = s;
            }
            for (int i = 0, argsLength = args.length; i < argsLength; i++) {
                args[i] = args[i].trim().toLowerCase(Locale.ROOT);
            }

            if (args.length == 0) {
                Home home = HomeStorage.getHomeByName(p.getName(), "Main");
                if (Objects.nonNull(home)) {
                    if (Objects.isNull(McfineHome.getPlugin().getServer().getWorld(home.getWorld()))) {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cМир не найден!"));
                        return false;
                    }
                    if (Teleporter.tryTp(p, home.getLocation())) {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eВы телепортировались в домой"));
                        return false;
                    }
                } else {
                    home = HomeStorage.getAnyHomeByName(p.getName());
                    if (Objects.nonNull(home)) {
                        if (Teleporter.tryTp(p, home.getLocation())) {
                            //p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eВы телепортировались в домой"));
                            if (home.getHomeName().equals("bed")) {
                                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eВы телепортировались к своей кровати"));
                                return false;
                            }
                            p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eВы телепортировались в дом " + home.getHomeName()));
                            return false;
                        }
                    } else {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cУ вас нет дома!"));
                        return false;
                    }
                }
            } else if (args[0].equals("set")) {
                setHome(p, args);
            } else if (args[0].equals("delete")) {
                delHome(p, args);
            } else if (args[0].equals("invite")) {
                if (args.length == 2) {
                    if (args2[1].trim().equals(p.getName())) {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Это ваш ник!"));
                        return false;
                    }
                    Home home = HomeStorage.getHomeByName(p.getName(), "Main");

                    if (Objects.requireNonNull(home).getInvitedNames().contains(args2[1].trim())) {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Игрок уже приглашен!"));
                    } else {
                        ArrayList<String> tmp = home.getInvitedNames();
                        tmp.add(args2[1].trim());
                        home.setInvitedNames(tmp);
                        HomeStorage.replaceHome(p.getUniqueId().toString(), home, "Main");
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eИгрок &6" + args[1] + " &eприглашен!"));
                        Player p2=McfineHome.getPlugin().getServer().getPlayerExact(args[1]);
                        if(Objects.nonNull(p2)){
                            if(p2.isOnline()){
                                p2.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eИгрок &6" +p.getName()+" &eпригласил вас к себе домой.\n Напишите &6/home "+p2.getName()+" &eдля телепортации"));
                            }
                        }
                        return false;
                    }
                } else if (args.length > 2) {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cСлишком много аргументов!"));
                    return false;
                } else {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9/home invite &7<Ник>     &6-  &6Пригласить домой"));
                    return false;
                }
            } else if (args[0].equals("uninvite")) {
                if (args.length == 2) {
                    Home home = HomeStorage.getHomeByName(p.getName(), "Main");
                    if (!Objects.requireNonNull(home).getInvitedNames().contains(args2[1].trim())) {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Игрок и так не приглашен к вам в дом!"));
                    } else {
                        ArrayList<String> tmp = home.getInvitedNames();
                        tmp.remove(args2[1].trim());
                        home.setInvitedNames(tmp);
                        HomeStorage.replaceHome(p.getUniqueId().toString(), home, "Main");
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eИгрок &6" + args2[1] + " &eболее не приглашен к вам!"));
                    }
                } else {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cВы написали что то не то!"));
                }
                return false;
            } else if (args[0].equals("help")) {
                if (args.length == 1) {
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
                    return false;
                }
            } else if (args[0].equals("invited") && args.length == 1) {
                Home home = HomeStorage.getHomeByName(p.getName(), "Main");
                String list;
                if (Objects.requireNonNull(home).getInvitedNames().size() == 0) {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Нет приглашенных игроков"));
                } else {
                    StringBuilder listBuilder = new StringBuilder("&9Home &7▪ &6Приглашенные игроки: &e");
                    for (String s : home.getInvitedNames()) {
                        listBuilder.append(s.trim()).append(", &e");
                    }
                    list = listBuilder.toString();
                    list = list.substring(0, list.length() - 4);
                    p.sendMessage(ColorTranslator.translateColorCodes(list));
                }
                return false;
            } else if (args.length == 1 && args[0].trim().toLowerCase(Locale.ROOT).equals("public")) {
                Home home = HomeStorage.getHomeByName(p.getName(), "Main");
                if (Objects.isNull(home)) {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cУ вас нет дома!"));
                }
                else {
                    if (home.isPubl()) {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Ваш дом уже является открытым"));
                    } else {
                        home.setPubl(true);
                        HomeStorage.replaceHome(p.getUniqueId().toString(), home, "Main");
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Теперь ваш дом открыт для всех"));
                    }
                }
                return false;
            } else if (args.length == 1 && args[0].trim().toLowerCase(Locale.ROOT).equals("private")) {
                Home home = HomeStorage.getHomeByName(p.getName(), "Main");
                if (Objects.isNull(home)) {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cУ вас нет дома!"));
                }
                else {
                    if (!home.isPubl()) {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Ваш дом уже является закрытым"));
                    } else {
                        home.setPubl(false);
                        HomeStorage.replaceHome(p.getUniqueId().toString(), home, "Main");
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Теперь ваш дом не является общедоступным"));
                    }
                }
                return false;
           /* } else if (args[0].toLowerCase(Locale.ROOT).trim().equals("bed") && args.length == 1) {
                Home home = HomeStorage.getHomeByName(p.getName(), "bed");
                if (Objects.nonNull(home)) {
                    //if(home.getLocation().getBlock().getType()==Material.BED)
                    if (Teleporter.tryTp(p, home.getLocation())) {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eВы телепортировались к вашей кровати"));
                        return false;
                    }
                } else {
                    //home = HomeStorage.getAnyHomeByUID(p.getUniqueId().toString());
                    //if(Objects.nonNull(home)){
                    //    Location loc = new Location(McfineHome.getPlugin().getServer().getWorld(home.getWorld()), home.getX(), home.getY(), home.getZ(), home.getYaw(), home.getPitch());
                    //    p.teleport(loc);
                    //    p.sendMessage(ColorTranslator.translateColorCodes("&9[Home] &eВы телепортировались в дом "+home.getHomeName()));
                    //} else {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cУ вас нет такого дома! Поспите в кровати"));
                    //}
                }
                return false;*/
            } else if (args[0].equals("rename")) {
                if (args.length != 3) {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cНе верная команда. Используйте /home rename <старое имя> <новое имя>"));
                } else {
                    Home home = HomeStorage.getHomeByName(p.getUniqueId().toString(), args[1]);
                    if (Objects.isNull(home)) {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cУ вас нет дома с таким именем!"));
                    } else {
                        home.setHomeName(args[2]);
                        HomeStorage.replaceHomeName(p.getName(), home, args[1]);
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &aДом успешно переименован из &e" + args[1] + "&a в &e" + args[2]));
                    }
                }
                return false;
            } else if (args[0].equals("list")) {
                if (args.length != 1) {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cНе верная команда. Используйте /home list"));
                } else {
                    ArrayList<Home> list = HomeStorage.getHomeList(p.getUniqueId().toString());
                    if (Objects.isNull(list)) {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cУ вас нет ни одного дома"));
                    } else {
                        StringBuilder homesBuilder = new StringBuilder("&9Home &7▪ &eВаши дома: &6");
                        for (Home hm : list) {
                            homesBuilder.append(hm.getHomeName()).append("&e, &6");
                        }
                        String homes = homesBuilder.toString();
                        homes = homes.substring(0, homes.length() - 4);
                        p.sendMessage(ColorTranslator.translateColorCodes(homes));
                    }
                }
                return false;
            } else if(args.length==1 && args[0].equalsIgnoreCase("invitedme")){
                printInvitedmeNames(p);
            }
            else if (args.length == 1) {

                Home hm = HomeStorage.getHomeByName(p.getName(), args[0].toLowerCase(Locale.ROOT).trim());
                if (Objects.nonNull(hm)) {
                    if(Teleporter.tryTp(p,hm.getLocation())) {
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eВы телепортировались в дом: " + args[0]));
                    }
                    return false;
                }


                Home home = HomeStorage.getHomeByName(args[0], "Main");
                if (Objects.isNull(home)) {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cУ игрока нет главного дома. Установить командой &6/home set"));
                    return false;
                }
                if (home.isPubl() || home.isInvited(p.getName())) {
                    if (Teleporter.tryTp(p, home.getLocation())) {
                        if(home.getHomeName().equalsIgnoreCase("Main")){
                            p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eВы телепортировались в дом &6" + home.getPlayerName()));
                            return false;
                        }
                        p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eВы телепортировались в дом: &6" + home.getHomeName() + " &eигрока: &6" + home.getPlayerName()));
                    }
                } else {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cВас не пригласили к &6" + home.getPlayerName()));
                }
                return false;
            } else {
                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cКоманда неизвестна"));
            }


        }

        return false;
    }

    public static void delHome(Player p, String[] args) {
        if (args.length == 1) {
            Home home = HomeStorage.getHomeByName(p.getName(), "Main");
            Home home2 = HomeStorage.getAnyHomeByName(p.getName());
            if (Objects.nonNull(home)) {
                HomeStorage.deleteHome(p.getUniqueId().toString(), "Main");
                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eДом удален"));
            } else if (Objects.nonNull(home2)) {
                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cВы написали что то не то!"));
            } else {
                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6У вас нет дома!"));
            }
        } else if (args.length == 2) {
            Home home = HomeStorage.getHomeByName(p.getName(), args[1]);
            if (Objects.nonNull(home)) {
                HomeStorage.deleteHome(p.getUniqueId().toString(), args[1]);
                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &eДом &9" + args[1] + " &eудален"));
            } else {
                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cНе верное имя дома"));
            }
        } else {
            p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cВы написали что то не то"));
        }
    }

    public static void setHome(Player p, String[] args) {
        if (args.length == 1) {
            if (McfineHome.getPlugin().getConfig().getBoolean("onlyBedSetsHome")) {
                if (McfineHome.bybed) McfineHome.bybed = false;
                else
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Дом можно установить только поспав в кровати!"));
            }

            Home home = HomeStorage.getHomeByName(p.getName(), "Main");
            if (Objects.nonNull(home)) {

                Home hm = new Home(p, "Main");
                hm.setInvitedNames(home.getInvitedNames());
                HomeStorage.replaceHome(p.getUniqueId().toString(), hm, "Main");
                p.setBedSpawnLocation(hm.getLocation());
                p.setCompassTarget(hm.getLocation());
                p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Главный дом перемещен"));
            } else {
                if (HomeStorage.createHome(p, "Main")) {
                    p.setBedSpawnLocation(Objects.requireNonNull(HomeStorage.getHomeByName(p.getName(), "Main")).getLocation());
                    p.setCompassTarget(Objects.requireNonNull(HomeStorage.getHomeByName(p.getName(), "Main")).getLocation());
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &aТочка главного дома поставлена"));
                } else {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cОшибка! Обратитесь к администрации"));
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
            if (HomeStorage.getHomeNumber(p.getUniqueId().toString()) < max) {
                if (args[1].equals("invite") || args[1].equals("set") || args[1].equals("delete") || args[1].equals("invited") || args[1].equals("uninvite") || args[1].equals("private") || args[1].equals("public") || args[1].equals("help") || args[1].equals("rename")) {
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cЭто имя зарезервированно! Используйте другое"));
                    return;
                }
                Home home = HomeStorage.getHomeByName(p.getName(), args[1].toLowerCase(Locale.ROOT).trim());
                if (Objects.nonNull(home)) {

                    Home hm = new Home(p, args[1].toLowerCase(Locale.ROOT).trim());
                    hm.setInvitedNames(home.getInvitedNames());
                    HomeStorage.replaceHomeName(p.getName(), hm, args[1].toLowerCase(Locale.ROOT).trim());
                    p.setBedSpawnLocation(hm.getLocation());
                    p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &6Дом перемещен"));
                } else {
                    if (HomeStorage.createHome(p, args[1].toLowerCase(Locale.ROOT).trim())) {
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

    public static void printInvitedmeNames(Player p){
        ArrayList<Home> tmp=HomeStorage.getHomeSet();
        StringBuilder homesBuilder = new StringBuilder("&9Home &7▪ &eВы приглашены к: &6");
        boolean any=false;
        ArrayList<String> players = new ArrayList<>();
        for(Home hm : tmp){
            if(hm.isInvited(p.getName()) && !hm.getPlayerName().equalsIgnoreCase(p.getName()) && !players.contains(hm.getPlayerName())) {
                any=true;
                homesBuilder.append(hm.getPlayerName()).append("&e, &6");
                players.add(hm.getPlayerName());
            }
        }
        String homes = homesBuilder.toString();
        if(!any){
            p.sendMessage(ColorTranslator.translateColorCodes("&9Home &7▪ &cВас еще никто не пригласил"));
        } else{
            homes = homes.substring(0, homes.length() - 4);
            p.sendMessage(ColorTranslator.translateColorCodes(homes));
        }
    }


}
