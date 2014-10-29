package com.lostshard.NPC;

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
	
	private NPCRegistry registry = CitizensAPI.createAnonymousNPCRegistry(null);

	public NPCRegistry getRegistry() {
		return registry;
	}

	public void setRegistry(NPCRegistry registry) {
		this.registry = registry;
	}
	
	@SuppressWarnings("deprecation")
	public NPC spawnNPC(String name, Location location, NPCType npctype) {
		NPC npc = this.registry.createNPC(EntityType.PLAYER, name);
		
		npc.setProtected(true);
		npc.spawn(location);
		
		if(npctype == NPCType.BANKER) {
			npc.getBukkitEntity().getEquipment().setHelmet(new ItemStack(Material.GOLD_HELMET));
			npc.getBukkitEntity().getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
			npc.getBukkitEntity().getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
			npc.getBukkitEntity().getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
			npc.getBukkitEntity().getEquipment().setItemInHand(new ItemStack(Material.GOLD_INGOT));
		}else if(npctype == NPCType.GUARD) {
			npc.getBukkitEntity().getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			npc.getBukkitEntity().getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			npc.getBukkitEntity().getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			npc.getBukkitEntity().getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			npc.getBukkitEntity().getEquipment().setItemInHand(new ItemStack(Material.IRON_SWORD));
		}else if(npctype == NPCType.VENDOR) {
			npc.getBukkitEntity().getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
			npc.getBukkitEntity().getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
			npc.getBukkitEntity().getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
			npc.getBukkitEntity().getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));
			npc.getBukkitEntity().getEquipment().setItemInHand(new ItemStack(Material.GOLD_INGOT));
		}
		
		return npc;
	}
	
	public NPC getNPC(Entity entity) {
		NPC npc = this.getRegistry().getNPC(entity);
		return npc;
	}
	
	public NPC renameNPC(Entity entity, String name) {
		NPC npc = this.registry.getNPC(entity);
		npc.setName(name);
		return npc;
	}
	
	public NPC moveNPC(Entity entity, Location location) {
		NPC npc = this.registry.getNPC(entity);
		npc.teleport(location, TeleportCause.COMMAND);
		return npc;
	}
	
	public NPC removeNPC(Entity entity, Location location) {
		NPC npc = this.registry.getNPC(entity);
		npc.despawn();
		npc.destroy();
		return npc;
	}
	
}
