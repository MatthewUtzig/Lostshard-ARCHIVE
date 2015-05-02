package com.lostshard.lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Skills.Skill;
import com.lostshard.lostshard.Utils.Utils;

public class SkillsGUI extends GUI {

	public SkillsGUI(PseudoPlayer pPlayer) {
		super(18, "Skills", pPlayer);
		optionSelector();
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if(event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) && event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta().hasDisplayName()) {
			String[] skillName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).split(" ");
			if(skillName.length < 1)
				return;
			Skill skill = getPlayer().getSkillByName(skillName[0]);
			if(skill == null)
				return;
			if(skill.isLocked())
				skill.setLocked(false);
			else
				skill.setLocked(true);
			ItemMeta item = event.getCurrentItem().getItemMeta();
			if(skill.isLocked())
				item.setDisplayName(ChatColor.RED+skill.getName()+" "+Utils.scaledIntToString(skill.getLvl()));
			else
				item.setDisplayName(ChatColor.GREEN+skill.getName()+" "+Utils.scaledIntToString(skill.getLvl()));
			List<String> lore = item.getLore();
			lore.set(1,"Locked: "+(skill.isLocked() ? ChatColor.RED+"yes" : ChatColor.GREEN+"no"));
			item.setLore(lore);
			event.getCurrentItem().setItemMeta(item);
		}
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
			skillLore.add(Utils.scaledIntToString(getPlayer().getCurrentBuild().getTotalSkillVal())+"/"+Utils.scaledIntToString(getPlayer().getMaxSkillValTotal())+" skill points");
			skillLore.add(s.howToGain());
			skillLore.add("Locked: "+(s.isLocked() ? ChatColor.RED+"yes" : ChatColor.GREEN+"no"));
			skillLore.add("You can lock the skill by shift clicking it");
			skillLore.add(ChatColor.GOLD+"Commands");
			skillLore.add("/skills resetall "+ChatColor.RED+"(skill)");
			skillLore.add("/skills lock "+ChatColor.RED+"(skill)");
			skillLore.add("/skills unlock "+ChatColor.RED+"(skill)");
			skillLore.add("/skills reduce "+ChatColor.RED+"(amount)");
			skillLore.add("/skills increase "+ChatColor.RED+"(amount)");
			skillMeta.setLore(skillLore);
			skillItem.setItemMeta(skillMeta);
			addOption(skillItem);
		}
	}
}
