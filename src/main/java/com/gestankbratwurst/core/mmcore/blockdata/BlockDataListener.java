package com.gestankbratwurst.core.mmcore.blockdata;

import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 09.01.2022
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public record BlockDataListener(BlockDataManager blockDataManager) implements Listener {

  public void initialInit() {
    Bukkit.getWorlds().forEach(blockDataManager::initWorld);
  }

  public void cleanupTermination() {
    Bukkit.getWorlds().forEach(blockDataManager::terminateWorld);
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onWorldLoad(WorldLoadEvent event) {
    blockDataManager.initWorld(event.getWorld());
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onWorldUnLoad(WorldUnloadEvent event) {
    blockDataManager.terminateWorld(event.getWorld());
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onChunkLoad(ChunkLoadEvent event) {
    blockDataManager.initChunk(event.getChunk());
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onChunkUnload(ChunkUnloadEvent event) {
    blockDataManager.terminateChunk(event.getChunk());
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onBreak(BlockBreakEvent event) {
    blockDataManager.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onBurn(BlockBurnEvent event) {
    blockDataManager.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onExplode(BlockExplodeEvent event) {
    event.blockList().forEach(blockDataManager::terminateBlock);
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onExplode(EntityExplodeEvent event) {
    event.blockList().forEach(blockDataManager::terminateBlock);
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onFade(BlockFadeEvent event) {
    blockDataManager.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onForm(BlockFormEvent event) {
    blockDataManager.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onFromTo(BlockFromToEvent event) {
    blockDataManager.terminateBlock(event.getToBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onGrow(BlockGrowEvent event) {
    blockDataManager.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onMultiPlace(BlockMultiPlaceEvent event) {
    blockDataManager.terminateBlock(event.getBlock());
    event.getReplacedBlockStates().stream().map(BlockState::getBlock).forEach(blockDataManager::terminateBlock);
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onExtend(BlockPistonExtendEvent event) {
    blockDataManager.shiftDataInDirection(event.getDirection(), event.getBlocks());
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onRetract(BlockPistonRetractEvent event) {
    blockDataManager.shiftDataInDirection(event.getDirection(), event.getBlocks());
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlace(BlockPlaceEvent event) {
    blockDataManager.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onSpread(BlockSpreadEvent event) {
    blockDataManager.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onForm(EntityBlockFormEvent event) {
    blockDataManager.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onDecay(LeavesDecayEvent event) {
    blockDataManager.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onBreak(BlockBreakBlockEvent event) {
    blockDataManager.terminateBlock(event.getBlock());
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onPrime(TNTPrimeEvent event) {
    blockDataManager.terminateBlock(event.getBlock());
  }

}
