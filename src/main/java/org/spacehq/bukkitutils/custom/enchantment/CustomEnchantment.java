package org.spacehq.bukkitutils.custom.enchantment;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CustomEnchantment extends Enchantment {
	private String bukkitName;
	private String displayName;
	private int minLevel;
	private int maxLevel;
	private EnchantmentTarget target;
	private List<Enchantment> conflicts;

	public CustomEnchantment(int id, String bukkitName, String displayName, int minLevel, int maxLevel, EnchantmentTarget target, Enchantment... conflicts) {
		super(id);
		this.bukkitName = bukkitName;
		this.displayName = displayName;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.target = target;
		this.conflicts = Arrays.asList(conflicts != null ? conflicts : new Enchantment[0]);
	}

	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public String getName() {
		return this.bukkitName;
	}

	@Override
	public int getMaxLevel() {
		return this.maxLevel;
	}

	@Override
	public int getStartLevel() {
		return this.minLevel;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return this.target;
	}

	@Override
	public boolean conflictsWith(Enchantment enchantment) {
		return this.conflicts.contains(enchantment);
	}

	@Override
	public boolean canEnchantItem(ItemStack itemStack) {
		return this.getItemTarget().includes(itemStack);
	}
}
