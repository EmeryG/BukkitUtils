package org.spacehq.bukkitutils.reflect;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class SafeConstructor {

	private Class<?> clazz;
	private Class<?>[] args;

	public SafeConstructor(Class<?> clazz, Class<?>... args) {
		this.clazz = clazz;
		this.args = args;
	}

	public <T> T construct(Object... args) {
		try {
			Constructor<?> constructor = this.clazz.getDeclaredConstructor(this.args);
			constructor.setAccessible(true);
			return (T) constructor.newInstance(this.args);
		} catch(Exception e) {
			throw new ReflectException("Could not construct instance of class \"" + this.clazz.getName() + "\" with argument types \"" + Arrays.toString(this.args) + "\".", e);
		}
	}

}
