package com.gestankbratwurst.core.mmcore.data.config;

import com.gestankbratwurst.core.mmcore.MMCore;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 06.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MMCoreConfigManager {

  @Getter(AccessLevel.PROTECTED)
  private static MMCoreConfiguration configuration;

  public static void init(final JavaPlugin plugin) {
    createMainFolderIfNotExist(plugin);
    try {
      loadAndMergeConfig(plugin);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private static void createMainFolderIfNotExist(final JavaPlugin plugin) {
    final File folder = plugin.getDataFolder();
    if (!folder.exists()) {
      folder.mkdirs();
    }
  }

  private static void loadAndMergeConfig(final JavaPlugin plugin) throws IOException {
    final File configFile = new File(plugin.getDataFolder(), "configuration.json");
    if (configFile.exists()) {
      plugin.getLogger().info("Config file exists. Merging defaults.");
      String json = Files.readString(configFile.toPath());
      final MMCoreConfiguration config = MMCore.getGsonProvider().fromJson(json, MMCoreConfiguration.class);
      json = MMCore.getGsonProvider().toJsonPretty(config);
      MMCoreConfigManager.configuration = config;
      Files.writeString(configFile.toPath(), json);
    } else {
      plugin.getLogger().info("Config file does not exist. Creating defaults.");
      final MMCoreConfiguration configuration = new MMCoreConfiguration();
      String json = MMCore.getGsonProvider().toJsonPretty(configuration);
      MMCoreConfigManager.configuration = configuration;
      Files.writeString(configFile.toPath(), json);
    }
  }

}
