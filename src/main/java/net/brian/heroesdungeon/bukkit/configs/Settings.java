package net.brian.heroesdungeon.bukkit.configs;

import lombok.Getter;
import lombok.extern.java.Log;
import net.brian.heroesdungeon.HeroesDungeon;
import net.brian.heroesdungeon.core.utils.Logger;
import net.brian.playerdatasync.config.SpigotConfig;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Settings extends SpigotConfig {



    public static Location DEFAULT_LOBBY;
    public static GameMode DEFAULT_LOBBY_GAMEMODE;
    public static List<String> ALLOWED_COMMANDS;


    public Settings(JavaPlugin plugin) {
        super(plugin, "settings.yml");
        reload();
    }


    @Override
    public void reload(){
        loadFile();
        ALLOWED_COMMANDS = configuration.getStringList("ALLOWED_COMMANDS");
        DEFAULT_LOBBY_GAMEMODE = GameMode.valueOf(configuration.getString("DEFAULT_LOBBY_GAMEMODE",GameMode.ADVENTURE.name()));

        String defaultLobby = configuration.getString("DEFAULT_LOBBY","world:0:100:0:0:0");
        String[] args = defaultLobby.split(":");
        double x = Double.parseDouble(args[1]);
        double y = Double.parseDouble(args[2]);
        double z = Double.parseDouble(args[3]);
        float yaw = Float.parseFloat(args[4]);
        float pitch = Float.parseFloat(args[5]);

        Bukkit.getScheduler().runTask(HeroesDungeon.getInstance(),()->{
            World world = Bukkit.getWorld(args[0]);
            if(world != null){
                DEFAULT_LOBBY = new Location(world,x,y,z,yaw,pitch);
                Logger.debug("loaded default lobby "+DEFAULT_LOBBY.getWorld().getName());
            }
            else{
                Logger.warn("Can't find world "+ args[0]);
            }
        });
    }
}
