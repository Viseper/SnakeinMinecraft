package me.viseper.snake;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Movement implements Listener {

    Plugin plugin = Snake.getPlugin(Snake.class);

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Material noteblock = Material.NOTE_BLOCK;
        if(event.getClickedBlock() != null) {
            Location loc = event.getClickedBlock().getLocation();
            if (event.getClickedBlock().getType() == noteblock) {
                if (loc.equals(plugin.getConfig().getLocation("up"))) {
                    plugin.getConfig().set("direction", "up");
                } else if (loc.equals(plugin.getConfig().getLocation("down"))) {
                    plugin.getConfig().set("direction", "down");
                } else if (loc.equals(plugin.getConfig().getLocation("north"))) {
                    plugin.getConfig().set("direction", "north");
                } else if (loc.equals(plugin.getConfig().getLocation("south"))) {
                    plugin.getConfig().set("direction", "south");
                } else if (loc.equals(plugin.getConfig().getLocation("east"))) {
                    plugin.getConfig().set("direction", "east");
                } else if (loc.equals(plugin.getConfig().getLocation("west"))) {
                    plugin.getConfig().set("direction", "west");
                } else {
                    player.sendMessage("Noteblock is unregonized.");
                }
                plugin.saveConfig();
            }
        }

    }
}
