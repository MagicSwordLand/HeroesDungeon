package net.brian.heroesdungeon.core.utils.scheduler;

import net.brian.heroesdungeon.api.dungeon.DungeonInstance;

public interface SchedulerAction {

    void run(DungeonInstance instance,Runnable onComplete);

}
