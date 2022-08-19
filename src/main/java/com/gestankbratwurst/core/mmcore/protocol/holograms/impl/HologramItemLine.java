package com.gestankbratwurst.core.mmcore.protocol.holograms.impl;


import com.gestankbratwurst.core.mmcore.protocol.holograms.AbstractHologramItemLine;
import com.gestankbratwurst.core.mmcore.protocol.packetwrapper.WrapperPlayServerMount;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class HologramItemLine extends AbstractHologramItemLine {

  public HologramItemLine(final Location location, final ItemStack itemStack, final Hologram hologram) {
    super(location, itemStack, hologram);
    this.itemEntity = new ItemEntity(location, itemStack);
    this.itemVehicle = new ItemVehicle(location.clone().add(0, -0.1, 0));
    this.packets = new Packet<?>[6];
    this.packets[0] = this.itemEntity.getSpawnPacket();
    this.packets[1] = this.itemEntity.getDespawnPacket();
    this.packets[2] = this.itemEntity.getUpdatePacket();
    this.packets[3] = this.itemVehicle.getSpawnPacket();
    this.packets[4] = this.itemVehicle.getDespawnPacket();
    this.packets[5] = this.itemVehicle.getHidePacket();
    this.mountPacket = this.itemVehicle.getMountPacket(this.itemEntity);
  }

  private final ItemEntity itemEntity;
  private final ItemVehicle itemVehicle;
  private final Packet<?>[] packets;
  private WrapperPlayServerMount mountPacket;

  @Override
  public void showTo(final Player player) {
    final ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
    connection.send(this.packets[0]);
    connection.send(this.packets[3]);
    connection.send(this.packets[2]);
    connection.send(this.packets[5]);
    this.mountPacket.sendPacket(player);
  }

  @Override
  public void hideFrom(final Player player) {
    final ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
    connection.send(this.packets[1]);
    connection.send(this.packets[4]);
  }

  @Override
  public void update(final ItemStack newValue) {
    this.itemEntity.setItem(CraftItemStack.asNMSCopy(newValue));
    this.packets[0] = this.itemEntity.getSpawnPacket();
    this.packets[1] = this.itemEntity.getDespawnPacket();
    this.packets[2] = this.itemEntity.getUpdatePacket();
    this.mountPacket = this.itemVehicle.getMountPacket(this.itemEntity);

    for (final Player player : this.getHostingHologram().getViewers()) {
      final ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
      connection.send(this.packets[2]);
      connection.send(this.packets[5]);
    }
  }

  private static final class ItemVehicle extends ArmorStand {

    public ItemVehicle(final Location location) {
      super(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY() + 0.2,
          location.getZ());
      this.setMarker(true);
      this.setInvisible(true);
      this.setCustomNameVisible(false);
    }

    public ClientboundAddEntityPacket getSpawnPacket() {
      return new ClientboundAddEntityPacket(this);
    }

    public ClientboundSetEntityDataPacket getHidePacket() {
      return new ClientboundSetEntityDataPacket(this.getId(), this.entityData, true);
    }

    public ClientboundRemoveEntitiesPacket getDespawnPacket() {
      return new ClientboundRemoveEntitiesPacket(this.getId());
    }

    public WrapperPlayServerMount getMountPacket(final net.minecraft.world.entity.item.ItemEntity item) {
      final WrapperPlayServerMount packet = new WrapperPlayServerMount();

      packet.setEntityID(this.getId());
      packet.setPassengerIds(new int[]{item.getId()});

      return packet;
    }

  }

  private static final class ItemEntity extends net.minecraft.world.entity.item.ItemEntity {

    private ItemEntity(final Location location, final ItemStack item) {
      super(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(),
          location.getZ(), CraftItemStack.asNMSCopy(item));
      this.setInvulnerable(true);
      this.setNoGravity(true);
      moveTo(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void tick() {

    }

    public ClientboundAddEntityPacket getSpawnPacket() {
      return new ClientboundAddEntityPacket(this);
    }

    public ClientboundRemoveEntitiesPacket getDespawnPacket() {
      return new ClientboundRemoveEntitiesPacket(this.getId());
    }

    public ClientboundSetEntityDataPacket getUpdatePacket() {
      return new ClientboundSetEntityDataPacket(this.getId(), this.entityData, true);
    }

  }

  @Override
  public void sendMove(final Player player, final Vector direction) {
    final ClientboundMoveEntityPacket movePacket = new ClientboundMoveEntityPacket.Pos(this.itemEntity.getId(),
        (short) (direction.getX() * 4096), (short) (direction.getY() * 4096),
        (short) (direction.getZ() * 4096), false);
    ((CraftPlayer) player).getHandle().connection.send(movePacket);
  }

}
