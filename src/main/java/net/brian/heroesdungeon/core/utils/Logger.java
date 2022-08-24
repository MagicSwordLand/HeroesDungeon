package net.brian.heroesdungeon.core.utils;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class Logger {

    public static boolean debugging = true;

    public static void debug(String msg){
        if(debugging){
            Bukkit.getLogger().log(Level.INFO,msg);
        }
    }

    public static void warn(String msg){
        Bukkit.getLogger().log(Level.WARNING,msg);
    }
}
