package com.gestankbratwurst.core.mmcore.util.common;

import com.google.common.base.Preconditions;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FeroCore and was created at the 10.11.2020
 *
 * FeroCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilInv {

  public static void scramble(Inventory inventory) {
    int size = inventory.getSize();
    ItemStack[] content = new ItemStack[size];
    for (ItemStack itemStack : inventory) {
      if(itemStack == null) {
        continue;
      }
      while (itemStack.getAmount() > 0) {
        ItemStack single = itemStack.asOne();
        itemStack = itemStack.subtract();
        randomlyPlaceInSingle(content, single);
      }
    }
    inventory.setContents(content);
  }

  private static void randomlyPlaceInSingle(ItemStack[] content, ItemStack itemStack) {
    Preconditions.checkArgument(itemStack.getAmount() == 1);
    boolean wrapAround = false;
    int index = ThreadLocalRandom.current().nextInt(content.length);
    while (true) {
      if (content[index] == null) {
        content[index] = itemStack;
        return;
      }
      if (content[index].isSimilar(itemStack)) {
        if (content[index].getAmount() < content[index].getMaxStackSize()) {
          content[index].add();
          return;
        }
      }
      index++;
      if (index == content.length) {
        if (wrapAround) {
          throw new IllegalStateException("No space for " + itemStack + " while scrambling.");
        }
        wrapAround = true;
        index = 0;
      }
    }
  }

  public static ItemStack[] getVerticallyFlippedContent(final Inventory inventory) {
    if (inventory.getType() != InventoryType.CHEST) {
      throw new UnsupportedOperationException("Currently only supports chest inventories.");
    }
    final ItemStack[] baseContent = inventory.getContents();
    final ItemStack[] flippedContent = new ItemStack[baseContent.length];

    final int rows = baseContent.length % 9;

    for (int row = 0; row < rows; row++) {
      for (int x = 0; x < 9; x++) {
        flippedContent[row * 9 + (8 - x)] = baseContent[row * 9 + x];
      }
    }

    return flippedContent;
  }

  public static int getVerticallyFlippedIndex(final int index) {
    final int x = index % 9;
    final int row = index / 9;
    return row * 9 + (8 - x);
  }

  public static int remove(final Iterable<ItemStack> content, final ItemStack item, final int amount) {
    int left = amount;
    for (final ItemStack invItem : content) {
      if (invItem != null && invItem.isSimilar(item)) {
        final int size = invItem.getAmount();
        if (size > left) {
          invItem.setAmount(size - left);
          left = 0;
          break;
        } else {
          left -= size;
          invItem.setAmount(0);
        }
      }
    }
    return amount - left;
  }

  public static int getAmountCrafted(final CraftItemEvent event) {
    if (event.isCancelled()) {
      return 0;
    }

    final ItemStack current = event.getCurrentItem();

    if (current == null) {
      return 0;
    }

    final int itemsPerCraft = current.getAmount();
    final int maxStackSize = current.getMaxStackSize();
    final Player player = (Player) event.getWhoClicked();

    if (event.getClick() == ClickType.DROP) {
      return 1;
    }

    if (!event.isShiftClick()) {
      final ItemStack cursorItem = player.getItemOnCursor();
      if (!cursorItem.isSimilar(current)) {
        return 0;
      }
      if (maxStackSize - cursorItem.getAmount() < itemsPerCraft) {
        return 0;
      }
      return 1;
    }

    int lowestIngredientAmount = 64;
    for (final ItemStack ingredient : event.getInventory().getMatrix()) {
      if (ingredient == null || ingredient.getType() == Material.AIR) {
        continue;
      }
      final int amount = ingredient.getAmount();
      if (amount < lowestIngredientAmount) {
        lowestIngredientAmount = amount;
      }
    }

    final int potentialCraftedAmount = itemsPerCraft * lowestIngredientAmount;

    int freeSpaces = 0;
    for (final ItemStack invItem : player.getInventory().getStorageContents()) {
      if (invItem == null || invItem.getType() == Material.AIR) {
        freeSpaces += maxStackSize;
      } else if (invItem.isSimilar(current)) {
        freeSpaces += maxStackSize - invItem.getAmount();
      }
      if (freeSpaces >= potentialCraftedAmount) {
        freeSpaces = potentialCraftedAmount;
        break;
      }
    }

    return freeSpaces;
  }

  public static boolean contains(final Iterable<ItemStack> content, final ItemStack item, final int amount) {
    int left = amount;
    for (final ItemStack invItem : content) {
      if (invItem != null && invItem.isSimilar(item)) {
        left -= invItem.getAmount();
        if (left <= 0) {
          return true;
        }
      }
    }
    return false;
  }

}
