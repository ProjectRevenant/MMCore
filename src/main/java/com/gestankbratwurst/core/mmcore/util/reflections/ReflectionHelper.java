package com.gestankbratwurst.core.mmcore.util.reflections;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ReflectionHelper {

  private static final Map<Class<?>, Map<Class<?>[], Constructor<?>>> constructorCache = new HashMap<>();

  public static <T> T constructInstance(Class<T> tClass) {
    return constructInstance(tClass, new Class[0], new Object[0]);
  }

  public static <T> T constructInstance(Class<T> tClass, Class<?>[] types, Object[] params) {
    try {
      return getConstructor(tClass, types).newInstance(params);
    } catch (ReflectiveOperationException exception) {
      throw new RuntimeException(exception);
    }
  }

  public static <T> Constructor<T> getConstructor(Class<T> tClass, Class<?>... paramTypes) {
    return (Constructor<T>) constructorCache.computeIfAbsent(tClass, type -> new HashMap<>())
        .computeIfAbsent(paramTypes, params -> fetchConstructor(tClass, paramTypes));
  }

  private static <T> Constructor<T> fetchConstructor(Class<T> tClass, Class<?>... paramTypes) {
    try {
      return tClass.getConstructor(paramTypes);
    } catch (ReflectiveOperationException exception) {
      throw new IllegalStateException("No constructor found for " + tClass + " with " + Arrays.toString(paramTypes));
    }
  }

}
