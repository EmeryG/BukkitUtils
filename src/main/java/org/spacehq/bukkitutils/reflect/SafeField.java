package org.spacehq.bukkitutils.reflect;

import java.lang.reflect.Field;

public class SafeField {

	private Class<?> clazz;
	private String name;

	public SafeField(Class<?> clazz, String name) {
		this.clazz = clazz;
		this.name = name;
	}

	public <T> T get(Object instance) {
		try {
			Field field = this.clazz.getDeclaredField(this.name);
			field.setAccessible(true);
			return (T) field.get(instance);
		} catch(Exception e) {
			throw new ReflectException("Could not get value of field \"" + this.name + "\" of class \"" + this.clazz.getName() + "\".", e);
		}
	}

}
