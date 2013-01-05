package com.cyprias.mydrops;

import java.io.IOException;

import org.bukkit.Server;
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
		
		this.config = new Config(this);
		this.events = new Events(this);
		
		this.server = getServer();
		
		if (Config.checkNewVersionOnStartup == true)
			VersionChecker.retreiveVersionInfo(this, "http://dev.bukkit.org/server-mods/mydrops/files.rss");
		
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {}
		
		getServer().getPluginManager().registerEvents(this.events, this);
	}
	
	static public void info(String msg) {
		server.getConsoleSender().sendMessage(chatPrefix + msg);
	}

	
	public static double getUnixTime() {
		return (System.currentTimeMillis() / 1000D);
	}
	
}
