package net.brian.heroesdungeon.bukkit.commands.subcommands;

import net.brian.heroesdungeon.HeroesDungeon;
import net.brian.heroesdungeon.bukkit.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectatorCommand extends SubCommand {
    public SpectatorCommand(HeroesDungeon plugin) {
        super(plugin, "spec");
    }


    //dungeon spec <player>
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission("hd.admin") && args.length >= 2){
            if(sender instanceof Player player){
                Player target = Bukkit.getPlayer(args[1]);
                if(target != null){
                    plugin.getDungeonManager().getOnGoingDungeon(target)
                            .ifPresentOrElse(dungeon -> dungeon.addSpectator(player),()->{
                                player.sendMessage("此玩家不在遊戲中");
                            });
                }
            }
            else sender.sendMessage("此指令只能由玩家執行");
        }
    }
}
