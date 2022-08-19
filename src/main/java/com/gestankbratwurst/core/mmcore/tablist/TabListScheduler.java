package com.gestankbratwurst.core.mmcore.tablist;

import com.gestankbratwurst.core.mmcore.tablist.implementation.ScheduledSingleUserTabList;
import com.gestankbratwurst.core.mmcore.util.tasks.TaskManager;
import java.util.ArrayList;
import java.util.List;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 22.12.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class TabListScheduler implements Runnable {

  private static final TabListScheduler INSTANCE = new TabListScheduler();

  protected static TabListScheduler getInstance() {
    return INSTANCE;
  }

  private final List<ScheduledSingleUserTabList> tabListList = new ArrayList<>();

  private TabListScheduler() {
    TaskManager.getInstance().runRepeatedBukkit(this, 1, 1);
  }

  protected void register(ScheduledSingleUserTabList tabList) {
    tabListList.add(tabList);
  }

  protected void unregister(ScheduledSingleUserTabList tabList) {
    tabListList.remove(tabList);
  }

  @Override
  public void run() {
    tabListList.forEach(ScheduledSingleUserTabList::onTick);
  }
}
