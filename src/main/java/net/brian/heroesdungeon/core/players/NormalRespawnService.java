package net.brian.heroesdungeon.core.players;

import net.brian.heroesdungeon.HeroesDungeon;
import net.brian.heroesdungeon.api.players.RespawnService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.util.io.BukkitObjectInputStream;

import javax.annotation.Nullable;
import java.util.HashMap;

public class NormalRespawnService implements RespawnService, Listener {

    HashMap<Player,Location> respawnPoints = new HashMap<>();

    public NormalRespawnService(HeroesDungeon plugin){
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        Location loc = respawnPoints.get(event.getPlayer());
        if(loc != null && loc.isWorldLoaded()){
            event.setRespawnLocation(loc);
        }
    }

    @Override
    public void setRespawnPoint(Player player,@Nullable Location loc) {
        respawnPoints.put(player,loc);
    }
}
