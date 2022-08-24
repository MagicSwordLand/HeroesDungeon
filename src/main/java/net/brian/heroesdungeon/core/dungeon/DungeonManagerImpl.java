package net.brian.heroesdungeon.core.dungeon;

import net.brian.heroesdungeon.HeroesDungeon;
import net.brian.heroesdungeon.api.dungeon.DungeonInstance;
import net.brian.heroesdungeon.api.dungeon.DungeonManager;
import net.brian.heroesdungeon.api.dungeon.properties.DynamicProperties;
import net.brian.heroesdungeon.api.dungeon.properties.ImmutableProperties;
import net.brian.heroesdungeon.api.world.WorldService;
import net.brian.heroesdungeon.core.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;


public class DungeonManagerImpl implements DungeonManager {

    private final HashMap<UUID,DungeonInstance> onGoingDungeons = new HashMap<>();
    private final HashMap<String,DungeonFactory> dungeonFactories = new HashMap<>();



    private final HeroesDungeon plugin;
    private final WorldService worldService;

    public DungeonManagerImpl(HeroesDungeon plugin){
        worldService = plugin.getWorldService();
        this.plugin = plugin;

    }


    @Override
    public void startDungeon(String dungeon, List<Player> players, DynamicProperties properties) {
        DungeonFactory factory = dungeonFactories.get(dungeon);
        if(factory == null){
            Logger.debug("Can't find dungeon "+dungeon);
            return;
        }
        UUID dungeonID = UUID.randomUUID();
        ImmutableProperties immProps = factory.getProperties();
        Logger.debug("Creating World "+immProps.getWorldName());
        worldService.createCopyWorld(immProps,dungeonID).thenAccept(w->{
            if(w == null) {
                Logger.debug("Can't copy world and create world "+immProps.getWorldName());
                return;
            }
            Logger.debug("world created");
            Bukkit.getScheduler().runTask(plugin,()->{
                onGoingDungeons.put(dungeonID,factory.create(players,dungeonID,w,properties));
            });
        });
    }

    @Override
    public void registerDungeon(String dungeonID, DungeonFactory factory) {
        dungeonFactories.put(dungeonID,factory);
    }

    @Override
    public Collection<DungeonInstance> getDungeons() {
        return onGoingDungeons.values();
    }

    @Override
    public Optional<DungeonInstance> getOnGoingDungeon(Player player) {
        for (DungeonInstance dungeonInstance : onGoingDungeons.values()) {
            if(dungeonInstance.getPlayers().contains(player)){
                return Optional.of(dungeonInstance);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<DungeonInstance> getOnGoingDungeon(UUID dungeonUUID) {
        return Optional.ofNullable(onGoingDungeons.get(dungeonUUID));
    }

}
