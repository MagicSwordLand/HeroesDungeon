package net.brian.heroesdungeon.core.utils.objectives;

import lombok.Getter;
import lombok.Setter;
import net.brian.heroesdungeon.api.dungeon.DungeonInstance;
import net.brian.scriptedquests.api.objectives.Objective;
import net.brian.scriptedquests.api.player.ObjectiveCompleter;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class DungeonCompleter extends ObjectiveCompleter {


    @Getter
    private final DungeonInstance dungeonInstance;

    private static final HashMap<UUID,DungeonCompleter> instances = new HashMap<>();

    @Setter
    private Objective currentObjective = null;

    private DungeonCompleter(DungeonInstance dungeonInstance) {
        super(dungeonInstance.getUniqueID());
        this.dungeonInstance = dungeonInstance;
    }

    @Override
    public Collection<Player> getPlayers() {
        return dungeonInstance.getLivingPlayers();
    }

    @Override
    public boolean removePlayer(Player player) {
        return dungeonInstance.removePlayer(player);
    }

    @Override
    public boolean alive() {
        if(dungeonInstance.isDisposed()) {
            instances.remove(dungeonInstance.getUniqueID());
            return false;
        }
        return true;
    }

    public static DungeonCompleter getInstance(DungeonInstance dungeonInstance){
        DungeonCompleter d = instances.get(dungeonInstance.getUniqueID());
        if(d == null || !d.dungeonInstance.equals(dungeonInstance)){
            d = new DungeonCompleter(dungeonInstance);
            instances.put(dungeonInstance.getUniqueID(),d);
        }
        return d;
    }


    public Optional<Objective> getCurrentObjective(){
        return Optional.ofNullable(currentObjective);
    }

    @Override
    public void run(Consumer<Player> consumer) {
        dungeonInstance.getLivingPlayers().forEach(consumer);
    }

}
