package com.gestankbratwurst.core.mmcore.protocol.holograms;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class AbstractHologram {

  private static final double MAX_MOVE_DIST = 8 * 8;

  public AbstractHologram(final Location baseLocation, final Predicate<Player> playerFilter, final AbstractHologramManager manager) {
    this(baseLocation, playerFilter, manager, UUID.randomUUID());
  }

  public AbstractHologram(
      final Location baseLocation, final Predicate<Player> playerFilter, final AbstractHologramManager manager, final UUID uuid) {
    this.lines = Lists.newArrayList();
    this.manager = manager;
    this.baseLocation = baseLocation;
    this.playerFilter = playerFilter;
    this.clickable = false;
    this.holoID = uuid;
  }

  protected final AbstractHologramManager manager;
  private final Location baseLocation;
  protected final ArrayList<IHologramLine<?>> lines;
  private Predicate<Player> playerFilter;
  protected boolean clickable;
  @Getter
  private final UUID holoID;


  private static Vector vectorFromRotations(double pitch, double yaw) {
    return new Vector(Math.sin(pitch) * Math.cos(yaw), Math.sin(pitch) * Math.sin(yaw), Math.cos(pitch));
  }

  public static Location getLocalCoord(double x, double y, double z, Location origin) {
    Location arrival = origin.clone();

    Vector dirX = vectorFromRotations(arrival.getPitch(), Location.normalizeYaw(arrival.getYaw() - 90));
    Vector dirY = vectorFromRotations(arrival.getYaw(), arrival.getPitch() - 90);
    Vector dirZ = arrival.getDirection().normalize();

    return arrival.add(dirX.multiply(x)).add(dirY.multiply(y)).add(dirZ.multiply(z));
  }

  public static void giveOrDrop(Player player, Collection<ItemStack> items) {
    giveOrDrop(player, items.toArray(new ItemStack[0]));
  }

  public static void giveOrDrop(Player player, ItemStack... items) {
    World world = player.getWorld();
    Location loc = player.getLocation();
    player.getInventory().addItem(items).values().forEach(overflown -> world.dropItem(loc, overflown));
  }

  protected void registerClickableEntities() {
    this.manager.setClickableIdentifier(this.getClickableEntityIds(), this);
  }

  public void delete() {
    this.manager.removeHologram(this);
  }

  public Set<Player> getViewers() {
    return this.manager.getViewing(this);
  }

  public void move(final Vector direction) {
    Preconditions.checkArgument(direction.lengthSquared() < MAX_MOVE_DIST, "Move distance can be 8 at most.");
    this.moveHologram(direction);
  }

  public int getSize() {
    return this.lines.size();
  }

  public void setPlayerFilter(final Predicate<Player> filter) {
    this.playerFilter = filter;
  }

  public boolean isViableViewer(final Player player) {
    return this.playerFilter.test(player);
  }

  protected void appendLine(final IHologramLine<?> line) {
    this.lines.add(line);
    for (final Player viewer : this.getViewers()) {
      line.showTo(viewer);
    }
  }

  public IHologramLine<?> getHologramLine(final int index) {
    return this.lines.get(index);
  }

  public void showTo(final Player player) {
    for (final IHologramLine<?> line : this.lines) {
      line.showTo(player);
      this.showClickableEntities(player);
    }
  }

  public void hideFrom(final Player player) {
    for (final IHologramLine<?> line : this.lines) {
      line.hideFrom(player);
      this.hideClickableEntities(player);
    }
  }

  public Location getBaseLocation() {
    return this.baseLocation;
  }

  public abstract void setClickable();

  protected abstract void showClickableEntities(Player player);

  protected abstract void hideClickableEntities(Player player);

  protected abstract Set<Integer> getClickableEntityIds();

  protected abstract void moveHologram(Vector direction);

  public abstract void appendTextLine(String text);

  public abstract void appendDynamicTextLine(Function<Player, String> dynamicLine);
  
  public abstract void appendItemLine(ItemStack item);

}