package ru.mclegendary.kitpvp.database;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import ru.mclegendary.kitpvp.KitPvP;

import static ru.mclegendary.kitpvp.KitPvP.getMain;

public class SQLManager {
    String table = getMain().getConfig().getString("MySQL.Database.Table");

    public SQLManager(KitPvP plugin) { }

    public void createTable() {
        try {
            PreparedStatement statement = getMain().sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + table
                    + " (uuid VARCHAR(40),username VARCHAR(20),kills INT(100),deaths INT(100),PRIMARY KEY (uuid))");
            statement.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void createPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        try {

            if (!playerExist(uuid)) {
                PreparedStatement statement = getMain().sql.getConnection().prepareStatement("INSERT IGNORE INTO " + table +
                        " (uuid,username) VALUES (?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, player.getName());
                statement.executeUpdate();
            }

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public boolean playerExist(UUID uuid) {
        try {
            PreparedStatement statement = getMain().sql.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE uuid=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }

        } catch (SQLException e) { e.printStackTrace(); }

        return false;
    }


    //Kills counter
    public int getKills(UUID uuid) {
        try {
            PreparedStatement statement = getMain().sql.getConnection().prepareStatement("SELECT kills FROM " + table + " WHERE uuid=?");
            statement.setString(1, uuid.toString());

            int kills;

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                kills = results.getInt("kills");
                return kills;
            }

        } catch (SQLException e) { e.printStackTrace(); }

        return 0;
    }

    public void addKills(UUID uuid, int kills) {
        try {
            PreparedStatement statement = getMain().sql.getConnection().prepareStatement("UPDATE " + table + " SET kills=? WHERE uuid=?");
            statement.setInt(1, (getKills(uuid) + kills));
            statement.setString(2, uuid.toString());
            statement.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }


    // Deaths counter
    public int getDeaths(UUID uuid) {
        try {
            PreparedStatement statement = getMain().sql.getConnection().prepareStatement("SELECT deaths FROM " + table + " WHERE uuid=?");
            statement.setString(1, uuid.toString());

            int deaths;

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                deaths = results.getInt("deaths");
                return deaths;
            }

        } catch (SQLException e) { e.printStackTrace(); }

        return 0;
    }

    public void addDeaths(UUID uuid, int deaths) {
        try {
            PreparedStatement statement = getMain().sql.getConnection().prepareStatement("UPDATE " + table + " SET deaths=? WHERE uuid=?");
            statement.setInt(1, (getDeaths(uuid) + deaths));
            statement.setString(2, uuid.toString());
            statement.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }
}
