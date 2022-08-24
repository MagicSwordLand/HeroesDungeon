package net.brian.heroesdungeon.core.world;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.grinderwolf.swm.plugin.config.ConfigManager;
import net.brian.heroesdungeon.HeroesDungeon;
import net.brian.heroesdungeon.api.dungeon.properties.ImmutableProperties;
import net.brian.heroesdungeon.api.world.WorldService;
import net.brian.heroesdungeon.bukkit.configs.Settings;
import net.brian.heroesdungeon.core.utils.Logger;
import net.brian.heroesdungeon.core.utils.Pair;
import net.brian.playerdatasync.util.time.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class SlimeWorldService implements WorldService {

    private final SlimePlugin slimePlugin;
    private final HeroesDungeon dungeonPlugin;
    private final SlimeLoader slimeLoader;
    private final File worldsFolder;

    private final BlockingQueue<Pair<CountDownLatch,Long>> worldCreatingProcess = new LinkedBlockingQueue<>();

    public SlimeWorldService(HeroesDungeon dungeonPlugin){
        this.dungeonPlugin = dungeonPlugin;
        slimePlugin  = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        slimeLoader = slimePlugin.getLoader("file");
        worldsFolder = new File(ConfigManager.getDatasourcesConfig().getFileConfig().getPath());
        Bukkit.getScheduler().runTask(dungeonPlugin, this::clearWorlds);

        Bukkit.getScheduler().runTaskTimer(dungeonPlugin,()->{
            Pair<CountDownLatch, Long> pair = worldCreatingProcess.peek();
            if(pair != null){
                Logger.debug("not empty");
                if((System.currentTimeMillis()-pair.second()) > TimeUnit.SECOND){
                    pair.first().countDown();
                    worldCreatingProcess.poll();
                    Logger.debug("counted down");
                }
            }
        },0,20);
    }


    @Override
    public CompletableFuture<World> createCopyWorld(ImmutableProperties immProps, UUID dungeonUUID) {
        return CompletableFuture.supplyAsync(()->{
            File file = new File(worldsFolder,immProps.getWorldName()+".slime");
            if(!file.exists()) {
                Logger.debug("Can't find world "+file.getAbsolutePath());
                return null;
            }
            File targetFile = new File(worldsFolder,WORLD_NAMESPACE+dungeonUUID+".slime");
            try {
                FileUtils.copyFile(file,targetFile);
                SlimePropertyMap slimeProps = new SlimePropertyMap();

                slimeProps.setValue(SlimeProperties.ENVIRONMENT,immProps.getEnviroment());
                SlimeWorld slimeWorld = slimePlugin.loadWorld(slimeLoader,WORLD_NAMESPACE+dungeonUUID,false,slimeProps);
                if(slimeWorld == null){
                    Logger.debug("SlimePlugin could not load world "+WORLD_NAMESPACE+dungeonUUID);
                    return null;
                }
                CountDownLatch firstLatch = new CountDownLatch(1);
                Logger.debug("first latch created");
                worldCreatingProcess.add(new Pair<>(firstLatch,System.currentTimeMillis()));
                Logger.debug("added to queue awaiting now");
                firstLatch.await();
                Logger.debug(" first latch complete ");
                CountDownLatch secondLatch = new CountDownLatch(1);
                Logger.debug("second latch created");
                Bukkit.getScheduler().runTask(dungeonPlugin,()->{
                    Logger.debug("generating world");
                    slimePlugin.generateWorld(slimeWorld);
                    secondLatch.countDown();
                    Logger.debug("second latch count down");
                });
                Logger.debug("second latch waiting");
                secondLatch.await();
                return Bukkit.getWorld(WORLD_NAMESPACE+dungeonUUID);
            }
            catch (UnknownWorldException | WorldInUseException | IOException | CorruptedWorldException | NewerFormatException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public void removeWorld(UUID dungeonUUID) {
        String worldName = WORLD_NAMESPACE+dungeonUUID;
        World world = Bukkit.getWorld(worldName);
        ConfigManager.getWorldConfig().getWorlds().remove(worldName);
        if(world != null){
            world.getPlayers().forEach(p->p.teleport(Settings.DEFAULT_LOBBY));
            world.getEntities().forEach(Entity::remove);
            Bukkit.getScheduler().runTaskAsynchronously(dungeonPlugin,()->{
                try {
                    slimeLoader.deleteWorld(worldName);
                } catch (UnknownWorldException | IOException e) {
                    e.printStackTrace();
                }
            });
            Bukkit.unloadWorld(world,false);
        }
        else{
            Bukkit.getScheduler().runTaskAsynchronously(dungeonPlugin,()->{
                File file = new File(worldsFolder,worldName+".slime");
                if(file.exists()){
                    try {
                        FileUtils.forceDelete(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    @Override
    public void clearWorlds() {
        try {
            for (String worldName : slimeLoader.listWorlds()) {
                if(worldName.startsWith(WORLD_NAMESPACE)){
                    World world = Bukkit.getWorld(worldName);
                    ConfigManager.getWorldConfig().getWorlds().remove(worldName);
                    if(world != null){
                        world.getPlayers().forEach(p->p.teleport(Settings.DEFAULT_LOBBY));
                        Bukkit.unloadWorld(worldName,false);
                        slimeLoader.deleteWorld(worldName);
                    }
                    else FileUtils.delete(new File(worldsFolder,worldName+".slime"));
                }
            }
        } catch (IOException | UnknownWorldException e) {
            e.printStackTrace();
        }
        ConfigManager.getWorldConfig().save();
    }




}
