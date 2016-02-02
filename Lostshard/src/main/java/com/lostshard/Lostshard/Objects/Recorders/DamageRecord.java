package com.lostshard.Lostshard.Objects.Recorders;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.google.gson.Gson;

@Entity
public class DamageRecord extends Record {
	
	private double base;
	private double armor;
	private double magic;
	private double resistance;
	private double finaldmg;
	
	@Enumerated(EnumType.STRING)
	private Material weaponType;
	private String enchants;

	@Enumerated(EnumType.STRING)
	private Material helmet;
	@Enumerated(EnumType.STRING)
	private Material chestplate;
	@Enumerated(EnumType.STRING)
	private Material leggings;
	@Enumerated(EnumType.STRING)
	private Material boots;
	
	private String helmetEnchants;
	private String chestplateEnchants;
	private String leggingsEnchants;
	private String bootsEnchants;
	
	public DamageRecord() {
		
	}

	/**
	 * @return the base
	 */
	public double getBase() {
		return this.base;
	}
	
	

	/**
	 * @param base
	 * @param armor
	 * @param magic
	 * @param resistance
	 * @param finaldmg
	 * @param weaponType
	 */
	public DamageRecord(double base, double armor, double magic, double resistance, double finaldmg,
			ItemStack weapon, PlayerInventory inv) {
		super();
		this.base = base;
		this.armor = armor;
		this.magic = magic;
		this.resistance = resistance;
		this.finaldmg = finaldmg;
		
		Gson gson = new Gson();
		if(weapon != null) {
			this.weaponType = weapon.getType();
			try {
				if(weapon.hasItemMeta() && weapon.getItemMeta().hasEnchants())
					this.enchants = gson.toJson(weapon.getItemMeta().getEnchants());
			} catch(Exception e) {
				
			}
		}
		
		ItemStack helmet = inv.getHelmet();
		if(helmet != null) {
			this.helmet = helmet.getType();
			try {
				if(helmet.hasItemMeta() && helmet.getItemMeta().hasEnchants())
					this.helmetEnchants = gson.toJson(helmet.getItemMeta().getEnchants());
			} catch(Exception e) {
				
			}
		}
		
		ItemStack chestplate = inv.getChestplate();
		if(chestplate != null) {
			this.chestplate = chestplate.getType();
			try {
				if(chestplate.hasItemMeta() && chestplate.getItemMeta().hasEnchants())
					this.helmetEnchants = gson.toJson(chestplate.getItemMeta().getEnchants());
			} catch(Exception e) {
				
			}
		}
		
		ItemStack leggings = inv.getLeggings();
		if(leggings != null) {
			this.leggings = leggings.getType();
			try {
				if(leggings.hasItemMeta() && leggings.getItemMeta().hasEnchants())
					this.helmetEnchants = gson.toJson(leggings.getItemMeta().getEnchants());
			} catch(Exception e) {
				
			}
		}
		
		ItemStack boots = inv.getBoots();
		if(boots != null) {
			this.boots = boots.getType();
			try {
				if(boots.hasItemMeta() && boots.getItemMeta().hasEnchants())
					this.bootsEnchants = gson.toJson(boots.getItemMeta().getEnchants());
			} catch(Exception e) {
				
			}
		}
	}

	/**
	 * @param id
	 * @param date
	 * @param base
	 * @param armor
	 * @param magic
	 * @param resistance
	 * @param finaldmg
	 * @param weaponType
	 * @param enchants
	 * @param helmet
	 * @param chestplate
	 * @param leggings
	 * @param boots
	 * @param helmetEnchants
	 * @param chestplateEnchants
	 * @param leggingsEnchants
	 * @param bootsEnchants
	 */
	public DamageRecord(double base, double armor, double magic, double resistance, double finaldmg,
			Material weaponType, String enchants, Material helmet, Material chestplate, Material leggings,
			Material boots, String helmetEnchants, String chestplateEnchants, String leggingsEnchants,
			String bootsEnchants) {
		super();
		this.base = base;
		this.armor = armor;
		this.magic = magic;
		this.resistance = resistance;
		this.finaldmg = finaldmg;
		this.weaponType = weaponType;
		this.enchants = enchants;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
		this.helmetEnchants = helmetEnchants;
		this.chestplateEnchants = chestplateEnchants;
		this.leggingsEnchants = leggingsEnchants;
		this.bootsEnchants = bootsEnchants;
	}

	/**
	 * @param base the base to set
	 */
	public void setBase(double base) {
		this.base = base;
	}

	/**
	 * @return the armor
	 */
	public double getArmor() {
		return this.armor;
	}

	/**
	 * @param armor the armor to set
	 */
	public void setArmor(double armor) {
		this.armor = armor;
	}

	/**
	 * @return the magic
	 */
	public double getMagic() {
		return this.magic;
	}

	/**
	 * @param magic the magic to set
	 */
	public void setMagic(double magic) {
		this.magic = magic;
	}

	/**
	 * @return the resistance
	 */
	public double getResistance() {
		return this.resistance;
	}

	/**
	 * @param resistance the resistance to set
	 */
	public void setResistance(double resistance) {
		this.resistance = resistance;
	}

	/**
	 * @return the finaldmg
	 */
	public double getFinaldmg() {
		return this.finaldmg;
	}

	/**
	 * @param finaldmg the finaldmg to set
	 */
	public void setFinaldmg(double finaldmg) {
		this.finaldmg = finaldmg;
	}

	/**
	 * @return the weaponType
	 */
	public Material getWeaponType() {
		return this.weaponType;
	}

	/**
	 * @param weaponType the weaponType to set
	 */
	public void setWeaponType(Material weaponType) {
		this.weaponType = weaponType;
	}

	/**
	 * @return the enchants
	 */
	public String getEnchants() {
		return this.enchants;
	}

	/**
	 * @param enchants the enchants to set
	 */
	public void setEnchants(String enchants) {
		this.enchants = enchants;
	}

	/**
	 * @return the helmet
	 */
	public Material getHelmet() {
		return this.helmet;
	}

	/**
	 * @param helmet the helmet to set
	 */
	public void setHelmet(Material helmet) {
		this.helmet = helmet;
	}

	/**
	 * @return the chestplate
	 */
	public Material getChestplate() {
		return this.chestplate;
	}

	/**
	 * @param chestplate the chestplate to set
	 */
	public void setChestplate(Material chestplate) {
		this.chestplate = chestplate;
	}

	/**
	 * @return the leggings
	 */
	public Material getLeggings() {
		return this.leggings;
	}

	/**
	 * @param leggings the leggings to set
	 */
	public void setLeggings(Material leggings) {
		this.leggings = leggings;
	}

	/**
	 * @return the boots
	 */
	public Material getBoots() {
		return this.boots;
	}

	/**
	 * @param boots the boots to set
	 */
	public void setBoots(Material boots) {
		this.boots = boots;
	}

	/**
	 * @return the helmetEnchants
	 */
	public String getHelmetEnchants() {
		return this.helmetEnchants;
	}

	/**
	 * @param helmetEnchants the helmetEnchants to set
	 */
	public void setHelmetEnchants(String helmetEnchants) {
		this.helmetEnchants = helmetEnchants;
	}

	/**
	 * @return the chestplateEnchants
	 */
	public String getChestplateEnchants() {
		return this.chestplateEnchants;
	}

	/**
	 * @param chestplateEnchants the chestplateEnchants to set
	 */
	public void setChestplateEnchants(String chestplateEnchants) {
		this.chestplateEnchants = chestplateEnchants;
	}

	/**
	 * @return the leggingsEnchants
	 */
	public String getLeggingsEnchants() {
		return this.leggingsEnchants;
	}

	/**
	 * @param leggingsEnchants the leggingsEnchants to set
	 */
	public void setLeggingsEnchants(String leggingsEnchants) {
		this.leggingsEnchants = leggingsEnchants;
	}

	/**
	 * @return the bootsEnchants
	 */
	public String getBootsEnchants() {
		return this.bootsEnchants;
	}

	/**
	 * @param bootsEnchants the bootsEnchants to set
	 */
	public void setBootsEnchants(String bootsEnchants) {
		this.bootsEnchants = bootsEnchants;
	}
}
