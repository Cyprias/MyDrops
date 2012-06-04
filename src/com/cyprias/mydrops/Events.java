package com.cyprias.mydrops;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {
	private MyDrops plugin;

	public Events(MyDrops plugin) {
		this.plugin = plugin;
	}

	Location lastDeathLoc;
	Player lastDeathPlayer;

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent event) {
		
		if (plugin.hasPermission(event.getEntity(), "mydrops.protect")){
			lastDeathLoc = event.getEntity().getLocation();
			lastDeathPlayer = event.getEntity();
		}
	}

	public static class dropInfo {
		Player player;
		String playerName;
		Location loc;
		Double protectionExpires;

		public dropInfo(Player a, Location b) {
			player = a;
			playerName = a.getName();
			loc = b;
			protectionExpires = MyDrops.getUnixTime() + Config.protectDuration;
		}
	}

	HashMap<Item, dropInfo> playerDrops = new HashMap<Item, dropInfo>();

	@EventHandler(priority = EventPriority.NORMAL)
	public void onItemSpawn(ItemSpawnEvent event) {
		if (event.isCancelled())
			return;

		// plugin.info("getUniqueId " + event.getEntity().getUniqueId());

		if (lastDeathLoc != null && lastDeathLoc.getWorld().equals(event.getEntity().getWorld())) {
			double dist = event.getEntity().getLocation().distance(lastDeathLoc);
			// plugin.info("distance " + dist);
			if (dist < 0.1) {
			//	plugin.info("onItemSpawn " + event.getEntity().getType());
			//	plugin.info("onItemSpawn " + event.getEntity().getEntityId());

				// Item x = event.getEntity();

				playerDrops.put(event.getEntity(), new dropInfo(lastDeathPlayer, event.getEntity().getLocation())); // lastDeathPlayer.getName()

			}

		}

	}

	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (event.isCancelled())
			return;

		Item x = event.getItem();

		if (playerDrops.containsKey(x)) {

			dropInfo info = playerDrops.get(x);
			// plugin.info("onPlayerPickupItem2 " + event.getPlayer() + " " +
			// event.getItem().getEntityId() + " " + (info.protectionExpires -
			// MyDrops.getUnixTime()));

			if (info.playerName.equalsIgnoreCase(event.getPlayer().getName())){
				playerDrops.remove(x);
				return;
			}
			if (plugin.hasPermission(event.getPlayer(), "mydrops.exempt")){
				playerDrops.remove(x);
				return;
			}
			
			if (MyDrops.getUnixTime() < info.protectionExpires) {
				event.setCancelled(true);
				return;
			}

			
			
		}

	}

}
