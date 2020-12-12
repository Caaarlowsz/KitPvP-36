package ru.mclegendary.kitpvp.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

import ru.mclegendary.kitpvp.database.SQLManager;

import static ru.mclegendary.kitpvp.KitPvP.getMain;

public class KTPExpansion extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "ktp";
    }

    @Override
    public String getAuthor() {
        return "mclegendary";
    }

    @Override
    public String getVersion() {
        return "6.6";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null) return (ChatColor.RED + "Игрок не найден.");

        SQLManager data = getMain().getSQLManager();
        UUID playerUUID = player.getUniqueId();

        switch (params) {
            case "kills":
                return String.valueOf(data.getKills(playerUUID));

            case "deaths":
                return String.valueOf(data.getDeaths(playerUUID));
        }

        return null;
    }
}
