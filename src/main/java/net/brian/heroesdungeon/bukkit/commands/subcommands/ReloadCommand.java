package net.brian.heroesdungeon.bukkit.commands.subcommands;

import net.brian.heroesdungeon.HeroesDungeon;
import net.brian.heroesdungeon.bukkit.commands.SubCommand;
import net.brian.heroesdungeon.bukkit.configs.Settings;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {
    public ReloadCommand(HeroesDungeon plugin) {
        super(plugin, "reload");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission("hd.admin")){
            plugin.getSettings().reload();
            sender.sendMessage("config reloaded");
        }
    }
}
