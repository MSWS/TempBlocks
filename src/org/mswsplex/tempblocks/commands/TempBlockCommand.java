package org.mswsplex.tempblocks.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mswsplex.tempblocks.msws.TempBlocks;
import org.mswsplex.tempblocks.utils.MSG;

public class TempBlockCommand implements CommandExecutor, TabCompleter {
	private TempBlocks plugin;

	public TempBlockCommand(TempBlocks plugin) {
		this.plugin = plugin;
		plugin.getCommand("tempblock").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("tempblock.give")) {
			MSG.noPerm(sender);
			return true;
		}
		if (args.length < 3) {
			MSG.tell(sender, "/tempblock give [Player] [Type] <Amount> <Duration>");
			return true;
		}

		switch (args[0].toLowerCase()) {
		case "give":

			int amo = 1;
			long lifetime = 1000;

			Player target = Bukkit.getPlayer(args[1]);

			Material mat = null;

			String matName = args[2].contains(":") ? args[2].split(":")[0] : args[2];

			try {
				mat = Material.valueOf(matName);
			} catch (Exception e) {
				for (Material m : Material.values()) {
					if (m.toString().toLowerCase().replace("_", "").equals(matName.toLowerCase())) {
						mat = m;
						break;
					}
				}
				if (mat == null) {
					MSG.tell(target, "Unknown material.");
					return true;
				}
			}

			byte data = 0;

			if (args[2].contains(":")) {
				data = Byte.parseByte(args[2].split(":")[1]);
			}

			if (args.length > 3)
				amo = Integer.parseInt(args[3]);
			if (args.length > 4)
				lifetime = Long.parseLong(args[4]);

			ItemStack item = new ItemStack(mat, amo);
			item.setDurability(data);
			UUID uuid = UUID.randomUUID();
			ItemMeta meta = item.getItemMeta();

			meta.setDisplayName(MSG.color("&a&lTemporary Block &7(&e" + MSG.getTime((double) lifetime) + "&7)"));
			List<String> lore = new ArrayList<>();
			lore.add(MSG.color("&8" + uuid.toString()));
			meta.setLore(lore);
			item.setItemMeta(meta);

			target.getInventory().addItem(item);

			plugin.addTempItem(uuid.toString(), lifetime);
			break;
		}
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> result = new ArrayList<>();

		if (args.length == 1) {
			for (String res : new String[] { "give" }) {
				if (res.toLowerCase().startsWith(args[0].toLowerCase()))
					result.add(res);
			}
		}

		if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getName().toLowerCase().startsWith(args[1].toLowerCase()))
					result.add(p.getName());
			}
		}

		if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
			for (Material mat : Material.values()) {
				if (mat.toString().replace("_", "").toLowerCase().startsWith(args[2].toLowerCase())) {
					if (result.size() < 10)
						result.add(MSG.camelCase(mat.toString()).replace(" ", ""));
				}
			}
		}

		return result;
	}
}
