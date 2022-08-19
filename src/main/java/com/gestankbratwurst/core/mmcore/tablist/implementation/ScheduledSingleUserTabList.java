package com.gestankbratwurst.core.mmcore.tablist.implementation;

import java.util.UUID;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 22.12.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class ScheduledSingleUserTabList extends SingleUserTabList {

  private final int scheduledInterval;
  private long counter = 0L;

  public ScheduledSingleUserTabList(UUID userID, int scheduledInterval) {
    super(userID);
    this.scheduledInterval = scheduledInterval;
  }

  public abstract void tickAction();

  public final void onTick() {
    if (++counter % scheduledInterval == 0) {
      tickAction();
    }
  }

}
