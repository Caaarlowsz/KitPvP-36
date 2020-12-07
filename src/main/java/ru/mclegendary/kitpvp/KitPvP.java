package ru.mclegendary.kitpvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

import ru.mclegendary.kitpvp.database.*;
import ru.mclegendary.kitpvp.event.KitPvPCountListeners;
import ru.mclegendary.kitpvp.placeholder.KTPExpansion;

public class KitPvP extends JavaPlugin {
    private static KitPvP main;

    public SQLManager data;
    public MySQL sql;

    @Override
    public void onEnable() {
        // Main start
        main = this;

        configSetup();
        placeholdersSetup();
        getLogger().info(ChatColor.DARK_PURPLE + "I'm in danger :(");

        // MySQL
        this.sql = new MySQL();
        this.data = new SQLManager(this);

        // Counter listeners with MySQL setup
        connectionSetup();
    }

    public static KitPvP getMain() {
        return main;
    }
    public SQLManager getSQLManager() {
        return data;
    }

    public void connectionSetup() {
        try {
            sql.connect();
        } catch (ClassNotFoundException | SQLException e) {
            getLogger().info(ChatColor.DARK_RED + "Something went wrong with connection to database!");
        }

        if (sql.isConnected()) {
            getLogger().info(ChatColor.GREEN + "MySQL database is connected :)");
            data.createTable();

            // Count listener
            Bukkit.getPluginManager().registerEvents(new KitPvPCountListeners(), this);
        }

    }

    public void placeholdersSetup() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().info(ChatColor.DARK_RED + "Something went wrong with PAPI :P");
            return;
        }

        new KTPExpansion().register();
    }

    public void configSetup() {
        if (new File(this.getDataFolder(), "config.yml").exists()) {
            return;
        }

        this.saveResource("config.yml", false);
    }

    @Override
    public void onDisable() {
        sql.disconnect();
        getLogger().info("Disabled.");
    }
}
