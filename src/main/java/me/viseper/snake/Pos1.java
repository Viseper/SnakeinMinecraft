package me.viseper.snake;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Pos1 implements CommandExecutor {

    Plugin plugin = Snake.getPlugin(Snake.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            Location loc = player.getLocation();
            Block blockLoc = loc.getBlock();
            loc = blockLoc.getLocation();
            plugin.getConfig().set("pos1", loc);

        }else {
            System.out.println("Must be executed by a player.");
        }

        return true;
    }
}
