package me.viseper.snake;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena implements CommandExecutor {

    Plugin plugin = Snake.getPlugin(Snake.class);
    Death death = new Death();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            Location loc1 = plugin.getConfig().getLocation("pos1");
            Location loc2 = plugin.getConfig().getLocation("pos2");
            build(loc1, loc2, Material.PINK_STAINED_GLASS, Material.PINK_WOOL);
            controls(loc1, loc2);
            summon(loc1, loc2);
            player.sendMessage("Center of arena is: " + center(loc1.getX(), loc2.getX()) + ", "
                    + center(loc1.getY(), loc2.getY()) + ", " + center(loc1.getZ(), loc2.getZ()) + ".");

        }else {
            System.out.println("Must be executed by a player.");
        }
        return true;
    }

    public void build(Location pos1, Location pos2, Material outside, Material floor) {
        double temp = 0;
        if(pos1.getX() > pos2.getX()) {
            temp = pos1.getX();
            pos1.setX(pos2.getX());
            pos2.setX(temp);
        }
        if(pos1.getY() > pos2.getY()) {
            temp = pos1.getY();
            pos1.setY(pos2.getY());
            pos2.setY(temp);
        }
        if(pos1.getZ() > pos2.getZ()) {
            temp = pos1.getZ();
            pos1.setZ(pos2.getZ());
            pos2.setZ(temp);
        }
        for(double x = pos1.getX(); x <= pos2.getX(); x++) {
            for(double y = pos1.getY(); y <= pos2.getY() + 1; y++) {
                for(double z = pos1.getZ(); z <= pos2.getZ(); z++) {
                    Location pos = new Location(pos1.getWorld(),x,y,z);
                    pos.getBlock().setType(outside);
                }
            }
        }
        for(double x = pos1.getX() + 1; x <= pos2.getX() - 1; x++) {
            for(double y = pos1.getY() + 1; y <= pos2.getY(); y++) {
                for(double z = pos1.getZ() + 1; z <= pos2.getZ() - 1; z++) {
                    Location pos = new Location(pos1.getWorld(),x,y,z);
                    pos.getBlock().setType(Material.AIR);
                }
            }
        }

        for(double x = pos1.getX() + 1; x <= pos2.getX() - 1; x++) {
                for(double z = pos1.getZ() + 1; z <= pos2.getZ() - 1; z++) {
                    Location pos = new Location(pos1.getWorld(),x,pos1.getY(),z);
                    pos.getBlock().setType(floor);
                    pos = new Location(pos1.getWorld(),x,pos1.getY() + 1,z);
                    pos.getBlock().setType(Material.AIR);
                    pos = new Location(pos1.getWorld(),x,pos1.getY() + 2,z);
                    pos.getBlock().setType(Material.AIR);
                }
            }
    }

    public void controls(Location pos1, Location pos2) {
        Location cent = new Location(pos1.getWorld(), 0,0,0);
        double temp = 0;
        if(pos1.getY() > pos2.getY()) {
            temp = pos1.getY();
            pos1.setY(pos2.getY());
            pos2.setY(temp);
        }
        cent.setX(center(pos1.getX(), pos2.getX()));
        cent.setY(pos2.getY() + 2);
        cent.setZ(center(pos1.getZ(), pos2.getZ()));
        cent.getBlock().setType(Material.NOTE_BLOCK);
        plugin.getConfig().set("down", cent.getBlock().getLocation());
        cent.setY(cent.getY() + 4);
        cent.getBlock().setType(Material.NOTE_BLOCK);
        plugin.getConfig().set("up", cent.getBlock().getLocation());
        cent.setY(cent.getY() - 2);
        cent.setX(cent.getX() + 2);
        cent.getBlock().setType(Material.NOTE_BLOCK);
        plugin.getConfig().set("north", cent.getBlock().getLocation());
        cent.setX(cent.getX() - 4);
        cent.getBlock().setType(Material.NOTE_BLOCK);
        plugin.getConfig().set("south", cent.getBlock().getLocation());
        cent.setX(cent.getX() + 2);
        cent.setZ(cent.getZ() + 2);
        cent.getBlock().setType(Material.NOTE_BLOCK);
        plugin.getConfig().set("east", cent.getBlock().getLocation());
        cent.setZ(cent.getZ() - 4);
        cent.getBlock().setType(Material.NOTE_BLOCK);
        plugin.getConfig().set("west", cent.getBlock().getLocation());

    }

    public double center(double pos1, double pos2) {
        double temp = pos1 - pos2;
        temp = Math.abs(temp);
        temp = temp / 2;
        return pos1 + temp;
    }

    public void summon(Location pos1, Location pos2) {
        Location cent = new Location(pos1.getWorld(), 0,0,0);
        double temp = 0;
        if(pos1.getX() > pos2.getX()) {
            temp = pos1.getX();
            pos1.setX(pos2.getX());
            pos2.setX(temp);
        }
        if(pos1.getY() > pos2.getY()) {
            temp = pos1.getY();
            pos1.setY(pos2.getY());
            pos2.setY(temp);
        }
        if(pos1.getZ() > pos2.getZ()) {
            temp = pos1.getZ();
            pos1.setZ(pos2.getZ());
            pos2.setZ(temp);
        }
        cent.setX(center(pos1.getX(), pos2.getX()));
        cent.setY(pos1.getY() + 1);
        cent.setZ(center(pos1.getZ(), pos2.getZ()));
        cent.getBlock().setType(Material.BLACK_CONCRETE);
        plugin.getConfig().set("snake", cent);
        plugin.getConfig().set("direction", "none");
        plugin.getConfig().set("colour", true);
        plugin.getConfig().set("size", 1);
        plugin.getConfig().set("locations", new ArrayList<>());
        plugin.getConfig().set("dead", false);
        Feeding feed = new Feeding();
        feed.eat();

        new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = plugin.getConfig().getLocation("snake");
                Location ahead = loc;
                int tailSize = plugin.getConfig().getInt("tailsize");
                int size = plugin.getConfig().getInt("size");
                boolean colour = plugin.getConfig().getBoolean("colour");
                Death death = new Death();

                List<String> tailList;
                List<Location> tailLoc;

                if(!plugin.getConfig().getStringList("locations").isEmpty()) {
                    tailList = plugin.getConfig().getStringList("locations");
                    tailLoc = toLocations(tailList);
                }else {
                    tailList = new ArrayList<>();
                    tailLoc = new ArrayList<>();
                }

                if(tailLoc.size() > size) {
                    tailLoc.get(0).getBlock().setType(Material.AIR);
                    tailLoc.remove(0);
                }

                if(plugin.getConfig().getString("direction") == "up") {
                    ahead.add(0,1,0);
                    if(ahead.getBlock().getType().equals(Material.PINK_STAINED_GLASS) ||
                            ahead.getBlock().getType().equals(Material.PINK_STAINED_GLASS) ||
                            ahead.getBlock().getType().equals(Material.PINK_CONCRETE) ||
                            ahead.getBlock().getType().equals(Material.RED_CONCRETE)) {
                        death.death();
                        cancel();
                    }else {
                        ahead.add(0,-1,0);
                        if(colour) {
                            loc.getBlock().setType(Material.RED_CONCRETE);
                            plugin.getConfig().set("colour", false);
                        }else {
                            loc.getBlock().setType(Material.PINK_CONCRETE);
                            plugin.getConfig().set("colour", true);
                        }
                    }
                    loc.add(0,1,0);
                    loc.getBlock().setType(Material.BLACK_CONCRETE);
                   plugin.getConfig().set("snake", loc);
                }else if(plugin.getConfig().getString("direction") == "down") {
                    ahead.add(0,-1,0);
                    if(ahead.getBlock().getType().equals(Material.PINK_STAINED_GLASS) ||
                            ahead.getBlock().getType().equals(Material.PINK_STAINED_GLASS) ||
                            ahead.getBlock().getType().equals(Material.PINK_CONCRETE) ||
                            ahead.getBlock().getType().equals(Material.RED_CONCRETE)) {
                        death.death();
                        cancel();
                    }else {
                        ahead.add(0,1,0);
                        if(colour) {
                            loc.getBlock().setType(Material.RED_CONCRETE);
                            plugin.getConfig().set("colour", false);
                            
                        }else {
                            loc.getBlock().setType(Material.PINK_CONCRETE);
                            plugin.getConfig().set("colour", true);
                            
                        }
                    }
                    loc.add(0,-1,0);
                    loc.getBlock().setType(Material.BLACK_CONCRETE);
                   plugin.getConfig().set("snake", loc);
                }else if(plugin.getConfig().getString("direction") == "north") {
                    ahead.add(1,0,0);
                    if(ahead.getBlock().getType().equals(Material.PINK_STAINED_GLASS) ||
                            ahead.getBlock().getType().equals(Material.PINK_STAINED_GLASS) ||
                            ahead.getBlock().getType().equals(Material.PINK_CONCRETE) ||
                            ahead.getBlock().getType().equals(Material.RED_CONCRETE)) {
                        death.death();
                        cancel();
                    }else {
                        ahead.add(-1,0,0);
                        if(colour) {
                            loc.getBlock().setType(Material.RED_CONCRETE);
                            plugin.getConfig().set("colour", false);
                            
                        }else {
                            loc.getBlock().setType(Material.PINK_CONCRETE);
                            plugin.getConfig().set("colour", true);
                            
                        }
                    }
                    loc.add(1,0,0);
                    loc.getBlock().setType(Material.BLACK_CONCRETE);
                   plugin.getConfig().set("snake", loc);
                }else if(plugin.getConfig().getString("direction") == "south") {
                    ahead.add(-1,0,0);
                    if(ahead.getBlock().getType().equals(Material.PINK_STAINED_GLASS) ||
                            ahead.getBlock().getType().equals(Material.PINK_STAINED_GLASS) ||
                            ahead.getBlock().getType().equals(Material.PINK_CONCRETE) ||
                            ahead.getBlock().getType().equals(Material.RED_CONCRETE)) {
                        death.death();
                        cancel();
                    }else {
                        ahead.add(1,0,0);
                        if(colour) {
                            loc.getBlock().setType(Material.RED_CONCRETE);
                            plugin.getConfig().set("colour", false);
                            
                        }else {
                            loc.getBlock().setType(Material.PINK_CONCRETE);
                            plugin.getConfig().set("colour", true);
                            
                        }
                    }
                    loc.add(-1,0,0);
                    loc.getBlock().setType(Material.BLACK_CONCRETE);
                   plugin.getConfig().set("snake", loc);
                }else if(plugin.getConfig().getString("direction") == "east") {
                    if(ahead.getBlock().getType().equals(Material.PINK_STAINED_GLASS) ||
                            ahead.getBlock().getType().equals(Material.PINK_STAINED_GLASS) ||
                            ahead.getBlock().getType().equals(Material.PINK_CONCRETE) ||
                            ahead.getBlock().getType().equals(Material.RED_CONCRETE)) {
                        death.death();
                        cancel();
                    }else {
                        if(colour) {
                            loc.getBlock().setType(Material.RED_CONCRETE);
                            plugin.getConfig().set("colour", false);
                            
                        }else {
                            loc.getBlock().setType(Material.PINK_CONCRETE);
                            plugin.getConfig().set("colour", true);
                            
                        }
                    }
                    loc.add(0,0,1);
                    loc.getBlock().setType(Material.BLACK_CONCRETE);
                   plugin.getConfig().set("snake", loc);
                }else if(plugin.getConfig().getString("direction") == "west") {
                    ahead.add(0,0,-1);
                    if(ahead.getBlock().getType().equals(Material.PINK_STAINED_GLASS) ||
                            ahead.getBlock().getType().equals(Material.PINK_STAINED_GLASS) ||
                            ahead.getBlock().getType().equals(Material.PINK_CONCRETE) ||
                            ahead.getBlock().getType().equals(Material.RED_CONCRETE)) {
                        death.death();
                        cancel();
                    }else {
                        ahead.add(0,0,1);
                        if(colour) {
                            loc.getBlock().setType(Material.RED_CONCRETE);
                            plugin.getConfig().set("colour", false);
                            
                        }else {
                            loc.getBlock().setType(Material.PINK_CONCRETE);
                            plugin.getConfig().set("colour", true);
                            
                        }
                    }
                    loc.add(0,0,-1);
                    loc.getBlock().setType(Material.BLACK_CONCRETE);
                   plugin.getConfig().set("snake", loc);
                }

                //String place = "tail:" + (tailSize);
                //plugin.getConfig().set(place, loc);
                //plugin.getConfig().set("tailsize", tailSize + 1);

                tailLoc.add(loc);
                tailList.clear();
                tailList.addAll(toStrings(tailLoc));

                plugin.getConfig().set("locations", tailList);

                if(loc.getX() < pos1.getX() || loc.getX() > pos2.getX() ||
                        loc.getY() < pos1.getY() || loc.getY() > pos2.getY() ||
                        loc.getZ() < pos1.getZ() || loc.getZ() > pos2.getZ()) {
                    death.death();
                    cancel();
                }else if(plugin.getConfig().getBoolean("dead")) {
                    death.death();
                    cancel();
                }

            }
        }.runTaskTimer(plugin, 10, 5);
    }

    public List<String> toStrings(List<Location> raw) {
        List<String> output = new ArrayList<>();
        for (Location s : raw) {
            output.add(toStr(s));
        }
        return output;


    }

    public String toStr(Location raw) {
        double x = raw.getX();
        double y = raw.getY();
        double z = raw.getZ();
        World world = raw.getWorld();
        String str = world.getName() + "," + x + "," + y + "," + z;
        return str;
    }

    public List<Location> toLocations(List<String> raw)
    {
        List<Location> output = new ArrayList<>();
        for (String s : raw) {
            output.add(toLocation(s));
        }
        return output;
    }

    public Location toLocation(String raw)
    {
        String[] spliced = raw.split(",");

        World world = Bukkit.getWorld(spliced[0]);
        double x = Double.valueOf(spliced[1]);
        double y = Double.valueOf(spliced[2]);
        double z = Double.valueOf(spliced[3]);

        return new Location(world, x, y, z);
    }
}
