package com.gestankbratwurst.core.mmcore.tablist.implementation;

import java.util.UUID;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 22.12.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class SingleUserTabList extends AbstractTabList {

  @Getter
  protected final UUID userID;

  public SingleUserTabList(UUID userID) {
    this.userID = userID;
  }

  public abstract void onPlayerAdd();

  public abstract void onPlayerRemove();

}
