package org.mswsplex.tempblocks.blocks;

import org.bukkit.scheduler.BukkitRunnable;
import org.mswsplex.tempblocks.msws.TempBlocks;

public class BlockScheduler {
	public BlockScheduler(TempBlocks plugin) {
		new BukkitRunnable() {
			@Override
			public void run() {
				
			}
		}.runTaskLater(plugin, 5);
	}
}
