package org.spacehq.bukkitutils.custom.enchantment;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.ChatColor;
import org.bukkit.command.defaults.EnchantCommand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.spacehq.bukkitutils.reflect.BukkitReflection;
import org.spacehq.bukkitutils.reflect.SafeConstructor;
import org.spacehq.bukkitutils.reflect.SafeField;
import org.spacehq.bukkitutils.reflect.SafeMethod;
import org.spacehq.bukkitutils.util.RomanNumeral;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentRegistry {
	private static final SafeField BY_ID = new SafeField(Enchantment.class, "byId");
	private static final SafeField BY_NAME = new SafeField(Enchantment.class, "byName");
	private static final SafeField ENCHANTMENT_NAMES = new SafeField(EnchantCommand.class, "ENCHANTMENT_NAMES");

	private static final Class<?> CRAFT_ITEM_STACK = BukkitReflection.getCraftBukkitClass("inventory.CraftItemStack");
	private static final Class<?> ITEM_STACK = BukkitReflection.getNMSClass("ItemStack");
	private static final Class<?> NBT_BASE = BukkitReflection.getNMSClass("NBTBase");
	private static final Class<?> NBT_TAG_LIST = BukkitReflection.getNMSClass("NBTTagList");
	private static final Class<?> NBT_TAG_COMPOUND = BukkitReflection.getNMSClass("NBTTagCompound");
	private static final SafeMethod AS_NMS_COPY = new SafeMethod(CRAFT_ITEM_STACK, "asNMSCopy", ItemStack.class);
	private static final SafeMethod AS_CRAFT_MIRROR = new SafeMethod(CRAFT_ITEM_STACK, "asCraftMirror", ITEM_STACK);
	private static final SafeMethod GET_TAG = new SafeMethod(ITEM_STACK, "getTag");
	private static final SafeMethod SET_TAG = new SafeMethod(ITEM_STACK, "setTag", NBT_TAG_COMPOUND);
	private static final SafeMethod COMPOUND_SET = new SafeMethod(NBT_TAG_COMPOUND, "set", String.class, NBT_BASE);
	private static final SafeConstructor NBT_LIST = new SafeConstructor(NBT_TAG_LIST);

	private static final Map<Enchantment, String> NAME_MAP = new HashMap<Enchantment, String>();

	private static List<CustomEnchantment> registeredEnchantments = new ArrayList<CustomEnchantment>();
	private static Plugin plugin;

	static {
		NAME_MAP.put(Enchantment.ARROW_DAMAGE, "Power");
		NAME_MAP.put(Enchantment.ARROW_FIRE, "Flame");
		NAME_MAP.put(Enchantment.ARROW_INFINITE, "Infinity");
		NAME_MAP.put(Enchantment.ARROW_KNOCKBACK, "Punch");
		NAME_MAP.put(Enchantment.DAMAGE_ALL, "Sharpness");
		NAME_MAP.put(Enchantment.DAMAGE_ARTHROPODS, "Bane of Arthropods");
		NAME_MAP.put(Enchantment.DAMAGE_UNDEAD, "Smite");
		NAME_MAP.put(Enchantment.DIG_SPEED, "Efficiency");
		NAME_MAP.put(Enchantment.DURABILITY, "Unbreaking");
		NAME_MAP.put(Enchantment.FIRE_ASPECT, "Fire Aspect");
		NAME_MAP.put(Enchantment.KNOCKBACK, "Knockback");
		NAME_MAP.put(Enchantment.LOOT_BONUS_BLOCKS, "Fortune");
		NAME_MAP.put(Enchantment.LOOT_BONUS_MOBS, "Looting");
		NAME_MAP.put(Enchantment.LUCK, "Luck of the Sea");
		NAME_MAP.put(Enchantment.LURE, "Lure");
		NAME_MAP.put(Enchantment.OXYGEN, "Respiration");
		NAME_MAP.put(Enchantment.PROTECTION_ENVIRONMENTAL, "Protection");
		NAME_MAP.put(Enchantment.PROTECTION_EXPLOSIONS, "Blast Protection");
		NAME_MAP.put(Enchantment.PROTECTION_FALL, "Feather Falling");
		NAME_MAP.put(Enchantment.PROTECTION_FIRE, "Fire Protection");
		NAME_MAP.put(Enchantment.PROTECTION_PROJECTILE, "Projectile Protection");
		NAME_MAP.put(Enchantment.SILK_TOUCH, "Silk Touch");
		NAME_MAP.put(Enchantment.THORNS, "Thorns");
		NAME_MAP.put(Enchantment.WATER_WORKER, "Aqua Affinity");
	}

	public static void initialize(Plugin plug) {
		plugin = plug;
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.WINDOW_ITEMS) {
			@Override
			public void onPacketSending(PacketEvent event) {
				StructureModifier<ItemStack[]> items = event.getPacket().getItemArrayModifier();
				items.write(0, modifyItems(items.read(0)));
			}
		});

		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.ENTITY_EQUIPMENT) {
			@Override
			public void onPacketSending(PacketEvent event) {
				StructureModifier<ItemStack> items = event.getPacket().getItemModifier();
				items.write(0, modifyItems(items.read(0))[0]);
			}
		});
	}

	public static void cleanup() {
		for(CustomEnchantment enchantment : new ArrayList<CustomEnchantment>(registeredEnchantments)) {
			unregister(enchantment);
		}

		ProtocolLibrary.getProtocolManager().removePacketListeners(plugin);
		plugin = null;
	}

	public static void register(CustomEnchantment enchantment) {
		((Map<Integer, Enchantment>) BY_ID.get(null)).put(enchantment.getId(), enchantment);
		((Map<String, Enchantment>) BY_NAME.get(null)).put(enchantment.getName(), enchantment);
		registeredEnchantments.add(enchantment);
		List<String> enchantNames = (List<String>) ENCHANTMENT_NAMES.get(null);
		enchantNames.add(enchantment.getName());
		Collections.sort(enchantNames);
	}

	public static void unregister(CustomEnchantment enchantment) {
		((Map<Integer, Enchantment>) BY_ID.get(null)).remove(enchantment.getId());
		((Map<String, Enchantment>) BY_NAME.get(null)).remove(enchantment.getName());
		registeredEnchantments.remove(enchantment);
		List<String> enchantNames = (List<String>) ENCHANTMENT_NAMES.get(null);
		enchantNames.remove(enchantment.getName());
		Collections.sort(enchantNames);
	}

	private static ItemStack[] modifyItems(ItemStack... items) {
		ItemStack ret[] = new ItemStack[items.length];
		for(int index = 0; index < items.length; index++) {
			ItemStack item = items[index] != null ? items[index].clone() : null;
			if(item != null) {
				Map<Enchantment, Integer> enchantments = item.getEnchantments();
				if(enchantments.size() > 0) {
					for(Enchantment enchantment : enchantments.keySet()) {
						item.removeEnchantment(enchantment);
						ItemMeta meta = item.getItemMeta();
						List<String> lore = meta.getLore();
						if(lore == null) {
							lore = new ArrayList<String>();
						}

						String name = enchantment.getName();
						if(enchantment instanceof CustomEnchantment) {
							name = ((CustomEnchantment) enchantment).getDisplayName();
						} else if(NAME_MAP.containsKey(enchantment)) {
							name = NAME_MAP.get(enchantment);
						}

						lore.add(ChatColor.RESET.toString() + ChatColor.GRAY + name + ChatColor.RESET + ChatColor.GRAY + " " + RomanNumeral.toRomanNumeral(enchantments.get(enchantment)));
						meta.setLore(lore);
						item.setItemMeta(meta);
					}

					item = addGlow(item);
				}
			}

			ret[index] = item;
		}

		return ret;
	}

	private static ItemStack addGlow(ItemStack item) {
		Object nmsStack = AS_NMS_COPY.call(null, item);
		Object tag = GET_TAG.call(nmsStack);
		Object enchList = NBT_LIST.construct();
		COMPOUND_SET.call(tag, "ench", enchList);
		SET_TAG.call(nmsStack, tag);
		return AS_CRAFT_MIRROR.call(null, nmsStack);
	}
}
