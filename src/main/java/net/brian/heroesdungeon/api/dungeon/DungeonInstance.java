package net.brian.heroesdungeon.api.dungeon;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public interface DungeonInstance {

    @NotNull UUID getUniqueID();

    @NotNull Collection<Player> getPlayers();

    @NotNull Collection<Player> getLivingPlayers();

    @NotNull Collection<Player> getSpectators();

    void addSpectator(Player player);


    /**
     * @param player the player to remove
     * @return returns true if the player is playing the dungeon, and removes it
     */
    boolean removePlayer(Player player);

    @NotNull World getWorld();

    void dispose();
    boolean isDisposed();

    Location fixed(double x,double y, double z);
    Location fixed(double x,double y, double z,float yaw,float pitch);

}
