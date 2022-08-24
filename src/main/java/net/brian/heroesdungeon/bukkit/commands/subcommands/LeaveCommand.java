package net.brian.heroesdungeon.bukkit.commands.subcommands;

import net.brian.heroesdungeon.HeroesDungeon;
import net.brian.heroesdungeon.bukkit.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand extends SubCommand {
    public LeaveCommand(HeroesDungeon plugin) {
        super(plugin, "leave");
    }

    // /hd leave <player>

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(args.length == 1 && sender instanceof Player player){
            plugin.getDungeonManager().getOnGoingDungeon(player).ifPresent(d->
                    d.removePlayer(player)
            );
        }
        else if(sender.hasPermission("hd.admin") && args.length >= 2){
            Player target = Bukkit.getPlayer(args[1]);
            if(target != null){
                plugin.getDungeonManager().getOnGoingDungeon(target).ifPresent(d->{
                    d.removePlayer(target);
                });
            }
        }
    }
}
