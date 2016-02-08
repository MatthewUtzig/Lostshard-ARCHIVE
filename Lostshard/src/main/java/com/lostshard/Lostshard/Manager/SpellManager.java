package com.lostshard.Lostshard.Manager;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.Lostshard.Handlers.ChatHandler;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Spells.Spell;
import com.lostshard.Lostshard.Spells.Spells.SPL_ClearSky;
import com.lostshard.Lostshard.Spells.Spells.SPL_Day;
import com.lostshard.Lostshard.Utils.ItemUtils;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.Utils;

public class SpellManager {

	static SpellManager manager = new SpellManager();

	public static SpellManager getManager() {
		return manager;
	}

	PlayerManager pm = PlayerManager.getManager();

	PlotManager ptm = PlotManager.getManager();

	private final double minChanceToCast = .2;

	private final double minPercentChanceSkillGain = .2;

	public void castDelayed(Player player, PseudoPlayer pPlayer, Spell spell) {
		if (spell.getPrompt() == null) {
			spell.preAction(player);
			pPlayer.getTimer().delayedSpell = spell;
		} else
			pPlayer.setPromptedSpell(spell);
	}

	public void castInstant(Player player, PseudoPlayer pPlayer, Spell spell) {
		if (spell.getPrompt() == null) {
			spell.preAction(player);
			pPlayer.getTimer().cantCastTicks = spell.getCooldown();
			spell.doAction(player);
		} else
			pPlayer.setPromptedSpell(spell);
	}

	public boolean castSpell(Player player, Spell spell) {
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);

		if (pPlayer.getTimer().cantCastTicks > 0) {
			Output.simpleError(player, "You can't cast another spell again so soon.");
			return false;
		}

		if (pPlayer.getTimer().stunTick > 0) {
			Output.simpleError(player, "You can't use magic while stunned.");
			return false;
		}

		if (pPlayer.getDieLog() > 0) {
			Output.simpleError(player, "You can't cast a spell right now.");
			return false;
		}

		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot != null)
			if (!plot.isAllowMagic()) {
				Output.simpleError(player, "You can't use magic in " + plot.getName() + ".");
				return false;
			}

		if (pPlayer.getPromptedSpell() != null) {
			Output.simpleError(player, "You are already casting a spell.");
			return false;
		}
		
		if(spell == null) {
			Output.simpleError(player, "There do not exists a spell witht that name.");
			return false;
		}

		if (!pPlayer.getSpellbook().contains(spell.getScroll())) {
			Output.simpleError(player, "Your spellbook does not contain the " + spell.getName() + " spell.");
			return false;
		}

		pPlayer.getTimer().cantCastTicks = spell.getCooldown();

		final Location loc = player.getLocation();

		final int size = 2;

		for (int x = -size; x <= size; x++)
			for (int y = -size; y <= size; y++)
				for (int z = -size; z <= size; z++) {
					final Material type = player.getWorld()
							.getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z).getType();
					if (type == Material.LAPIS_BLOCK) {
						Output.simpleError(player, "You can't seem to cast a spell here...");
						return false;
					}
				}

		if (!spell.verifyCastable(player))
			return false;

		if (!(player.isOp() || pPlayer.getMana() >= spell.getManaCost())) {
			this.notEnoughMana(player, spell);
			return false;
		}

		if (!this.hasReagents(player, spell.getReagentCost())) {
			this.notEnoughReagents(player, spell.getReagentCost());
			return false;
		}

		final int magerySkill = pPlayer.getCurrentBuild().getMagery().getLvl();
		final int minMagery = spell.getMinMagery();

		if (!(magerySkill >= minMagery)) {
			this.notSkilledEnough(player, spell);
			return false;
		}

		final int skillDif = magerySkill - minMagery;
		double chanceToCast = (double) skillDif / 200;
		if (chanceToCast < this.minChanceToCast)
			chanceToCast = this.minChanceToCast;
		final double rand = Math.random();

		if (!(magerySkill >= 1000 || rand < chanceToCast)) {
			// spell fizzled, so we want to tell the player if fizzled
			this.spellFizzled(player, spell);
			// and start the spell cooldown
			pPlayer.getTimer().cantCastTicks = spell.getCooldown();
			return false;
		}
		// chant the spell words so people know what you are doing
		this.chant(player, spell.getSpellWords());
		// take mana, we succeeded
		pPlayer.setMana(pPlayer.getMana() - spell.getManaCost());

		// determine if the spell is delayed
		final int castingDelay = spell.getCastingDelay();
		if (castingDelay > 0)
			this.castDelayed(player, pPlayer, spell);
		else
			this.castInstant(player, pPlayer, spell);
		// Whether we succeeded or failed, we have a chance to gain
		this.possibleSkillGain(player, pPlayer, spell);
		// but you also lose the regs
		this.takeRegs(player, spell.getReagentCost());

		// output the prompt if there is one (name rune, etc)
		if (spell.getPrompt() != null) {
			final String prompt = spell.getPrompt();
			player.sendMessage(ChatColor.YELLOW + prompt);
			spell.runebook(player);
		}
		return true;
	}

	public void chant(Player player, String spellWords) {
		player.sendMessage(ChatColor.AQUA + "You chant \"" + spellWords + "\".");
		if (Lostshard.isVanished(player))
			return;
		final int localRange = ChatHandler.getLocalChatRange();
		for (final Player p : Bukkit.getOnlinePlayers())
			if (!p.equals(player))
				if (p.getWorld().equals(player.getWorld()))
					if (Utils.isWithin(player.getLocation(), p.getLocation(), localRange * 2))
						p.sendMessage(ChatColor.AQUA + player.getName() + " chants \"" + spellWords + "\".");
	}

	public void damage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			final Player player = (Player) event.getEntity();
			final PseudoPlayer pPlayer = this.pm.getPlayer(player);
			if (pPlayer.getPromptedSpell() != null || pPlayer.getTimer().delayedSpell != null) {
				pPlayer.setPromptedSpell(null);
				pPlayer.getTimer().delayedSpell = null;
				Output.simpleError(player, "Damaged while casting a spell, it was disrupted.");
			} else if (pPlayer.getTimer().goToSpawnTicks > 0) {
				pPlayer.getTimer().goToSpawnTicks = 0;
				Output.simpleError(player, "Damaged while casting, /spawn was disrupted.");
			} else if (SPL_ClearSky.casting.removeIf(u -> player.getUniqueId().equals(u))) {
				Output.simpleError(player, "Moved while casting a Clear Sky, it was disrupted.");
			} else if (SPL_Day.casting.removeIf(u -> player.getUniqueId().equals(u))) {
				Output.simpleError(player, "Moved while casting a Day, it was disrupted.");
			}
		}
	}

	public Spell getSpellByName(String name) {
		final Scroll st = Scroll.getByString(name);
		if (st != null)
			return st.getSpell();
		return null;
	}

	public boolean hasReagents(Player player, List<ItemStack> list) {
		if (player.isOp())
			return true;
		if (list == null)
			return true;
		for (final ItemStack reagent : list)
			if (!player.getInventory().contains(reagent.getType(), reagent.getAmount()))
				return false;
		return true;
	}

	public void move(PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		if (pPlayer.getPromptedSpell() != null || pPlayer.getTimer().delayedSpell != null) {
			pPlayer.setPromptedSpell(null);
			pPlayer.getTimer().delayedSpell = null;
			Output.simpleError(player, "Moved while casting a spell, it was disrupted.");
		} else if (pPlayer.getTimer().goToSpawnTicks > 0) {
			pPlayer.getTimer().goToSpawnTicks = 0;
			Output.simpleError(player, "Moved while casting, /spawn was disrupted.");
		} else if (SPL_ClearSky.casting.removeIf(u -> player.getUniqueId().equals(u))) {
			Output.simpleError(player, "Moved while casting a Clear Sky, it was disrupted.");
		} else if (SPL_Day.casting.removeIf(u -> player.getUniqueId().equals(u))) {
			Output.simpleError(player, "Moved while casting a Day, it was disrupted.");
		}
	}

	public void notEnoughMana(Player player, Spell spell) {
		Output.simpleError(player, "You do not have enough mana to cast " + spell.getName() + ".");
	}

	public void notEnoughReagents(Player player, List<ItemStack> list) {
		Output.simpleError(player, "You do not have the proper reagents for that spell, they are:");
		for (final ItemStack reagent : list)
			Output.simpleError(player, "-" + reagent.getType().name());
	}

	public void notSkilledEnough(Player player, Spell spell) {
		Output.simpleError(player, "You are not skilled enough at magery to cast " + spell.getName() + ".");
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)))
			return;
		final Player player = event.getPlayer();
		if (player.getItemInHand().getType().equals(Material.STICK)) {
			final ItemStack wand = player.getItemInHand();
			final ItemMeta wandMeta = wand.getItemMeta();
			if (wandMeta.hasLore() && wandMeta.getLore().size() > 0 && wandMeta.getLore().get(0)
					.equalsIgnoreCase("This magical wand can be used to cast a spell with only a touch of a button."))
				if (wandMeta.hasDisplayName()) {
					final Scroll scroll = Scroll.getByString(ChatColor.stripColor(wandMeta.getDisplayName()));
					manager.castSpell(player, scroll.getSpell());
				}
		}
	}

	public void onPlayerPromt(AsyncPlayerChatEvent event) {
		final Player player = event.getPlayer();
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		final String message = event.getMessage();
		final Spell spell = pPlayer.getPromptedSpell();
		spell.setResponse(message);

		final int castingDelay = spell.getCastingDelay();
		if (castingDelay > 0) {
			spell.preAction(player);
			pPlayer.getTimer().delayedSpell = spell;
		} else {
			spell.preAction(player);
			pPlayer.getTimer().cantCastTicks = spell.getCooldown();
			spell.doAction(player);
		}
		pPlayer.setPromptedSpell(null);
		event.setCancelled(true);
	}

	public void possibleSkillGain(Player player, PseudoPlayer pseudoPlayer, Spell spell) {
		if (pseudoPlayer.getCurrentBuild().getMagery().isLocked())
			return;
		final int curSkill = pseudoPlayer.getCurrentBuild().getMagery().getLvl();
		if (curSkill < 1000 && pseudoPlayer.getCurrentBuild().getTotalSkillVal() < pseudoPlayer.getMaxSkillValTotal()) {
			final int minMagery = spell.getMinMagery();
			double percentChance;
			if (curSkill < minMagery + 200) {
				// possible to gain (within 20 skill points)
				percentChance = (double) (1000 - curSkill) / 1000;
				// we don't want it to be TOOOO hard to gain skill
				if (percentChance > this.minPercentChanceSkillGain) {
					final int gain = pseudoPlayer.getCurrentBuild().getMagery().skillGain(pseudoPlayer);
					Output.gainSkill(player, "Magery", gain, curSkill+gain);
				}
			}
		}
	}

	public void spellFizzled(Player player, Spell spell) {
		player.sendMessage(ChatColor.DARK_GRAY + "The spell fizzles.");
		player.getWorld().playEffect(player.getLocation(), Effect.EXTINGUISH, 15);
		this.takeRegs(player, spell.getReagentCost());
	}

	public void takeRegs(Player player, List<ItemStack> list) {
		if (list == null)
			return;
		for (final ItemStack reagent : list)
			ItemUtils.removeItem(player.getInventory(), reagent.getType(), reagent.getAmount());
	}

	public boolean useScroll(Player player, Scroll scroll) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot != null)
			if (!plot.isAllowMagic()) {
				Output.simpleError(player, "You can't use magic in " + plot.getName() + ".");
				return false;
			}

		final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);

		if (pseudoPlayer.getTimer().cantCastTicks > 0) {
			Output.simpleError(player, "You can't cast another spell again so soon.");
			return false;
		}

		if (pseudoPlayer.getTimer().stunTick > 0) {
			Output.simpleError(player, "You can't use magic while stunned.");
			return false;
		}

		final Location loc = player.getLocation();
		final Spell spell = scroll.getSpell();
		for (int x = -2; x <= 2; x++)
			for (int y = -2; y <= 2; y++)
				for (int z = -2; z <= 2; z++) {
					final Material blockMat = player.getWorld()
							.getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z).getType();
					if (blockMat == Material.LAPIS_BLOCK) {
						Output.simpleError(player, "You can't seem to cast a spell here...");
						pseudoPlayer.getTimer().cantCastTicks = spell.getCooldown();
						return false;
					}
				}
		// make sure we even have that spell still
		if (spell.verifyCastable(player)) {
			if (player.isOp() || pseudoPlayer.getMana() >= spell.getManaCost()) {
				// chant the spell words so people know what you are doing
				this.chant(player, spell.getSpellWords());
				pseudoPlayer.setMana(pseudoPlayer.getMana() - spell.getManaCost());
				pseudoPlayer.update();

				// determine if the spell is delayed
				final int castingDelay = spell.getCastingDelay();
				boolean spellGood = false;
				if (castingDelay > 0) {
					this.castDelayed(player, pseudoPlayer, spell);
					spellGood = true;
				} else {
					this.castInstant(player, pseudoPlayer, spell);
					spellGood = true;
				}

				if (spell.getPrompt() != null && spellGood) {
					final String prompt = spell.getPrompt();
					player.sendMessage(ChatColor.YELLOW + prompt);
					spell.runebook(player);
				}
			} else {
				this.notEnoughMana(player, spell);
				return false;
			}
		} else {
			pseudoPlayer.getTimer().cantCastTicks = spell.getCooldown();
			return false;
		}
		return true;
	}
}
