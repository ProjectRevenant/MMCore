package com.gestankbratwurst.core.mmcore.actionbar;

import com.gestankbratwurst.core.mmcore.MMCore;
import com.gestankbratwurst.core.mmcore.util.tasks.TaskManager;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 28.07.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ActionBarManager {

  private static final int DISTRIBUTION_SIZE = 10;

  private final Object2ObjectOpenHashMap<UUID, ActionBarBoard> boardMap;
  @Getter(AccessLevel.PROTECTED)
  private final TaskManager taskManager;
  private final List<List<Player>> distributedOnlineList = new ArrayList<>();
  private final Object2IntMap<Player> playerPositionMap = new Object2IntOpenHashMap<>();
  private int currentIndex = 0;

  public ActionBarManager(final MMCore plugin) {
    this.boardMap = new Object2ObjectOpenHashMap<>();
    this.taskManager = TaskManager.getInstance();
    for (final Player player : Bukkit.getOnlinePlayers()) {
      this.init(player);
    }
    Bukkit.getPluginManager().registerEvents(new ActionBarListener(this), plugin);

    for (int i = 0; i < DISTRIBUTION_SIZE; i++) {
      this.distributedOnlineList.add(new ArrayList<>());
    }

    this.taskManager.runRepeatedBukkit(new ActionBarUpdateRunnable(this), 0L, 1);
  }

  public ActionBarBoard getBoard(final UUID playerID) {
    return this.boardMap.get(playerID);
  }

  public ActionBarBoard getBoard(final Player player) {
    return this.getBoard(player.getUniqueId());
  }

  protected void init(final Player player) {
    this.boardMap.put(player.getUniqueId(), new ActionBarBoard(this));
    final int lowest = this.getLowestIndex();
    this.playerPositionMap.put(player, lowest);
    this.distributedOnlineList.get(lowest).add(player);
  }

  protected void terminate(final Player player) {
    this.boardMap.remove(player.getUniqueId());
    final int pos = this.playerPositionMap.removeInt(player);
    this.distributedOnlineList.get(pos).remove(player);
  }

  private int getLowestIndex() {
    int index = 0;
    int lowest = 999;
    for (int i = 0; i < DISTRIBUTION_SIZE; i++) {
      final List<Player> playerList = this.distributedOnlineList.get(i);
      if (playerList.isEmpty()) {
        return index;
      } else if (playerList.size() < lowest) {
        index = i;
        lowest = playerList.size();
      }
    }
    return index;
  }

  public void showTo(final Player player) {
    player.sendActionBar(this.getBoard(player).getCurrentDisplay());
  }

  public void updateAndShow(final Player player) {
    final ActionBarBoard board = this.getBoard(player);
    board.update();
    player.sendActionBar(board.getCurrentDisplay());
  }

  protected void updateAndNextIndex() {
    this.updateCurrentIndex();
    this.nextIndex();
  }

  private void nextIndex() {
    if (++this.currentIndex == DISTRIBUTION_SIZE) {
      this.currentIndex = 0;
    }
  }

  private void updateCurrentIndex() {
    this.distributedOnlineList.get(this.currentIndex).forEach(this::updateAndShow);
  }

  protected void updateAndShowAll() {
    for (final Player player : Bukkit.getOnlinePlayers()) {
      this.updateAndShow(player);
    }
  }

  protected void showToAll() {
    for (final Player player : Bukkit.getOnlinePlayers()) {
      this.showTo(player);
    }
  }

  public void update(final Player player) {
    this.boardMap.get(player.getUniqueId()).update();
  }

  protected void updateAll() {
    for (final Player player : Bukkit.getOnlinePlayers()) {
      this.update(player);
    }
  }

}
