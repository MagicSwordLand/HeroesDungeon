package net.brian.heroesdungeon.core.test;

import net.brian.heroesdungeon.api.dungeon.AbstractDungeon;
import net.brian.heroesdungeon.api.dungeon.properties.ImmutableProperties;
import net.brian.heroesdungeon.core.utils.Logger;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class TestDungeon extends AbstractDungeon {


    public TestDungeon(UUID uuid, List<Player> players, World world, ImmutableProperties props) {
        super(uuid, players, world, props);
        Logger.debug("TestDungeon created");
        for (Player player : players) {
            player.sendMessage("副本開始!");
        }

    }

    @Override
    public void onDispose() {
        for (Player player : players) {
            player.sendMessage("副本結束");
        }
    }

    @Override
    public void onNotifyPlayerQuit(Player player) {

    }

    @Override
    protected List<String> getScoreboardLines(Player player) {
        return null;
    }


}
