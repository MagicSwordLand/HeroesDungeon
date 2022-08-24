package net.brian.heroesdungeon;

import lombok.Getter;
import lombok.Setter;
import net.brian.heroesdungeon.api.dungeon.DungeonInstance;
import net.brian.heroesdungeon.api.dungeon.DungeonManager;
import net.brian.heroesdungeon.api.players.PartyService;
import net.brian.heroesdungeon.api.players.RespawnService;
import net.brian.heroesdungeon.api.scoreboard.ScoreboardService;
import net.brian.heroesdungeon.api.world.WorldService;
import net.brian.heroesdungeon.bukkit.commands.CommandManager;
import net.brian.heroesdungeon.bukkit.configs.Settings;
import net.brian.heroesdungeon.core.dungeon.DungeonManagerImpl;
import net.brian.heroesdungeon.core.players.NormalRespawnService;
import net.brian.heroesdungeon.core.players.PAFPartyService;
import net.brian.heroesdungeon.core.scoreboard.EmptyScoreboardService;
import net.brian.heroesdungeon.core.test.Test;
import net.brian.heroesdungeon.core.world.SlimeWorldService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class HeroesDungeon extends JavaPlugin {

    @Getter
    private static HeroesDungeon instance;

    @Getter
    private WorldService worldService;

    @Getter
    private DungeonManager dungeonManager;

    @Getter
    private RespawnService respawnService;

    @Getter
    @Setter
    private PartyService partyService;

    @Getter
    @Setter
    private ScoreboardService scoreboardService = new EmptyScoreboardService();


    @Getter
    private Settings settings;

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pluginManager = Bukkit.getPluginManager();
        if(pluginManager.getPlugin("SlimeWorldManager") != null){
            worldService = new SlimeWorldService(this);
        }
        else{
            getLogger().log(Level.SEVERE, ChatColor.RED +"Can't find SlimeWorldManager Disabling plugin");
            pluginManager.disablePlugin(this);
            return;
        }
        if(pluginManager.getPlugin("PartyAndFriendsGUI") != null){
            partyService = new PAFPartyService(this);
        }
        else{
            getLogger().log(Level.SEVERE, ChatColor.RED +"Can't find PartyAndFriends Disabling plugin");
            pluginManager.disablePlugin(this);
            return;
        }
        new CommandManager(this);
        dungeonManager = new DungeonManagerImpl(this);
        respawnService = new NormalRespawnService(this);
        settings = new Settings(this);
        Test.run(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public void setRespawnService(RespawnService respawnService){
        if(this.respawnService instanceof NormalRespawnService normalRespawnService){
            HandlerList.unregisterAll(normalRespawnService);
        }
        this.respawnService = respawnService;
    }

}
