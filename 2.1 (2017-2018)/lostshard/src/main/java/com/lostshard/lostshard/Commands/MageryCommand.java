package com.lostshard.lostshard.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.lostshard.Intake.Sender;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;
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
