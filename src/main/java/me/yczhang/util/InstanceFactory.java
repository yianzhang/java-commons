package me.yczhang.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * Created by YCZhang on 1/29/16.
 */
public class InstanceFactory {

	protected Class klass;
	protected Object instance;

	protected InstanceFactory() {

	}

	public static InstanceFactory create(@Nonnull Class klass, @Nullable Object[] constructorArgs) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		Objects.requireNonNull(klass);

		Object instance = _newInstance(klass, constructorArgs);
		InstanceFactory factory = new InstanceFactory();
		factory.klass = klass;
		factory.instance = instance;
		return factory;
	}

	public InstanceFactory setField(@Nonnull String name, @Nullable Object value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Objects.requireNonNull(name);

		invokeSetMethod(instance, klass, name, value);

		return this;
	}

	public Object newInstance() {
		return instance;
	}

	public static Object newInstance(@Nonnull Class klass, @Nullable Object[] constructorArgs, @Nullable Map<String, Object> injectFields) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		Objects.requireNonNull(klass);

		Object instance = _newInstance(klass, constructorArgs);
		if (injectFields != null) {
			for (Map.Entry<String, Object> entry : injectFields.entrySet()) {
				invokeSetMethod(instance, klass, entry.getKey(), entry.getValue());
			}
		}

		return instance;
	}

	protected static Object _newInstance(@Nonnull Class klass, @Nullable Object[] constructorArgs) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Constructor[] constructors = klass.getConstructors();

		Constructor theConstructor = null;
		for (Constructor constructor : constructors) {
			Class[] paramTypes = constructor.getParameterTypes();
			if (constructorArgs == null && paramTypes.length == 0) {
				theConstructor = constructor;
			}
			else if (constructorArgs != null && paramTypes.length == constructorArgs.length) {
				theConstructor = constructor;
				for (int i = 0; i < paramTypes.length; ++i) {
					if (!isInstanceOf(constructorArgs[i], paramTypes[i])) {
						theConstructor = null;
						break;
					}
				}
			}
			if (theConstructor != null)
				break;
		}

		if (theConstructor == null)
			throw new NoSuchMethodException("no such constructor suit to args");

		return theConstructor.newInstance(constructorArgs);
	}

	protected static void invokeSetMethod(@Nonnull Object obj, @Nonnull Class klass, @Nonnull String name, @Nullable Object value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		String methodName = name == null || name.length() == 0 ? "set" : "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);

		Method[] methods = klass.getMethods();
		Method theMethod = null;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] paramTypes = method.getParameterTypes();
				if (paramTypes.length == 1 && isInstanceOf(value, paramTypes[0])) {
					theMethod = method;
					break;
				}
			}
		}

		if (theMethod == null)
			throw new NoSuchMethodException("no such method " + methodName + "(" + (value == null ? "null" : value.getClass().getName()) + ")");

		theMethod.invoke(obj, value);
	}

	protected static boolean isInstanceOf(@Nullable Object obj, @Nonnull Class klass) {
		if (klass.equals(boolean.class))
			return Boolean.class.isInstance(obj);
		else if (klass.equals(byte.class))
			return Byte.class.isInstance(obj);
		else if (klass.equals(char.class))
			return Character.class.isInstance(obj);
		else if (klass.equals(double.class))
			return Double.class.isInstance(obj);
		else if (klass.equals(float.class))
			return Float.class.isInstance(obj);
		else if (klass.equals(int.class))
			return Integer.class.isInstance(obj);
		else if (klass.equals(long.class))
			return Long.class.isInstance(obj);
		else if (klass.equals(short.class))
			return Short.class.isInstance(obj);
		else if (obj == null)
			return true;
		if (klass.isInstance(obj))
			return true;
		else
			return false;
	}

	protected static boolean hasNonnullAnnotation(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			if (annotation instanceof Nonnull)
				return true;
		}

		return false;
	}
}
