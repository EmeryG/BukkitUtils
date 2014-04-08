package org.spacehq.bukkitutils.item;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItemRegistry {
	private static Map<MaterialData, Map<String, CustomItem>> ITEMS = new HashMap<MaterialData, Map<String, CustomItem>>();

	public static void initialize(Plugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents(new CustomItemListener(), plugin);
	}

	public static void register(CustomItem item) {
		if(!ITEMS.containsKey(item.getMaterial())) {
			ITEMS.put(item.getMaterial(), new HashMap<String, CustomItem>());
		}

		ITEMS.get(item.getMaterial()).put(item.getName().toLowerCase(), item);
	}

	public static CustomItem get(MaterialData material, String name) {
		if(!ITEMS.containsKey(material)) {
			return null;
		}

		if(name == null) {
			return null;
		}

		return ITEMS.get(material).get(name.toLowerCase());
	}

	public static List<CustomItem> get(MaterialData material) {
		if(!ITEMS.containsKey(material)) {
			return new ArrayList<CustomItem>();
		}

		return new ArrayList<CustomItem>(ITEMS.get(material).values());
	}

	public static CustomItem get(ItemStack item) {
		if(item == null || item.getItemMeta() == null) {
			return null;
		}

		return get(item.getData(), item.getItemMeta().getDisplayName());
	}
}
