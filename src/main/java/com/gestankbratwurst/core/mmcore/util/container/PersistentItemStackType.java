package com.gestankbratwurst.core.mmcore.util.container;

import com.gestankbratwurst.core.mmcore.util.common.UtilItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 20.10.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class PersistentItemStackType implements PersistentDataType<String, ItemStack> {

  @Override
  public Class<String> getPrimitiveType() {
    return String.class;
  }

  @Override
  public Class<ItemStack> getComplexType() {
    return ItemStack.class;
  }

  @Override
  public String toPrimitive(final ItemStack object, final PersistentDataAdapterContext persistentDataAdapterContext) {
    return UtilItem.serialize(object);
  }

  @Override
  public ItemStack fromPrimitive(final String object, final PersistentDataAdapterContext persistentDataAdapterContext) {
    return UtilItem.deserializeItemStack(object);
  }

}
