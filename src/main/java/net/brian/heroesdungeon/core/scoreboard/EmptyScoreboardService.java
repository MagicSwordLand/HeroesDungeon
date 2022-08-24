package net.brian.heroesdungeon.core.scoreboard;

import net.brian.heroesdungeon.api.scoreboard.ScoreboardService;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;

import java.util.List;

public class EmptyScoreboardService implements ScoreboardService {

    @Override
    public void update(Player player,Object registery, List<String> lines) {

    }

    @Override
    public void removeLines(Player player, Object registery) {

    }

}
