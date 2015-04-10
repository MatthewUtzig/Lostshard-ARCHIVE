package com.lostshard.lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Utils.Output;

public class ScrollGUI extends GUI {

	SpellManager sm = SpellManager.getManager();
	
	public ScrollGUI(PseudoPlayer pPlayer) {
		super(36, "Scrolls", pPlayer);
		optionSelector();
	}

	@Override
	public void optionSelector() {
		List<Scroll> scrolls = new ArrayList<Scroll>();
		for(Scroll s : getPlayer().getScrolls()) {
			if(!scrolls.contains(s))
				scrolls.add(s);
		}
		for(Scroll s : scrolls) {
			int amount = Collections.frequency(scrolls, s);
			ItemStack item = new ItemStack(s.getReagentCost().get(0).getType(), amount);
			ItemMeta itemMeta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			
			if(getPlayer().getSpellbook().containSpell(s))
				itemMeta.setDisplayName(ChatColor.GREEN+s.getName());
			else
				itemMeta.setDisplayName(ChatColor.RED+s.getName());
			
			lore.add(ChatColor.GOLD+"Amount: "+amount);
			
			lore.add(ChatColor.BLUE+"Mana cost: "+s.getManaCost());
			lore.add("You can use the scroll by shift clicking it");
			lore.add("/scrolls use"+ChatColor.RED+"(scroll)");
			lore.add("/scrolls give"+ChatColor.RED+"(scroll)");
			itemMeta.setLore(lore);
			
			item.setItemMeta(itemMeta);
			addOption(item);
		}
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if(event.getCurrentItem().getItemMeta().hasDisplayName() && event.getAction().equals(InventoryAction.PICKUP_ALL)) {
			Player player = (Player)event.getWhoClicked();
			Scroll scroll = Scroll.getByString(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
			if(scroll == null)
				return;
			getPlayer().addSpell(scroll);
			Database.deleteScroll(scroll, getPlayer().getId());
			getPlayer().update();
			Output.positiveMessage(player, "You have transferred "+scroll.getName()+" to your spellbook.");
			forceClose();
		}else if(event.getCurrentItem().getItemMeta().hasDisplayName() && event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
			Scroll scroll = Scroll.getByString(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
			Player player = (Player) event.getWhoClicked();
			if(scroll == null || !getPlayer().getScrolls().contains(scroll))
				return;
			if(sm.useScroll(player, scroll)) {
				getPlayer().getScrolls().remove(scroll);
				Database.deleteScroll(scroll, getPlayer().getId());
			}
		}
	}

	@Override
	public void onClose(InventoryCloseEvent event) {
		
	}
}
