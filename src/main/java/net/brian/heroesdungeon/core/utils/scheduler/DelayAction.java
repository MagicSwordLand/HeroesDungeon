package net.brian.heroesdungeon.core.utils.scheduler;

import net.brian.heroesdungeon.HeroesDungeon;
import net.brian.heroesdungeon.api.dungeon.DungeonInstance;
import org.bukkit.Bukkit;

public class DelayAction implements SchedulerAction{

    private final long ticks;

    public DelayAction(long ticks){
        this.ticks = ticks;
    }

    @Override
    public void run(DungeonInstance instance, Runnable onComplete) {
        Bukkit.getScheduler().runTaskLater(HeroesDungeon.getInstance(), onComplete,ticks);
    }

}
