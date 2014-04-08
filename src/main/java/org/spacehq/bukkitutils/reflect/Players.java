package org.spacehq.bukkitutils.reflect;

import org.bukkit.entity.Player;

public class Players {
	private static final SafeMethod GET_HANDLE = new SafeMethod(BukkitReflection.getCraftBukkitClass("entity.CraftPlayer"), "getHandle");
	private static final SafeField LOCALE = new SafeField(BukkitReflection.getNMSClass("EntityPlayer"), "locale");

	public static String getLocale(Player player) throws ReflectException {
		return LOCALE.get(GET_HANDLE.call(player));
	}
}
