package ru.mclegendary.kitpvp.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

import static ru.mclegendary.kitpvp.KitPvP.getMain;

public class MySQL {
    private final HikariDataSource hikari;

    public MySQL() {
        final FileConfiguration config = YamlConfiguration.loadConfiguration(getMain().getConfigFile());
        HikariConfig hikariConfig = new HikariConfig();

        final String host = config.getString("MySQL.Host");
        final int port = config.getInt("MySQL.Port");
        final String database = config.getString("MySQL.Database.Name");
        final boolean useSSL = config.getBoolean("MySQL.UseSSL");
        final String username = config.getString("MySQL.Username");
        final String password = config.getString("MySQL.Password");

        final int maxPoolSize = config.getInt("MySQL.Max-Pool-Size");
        final int minPoolIdle = config.getInt("MySQL.Min-Pool-Idle");
        final int maxPoolLifetime = config.getInt("MySQL.Max-Pool-Lifetime");
        final int maxPoolTimeout = config.getInt("MySQL.Max-Pool-Timeout");


        hikariConfig.setMaximumPoolSize(maxPoolSize);
        hikariConfig.setMinimumIdle(minPoolIdle);
        hikariConfig.setMaxLifetime(maxPoolLifetime);
        hikariConfig.setConnectionTimeout(maxPoolTimeout);

        hikariConfig.setPoolName("JustStats MySQL Connection Pool");
        hikariConfig.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

        hikariConfig.addDataSourceProperty("serverName", host);
        hikariConfig.addDataSourceProperty("port", port);
        hikariConfig.addDataSourceProperty("databaseName", database);
        hikariConfig.addDataSourceProperty("user", username);
        hikariConfig.addDataSourceProperty("password", password);
        hikariConfig.addDataSourceProperty("useSSL", useSSL);

        this.hikari = new HikariDataSource(hikariConfig);
    }

    public final boolean isConnected() { return hikari != null && hikari.isRunning(); }

    public HikariDataSource getHikari() { return hikari; }

    public Connection getConnection() throws SQLException { return hikari.getConnection(); }

    public void disconnect() {
        if (isConnected()) {
            try {
                hikari.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
