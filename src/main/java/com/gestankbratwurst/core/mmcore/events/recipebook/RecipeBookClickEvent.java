package com.gestankbratwurst.core.mmcore.events.recipebook;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class RecipeBookClickEvent extends PlayerEvent {

  private static final HandlerList HANDLERS = new HandlerList();

  @Getter
  private final boolean state;

  public RecipeBookClickEvent(@NotNull Player who, boolean state) {
    super(who);
    this.state = state;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

}
