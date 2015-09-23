package com.lostshard.Lostshard.Spells;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Objects.InventoryGUI.GUI;
import com.lostshard.Lostshard.Objects.InventoryGUI.RunebookGUI;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;

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
		this.scroll.getReagentCost().add(item);
	}

	public abstract void doAction(Player player);

	public void finish(Player player) {
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		pPlayer.setPromptedSpell(null);
		pPlayer.getTimer().delayedSpell = null;
	}

	public int getCastingDelay() {
		return this.scroll.getCastingDelay();
	}

	public int getCooldown() {
		return this.scroll.getCooldown();
	}

	public int getManaCost() {
		return this.scroll.getManaCost();
	}

	public int getMinMagery() {
		return this.scroll.getMinMagery();
	}

	public String getName() {
		return this.scroll.getName();
	}

	public int getPage() {
		return this.scroll.getPage();
	}

	public String getPrompt() {
		return this.prompt;
	}

	public List<ItemStack> getReagentCost() {
		return this.scroll.getReagentCost();
	}

	public String getResponse() {
		return this.response;
	}

	public Scroll getScroll() {
		return this.scroll;
	}

	public String getSpellWords() {
		return this.scroll.getSpellWords();
	}

	public int getTick() {
		return this.tick;
	}

	public abstract void preAction(Player player);

	public void runebook(Player player) {
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		if (pPlayer.isAllowGui()) {
			final GUI gui = new RunebookGUI(pPlayer);
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
		this.tick++;
		if (this.tick >= this.getCastingDelay()) {
			this.doAction(player);
			this.finish(player);
		}
	}

	public abstract boolean verifyCastable(Player player);
}
