package com.lostshard.lostshard.Spells;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public class Spell {
	
	public enum SpellType {
		MARK(new SPL_Mark()),
		TELEPORT(new SPL_Teleport()), 
		RECALL(new SPL_Recall()), 
		PERMANENTGATETRAVEL(new SPL_PermanentGateTravel()),
		GATETRAVEL(new SPL_GateTravel()),
		FLARE(new SPL_Flare()),
		SLOWFIELD(new SPL_Slowfield()),
		GRASS(new SPL_Grass());
		
		private Spell spell;
		
		private SpellType(Spell spell) {
			this.spell = spell;
		}
		
		public Spell getSpell() {
			return spell.getNew();
		}
		
		public static SpellType getByString(String string) {
			for(SpellType st : values())
				if(st.name().equalsIgnoreCase(string.trim().replace(" ", "")))
					return st;
			return null;
		}
	}
	
	PlayerManager pm = PlayerManager.getManager();
	PlotManager ptm = PlotManager.getManager();

	private String name;
	private String spellWords;
	private int cooldown;
	private int castingDelay;
	private int manaCost;
	private int minMagery;
	private int page;
	private List<ItemStack> reagentCost = new ArrayList<ItemStack>();
	private String response;
	private boolean wandable;
	private String prompt;

	private int tick = 0;
	
	public SpellType getType() {
		return SpellType.getByString(getName());
	}
	
	public Spell getNew() {
		try {
			return this.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpellWords() {
		return spellWords;
	}

	public void setSpellWords(String spellWords) {
		this.spellWords = spellWords;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public int getManaCost() {
		return manaCost;
	}

	public void setManaCost(int manaCost) {
		this.manaCost = manaCost;
	}

	public int getMinMagery() {
		return minMagery;
	}

	public void setMinMagery(int minMagery) {
		this.minMagery = minMagery;
	}

	public List<ItemStack> getReagentCost() {
		return reagentCost;
	}

	public void setReagentCost(List<ItemStack> reagentCost) {
		this.reagentCost = reagentCost;
	}
	
	public void addReagentCost(ItemStack item) {
		reagentCost.add(item);
	}
	
	public int getTick() {
		return tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}

	public int getCastingDelay() {
		return castingDelay;
	}

	public void setCastingDelay(int castingDelay) {
		this.castingDelay = castingDelay;
	}

	public void tick(Player player) {
		if(tick > 0)
			tick++;
		if(castingDelay > 0)
			castingDelay--;
		else {
			doAction(player);
			finish(player);
		}
	}
	
	private void finish(Player player) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
		pPlayer.setPromptedSpell(null);
		pPlayer.getTimer().delayedSpell = null;
	}

	public boolean verifyCastable(Player player) {
		return true;
	}
	
	public void preAction(Player player) {
		
	}
	
	public void doAction(Player player) {
		
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public boolean isWandable() {
		return wandable;
	}

	public void setWandable(boolean wandable) {
		this.wandable = wandable;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
}
