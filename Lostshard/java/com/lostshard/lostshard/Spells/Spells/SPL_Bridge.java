package com.lostshard.lostshard.Spells.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Structures.Bridge;
import com.lostshard.lostshard.Utils.Bresenham;
import com.lostshard.lostshard.Utils.IntPoint;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

public class SPL_Bridge extends RangedSpell {

	public SPL_Bridge(Scroll scroll) {
		super(scroll);
		setRange(20);
		setCarePlot(false);
	}
	
	public boolean verifyCastable(Player player) {
		setFoundBlock(SpellUtils.blockInLOS(player, getRange()));
		if(getFoundBlock() == null) {
			Output.simpleError(player, "Invalid target.");
			return false;
		}
		
		//check for lapis below you
		if(player.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.LAPIS_BLOCK) ||
		   player.getLocation().getBlock().getRelative(0,-2,0).getType().equals(Material.LAPIS_BLOCK)){
			Output.simpleError(player, "can't bridge from a Lapis Lazuli block.");
			return false;
		}
		
		//check for lapis
		for(int x=getFoundBlock().getX()-3; x<=getFoundBlock().getX()+3; x++) {
			for(int y=getFoundBlock().getY()-3; y<=getFoundBlock().getY()+3; y++) {
				for(int z=getFoundBlock().getZ()-3; z<=getFoundBlock().getZ()+3; z++) {
					if(getFoundBlock().getWorld().getBlockAt(x, y, z).getType().equals(Material.LAPIS_BLOCK)) {
						Output.simpleError(player, "can't bridge to a location near Lapis Lazuli blocks.");
						return false;
					}
				}
			}
		}
		
		return true;
	}

	@Override
	public void preAction(Player player) {
		
	}

	@Override
	public void doAction(Player player) {
		IntPoint srcPoint = new IntPoint(player.getLocation().getBlockX(), player.getLocation().getBlockY()-1, player.getLocation().getBlockZ());
		IntPoint destPoint = new IntPoint(getFoundBlock().getX(), getFoundBlock().getY(), getFoundBlock().getZ());
		
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		ArrayList<IntPoint> intPoints = Bresenham.bresenham3d(srcPoint.x, srcPoint.y, srcPoint.z, destPoint.x, destPoint.y, destPoint.z);
		for(IntPoint intPoint : intPoints) {
			Block b = player.getWorld().getBlockAt(intPoint.x, intPoint.y, intPoint.z);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x+1, intPoint.y, intPoint.z);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x-1, intPoint.y, intPoint.z);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x, intPoint.y, intPoint.z+1);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x, intPoint.y, intPoint.z-1);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x+1, intPoint.y, intPoint.z+1);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x+1, intPoint.y, intPoint.z-1);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x-1, intPoint.y, intPoint.z-1);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x-1, intPoint.y, intPoint.z+1);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
		}
		
		if(blocks.size() > 0) {
			new Bridge(blocks, player.getUniqueId(), 150);
		}
	}

}
