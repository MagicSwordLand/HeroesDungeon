package net.brian.heroesdungeon.api.scoreboard;

import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;

import java.util.List;

public interface ScoreboardService {


    void update(Player player, List<String> lines);

}
