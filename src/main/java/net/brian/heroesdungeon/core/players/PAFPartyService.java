package net.brian.heroesdungeon.core.players;

import de.simonsator.partyandfriends.main.PAFPlugin;
import de.simonsator.partyandfriendsgui.PAFGUIPlugin;
import de.simonsator.partyandfriendsgui.api.datarequest.DataRequestPlayerInfo;
import de.simonsator.partyandfriendsgui.api.datarequest.party.PartyDataRequestCallbackAPI;
import de.simonsator.partyandfriendsgui.communication.BungeecordCommunication;
import net.brian.heroesdungeon.HeroesDungeon;
import net.brian.heroesdungeon.api.players.PartyService;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PAFPartyService implements PartyService {

    private final HeroesDungeon plugin;

    public PAFPartyService(HeroesDungeon plugin){
        this.plugin = plugin;
    }




    @Override
    public void partyCallBack(Player player, Consumer<PartyData> callBack) {
        PartyDataRequestCallbackAPI.getInstance()
                .fetchPartyData(player,  (pPlayer, partyData, i)->{
                    if(partyData.DOES_EXIST){
                        List<UUID> members = new ArrayList<>(partyData.getPartyMembers().stream().map(d -> d.PLAYER_UUID).toList());
                        members.add(partyData.getPartyLeader().PLAYER_UUID);
                        callBack.accept(new PartyData(
                                partyData.getPartyLeader().PLAYER_UUID,
                                members
                        ));
                    }
                    else new PartyData(player.getUniqueId(),Collections.singletonList(player.getUniqueId()));
                });
    }
}
