package com.gestankbratwurst.core.mmcore.util.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 22/01/2022
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ServerPingUtil implements PluginMessageListener {

  private static final ServerPingUtil INSTANCE = new ServerPingUtil();
  private static final int DEFAULT_TIMEOUT = 3000;
  private static boolean registered = false;
  private static JavaPlugin registrationHost;

  public static CompletableFuture<Boolean> pingServerOnline(Player target, String serverName, int timeOutMillis) {
    return INSTANCE.isReachable(target, serverName, timeOutMillis);
  }

  public static CompletableFuture<Boolean> pingServerOnline(Player target, String serverName) {
    return INSTANCE.isReachable(target, serverName, DEFAULT_TIMEOUT);
  }

  public static void enable(JavaPlugin plugin) {
    if (registered) {
      return;
    }
    plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
    plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", INSTANCE);
    registrationHost = plugin;
    registered = true;
  }

  public static void disable() {
    if (!registered) {
      return;
    }
    registrationHost.getServer().getMessenger().unregisterIncomingPluginChannel(registrationHost);
    registrationHost.getServer().getMessenger().unregisterOutgoingPluginChannel(registrationHost);
    registered = false;
  }

  private final Map<String, String> addressCache = new HashMap<>();
  private final Map<String, List<BlockingQueue<String>>> awaitingConsumer = new HashMap<>();

  private ServerPingUtil() {}

  private CompletableFuture<Boolean> isReachable(Player player, String serverName, int timeOutMillis) {
    return CompletableFuture.supplyAsync(() -> {
      String address = getIpBlocking(player, serverName, timeOutMillis);
      try {
        return InetAddress.getByName(address).isReachable(timeOutMillis);
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    });
  }

  private String getIpBlocking(Player player, String serverName, int timeoutMillis) {
    return addressCache.computeIfAbsent(serverName, name -> sendAndAwaitAddress(player, name, timeoutMillis));
  }

  private String sendAndAwaitAddress(Player player, String serverName, int timeoutMillis) {
    SynchronousQueue<String> handOffGateway = new SynchronousQueue<>();
    awaitingConsumer.computeIfAbsent(serverName, key -> new ArrayList<>()).add(handOffGateway);
    sendIPMessage(player, serverName);
    try {
      return handOffGateway.poll(timeoutMillis, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      return null;
    }
  }

  private void sendIPMessage(Player player, String serverName) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("ServerIP");
    out.writeUTF(serverName);
    player.sendPluginMessage(registrationHost, "BungeeCord", out.toByteArray());
  }

  @Override
  public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
    if (!channel.equals("BungeeCord")) {
      return;
    }
    ByteArrayDataInput in = ByteStreams.newDataInput(message);
    String subChannel = in.readUTF();
    if (subChannel.equals("ServerIP")) {
      String serverName = in.readUTF();
      String ip = in.readUTF();
      int port = in.readUnsignedShort();
      String address = ip + ":" + port;
      List<BlockingQueue<String>> consumerList = awaitingConsumer.remove(serverName);
      for (BlockingQueue<String> consumer : consumerList) {
        consumer.offer(address);
      }
    }
  }

}
