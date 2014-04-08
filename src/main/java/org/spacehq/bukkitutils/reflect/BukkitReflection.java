package org.spacehq.bukkitutils.reflect;

public class BukkitReflection {

	public static final String NMS_PACKAGE;
	public static final String CB_PACKAGE;

	static {
		NMS_PACKAGE = getVersionedPackage("net.minecraft.server");
		CB_PACKAGE = getVersionedPackage("org.bukkit.craftbukkit");
	}

	public static Class<?> getNMSClass(String name) throws ReflectException {
		return getClass(NMS_PACKAGE, name);
	}

	public static Class<?> getCraftBukkitClass(String name) throws ReflectException {
		return getClass(CB_PACKAGE, name);
	}

	public static Class<?> getClass(String pkg, String name) throws ReflectException {
		try {
			return Class.forName(pkg + "." + name);
		} catch(ClassNotFoundException e) {
			throw new ReflectException("Could not find class \"" + name + "\" in package \"" + pkg + "\".", e);
		}
	}

	public static String getVersionedPackage(String base) throws ReflectException {
		for(Package pkg : Package.getPackages()) {
			String name = pkg.getName();
			if(name.startsWith(base + ".v")) {
				int index = (base + ".v").length();
				String after = name.substring(index);
				return name.substring(0, (after.contains(".") ? after.indexOf(".") : after.length()) + index);
			}
		}

		// Unversioned package or we're in a development environment.
		return base;
	}

}
