package me.viseper.snake;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Feeding {

    Plugin plugin = Snake.getPlugin(Snake.class);
    public void eat() {
        plugin.getConfig().set("size", 1);
        new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = plugin.getConfig().getLocation("snake");
                Collection<Entity> entites = loc.getWorld().getNearbyEntities(loc,0.5,0.5,0.5);
                int size = plugin.getConfig().getInt("size");
                for (Entity entity : entites) {
                    if(entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.setHealth(0);
                        size++;
                    }
                }

                if(plugin.getConfig().getBoolean("dead")) {
                    cancel();
                }

                plugin.getConfig().set("size", size);
            }
        }.runTaskTimer(plugin, 5, 5);
    }

    public void destroy(Location loc) {
        int size = plugin.getConfig().getInt("size");
        System.out.println(loc);
        new BukkitRunnable() {

            @Override
            public void run() {
                loc.getBlock().setType(Material.AIR);
                System.out.println(loc);
            }
        }.runTaskLater(plugin, ((5 * size) + 20));
    }
}
