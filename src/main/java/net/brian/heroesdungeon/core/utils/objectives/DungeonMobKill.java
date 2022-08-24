package net.brian.heroesdungeon.core.utils.objectives;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.api.objectives.PersistentObjective;
import net.brian.scriptedquests.api.objectives.data.IntegerData;
import net.brian.scriptedquests.api.objectives.data.MapData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Map;

public class DungeonMobKill extends PersistentObjective<MapData> {

    Map<String,Integer> killRequire;

    public DungeonMobKill(String objectiveID, Map<String,Integer> killRequire) {
        super(objectiveID);
        this.killRequire = killRequire;
    }

    @EventHandler
    public void onKill(MythicMobDeathEvent event){
        if(event.getKiller() == null) return;
        processIfIsDoing(event.getKiller().getKiller(), (c,data)->{
            String type = event.getMobType().getInternalName();
            Integer require = killRequire.get(type);
            if(require == null || data.get(type) >= require){
                return;
            }
            data.add(type,1);
            if(checkComplete(data)) finish(c);
        });
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onMobDeath(MythicMobDeathEvent event) {
        String mobKey = getKey(event.getMobType().getInternalName());
        if (mobKey != null && event.getKiller() instanceof Player player) {
            this.processIfIsDoing(player, (completer, data) -> {
                int require = this.killRequire.get(mobKey);
                if (data.get(mobKey) < require) {
                    player.sendMessage(getInstruction(player));
                    data.add(mobKey, 1);
                    if (this.checkComplete(data)) {
                        this.finish(completer);
                    }
                }

            });
        }
    }

    private String getKey(String mobName){
        for (String s : killRequire.keySet()) {
            if(mobName.startsWith(s)){
                return s;
            }
        }
        return null;
    }

    private boolean checkComplete(MapData mapData){
        for (Map.Entry<String, Integer> entry : killRequire.entrySet()) {
            if(mapData.get(entry.getKey()) < entry.getValue())
                return false;
        }
        return true;
    }

    @Override
    public Class<MapData> getDataClass() {
        return MapData.class;
    }

    @Override
    public MapData newObjectiveData() {
        return new MapData();
    }
}
