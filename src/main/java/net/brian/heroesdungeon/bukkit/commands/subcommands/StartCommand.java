package net.brian.heroesdungeon.bukkit.commands.subcommands;

import net.brian.heroesdungeon.HeroesDungeon;
import net.brian.heroesdungeon.api.dungeon.properties.DynamicProperties;
import net.brian.heroesdungeon.api.dungeon.properties.ImmutableProperties;
import net.brian.heroesdungeon.bukkit.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.BatchUpdateException;
import java.util.UUID;
import java.util.stream.Collectors;

public class StartCommand extends SubCommand {

    public StartCommand(HeroesDungeon plugin) {
        super(plugin, "start");
    }


    // /dungeon start <player> <dungeon> <difficulty>
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("hd.admin")) return;
        if(args.length < 3) return;
        Player player = Bukkit.getPlayer(args[1]);
        if(player == null) return;
        plugin.getPartyService().partyCallBack(player,data->{
            if(data.leader().equals(player.getUniqueId())){
                for (UUID member : data.members()) {
                    if(Bukkit.getPlayer(member) == null){
                        player.sendMessage("隊伍還沒到齊");
                        return;
                    }
                }
                int[] difficulty = {0};
                if(args.length >= 4) difficulty[0] = Integer.parseInt(args[3]);
                plugin.getDungeonManager()
                        .startDungeon(args[2],
                                data.members().stream()
                                        .map(Bukkit::getPlayer).collect(Collectors.toList()),
                                DynamicProperties.builder()
                                        .difficulty(difficulty[0]).build()
                        );
            }
            else player.sendMessage("你不是隊伍的領導");
        });
    }


}
