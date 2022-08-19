package com.gestankbratwurst.core.mmcore.util.container;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 12.01.2022
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class AnyPersistentDataType<T> implements PersistentDataType<String, T> {

  private static final List<Consumer<GsonBuilder>> GSON_BUILDER_MIXINS = new ArrayList<>();
  private static Gson GSON;
  private static boolean gsonChanged = true;

  public static void injectIntoGson(Consumer<GsonBuilder> action) {
    GSON_BUILDER_MIXINS.add(action);
    gsonChanged = true;
  }

  private static void reBuildGson() {
    GsonBuilder builder = new GsonBuilder();
    GSON_BUILDER_MIXINS.forEach(action -> action.accept(builder));
    GSON = builder.create();
    gsonChanged = false;
  }

  private static Gson getGson() {
    if (gsonChanged) {
      reBuildGson();
    }
    return GSON;
  }

  private final TypeToken<T> typeToken;

  @Override
  public @NotNull Class<String> getPrimitiveType() {
    return String.class;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull Class<T> getComplexType() {
    return (Class<T>) typeToken.getRawType();
  }

  @Override
  public @NotNull String toPrimitive(@NotNull T complex, @NotNull PersistentDataAdapterContext context) {
    return getGson().toJson(complex);
  }

  @Override
  public @NotNull T fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
    return getGson().fromJson(primitive, typeToken.getType());
  }

}
