package ru.mclegendary.kitpvp.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static ru.mclegendary.kitpvp.KitPvP.getMain;

public class KitPvPCountListeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        getMain().getSQLManager().createPlayer(e.getPlayer());
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        Player victim = e.getEntity().getPlayer();
        Player killer = e.getEntity().getKiller();

        if (victim == null) return;

        getMain().getSQLManager().addDeaths(victim.getUniqueId(), 1);

        if (killer != null) {
            getMain().getSQLManager().addKills(killer.getUniqueId(), 1);
        }
    }
}
