package net.brian.heroesdungeon.api.world;

import net.brian.heroesdungeon.api.dungeon.properties.ImmutableProperties;
import org.bukkit.World;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public interface WorldService {

    String WORLD_NAMESPACE = "dungeon-";


    //Only run synchronized
    CompletableFuture<World> createCopyWorld(ImmutableProperties properties, UUID dungeonUUID);

    void removeWorld(UUID dungeonUUID);

    void clearWorlds();

}
