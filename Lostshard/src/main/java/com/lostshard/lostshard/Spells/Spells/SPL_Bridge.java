package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.entity.Player;

import com.lostshard.lostshard.Spells.RangedSpell;

public class SPL_Bridge extends RangedSpell {

	public SPL_Bridge() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void preAction(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doAction(Player player) {
//		IntPoint srcPoint = new IntPoint(player.getLocation().getBlockX(), player.getLocation().getBlockY()-1, player.getLocation().getBlockZ());
//		IntPoint destPoint = new IntPoint(_blockFound.getX(), _blockFound.getY(), _blockFound.getZ());
//		
//		ArrayList<Block> blocks = new ArrayList<Block>();
//		
//		ArrayList<IntPoint> intPoints = Bresenham.bresenham3d(srcPoint.x, srcPoint.y, srcPoint.z, destPoint.x, destPoint.y, destPoint.z);
//		for(IntPoint intPoint : intPoints) {
//			Block b = player.getWorld().getBlockAt(intPoint.x, intPoint.y, intPoint.z);
//			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
//			b = player.getWorld().getBlockAt(intPoint.x+1, intPoint.y, intPoint.z);
//			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
//			b = player.getWorld().getBlockAt(intPoint.x-1, intPoint.y, intPoint.z);
//			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
//			b = player.getWorld().getBlockAt(intPoint.x, intPoint.y, intPoint.z+1);
//			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
//			b = player.getWorld().getBlockAt(intPoint.x, intPoint.y, intPoint.z-1);
//			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
//			b = player.getWorld().getBlockAt(intPoint.x+1, intPoint.y, intPoint.z+1);
//			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
//			b = player.getWorld().getBlockAt(intPoint.x+1, intPoint.y, intPoint.z-1);
//			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
//			b = player.getWorld().getBlockAt(intPoint.x-1, intPoint.y, intPoint.z-1);
//			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
//			b = player.getWorld().getBlockAt(intPoint.x-1, intPoint.y, intPoint.z+1);
//			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
//		}
//		
//		if(blocks.size() > 0) {
//			Bridge bridge = new Bridge(blocks, player.getName(), 150);
//			Magery.addMagicStructure(bridge);
//		}
	}

}
