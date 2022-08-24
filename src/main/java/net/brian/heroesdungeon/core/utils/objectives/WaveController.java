package net.brian.heroesdungeon.core.utils.objectives;

import lombok.Setter;
import net.brian.heroesdungeon.api.dungeon.DungeonInstance;
import net.brian.scriptedquests.api.objectives.Objective;
import net.brian.scriptedquests.api.player.ObjectiveCompleter;
import net.brian.scriptedquests.api.quests.CompletableContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class WaveController extends CompletableContainer {

    @Setter
    Consumer<DungeonInstance> endRunnable;

    final HashMap<String,Consumer<DungeonInstance>> objectiveStartProcess;

    public WaveController(String id) {
        super(id);
        objectiveStartProcess = new HashMap<>();
    }


    public void start(DungeonCompleter completer){
        for (Map.Entry<String, Objective> entry : completables.entrySet()) {
            entry.getValue().start(completer);
            completer.setCurrentObjective(entry.getValue());
            return;
        }
    }

    @Override
    public void onFinish(ObjectiveCompleter objectiveCompleter) {
        if(endRunnable != null && objectiveCompleter instanceof DungeonCompleter d){
            endRunnable.accept(d.getDungeonInstance());
        }
    }


    @Override
    public void onNextObjective(ObjectiveCompleter objectiveCompleter, Objective objective) {
        if(objectiveCompleter instanceof  DungeonCompleter d) {
            d.setCurrentObjective(objective);
            Consumer<DungeonInstance> c = objectiveStartProcess.get(objective.getObjectiveID());
            if(c != null) c.accept(d.getDungeonInstance());
        }
    }

    public void setObjectiveStartProcess(String objID,Consumer<DungeonInstance> d){
        objectiveStartProcess.put(objID,d);
    }

    public void pushObj(Objective obj){
        obj.setParent(this);
        completables.put(obj.getObjectiveID(),obj);
    }

    public void abort(DungeonInstance dungeonInstance){
        DungeonCompleter c = DungeonCompleter.getInstance(dungeonInstance);
        c.getCurrentObjective().ifPresent(o->{
            o.removeCache(c);
        });
    }
}
