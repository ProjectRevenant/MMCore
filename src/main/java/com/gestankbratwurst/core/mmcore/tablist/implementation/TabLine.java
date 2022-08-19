package com.gestankbratwurst.core.mmcore.tablist.implementation;

import com.gestankbratwurst.core.mmcore.tablist.abstraction.ITabLine;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket.Action;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of AvarionCore and was created at the 10.12.2019
 *
 * AvarionCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class TabLine implements ITabLine {

  private static final List<String> INDEXED_NAMES = IntStream.range(0, 1000).mapToObj(i -> String.format("%04d", i)).toList();

  @Getter
  private final ServerPlayer entity;
  private ClientboundPlayerInfoPacket showPacket;
  private final ClientboundPlayerInfoPacket hidePacket;
  private ClientboundPlayerInfoPacket namePacket;

  public TabLine(final UUID playerID, final int index, final String display) {
    final GameProfile profile = new GameProfile(playerID, " " + INDEXED_NAMES.get(index));
    final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
    final ServerLevel worldServer = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();

    final ServerPlayer player = new ServerPlayer(server, worldServer, profile, null);
    player.listName = Component.literal(display);

    this.entity = player;

    this.showPacket = new ClientboundPlayerInfoPacket(Action.ADD_PLAYER, player);
    this.namePacket = new ClientboundPlayerInfoPacket(Action.UPDATE_DISPLAY_NAME, player);
    this.hidePacket = new ClientboundPlayerInfoPacket(Action.REMOVE_PLAYER, player);
  }

  public TabLine(final int index) {
    this(UUID.randomUUID(), index, " ");
  }

  public TabLine(final int index, final String display) {
    this(UUID.randomUUID(), index, display);
  }

  @Override
  public void setDisplay(final String display) {
    this.entity.listName = Component.literal(display);
    this.namePacket = new ClientboundPlayerInfoPacket(Action.UPDATE_DISPLAY_NAME, this.entity);
  }

  @Override
  public String getDisplay() {
    return this.entity.listName.getString();
  }

  @Override
  public void setTexture(final String texture, final String signature) {
    final GameProfile profile = this.entity.getGameProfile();
    profile.getProperties().put("textures", new Property("textures", texture, signature));
    this.showPacket = new ClientboundPlayerInfoPacket(Action.ADD_PLAYER, this.entity);
  }

  @Override
  public void setTextureBase64(final String textureBase64) {
    final GameProfile profile = this.entity.getGameProfile();
    profile.getProperties().removeAll("textures");
    profile.getProperties().put("textures", new Property("textures", textureBase64));
    this.showPacket = new ClientboundPlayerInfoPacket(Action.ADD_PLAYER, this.entity);
  }

  @Override
  public void send(final ServerGamePacketListenerImpl connection) {
    connection.send(this.showPacket);
  }

  @Override
  public void sendDisplayUpdate(final ServerGamePacketListenerImpl connection) {
    connection.send(this.namePacket);
  }

  @Override
  public void sendProfileUpdate(final ServerGamePacketListenerImpl connection) {
    sendHide(connection);
    send(connection);
  }

  @Override
  public void sendHide(final ServerGamePacketListenerImpl connection) {
    connection.send(this.hidePacket);
  }

}