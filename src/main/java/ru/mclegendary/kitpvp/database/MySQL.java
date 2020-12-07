package ru.mclegendary.kitpvp.database;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static ru.mclegendary.kitpvp.KitPvP.getMain;

public class MySQL {
    private final FileConfiguration config = YamlConfiguration.loadConfiguration(new File(getMain().getDataFolder(), "config.yml"));

    private final String host = config.getString("MySQL.Host");
    private final String port = config.getString("MySQL.Port");
    private final String database = config.getString("MySQL.Database.Name");
    private final String useSSL = config.getString("MySQL.UseSSL");
    private final String username = config.getString("MySQL.Username");
    private final String password = config.getString("MySQL.Password");

    private Connection connection;

    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if (isConnected()) {
            getMain().getLogger().info(ChatColor.RED + "Database is already connected!");
            return;
        }

        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + useSSL, username, password);
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
