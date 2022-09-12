package com.gestankbratwurst.core.mmcore.resourcepack.sounds;

import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of avarioncore and was created at the 26.04.2020
 *
 * avarioncore can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public enum CustomSound {

  COINS_SOUND(),
  PEACE_SOUND(),
  TRUMPET(),
  DARK_WAVE(),
  DARK_WAVE_HIT(),
  ORC_HORN(),
  ORC_CRY(),
  HEART_BEAT_10S(),
  UNDEAD_THEME(),
  ORC_THEME(),
  ELF_THEME(),
  HUMAN_THEME(),
  DWARF_THEME(),
  CHOIRS_OF_WAR(),
  APOLLO(),
  MERAKI(),
  STORM(),
  WAR_HORN(),
  STICK_HIT_ONE(),
  STICK_HIT_TWO(),
  STICK_HIT_THREE();

  @Getter
  private ResourceLocation key = null;

  public void playHeadArtificialDist(final Location location, final double radius, final float pitch,
      final SoundSource soundCategory) {
    final double maxSquared = -(radius * radius);
    location.getNearbyPlayers(radius).forEach(
        player -> this.play(player, location, soundCategory, this.calcVolumeSquared(location, player.getLocation(), maxSquared), pitch));
  }

  private float calcVolumeSquared(final Location soundPoint, final Location distPoint, final double maxSq) {
    final double distSq = soundPoint.distanceSquared(distPoint);
    return (float) (1.0 - (distSq / maxSq));
  }

  public void stopFor(Player player) {
    stopFor(player, SoundSource.AMBIENT);
  }

  public void stopFor(Player player, SoundSource category) {
    ClientboundStopSoundPacket stopPacket = new ClientboundStopSoundPacket(key, category);
    ((CraftPlayer) player).getHandle().connection.send(stopPacket);
  }

  public void play(final Player player) {
    this.play(player, player.getEyeLocation(), SoundSource.AMBIENT, 1F, 1F);
  }

  public void play(final Player player, final Location location) {
    this.play(player, location, SoundSource.AMBIENT, 1F, 1F);
  }

  public void play(final Player player, final Location location, final float volume, final float pitch) {
    this.play(player, location, SoundSource.AMBIENT, volume, pitch);
  }

  public void play(final Player player, final Location location, final SoundSource soundCategory) {
    this.play(player, location, soundCategory, 1F, 1F);
  }

  public void play(final Player player, final SoundSource soundCategory, final float volume, final float pitch) {
    this.play(player, player.getEyeLocation(), soundCategory, volume, pitch);
  }

  public void play(final Player player, final float volume, final float pitch) {
    this.play(player, player.getEyeLocation(), SoundSource.AMBIENT, volume, pitch);
  }

  public void playAt(final Location location, final org.bukkit.SoundCategory soundCategory, final float volume, final float pitch) {
    if (this.key == null) {
      this.key = new ResourceLocation("custom." + this.toString().toLowerCase());
    }
    location.getWorld().playSound(location, this.key.getPath(), soundCategory, volume, pitch);
  }

  public void play(final Player player, final Location location, final SoundSource soundCategory, final float volume, final float pitch) {
    if (this.key == null) {
      this.key = new ResourceLocation("custom." + this.toString().toLowerCase());
    }
    final Vec3 vec = new Vec3(location.getX(), location.getY(), location.getZ());
    final long seed = ThreadLocalRandom.current().nextInt();
    final ClientboundCustomSoundPacket packet = new ClientboundCustomSoundPacket(this.key, soundCategory, vec, volume, pitch, seed);
    ((CraftPlayer) player).getHandle().connection.send(packet);
  }

}
