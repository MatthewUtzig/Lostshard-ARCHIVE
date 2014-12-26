package com.lostshard.lostshard.NPC;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;

/**
 * @author Jacob Rosborg
 *
 */
public class NPCManager {

	public static NPCRegistry registry = CitizensAPI
			.getNPCRegistry();

	public static NPCRegistry getRegistry() {
		return registry;
	}
	
	public static void setRegistry(NPCRegistry registry) {
		NPCManager.registry = registry;
	}

	public static NPC spawnNPC(com.lostshard.lostshard.NPC.NPC npc) {
		NPC cNPC = NPCManager.registry.createNPC(EntityType.PLAYER, UUID.randomUUID(), npc.getId(),
				"[" + npc.getType().toString() + "] " + npc.getName());
		cNPC.spawn(npc.getLocation());
		cNPC.setProtected(true);
		cNPC.setFlyable(true);
		
		Equipment et = cNPC.getTrait(Equipment.class);
		
		if (npc.getType() == NPCType.BANKER) {
			et.set(EquipmentSlot.HELMET, new ItemStack(Material.GOLD_HELMET));
			et.set(EquipmentSlot.CHESTPLATE, new ItemStack(Material.GOLD_CHESTPLATE));
			et.set(EquipmentSlot.LEGGINGS, new ItemStack(Material.GOLD_LEGGINGS));
			et.set(EquipmentSlot.BOOTS, new ItemStack(Material.GOLD_BOOTS));
			et.set(EquipmentSlot.HAND, new ItemStack(Material.GOLD_INGOT));
		} else if (npc.getType() == NPCType.GUARD) {
			et.set(EquipmentSlot.HELMET, new ItemStack(Material.IRON_HELMET));
			et.set(EquipmentSlot.CHESTPLATE, new ItemStack(Material.IRON_CHESTPLATE));
			et.set(EquipmentSlot.LEGGINGS, new ItemStack(Material.IRON_LEGGINGS));
			et.set(EquipmentSlot.BOOTS, new ItemStack(Material.IRON_BOOTS));
			et.set(EquipmentSlot.HAND, new ItemStack(Material.IRON_SWORD));
		} else if (npc.getType() == NPCType.VENDOR) {
			et.set(EquipmentSlot.HELMET, new ItemStack(Material.LEATHER_HELMET));
			et.set(EquipmentSlot.CHESTPLATE, new ItemStack(Material.LEATHER_CHESTPLATE));
			et.set(EquipmentSlot.LEGGINGS, new ItemStack(Material.LEATHER_LEGGINGS));
			et.set(EquipmentSlot.BOOTS, new ItemStack(Material.LEATHER_BOOTS));
			et.set(EquipmentSlot.HAND, new ItemStack(Material.GOLD_INGOT));
		}

		return cNPC;
	}

	public static NPC getNPC(com.lostshard.lostshard.NPC.NPC npc) {
		return getRegistry().getById(npc.getId());
	}

	public static NPC getNPC(Entity entity) {
		NPC npc = NPCManager.getRegistry().getNPC(entity);
		return npc;
	}

	public static NPC renameNPC(Entity entity, String name) {
		NPC npc = NPCManager.registry.getNPC(entity);
		npc.setName(name);
		return npc;
	}

	public static NPC moveNPC(Entity entity, Location location) {
		NPC npc = NPCManager.registry.getNPC(entity);
		npc.teleport(location, TeleportCause.COMMAND);
		return npc;
	}

	public static NPC removeNPC(Entity entity, Location location) {
		NPC npc = NPCManager.registry.getNPC(entity);
		npc.despawn();
		npc.destroy();
		return npc;
	}

	public static NPC getNPC(int id) {
		NPC npc = NPCManager.getRegistry().getById(id);
		return npc;
	}

	public static NPC renameNPC(int id, String name) {
		NPC npc = NPCManager.registry.getById(id);
		npc.setName(name);
		return npc;
	}

	public static NPC moveNPC(int id, Location location) {
		NPC npc = NPCManager.registry.getById(id);
		npc.teleport(location, TeleportCause.COMMAND);
		return npc;
	}

	public static NPC removeNPC(int id, Location location) {
		NPC npc = NPCManager.registry.getById(id);
		npc.despawn();
		npc.destroy();
		return npc;
	}

}
