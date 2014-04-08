package org.spacehq.bukkitutils.reflect;

import java.lang.reflect.Method;
import java.util.Arrays;

public class SafeMethod {

	private Class<?> clazz;
	private String name;
	private Class<?>[] args;

	public SafeMethod(Class<?> clazz, String name, Class<?>... args) {
		this.clazz = clazz;
		this.name = name;
		this.args = args;
	}

	public <T> T call(Object instance, Object... args) {
		try {
			Method method = this.clazz.getDeclaredMethod(this.name, this.args);
			method.setAccessible(true);
			return (T) method.invoke(instance, args);
		} catch(Exception e) {
			throw new ReflectException("Could not get call method \"" + this.name + "\" of class \"" + this.clazz.getName() + "\" with argument types \"" + Arrays.toString(this.args) + "\".", e);
		}
	}

}
