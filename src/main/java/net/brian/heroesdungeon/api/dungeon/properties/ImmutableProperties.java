package net.brian.heroesdungeon.api.dungeon.properties;

import lombok.Builder;
import lombok.Getter;
import net.brian.heroesdungeon.bukkit.configs.Settings;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;


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

    @Getter
    @Builder.Default
    private Location lobby = Settings.DEFAULT_LOBBY;

    @Getter
    @Builder.Default
    private GameMode lobbyGameMode = Settings.DEFAULT_LOBBY_GAMEMODE;

}
