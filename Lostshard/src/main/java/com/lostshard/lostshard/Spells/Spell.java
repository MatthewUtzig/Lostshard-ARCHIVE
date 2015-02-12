package com.lostshard.lostshard.Spells;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Groups.Party;
import com.lostshard.lostshard.Utils.AimBlock;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

public class Spell {

	PlayerManager pm = PlayerManager.getManager();
	PlotManager ptm = PlotManager.getManager();
	
	@SuppressWarnings("deprecation")
	public static HashSet<Byte> invisibleBlocks = new HashSet<Byte>(Arrays.asList(
			(byte)Material.AIR.getId(),
			(byte)Material.SNOW.getId(),
			(byte)Material.FIRE.getId(),
			(byte)Material.TORCH.getId(),
			(byte)Material.LADDER.getId(),
			(byte)Material.RED_MUSHROOM.getId(),
			(byte)Material.RED_ROSE.getId(),
			(byte)Material.YELLOW_FLOWER.getId(),
			(byte)Material.BROWN_MUSHROOM.getId(),
			(byte)Material.REDSTONE_WIRE.getId(),
			(byte)Material.WOOD_PLATE.getId(),
			(byte)Material.STONE_PLATE.getId(),
			(byte)Material.PORTAL.getId(),
			(byte)Material.LAVA.getId(),
			(byte)Material.WATER.getId(),
			(byte)Material.STONE_BUTTON.getId(),
			(byte)Material.LEVER.getId(),
			(byte) Material.CARPET.getId(),
			(byte) Material.IRON_PLATE.getId(),
			(byte) Material.GOLD_PLATE.getId(),
			(byte) Material.WOOD_BUTTON.getId(),
			(byte) Material.LONG_GRASS.getId(),
			(byte) Material.RAILS.getId(),
			(byte) Material.VINE.getId(),
			(byte) Material.SIGN_POST.getId(),
			(byte) Material.WALL_SIGN.getId()));
	
	
	@SuppressWarnings("deprecation")
	protected static boolean isValidRuneLocation(Player player, Location location) {

		Block blockAt = location.getBlock();
		Block blockAbove = blockAt.getRelative(0,1,0);
		if(!invisibleBlocks.contains((byte)blockAt.getType().getId()) || !invisibleBlocks.contains((byte)blockAbove.getType().getId())) {
			Output.simpleError(player, "Invalid location, blocked.");
			return false;
		}
		
		return true;
	}
	
	private String[] description = { ChatColor.LIGHT_PURPLE + "No description" };

	public String getName() {
		return "";
	}

	public String getSpellWords() {
		return "";
	}

	public int getManaCost() {
		return 0;
	}

	public int getCircle() {
		return 0;
	}

	public int getCastingDelay() {
		return 0;
	}

	public boolean isWand() {
		return false;
	}

	public String[] getDescription() {
		return description;
	}

	public void setDescription(String[] description) {
		this.description = description;
	}

	public boolean verifyCastable(Player player, PseudoPlayer pPlayer) {
		return true;
	}

	public void preAction(Player player) {
		System.out.println("Attempting to preAction Spell");
	}

	public void doAction(Player player) {
		System.out.println("Attempting to doAction Spell");
	}
	
	protected static Player playerInLOS(Player player, PseudoPlayer pseudoPlayer, int range) {
		AimBlock aimHit = new AimBlock(player, range, .5);
		
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		ArrayList<Player> playersInRange = new ArrayList<Player>();
		for(Player p : onlinePlayers) {
			if(p != player) {
				if(Utils.isWithin(player.getLocation(), p.getLocation(), range)) {
					playersInRange.add(p);
				}
			}
		}
		
		if(playersInRange.size() <= 0)
			return null;
		
		Clan clan = pseudoPlayer.getClan();
		Party party = pseudoPlayer.getParty();
		
		boolean done = false;
		Player _firstNeutralFound = null;
		while (!done) {
			Block b = aimHit.getNextBlock();
			if(b != null) {
				for(Player p : playersInRange) {
					if(Utils.isWithin(b.getLocation(), p.getLocation(), 1.5)) {
						if(((clan != null) && clan.isInClan(p.getUniqueId())) || ((party != null) && (party.isMember(p)))) {
							return p;
						}
						
						if(_firstNeutralFound == null)
							_firstNeutralFound = p;
					}
				}
			}
			else return _firstNeutralFound;
		}
		
		return _firstNeutralFound;
	}

	@SuppressWarnings("deprecation")
	protected static Block blockInLOS(Player player, int range) {
		Block targetBlock = player.getTargetBlock(invisibleBlocks, range);
		return targetBlock;
	}

	public int getCooldownTicks() {
		return 0;
	}

	public int getMinMagery() {
		return 0;
	}
	
	public String getPrompt() {
		return null;
	}

	public ItemStack[] getReagentCost() {
		return null;
	}
}
