package net.voidteam.networking;

import net.voidteam.networking.modules.JavaModule;
import net.voidteam.networking.modules.ModuleLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Robby Duke on 8/23/14
 * Copyright (c) 2014
 */
public class SocketCommunications extends JavaPlugin {
    public static List<JavaModule> loadedModules = new ArrayList<JavaModule>();
    private static Logger LOG = Logger.getLogger("SocketCommunications");

    @Override
    public void onDisable() {
        for(JavaModule module : loadedModules) {
            LOG.info(
                    String.format("Disabling module: %s", module.getClass().getSimpleName())
            );

            module.onDisable();
        }
    }

    @Override
    public void onEnable() {
        String moduleDirectory = getDataFolder().toString().replace("\\", "/") + "/modules";
        loadedModules = ModuleLoader.load(moduleDirectory);

        for(JavaModule module : loadedModules) {
            LOG.info(
                    String.format("Enabling module: %s", module.getClass().getSimpleName())
            );
            module.onEnable();
        }
    }

    public static Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin("SocketCommunications");
    }
}
