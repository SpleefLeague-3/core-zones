package com.spleefleague.zone.fragments;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.spleefleague.core.Core;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author NickM13
 * @since 2/15/2021
 */
public class FragmentUtils {

    private static final ProtocolManager protocolManager = Core.getProtocolManager();

    private static final UUID unused = UUID.randomUUID();
    private static final EntityArmorStand entityArmorStand = new EntityArmorStand(((CraftWorld) Core.OVERWORLD).getHandle(), 0, 0, 0);

    static {
        ArmorStand armorStand = (ArmorStand) entityArmorStand.getBukkitEntity();
        armorStand.setInvisible(true);
        armorStand.setSmall(true);
    }

    public static void sendSpawnPacket(Player player, int entityId, double x, double y, double z, ItemStack item) {
        // Create a new packet of type "SPAWN_ENTITY".
        PacketContainer spawnPacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        // Set the entity ID to a new unique value.
        spawnPacket.getIntegers().write(0, player.getEntityId() + 1);
        // Set the entity UUID to a random value.
        spawnPacket.getUUIDs().write(0, unused);
        // Set the entity type to armor stand.
        spawnPacket.getIntegers().write(1, 2);
        // Set the location of the armor stand.
        spawnPacket.getDoubles().write(0, x);
        spawnPacket.getDoubles().write(1, y);
        spawnPacket.getDoubles().write(2, z);
        // Set the pitch and yaw of the armor stand to 0.
        spawnPacket.getBytes().write(0, (byte) 0);
        spawnPacket.getBytes().write(1, (byte) 0);
        // Set the object data to 0.
        spawnPacket.getIntegers().write(4, 0);
        // Set velocity to 0.
        spawnPacket.getShorts().write(0, (short) 0);
        spawnPacket.getShorts().write(1, (short) 0);
        spawnPacket.getShorts().write(2, (short) 0);

        List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> items = new ArrayList<>();
        items.add(new Pair<>(EnumItemSlot.f, item));
        PacketPlayOutEntityEquipment packetPlayOutEntityEquipment = new PacketPlayOutEntityEquipment(entityId, items);
        PacketContainer equipmentPacket = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT, packetPlayOutEntityEquipment);

        // Send the packets to the player.
        protocolManager.sendServerPacket(player, spawnPacket, null, false);
        protocolManager.sendServerPacket(player, equipmentPacket, null, false);
    }

    public static void sendEntityMovePacket(Player player, int entityId, short changeX, short changeY, short changeZ) {
        PacketPlayOutEntity.PacketPlayOutRelEntityMove relEntityMovePacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                entityId,
                changeX,
                changeY,
                changeZ,
                true
        );
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.REL_ENTITY_MOVE, relEntityMovePacket);

        protocolManager.sendServerPacket(player, packet, null, false);
    }

    public static void sendDestroyPacket(Player player, int entityId) {
        PacketPlayOutEntityDestroy entityDestroyPacket = new PacketPlayOutEntityDestroy(entityId);
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY, entityDestroyPacket);

        protocolManager.sendServerPacket(player, packet, null, false);
    }

}
