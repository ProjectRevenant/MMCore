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
public class BlockDataSaveEvent extends BlockEvent {

  private static final HandlerList HANDLERS = new HandlerList();

  @Getter
  private final PersistentDataContainer dataContainer;

  public BlockDataSaveEvent(@NotNull Block theBlock, @NotNull PersistentDataContainer dataContainer) {
    super(theBlock);
    this.dataContainer = dataContainer;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

}
