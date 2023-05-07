package com.spleefleague.zone.fragments;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.spleefleague.core.Core;
import com.spleefleague.core.logger.CoreLogger;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3D;
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
        PacketPlayOutSpawnEntity packetPlayOutSpawnEntity = new PacketPlayOutSpawnEntity(entityId, unused, x, y, z, 0, 0, EntityTypes.d, 0, new Vec3D(0, 0, 0), 0);
        PacketContainer spawnPacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY, packetPlayOutSpawnEntity);

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
