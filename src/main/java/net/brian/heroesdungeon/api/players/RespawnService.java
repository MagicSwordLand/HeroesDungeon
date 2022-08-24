package net.brian.heroesdungeon.api.players;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface RespawnService {

    void setRespawnPoint(Player player, Location loc);

}
