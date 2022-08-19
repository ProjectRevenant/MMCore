package com.gestankbratwurst.core.mmcore.blockdata;

import com.gestankbratwurst.core.mmcore.blockdata.events.BlockDataCreateEvent;
import com.gestankbratwurst.core.mmcore.blockdata.events.BlockDataLoadEvent;
import com.gestankbratwurst.core.mmcore.blockdata.events.BlockDataRemoveEvent;
import com.gestankbratwurst.core.mmcore.blockdata.events.BlockDataSaveEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 09.01.2022
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@RequiredArgsConstructor
public class BlockDataChunkDomain {

  private final UUID worldID;
  private final long chunkKey;
  private final Int2ObjectMap<PersistentDataContainer> blockDataMap = new Int2ObjectOpenHashMap<>();
  private final NamespacedKey parentKey = Objects.requireNonNull(NamespacedKey.fromString("mmcore:chunk-data-parent"));
  private final NamespacedKey positionKey = Objects.requireNonNull(NamespacedKey.fromString("mmcore:chunk-data-position"));
  private final NamespacedKey dataKey = Objects.requireNonNull(NamespacedKey.fromString("mmcore:chunk-data"));

  protected void save() {
    World world = Bukkit.getWorld(worldID);
    if (world == null) {
      return;
    }
    Chunk chunk = world.getChunkAt(chunkKey);
    PersistentDataContainer container = chunk.getPersistentDataContainer();
    if (blockDataMap.isEmpty()) {
      container.remove(parentKey);
      return;
    }
    PersistentDataContainer[] dataContainers = new PersistentDataContainer[blockDataMap.size()];
    AtomicInteger counter = new AtomicInteger();
    blockDataMap.int2ObjectEntrySet().forEach(entry -> {
      int x = keyToX(entry.getIntKey());
      int y = keyToY(entry.getIntKey());
      int z = keyToZ(entry.getIntKey());
      Block block = chunk.getBlock(x, y, z);
      Bukkit.getPluginManager().callEvent(new BlockDataSaveEvent(block, entry.getValue()));
      PersistentDataContainer mapContainer = container.getAdapterContext().newPersistentDataContainer();
      mapContainer.set(positionKey, PersistentDataType.INTEGER, entry.getIntKey());
      mapContainer.set(dataKey, PersistentDataType.TAG_CONTAINER, entry.getValue());
      dataContainers[counter.getAndIncrement()] = mapContainer;
    });
    container.set(parentKey, PersistentDataType.TAG_CONTAINER_ARRAY, dataContainers);
  }

  protected boolean load() {
    World world = Bukkit.getWorld(worldID);
    if (world == null) {
      return false;
    }
    Chunk chunk = world.getChunkAt(chunkKey);
    PersistentDataContainer container = chunk.getPersistentDataContainer();
    PersistentDataContainer[] dataContainers = container.get(parentKey, PersistentDataType.TAG_CONTAINER_ARRAY);
    if (dataContainers == null || dataContainers.length == 0) {
      return false;
    }
    for (PersistentDataContainer dataContainer : dataContainers) {
      Integer pos = dataContainer.get(positionKey, PersistentDataType.INTEGER);
      if (pos == null) {
        continue;
      }
      PersistentDataContainer data = dataContainer.get(dataKey, PersistentDataType.TAG_CONTAINER);
      if (data == null) {
        continue;
      }
      int x = keyToX(pos);
      int y = keyToY(pos);
      int z = keyToZ(pos);
      Block block = chunk.getBlock(x, y, z);
      Bukkit.getPluginManager().callEvent(new BlockDataLoadEvent(block, data));
      blockDataMap.put(pos.intValue(), data);
    }
    return true;
  }

  protected Optional<PersistentDataContainer> getDataOf(Block block) {
    return Optional.ofNullable(blockDataMap.get(keyOf(block)));
  }

  protected PersistentDataContainer createData(Block block) {
    PersistentDataAdapterContext context = block.getChunk().getPersistentDataContainer().getAdapterContext();
    PersistentDataContainer container = blockDataMap.computeIfAbsent(keyOf(block), key -> context.newPersistentDataContainer());
    Bukkit.getPluginManager().callEvent(new BlockDataCreateEvent(block, container));
    return container;
  }

  protected void clearDataOf(Block block) {
    PersistentDataContainer container = blockDataMap.remove(keyOf(block));
    if (container != null) {
      Bukkit.getPluginManager().callEvent(new BlockDataRemoveEvent(block, container));
    }
  }

  protected Map<Block, PersistentDataContainer> getDataInChunk() {
    Map<Block, PersistentDataContainer> data = new HashMap<>();
    World world = Bukkit.getWorld(worldID);
    if (world == null) {
      return Collections.emptyMap();
    }

    Chunk chunk = world.getChunkAt(chunkKey);
    blockDataMap.int2ObjectEntrySet().forEach(entry -> {
      int key = entry.getIntKey();
      int x = keyToX(key);
      int y = keyToY(key);
      int z = keyToZ(key);
      Block block = chunk.getBlock(x, y, z);
      PersistentDataContainer container = entry.getValue();
      data.put(block, container);
    });

    return data;
  }

  protected void putData(Block block, PersistentDataContainer container) {
    blockDataMap.put(keyOf(block), container);
  }

  private int keyOf(Block block) {
    final int relX = (block.getX() % 16 + 16) % 16;
    final int relZ = (block.getZ() % 16 + 16) % 16;
    final int relY = block.getY();
    return (relY & 0xFFFF) | ((relX & 0xFF) << 16) | ((relZ & 0xFF) << 24);
  }

  private int keyToX(int key) {
    return (key >> 16) & 0xFF;
  }

  private int keyToY(int key) {
    return key & 0xFFFF;
  }

  private int keyToZ(int key) {
    return (key >> 24) & 0xFF;
  }

}
