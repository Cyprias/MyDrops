package com.cyprias.mydrops;

import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.cyprias.mydrops.VersionChecker.VersionCheckerEvent;

public class Events implements Listener {
	private JavaPlugin plugin;

	public Events(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	// Location lastDeathLoc;
	// Player lastDeathPlayer;

	// HashMap<ItemStack, DropInfo> playerDrops2 = new HashMap<ItemStack,
	// DropInfo>();

	DeathInfo lastDeath;// = new DeathInfo();

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent event) {

		Player player = event.getEntity();
		if (!player.hasPermission("mydrops.protect")) {
			return;
		}
		if (Config.debugMessages)
			MyDrops.info(player.getName() + " has died, preping to protect their items.");

		if (lastDeath == null)
			lastDeath = new DeathInfo();
		
		lastDeath.setInfo(event.getEntity());


	}

	public static class DeathInfo {
		private double time;

		private Location location;
		private Player player;

		public Player getPlayer() {
			return this.player;
		}

		public Location getLocation() {
			return this.location;
		}

		public double getTime() {
			return this.time;
		}

		public void setInfo(Player entity) {
			this.player = entity;
			this.location = entity.getLocation();
			this.time = MyDrops.getUnixTime();
		}
	}

	public static class DropInfo {
		private Player owner;
		private Double time;

		public DropInfo(Player lastDeathPlayer) {
			this.owner = lastDeathPlayer;
			this.time = MyDrops.getUnixTime();
			// TODO Auto-generated constructor stub
		}

		public void setOwner(Player player) {
			this.owner = player;
		}

		public Player getOwner() {
			return this.owner;
		}

		public void setDropTime(Double time) {
			this.time = time;
		}

		public Double getDropTime() {
			return this.time;
		}
	}

	HashMap<Item, DropInfo> playerDrops = new HashMap<Item, DropInfo>();

	@EventHandler(priority = EventPriority.NORMAL)
	public void onItemSpawn(ItemSpawnEvent event) {
		if (event.isCancelled())
			return;

		Item item = event.getEntity();

		
		
		if (Config.debugMessages)
			MyDrops.info(event.getEventName()+" " + item.getType() + "(" + item.getEntityId() + ") = " + playerDrops.containsKey(item));
		
		if (lastDeath != null && lastDeath.getLocation().getWorld().equals(item.getWorld()) && (lastDeath.getTime()+1 >= (MyDrops.getUnixTime()))) {// 
			double dist = event.getEntity().getLocation().distance(lastDeath.getLocation());
			if (dist < Config.protectRadius) {

				if (Config.debugMessages)
					MyDrops.info("Protecting " + lastDeath.getPlayer().getName() + "'s " + item.getType() + "(" + item.getEntityId() + ")");

				playerDrops.put(item, new DropInfo(lastDeath.getPlayer()));
			//} else {
			//	plugin.info("distance " + dist);
			}

		}

	}

	/*
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.isCancelled())
			return;

		Entity item = event.getEntity();
		
		if (Config.debugMessages)
			MyDrops.info(event.getEventName()+" " + item.getType() + "(" + item.getEntityId() + ") = " + playerDrops.containsKey(item));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityCombust(EntityCombustEvent event) {
		if (event.isCancelled())
			return;

		Entity item = event.getEntity();
		if (Config.debugMessages)
			MyDrops.info(event.getEventName()+" " + item.getType() + "(" + item.getEntityId() + ") = " + playerDrops.containsKey(item));
		
		//if (playerDrops.containsKey(item)) {

		
		
			//if (MyDrops.getUnixTime() < (playerDrops.get(item).getDropTime() + Config.protectDuration)) {
				if (Config.debugMessages)
					MyDrops.info("Blocking item from combusting" + item.getType()
						+ "(" + item.getEntityId() + ")");
				event.setCancelled(true);
				
			//}
			
			//playerDrops.remove(item);
		//}
	}
*/
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onItemDespawn(ItemDespawnEvent event) {
		if (event.isCancelled())
			return;

	
		
		Item item = event.getEntity();
		if (Config.debugMessages)
			MyDrops.info(event.getEventName()+" " + item.getType() + "(" + item.getEntityId() + ") = " + playerDrops.containsKey(item));
		
		if (playerDrops.containsKey(item) && MyDrops.getUnixTime() < (playerDrops.get(item).getDropTime() + Config.protectDuration)) {
				if (Config.debugMessages)
					MyDrops.info("Blocking " + playerDrops.get(item).getOwner().getName() + "'s item from despawning" + item.getType()
						+ "(" + item.getEntityId() + ")");
				event.setCancelled(true);
				return;
			}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (event.isCancelled())
			return;

		Item item = event.getItem();
		
		//if (Config.debugMessages)
		//	plugin.info("onPlayerPickupItem " + player.getName() + " " + item.getType() + "(" + item.getEntityId() + ")");

		if (playerDrops.containsKey(item)) {
			Player player = event.getPlayer();
			
			if (player.hasPermission("mydrops.exempt")) {
				if (Config.debugMessages)
					MyDrops.info("Allowing " + player.getName() + " to pickup " + playerDrops.get(item).getOwner().getName() + "'s " + item.getType() + "("
						+ item.getEntityId() + ")");
				return;
			}

			if (playerDrops.get(item).getOwner().equals(player)) {
				if (Config.debugMessages)
					MyDrops.info("Allowing " + player.getName() + " to pickup " + item.getType() + "(" + item.getEntityId() + ")");
				return;
			}

			if (MyDrops.getUnixTime() < (playerDrops.get(item).getDropTime() + Config.protectDuration)) {
				if (Config.debugMessages)
					MyDrops.info("Blocking " + player.getName() + " from picking up " + playerDrops.get(item).getOwner().getName() + "'s " + item.getType()
						+ "(" + item.getEntityId() + ")");
				event.setCancelled(true);
				return;
			}
		}


	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onVersionCheckerEvent(VersionCheckerEvent event) {
		if (event.getPluginName() == plugin.getName()) {
			VersionChecker.versionInfo info = event.getVersionInfo(0);
			Object[] args = event.getArgs();

			String curVersion = plugin.getDescription().getVersion();

			if (args.length == 0) {

				int compare = VersionChecker.compareVersions(curVersion, info.getTitle());
				// plugin.info("curVersion: " + curVersion +", title: " +
				// info.getTitle() + ", compare: " + compare);
				if (compare < 0) {
					MyDrops.info("We're running v" + curVersion + ", v" + info.getTitle() + " is available");
					MyDrops.info(info.getLink());
				}

				return;
			}
		}
	}
}
