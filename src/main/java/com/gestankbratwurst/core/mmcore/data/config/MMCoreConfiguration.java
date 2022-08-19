package com.gestankbratwurst.core.mmcore.data.config;

import com.gestankbratwurst.core.mmcore.data.mongodb.MongoDriverProperties;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of MMCore and was created at the 06.08.2021
 *
 * MMCore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@NoArgsConstructor
public class MMCoreConfiguration {

  public static MMCoreConfiguration get() {
    return MMCoreConfigManager.getConfiguration();
  }

  public String getServerName() {
    return this.serverConfigs.serverName;
  }

  public String getRedisAddress() {
    return this.serverConfigs.redisAddress;
  }

  public MongoDriverProperties getMongoDriverProperties() {
    return this.serverConfigs.mongoDriverProperties;
  }

  public String getResourcePackServerIP() {
    return this.serverConfigs.resourcePackServerIP;
  }

  public int getResourcePackServerPort() {
    return this.serverConfigs.resourcePackServerPort;
  }

  public String getMineSkinClientAPIToken() {
    return this.serverConfigs.mineSkinClientAPIToken;
  }

  public String getMineSkinClientKeySecret() {
    return this.serverConfigs.mineSkinClientKeySecret;
  }

  public String getMineSkinClientUserAgent() {
    return this.serverConfigs.mineSkinClientUserAgent;
  }

  public boolean isRedisEnabled() {
    return this.serverConfigs.redisEnabled;
  }

  private static class ServerConfigs {

    @SerializedName("ServerName")
    private String serverName = "Instanz_I";
    @SerializedName("RedisAddress")
    private String redisAddress = "redis://127.0.0.1:6379";
    @SerializedName("MongoDriverProperties")
    private MongoDriverProperties mongoDriverProperties = MongoDriverProperties.builder()
        .database("Revenant")
        .user("admin")
        .password("admin")
        .hostAddress("localhost")
        .hostPort(27017)
        .build();
    @SerializedName("RedisEnabled")
    private boolean redisEnabled = false;
    @SerializedName("ArbitraryDataDefaultCacheMinutes")
    private long arbitraryDataDefaultCacheMinutes = 30;
    @SerializedName("PlayerDataCacheMinutes")
    private long playerDataCacheMinutes = 60;
    @SerializedName("CacheWriteBehindDelayMillis")
    private int cacheWriteBehindDelayMillis = 5000;
    @SerializedName("CacheWriteBehindBatchSize")
    private int cacheWriteBehindBatchSize = 50;
    @SerializedName("ResourcePackServerIP")
    private String resourcePackServerIP = "127.0.0.1";
    @SerializedName("ResourcePackServerPort")
    private int resourcePackServerPort = 9988;
    @SerializedName("MineSkinClientAPIToken")
    private String mineSkinClientAPIToken = "4e4d5e9f0d61a084e0673f99f49fd182280fb670151209f46fdc5c2a38867fdb";
    @SerializedName("MineSkinClientKeySecret")
    private String mineSkinClientKeySecret = "4ee3343aec34213a2df5616b137c6a36f4a0e89884b9b3b4852019b7faa33c4d912d9cd89c78c4b968d0b78d34b0c35d9025b8f7d7da4a78c2ca131ff5c05528";
    @SerializedName("MineSkinClientUserAgent")
    private String mineSkinClientUserAgent = "BlockLifeAgent";

  }

  @Getter
  private final ServerConfigs serverConfigs = new ServerConfigs();

}