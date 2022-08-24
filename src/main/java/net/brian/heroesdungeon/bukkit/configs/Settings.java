package net.brian.heroesdungeon.bukkit.configs;

import net.brian.playerdatasync.config.SpigotConfig;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

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
        ALLOWED_COMMANDS = configuration.getStringList("allowed_commands");
        DEFAULT_LOBBY_GAMEMODE = GameMode.valueOf(configuration.getString("lobby_gamemode",GameMode.ADVENTURE.name()));

        DEFAULT_LOBBY = new Location(Bukkit.getWorld("world"),0,100,0);
    }
}
