package net.brian.heroesdungeon.api.players;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PartyService {


    CompletableFuture<PartyData> getParty(Player player);

    /**
     * The data of a party,
     * the members contains the leader
     */
    record PartyData(UUID leader, List<UUID> members){};

}
