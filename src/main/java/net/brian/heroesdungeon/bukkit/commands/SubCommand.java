package net.brian.heroesdungeon.bukkit.commands;

import lombok.Getter;
import net.brian.heroesdungeon.HeroesDungeon;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    @Getter
    private final String name;
    protected final HeroesDungeon plugin;

    public SubCommand(HeroesDungeon plugin, String name){
        this.name = name;
        this.plugin = plugin;
    }


    public abstract void onCommand(CommandSender sender,String[] args);

}
