package net.brian.heroesdungeon.core.world;

import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.plugin.SWMPlugin;
import com.grinderwolf.swm.plugin.config.ConfigManager;
import com.grinderwolf.swm.plugin.config.WorldData;
import com.grinderwolf.swm.plugin.config.WorldsConfig;
import com.grinderwolf.swm.plugin.loaders.LoaderUtils;
import com.grinderwolf.swm.plugin.log.Logging;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class UnloadUtil {

    public static void unloadWorld(World world) {
        SlimeLoader loader = LoaderUtils.getLoader("file");
        Bukkit.unloadWorld(world, true);
        unlockWorldFinally(world, loader);
    }

    private static void unlockWorldFinally(World world, SlimeLoader loader) {
        String worldName = world.getName();
        System.out.println("Attempting to unlock world.. " + worldName + ".");
        try {
            if (loader != null && loader.isWorldLocked(worldName)) {
                System.out.println("World.. " + worldName + " is locked.");
                loader.unlockWorld(worldName);
                System.out.println("Attempted to unlock world.. " + worldName + ".");
            } else {
                System.out.println(worldName + " was not unlocked. This could be because the world is either unlocked or not in the config. This is not an error");
            }
        } catch (IOException | UnknownWorldException var6) {
            var6.printStackTrace();
        }
    }
}
