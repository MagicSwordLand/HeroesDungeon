package net.brian.heroesdungeon.api.dungeon;

import lombok.Getter;
import net.brian.heroesdungeon.HeroesDungeon;
import net.brian.heroesdungeon.api.dungeon.properties.ImmutableProperties;
import net.brian.heroesdungeon.bukkit.configs.Settings;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class AbstractDungeon implements DungeonInstance , Listener {

    @Getter
    private final UUID uniqueID;
    @Getter
    protected final World world;
    @Getter
    protected final Set<Player> players = new HashSet<>();
    @Getter
    protected final List<Player> spectators = new ArrayList<>();
    protected final HashMap<Player,Integer> alivePlayersAndLives = new HashMap<>();
    protected final ImmutableProperties properties;

    @Getter
    protected boolean disposed = false;
    protected Location respawnPoint;

    protected final BukkitTask scoreboardTask;

    public AbstractDungeon(UUID uuid, List<Player> players, World world, ImmutableProperties immProps){
        this.uniqueID = uuid;
        this.world = world;
        this.players.addAll(players);
        Bukkit.getPluginManager().registerEvents(this,HeroesDungeon.getInstance());
        properties = immProps;
        respawnPoint = fixed(immProps.getSpawnX(),immProps.getSpawnY(),immProps.getSpawnZ());
        this.players.forEach(p-> {
            p.teleport(respawnPoint);
            HeroesDungeon.getInstance().getRespawnService().setRespawnPoint(p,respawnPoint);
            alivePlayersAndLives.put(p, immProps.getLives());
        });
        scoreboardTask = Bukkit.getScheduler().runTaskTimer(HeroesDungeon.getInstance(),()->{
            players.forEach(p->HeroesDungeon.getInstance().getScoreboardService().update(p,getScoreboardLines(p)));
        },0,1);
    }


    @Override
    public @NotNull Collection<Player> getLivingPlayers() {
        return alivePlayersAndLives.keySet();
    }

    @Override
    public void addSpectator(Player player) {
        alivePlayersAndLives.remove(player);
        players.add(player);
        player.setGameMode(GameMode.SPECTATOR);
        HeroesDungeon.getInstance().getRespawnService().setRespawnPoint(player,properties.getLobby());
    }

    @Override
    public boolean removePlayer(Player player) {
        if(players.remove(player)){
            spectators.remove(player);
            player.teleport(properties.getLobby());
            player.setGameMode(properties.getLobbyGameMode());
            if(alivePlayersAndLives.remove(player) != null){
                HeroesDungeon.getInstance().getRespawnService().setRespawnPoint(player, properties.getLobby());
                return true;
            }
        }
        return false;
    }

    @Override
    public void dispose() {
        disposed = true;
        HandlerList.unregisterAll(this);
        players.forEach(p->{
            HeroesDungeon.getInstance().getRespawnService().setRespawnPoint(p,properties.getLobby());
            p.teleport(properties.getLobby());
            p.setGameMode(properties.getLobbyGameMode());
        });
        HeroesDungeon.getInstance().getWorldService().removeWorld(uniqueID);
        onDispose();
    }

    protected abstract void onDispose();
    protected abstract void onNotifyPlayerQuit(Player player);
    protected abstract List<String> getScoreboardLines(Player player);

    @Override
    public Location fixed(double x, double y, double z) {
        return fixed(x,y,z,0,0);
    }

    @Override
    public Location fixed(double x, double y, double z, float yaw,float pitch) {
        return new Location(world,x,y,z,yaw,pitch);
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Integer lives = alivePlayersAndLives.get(event.getPlayer());
        if(lives != null){
            if(lives == 1) {
                addSpectator(event.getPlayer());
                if(alivePlayersAndLives.isEmpty()){
                    dispose();
                }
                else onNotifyPlayerQuit(event.getPlayer());
            }
            else {
                alivePlayersAndLives.put(event.getPlayer(),lives-1);
            };
        }
    }



    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        if(removePlayer(event.getPlayer())){
            if(alivePlayersAndLives.isEmpty()){
                dispose();
            }
            else onNotifyPlayerQuit(event.getPlayer());
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        if(players.contains(event.getPlayer())){
            for (String cmd : Settings.ALLOWED_COMMANDS) {
                if(!event.getMessage().startsWith(cmd)){
                    if(event.getPlayer().hasPermission("dungeon.admin")){
                        event.getPlayer().sendMessage("在副本中是不能使用此指令的，但因你有權限，所以可以");
                        event.setCancelled(true);
                    }
                    else event.getPlayer().sendMessage("副本中不能使用此指令");
                }
            }
        }
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event){
        if(alivePlayersAndLives.containsKey(event.getPlayer())){
            removePlayer(event.getPlayer());
        }
    }
}
