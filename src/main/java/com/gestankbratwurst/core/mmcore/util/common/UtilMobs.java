package com.gestankbratwurst.core.mmcore.util.common;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of avarioncore and was created at the 08.04.2020
 *
 * avarioncore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilMobs implements Listener {

  private static final String DOMESTICATION_TAG = "DOMESTIC";
  private static final Cache<Integer, Entity> ENTITY_UNLOAD_CACHE = Caffeine.newBuilder()
      .expireAfterWrite(1, TimeUnit.MINUTES)
      .build();


  public static Entity getEntity(final int id, final World world) {
    final net.minecraft.world.entity.Entity nmsEntity = ((CraftWorld) world).getHandle().getEntity(id);
    if (nmsEntity != null) {
      return nmsEntity.getBukkitEntity();
    }
    return UtilMobs.ENTITY_UNLOAD_CACHE.getIfPresent(id);
  }

  public static void init(final JavaPlugin plugin) {
    Bukkit.getPluginManager().registerEvents(new UtilMobs(plugin), plugin);
  }

  public static boolean isDomesticated(final LivingEntity entity) {
    return entity.getScoreboardTags().contains(UtilMobs.DOMESTICATION_TAG);
  }

  public static String serialize(final Entity entity) {
    final net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();
    final CompoundTag compound = new CompoundTag();
    nmsEntity.save(compound);
    return compound.toString();
  }

  public static void trueDamage(final LivingEntity entity, final DamageCause damageCause, final double damage) {
    final EntityDamageEvent event = new EntityDamageEvent(entity, damageCause, damage);
    if (event.callEvent()) {
      final double current = entity.getHealth();
      final double newHealth = current - damage;
      entity.setHealth(Math.max(0, newHealth));
    }
  }

  public static Entity deserialize(final String data, final Location location) throws CommandSyntaxException {
    final CompoundTag compound = TagParser.parseTag(data);
    return UtilMobs.getEntityFromNBT(compound, location);
  }

  private static Entity getEntityFromNBT(final CompoundTag compound, final Location location) {
    final CompoundTag clone = compound;

    final ServerLevel worldServer = ((CraftWorld) location.getWorld()).getHandle();

    net.minecraft.world.entity.Entity entity = EntityType.loadEntityRecursive(clone, worldServer, (spawnedEntity) -> {

      // FIXME :       spawnedEntity.dead = false;
      //               spawnedEntity.setUUID(UUID.randomUUID());
      // spawnedEntity.dead = false;
      // spawnedEntity.setUUID(UUID.randomUUID());
      spawnedEntity.moveTo(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getPitch());

      return !worldServer.addFreshEntity(spawnedEntity) ? null : spawnedEntity;
    });

    if (entity != null) {
      if (entity instanceof PathfinderMob) {
        ((PathfinderMob) entity).finalizeSpawn(worldServer, worldServer.getCurrentDifficultyAt(entity.getOnPos()), MobSpawnType.COMMAND,
            null, clone);
        // FIXME : EnumMobSpawn.COMMAND
      }
      return entity.getBukkitEntity();
    }
    return null;
  }

  private UtilMobs(final JavaPlugin plugin) {

  }

  private final Set<SpawnReason> domesticSpawnReasons = ImmutableSet.of(
      SpawnReason.BREEDING,
      SpawnReason.DISPENSE_EGG,
      SpawnReason.EGG);

  @EventHandler
  public void onSpawn(final CreatureSpawnEvent event) {
    if (this.domesticSpawnReasons.contains(event.getSpawnReason())) {
      event.getEntity().getScoreboardTags().add(UtilMobs.DOMESTICATION_TAG);
    }
  }

  @EventHandler
  public void onBreed(final EntityBreedEvent event) {
    if (event.getBreeder() == null) {
      return;
    }
    event.getMother().getScoreboardTags().add(UtilMobs.DOMESTICATION_TAG);
    event.getFather().getScoreboardTags().add(UtilMobs.DOMESTICATION_TAG);
  }

  @EventHandler
  public void onTame(final EntityTameEvent event) {
    event.getEntity().getScoreboardTags().add(UtilMobs.DOMESTICATION_TAG);
  }

  @EventHandler
  public void onUnload(final ChunkUnloadEvent event) {
    for (final Entity entity : event.getChunk().getEntities()) {
      UtilMobs.ENTITY_UNLOAD_CACHE.put(entity.getEntityId(), entity);
    }
  }

  @EventHandler
  public void onUnload(final WorldUnloadEvent event) {
    for (final Entity entity : event.getWorld().getEntities()) {
      UtilMobs.ENTITY_UNLOAD_CACHE.put(entity.getEntityId(), entity);
    }
  }

}
