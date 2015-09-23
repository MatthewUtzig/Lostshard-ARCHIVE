package com.lostshard.Lostshard.Objects.InventoryGUI;

import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Skills.Skill;
import com.lostshard.Lostshard.Utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SkillsGUI extends GUI {
	public SkillsGUI(PseudoPlayer pPlayer) {
		super("Skills", pPlayer, new GUIItem[0]);
		GUIItem[] items = new GUIItem[pPlayer.getCurrentBuild().getSkills().size()];
		for (int i = 0; i < pPlayer.getCurrentBuild().getSkills().size(); i++) {
			final Skill s = (Skill) pPlayer.getCurrentBuild().getSkills().get(i);
			ItemStack skillItem = new ItemStack(s.getMat());
			ItemMeta skillMeta = skillItem.getItemMeta();
			List<String> skillLore = new ArrayList<String>();
			if (s.isLocked()) {
				skillMeta.setDisplayName(ChatColor.RED + s.getName() + " " + Utils.scaledIntToString(s.getLvl()));
			} else {
				skillMeta.setDisplayName(ChatColor.GREEN + s.getName() + " " + Utils.scaledIntToString(s.getLvl()));
			}
			skillLore.add(Utils.scaledIntToString(pPlayer.getCurrentBuild().getTotalSkillVal()) + "/"
					+ Utils.scaledIntToString(pPlayer.getMaxSkillValTotal()) + " skill points");
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
			items[i] = new GUIItem(skillItem, new GUIClick() {
				public void click(Player player, PseudoPlayer pPlayer, ItemStack item, ClickType click, Inventory inv,
						int slot) {
					if (click.equals(ClickType.SHIFT_LEFT)) {
						Skill skill = s;
						if (skill.isLocked()) {
							skill.setLocked(false);
						} else {
							skill.setLocked(true);
						}
						ItemMeta itemMeta = item.getItemMeta();
						if (skill.isLocked()) {
							itemMeta.setDisplayName(
									ChatColor.RED + skill.getName() + " " + Utils.scaledIntToString(skill.getLvl()));
						} else {
							itemMeta.setDisplayName(
									ChatColor.GREEN + skill.getName() + " " + Utils.scaledIntToString(skill.getLvl()));
						}
						List<String> lore = itemMeta.getLore();
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
				}
			});
		}
		setItems(items);
	}
}
