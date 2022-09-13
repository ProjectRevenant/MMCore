package com.gestankbratwurst.core.mmcore.inventories.guis;

import com.gestankbratwurst.core.mmcore.MMCore;
import com.gestankbratwurst.core.mmcore.util.tasks.TaskManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class GUIManager implements Runnable {

  protected static GUIManager instance;

  protected static GUIManager getInstance() {
    if (instance == null) {
      instance = new GUIManager();
      final GUIListener listener = new GUIListener(instance);
      Bukkit.getPluginManager().registerEvents(listener, JavaPlugin.getPlugin(MMCore.class));
      TaskManager.getInstance().runTaskTimer(instance, 1, 1);
    }
    return instance;
  }

  private final Map<Inventory, AbstractGUIInventory> inventoryHandlerMap = new Object2ObjectOpenHashMap<>();

  protected void addHandler(final Inventory inventory, final AbstractGUIInventory handler) {
    this.inventoryHandlerMap.put(inventory, handler);
  }

  protected Optional<AbstractGUIInventory> getOptionalHandlerOf(final Inventory inventory) {
    return Optional.ofNullable(this.inventoryHandlerMap.get(inventory));
  }

  protected void terminate(final Inventory inventory) {
    this.inventoryHandlerMap.remove(inventory);
  }

  @Override
  public void run() {
    Bukkit.getOnlinePlayers().forEach(player -> {
      Inventory inventory = player.getOpenInventory().getTopInventory();
      getOptionalHandlerOf(inventory).ifPresent(handler -> handler.autoUpdateButtons(player, inventory));
    });
  }
}