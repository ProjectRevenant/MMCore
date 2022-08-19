package com.gestankbratwurst.core.mmcore.blockdata;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 09.01.2022
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class BlockDataWorldDomain {

  private final UUID worldID;
  private final Long2ObjectMap<BlockDataChunkDomain> chunkMap = new Long2ObjectOpenHashMap<>();

  public BlockDataWorldDomain(UUID worldID) {
    this.worldID = worldID;
  }

  protected void initChunk(Chunk chunk) {
    BlockDataChunkDomain chunkDomain = new BlockDataChunkDomain(worldID, chunk.getChunkKey());
    if (chunkDomain.load()) {
      chunkMap.put(chunk.getChunkKey(), chunkDomain);
    }
  }

  protected void terminateChunk(Chunk chunk) {
    Optional.ofNullable(chunkMap.get(chunk.getChunkKey())).ifPresent(BlockDataChunkDomain::save);
  }

  protected Optional<PersistentDataContainer> getDataOf(Block block) {
    BlockDataChunkDomain chunkDomain = chunkMap.get(block.getChunk().getChunkKey());
    if (chunkDomain == null) {
      return Optional.empty();
    }
    return chunkDomain.getDataOf(block);
  }

  protected PersistentDataContainer createData(Block block) {
    long cKey = block.getChunk().getChunkKey();
    return chunkMap.computeIfAbsent(block.getChunk().getChunkKey(), key -> new BlockDataChunkDomain(worldID, cKey)).createData(block);
  }

  protected void clearDataOf(Block block) {
    BlockDataChunkDomain chunkDomain = chunkMap.get(block.getChunk().getChunkKey());
    if (chunkDomain == null) {
      return;
    }
    chunkDomain.clearDataOf(block);
  }

  public Map<Block, PersistentDataContainer> getDataInChunk(Chunk chunk) {
    long chunkKey = chunk.getChunkKey();
    BlockDataChunkDomain chunkDomain = chunkMap.get(chunkKey);
    if (chunkDomain == null) {
      return Collections.emptyMap();
    }
    return chunkDomain.getDataInChunk();
  }

  protected void putData(Block block, PersistentDataContainer container) {
    long cKey = block.getChunk().getChunkKey();
    chunkMap.computeIfAbsent(block.getChunk().getChunkKey(), key -> new BlockDataChunkDomain(worldID, cKey)).putData(block, container);
  }
}
