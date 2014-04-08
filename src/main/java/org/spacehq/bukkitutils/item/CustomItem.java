package org.spacehq.bukkitutils.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.List;

public class CustomItem {
	private String name;
	private List<String> defaultLore;
	private MaterialData material;

	public CustomItem(String name, List<String> defaultLore, MaterialData material) {
		this.name = name;
		this.defaultLore = defaultLore;
		this.material = material;
		if(this.material.getItemType().getMaxDurability() > 0) {
			this.material.setData((byte) 0);
		}
	}

	public String getName() {
		return this.name;
	}

	public List<String> getDefaultLore() {
		return this.defaultLore;
	}

	public MaterialData getMaterial() {
		return this.material;
	}

	public ItemStack newItemStack(int amount) {
		ItemStack item = new ItemStack(this.material.getItemType(), amount, this.material.getData());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.name);
		meta.setLore(this.defaultLore);
		item.setItemMeta(meta);
		return item;
	}

	public void apply(ItemStack item) {
		item.setType(this.material.getItemType());
		item.setData(this.material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.name);
		meta.setLore(this.defaultLore);
		item.setItemMeta(meta);
	}
}
