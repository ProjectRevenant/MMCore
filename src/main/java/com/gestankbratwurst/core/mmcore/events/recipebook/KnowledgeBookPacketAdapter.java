package com.gestankbratwurst.core.mmcore.events.recipebook;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.gestankbratwurst.core.mmcore.util.tasks.TaskManager;
import org.bukkit.plugin.Plugin;

public class KnowledgeBookPacketAdapter extends PacketAdapter {
  public KnowledgeBookPacketAdapter(Plugin plugin) {
    super(plugin, PacketType.Play.Server.RECIPE_UPDATE);
  }

  @Override
  public void onPacketReceiving(PacketEvent event) {
    PacketContainer container = event.getPacket();
    boolean bookOpen = container.getBooleans().read(0);
    event.setCancelled(true);
    RecipeBookClickEvent recipeBookClickEvent = new RecipeBookClickEvent(event.getPlayer(), bookOpen);
    TaskManager.getInstance().runBukkitSync(recipeBookClickEvent::callEvent);
  }
}
