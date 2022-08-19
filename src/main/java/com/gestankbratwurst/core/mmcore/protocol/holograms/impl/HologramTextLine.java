package com.gestankbratwurst.core.mmcore.protocol.holograms.impl;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.gestankbratwurst.core.mmcore.protocol.holograms.AbstractHologramTextLine;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.util.Vector;

public class HologramTextLine extends AbstractHologramTextLine {

  public HologramTextLine(final Location location, final String text, final Hologram hologram) {
    super(location, hologram);
    this.textEntity = new TextEntity(location, text);
  }

  private final TextEntity textEntity;

  @Override
  public void showTo(final Player player) {
    this.textEntity.sendSpawnPacket(player);
  }

  @Override
  public void hideFrom(final Player player) {
    this.textEntity.sendDespawnPacket(player);
  }

  @Override
  public void update(final String newValue) {
    this.textEntity.setCustomName(Component.literal(newValue));
    for (final Player player : this.getHostingHologram().getViewers()) {
      this.textEntity.updateMetadata(player);
    }
  }

  private static final class TextEntity extends ArmorStand {

    public TextEntity(final Location location, final String line) {
      super(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
      this.setMarker(true);
      this.setInvulnerable(true);
      this.setInvisible(true);
      this.setCustomName(Component.literal(line));
      this.setCustomNameVisible(true);
    }

    public void sendSpawnPacket(final Player player) {
      ((CraftPlayer) player).getHandle().connection.send(new ClientboundAddEntityPacket(this));
      this.updateMetadata(player);
    }

    public void sendDespawnPacket(final Player player) {

      ((CraftPlayer) player).getHandle().connection.send(new ClientboundRemoveEntitiesPacket(this.getId()));
    }

    public void updateMetadata(final Player player) {
      final PacketContainer container = PacketContainer
          .fromPacket(new ClientboundSetEntityDataPacket(this.getId(), this.entityData, true));
      container.setMeta("mmcore-hologram", true);
      ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);
      //			((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityMetadata(this.getId(), this.getDataWatcher(), true));
    }


  }

  @Override
  public void sendMove(final Player player, final Vector direction) {
    final ClientboundMoveEntityPacket movePacket = new ClientboundMoveEntityPacket.Pos(this.textEntity.getId(),
        (short) (direction.getX() * 4096), (short) (direction.getY() * 4096),
        (short) (direction.getZ() * 4096), false);
    ((CraftPlayer) player).getHandle().connection.send(movePacket);
  }

}
