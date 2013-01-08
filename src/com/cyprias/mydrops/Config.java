package com.cyprias.mydrops;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;

public class Config {
	private static MyDrops plugin;
	private static Configuration config;
	
	public Config(MyDrops plugin) throws FileNotFoundException, IOException, InvalidConfigurationException {
		this.plugin = plugin;
		config = plugin.getConfig().getRoot();
		config.options().copyDefaults(true);
		plugin.saveConfig();
		reloadOurConfig();
	}

	public void reloadOurConfig() throws FileNotFoundException, IOException, InvalidConfigurationException{
		plugin.reloadConfig();
		config = plugin.getConfig().getRoot();
		loadConfigOpts();
	}
	
	public static int protectDuration;
	public static boolean checkNewVersionOnStartup, debugMessages;
	public static double protectRadius;
	
	private void loadConfigOpts() throws FileNotFoundException, IOException, InvalidConfigurationException{
		protectDuration = config.getInt("protectDuration");
		checkNewVersionOnStartup =config.getBoolean("checkNewVersionOnStartup");
		debugMessages = config.getBoolean("debugMessages");
		protectRadius = config.getDouble("protectRadius");
		
		loadBlacklist();
	}
	
//	static HashMap<ItemStack, Boolean> blacklistedItems = new HashMap<ItemStack, Boolean>();
	static List<ItemStack> blacklistedItems = new ArrayList<ItemStack>();
	
	static public Boolean isBlacklsited(ItemStack is){
		for (int i=0;i<blacklistedItems.size();i++){
			if (blacklistedItems.get(i).getTypeId() == is.getTypeId() && blacklistedItems.get(i).getDurability() == is.getDurability())
				return true;
		}
		
		return false;
	}
	
	private static void loadBlacklist() throws FileNotFoundException, IOException, InvalidConfigurationException{
		blacklistedItems.clear();
		
		YML yml = new YML(plugin.getResource("blacklist.yml"),plugin.getDataFolder(), "blacklist.yml");

		List<String> blacklistStrings = yml.getStringList("blacklist");
		//.getConfigurationSection("blacklist");
		
		for (String itemString : blacklistStrings) {
			String[] splitString = itemString.split(":");
			
			int typeId = (splitString.length>0) ? Integer.parseInt(splitString[0]) : 0;
			short durability = (splitString.length>1) ? Short.parseShort(splitString[1]) : 0;

			ItemStack is = new ItemStack(typeId, 1);
			is.setDurability(durability);

			blacklistedItems.add(is);
			
			if (debugMessages)
				MyDrops.info("Added " + is.getType() + ":" + is.getDurability() + " to blacklist.");

		}
		
		
	}
	
}
