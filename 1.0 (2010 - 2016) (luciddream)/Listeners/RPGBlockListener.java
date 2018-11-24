package com.lostshard.RPG.Listeners;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.ItemStack;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.RPG;
import com.lostshard.RPG.TickObject;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Plots.LockedBlock;
import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Plots.PlotHandler;
import com.lostshard.RPG.Skills.Camp;
import com.lostshard.RPG.Skills.Lumberjacking;
import com.lostshard.RPG.Skills.Magery;
import com.lostshard.RPG.Skills.Mining;
import com.lostshard.RPG.Skills.Survivalism;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

/**
 * RPG block listener
 * @author luciddream
 */
public class RPGBlockListener implements Listener {
	@SuppressWarnings("unused")
	private RPG plugin;
	public static ArrayList<TickObject> _tickObjects = new ArrayList<TickObject>();
	
	public RPGBlockListener(RPG plugin) {
    	plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public static void tick() {
    	int numTickObjects = _tickObjects.size();
    	for(int i=numTickObjects-1; i>=0; i--) {
    		_tickObjects.get(i).tick();
    		if(_tickObjects.get(i).isDead()) {
    			_tickObjects.remove(i);
    			//System.out.println("Removing tick object, remaining: " + _tickObjects.size());
    		}
    	}
    }
    
    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
    	Block block = event.getBlock();
    	Plot plot = PlotHandler.findPlotAt(block.getLocation());
    	if(plot != null) {
    		if(plot.isProtected()) {
    			event.setCancelled(true);
    		}
    	}
    }
    
    @EventHandler
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
    	if (event.getNewCurrent() > 0) {
    		// greater than off
	    	Block block = event.getBlock();
	    	if(block.getTypeId() == 86) {
	    		block.setTypeId(91);
	    	}
    	}
    	else {
    		// off
    		Block block = event.getBlock();
	    	if(block.getTypeId() == 91) {
	    		block.setTypeId(86);
	    	}
    	}
    }
    
    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
    	if(RPG._debugV)
    		System.out.println("Block Burn");
    	
    	Block block = event.getBlock();
    	Plot plot = PlotHandler.findPlotAt(block.getLocation());
    	if(plot != null) {
    		if(plot.isProtected()) {
    			event.setCancelled(true);
    			return;
    		}
    	}
    	
    	if(block.getType().equals(Material.LOG)) {
    		ArrayList<Camp> camps = Survivalism.getCamps();
    		for(Camp camp : camps) {
    			if(camp.getLogBlock().equals(block)) {
    				event.setCancelled(true);
    				break;
    			}
    		}
    	}
    }
    
    /*@Override
    public void onBlockRightClick(BlockRightClickEvent event) {
    	if(RPG._debugV)
    		System.out.println("Block Right Clicked");
    	
    	if(event.getBlock().getType().equals(Material.IRON_BLOCK)) {
    		Blacksmithy.repair(event.getPlayer());
    	}
    }*/
    
    /*@Override
    public void onRedstoneChange(BlockFromToEvent event) {
    	
    }*/
    
    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
    	Block block = event.getBlock();
    	Player player = event.getPlayer();
    	
    	if(block.getType().equals(Material.TNT)) {
    		// Get all the plots within 10 blocks
    		ArrayList<Plot> plots = PlotHandler.getPlots();
    		ArrayList<Plot> plotsWithin = new ArrayList<Plot>();
    		for(Plot plot : plots) {
				if(plot.getLocation().getWorld().equals(block.getWorld())) {
					if(plot.isProtected()) {
						if(Utils.isWithin(plot.getLocation(), block.getLocation(), plot.getRadius()+10)) {
							plotsWithin.add(plot);
						}
					}
				}
			}
			
    		// If we are not within 10 blocks of any plots, fake old TNT but do not store ID
    		if(plotsWithin.size() <= 0) {
    			block.setType(Material.AIR);
    			Location loc = block.getLocation();
    			Location explodeAt = new Location(loc.getWorld(), loc.getX()+.5, loc.getY()+.5, loc.getZ()+.5);
        		block.getWorld().spawn(explodeAt, TNTPrimed.class);
    			return;
    		}
    		
    		// Determine if we are NOT owner/co-owner with any of them
			boolean allowed = true;
			Plot foundPlot = null;
			for(Plot p : plotsWithin) {
				if(!p.isOwner(player.getName()) && !p.isCoOwner(player.getName())) {
					foundPlot = p;
					allowed = false;
					break;
				}
			}

			
			// If we are not allowed to set off TNT here, cancel the event
			if(!allowed) {
				if(foundPlot != null)
					Output.simpleError(player, "Cannot set off TNT here, too close to "+foundPlot.getName());
				event.setCancelled(true);
				return;
			}
			
			// If we are allowed to set off TNT here, fake old TNT and store the allowed ID
			block.setType(Material.AIR);
			Location loc = block.getLocation();
			Location explodeAt = new Location(loc.getWorld(), loc.getX()+.5, loc.getY()+.5, loc.getZ()+.5);
    		TNTPrimed tntPrimed = block.getWorld().spawn(explodeAt, TNTPrimed.class);
    		int entityId = tntPrimed.getEntityId();
    		RPG._primedPlotTNT.add(entityId);
    	}
    	
    		if(event.getBlock().getType().equals(Material.STONE_BUTTON) || event.getBlock().getType().equals(Material.LEVER)) {
        		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
        		if(pseudoPlayer._lockingTicks <= 0 && pseudoPlayer._unlockingTicks <= 0) {
        			return;
        		}
        		
    			Plot plot = PlotHandler.findPlotAt(block.getLocation());
    			if(plot == null) {
    				if(pseudoPlayer._lockingTicks > 0) {
	    				Output.simpleError(player, "That device cannot be locked, it is not in a plot.");
	    				pseudoPlayer._lockingTicks = 0;
    				}
    				else if(pseudoPlayer._unlockingTicks > 0) {
    					Output.simpleError(player, "That device cannot be locked, it is not in a plot.");
	    				pseudoPlayer._unlockingTicks = 0;
    				}
    				return;
    			}
    			
    			if((pseudoPlayer._plottest == plot) || ( !plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName() ))) {
    				if(pseudoPlayer._lockingTicks > 0) {
    					Output.simpleError(player, "Only the owner or co-owner may lock a device in a plot.");
    					pseudoPlayer._lockingTicks = 0;
    				}
    				else if(pseudoPlayer._unlockingTicks > 0) {
    					Output.simpleError(player, "Only the owner or co-owner may unlock a device in a plot.");
    					pseudoPlayer._unlockingTicks = 0;
    				}
    				return;
    			}
    			else {
    				Location loc = block.getLocation();
    				String locString = block.getWorld().getName()+","+loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ();
    				if(plot.getLockedBlocks().containsKey(locString)) {
    					if(pseudoPlayer._lockingTicks > 0) {
    						pseudoPlayer._lockingTicks = 0;
    						Output.simpleError(player, "That device has already been locked.");
    						return;
    					}
    					else if(pseudoPlayer._unlockingTicks > 0) {
    						// trying to unlock a block that has been locked
    						pseudoPlayer._unlockingTicks = 0;
    						LockedBlock lockedBlock = plot.getLockedBlocks().get(locString);
    						plot.getLockedBlocks().remove(locString);
    						Database.removeLockedBlock(lockedBlock);
    						Output.positiveMessage(player, "You have unlocked the device.");
    						return;
    					}
    				}
    				else {
    					if(pseudoPlayer._lockingTicks > 0) {
    						pseudoPlayer._lockingTicks = 0;
    						// trying to lock a block that has not been locked
    						LockedBlock lockedBlock = Database.addLockedBlock(plot.getId(),locString);
    						plot.getLockedBlocks().put(locString, lockedBlock);
    						Output.positiveMessage(player, "You have locked the device.");
    						return;
    						/*Output.simpleError(player, "A key has been added to your keyring.");
    						pseudoPlayer.getKeyring().addKey(lockedBlock.getKey());
    						Database.updatePlot(plot);
    						Database.updatePlayer(player.getName());*/
    					}
    					else if(pseudoPlayer._unlockingTicks > 0) {
    						pseudoPlayer._unlockingTicks = 0;
    						// trying to unlock a block that has not been locked
    						Output.simpleError(player, "That device has not been locked.");
    						return;
    					}
    				}
    			}
    		}
    	//}
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
    	Player player = event.getPlayer();
    	PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(event.getPlayer().getName());
    	if(RPG._debugV)
    		System.out.println("Block Broken");
    	
    	if(player.isOp()) {
    		ItemStack itemInHand = player.getItemInHand();
    		if(itemInHand.getType().equals(Material.WOOD_AXE) || itemInHand.getType().equals(Material.COMPASS))
    			event.setCancelled(true);
    	}
    	
    	Block block = event.getBlock();
    	if(block.getType().equals(Material.WEB) || block.getType().equals(Material.LEAVES) || block.getType().equals(Material.SNOW_BLOCK)) {
    		// If its a web block they might be trying to dig a slow field web, let them if it is
    		String name = Magery.findCreatorNameOfBlockAt(block.getX(), block.getY(), block.getZ());
    		if(name != null) {
    			block.setType(Material.AIR);
    			event.setCancelled(true);
    			return;
    		}
    	}
    	
    	if(block.getType().equals(Material.GOLD_ORE)) {
    		System.out.println("Gold Ore Broken at ("+block.getX()+","+block.getY()+","+block.getZ()+") by "+event.getPlayer().getName()+".");
    		Output.sendToAdminIRC(event.getPlayer(), "X-RAY(BEEP)", "Gold Ore Broken at ("+block.getX()+","+block.getY()+","+block.getZ()+") by "+event.getPlayer().getName()+".");
    	}
    	else if(block.getType().equals(Material.DIAMOND_ORE)) {
    		System.out.println("Diamond Ore Broken at ("+block.getX()+","+block.getY()+","+block.getZ()+") by "+event.getPlayer().getName()+".");
    		Output.sendToAdminIRC(event.getPlayer(), "X-RAY(BEEP)", "Diamond Ore Broken at ("+block.getX()+","+block.getY()+","+block.getZ()+") by "+event.getPlayer().getName()+".");
    	}
    	else if(block.getType().equals(Material.DIAMOND_BLOCK)) {
    		System.out.println("Diamond BLOCK Broken at ("+block.getX()+","+block.getY()+","+block.getZ()+") by "+event.getPlayer().getName()+".");
    		Output.sendToAdminIRC(event.getPlayer(), "X-RAY(BEEP)", "Diamond BLOCK at ("+block.getX()+","+block.getY()+","+block.getZ()+") by "+event.getPlayer().getName()+".");
    	}
    	else if(block.getType().equals(Material.GOLD_BLOCK)) {
    		System.out.println("Gold BLOCK Broken at ("+block.getX()+","+block.getY()+","+block.getZ()+") by "+event.getPlayer().getName()+".");
    		Output.sendToAdminIRC(event.getPlayer(), "X-RAY(BEEP)", "Gold BLOCK at ("+block.getX()+","+block.getY()+","+block.getZ()+") by "+event.getPlayer().getName()+".");
    	}
    	
    	/*if(block.getType().equals(Material.CHEST)) {
    		Chest chest = (Chest)block.getState();
    		ItemStack[] contents = chest.getInventory().getContents();
    		
    		boolean allowed = true;
    		
    		for (ItemStack iS : contents) {
    			if(iS != null) {
    				allowed = false;
    				break;
    			}
    		}
    			
    		if(!allowed) {
	    		Output.simpleError(player, "You cannot break chests with items in them.");
	    		event.setCancelled(true);
    		}
    	}*/
    	
    	// Get the plot for our current location
    	Plot plot = PlotHandler.findPlotAt(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ()));
    	// If we are not in the wilderness
		if(plot != null) {
			// and the plot is protected
			if(plot.isProtected()) {
				// Get the person who broke the block
				
				// See if they are the owner or a co-owner
				if((pseudoPlayer._plottest == plot) || ( !plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName()))) {
					if(plot.isFriendBuild() && plot.isFriend(player.getName())) {
						
					}
					else {
						Output.simpleError(player, "Cannot break blocks here, "+plot.getName()+" is protected.");
						event.setCancelled(true);
						return;
					}
				}
			}
		}
		
		if(block.getType().equals(Material.STONE)) {
			int curSkill = pseudoPlayer.getSkill("mining");
			double percent = (double)curSkill / 1000.0;
			
			// If we are not capped, see if we gained skill
			if(curSkill < 1000) {
				double chance = .2;
				double rand = Math.random();
				if(rand < chance) {
					Mining.possibleSkillGain(event.getPlayer(), pseudoPlayer);
				}
			}
			
			
			double chanceOfRedstone = .02*percent; // 1:50
			if(Math.random() < chanceOfRedstone) {
				if(.2 > Math.random())
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.REDSTONE_ORE.getId(), 1));
				else
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.REDSTONE.getId(), 1));
			}
			
			double chanceOfLapis = .02*percent; // 1:50
			if(Math.random() < chanceOfLapis) {
				if(.2 > Math.random())
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.LAPIS_BLOCK.getId(), 1));
				else
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.LAPIS_ORE.getId(), 1));
			}
			
			/*double chanceOfNetherrack = .001*percent; // 1:50
			if(Math.random() < chanceOfNetherrack) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.NETHERRACK.getId(), 1));
			}*/
			
			double chanceOfGold = .01*percent; // 1:100
			if(Math.random() < chanceOfGold) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_ORE.getId(), 1));
			}
			
			double chanceOfObsidian = .005*percent; // 1:200
			if(Math.random() < chanceOfObsidian) {
				if(.2 > Math.random())
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.OBSIDIAN.getId(), 1));
			}
			
			double chanceOfDiamond = .005*percent; // 1:200
			if(Math.random() < chanceOfDiamond) {
				if(.2 > Math.random())
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.DIAMOND_ORE.getId(), 1));
				else
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.DIAMOND.getId(), 1));
			}
			
			double chanceOfIron = .02*percent; // 1:50
			if(Math.random() < chanceOfIron) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_ORE.getId(), 1));
			}
			
			double chanceOfCoal = .05*percent; // 1:20
			if(Math.random() < chanceOfCoal) {
				if(.2 > Math.random())
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.COAL_ORE.getId(), 1));
				else
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.COAL.getId(), 1));
			}
		}
		
		// If we get to here, we are not trying to break a protected block
		
		// See if the block that was broken was obsidian
		/*if(block.getType().equals(Material.OBSIDIAN)) {
			Block otherBlock;
			if(block.getWorld().equals(RPG._normalWorld))
				otherBlock = RPG._netherWorld.getBlockAt(block.getX(), block.getY(), block.getZ());
			else
				otherBlock = RPG._normalWorld.getBlockAt(block.getX(), block.getY(), block.getZ());
			if(otherBlock.getType().equals(Material.OBSIDIAN))
				otherBlock.setType(Material.AIR);
		}*/
		
		if(block.getType().equals(Material.LEAVES)) {
			int survSkill = pseudoPlayer.getSkill("survivalism");
			double chanceToDropApple = (double)survSkill/4000;
			double chanceToDropGoldenApple = (double)survSkill/400000;
			double rand = Math.random();
			//00System.out.println("Survivalism skill: " + survSkill+", chanceToDropApple: " + chanceToDropApple+", chanceToDropGoldenApple: " + chanceToDropGoldenApple +", rand: " + rand);
			if(rand < chanceToDropGoldenApple) {
				event.getPlayer().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLDEN_APPLE, 1));
			}
			else if(rand < chanceToDropApple) {
				double r = Math.random();
				if(r < .25)
					event.getPlayer().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.APPLE, 1));
				else if(r < .50)
					event.getPlayer().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.LEAVES, 1));
				else if(r < .75)
					event.getPlayer().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.STICK, 1));
				else
					event.getPlayer().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.INK_SACK, 1, (short)3));
			}
			/*if(survSkill < 1000) {
				if(Math.random() < .015)
					Survivalism.possibleSkillGain(event.getPlayer(), pseudoPlayer);
			}*/
		}
		
		if(block.getType().equals(Material.LOG)) {
			if(!RPG._placedLogStrings.contains(block.toString())) {
				int lumberSkill = pseudoPlayer.getSkill("lumberjacking");
				double chanceToDropPlank = (double)lumberSkill/1000;
				if(Math.random() < chanceToDropPlank)
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.WOOD, 1));
				if(Math.random() < chanceToDropPlank)
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.WOOD, 1));
				if(Math.random() < chanceToDropPlank)
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.WOOD, 1));
				if(Math.random() < chanceToDropPlank)
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.WOOD, 1));
				Lumberjacking.possibleSkillGain(event.getPlayer(), pseudoPlayer, null);
			}
		}
		
		//if(block.getType().equals(Material.WEB)) {
			//String creatorName = Magery.findCreatorNameOfBlockAt(block.getX(), block.getY(), block.getZ());
			//if(creatorName != null) {
			//	block.setType(Material.AIR);
			//	event.setCancelled(true);
			//}
			/*ArrayList<MagicStructure> magicStructures = Magery.getMagicStructures();
			for(MagicStructure mS : magicStructures) {
				if(mS instanceof WebTrap) {
					mS.
				}
			}*/
		//}
    	
    }
    
    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
    	/*if(RPG._debug)
    		System.out.println("Block Flow");*/
    	
    	// Don't allow water to knock out web, turns to string when it does
    	if(event.getToBlock().getType().equals(Material.WEB))
    		event.setCancelled(true);
    	
	    if(!event.getToBlock().getWorld().equals(RPG._netherWorld)) {
		    Block toBlock = event.getToBlock();
		    Plot toPlot = PlotHandler.findPlotAt(toBlock.getLocation());
		    if(toPlot != null) {
		    	if(toPlot.isProtected()) {
			    	Block fromBlock = event.getBlock();
			    	Plot fromPlot = PlotHandler.findPlotAt(fromBlock.getLocation());
			    	if(fromPlot != toPlot) {
				    	// flowing from plot A to plot B
				    	event.setCancelled(true);
			    	}
		    	}
	    	}
    	}
    }
    
    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
    	if(RPG._debugV)
    		System.out.println("Block Ignite");
    	
    	if(event.getCause().equals(IgniteCause.LIGHTNING)) {
    		event.setCancelled(true);
    		return;
    	}

    	if(!event.getCause().equals(IgniteCause.FLINT_AND_STEEL)) {
    		event.setCancelled(true);
    		return;
    	}
    	
    	/*if(event.getCause().equals(IgniteCause.SPREAD) || event.getCause().equals(IgniteCause.LAVA)) {
    		Block ignitedBlock = event.getBlock();
    		Plot plot = PlotHandler.findPlotAt(ignitedBlock.getLocation());
    		if(plot != null && plot.isProtected()) {*/
    			//System.out.println("CLEARING FIRE");
    			/*for(int x=ignitedBlock.getX() - 5; x<= ignitedBlock.getX()+5; x++) {
    				for(int y=ignitedBlock.getY() - 5; y<= ignitedBlock.getY()+5; y++) {
    					for(int z=ignitedBlock.getZ() - 5; z<= ignitedBlock.getZ()+5; z++) {
    	    				Block b = ignitedBlock.getWorld().getBlockAt(x,y,z);
    	    				if(b.getType().equals(Material.FIRE)) {
    	    					b.setType(Material.AIR);
    	    				}
    	    			}
        			}
    			}*/
    			/*event.setCancelled(true);
    			return;
    		}
    	}*/
    	if(event.getCause().equals(IgniteCause.FLINT_AND_STEEL)) {
    		Player player = event.getPlayer();
	    	Block ignitedBlock = event.getBlock();
	    	Plot plot = PlotHandler.findPlotAt(ignitedBlock.getLocation());
	    	if(plot != null) {
	    		if(plot.isProtected()) {
	    			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
	    			if((pseudoPlayer._plottest == plot) || ( !plot.isCoOwner(player.getName()) && !plot.isOwner(player.getName()) )) {
	    				Output.simpleError(player, "Cannot set fire here, "+plot.getName()+" is protected.");
	    				event.setCancelled(true);
	    				return;
	    			}
	    		}
	    	}
    	}
    }
    
    /*public boolean portalMatch(ArrayList<Block> blocks) {
    	boolean matching = true;
		// make sure the shape of the obsidian matches
		for(int i=0; i<10; i++) {
			Block b = blocks.get(i);
			if(!b.getType().equals(Material.OBSIDIAN)) {	matching = false; break; }
		}
		if(matching) {
			// see if the portal is empty
    		for(int i=10; i<16; i++) { 
    			Block b = blocks.get(i);
    			if(!b.getType().equals(Material.AIR)) {	matching = false; break; }
    		}
		}
		if(matching)
			return true;
		return false;
    }*/
    
    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
    	/*if(RPG._debug)
    		System.out.println("Block Physics");*/
    	if(event.getBlock().getType().equals(Material.PORTAL)) {
    		event.setCancelled(true);
    	}
    	
    	/*if(event.getBlock().getType().equals(Material.PORTAL)) {
    		if(!RPG._portalPhysics) {
    			event.setCancelled(true);
    		}
    		else {
    			if(event.getBlock().getType().equals(Material.PORTAL)) {
    	    		int numMagicStructures = Magery.getMagicStructures().size();
    	    		for(int i=numMagicStructures-1; i>=0; i--) {
    	    			MagicStructure ms = Magery.getMagicStructures().get(i);
    	    			if(ms instanceof Gate) {
    	    				Gate gate = (Gate)ms;
    	    				if(gate.getSourceBlock().equals(event.getBlock()) || gate.getDestBlock().equals(event.getBlock())) {
    	    					gate.cleanUp();
    	    					if(gate instanceof PermanentGate) {
    	    						PermanentGate pGate = (PermanentGate)gate;
    	    						Database.removePermanentGate(pGate.getId());
    	    					}
    	    				}
    	    			}
    	    		}
    	    	}
    		}
    	}*/
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
    	if(RPG._debugV)
    		System.out.println("Block Place");    	
        Block block = event.getBlockPlaced();
        Player player = event.getPlayer();
        if(!player.isOp()) {
        	/*if(block.getTypeId() == 29 || block.getTypeId() == 33) {
        		Output.simpleError(player, "Cannot place pistons because of an exploit, waiting on Notch fix.");
        		event.setCancelled(true);
        		return;
        	}*/
	        if((block.getTypeId() == 29 || block.getTypeId() == 33)) {// || block.getType().equals(Material.REDSTONE_TORCH_OFF) || block.getType().equals(Material.REDSTONE_TORCH_ON) || block.getType().equals(Material.REDSTONE_ORE) || block.getType().equals(Material.GLOWING_REDSTONE_ORE) || block.getType().equals(Material.DETECTOR_RAIL) || block.getType().equals(Material.DIODE) || block.getType().equals(Material.DIODE_BLOCK_OFF) || block.getType().equals(Material.DIODE_BLOCK_ON) || block.getType().equals(Material.REDSTONE_WIRE) || block.getType().equals(Material.REDSTONE) || block.getType().equals(Material.STONE_BUTTON) || block.getType().equals(Material.LEVER) || block.getType().equals(Material.STONE_PLATE) || block.getType().equals(Material.WOOD_PLATE))) {
	        	System.out.println(player.getName()+" placed a piston at "+player.getLocation().toString());
	        	ArrayList<Plot> plots = PlotHandler.getPlots();
	        	for(Plot plot : plots) {
	        		if(Utils.isWithin(plot.getLocation(), block.getLocation(), plot.getRadius()+20)) {
	        			if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {}
	        			else {
		        			event.setCancelled(true);
		        			Output.simpleError(player, "You cannot place that here, it is too close to "+plot.getName()+".");
		        			return;
	        			}
	        		}
	        	}
	        	System.out.println(player.getName()+" placed a piston at "+player.getLocation().toString());
	        }
        }
        
        if(block.getType().equals(Material.LOG)) {
        	RPG._placedLogStrings.add(block.toString());
        }
        
        
        
        Plot plot = PlotHandler.findPlotAt(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ()));
        if(plot != null) {
	        if(plot.isProtected()) {
	       		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
	       		if((pseudoPlayer._plottest == plot) || ( !plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName()) )) {
					if(plot.isFriendBuild() && plot.isFriend(player.getName())) {
											
					}
					else {
			       		Output.simpleError(player, "Cannot place blocks here, "+plot.getName()+" is protected.");
						event.setCancelled(true);
					}
        		}	        	
	       	}
        }
    }
}

