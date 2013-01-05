package com.cyprias.mydrops;

import org.bukkit.configuration.Configuration;

public class Config {
	private MyDrops plugin;
	private static Configuration config;
	
	public Config(MyDrops plugin) {
		this.plugin = plugin;
		config = plugin.getConfig().getRoot();
		config.options().copyDefaults(true);
		plugin.saveConfig();
		reloadOurConfig();
	}

	public void reloadOurConfig(){
		plugin.reloadConfig();
		config = plugin.getConfig().getRoot();
		loadConfigOpts();
	}
	
	public static int protectDuration;
	public static boolean checkNewVersionOnStartup, debugMessages;
	public static double protectRadius;
	
	private void loadConfigOpts(){
		protectDuration = config.getInt("protectDuration");
		checkNewVersionOnStartup =config.getBoolean("checkNewVersionOnStartup");
		debugMessages = config.getBoolean("debugMessages");
		protectRadius = config.getDouble("protectRadius");
	}
	
}
