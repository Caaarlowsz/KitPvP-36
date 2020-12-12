package ru.mclegendary.kitpvp.database;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.Synchronized;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import ru.mclegendary.kitpvp.KitPvP;

import static ru.mclegendary.kitpvp.KitPvP.getMain;

public class SQLManager {
    String table = YamlConfiguration.loadConfiguration(getMain().getConfigFile()).getString("MySQL.Database.Table");

    public SQLManager(KitPvP plugin) { }

    @SneakyThrows
    public void createTable() {
        @Cleanup Connection connection = getMain().getSQL().getConnection();

        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + table
                + " (uuid VARCHAR(40),username VARCHAR(20),kills INT(100),deaths INT(100),PRIMARY KEY (uuid))");
        statement.executeUpdate();
    }

    @Synchronized
    @SneakyThrows
    public void createPlayer(Player player) {
        UUID uuid = player.getUniqueId();

        if (!playerExist(uuid)) {
            @Cleanup Connection connection = getMain().getSQL().getConnection();

            PreparedStatement statement = connection.prepareStatement("INSERT IGNORE INTO " + table +
                        " (uuid,username) VALUES (?,?)");
            statement.setString(1, uuid.toString());
            statement.setString(2, player.getName());

            statement.executeUpdate();
        }

    }

    @Synchronized
    @SneakyThrows
    public boolean playerExist(UUID uuid) {
        @Cleanup Connection connection = getMain().getSQL().getConnection();

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE uuid=?");
        statement.setString(1, uuid.toString());

        ResultSet results = statement.executeQuery();
        if (results.next()) {
            return true;
        }

        return false;
    }

    // Kill counter
    @Synchronized
    @SneakyThrows
    public int getKills(UUID uuid) {
        @Cleanup Connection connection = getMain().getSQL().getConnection();

        PreparedStatement statement = connection.prepareStatement("SELECT kills FROM " + table + " WHERE uuid=?");
        statement.setString(1, uuid.toString());

        int kills;

        ResultSet results = statement.executeQuery();
        if (results.next()) {
            kills = results.getInt("kills");
            return kills;
        }

        return 0;
    }

    @Synchronized
    @SneakyThrows
    public void addKills(UUID uuid, int kills) {
        @Cleanup Connection connection = getMain().getSQL().getConnection();

        PreparedStatement statement = connection.prepareStatement("UPDATE " + table + " SET kills=? WHERE uuid=?");
        statement.setInt(1, (getKills(uuid) + kills));
        statement.setString(2, uuid.toString());

        statement.executeUpdate();
    }


    // Deaths counter
    @Synchronized
    @SneakyThrows
    public int getDeaths(UUID uuid) {
        @Cleanup Connection connection = getMain().getSQL().getConnection();

        PreparedStatement statement = connection.prepareStatement("SELECT deaths FROM " + table + " WHERE uuid=?");
        statement.setString(1, uuid.toString());

        int deaths;

        ResultSet results = statement.executeQuery();
        if (results.next()) {
            deaths = results.getInt("deaths");
            return deaths;
        }

        return 0;
    }

    @Synchronized
    @SneakyThrows
    public void addDeaths(UUID uuid, int deaths) {
        @Cleanup Connection connection = getMain().getSQL().getConnection();

        PreparedStatement statement = connection.prepareStatement("UPDATE " + table + " SET deaths=? WHERE uuid=?");
        statement.setInt(1, (getDeaths(uuid) + deaths));
        statement.setString(2, uuid.toString());

        statement.executeUpdate();
    }
}
