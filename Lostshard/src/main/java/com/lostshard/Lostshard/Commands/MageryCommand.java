package com.lostshard.Lostshard.Commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.Lostshard.Intake.Sender;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.SpellManager;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Player.Rune;
import com.lostshard.Lostshard.Objects.Player.Runebook;
import com.lostshard.Lostshard.Objects.Player.SpellBook;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Spells.Spell;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.Utils;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Text;

public class MageryCommand {

	PlayerManager pm = PlayerManager.getManager();
	SpellManager sm = SpellManager.getManager();

	
	@Command(aliases = { "bind" }, desc = "Turns a stick into a magical wand which is bound to a spell", usage = "<spell>")
	public void bind(@Sender Player player, @Text String scrollName) {
		if (!player.getItemInHand().getType().equals(Material.STICK)) {
			Output.simpleError(player, "You can only bind spells to sticks.");
			return;
		}
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		final Scroll scroll = Scroll.getByString(scrollName);
		if (scroll == null || !pPlayer.getSpellbook().contains(scroll)) {
			Output.simpleError(player, "Your spellbook does not contain " + scrollName + ".");
			return;
		}
		final String spellName = scroll.getName();
		final ItemStack wand = player.getItemInHand();
		final ItemMeta wandMeta = wand.getItemMeta();
		wandMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		wandMeta.addEnchant(Enchantment.DURABILITY, 0, false);
		wandMeta.setDisplayName(ChatColor.GOLD + spellName);
		final List<String> wandLore = new ArrayList<String>();
		wandLore.add("This magical wand can be used to cast a spell with only a touch of a button.");
		wandMeta.setLore(wandLore);
		wand.setItemMeta(wandMeta);
		player.setItemInHand(wand);
		Output.positiveMessage(player, "You have bound " + spellName + " to the wand.");
	}

	public boolean haveScroll(Scroll scroll, PseudoPlayer pPlayer, Player player) {
		if (scroll != null && pPlayer.getScrolls().contains(scroll))
			return true;
		if(scroll == null)
			Output.simpleError(player, "There do not exist a scroll with the name.");
		else
			Output.simpleError(player, "You do not have a scroll of " + scroll.getName() + ".");
		return false;
	}
	
	
	@Command(aliases = { "cast" }, desc = "Casts a spell", usage = "<spell>")
	public void cast(@Sender Player player, @Text String spellName) {
		final Spell spell = this.sm.getSpellByName(spellName);
		if (spell == null) {
			Output.simpleError(player, "No spell with the name \"" + spellName + "\".");
		}
		this.sm.castSpell(player, spell);
	}
	
	@Command(aliases = { "runebook" }, desc = "Shows you your runebook", usage = "<subcmd>")
	public void runebook(@Sender Player player, @Text @Optional(value="") String arg) {
		String[] args = arg.split(" ");
		if (args.length == 0 || args.length >= 2 && args[0].equalsIgnoreCase("page")) {
			int page;
			try {
				page = Integer.parseInt(args[1]);
			} catch (Exception e) {
				page = 0;
			}
			Output.outputRunebook(player, page);
		} else if (args.length > 0) {
			final String secondaryCommand = args[0];
			if (secondaryCommand.equalsIgnoreCase("give")) {
				if (args.length >= 3) {
					final String targetName = args[1];
					final Player targetPlayer = Bukkit.getPlayer(targetName);
					if (targetPlayer != null || Lostshard.isVanished(targetPlayer) && !player.isOp()) {
						final PseudoPlayer targetPseudoPlayer = this.pm.getPlayer(targetPlayer);

						final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);
						final Runebook runebook = pseudoPlayer.getRunebook();
						final Runebook targetRunebook = targetPseudoPlayer.getRunebook();
						if (targetPlayer.isOp()
								|| targetPseudoPlayer.wasSubscribed() && targetRunebook.size() < 16
								|| targetRunebook.size() < 8) {

							String runeLabel = "";
							final int splitLength = args.length;
							for (int i = 2; i < splitLength; i++) {
								runeLabel += args[i];
								if (i < splitLength - 1)
									runeLabel += " ";
							}
							runeLabel = runeLabel.trim();

							final Runebook runes = runebook;
							Rune foundRune = null;
							for (final Rune rune : runes)
								if (rune.getLabel().equalsIgnoreCase(runeLabel)) {
									foundRune = rune;
									break;
								}
							if (foundRune != null) {
								final Runebook targetRunes = targetRunebook;
								boolean foundMatching = false;
								for (final Rune rune : targetRunes)
									if (rune.getLabel().equalsIgnoreCase(foundRune.getLabel())) {
										foundMatching = true;
										break;
									}
								if (!foundMatching) {
									runebook.remove(foundRune);
									targetRunebook.add(foundRune);
									targetPseudoPlayer.update();
									Output.positiveMessage(player, "You have given the rune " + foundRune.getLabel()
											+ " to " + targetPlayer.getName());
									Output.positiveMessage(targetPlayer,
											player.getName() + " has given you the rune " + foundRune.getLabel() + ".");
								} else
									Output.simpleError(player,
											targetPlayer.getName() + " already has a rune with that label.");
							} else
								Output.simpleError(player, "Could not find a rune with that label.");
						} else
							Output.simpleError(player, targetPlayer.getName() + " has too many runes.");
					} else
						Output.simpleError(player, "That player is not online.");
				} else
					Output.simpleError(player, "Use /runebook give (player name) (rune name)");
			} else if (secondaryCommand.equalsIgnoreCase("remove"))
				if (args.length >= 2) {

					String runeLabel = "";
					final int splitLength = args.length;
					for (int i = 1; i < splitLength; i++) {
						runeLabel += args[i];
						if (i < splitLength - 1)
							runeLabel += " ";
					}
					runeLabel = runeLabel.trim();

					final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);
					final Runebook runebook = pseudoPlayer.getRunebook();
					final Rune foundRune = runebook.get(runeLabel);
					if (foundRune != null) {
						runebook.remove(foundRune);
						Output.positiveMessage(player, "You have removed the rune " + foundRune.getLabel());
						pseudoPlayer.update();
					} else
						Output.simpleError(player, "Could not find a rune with that label.");
				} else
					Output.simpleError(player, "Use /runebook remove (rune label)");
		}

	}

	@Command(aliases = { "scrolls" }, desc = "Shows you your runebook", usage = "<subcmd>")
	public void scrolls(@Sender Player player, @Text @Optional(value="") String arg) {
		String[] args = arg.split(" ");
		if (args.length >= 2) {
			if (args[0].equalsIgnoreCase("use") || args[0].equalsIgnoreCase("cast")) {
				final PseudoPlayer pPlayer = this.pm.getPlayer(player);
				final String scrollName = StringUtils.join(args, "", 1, args.length);
				final Scroll scroll = Scroll.getByString(scrollName);
				if (!this.haveScroll(scroll, pPlayer, player))
					return;
				if (this.sm.useScroll(player, scroll)) {
					pPlayer.getScrolls().remove(scroll);
					pPlayer.update();
				}
			} else if (args[0].equalsIgnoreCase("give")) {
				if (args.length < 3) {
					Output.simpleError(player, "Use \"/scrolls give (player name) (spell name)\"");
					return;
				}

				final PseudoPlayer pPlayer = this.pm.getPlayer(player);
				final Player targetPlayer = player.getServer().getPlayer(args[1]);
				if (targetPlayer == null) {
					Output.simpleError(player, args[1] + " is not online.");
					return;
				}
				
				if(player == targetPlayer) {
					Output.simpleError(player, "You cannot give your self a scroll.");
					return;
				}

				final PseudoPlayer tpPlayer = this.pm.getPlayer(targetPlayer);

				if (!player.isOp())
					if (!Utils.isWithin(player.getLocation(), targetPlayer.getLocation(), 10)) {
						Output.simpleError(player,
								"You are not close enough to give " + targetPlayer.getName() + " a scroll.");
						return;
					}

				final String scrollName = StringUtils.join(args, "", 2, args.length);
				if (scrollName.equalsIgnoreCase("all") && player.isOp()) {
					for (final Scroll s : Scroll.values())
						tpPlayer.addScroll(s);
					Output.positiveMessage(player, "You have given " + targetPlayer.getName() + " all scrolls.");
					Output.positiveMessage(targetPlayer, player.getName() + " has given you all scrolls.");
					return;
				}
				final Scroll scroll = Scroll.getByString(scrollName);
				if ((scroll == null || !pPlayer.getScrolls().contains(scroll)) && !(player.isOp() || scroll != null)) {
					Output.simpleError(player, "You do not have a scroll of " + scrollName + ".");
					return;
				}
				pPlayer.getScrolls().remove(scroll);
				tpPlayer.getScrolls().add(scroll);
				pPlayer.update();
				tpPlayer.update();
				if (scroll == null) {
					Output.simpleError(player, "Theres no scroll with the name \"" + scrollName + "\".");
					return;
				}
				Output.positiveMessage(player,
						"You have given " + targetPlayer.getName() + " a scroll of " + scroll.getName() + ".");
				Output.positiveMessage(targetPlayer,
						player.getName() + " has given you a scroll of " + scroll.getName() + ".");
			} else if (args[0].equalsIgnoreCase("spellbook")) {
				final PseudoPlayer pPlayer = this.pm.getPlayer(player);
				final String scrollName = StringUtils.join(args, "", 1, args.length);
				final Scroll scroll = Scroll.getByString(scrollName);
				if (scroll == null) {
					Output.simpleError(player, "There does not exist a sroll with that name.");
					return;
				}
				if (!this.haveScroll(scroll, pPlayer, player))
					return;
				final SpellBook spellbook = pPlayer.getSpellbook();
				if (!spellbook.contains(scroll)) {
					spellbook.add(scroll);
					pPlayer.update();
					pPlayer.update();
					Output.positiveMessage(player, "You have transferred " + scroll.getName() + " to your spellbook.");
				} else
					Output.simpleError(player, "Your spellbook already contains the " + scroll.getName() + " spell.");
			}
			return;
		} else {
			Output.outputScrolls(player, args);
			return;
		}
	}

	@Command(aliases = { "spellbook" }, desc = "Shows you your spellbook", usage = "<subcmd>")
	public void spellbook(@Sender Player player, @Optional int page) {
		Output.outputSpellbook(player, page);
	}

	@Command(aliases = { "unbind" }, desc = "Turns a wand into a regular stick", usage = "<spell>")
	public void unbind(@Sender Player player) {
		if (!player.getItemInHand().getType().equals(Material.STICK)) {
			Output.simpleError(player, "You can only bind spells to sticks.");
			return;
		}
		final ItemStack wand = player.getItemInHand();
		final ItemMeta wandMeta = wand.getItemMeta();
		if (wandMeta.hasLore() && wandMeta.getLore().size() > 0 && wandMeta.getLore().get(0)
				.equalsIgnoreCase("This magical wand can be used to cast a spell with only a touch of a button.")) {
			if (wandMeta.hasDisplayName()) {
				wand.setItemMeta(null);
				final String spellName = ChatColor.stripColor(wandMeta.getLore().get(0));
				player.setItemInHand(wand);
				Output.positiveMessage(player, "You have unbound " + spellName.toLowerCase() + " from the wand.");
			} else
				Output.simpleError(player, "Thats just a simple stick you fool.");
		} else
			Output.simpleError(player, "Thats just a simple stick you fool.");
	}
}
