package net.voidteam.networking.modules;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Robby Duke on 8/23/14
 * Copyright (c) 2014
 */
public class ModuleLoader {
    private static Logger LOG = Logger.getLogger("ModuleLoader");

    /**
     * Big thanks to votiifer, too stupid to write my own ClassLoader
     */
    public static List<JavaModule> load(String directory) /* throws Exception */{
        List<JavaModule> modules = new ArrayList<JavaModule>();
        File dir = new File(directory);

        if (!dir.exists()) {
            LOG.log(Level.WARNING, "No modules loaded! Cannot find module directory '" + dir + "' ");
            return modules;
        }

        ClassLoader loader;
        try {
            loader = new URLClassLoader(new URL[] { dir.toURI().toURL() }, JavaModule.class.getClassLoader());
        } catch (MalformedURLException ex) {
            LOG.log(Level.SEVERE, "Error while configuring module class loader", ex);
            return modules;
        }
        for (File file : dir.listFiles()) {
            if (!file.getName().endsWith(".class")) {
                continue; // Only load class files!
            }

            String name = file.getName().substring(0, file.getName().lastIndexOf("."));

            try {
                Class<?> clazz = loader.loadClass(name);
                Object object = clazz.newInstance();
                if (!(object instanceof JavaModule)) {
                    LOG.info("Not a module: " + clazz.getSimpleName());
                    continue;
                }

                JavaModule module = (JavaModule) object;
                modules.add(module);

                LOG.info("Loaded module: " + module.getClass().getSimpleName());
            }

			/*
			 * Catch the usual definition and dependency problems with a loader
			 * and skip the problem listener.
			 */
            catch (Exception ex) {
                LOG.log(Level.WARNING, "Error loading '" + name + "' module! Module disabled.");
                ex.printStackTrace();
            } catch (Error ex) {
                LOG.log(Level.WARNING, "Error loading '" + name + "' module! Module disabled.");
                ex.printStackTrace();

            }
        }

        return modules;
    }
}
