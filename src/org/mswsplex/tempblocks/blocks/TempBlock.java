package org.mswsplex.tempblocks.blocks;

import org.bukkit.Location;
import org.bukkit.Material;

public class TempBlock {
	private Location loc;
	private Material blockType;
	private long lifetime;

	private boolean isPlaced;

	public TempBlock(Location loc, Material type) {
		new TempBlock(loc, type, 1000);
	}

	public TempBlock(Location loc, Material type, long lifetime) {
		this.loc = loc;
		this.blockType = type;
		this.lifetime = System.currentTimeMillis() + lifetime;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public Material getBlockType() {
		return blockType;
	}

	public void setBlockType(Material blockType) {
		this.blockType = blockType;
	}

	public long getLifetime() {
		return lifetime;
	}

	public long getTimeToRemoval() {
		return System.currentTimeMillis() - lifetime;
	}

	public void remove() {
		if (loc.getBlock().getType() == blockType) {
			loc.getBlock().setType(Material.AIR);
			isPlaced = false;
		}
	}

	public void place() {
		if (loc.getBlock().getType() == Material.AIR) {
			loc.getBlock().setType(blockType);
			isPlaced = true;
		}
	}

	public boolean shouldRemove() {
		return System.currentTimeMillis() > lifetime;
	}

	public boolean isPlaced() {
		return isPlaced;
	}
}
