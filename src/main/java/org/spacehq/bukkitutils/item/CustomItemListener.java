package org.spacehq.bukkitutils.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class CustomItemListener implements Listener {
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getInventory().getType() == InventoryType.ANVIL && CustomItemRegistry.get(event.getCurrentItem()) != null) {
			event.setCancelled(true);
		}
	}
}
