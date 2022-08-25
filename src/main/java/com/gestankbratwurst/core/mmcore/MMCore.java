package com.gestankbratwurst.core.mmcore;

import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.gestankbratwurst.core.mmcore.actionbar.ActionBarManager;
import com.gestankbratwurst.core.mmcore.blockdata.BlockDataListener;
import com.gestankbratwurst.core.mmcore.blockdata.BlockDataManager;
import com.gestankbratwurst.core.mmcore.data.config.MMCoreConfigManager;
import com.gestankbratwurst.core.mmcore.data.config.MMCoreConfiguration;
import com.gestankbratwurst.core.mmcore.data.json.GsonProvider;
import com.gestankbratwurst.core.mmcore.data.json.PostProcessingEnabler;
import com.gestankbratwurst.core.mmcore.data.json.adapter.ClassAdapter;
import com.gestankbratwurst.core.mmcore.data.json.adapter.ItemStackAdapter;
import com.gestankbratwurst.core.mmcore.data.json.adapter.LocationAdapter;
import com.gestankbratwurst.core.mmcore.data.mongodb.MongoStorage;
import com.gestankbratwurst.core.mmcore.data.redis.RedisGsonCodec;
import com.gestankbratwurst.core.mmcore.protocol.holograms.impl.HologramManager;
import com.gestankbratwurst.core.mmcore.resourcepack.ModelCommand;
import com.gestankbratwurst.core.mmcore.resourcepack.ResourcepackModule;
import com.gestankbratwurst.core.mmcore.resourcepack.SkinChooserCommand;
import com.gestankbratwurst.core.mmcore.resourcepack.skins.TextureModel;
import com.gestankbratwurst.core.mmcore.skinclient.PlayerSkinManager;
import com.gestankbratwurst.core.mmcore.tablist.TabListManager;
import com.gestankbratwurst.core.mmcore.tablist.implementation.EmptyTabList;
import com.gestankbratwurst.core.mmcore.tokenclick.TokenActionManager;
import com.gestankbratwurst.core.mmcore.tracking.ChunkTracker;
import com.gestankbratwurst.core.mmcore.tracking.EntityTracker;
import com.gestankbratwurst.core.mmcore.util.MsgCommand;
import com.gestankbratwurst.core.mmcore.util.common.BukkitTime;
import com.gestankbratwurst.core.mmcore.util.common.ChatInput;
import com.gestankbratwurst.core.mmcore.util.common.NamespaceFactory;
import com.gestankbratwurst.core.mmcore.util.common.UtilItem;
import com.gestankbratwurst.core.mmcore.util.common.UtilMobs;
import com.gestankbratwurst.core.mmcore.util.common.UtilPlayer;
import com.gestankbratwurst.core.mmcore.util.items.display.ItemDisplayCompiler;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

public final class MMCore extends JavaPlugin {

  private static ResourcepackModule resourcepackModule;

  @Getter(AccessLevel.MODULE)
  private static MMCore instance;
  @Getter
  private static ActionBarManager actionBarManager;
  @Getter
  private static HologramManager hologramManager;
  @Getter
  private static TabListManager tabListManager;
  @Getter
  private static ItemDisplayCompiler displayCompiler;
  @Getter
  private static ProtocolManager protocolManager;
  @Getter
  private static PlayerSkinManager playerSkinManager;
  @Getter
  private static PaperCommandManager paperCommandManager;
  @Getter
  private static TokenActionManager tokenActionManager;
  @Getter
  private static RedissonClient redissonClient;
  @Getter
  private static BlockDataManager blockDataManager;
  @Getter
  private static GsonProvider gsonProvider;
  @Getter
  private static MongoStorage mongoStorage;

  private BlockDataListener blockDataListener;

  @Override
  public void onEnable() {
    instance = this;
    gsonProvider = GsonProvider.INSTANCE;

    this.getLogger().info("Registering default gson (de)serializer.");
    this.registerDefaultGsonSerializer();

    this.getLogger().info("Loading configuration data.");
    MMCoreConfigManager.init(this);

    this.getLogger().info("Enabling mongo storage.");
    LoggerContext loggerContext = LoggerContext.getContext();
    Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
    rootLogger.setLevel(Level.WARN);

    mongoStorage = new MongoStorage();

    if (!MMCoreConfiguration.get().isRedisEnabled()) {
      this.getLogger().info("Connecting to redis.");
      final Config config = new Config();
      SingleServerConfig singleServerConfig = config.useSingleServer();
      singleServerConfig.setRetryInterval(2000);
      singleServerConfig.setTimeout(5000);
      singleServerConfig.setConnectionPoolSize(128);
      singleServerConfig.setAddress(MMCoreConfiguration.get().getRedisAddress());
      config.setNettyThreads(64);
      config.setCodec(new RedisGsonCodec());
      redissonClient = Redisson.create(config);
    }

    this.getLogger().info("Creating manager instances.");
    blockDataManager = new BlockDataManager();
    paperCommandManager = new PaperCommandManager(this);
    protocolManager = ProtocolLibrary.getProtocolManager();
    actionBarManager = new ActionBarManager(this);
    hologramManager = new HologramManager(this);
    tabListManager = new TabListManager(this, player -> new EmptyTabList());
    displayCompiler = new ItemDisplayCompiler(this);
    playerSkinManager = PlayerSkinManager.load();
    resourcepackModule = new ResourcepackModule();
    resourcepackModule.enable(this);
    tokenActionManager = new TokenActionManager();

    paperCommandManager.getCommandCompletions().registerStaticCompletion("Model", () -> Arrays
        .stream(TextureModel.values())
        .map(Enum::toString)
        .collect(Collectors.toList()));
    paperCommandManager.registerCommand(new ModelCommand());

    this.getLogger().info("Initiating utility classes.");
    this.initUtils();
  }

  private void registerDefaultGsonSerializer() {
    gsonProvider.disableHtmlEscaping();

    gsonProvider.registerTypeAdapter(Class.class, new ClassAdapter());
    gsonProvider.registerTypeAdapter(Location.class, new LocationAdapter());
    gsonProvider.registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter());

    gsonProvider.registerTypeAdapterFactory(new PostProcessingEnabler());
  }

  private void initUtils() {
    ProtocolLibrary.getProtocolManager().addPacketListener(displayCompiler);
    BukkitTime.start(this);
    ChatInput.init(this);
    NamespaceFactory.init(this);
    UtilPlayer.init(this);
    UtilMobs.init(this);
    UtilItem.init(this);
    Bukkit.getPluginManager().registerEvents(new ChunkTracker(this), this);
    Bukkit.getPluginManager().registerEvents(new EntityTracker(this), this);
    blockDataListener = new BlockDataListener(blockDataManager);
    blockDataListener.initialInit();
    Bukkit.getPluginManager().registerEvents(blockDataListener, this);
    paperCommandManager.registerCommand(new SkinChooserCommand());
    paperCommandManager.registerCommand(new MsgCommand());
  }

  @SneakyThrows
  @Override
  public void onDisable() {
    Optional.ofNullable(blockDataListener).ifPresent(BlockDataListener::cleanupTermination);
    Optional.ofNullable(resourcepackModule).ifPresent(module -> module.disable(this));
    mongoStorage.getAccess().disconnect();
    Optional.ofNullable(redissonClient).ifPresent(RedissonClient::shutdown);
  }

}
