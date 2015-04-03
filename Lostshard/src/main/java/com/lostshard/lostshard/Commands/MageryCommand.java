package com.lostshard.lostshard.Commands;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Rune;
import com.lostshard.lostshard.Objects.Runebook;
import com.lostshard.lostshard.Objects.SpellBook;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

public class MageryCommand implements CommandExecutor, TabCompleter {

	PlayerManager pm = PlayerManager.getManager();
	SpellManager sm = SpellManager.getManager();
	
	/**
	 * @param Lostshard
	 *            as plugin
	 */
	public MageryCommand(Lostshard plugin) {
		plugin.getCommand("cast").setExecutor(this);
		plugin.getCommand("scrolls").setExecutor(this);
		plugin.getCommand("runebook").setExecutor(this);
		plugin.getCommand("spellbook").setExecutor(this);
		plugin.getCommand("bind").setExecutor(this);
		plugin.getCommand("unbind").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("cast")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			if(args.length < 1) {
				Output.simpleError(sender, "/cast (spell)");
				return true;
			}
			String spellName = StringUtils.join(args, " ");
			Player player = (Player) sender;
			Spell spell = sm.getSpellByName(spellName);
			if(spell == null) {
				Output.simpleError(sender, "No spell with the name \""+spellName+"\".");
				return true;
			}
			sm.castSpell(player, spell);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("scrolls")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			scrolls(player, args);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("runebook")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			runebook(player, args);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("spellbook")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			spellbook(player, args);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("bind")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			bind(player, args);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("unbind")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			unbind(player);
			return true;
		}
		return false;
	}

	private void bind(Player player, String[] args) {
		if(!player.getItemInHand().getType().equals(Material.STICK)) {
			Output.simpleError(player, "You can only bind spells to sticks.");
			return;
		}
		if(args.length < 1) {
			Output.simpleError(player, "/bind (spell)");
			return;
		}
		PseudoPlayer pPlayer = pm.getPlayer(player);
		String scrollName = StringUtils.join(args, "", 0, args.length);
		Scroll scroll = Scroll.getByString(scrollName);
		if(scroll == null || !pPlayer.getSpellbook().containSpell(scroll)) {
			Output.simpleError(player, "Your spellbook does not contain "+scrollName+".");
			return;
		}
		String spellName = scroll.getName();
		ItemStack wand = player.getItemInHand();
		ItemMeta wandMeta = wand.getItemMeta();
		wandMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		wandMeta.addEnchant(Enchantment.DURABILITY, 0, false);
		wandMeta.setDisplayName(ChatColor.GOLD+spellName);
		List<String> wandLore = new ArrayList<String>();
		wandLore.add(ChatColor.GREEN+"Wand");
		wandMeta.setLore(wandLore);
		wand.setItemMeta(wandMeta);
		player.setItemInHand(wand);
		Output.positiveMessage(player, "You have bound "+spellName+" to the wand.");
	}
	
	private void unbind(Player player) {
		if(!player.getItemInHand().getType().equals(Material.STICK)) {
			Output.simpleError(player, "You can only bind spells to sticks.");
			return;
		}
		ItemStack wand = player.getItemInHand();
		ItemMeta wandMeta = wand.getItemMeta();
		if(wandMeta.hasLore() && wandMeta.getLore().size() > 0 && ChatColor.stripColor(wandMeta.getLore().get(0)).equalsIgnoreCase("Wand")) {
			if(wandMeta.hasDisplayName()) {
				wand.setItemMeta(null);
				String spellName = ChatColor.stripColor(wandMeta.getLore().get(0));
				player.setItemInHand(wand);
				Output.positiveMessage(player, "You have unbound "+spellName.toLowerCase()+" from the wand.");
			}else{
				Output.simpleError(player, "Thats just a simple stick you fool.");
			}
		}else{
			Output.simpleError(player, "Thats just a simple stick you fool.");
		}
	}
	

	@SuppressWarnings("deprecation")
	private void scrolls(Player player, String[] args) {
		if(args.length >= 2) {
			if(args[0].equalsIgnoreCase("use") || args[0].equalsIgnoreCase("cast")) {
				PseudoPlayer pPlayer = pm.getPlayer(player);
				String scrollName = StringUtils.join(args, "", 1, args.length);
				Scroll scroll = Scroll.getByString(scrollName);
				if(scroll == null || !pPlayer.getScrolls().contains(scroll)) {
					Output.simpleError(player, "You do not have a scroll of "+scrollName+".");
					return;
				}
				if(sm.useScroll(player, scroll)) {
					pPlayer.getScrolls().remove(scroll);
					Database.deleteScroll(scroll, pPlayer.getId());
				}
			}
			else if(args[0].equalsIgnoreCase("give")) {
				if(args.length < 3) {
					Output.simpleError(player, "Use \"/scrolls give (player name) (spell name)\"");
					return;
				}
				
				PseudoPlayer pPlayer = pm.getPlayer(player);
				Player targetPlayer = player.getServer().getPlayer(args[1]);
				if(targetPlayer == null) {
					Output.simpleError(player, args[1]+" is not online.");
					return;
				}
				
				
				PseudoPlayer tpPlayer = pm.getPlayer(targetPlayer);
				
				if(!player.isOp()){
					if(!Utils.isWithin(player.getLocation(), targetPlayer.getLocation(), 10)) {
						Output.simpleError(player, "You are not close enough to give "+targetPlayer.getName()+" a scroll.");
						return;
					}
				}
				
				String scrollName = StringUtils.join(args, "", 1, args.length);
				Scroll scroll = Scroll.getByString(scrollName);
				if(scroll == null || !pPlayer.getScrolls().contains(scroll)) {
					Output.simpleError(player, "You do not have a scroll of "+scrollName+".");
					return;
				}
				
				pPlayer.getScrolls().remove(scroll);
				tpPlayer.getScrolls().add(scroll);
				Database.updateScrollOwner(scroll, tpPlayer.getId(), pPlayer.getId());
				Output.positiveMessage(player, "You have given "+targetPlayer.getName()+" a scroll of "+scroll.getName()+".");
				Output.positiveMessage(targetPlayer, player.getName()+" has given you a scroll of "+scroll.getName()+".");
			}else if(args[0].equalsIgnoreCase("spellbook")) {
				PseudoPlayer pPlayer = pm.getPlayer(player);
				String scrollName = StringUtils.join(args, "", 1, args.length);
				Scroll scroll = Scroll.getByString(scrollName);
				if(scroll == null || !pPlayer.getScrolls().contains(scroll)) {
					Output.simpleError(player, "You do not have a scroll of "+scrollName+".");
					return;
				}
				SpellBook spellbook = pPlayer.getSpellbook();
				if(!spellbook.containSpell(scroll)) {
					spellbook.addSpell(scroll);
					Database.deleteScroll(scroll, pPlayer.getId());
					pPlayer.update();
					Output.positiveMessage(player, "You have transferred "+scroll.getName()+" to your spellbook.");
				}
				else Output.simpleError(player, "Your spellbook already contains the "+scroll.getName()+" spell.");
			}
			return;
		}else {
			Output.outputScrolls(player, args);
			return;
		}
	}

	private void spellbook(Player player, String[] args) {
		Output.outputSpellbook(player, args);
	}

	@SuppressWarnings("deprecation")
	private void runebook(Player player, String[] args) {
		if(args.length == 0 || (args.length >= 11 && args[1].equalsIgnoreCase("page"))) {
        	Output.outputRunebook(player, args);
    	}
    	else if(args.length > 0) {
    		String secondaryCommand = args[0];
    		if(secondaryCommand.equalsIgnoreCase("give")) {
    			if(args.length >= 3) {
    				String targetName = args[1];
    				Player targetPlayer = Bukkit.getPlayer(targetName);
    				if(targetPlayer != null) {
    					PseudoPlayer targetPseudoPlayer = pm.getPlayer(targetPlayer);
    					
    					PseudoPlayer pseudoPlayer = pm.getPlayer(player);
    					Runebook runebook = pseudoPlayer.getRunebook();
    					Runebook targetRunebook = targetPseudoPlayer.getRunebook();
    					if(targetPlayer.isOp() || (targetPseudoPlayer.wasSubscribed() && targetRunebook.getNumRunes() < 16) || targetRunebook.getNumRunes() < 8) {
    						
    						String runeLabel = "";
    						int splitLength = args.length;
    						for(int i=2; i<splitLength; i++) {
    							runeLabel += args[i];
    							if(i < splitLength-1)
    								runeLabel+= " ";
    						}
    						runeLabel = runeLabel.trim();
    						
    						ArrayList<Rune> runes = runebook.getRunes();
    						Rune foundRune = null;
    						for(Rune rune : runes) {
    							if(rune.getLabel().equalsIgnoreCase(runeLabel)) {
    								foundRune = rune;
    								break;
    							}
    						}
    						if(foundRune != null) {
    							ArrayList<Rune> targetRunes = targetRunebook.getRunes();
    							boolean foundMatching = false;
    							for(Rune rune : targetRunes) {
    								if(rune.getLabel().equalsIgnoreCase(foundRune.getLabel())) {
    									foundMatching = true;
    									break;
    								}
    							}
    							if(!foundMatching) {
    								runebook.removeRune(foundRune);
    								targetRunebook.addRune(foundRune);
    								Database.updateRune(targetPseudoPlayer, foundRune);
    								Output.positiveMessage(player, "You have given the rune "+foundRune.getLabel()+" to "+targetPlayer.getName());
    								Output.positiveMessage(targetPlayer, player.getName()+" has given you the rune "+foundRune.getLabel()+".");
    							}
    							else Output.simpleError(player, targetPlayer.getName()+" already has a rune with that label.");
    						}
    						else Output.simpleError(player, "Could not find a rune with that label.");
    					}
    					else Output.simpleError(player, targetPlayer.getName()+" has too many runes.");
    				}
    				else Output.simpleError(player, "That player is not online.");
    			}
    			else Output.simpleError(player, "Use /runebook give (player name) (rune name)");
    		}
    		else if(secondaryCommand.equalsIgnoreCase("remove")) {
    			if(args.length >= 2) {
    				
    				String runeLabel = "";
					int splitLength = args.length;
					for(int i=1; i<splitLength; i++) {
						runeLabel += args[i];
						if(i < splitLength-1)
							runeLabel+= " ";
					}
					runeLabel = runeLabel.trim();
    				
    				PseudoPlayer pseudoPlayer = pm.getPlayer(player);
    				Runebook runebook = pseudoPlayer.getRunebook();
    				ArrayList<Rune> runes = runebook.getRunes();
    				Rune foundRune = null;
    				for(Rune rune : runes) {
    					if(rune.getLabel().equalsIgnoreCase(runeLabel)) {
    						foundRune = rune;
    						break;
    					}
    				}
    				if(foundRune != null) {
    					runebook.removeRune(foundRune);
    					Output.positiveMessage(player, "You have removed the rune "+foundRune.getLabel());
    					Database.deleteRune(foundRune);
    				}
    				else Output.simpleError(player, "Could not find a rune with that label.");
    			}
    			else Output.simpleError(player, "Use /runebook remove (rune label)");
    		}
    	}

	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}
}
