package net.brian.heroesdungeon.core.utils.scheduler;

import io.lumine.mythic.bukkit.MythicBukkit;
import net.brian.heroesdungeon.api.dungeon.DungeonInstance;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SpawnMobAction implements SchedulerAction {

    List<MobInfo> mobInfos = new ArrayList<>();

    public SpawnMobAction(MobInfo... mobInfos){
        this.mobInfos.addAll(Arrays.asList(mobInfos));
    }

    @Override
    public void run(DungeonInstance instance, Runnable onComplete) {
        mobInfos.forEach(m->m.spawn(instance));
        onComplete.run();
    }


    public record MobInfo(String type,double x,double y, double z,int level,int amount){

        public MobInfo(String type,double x,double y, double z){
            this(type,x,y,z,1,1);
        }

        public MobInfo(String type,double x,double y, double z,int level){
            this(type,x,y,z,level,1);
        }

        private void spawn(DungeonInstance instance){
            if(instance.isDisposed()) return;
            if(level > 1){
                Location loc = instance.fixed(x,y,z);
                for(int i=0;i<level;i++){
                    MythicBukkit.inst().getMobManager().spawnMob(type,loc,level);
                }
            }
            else MythicBukkit.inst().getMobManager().spawnMob(type,instance.fixed(x,y,z),level);
        }

    }

}
