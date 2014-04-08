package org.spacehq.bukkitutils.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.List;

public class CustomItem {
	private String name;
	private List<String> lore;
	private MaterialData material;

	public CustomItem(String name, List<String> lore, MaterialData material) {
		this.name = name;
		this.lore = lore;
		this.material = material;
		if(this.material.getItemType().getMaxDurability() > 0) {
			this.material.setData((byte) 0);
		}
	}

	public String getName() {
		return this.name;
	}

	public List<String> getLore() {
		return this.lore;
	}

	public MaterialData getMaterial() {
		return this.material;
	}

	public ItemStack newItemStack(int amount) {
		ItemStack item = new ItemStack(this.material.getItemType(), amount, this.material.getData());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.name);
		meta.setLore(this.lore);
		item.setItemMeta(meta);
		return item;
	}
}
