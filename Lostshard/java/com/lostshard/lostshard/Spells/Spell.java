package com.lostshard.lostshard.Spells;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;

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
	
	public Scroll getScroll() {
		return scroll;
	}

	public void setScroll(Scroll scroll) {
		this.scroll = scroll;
	}
	
	public Spell getNew() {
		try {
			return this.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public abstract boolean verifyCastable(Player player);
	
	public abstract void preAction(Player player);
	
	public abstract void doAction(Player player);
	
	public String getName() {
		return scroll.getName();
	}

	public String getSpellWords() {
		return scroll.getSpellWords();
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

	public List<ItemStack> getReagentCost() {
		return scroll.getReagentCost();
	}
	
	public void addReagentCost(ItemStack item) {
		scroll.getReagentCost().add(item);
	}
	
	public int getPage() {
		return scroll.getPage();
	}
	

	public int getCastingDelay() {
		return scroll.getCastingDelay();
	}
	public int getTick() {
		return tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}


	public void tick(Player player) {
		tick++;
		if(scroll.getCastingDelay() > 0)
			scroll.setCastingDelay(scroll.getCastingDelay()-1);
		else {
			doAction(player);
			finish(player);
		}
	}
	
	public void finish(Player player) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
		pPlayer.setPromptedSpell(null);
		pPlayer.getTimer().delayedSpell = null;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
}
