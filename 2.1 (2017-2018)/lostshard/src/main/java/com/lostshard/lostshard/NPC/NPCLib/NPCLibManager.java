package com.lostshard.lostshard.NPC.NPCLib;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.NPC.NPCType;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;

public class NPCLibManager {

	private final static NPCLibManager manager = new NPCLibManager();

	public static NPCLibManager getManager() {
		return manager;
	}

	private NPCRegistry registry;

	private boolean isEnable = true;

	private NPCLibManager() {
		if (!Bukkit.getPluginManager().isPluginEnabled("Citizens")) {
			this.isEnable = false;
			this.registry = null;
		} else {
			this.registry = CitizensAPI.createNamedNPCRegistry("Lostsahrd", null);
		}
	}

	public boolean despawnNPC(com.lostshard.lostshard.NPC.NPC lnpc) {
		if (!this.isEnable)
			return false;
		final NPC npc = this.registry.getById(lnpc.getId());
		npc.despawn();
		return true;
	}

	private int generateID() {
		int id = 0;
		for (final NPC npc : this.registry.sorted()) {
			if (npc.getId() == id)
				id++;
			else
				break;
		}
		return id;
	}

	public NPC getNPC(int id) {
		return this.registry.getById(id);
	}

	public int getNPCID(Entity e) {
		return this.registry.getNPC(e).getId();
	}

	public int getNPCID(UUID uuid) {
		final NPC npc = this.registry.getByUniqueId(uuid);
		if (npc == null)
			return -1;
		return npc.getId();
	}

	public NPCRegistry getRegistry() {
		return this.registry;
	}

	public boolean isEnable() {
		return this.isEnable;
	}

	public boolean moveNPC(com.lostshard.lostshard.NPC.NPC lnpc) {
		if (!this.isEnable)
			return false;
		final NPC npc = this.registry.getById(lnpc.getId());
		npc.teleport(lnpc.getLocation(), TeleportCause.PLUGIN);
		return true;
	}

	public void setArmor(NPC npc, NPCType nt) {
		final Equipment eq = npc.getTrait(Equipment.class);
		if (nt.equals(NPCType.BANKER)) {
			eq.set(EquipmentSlot.HELMET, new ItemStack(Material.GOLD_HELMET));
			eq.set(EquipmentSlot.CHESTPLATE, new ItemStack(Material.GOLD_CHESTPLATE));
			eq.set(EquipmentSlot.LEGGINGS, new ItemStack(Material.GOLD_LEGGINGS));
			eq.set(EquipmentSlot.BOOTS, new ItemStack(Material.GOLD_BOOTS));
			eq.set(EquipmentSlot.HAND, new ItemStack(Material.GOLD_INGOT));
		} else if (nt.equals(NPCType.GUARD)) {
			eq.set(EquipmentSlot.HELMET, new ItemStack(Material.IRON_HELMET));
			eq.set(EquipmentSlot.CHESTPLATE, new ItemStack(Material.IRON_CHESTPLATE));
			eq.set(EquipmentSlot.LEGGINGS, new ItemStack(Material.IRON_LEGGINGS));
			eq.set(EquipmentSlot.BOOTS, new ItemStack(Material.IRON_BOOTS));
			eq.set(EquipmentSlot.HAND, new ItemStack(Material.IRON_SWORD));
		} else if (nt.equals(NPCType.VENDOR)) {
			eq.set(EquipmentSlot.HELMET, new ItemStack(Material.LEATHER_HELMET));
			eq.set(EquipmentSlot.CHESTPLATE, new ItemStack(Material.LEATHER_CHESTPLATE));
			eq.set(EquipmentSlot.LEGGINGS, new ItemStack(Material.LEATHER_LEGGINGS));
			eq.set(EquipmentSlot.BOOTS, new ItemStack(Material.LEATHER_BOOTS));
		}
	}

	public int spawnNPC(com.lostshard.lostshard.NPC.NPC lnpc) {
		if (!this.isEnable)
			return -1;
		final int id = this.generateID();
		lnpc.setId(id);
		final NPC npc = this.registry.createNPC(EntityType.PLAYER, lnpc.getUUID(), id, lnpc.getDisplayName());
		npc.setProtected(true);
		lnpc.getLocation().getWorld().loadChunk(lnpc.getLocation().getChunk());
		npc.spawn(lnpc.getLocation());
		this.setArmor(npc, lnpc.getType());
		lnpc.getLocation().getWorld().unloadChunk(lnpc.getLocation().getChunk());
		return npc.getId();
	}

	public boolean teleportNPC(int id, Location location, TeleportCause reason) {
		if (!this.isEnable)
			return false;
		final NPC npc = this.registry.getById(id);
		npc.teleport(location, reason);
		return true;
	}
}
