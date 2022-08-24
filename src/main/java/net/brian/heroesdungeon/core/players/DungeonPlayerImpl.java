package net.brian.heroesdungeon.core.players;

import lombok.Getter;
import lombok.Setter;
import net.brian.heroesdungeon.api.dungeon.DungeonInstance;
import net.brian.heroesdungeon.api.players.DungeonPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class DungeonPlayerImpl implements DungeonPlayer {

    private static final HashMap<UUID,DungeonPlayer> players = new HashMap<>();

    DungeonInstance dungeonInstance;

    @Getter
    private final Player player;
    private DungeonPlayerImpl(Player player){
        this.player = player;
    }


    @Override
    public void setDungeon(DungeonInstance dungeonInstance) {
        this.dungeonInstance = dungeonInstance;
    }

    @Override
    public boolean inDungeon() {
        return dungeonInstance == null || dungeonInstance.isDisposed();
    }

    @Override
    public Optional<DungeonInstance> getOnGoingDungeon() {
        if(inDungeon()) return Optional.of(dungeonInstance);
        return Optional.empty();
    }

    public static DungeonPlayer inst(Player player){
        DungeonPlayer d = players.get(player.getUniqueId());
        if(d != null && d.getPlayer().equals(player)){
            return d;
        }
        d = new DungeonPlayerImpl(player);
        players.put(player.getUniqueId(),d);
        return d;
    }
}
