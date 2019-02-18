package org.mswsplex.tempblocks.msws;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mswsplex.tempblocks.commands.TempBlockCommand;
import org.mswsplex.tempblocks.data.CPlayer;
import org.mswsplex.tempblocks.data.PlayerManager;
import org.mswsplex.tempblocks.events.Events;
import org.mswsplex.tempblocks.utils.MSG;

public class TempBlocks extends JavaPlugin {
	public FileConfiguration config, data, lang, gui;
	public File configYml = new File(getDataFolder(), "config.yml"), dataYml = new File(getDataFolder(), "data.yml"),
			langYml = new File(getDataFolder(), "lang.yml"), guiYml = new File(getDataFolder(), "guis.yml");

	private PlayerManager pManager;

	private Map<String, Long> tempItems;

	public void onEnable() {
		if (!configYml.exists())
			saveResource("config.yml", true);
		if (!langYml.exists())
			saveResource("lang.yml", true);
		if (!guiYml.exists())
			saveResource("guis.yml", true);
		config = YamlConfiguration.loadConfiguration(configYml);
		data = YamlConfiguration.loadConfiguration(dataYml);
		lang = YamlConfiguration.loadConfiguration(langYml);
		gui = YamlConfiguration.loadConfiguration(guiYml);

		new TempBlockCommand(this);
		new Events(this);

		MSG.plugin = this;
		pManager = new PlayerManager(this);

		tempItems = new HashMap<>();

		ConfigurationSection items = data.getConfigurationSection("Items");

		if (items != null)
			for (String pid : items.getKeys(false)) {
				tempItems.put(pid, Long.valueOf(items.getString(pid)));
			}

		MSG.log("&aSuccessfully Enabled!");
	}

	public void addTempItem(String uuid, long time) {
		tempItems.put(uuid, time);
		data.set("Items." + uuid, time);
	}

	public boolean isTempItem(String uuid) {
		return tempItems.containsKey(uuid);
	}

	public long getTime(String uuid) {
		return tempItems.containsKey(uuid) ? tempItems.get(uuid) : -1;
	}

	public void onDisable() {
		for (OfflinePlayer p : pManager.getLoadedPlayers())
			pManager.removePlayer(p);

		saveData();
	}

	public void saveData() {
		try {
			data.save(dataYml);
		} catch (Exception e) {
			MSG.log("&cError saving data file");
			MSG.log("&a----------Start of Stack Trace----------");
			e.printStackTrace();
			MSG.log("&a----------End of Stack Trace----------");
		}
	}

	public void saveConfig() {
		try {
			config.save(configYml);
		} catch (Exception e) {
			MSG.log("&cError saving data file");
			MSG.log("&a----------Start of Stack Trace----------");
			e.printStackTrace();
			MSG.log("&a----------End of Stack Trace----------");
		}
	}

	public PlayerManager getPlayerManager() {
		return pManager;
	}

	public CPlayer getCPlayer(Player player) {
		return pManager.getPlayer(player);
	}
}
