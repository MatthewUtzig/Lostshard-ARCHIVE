package com.lostshard.lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Skills.Skill;
import com.lostshard.lostshard.Utils.Utils;

public class SkillsGUI extends InventoryGUI {

	public SkillsGUI(PseudoPlayer pPlayer) {
		super(18, "Skills", GUIType.SKILLS, pPlayer);
	}

	@Override
	public void optionSelector() {
		for(Skill s : getPlayer().getCurrentBuild().getSkills()) {
			ItemStack skillItem = new ItemStack(s.getMat());
			ItemMeta skillMeta = skillItem.getItemMeta();
			List<String> skillLore = new ArrayList<String>();
			
			if(s.isLocked())
				skillMeta.setDisplayName(ChatColor.RED+s.getName()+" "+Utils.scaledIntToString(s.getLvl()));
			else
				skillMeta.setDisplayName(ChatColor.GREEN+s.getName()+" "+Utils.scaledIntToString(s.getLvl()));
			skillLore.add(Utils.scaledIntToString(getPlayer().getCurrentBuild().getTotalSkillVal())+"/"+Utils.scaledIntToString(4000)+" skill points");
			skillLore.add("Locked: "+(s.isLocked() ? ChatColor.RED+"yes" : ChatColor.GREEN+"no"));
			skillLore.add(ChatColor.GOLD+"Commands");
			skillLore.add("/skills resetall "+ChatColor.RED+"(skill)");
			skillLore.add("/skills lock");
			skillLore.add("/skills unlock");
			skillLore.add("/skills reduce "+ChatColor.RED+"(amount)");
			skillLore.add("/skills increase "+ChatColor.RED+"(amount)");
			
			skillMeta.setLore(skillLore);
			skillItem.setItemMeta(skillMeta);
			addOption(skillItem);
		}
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		
	}

	@Override
	public void onClose(InventoryCloseEvent event) {
		
	}
	
}
