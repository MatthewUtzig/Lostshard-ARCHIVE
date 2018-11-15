package com.lostshard.RPG.Plots;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;

public class VaultPlot extends Plot{
	private ArrayList<Block> _possibleGoldBlocks = new ArrayList<Block>();
	protected String _murdererStartString;
	protected String _murdererEndGoodString;
	protected String _murdererEndBadString;
	protected String _blueStartString;
	protected String _blueEndGoodString;
	protected String _blueEndBadString;
	
	public VaultPlot(String name, Location location, int radius, String owner,
			ArrayList<String> coOwners, ArrayList<String> friends, int money,
			boolean protect, boolean locked, boolean city,
			HashMap<String, LockedBlock> lockedBlocks, boolean friendBuild,
			ArrayList<PlotNPC> plotNPCs, int forSaleAmount,
			boolean kickUpgrade, boolean neutral, boolean explosionAllowed) {
		super(name, location, radius, owner, coOwners, friends, money, protect, locked,
				city, lockedBlocks, friendBuild, plotNPCs, forSaleAmount, kickUpgrade,
				neutral, explosionAllowed);
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<Block> getPossibleGoldBlocks() {
		return _possibleGoldBlocks;
	}
	
	public void spawnGold() {
		Player[] onlinePlayers = Bukkit.getOnlinePlayers();
		int numGoldBlocks = onlinePlayers.length;
		if(numGoldBlocks > _possibleGoldBlocks.size()) {
			numGoldBlocks = _possibleGoldBlocks.size();
		}
		
		for(int i=0; i<numGoldBlocks; i++) {
			Block b = _possibleGoldBlocks.get(i);
			b.setType(Material.GOLD_BLOCK);	
		}
		
		/*for(Player p : onlinePlayers) {
			PseudoPlayer pP = PseudoPlayerHandler.getPseudoPlayer(p.getName());
			if(pP.isMurderer()) {
				p.sendMessage(ChatColor.GREEN+"The "+this.getName()+" ")
			}
			else {
				
			}
		}*/
	}
}
