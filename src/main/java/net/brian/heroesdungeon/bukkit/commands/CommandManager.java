package net.brian.heroesdungeon.bukkit.commands;

import net.brian.heroesdungeon.HeroesDungeon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor {

    List<SubCommand> subCommands = new ArrayList<>();

    public CommandManager(HeroesDungeon plugin){

    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) return true;
        String name = args[0];
        for (SubCommand subCommand : subCommands) {
            if(subCommand.getName().equalsIgnoreCase(name)){
                subCommand.onCommand(sender,args);
                return true;
            }
        }
        return true;
    }
}
