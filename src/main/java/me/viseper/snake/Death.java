package me.viseper.snake;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import javax.swing.text.html.parser.Entity;

public class Death {
    Plugin plugin = Snake.getPlugin(Snake.class);

    public void death() {
        EntityType entity = EntityType.PRIMED_TNT;
        Location loc = plugin.getConfig().getLocation("snake");
        World world = loc.getWorld();
        for(int i = 0; i < 20 * plugin.getConfig().getInt("size"); i++) {
            world.spawnEntity(loc, entity);
        }
        plugin.getConfig().set("dead", true);
    }

}
