package com.gestankbratwurst.core.mmcore.protocol.holograms.impl;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.gestankbratwurst.core.mmcore.protocol.holograms.AbstractHologramTextLine;
import com.gestankbratwurst.core.mmcore.protocol.holograms.HologramLineType;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 04.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class DynamicHologramTextLine extends AbstractHologramTextLine {

  public DynamicHologramTextLine(final Location location, final Function<Player, String> showFunction, final Hologram hologram) {
    super(location, hologram);
    this.textEntity = new DynamicTextEntity(location, showFunction);
  }

  private final DynamicTextEntity textEntity;

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
    for (final Player player : this.getHostingHologram().getViewers()) {
      this.textEntity.updateMetadata(player);
    }
  }

  public void updateFunction(final Function<Player, String> function) {
    textEntity.showFunction = function;
    for (final Player player : this.getHostingHologram().getViewers()) {
      this.textEntity.updateMetadata(player);
    }
  }

  @Override
  public HologramLineType getType() {
    return HologramLineType.DYNAMIC_TEXT_LINE;
  }

  private static final class DynamicTextEntity extends ArmorStand {

    private Function<Player, String> showFunction;

    public DynamicTextEntity(final Location location, final Function<Player, String> showFunction) {
      super(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
      this.showFunction = showFunction;
      this.setMarker(true);
      this.setInvulnerable(true);
      this.setInvisible(true);
      this.setCustomName(Component.literal("§cERROR"));
      this.setCustomNameVisible(true);
    }

    public void sendSpawnPacket(final Player player) {
      this.setCustomName(Component.literal(this.showFunction.apply(player)));
      ((CraftPlayer) player).getHandle().connection.send(new ClientboundAddEntityPacket(this));
      this.updateMetadata(player);
    }

    public void sendDespawnPacket(final Player player) {
      ((CraftPlayer) player).getHandle().connection.send(new ClientboundRemoveEntitiesPacket(this.getId()));
    }

    public void updateMetadata(final Player player) {
      this.setCustomName(Component.literal(this.showFunction.apply(player)));
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
