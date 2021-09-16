package com.mcfine.mcfinehome.data;

import com.mcfine.mcfinehome.McfineHome;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Home {

    private double x, y, z;
    private float yaw, pitch;
    private String homeName;
    private String playerUid;
    private String playerName;
    private ArrayList<String> invitedNames;
    private String world;
    private boolean publ;

    public Home(double x, double y, double z, float yaw, float pitch, String homeName, String playerUid, String playerName, ArrayList<String> invitedNames, String world, boolean publ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.homeName = homeName;
        this.playerUid = playerUid;
        this.playerName = playerName;
        this.invitedNames = invitedNames;
        this.world = world;
        this.publ = publ;
    }

    public Home(Player p, String homeName) {
        this.x = p.getLocation().getX();
        this.y = p.getLocation().getY();
        this.z = p.getLocation().getZ();
        this.homeName = homeName;
        this.invitedNames = new ArrayList<>();
        this.world = p.getLocation().getWorld().getName();
        this.pitch = p.getLocation().getPitch();
        this.yaw = p.getLocation().getYaw();
        this.playerUid = p.getUniqueId().toString();
        this.playerName = p.getName();
        publ = McfineHome.getPlugin().getConfig().getBoolean("publicByDefault");
    }

    public double getX() {
        return x;
    }

    public boolean isPubl() {
        return publ;
    }

    public void setPubl(boolean publ) {
        this.publ = publ;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public Location getLocation() {
        return new Location(McfineHome.getPlugin().getServer().getWorld(world), x, y, z, yaw, pitch);
    }

    public boolean isInvited(String name) {
        for (String s : this.invitedNames) {
            if (s.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getPlayerUid() {
        return playerUid;
    }

    public void setPlayerUid(String playerUid) {
        this.playerUid = playerUid;
    }

    public String getPlayerName() {
        return playerName;
    }

    @SuppressWarnings("unused")
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public ArrayList<String> getInvitedNames() {
        return invitedNames;
    }

    public void setInvitedNames(ArrayList<String> invitedNames) {
        this.invitedNames = invitedNames;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public boolean invitePlayer(String playerName){
        if(this.invitedNames.contains(playerName))return false;
        else{
            this.invitedNames.add(playerName);
            return true;
        }
    }

    public boolean uninvitePlayer(String playerName){
        if(!this.invitedNames.contains(playerName))return false;
        else{
            this.invitedNames.remove(playerName);
            return true;
        }
    }

    public boolean setPublic(boolean pub){
        if(this.publ==pub) return false;
        else{
            this.publ=pub;
            return true;
        }
    }
}

