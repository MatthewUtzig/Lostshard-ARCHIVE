package com.lostshard.NPC;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class NPCManager {
	
	private static NPCRegistry registry = CitizensAPI.createAnonymousNPCRegistry(null);

	public static NPCRegistry getRegistry() {
		return registry;
	}

	public static void setRegistry(NPCRegistry registry) {
		NPCManager.registry = registry;
	}
	
	@SuppressWarnings("deprecation")
	public static NPC spawnNPC(com.lostshard.NPC.NPC npc) {
		NPC cNPC= NPCManager.registry.createNPC(EntityType.PLAYER, Bukkit.getOfflinePlayer(npc.getName()).getUniqueId(), npc.getId(), npc.getName());
		cNPC.setProtected(true);
		cNPC.spawn(npc.getLocation());
		
		if(npc.getType() == NPCType.BANKER) {
			cNPC.getBukkitEntity().getEquipment().setHelmet(new ItemStack(Material.GOLD_HELMET));
			cNPC.getBukkitEntity().getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
			cNPC.getBukkitEntity().getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
			cNPC.getBukkitEntity().getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
			cNPC.getBukkitEntity().getEquipment().setItemInHand(new ItemStack(Material.GOLD_INGOT));
		}else if(npc.getType() == NPCType.GUARD) {
			cNPC.getBukkitEntity().getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			cNPC.getBukkitEntity().getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			cNPC.getBukkitEntity().getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			cNPC.getBukkitEntity().getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			cNPC.getBukkitEntity().getEquipment().setItemInHand(new ItemStack(Material.IRON_SWORD));
		}else if(npc.getType() == NPCType.VENDOR) {
			cNPC.getBukkitEntity().getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
			cNPC.getBukkitEntity().getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
			cNPC.getBukkitEntity().getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
			cNPC.getBukkitEntity().getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));
			cNPC.getBukkitEntity().getEquipment().setItemInHand(new ItemStack(Material.GOLD_INGOT));
		}
		
		return cNPC;
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
