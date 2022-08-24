package net.brian.heroesdungeon.api.dungeon;

import io.netty.handler.address.DynamicAddressConnectHandler;
import net.brian.heroesdungeon.api.dungeon.properties.DynamicProperties;
import net.brian.heroesdungeon.api.dungeon.properties.ImmutableProperties;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DungeonManager {

    void startDungeon(String dungeon, List<Player> players,DynamicProperties immutableProperties);

    void registerDungeon(String dungeonID, DungeonFactory factory);

    Collection<DungeonInstance> getDungeons();

    Optional<DungeonInstance> getOnGoingDungeon(Player player);

    Optional<DungeonInstance> getOnGoingDungeon(UUID dungeonUUID);

    interface DungeonFactory{
        ImmutableProperties getProperties();
        DungeonInstance create(List<Player> players, UUID dungeonUUID, World world, DynamicProperties props);
    }

}
