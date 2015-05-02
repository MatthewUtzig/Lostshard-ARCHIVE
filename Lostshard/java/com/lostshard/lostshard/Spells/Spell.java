package com.lostshard.lostshard.Spells;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.InventoryGUI.GUI;
import com.lostshard.lostshard.Objects.InventoryGUI.RunebookGUI;

public abstract class Spell {
	
	protected PlayerManager pm = PlayerManager.getManager();
	protected PlotManager ptm = PlotManager.getManager();
	
	private Scroll scroll;
	private String response;
	private String prompt;

	private int tick = 0;
	
	public Spell(Scroll scroll) {
		this.scroll = scroll;
	}
	
	public void addReagentCost(ItemStack item) {
		scroll.getReagentCost().add(item);
	}
	
	public abstract void doAction(Player player);

	public void finish(Player player) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
		pPlayer.setPromptedSpell(null);
		pPlayer.getTimer().delayedSpell = null;
	}
	
	public int getCastingDelay() {
		return scroll.getCastingDelay();
	}
	
	public int getCooldown() {
		return scroll.getCooldown();
	}
	
	public int getManaCost() {
		return scroll.getManaCost();
	}
	
	public int getMinMagery() {
		return scroll.getMinMagery();
	}

	public String getName() {
		return scroll.getName();
	}

	public int getPage() {
		return scroll.getPage();
	}

	public String getPrompt() {
		return prompt;
	}

	public List<ItemStack> getReagentCost() {
		return scroll.getReagentCost();
	}

	public String getResponse() {
		return response;
	}
	
	public Scroll getScroll() {
		return scroll;
	}
	
	public String getSpellWords() {
		return scroll.getSpellWords();
	}
	

	public int getTick() {
		return tick;
	}
	public abstract void preAction(Player player);

	public void runebook(Player player) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(pPlayer.isAllowGui()) {
			GUI gui = new RunebookGUI(pPlayer);
			gui.openInventory(player);
			return;
		}
	}


	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	
	public void setResponse(String response) {
		this.response = response;
	}

	public void setScroll(Scroll scroll) {
		this.scroll = scroll;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}

	public void tick(Player player) {
		tick++;
		if(tick >= getCastingDelay()){
			doAction(player);
			finish(player);
		}
	}

	public abstract boolean verifyCastable(Player player);
}
