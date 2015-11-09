package com.lostshard.Lostshard.Objects.CustomObjects;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

@Embeddable
public class SerializableItemMeta {
	
	private String displayName;
	@ElementCollection(targetClass=String.class)
	private List<String> lore;
	private int repairCost;
	
	public SerializableItemMeta(ItemMeta itemMeta) {
		super();
		if(itemMeta.hasDisplayName())
			this.displayName = itemMeta.getDisplayName();
		if(itemMeta.hasLore())
			this.lore = itemMeta.getLore();
		if(itemMeta instanceof Repairable)
			this.repairCost = ((Repairable) itemMeta).getRepairCost();
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public List<String> getLore() {
		return lore;
	}
	
	public void setLore(List<String> lore) {
		this.lore = lore;
	}
	
	public int getRepairCost() {
		return repairCost;
	}
	
	public void setRepairCost(int repairCost) {
		this.repairCost = repairCost;
	}
}
