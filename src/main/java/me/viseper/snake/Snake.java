package me.viseper.snake;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Snake extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        System.out.println("Time for snake");
        getCommand("snakepos1").setExecutor(new Pos1());
        getCommand("snakepos2").setExecutor(new Pos2());
        getCommand("arena").setExecutor(new Arena());
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(new Movement(), this);

        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Goodbye Snake");
        this.saveConfig();
    }
}
