package com.gestankbratwurst.core.mmcore.util.items.display;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.List;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of LaLaLand-CorePlugin and was created at the 16.11.2019
 *
 * LaLaLand-CorePlugin can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ItemDisplayCompiler extends PacketAdapter {

  public static final NamespacedKey COMPILER_KEY = NamespacedKey.minecraft("display-compiler");

  public static void addDisplayCompileKey(final String key, final ItemStack item) {
    final int hash = key.hashCode();
    if (item == null) {
      return;
    }
    final ItemMeta meta = item.getItemMeta();
    if (meta == null) {
      return;
    }
    final PersistentDataContainer container = meta.getPersistentDataContainer();
    final int[] current = container.get(COMPILER_KEY, PersistentDataType.INTEGER_ARRAY);
    final int[] newValues;
    if (current == null) {
      newValues = new int[]{hash};
    } else {
      newValues = new int[current.length + 1];
      System.arraycopy(current, 0, newValues, 0, current.length);
      newValues[current.length] = hash;
    }
    container.set(COMPILER_KEY, PersistentDataType.INTEGER_ARRAY, newValues);
    item.setItemMeta(meta);
  }

  private final Int2ObjectMap<DisplayConverter> compilerMap;

  public ItemDisplayCompiler(final Plugin plugin) {
    super(plugin, Server.WINDOW_ITEMS, Server.SET_SLOT);
    this.compilerMap = new Int2ObjectOpenHashMap<>();
  }

  @Override
  public void onPacketSending(final PacketEvent event) {
    final PacketContainer packet = event.getPacket();
    final Player player = event.getPlayer();
    if (event.getPacketType() == Server.WINDOW_ITEMS) {
      final StructureModifier<List<ItemStack>> structMod = packet.getItemListModifier();
      for (int index = 0; index < structMod.size(); index++) {
        final List<ItemStack> itemArray = structMod.read(index);
        itemArray.replaceAll(itemStack -> this.apply(player, itemStack));
        structMod.write(index, itemArray);
      }
    } else {
      final StructureModifier<ItemStack> structMod = packet.getItemModifier();
      for (int index = 0; index < structMod.size(); index++) {
        structMod.modify(index, item -> this.apply(player, item));
      }
    }
    event.setPacket(packet);
  }

  public void registerConverter(final DisplayConverter converter) {
    this.compilerMap.put(converter.getDisplayKey().hashCode(), converter);
  }

  public ItemStack apply(final Player player, final ItemStack itemStack) {
    if (itemStack == null) {
      return null;
    }
    ItemStack clone = itemStack.clone();
    final ItemMeta meta = clone.getItemMeta();
    if (meta == null) {
      return itemStack;
    }
    final int[] compilerKeys = meta.getPersistentDataContainer().get(COMPILER_KEY, PersistentDataType.INTEGER_ARRAY);
    if (compilerKeys == null) {
      return itemStack;
    }
    for (final int key : compilerKeys) {
      clone = this.compilerMap.get(key).compileInfo(player, clone);
    }
    return clone;
  }
}