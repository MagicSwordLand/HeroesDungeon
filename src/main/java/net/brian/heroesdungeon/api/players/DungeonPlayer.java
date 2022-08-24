package net.brian.heroesdungeon.api.players;

import net.brian.heroesdungeon.api.dungeon.DungeonInstance;
import net.brian.heroesdungeon.core.players.DungeonPlayerImpl;
import org.bukkit.entity.Player;

import java.util.Optional;

public interface DungeonPlayer {

    public Player getPlayer();

    void setDungeon(DungeonInstance dungeonInstance);

    boolean inDungeon();

    Optional<DungeonInstance> getOnGoingDungeon();

    static DungeonPlayer getInstance(Player player){
        return DungeonPlayerImpl.inst(player);
    }

}
