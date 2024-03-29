package org.mswsplex.tempblocks.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.mswsplex.tempblocks.msws.TempBlocks;

public class PlayerManager {
	private TempBlocks plugin;

	private Map<OfflinePlayer, CPlayer> players;

	public PlayerManager(TempBlocks plugin) {
		this.plugin = plugin;
		players = new HashMap<>();
	}

	public CPlayer getPlayer(OfflinePlayer player) {
		if (!players.containsKey(player))
			players.put(player, new CPlayer(player, plugin));
		return players.get(player);
	}

	public List<OfflinePlayer> getLoadedPlayers() {
		return new ArrayList<OfflinePlayer>(players.keySet());
	}

	public void removePlayer(OfflinePlayer player) {
		if (players.containsKey(player))
			players.get(player).saveData();
		players.remove(player);
	}

	public void clearPlayers() {
		for (OfflinePlayer player : players.keySet())
			removePlayer(player);
	}
}
