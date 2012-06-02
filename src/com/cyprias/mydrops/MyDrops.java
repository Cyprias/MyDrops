package com.cyprias.mydrops;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MyDrops  extends JavaPlugin {
	String pluginName;
	public static String chatPrefix = "§f[§cMyDrops§f] ";
	public Config config;
	public Events events;
	
	public void onEnable() {
		pluginName = getDescription().getName();
		
		this.config = new Config(this);
		this.events = new Events(this);
		
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
		if (player.isOp()) {
			return true;
		}

		if (player.isPermissionSet(node))
			return player.hasPermission(node);

		String[] temp = node.split("\\.");
		String wildNode = temp[0];
		for (int i = 1; i < (temp.length); i++) {
			wildNode = wildNode + "." + temp[i];

			if (player.isPermissionSet(wildNode + ".*"))
				// plugin.info("wildNode1 " + wildNode+".*");
				return player.hasPermission(wildNode + ".*");

		}
		if (player.isPermissionSet(wildNode))
			return player.hasPermission(wildNode);

		if (player.isPermissionSet(wildNode))
			return player.hasPermission(wildNode);

		
		return player.hasPermission(pluginName.toLowerCase() + ".*");
	}
}
