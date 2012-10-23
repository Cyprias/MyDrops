package com.cyprias.mydrops;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MyDrops  extends JavaPlugin {
	String pluginName;
	public static String chatPrefix = "§f[§cMyDrops§f] ";
	public Config config;
	public Events events;
	public VersionChecker versionChecker;
	
	public void onEnable() {
		pluginName = getDescription().getName();
		
		this.config = new Config(this);
		this.events = new Events(this);
		
		this.versionChecker = new VersionChecker(this, "http://dev.bukkit.org/server-mods/mydrops/files.rss");
		if (Config.checkNewVersionOnStartup == true)
			this.versionChecker.retreiveVersionInfo();
		
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {}
		
		getServer().getPluginManager().registerEvents(this.events, this);
	}
	
	public void info(String msg) {
		getServer().getConsoleSender().sendMessage(chatPrefix + msg);
	}

	
	public static double getUnixTime() {
		return (System.currentTimeMillis() / 1000D);
	}
	
	public boolean hasPermission(CommandSender sender, String node) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player player = (Player) sender;

		if (player.isPermissionSet(node)) // in case admin purposely set the
											// node to false.
			return player.hasPermission(node);

		if (player.isPermissionSet(pluginName.toLowerCase() + ".*"))
			return player.hasPermission(pluginName.toLowerCase() + ".*");

		String[] temp = node.split("\\.");
		String wildNode = temp[0];
		for (int i = 1; i < (temp.length); i++) {
			wildNode = wildNode + "." + temp[i];

			if (player.isPermissionSet(wildNode + ".*"))
				// plugin.info("wildNode1 " + wildNode+".*");
				return player.hasPermission(wildNode + ".*");
		}

		return player.hasPermission(node);
	}
}
