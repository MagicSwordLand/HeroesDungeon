package net.brian.heroesdungeon.api.scoreboard;

import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;

import java.util.List;

public interface ScoreboardService {


    void update(Player player,Object registry, List<String> lines);

    void removeLines(Player player,Object registery);
}
