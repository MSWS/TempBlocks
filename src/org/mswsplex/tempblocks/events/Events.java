package org.mswsplex.tempblocks.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.mswsplex.tempblocks.msws.TempBlocks;

import net.md_5.bungee.api.ChatColor;

public class Events implements Listener {
	private TempBlocks plugin;

	public Events(TempBlocks plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		if (item == null || item.getType() == Material.AIR)
			return;

		if (!item.hasItemMeta() || !item.getItemMeta().hasLore())
			return;

		String uuid = ChatColor.stripColor(item.getItemMeta().getLore().get(0));

		if (!plugin.isTempItem(uuid))
			return;

		Block block = event.getBlockPlaced();
		BlockState replaced = event.getBlockReplacedState();

		new BukkitRunnable() {
			@Override
			public void run() {
				block.setType(replaced.getType());
			}
		}.runTaskLater(plugin, plugin.getTime(uuid) / 50);
	}
}
