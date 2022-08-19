package com.gestankbratwurst.core.mmcore.blockdata.events;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 09.01.2022
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class BlockDataMoveEvent extends BlockEvent {

  private static final HandlerList HANDLERS = new HandlerList();

  @Getter
  private final PersistentDataContainer dataContainer;
  @Getter
  private final Block toBlock;

  public BlockDataMoveEvent(@NotNull Block theBlock, @NotNull Block toBlock, @NotNull PersistentDataContainer dataContainer) {
    super(theBlock);
    this.dataContainer = dataContainer;
    this.toBlock = toBlock;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

}
