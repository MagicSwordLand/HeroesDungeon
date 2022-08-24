package net.brian.heroesdungeon.core.players;

import de.simonsator.partyandfriends.main.PAFPlugin;
import de.simonsator.partyandfriendsgui.PAFGUIPlugin;
import de.simonsator.partyandfriendsgui.communication.BungeecordCommunication;
import net.brian.heroesdungeon.HeroesDungeon;
import net.brian.heroesdungeon.api.players.PartyService;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class PAFPartyService implements PartyService {

    private final HeroesDungeon plugin;

    public PAFPartyService(HeroesDungeon plugin){
        this.plugin = plugin;
    }



    @Override
    public CompletableFuture<PartyData> getParty(Player player) {
        return CompletableFuture.supplyAsync(()->new PartyData(player.getUniqueId(), Collections.singletonList(player.getUniqueId())));
    }

}
