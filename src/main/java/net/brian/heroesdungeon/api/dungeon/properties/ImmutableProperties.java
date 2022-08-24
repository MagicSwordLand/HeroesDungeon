package net.brian.heroesdungeon.api.dungeon.properties;

import lombok.Builder;
import lombok.Getter;
import net.brian.heroesdungeon.bukkit.configs.Settings;
import org.bukkit.*;


@Builder
public class ImmutableProperties {

    @Getter
    private final String worldName;
    @Getter
    @Builder.Default
    private String enviroment = "normal";

    @Getter
    @Builder.Default
    private int maxPlayers = 4;

    @Getter
    @Builder.Default
    private int lives = 1;

    @Getter
    @Builder.Default
    private double spawnX = 0,spawnY = 100,spawnZ = 0;

    private Location lobby;

    private GameMode lobbyGameMode;

    @Getter
    @Builder.Default
    private GameMode gameMode = GameMode.SURVIVAL;

    @Getter
    @Builder.Default
    private Difficulty difficulty = Difficulty.EASY;

    @Getter
    @Builder.Default
    private long gameTime = 20*60*5;


    public Location getLobby(){
        if(lobby == null) return Settings.DEFAULT_LOBBY;
        return lobby;
    }

    public GameMode getLobbyGameMode(){
        if(lobbyGameMode == null) return Settings.DEFAULT_LOBBY_GAMEMODE;
        return lobbyGameMode;
    }

}
