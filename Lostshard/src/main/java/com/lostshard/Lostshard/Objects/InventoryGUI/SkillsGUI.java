package com.lostshard.Lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Skills.Skill;
import com.lostshard.Lostshard.Utils.Utils;

public class SkillsGUI extends GUI {
	public SkillsGUI(PseudoPlayer pPlayer) {
		super("Skills", pPlayer, new GUIItem[0]);
		final GUIItem[] items = new GUIItem[pPlayer.getCurrentBuild().getSkills().size()];
		for (int i = 0; i < pPlayer.getCurrentBuild().getSkills().size(); i++) {
			final Skill s = pPlayer.getCurrentBuild().getSkills().get(i);
			final ItemStack skillItem = new ItemStack(s.getMat());
			final ItemMeta skillMeta = skillItem.getItemMeta();
			final List<String> skillLore = new ArrayList<String>();
			if (s.isLocked()) {
				skillMeta.setDisplayName(ChatColor.RED + s.getName() + " " + Utils.scaledIntToString(s.getLvl()));
			} else {
				skillMeta.setDisplayName(ChatColor.GREEN + s.getName() + " " + Utils.scaledIntToString(s.getLvl()));
			}
			skillLore.add(Utils.scaledIntToString(pPlayer.getCurrentBuild().getTotalSkillVal()) + "/"
					+ Utils.scaledIntToString(pPlayer.getMaxSkillValTotal()) + " total skill points");
			skillLore.add(s.howToGain());
			skillLore.add("Locked: " + (s.isLocked() ? ChatColor.RED + "yes"
					: new StringBuilder().append(ChatColor.GREEN).append("no").toString()));
			skillLore.add("You can lock the skill by shift clicking it");
			skillLore.add(ChatColor.GOLD + "Commands");
			skillLore.add("/skills resetall " + ChatColor.RED + "(skill)");
			skillLore.add("/skills lock " + ChatColor.RED + "(skill)");
			skillLore.add("/skills unlock " + ChatColor.RED + "(skill)");
			skillLore.add("/skills reduce " + ChatColor.RED + "(amount)");
			skillLore.add("/skills increase " + ChatColor.RED + "(amount)");
			skillMeta.setLore(skillLore);
			skillItem.setItemMeta(skillMeta);
			items[i] = new GUIItem(skillItem, (player, pPlayer1, item, click, inv, slot) -> {
				if (click.equals(ClickType.SHIFT_LEFT)) {
					final Skill skill = s;
					if (skill.isLocked()) {
						skill.setLocked(false);
					} else {
						skill.setLocked(true);
					}
					final ItemMeta itemMeta = item.getItemMeta();
					if (skill.isLocked()) {
						itemMeta.setDisplayName(
								ChatColor.RED + skill.getName() + " " + Utils.scaledIntToString(skill.getLvl()));
					} else {
						itemMeta.setDisplayName(
								ChatColor.GREEN + skill.getName() + " " + Utils.scaledIntToString(skill.getLvl()));
					}
					final List<String> lore = itemMeta.getLore();
					lore.set(2, "Locked: " + (skill.isLocked() ? ChatColor.RED + "yes"
							: new StringBuilder().append(ChatColor.GREEN).append("no").toString()));
					itemMeta.setLore(lore);
					itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
					itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
					itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DESTROYS });
					itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
					itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_PLACED_ON });
					itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
					item.setItemMeta(itemMeta);
				}
			});
		}
		this.setItems(items);
	}
}
