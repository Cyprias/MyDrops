package com.cyprias.mydrops;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

public class MyDrops  extends JavaPlugin {
	String pluginName;
	public static String chatPrefix = "§f[§cMyDrops§f] ";
	public Config config;
	public Events events;
	public VersionChecker versionChecker;
	private static Server server;
	
	public void onEnable() {
		pluginName = getDescription().getName();
		MyDrops.server = getServer();
		
		try {
			this.config = new Config(this);
		} catch (FileNotFoundException e1) {e1.printStackTrace();
		} catch (IOException e1) {e1.printStackTrace();
		} catch (InvalidConfigurationException e1) {e1.printStackTrace();
		}
		this.events = new Events(this);
		
		
		
		if (Config.checkNewVersionOnStartup == true)
			VersionChecker.retreiveVersionInfo(this, "http://dev.bukkit.org/server-mods/mydrops/files.rss");
		
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {}
		
		getServer().getPluginManager().registerEvents(this.events, this);
	}
	
	private static Logger log = Logger.getLogger("Minecraft");
	public static void info(String msg) {
		log.info(chatPrefix + msg);
	}

	
	public static double getUnixTime() {
		return (System.currentTimeMillis() / 1000D);
	}
	
}
