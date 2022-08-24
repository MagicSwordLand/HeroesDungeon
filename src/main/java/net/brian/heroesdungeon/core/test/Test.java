package net.brian.heroesdungeon.core.test;

import net.brian.heroesdungeon.HeroesDungeon;
import net.brian.heroesdungeon.api.dungeon.DungeonInstance;
import net.brian.heroesdungeon.api.dungeon.DungeonManager;
import net.brian.heroesdungeon.api.dungeon.properties.DynamicProperties;
import net.brian.heroesdungeon.api.dungeon.properties.ImmutableProperties;
import net.brian.heroesdungeon.core.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Test implements Listener {

    public static void run(HeroesDungeon plugin){
        Bukkit.getPluginManager().registerEvents(new Test(),plugin);
        plugin.getDungeonManager().registerDungeon("test", new DungeonManager.DungeonFactory() {
            final ImmutableProperties prop = ImmutableProperties
                    .builder()
                    .worldName("CreepyCastle")
                    .lives(2)
                    .build();
            @Override
            public ImmutableProperties getProperties() {
                return prop;
            }

            @Override
            public DungeonInstance create(List<Player> players, UUID dungeonUUID, World world, DynamicProperties props) {
                Logger.debug("created test dungeon");
                return new TestDungeon(dungeonUUID,players,world,prop);
            }
        });
    }


}
