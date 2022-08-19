package com.gestankbratwurst.core.mmcore.tablist.implementation;

import com.gestankbratwurst.core.mmcore.tablist.abstraction.ITabLine;
import com.gestankbratwurst.core.mmcore.tablist.abstraction.ITabList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of AvarionCore and was created at the 10.12.2019
 *
 * AvarionCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class AbstractTabList implements ITabList {

  private final Set<ServerGamePacketListenerImpl> playerConnectionSet;
  protected final ArrayList<ITabLine> tabs;
  private ClientboundTabListPacket headerFooterPacket;

  public AbstractTabList() {
    this.playerConnectionSet = new HashSet<>();
    this.tabs = Lists.newArrayList();
    this.headerFooterPacket = new ClientboundTabListPacket(Component.literal("EMPTY_HEADER"), Component.literal("EMPTY_FOOTER"));
  }

  @Override
  public void addViewer(final ServerGamePacketListenerImpl connection) {
    this.playerConnectionSet.add(connection);
  }

  @Override
  public void removeViewer(final ServerGamePacketListenerImpl connection) {
    this.playerConnectionSet.remove(connection);
  }

  @Override
  public Set<ServerGamePacketListenerImpl> getViewers() {
    return this.playerConnectionSet;
  }

  @Override
  public int getSize() {
    return this.tabs.size();
  }

  @Override
  public ITabLine getLine(final int index) {
    return this.tabs.get(index);
  }

  @Override
  public void setHeader(final String header) {
    this.headerFooterPacket = new ClientboundTabListPacket(Component.literal(header), this.headerFooterPacket.footer);
  }

  @Override
  public String getHeader() {
    return this.headerFooterPacket.header.getString();
  }

  @Override
  public void setFooter(final String footer) {
    this.headerFooterPacket = new ClientboundTabListPacket(this.headerFooterPacket.header, Component.literal(footer));
  }

  @Override
  public String getFooter() {
    return this.headerFooterPacket.footer.getString();
  }

  @Override
  public void sendHeaderFooter(final ServerGamePacketListenerImpl connection) {
    connection.send(this.headerFooterPacket);
  }

  @Override
  public void addLine(final ITabLine line) {
    this.tabs.add(line);
  }

  public abstract void init();

}