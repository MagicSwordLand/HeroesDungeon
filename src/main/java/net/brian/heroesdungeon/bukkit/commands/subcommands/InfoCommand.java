package net.brian.heroesdungeon.bukkit.commands.subcommands;

import net.brian.heroesdungeon.HeroesDungeon;
import net.brian.heroesdungeon.api.dungeon.DungeonInstance;
import net.brian.heroesdungeon.bukkit.commands.SubCommand;
import org.bukkit.command.CommandSender;

public class InfoCommand extends SubCommand {

    public InfoCommand(HeroesDungeon plugin) {
        super(plugin, "info");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("hd.admin")) return;
        for (DungeonInstance dungeon : plugin.getDungeonManager().getDungeons()) {
            sender.sendMessage(dungeon.getUniqueID());
            dungeon.getLivingPlayers().forEach(p->{
                sender.sendMessage("  存活玩家: "+p.getName());
            });
            dungeon.getSpectators().forEach(p->{
                sender.sendMessage("  觀戰中: "+p.getName());
            });
        }
    }
}
