package com.lostshard.RPG.Listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import me.neodork.npclib.entity.HumanNPC;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R1.block.CraftChest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import com.ensifera.animosity.craftirc.RelayedMessage;
import com.lostshard.RPG.ChatHandler;
import com.lostshard.RPG.HelpHandler;
import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.RPG;
import com.lostshard.RPG.RandChest;
import com.lostshard.RPG.ScrollHandler;
import com.lostshard.RPG.Trade;
import com.lostshard.RPG.CustomContent.CustomHealingItems;
import com.lostshard.RPG.CustomContent.CustomHealingItems.FoodType;
import com.lostshard.RPG.Events.*;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Groups.Clans.Clan;
import com.lostshard.RPG.Groups.Clans.ClanHandler;
import com.lostshard.RPG.Groups.Parties.Party;
import com.lostshard.RPG.Groups.Parties.PartyHandler;
import com.lostshard.RPG.Plots.Bank;
import com.lostshard.RPG.Plots.BankBox;
import com.lostshard.RPG.Plots.NPCHandler;
import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Plots.PlotHandler;
import com.lostshard.RPG.Plots.PlotNPC;
import com.lostshard.RPG.Plots.Store;
import com.lostshard.RPG.Skills.*;
import com.lostshard.RPG.Spells.Gate;
import com.lostshard.RPG.Spells.MagicStructure;
import com.lostshard.RPG.Spells.PermanentGate;
import com.lostshard.RPG.Spells.Spell;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;


/**
 * Handle events for all Player related events
 * @author luciddream
 */
public class RPGPlayerListener implements Listener {
    private final RPG plugin;
    private static final int LOCKING_TICKS = 100;
    private static final int UNLOCKING_TICKS = 100;
    private int SOFT_MAX_PLAYERS = 50;
    private int HARD_MAX_PLAYERS = 60;
    
    public RPGPlayerListener(RPG instance) {
    	plugin = instance;
    	plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {    	
    	if(event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
    		System.out.println("Caughtfish");
    		Fishing.caughtFish(event);
    	}
    	else if(event.getState().equals(PlayerFishEvent.State.IN_GROUND)) {
    		if(event.getCaught() instanceof Block) {
    			System.out.println("Caughtblock");
    		}
    	}
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
    	byte[] iparray = event.getAddress().getAddress();
    	try {
    		byte firstBlock = iparray[0];
    		byte secondBlock = iparray[1];
    		
    		if((firstBlock == 96) && (secondBlock == 3)) {
    			Player player = event.getPlayer();
    			Output.chatAdmin(player, "I'm probably semir! *Semir Notifier v1.0*");
    			Output.sendToAdminIRC(player, "Semir Notifier v1.0", "I'm probably semir!");
    		}
    	}
    	catch(Exception e) {
    	
    	}
    	if(RPG._canLogin) {
	    	Player player = event.getPlayer();
	    	/*if(!player.isOp()) {
	    		event.disallow(Result.KICK_OTHER, "We're getting ready for the wipe, We'll post on the forum when we're ready.");
	    		return;
	    	}*/
	    	if(Utils.getPlugin().getServer().getOnlinePlayers().length >= SOFT_MAX_PLAYERS) {
	    		if(!Database.isPlayerPremium(player.getName())) {
	    			//event.setKickMessage("Non-Subscriber slots are full. (max "+SOFT_MAX_PLAYERS+")");
	    			event.disallow(Result.KICK_OTHER, "Non-Subscriber slots are full. (max "+SOFT_MAX_PLAYERS+")");
	    		}
	    		else {
	    			if(Utils.getPlugin().getServer().getOnlinePlayers().length >= HARD_MAX_PLAYERS) {
	    				//event.setKickMessage("Premium slots are full, oops.");
	        			event.disallow(Result.KICK_OTHER, "Subscriber slots are full, I'm sorry =( Let luciddream know.");
	    			}
	    		}
	    	}
    	}
    	else {
    		event.disallow(Result.KICK_OTHER, "Can't login right now, server is probably in the process of rebooting.");
    	}
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	if(RPG._debugV)
    		System.out.println("Player Join");
    	
    	Player player = event.getPlayer();
    	try {
    		PseudoPlayer pseudoPlayer;
    		boolean isNew = false;
	        if(Database.doesPlayerDataExist(player.getName())) {
	        	System.out.println("Existing Player Joined: "+player.getName()+", Total players: " + Utils.getPlugin().getServer().getOnlinePlayers().length);
	        	pseudoPlayer = Database.createPseudoPlayer(player.getName());
	        }
	        else {
	        	System.out.println("New Player Joined: "+player.getName()+", Total players: " + Utils.getPlugin().getServer().getOnlinePlayers().length);
	        	Output.chatAdmin(player, "has just joined the server for the first time.");
	        	Database.addNewPlayer(player.getName());
	        	pseudoPlayer = Database.createPseudoPlayer(player.getName());
	        	isNew = true;
	        	
	        	//player.teleport(RPG._tutorialLocation);
	        }
	        
	        //ContribPlayer cPlayer = (ContribPlayer)player;
	        //cPlayer.
	        
	        player.sendMessage(ChatColor.GOLD+"Welcome to Lost Shard, www.lostshard.com for more info.");
	        player.sendMessage(ChatColor.GOLD+"The guide has valuable information for getting started.");
	        player.sendMessage(ChatColor.GOLD+"Use /rules for rules and /help for help");
	        player.sendMessage(ChatColor.GOLD+"Visit forums.lostshard.com for more info");
	        player.sendMessage(ChatColor.RED+"Updated bukkit");
	        player.sendMessage(ChatColor.YELLOW+"Re-enabled anvils, enchantment tables & enderchests");
	        //player.sendMessage(ChatColor.RED+"-Map wipe is October 12, See forums for details.-");
	        /*player.sendMessage(ChatColor.YELLOW+"Recent Updates (See forums.lostshard.com for more info):");
	        player.sendMessage(ChatColor.YELLOW+"-Updated to 1.3 server, post on the forum if something is broken");
	        player.sendMessage(ChatColor.YELLOW+"-Temporarily disabled ender chests just in case");
	        player.sendMessage(ChatColor.YELLOW+"-Temporarily disabled villager trading");*/
	        /*player.sendMessage(ChatColor.YELLOW+"-[changed]Added small piercing to all swords at 100 skill, .5h for iron, 1h for diamond, 2h for gold, let me know how this works out, its sorta a test.");
	        player.sendMessage(ChatColor.YELLOW+"-Don't use potions to avoid murder counts/criminal, its an exploit");
	        player.sendMessage(ChatColor.YELLOW+"-Reduced mana cost on slow field to 15");
	        player.sendMessage(ChatColor.YELLOW+"-Repair cost for diamond armor changed to 3 iron instead of 1 diamond");
	        player.sendMessage(ChatColor.YELLOW+"-Increased mana cost on heal self to 35 from 25.");
	        player.sendMessage(ChatColor.YELLOW+"-Disabled Ender Pearls temporarily");*/
	        //player.sendMessage(ChatColor.YELLOW+"-(fixed)Disabled non-co-owners using bonemeal on protected plots");

	        player.sendMessage(ChatColor.RED+"-Combat logging drops your items on logout.");
	        
	        // last thing it does is adds the new pseudoplayer to the handler
	        String clanName = pseudoPlayer.getClanName();
	        //Clan clan = ClanHandler.findClanByPlayer(player);
	        if(!clanName.equals("") && !clanName.equals(" ")) {
	        	Clan clan = ClanHandler.findClanByHashmap(clanName);
	        	if(clan != null) {
		        	if(clan.isOwner(player.getName()) || clan.isLeader(player.getName()) || clan.isMember(player.getName()))
		        		pseudoPlayer.setClan(clan);
		        	else {
		        		Output.simpleError(player, "You have been ejected from "+clanName+" while you were offline.");
		        	}
	        	}
	        	else
	        		Output.simpleError(player, "Your clan has been disbanded.");
	        }
	        ArrayList<String> offlineMessages = Database.getOfflineMessages(player.getName());
	        for(String msg : offlineMessages) {
	        	player.sendMessage("["+ChatColor.GOLD+"MSG"+ChatColor.WHITE+"] "+msg);
	        }
	        PseudoPlayerHandler.add(player.getName(), pseudoPlayer);
	        Database.updatePlayer(player.getName());
	        
	        pseudoPlayer._kickOutTicks = 30;
	        
	        if(pseudoPlayer.isMurderer())
	        	player.setDisplayName(ChatColor.RED+player.getName());
	        else if(pseudoPlayer.isCriminal())
	        	player.setDisplayName(ChatColor.GRAY+player.getName());
	        else
	        	player.setDisplayName(ChatColor.BLUE+player.getName());
	        
	        Utils.setPlayerTitle(player);
	        //SpoutManager.getAppearanceManager().setGlobalTitle(player, player.getDisplayName());
	        //BukkitContrib.getAppearanceManager().setGlobalTitle(player, player.getDisplayName());
	        
	        pseudoPlayer._respawnTicks = 0;
	        pseudoPlayer._loggedInRecentlyTicks = 100;
	        pseudoPlayer._noTalkTicks = 10;
	        /*if(pseudoPlayer._dieLog > 0) {
	        	Date date = new Date();
	        	long time = date.getTime();
	        	long lastLogout = pseudoPlayer._lastLogout;
	        	long diff = time-lastLogout;
	        	diff/=1000;
	        	// diff is now in seconds
	        	if(pseudoPlayer._dieLog == 2 || diff > 30) {
	        		String message = "You logged out during combat at some point in the past, as a result you will be teleported to ";
	        		
	        		if(pseudoPlayer.isCriminal())
	        			message += "Order's spawn point and promptly executed.";
	        		else
	        			message += "Chaos' spawn point and promptly executed.";
	        		
	        		pseudoPlayer._punishTicks = 81;
	        		pseudoPlayer._dieLog = 2;
	        		Output.simpleError(player, message);
	        			
	        	}
	        	
	        	if(pseudoPlayer._dieLog == 1) {
		        	System.out.println("Time between: " + diff);
		        	player.sendMessage(ChatColor.RED+"You logged out during pvp. If you do it again you will be punished.");
		        	pseudoPlayer._dieLog = 0;
		        	pseudoPlayer._lastChanceTicks = 60;
	        	}
	        }*/
	        
	        // godmode for ops/admins
	        if(player.isOp()) {
	        	pseudoPlayer._convenient = true;
	        	pseudoPlayer._secret = true;
	        	//pseudoPlayer._vanished = true;
	        	Output.positiveMessage(player, "Secret: On, affects: /who, /msg");
	        	pseudoPlayer._ignoreDelay = true;
	        	player.chat("/vanish");
	        	player.chat("/nopickup");
	        	pseudoPlayer._ignoreDelay = false;
	        	//pseudoPlayer._justLoggedIn = true;
	        	event.setJoinMessage(null);
	        }
	        
	        //point compass north
	        Location loc = player.getLocation();
	        player.setCompassTarget(new Location(player.getWorld(), loc.getX(), loc.getY(),loc.getZ()-20000));
	        if(isNew) {
	        	player.teleport(RPG._tutorialLocation);
	        }
	        
	        pseudoPlayer.setMaxMana(Meditation.getMaxMana(pseudoPlayer));
	        
	        Location lastLoginLocation = pseudoPlayer.getLastLogoutLocation();
	        if(lastLoginLocation != null) {
		        if(Utils.distance(lastLoginLocation, player.getLocation()) > 2) {
		        	//if(!Spell.getInvisibleBlocks().contains(lastLoginLocation.getBlock().getType().getId())) {
			        	//Output.simpleError(player, "THATS STRANGE...");
			        	//Location highestYLoc = player.getLocation();
			        	//highestYLoc.setY(highestYLoc.getWorld().getHighestBlockYAt(highestYLoc));
			        	//player.teleport(highestYLoc);
			        	Output.sendToAdminIRC(player, "UNLIKELYACHEATER", player.getLocation()+" This jerk probably didn't just try to use that go up through blocks plot exploit, but its possible.");
		        	//}
		        }
	        }
	        
	        Clan clan = pseudoPlayer.getClan();
	        if(clan != null) {
	        	pseudoPlayer._maxMana = clan._maxMana;
	        	pseudoPlayer._maxStamina = clan._maxStamina;
	        }
    	}
    	catch(Exception e) {
    		System.out.println(e.toString());
    		System.out.println("PseudoPlayer retrieval error, kicking: "+player.getName());
    		player.kickPlayer("Server Error(1): Something went wrong, we're probably working on it.");
    	}
    	
    }
    
    private int _dontTryKickTicks = 0;
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
    	/*if(event.getPlayer().isOp()) {
    		event.setCancelled(true);
    		return;
    	}*/
    	
    	if(event.getReason().equalsIgnoreCase("Flying is not enabled on this server")) {
    		if(_dontTryKickTicks > 0) {
    			_dontTryKickTicks--;
    			event.setCancelled(true);
    		}
    		else {
	    		Player player = event.getPlayer();
	    		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
	    		if(pseudoPlayer._flyTicks > 0) {
	    			event.setCancelled(true);
	    			_dontTryKickTicks = 10;
	    			System.out.println("Cancelled Kick");
	    		}
	    		else {
	    			RPG.craftircHandle.sendMsgToTargetViaBot("[AUTO-REPORT] "+event.getPlayer().getName()+" kicked for flying.", "synirclostshard-admin", 0);
	    		}
    		}
    	}
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    	if(RPG._debugV)
    		System.out.println("Player Quit");
    	
    	Player player = event.getPlayer();
    	
    	System.out.println(player.getName()+" logged out at "+player.getLocation().toString());
    	
        PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
        if(pseudoPlayer == null)
        	return;
        
        if(pseudoPlayer._secret) {
        	event.setQuitMessage(null);
        }
        
        /*if(pseudoPlayer._pvpTicks > 0) {
        	if(player.getHealth() > 0) {
	        	Output.chatGlobal(player, "I logged out during PvP, when I log back in I'll die!");
	        	pseudoPlayer._dieLog = 1;
	        	player.damage(2000);
        	}
        }*/
        
        Party party = pseudoPlayer.getParty();
        if(party != null) {
        	party.removeMember(player.getName());
        	party.sendMessage(player.getName()+" has left the party.");
        	ArrayList<String> partyMemberNames = party.getPartyMemberNames();
        	if(partyMemberNames.size() == 1) {
        		Player lastPartyMember = Utils.getPlugin().getServer().getPlayer(partyMemberNames.get(0));
        		if(lastPartyMember != null) {
        			PartyHandler.partyLeave(lastPartyMember);
        			Output.positiveMessage(lastPartyMember, "You are the last person in your party so it has been disbanded.");
        		}
        	}
        }
        
        /*if(pseudoPlayer._respawnTicks > 0)
        	player.getServer().broadcastMessage(player.getDisplayName()+ChatColor.WHITE+" rage quit.");*/
        
        //if(pseudoPlayer._lastChanceTicks > 0) {
        if(pseudoPlayer._pvpTicks > 0 && !RPG._shuttingDown) {	
        	player.getServer().broadcastMessage(player.getDisplayName()+ChatColor.WHITE+" logged out while in combat and was punished.");
        	// Drop inventory
        	ItemStack[] itemStacks = player.getInventory().getContents();
        	for(ItemStack itemStack : itemStacks) {
        		if(itemStack == null)
        			continue;
        		player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        	}
        	// Drop armor
        	itemStacks = player.getInventory().getArmorContents();
        	for(ItemStack itemStack : itemStacks) {
        		if(itemStack == null || itemStack.getType().equals(Material.AIR))
        			continue;
        		player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        	}
        	
        	//clear the inventory
        	player.getInventory().setHelmet(new ItemStack(Material.AIR, 0));
        	player.getInventory().setChestplate(new ItemStack(Material.AIR, 0));
        	player.getInventory().setLeggings(new ItemStack(Material.AIR, 0));
        	player.getInventory().setBoots(new ItemStack(Material.AIR, 0));
        	player.getInventory().clear();
        	
        	pseudoPlayer._dieLog = 0;
        }
        
        if(pseudoPlayer._claiming) {
            ArrayList<Plot> controlPoints = PlotHandler.getControlPoints();
            for(Plot cP : controlPoints) {
            	if(cP._capturingPlayerName.equals(player.getName())) {
            		cP.failCaptureLeft(player);
            	}
            }
        }
        
        Date date = new Date();
        pseudoPlayer._lastLogout = date.getTime();
        
        Database.updateBank(player, pseudoPlayer);
        Database.updatePlayer(player.getName());
        pseudoPlayer.setLastLogoutLocation(player.getLocation());
        Database.updateLastLogoutLocation(pseudoPlayer);
        //if(pseudoPlayer._vanished)
        	//player.chat("/vanish");
        PseudoPlayerHandler.removed(player.getName());
        
        System.out.println("Player Left: " + player.getName()+" Remaining Players: "+(Utils.getPlugin().getServer().getOnlinePlayers().length-1));
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
    	if(RPG._debugV)
    		System.out.println("Player Teleport");
    	
    	
    	Player player = event.getPlayer();	
    	PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    	if(pseudoPlayer == null)
    		return;
    	if(pseudoPlayer.isMurderer())
    		player.setDisplayName(ChatColor.RED+player.getName());
    	else if(pseudoPlayer.isCriminal())
    		player.setDisplayName(ChatColor.GRAY+player.getName());
    	else
    		player.setDisplayName(ChatColor.BLUE+player.getName());
    	
    	Utils.setPlayerTitle(player);
  
    	//SpoutManager.getAppearanceManager().setGlobalTitle(player, player.getDisplayName());
    	//BukkitContrib.getAppearanceManager().setGlobalTitle(player, player.getDisplayName());
    	
    	/*if(!Utils.isWithin(event.getFrom(), event.getTo(), 200)) {
    		pseudoPlayer._hideTicks = 31;
    	}*/
    	/*Player[] onlinePlayers = Utils.getPlugin().getServer().getOnlinePlayers();
		for(Player p : onlinePlayers) {
			if(p != player) {
				//if(Utils.isWithin(p.getLocation(), player.getLocation(), 20)) {
					CraftPlayer unHide = (CraftPlayer) player;
					CraftPlayer unHideFrom = (CraftPlayer) p;
					unHideFrom.getHandle().a.b(new Packet20NamedEntitySpawn(unHide.getHandle()));
				//}
			}
		}*/
    }
    
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
    	if(RPG._debugV)
    		System.out.println("Player Chat");
    	
    	Player player = event.getPlayer();
    	
    	if(event.getMessage().contains("§")) {
    		Output.simpleError(player, "Nuh uh uh, you didn't say the magic word! (Note: There is no magic word.");
    		event.setCancelled(true);
    		return;
    	}
    	
    	PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    	
    	if(pseudoPlayer._noTalkTicks > 0) {
    		event.setCancelled(true);
    		return;
    	}
    	
    	pseudoPlayer._recentChatMessages++;
    	if(pseudoPlayer._recentChatMessages >= 5) {
    		Output.simpleError(player, "Chatting too fast, wait some time before chatting again.");
    	}
    	/*if(pseudoPlayer._noCommandTicks > 0) {
    		Output.simpleError(player, "Cannot send another message so quickly.");
    		event.setCancelled(true);
    		return;
    	}*/
    	
    	pseudoPlayer._awayTicks = 6000;
    	
    	if(pseudoPlayer.getPromptedSpell() == null) {    		
    		if(pseudoPlayer._addingSaleItem != null) {
        		// break out if the player cancels
        		if(event.getMessage().equalsIgnoreCase("cancel")) {
        			pseudoPlayer._addingSaleItem = null;
        			pseudoPlayer._addingSaleStore = null;
        			Output.simpleError(player, "You have cancelled adding the item to the vendor.");
        			event.setCancelled(true);
        			return;
        		}
        		
        		int costPerUnit = -1;
        		try{ costPerUnit = Integer.parseInt(event.getMessage()); }
        		catch(Exception e) {}
        		
        		// Break out if the player enters an invalid amount
        		if(costPerUnit <= 0) {
        			pseudoPlayer._addingSaleItem = null;
        			pseudoPlayer._addingSaleStore = null;
        			Output.simpleError(player, "Invalid price per unit, must be greater than 1.");
        			event.setCancelled(true);
        			return;
        		}
        		
        		Store store = pseudoPlayer._addingSaleStore;
        		ItemStack itemAdding = pseudoPlayer._addingSaleItem;
        		ItemStack itemInHand = player.getItemInHand();
        		if(itemInHand.getType().equals(itemAdding.getType()) && itemInHand.getAmount() == itemAdding.getAmount()) {
        			store.addItem(itemInHand.getTypeId(), itemInHand.getAmount(), itemInHand.getDurability(), costPerUnit, 0);
        			player.getInventory().clear(player.getInventory().getHeldItemSlot());
        			Database.updateStore(store);
        			Output.positiveMessage(player, "You have added "+itemAdding.getAmount()+" "+itemAdding.getType().name()+" to the vendor for "+costPerUnit+".");
        		}
        		else Output.simpleError(player, "You are no longer holding the item you are attempting to sell, cancelled.");
        		
        		pseudoPlayer._addingSaleItem = null;
        		pseudoPlayer._addingSaleStore = null;
        		event.setCancelled(true);
        		return;
        	}
    		else {
    			int chatChannel = pseudoPlayer._activeChannel;
    			if(pseudoPlayer._otherChatChannel != -1) {
    				chatChannel = pseudoPlayer._otherChatChannel;
    			}
    			if(chatChannel == ChatHandler.CHAT_CHANNEL_GLOBAL)
    				Output.chatGlobal(player, event.getMessage());
    			else if(chatChannel == ChatHandler.CHAT_CHANNEL_LOCAL)
    				Output.chatLocal(player, event.getMessage());
    			else if(chatChannel == ChatHandler.CHAT_CHANNEL_SHOUT)
    				Output.chatShout(player, event.getMessage());
    			else if(chatChannel == ChatHandler.CHAT_CHANNEL_PARTY)
    				ChatHandler.chatParty(player, event.getMessage());
    			else if(chatChannel == ChatHandler.CHAT_CHANNEL_CLAN)
    				ChatHandler.chatClan(player, event.getMessage());
    			else if(chatChannel == ChatHandler.CHAT_CHANNEL_WHISPER)
    				Output.chatWhisper(player, event.getMessage());
    			else if(chatChannel == ChatHandler.CHAT_CHANNEL_ADMIN)
    				Output.chatAdmin(player, event.getMessage());
    			else if(chatChannel == ChatHandler.CHAT_CHANNEL_MSG) {
    				String message = event.getMessage();
    				if(pseudoPlayer._msgPlayer != null) {
	    				Player targetPlayer = Bukkit.getPlayer(pseudoPlayer._msgPlayer);
	    				if(targetPlayer != null) {
	    					PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
	    					if(targetPseudoPlayer._secret) {
	    						Output.simpleError(player, pseudoPlayer._msgPlayer+" is not currently online.");
	    					}
	    					else if(!targetPseudoPlayer.isIgnoring(player.getName())) {
	    						String msg = message;
	    						targetPlayer.sendMessage("["+ChatColor.LIGHT_PURPLE+"MSG"+ChatColor.WHITE+"] "+player.getName()+": "+msg);
	    						player.sendMessage("["+ChatColor.LIGHT_PURPLE+"MSG to "+targetPlayer.getName()+ChatColor.WHITE+"] "+msg);
	    						targetPseudoPlayer._lastPlayerNameMsg = player.getName();
	    						Output.sendToAdminIRC(player, "MSG to "+targetPlayer.getName(), msg);
	    					}
	    				}
	    				else Output.simpleError(player, pseudoPlayer._msgPlayer+" is not currently online.");
    				}
    			}
        		// if the player says guards
        		if(event.getMessage().toUpperCase().contains("GUARD")) {
        			guards(player, pseudoPlayer, event.getMessage());
        		}
        		
        		if(event.getMessage().toUpperCase().contains("I WISH TO ATONE FOR MY SINS")) {
        			if(Utils.distance(player.getLocation(), RPG._murderShrineLocation) < 10) {
        				int counts = pseudoPlayer.getMurderCounts();
        				int costPerMurder = RPG.COST_PER_MURDER;
        				
        				if(counts < 10) {
        					Output.simpleError(player, "You may only reduce your murder counts to 10, you currently have "+counts+".");
        					event.setCancelled(true);
        					return;
        				}
        				
        				int diff = counts - 10;
        				int cost = diff * costPerMurder;
        				
        				if(cost > 25000)
        					cost = 25000;
        				
        				int playerMoney = pseudoPlayer.getMoney();
        				if(playerMoney < cost) {
        					Output.simpleError(player, "You cannot afford the $"+cost+" it would cost to reduce your murder counts to 10.");
        					event.setCancelled(true);
        					return;
        				}
        				
        				pseudoPlayer.setMurderCounts(10);
        				pseudoPlayer.setMoney(pseudoPlayer.getMoney() - cost);
        				Database.updatePlayerByPseudoPlayer(pseudoPlayer);
        				Output.positiveMessage(player, "You have reduced your murder counts to 10.");
        			}
        			else {
        				Output.simpleError(player, "You must be near the Shrine of Atonement to renounce your sins.");
        				event.setCancelled(true);
        				return;
        			}
        		}
    		}
    	}
    	else {
    		String message = event.getMessage();
    		Spell spell = pseudoPlayer.getPromptedSpell();
    		spell.setResponse(message);
    		
    		int castingDelay = spell.getCastingDelay();
			if(castingDelay > 0) {
				spell.preAction(player);
				pseudoPlayer.setDelayedSpell(spell);
				pseudoPlayer.setCastDelayTicks(spell.getCastingDelay());
			}
			else {
				spell.preAction(player);
				spell.doAction(player);
			}
    		pseudoPlayer.setPromptedSpell(null);
    	}
    	
    	pseudoPlayer._noCommandTicks = 4;
    	event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    	if(RPG._debugV)
    		System.out.println("Player Command");
    	
        String[] split = event.getMessage().split(" ");
        Player player = event.getPlayer();
        
        if(event.getMessage().contains("§")) {
    		Output.simpleError(player, "Nuh uh uh, you didn't say the magic word! (Note: There is no magic word.");
    		event.setCancelled(true);
    		return;
    	}
        
        PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
        
        if(!pseudoPlayer._ignoreDelay) {
	        if(pseudoPlayer._noCommandTicks > 0) {
	    		Output.simpleError(player, "Cannot send another message so quickly.");
	    		event.setCancelled(true);
	    		return;
	    	}
        }
        pseudoPlayer._noCommandTicks = 4;
        
        if(RPG._debug) {
        	System.out.println(event.getPlayer().getName()+" used: " + event.getMessage());
        }
        
        if(event.getMessage().toUpperCase().contains("GUARD")) {
			guards(player, pseudoPlayer, event.getMessage());
		}
        
        pseudoPlayer._awayTicks = 6000;
        
        String command = split[0];
        if(command.equalsIgnoreCase("/me")) {
        	Output.simpleError(player, "Unknown command.");
        	event.setCancelled(true);
        	return;
        }
        
        if(command.equalsIgnoreCase("/test")) {
        	System.out.println("lastattackers: " + plugin.entityListener._lastAttackers.size());
        	System.out.println("recentdeaths: " + plugin.entityListener._recentDeaths.size());
        }
        
        if(pseudoPlayer._addingSaleItem != null && (command.equalsIgnoreCase("/l") || command.equalsIgnoreCase("/local"))) {
	        ItemStack itemInHand = player.getItemInHand();
	    	if(itemInHand.getType().equals(pseudoPlayer._addingSaleItem.getType()) && itemInHand.getAmount() == pseudoPlayer._addingSaleItem.getAmount()) {
	    			
	    	}
	    	else Output.simpleError(player, "You are no longer holding the item you are attempting to sell, cancelled.");
	    	
    		pseudoPlayer._addingSaleItem = null;
    		event.setCancelled(true);
    		return;
        }
        
        if(ChatHandler.handleChat(player, split)) {
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/help") || command.equalsIgnoreCase("?")) {
        	HelpHandler.handle(player, split);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/private")) {
        	if(pseudoPlayer.isPrivate()) {
        		Output.simpleError(player, "You are already set to private.");
        	}
        	else {
        		Output.positiveMessage(player, "You have set yourself to private.");
        		pseudoPlayer.setPrivate(true);
        		Database.updatePlayerByPseudoPlayer(pseudoPlayer);
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/public")) {
        	if(!pseudoPlayer.isPrivate()) {
        		Output.simpleError(player, "You are already set to public.");
        	}
        	else {
        		Output.positiveMessage(player, "You have set yourself to public.");
        		pseudoPlayer.setPrivate(false);
        		Database.updatePlayerByPseudoPlayer(pseudoPlayer);
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/resetspawn")) {
        	pseudoPlayer.setCustomSpawn(null);
        	Database.updatePlayerCustomSpawn(pseudoPlayer);
        	if(pseudoPlayer.isMurderer())
        		Output.positiveMessage(player, "You have reset your spawn to Chaos.");
        	else
        		Output.positiveMessage(player, "You have reset your spawn to Order.");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/setsoftplayers") && player.isOp()) {
        	if(split.length >= 2) {
	        	int num = -1;
	        	try {
	        		num = Integer.parseInt(split[1]);
	        	}
	        	catch(Exception e) {}
	        	
	        	if(num > 0) {
	        		SOFT_MAX_PLAYERS = num;
	        		Output.positiveMessage(player, "Set soft max players to "+num+".");
	        	}
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/sethardplayers") && player.isOp()) {
        	if(split.length >= 2) {
	        	int num = -1;
	        	try {
	        		num = Integer.parseInt(split[1]);
	        	}
	        	catch(Exception e) {}
	        	
	        	if(num > 0) {
	        		HARD_MAX_PLAYERS = num;
	        		Output.positiveMessage(player, "Set hard max players to "+num+".");
	        	}
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/dc") && player.isOp()) {
        	if(split.length <= 1) {
        		Output.positiveMessage(player, "~Dungeon Chest Thingle~");
        		Output.positiveMessage(player, "/dc create (minhours) (maxhours)");
        		Output.positiveMessage(player, "/dc remove");
        		Output.positiveMessage(player, "/dc addcontents");
        		Output.positiveMessage(player, "/dc clearcontents");
        		Output.positiveMessage(player, "/dc fill");
        		return;
        	}
        	
        	Block block = player.getTargetBlock(Spell.invisibleBlocks, 10);
        	if(block.getType().equals(Material.CHEST)) {
        		CraftChest chest = new CraftChest(block);
        		Location location = block.getLocation();
	        	if(split[1].equalsIgnoreCase("create")) {
	        		if(split.length >= 4) {
		        		int numRandChests = RPG._randChests.size();
		        		for(int i=0; i<numRandChests; i++) {
		        			RandChest rC = RPG._randChests.get(i);
		        			Location l = rC.getLocation();
		        			if(l.getBlockX() == location.getBlockX() && l.getBlockY() == location.getBlockY() && l.getBlockZ() == location.getBlockZ()) {
		        				// chest already exists
		        				Output.positiveMessage(player, "This chest is already a Dungeon Chest.");
		        				event.setCancelled(true);
		        				return;
		        			}
		        		}
		        		
		        		// Must not have had a rand chest at that location
		        		int numMinHours = Integer.parseInt(split[2]);
		        		int numMaxHours = Integer.parseInt(split[3]);
		        		
		        		int minRand = (((numMinHours * 60) * 60) * 1000);
		        		int maxRand = (((numMaxHours * 60) * 60) * 1000);
		        		
		        		RandChest randChest = new RandChest(block.getLocation(), chest.getInventory().getContents(), minRand, maxRand);
		        		int id = Database.addRandChest(randChest);
		        		randChest.setId(id);
		        		RPG._randChests.add(randChest);
		        		Output.positiveMessage(player, "Dungeon Chest added.");
	        		}
	        		else Output.simpleError(player, "Use /dc create (min hours) (max hours)");
	        	}
	        	else if(split[1].equalsIgnoreCase("settime")) {
	        		if(split.length >= 4) {
	        			int numRandChests = RPG._randChests.size();
		        		for(int i=0; i<numRandChests; i++) {
		        			RandChest rC = RPG._randChests.get(i);
		        			Location l = rC.getLocation();
		        			if(l.getBlockX() == location.getBlockX() && l.getBlockY() == location.getBlockY() && l.getBlockZ() == location.getBlockZ()) {
		        				// chest already exists
		        				int numMinHours = Integer.parseInt(split[2]);
				        		int numMaxHours = Integer.parseInt(split[3]);
				        		int minRand = (((numMinHours * 60) * 60) * 1000);
				        		int maxRand = (((numMaxHours * 60) * 60) * 1000);
				        		rC.setMinRand(minRand);
				        		rC.setMaxRand(maxRand);
				        		rC.updateNextFill();
				        		Database.updateRandChest(rC);
				        		Date date = new Date();
				        		long curTime = date.getTime();
		        				Output.positiveMessage(player, "Chest time updated. Min Hours:"+numMinHours+", Max Hours:"+numMaxHours+", Next fill in roughly"+((rC.getNextFill() - curTime)/60/60/1000)+" hours.");
		        				event.setCancelled(true);
		        				return;
		        			}
		        		}
	        		}
	        	}
	        	else if(split[1].equalsIgnoreCase("remove")) {
	        		int numRandChests = RPG._randChests.size();
	        		for(int i=0; i<numRandChests; i++) {
	        			RandChest rC = RPG._randChests.get(i);
	        			Location l = rC.getLocation();
	        			if(l.getBlockX() == location.getBlockX() && l.getBlockY() == location.getBlockY() && l.getBlockZ() == location.getBlockZ()) {
	        				// chest exists, remove
	        				RPG._randChests.remove(i);
	        				Output.positiveMessage(player, "Removed the Dungeon Chest.");
	        				Database.removeRandChest(rC);
	        				event.setCancelled(true);
	        				return;
	        			}
	        		}
	        		Output.simpleError(player, "That doesn't seem to currently be a Dungeon Chest.");
	        	}
	        	else if(split[1].equalsIgnoreCase("addcontents")) {
	        		int numRandChests = RPG._randChests.size();
	        		for(int i=0; i<numRandChests; i++) {
	        			RandChest rC = RPG._randChests.get(i);
	        			Location l = rC.getLocation();
	        			if(l.getBlockX() == location.getBlockX() && l.getBlockY() == location.getBlockY() && l.getBlockZ() == location.getBlockZ()) {
	        				// chest exists, addcontents
	        				rC.addContents(chest.getInventory().getContents());
	        				Database.updateRandChest(rC);
	        				Output.positiveMessage(player, "Added a set of contents to the Dungeon Chest.");
	        				event.setCancelled(true);
	        				return;
	        			}
	        		}
	        		Output.simpleError(player, "That doesn't seem to currently be a Dungeon Chest.");
	        	}
	        	else if(split[1].equalsIgnoreCase("clearcontents")) {
	        		int numRandChests = RPG._randChests.size();
	        		for(int i=0; i<numRandChests; i++) {
	        			RandChest rC = RPG._randChests.get(i);
	        			Location l = rC.getLocation();
	        			if(l.getBlockX() == location.getBlockX() && l.getBlockY() == location.getBlockY() && l.getBlockZ() == location.getBlockZ()) {
	        				// chest exists, clearcontents
	        				rC.clearContents();
	        				Database.updateRandChest(rC);
	        				Output.positiveMessage(player, "Cleared the contents of the Dungeon Chest.");
	        				event.setCancelled(true);
	        				return;
	        			}
	        		}
	        		Output.simpleError(player, "That doesn't seem to currently be a Dungeon Chest.");
	        	}
	        	else if(split[1].equalsIgnoreCase("fill")) {
	        		try {
		        		int numRandChests = RPG._randChests.size();
		        		for(int i=0; i<numRandChests; i++) {
		        			RandChest rC = RPG._randChests.get(i);
		        			Location l = rC.getLocation();
		        			if(l.getBlockX() == location.getBlockX() && l.getBlockY() == location.getBlockY() && l.getBlockZ() == location.getBlockZ()) {
		        				// chest exists, fill
		        				rC.fill(player, chest);	        				
		        				event.setCancelled(true);
		        				Output.positiveMessage(player, "Filled the the Dungeon Chest with a random content group.");
		        				return;
		        			}
		        		}
		        		Output.simpleError(player, "That doesn't seem to currently be a Dungeon Chest.");
	        		}
	        		catch(Exception e) {e.printStackTrace();}
	        	}
	        	else {
	        		Output.positiveMessage(player, "~Dungeon Chest Thingle~");
	        		Output.positiveMessage(player, "/dc create (minhours) (maxhours)");
	        		Output.positiveMessage(player, "/dc remove");
	        		Output.positiveMessage(player, "/dc addcontents");
	        		Output.positiveMessage(player, "/dc clearcontents");
	        		Output.positiveMessage(player, "/dc fill");
	        	}
        	}
        	else Output.simpleError(player, "Only works on a chest block");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/updateentities") && player.isOp()) {
        	Player[] players = player.getServer().getOnlinePlayers();
        	int numPlayers = players.length;
        	for(int i=0; i<numPlayers; i++) {
        		Player p = players[i];
	        	List<Entity> entities = player.getWorld().getEntities();
	        	int numEntities = entities.size();
	        	Entity foundEntity = null;
	        	for(int j=0; j<numEntities; j++) {
	        		Entity e = entities.get(j);
	        		if(e instanceof Player) {
	        			Player eP = (Player)e;
	        			if(eP.getName().equalsIgnoreCase(p.getName())) {
	        				foundEntity = eP;
	        			}
	        		}
	        	}
	        	if(foundEntity == null) {
	        		System.out.println("No Entity found for "+p.getName());
	        	}
	        	else {
	        		System.out.println("Entity IS found for "+p.getName());
	        	}
        	}
        }
        else if(command.equalsIgnoreCase("/updatelivingentities") && player.isOp()) {
        	Player[] players = player.getServer().getOnlinePlayers();
        	int numPlayers = players.length;
        	for(int i=0; i<numPlayers; i++) {
        		Player p = players[i];
	        	List<LivingEntity> livingEntities = player.getWorld().getLivingEntities();
	        	int numLivingEntities = livingEntities.size();
	        	LivingEntity foundLivingEntity = null;
	        	for(int j=0; j<numLivingEntities; j++) {
	        		LivingEntity lE = livingEntities.get(j);
	        		if(lE instanceof Player) {
	        			Player lEP = (Player)lE;
	        			if(lEP.getName().equalsIgnoreCase(p.getName())) {
	        				foundLivingEntity = lEP;
	        			}
	        		}
	        	}
	        	if(foundLivingEntity == null) {
	        		System.out.println("No LivingEntity found for "+p.getName());
	        	}
	        	else {
	        		System.out.println("LivingEntity IS found for "+p.getName());
	        	}
        	}
        }
        else if(command.equalsIgnoreCase("/ignore") || command.equalsIgnoreCase("/mute")) {
        	if(split.length == 2) {
        		String playerName = split[1];
        		Player targetPlayer = Utils.getPlugin().getServer().getPlayer(playerName);
        		        		
        		if(targetPlayer == null) {
        			Output.simpleError(player, "That player is not online.");
        			event.setCancelled(true);
        			return;
        		}
        		
        		if(targetPlayer.isOp()) {
        			PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
        			if(targetPseudoPlayer._secret)
        				Output.simpleError(player, "That player is not online.");
        			else 
        				Output.simpleError(player, "You cannot ignore an Admin.");
        		}
        		
        		if(targetPlayer.getName().equals(player.getName())) {
        			Output.simpleError(player, "You cannot ignore yourself.");
        			event.setCancelled(true);
        			return;
        		}
        		
        		if(pseudoPlayer.isIgnoring(targetPlayer.getName())) {
    				Output.simpleError(player, "You are already ignoring "+targetPlayer.getName()+".");
    				event.setCancelled(true);
    				return;
    			}
        		
        		pseudoPlayer.addIgnored(targetPlayer.getName());
    			Database.updatePlayerByPseudoPlayer(pseudoPlayer);
    			Output.positiveMessage(player, "You have begun ignoring "+targetPlayer.getName()+".");
        	}
        	else Output.simpleError(player, "Use \"/ignore (player name)\"");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/unignore") || command.equalsIgnoreCase("/unmute")) {
        	if(split.length == 2) {
        		String playerName = split[1];
 
        		if(pseudoPlayer.isIgnoringLoose(playerName)) {
        			pseudoPlayer.removeIgnoredLoose(playerName);
        			Database.updatePlayerByPseudoPlayer(pseudoPlayer);
        			Output.positiveMessage(player, "You are no longer ignoring "+playerName+".");
        		}
        		else Output.simpleError(player, "You are not currenlty ignoring "+playerName+".");
        	}
        	else Output.simpleError(player, "Use \"/unignore (player name)\"");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/ignorelist") || command.equalsIgnoreCase("/mutelist")) {
        	String ignoreListString = ""+ChatColor.YELLOW+"Ignore List: "+ChatColor.WHITE;
        	HashSet<String> ignoreList = pseudoPlayer.getIgnoreList();
            if(ignoreList.size() > 0) {
            	Object[] ignoreListSplit = ignoreList.toArray();
            	int numItems = ignoreListSplit.length;
            	for(int i=0; i<numItems; i++) {
            		String s = (String)ignoreListSplit[i];
            		ignoreListString += s;
            		if(i < numItems-1)
            			ignoreListString += ", ";
            	}
            }
            player.sendMessage(ignoreListString);
        }
        else if(command.equalsIgnoreCase("/list")) {
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/vanish") && player.isOp()) {
        	if(pseudoPlayer._vanished) {
        		pseudoPlayer._vanished = false;
        	}
        	else
        		pseudoPlayer._vanished = true;
        	//event.setCancelled(true);
        }
        /*else if(command.equalsIgnoreCase("/vampire")) {
        	Date dateNow = new Date();
        	long nowTime = dateNow.getTime();
        	long lastChange = pseudoPlayer.getLastTraitChangeTime();
        	long diff = nowTime-lastChange;
        	// if 2 hours have passed 2 hours * 60 minutes * 60 seconds * 1000ms
        	if(!player.isOp() && diff < 2*60*60*1000) {
        		//System.out.println("Diff:"+diff);
        		long secRemaining = (2*60*60) - (diff/1000);
        		
        		long hoursRemaining = secRemaining/3600;
        		long minutesRemaining = (secRemaining - (hoursRemaining*3600))/60;
        		long secondsRemaining = secRemaining%60;
        		Output.simpleError(player, "Cannot change trait, "+hoursRemaining+" hours, "+minutesRemaining+" minutes and "+secondsRemaining+" seconds left.");
        		event.setCancelled(true);
        		return;
        	}
        	
        	pseudoPlayer._traitChangeTicks = 100;
        	pseudoPlayer._newTraitName = "Vampirism";
        	if(pseudoPlayer.isVampire())
        		Output.positiveMessage(player, "Removing vampirism in 10 seconds.");
        	else
        		Output.positiveMessage(player, "Acquiring vampirism in 10 seconds.");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/robot")) {
        	Date dateNow = new Date();
        	long nowTime = dateNow.getTime();
        	long lastChange = pseudoPlayer.getLastTraitChangeTime();
        	long diff = nowTime-lastChange;
        	// if 2 hours have passed 2 hours * 60 minutes * 60 seconds * 1000ms
        	if(!player.isOp() && diff < 2*60*60*1000) {
        		//System.out.println("Diff:"+diff);
        		long secRemaining = (2*60*60) - (diff/1000);
        		
        		long hoursRemaining = secRemaining/3600;
        		long minutesRemaining = (secRemaining - (hoursRemaining*3600))/60;
        		long secondsRemaining = secRemaining%60;
        		Output.simpleError(player, "Cannot change trait, "+hoursRemaining+" hours, "+minutesRemaining+" minutes and "+secondsRemaining+" seconds left.");
        		event.setCancelled(true);
        		return;
        	}
        	
        	pseudoPlayer._traitChangeTicks = 100;
    		pseudoPlayer._newTraitName = "Robotism";
    		if(pseudoPlayer.isRobot())
	    		Output.positiveMessage(player, "Removing Robotism in 10 seconds.");
    		else
    			Output.positiveMessage(player, "Acquiring Robotism in 10 seconds.");
	        event.setCancelled(true);
        }*/
        else if(command.equalsIgnoreCase("/taunt")) {
        	taunt(player, pseudoPlayer, split);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/sub")) {
        	Output.outputSubscriptionData(player);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/clearinv") && player.isOp()) {
        	player.getInventory().clear();
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/domaintenance") && player.isOp()) {
        	System.out.println("Performing Maintenance");
			player.getServer().broadcastMessage("Performing Maintenance");
			PlotHandler.doTax();
			Database.dailyMaintenance();
			System.out.println("Maintenance Complete");
			player.getServer().broadcastMessage("Maintenance Complete");
			event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/vendor")) {
        	vendor(player, split);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/report")) {
        	int splitNameLength = split.length;
        	if(splitNameLength < 3) {
        		Output.simpleError(player, "Use /report (name) (message) to report a player.");
        	}
        	else {
				String message = "";
				for(int i=1; i<splitNameLength; i++) {
					message += split[i];
					if(i < splitNameLength-1)
						message+= " ";
				}
				message = message.trim();
				
				final RelayedMessage rm = RPG.craftircHandle.newMsg(plugin, RPG.craftircHandle.getEndPoint("synirclostshard-admin"), "generic");
				
				String compiledMessage;
				
	        	if(pseudoPlayer.isMurderer())
	        		compiledMessage = "[REPORT] 4"+player.getName()+"1: "+message;
	    		else if(pseudoPlayer.isCriminal())
	    			compiledMessage = "[REPORT] 14"+player.getName()+"1: "+message;
	    		else
	    			compiledMessage = "[REPORT] 12"+player.getName()+"1: "+message;
	        	
	        	rm.setField("message", compiledMessage);
	    	    rm.post();
	        	
	        	Output.chatAdmin(player, message);
	        	
	        	Output.positiveMessage(player, "Your report has been received, thanks!");
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/rules") || command.equalsIgnoreCase("/rule")) {
        	Output.positiveMessage(player, "-Lost Shard Rules-");
        	player.sendMessage(ChatColor.YELLOW+"Short Version: Don't Cheat");
        	player.sendMessage(ChatColor.YELLOW+"Long Version: http://wiki.lostshard.com/index.php?title=Rules");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/spawn")) {
        	if(pseudoPlayer.getSpawnTicks() <= 0) {
        		
        		if(player.getWorld() == RPG._hungryWorld) {
        			Output.simpleError(player, "You can't do that here!");
        		}
        		else {
	        		pseudoPlayer._goToSpawnTicks = 100;
	        		Output.positiveMessage(player, "Returning to spawn in 10 seconds.");
        		}
        	}
        	else {
        		int ticks = pseudoPlayer.getSpawnTicks();
        		int seconds = ticks / 10;
        		player.sendMessage("Cannot go to spawn, "+(seconds/60)+" minutes, "+(seconds%60)+" seconds remaining.");
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/lock")) {
        	if(pseudoPlayer._unlockingTicks > 0) {
        		Output.simpleError(player, "You are currently unlocking a device.");
        		event.setCancelled(true);
        		return;
        	}
        	
        	if(pseudoPlayer._lockingTicks > 0) {
        		Output.simpleError(player, "You are already locking a device.");
        		event.setCancelled(true);
        		return;
        	}
        	
        	pseudoPlayer._lockingTicks = LOCKING_TICKS;
        	Output.positiveMessage(player, "Punch the device you wish to lock.");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/unlock")) {
        	if(pseudoPlayer._unlockingTicks > 0) {
        		Output.simpleError(player, "You are currently unlocking a device.");
        		event.setCancelled(true);
        		return;
        	}
        	
        	if(pseudoPlayer._lockingTicks > 0) {
        		Output.simpleError(player, "You are already locking a device.");
        		event.setCancelled(true);
        		return;
        	}
        	
        	pseudoPlayer._unlockingTicks = UNLOCKING_TICKS;
        	Output.positiveMessage(player, "Punch the device you wish to unlock.");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase(("/convenient")) && player.isOp()) {//&& RPG.Permissions.has(player, "convenient")) {
        	pseudoPlayer._convenient = !pseudoPlayer._convenient;
        	if(pseudoPlayer._convenient)
        		Output.positiveMessage(player, "Life is now more convenient.");
        	else Output.positiveMessage(player, "Life is no longer convenient.");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/playerlist") || command.equalsIgnoreCase("/who") ||command.equalsIgnoreCase("/players")) {
        	Player[] players = plugin.getServer().getOnlinePlayers();
        	
        	ArrayList<String> filteredPlayers = new ArrayList<String>();
        	for(Player p : players) {
        		if(p.isOp()) {
        			PseudoPlayer pP = PseudoPlayerHandler.getPseudoPlayer(p.getName());
        			if(pP._secret)
        				continue;
        		}
        		filteredPlayers.add(p.getDisplayName());
        	}
        	
        	Collections.sort(filteredPlayers, String.CASE_INSENSITIVE_ORDER);
        	
        	String message = ChatColor.YELLOW+"Online Players ("+filteredPlayers.size()+"/"+SOFT_MAX_PLAYERS+"):"+ChatColor.WHITE+" ";
        	for(int i=0; i<filteredPlayers.size(); i++) {
        		message+=filteredPlayers.get(i);
        		if(i < filteredPlayers.size()-1)
        			message+=", ";
        	}
        	
        	player.sendMessage(message);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/whois")) {
        	if(split.length == 2) {
        		String targetName = split[1];
        		Player p = plugin.getServer().getPlayer(targetName);
        		if(p != null) {
        			PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(p.getName());
        			if(targetPseudoPlayer._secret) {
        				Output.simpleError(player, "That player is not online.");
        			}
        			else {
        				Output.outputWho(player, targetPseudoPlayer);
        			}
        		}
        		else {
        			Output.simpleError(player, "That player is not online.");
        		}
        	}
        	else Output.simpleError(player, "Invalid syntax, use /whois (player name)");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/bank")) {
        	/*if(!player.isOp()) {
        		Output.simpleError(player, "Banks are broken for now, working on it.");
        		event.setCancelled(true);
        		return;
        	}*/
        	ArrayList<Bank> banks = RPG._banks;
        	boolean found = false;
        	for(Bank bank : banks) {
        		//if(bank.getLocation().getWorld().equals(player.getWorld())) {
	        		if(Utils.isWithin(bank.getLocation(), player.getLocation(), bank.getRadius())) {
	                	BankBox bankBox = pseudoPlayer.getBankBox();
	                	bankBox.open(player);
	                	found=true;
	                	break;
	        		}
        		//}
        	}
        	
        	if(!found) {
        		Plot plot = PlotHandler.findPlotAt(player.getLocation());
        		if(plot != null) {
        			ArrayList<PlotNPC> plotNPCs = plot.getPlotNPCs();
        			int numPlotNPCs = plotNPCs.size();
        			for(int i=0; i<numPlotNPCs; i++) {
        				PlotNPC npc = plotNPCs.get(i);
        				if(Utils.isWithin(player.getLocation(), npc.getLocation(), 10)) {
	        				if(npc.getJob().equalsIgnoreCase("banker")) {
	        					BankBox bankBox = pseudoPlayer.getBankBox();
	    	                	bankBox.open(player);
	    	                	found=true;
	    	                	break;
	        				}
        				}
        			}
        		}
        	}
        	
        	if(!found) {
        		Output.simpleError(player, "You are not currently in a bank.");
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/debug") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.debug")) {
        	if(!RPG._debug) {
        		RPG._debug = true;
        		System.out.println("Debug On");
            	Output.positiveMessage(player, "Debug On.");
        	}
        	else {
        		RPG._debug = false;
        		System.out.println("Debug On");
            	Output.positiveMessage(player, "Debug On.");
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/debugv") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.debug")) {
        	if(!RPG._debugV) {
        		RPG._debugV = true;
        		System.out.println("DebugV On");
            	Output.positiveMessage(player, "DebugV On.");
        	}
        	else {
        		RPG._debugV = false;
        		System.out.println("DebugV On");
            	Output.positiveMessage(player, "DebugV On.");
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/bankcreate") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.bankcreate")) {
        	System.out.println("F");
        	try {
        		int size = Integer.parseInt(split[1]);
        		int bankId = Database.addBank(player.getLocation(), size);
	        	Bank bank = new Bank(bankId, player.getLocation(), size);
	        	RPG._banks.add(bank);
	        	player.sendMessage("You have created a bank of size: "+size);
        	}
        	catch(Exception e) {
        		player.sendMessage("You fucked that up bro.");
        	}
	        event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/bankremove") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.bankremove")) {
        	ArrayList<Bank> banks = RPG._banks;
        	int numBanks = banks.size();
        	for(int i=numBanks-1; i>=0; i--) {
        		if(banks.get(i).getLocation().getWorld().equals(player.getLocation().getWorld())) {
	        		if(Utils.isWithin(banks.get(i).getLocation(), player.getLocation(), banks.get(i).getRadius())) {
	        			Database.removeBank(banks.get(i));
	        			banks.remove(i);
	        		}
        		}
        	}
	        event.setCancelled(true);
        }
       else if(command.equalsIgnoreCase("/setrealspawn") && player.isOp()) {
        	/*World world = player.getWorld();
        	net.minecraft.server.WorldServer server;
        	server = ((org.bukkit.craftbukkit.CraftWorld)world).getHandle();
        	server.spawnX = player.getLocation().getBlockX();
        	server.spawnY = player.getLocation().getBlockY()+1;
        	server.spawnZ = player.getLocation().getBlockZ();
        	Output.positiveMessage(player, "Spawn Set");*/
    	   Output.positiveMessage(player, "Spawn Set");
    	   player.getWorld().setSpawnLocation(player.getLocation().getBlockX(),player.getLocation().getBlockY(), player.getLocation().getBlockZ());
    	   event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/timeday") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.time")) {
        	player.getWorld().setTime(2000);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/timenight") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.time")) {
        	player.getWorld().setTime(14000);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/heal") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.heal")) {
        	if(split.length == 2) {
        		String playerName = split[1];
        		Player p = player.getServer().getPlayer(playerName);
        		if(p != null) {
        			p.setHealth(20);
        			event.setCancelled(true);
        			Output.positiveMessage(p, "You have been healed by "+player.getName()+".");
        			Output.positiveMessage(player, "You have healed "+p.getName()+".");
        			System.out.println(player.getName()+" healed "+p.getName());
        			return;
        		}
        		else {
        			Output.simpleError(player, playerName+" not found.");
        			return;
        		}
        	}
        	player.setHealth(20);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/plot")) {
        	PlotHandler.handleCommand(player, split, plugin);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/clan")) {
        	ClanHandler.handleCommand(player, split, plugin);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/stats")) {
        	if(split.length > 1 && player.isOp()) {
        		Player targetPlayer = Utils.getPlugin().getServer().getPlayer(split[1]);
        		if(targetPlayer != null) {
        			Output.outputStats(player, targetPlayer);
        			event.setCancelled(true);
        		}
        	}
        	else {
	        	Output.outputStats(player);
	        	event.setCancelled(true);
        	}
        }
        else if(command.equalsIgnoreCase("/skills")) {
        	if(split.length == 1) {
	        	Output.outputSkills(player);
	        	event.setCancelled(true);
        	}
        	else if(split.length > 1) {
        		if(split[1].equalsIgnoreCase("reduce")) {
        			try {
        				double amount = Double.parseDouble(split[3]);
        				int amountInt = (int)(amount*10);
        				if(amountInt > 0) {
		        			String skillName = split[2];
		        			int curSkill = pseudoPlayer.getSkill(skillName);
		        			int newSkill = curSkill - amountInt;
		        			if(newSkill < 0)
		        				newSkill = 0;
		        			if(pseudoPlayer.setSkill(skillName, newSkill)) {
		        				Database.updatePlayer(player.getName());
		        				Output.positiveMessage(player, "You have reduced your "+skillName+".");
		        			}
		        			else Output.simpleError(player, "That skill does not exist.");
        				}
        				else Output.simpleError(player, "Must reduce by at least 1.");
	        			
        			}
        			catch(Exception e) {
        				Output.simpleError(player, "Invalid skill amount, use /skills reduce (skill name) (amount)");
        			}
        			event.setCancelled(true);
        		}
        		else if(split[1].equalsIgnoreCase("increase")) {// && RPG.Permissions.has(player, "rpgplugin.increaseskill")) {
        			try {
        				double amount = Double.parseDouble(split[3]);
        				int amountInt = (int)(amount*10);
        				if(amountInt > pseudoPlayer.getFreeSkillPointsRemaining()) {
        					Output.simpleError(player, "Not enough free points. Remaining: "+Utils.scaledIntToString(pseudoPlayer.getFreeSkillPointsRemaining()));
        					event.setCancelled(true);
        					return;
        				}
        				if(amountInt + pseudoPlayer.getTotalSkillVal() > pseudoPlayer.getMaxSkillValTotal()) {
        					Output.simpleError(player, "Cannot increase skills beyond the max total of "+Utils.scaledIntToString(pseudoPlayer.getMaxSkillValTotal())+".");
        					event.setCancelled(true);
        					return;
        				}
        				if(amountInt > 0) {
		        			String skillName = split[2];
		        			int curSkill = pseudoPlayer.getSkill(skillName);
		        			int newSkill = curSkill + amountInt;
		        			int dif = 0;
		        			if(newSkill > 1000) {
		        				dif = newSkill - 1000;
		        				newSkill = 1000;
		        			}
		        			amountInt -= dif;
		        			if(pseudoPlayer.setSkill(skillName, newSkill)) {
		        				pseudoPlayer.setFreeSkillPointsRemaining(pseudoPlayer.getFreeSkillPointsRemaining()-amountInt);
		        				Database.updatePlayer(player.getName());
		        				Output.positiveMessage(player, "You have increased your "+skillName+".");
		        			}
		        			else Output.simpleError(player, "That skill does not exist.");
        				}
        				else Output.simpleError(player, "Must increase by at least 1.");
	        			
        			}
        			catch(Exception e) {
        				Output.simpleError(player, "Invalid skill amount, use /skills reduce (skill name) (amount)");
        			}
        			event.setCancelled(true);
        		}
        		else if(split[1].equalsIgnoreCase("lock")) {
        			if(split.length == 3) {
        				String skillName = split[2];
        				if(pseudoPlayer.setSkillLocked(skillName, true)) {
        					Output.positiveMessage(player, "You have locked "+skillName+" it should no longer gain.");
        					Database.updateSkillLocked(player, pseudoPlayer, skillName, true);
        				}
        				else Output.simpleError(player, "That skill does not exist.");
        			}
        			else Output.simpleError(player, "Use \"/skills lock (skill name)\"");
        			event.setCancelled(true);
        		}
        		else if(split[1].equalsIgnoreCase("unlock")) {
        			if(split.length == 3) {
        				String skillName = split[2];
        				if(pseudoPlayer.setSkillLocked(skillName, false)) {
        					Output.positiveMessage(player, "You have unlocked "+skillName+" it should once again gain.");
        					Database.updateSkillLocked(player, pseudoPlayer, skillName, false);
        				}
        				else Output.simpleError(player, "That skill does not exist.");
        			}
        			else Output.simpleError(player, "Use \"/skills unlock (skill name)\"");
        			event.setCancelled(true);
        		}
        	}
        }
        else if(command.equalsIgnoreCase("/spellbook")) {
        	/*SpoutPlayer spoutPlayer = SpoutManager.getPlayer(player);
        	if(spoutPlayer != null && spoutPlayer.isSpoutCraftEnabled()) {
        		//PopupSpellbook popupSpellbook = new PopupSpellbook();
        		spoutPlayer.getMainScreen().attachPopupScreen(new GenericPopup());
        	}
        	else*/
        		Output.outputSpellbook(player, split);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/runebook")) {
        	handleRunebook(player, split);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/scrolls")) {
        	ScrollHandler.handleCommand(player, split);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/blacksmithy") || command.equalsIgnoreCase("/bl")) {
        	Blacksmithy.handleCommand(player, split);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/repair")) {
        	Blacksmithy.repair(player);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/smelt")) {
        	Blacksmithy.smelt(player);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/harden")) {
        	Blacksmithy.hardenArmor(player);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/sharpen")) {
        	Blacksmithy.sharpenWeapon(player);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/power")) {
        	Blacksmithy.powerWeapon(player);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/enhance")) {
        	Blacksmithy.enhanceTool(player);
        	event.setCancelled(true);
        }
        /*else if(command.equalsIgnoreCase("/silk")) {
        	Blacksmithy.silkTool(player);
        	event.setCancelled(true);
        }*/
        /*else if(command.equalsIgnoreCase("/fortify")) {
        	Blacksmithy.fortifyTool(player);
        	event.setCancelled(true);
        }*/
        else if(command.equalsIgnoreCase("/magery") || command.equalsIgnoreCase("/ma")) {
        	Magery.handleCommand(player, split);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/bind")) {
        	String[] newSplit = new String[split.length+1];
        	newSplit[0] = "/ma";
        	newSplit[1] = "bind";
        	for(int i=2;i<newSplit.length; i++) {
        		newSplit[i] = split[i-1];
        	}
        	Magery.handleCommand(player, newSplit);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/cast")) {
        	String[] newSplit = new String[split.length+1];
        	newSplit[0] = "/ma";
        	newSplit[1] = "cast";
        	for(int i=2;i<newSplit.length; i++) {
        		newSplit[i] = split[i-1];
        	}
        	Magery.handleCommand(player, newSplit);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/survivalism") || command.equalsIgnoreCase("/su")) {
        	Survivalism.handleCommand(player, split);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/track")) {
        	String[] newSplit = new String[split.length+1];
        	newSplit[0] = "/su";
        	newSplit[1] = "track";
        	for(int i=2;i<newSplit.length; i++) {
        		newSplit[i] = split[i-1];
        	}
        	Survivalism.handleCommand(player, newSplit);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/camp")) {
        	String[] newSplit = new String[split.length+1];
        	newSplit[0] = "/su";
        	newSplit[1] = "camp";
        	for(int i=2;i<newSplit.length; i++) {
        		newSplit[i] = split[i-1];
        	}
        	Survivalism.handleCommand(player, newSplit);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/prospect")) {
        	Mining.prospect(player);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/item") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.item")) {
        	if(split.length >= 3) {
        		try {
        			int itemID = Integer.parseInt(split[1]);
        			int durability = -999;
        			if(split.length == 4)
        				durability = Integer.parseInt(split[3]);
        			boolean canDo = true;
        			if(itemID == 7 || itemID == 8 || itemID == 9 || itemID == 10 || itemID == 11 || itemID == 23 || itemID == 25 || itemID == 46 || itemID == 51 || itemID == 52 || itemID == 92 || itemID == 289 || itemID == 325 || itemID == 326 || itemID == 327 || itemID == 354) {
        				//canDo = false;
        				//if(RPG.Permissions.has(player, "rpgplugin.adminitems")) {
        					canDo = true;
        				//}
        			}
	        		if(canDo){
	        			int num = Integer.parseInt(split[2]);
	        			ItemStack itemStack = new ItemStack(itemID, num);
	        			if(itemStack.getType() != null) {
	        				if(durability != -999)
	        					itemStack.setDurability((short)durability);
		        			player.getInventory().addItem(itemStack);
		        			System.out.println(player.getName()+" added "+num+" "+itemID+"s");
		        			player.sendMessage("You gave yourself "+num+" "+Material.getMaterial(itemID).name()+"("+itemID+")");
	        			}
        			}
        			else Output.simpleError(player, "Nuh uh");
        		}
        		catch(Exception e) {
        			Output.simpleError(player, "Invalid itemID or amount");
        		}
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/tp") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.tp")) {
        	if(split.length == 2) {
        		String target = split[1];
        		Player targetPlayer = plugin.getServer().getPlayer(target);
        		if(targetPlayer != null) {
        			player.teleport(targetPlayer);
        		}
        		else Output.simpleError(player, "Could not find player \""+target+"\".");
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/tpabove") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.tp")) {
        	if(split.length == 2) {
        		String target = split[1];
        		Player targetPlayer = plugin.getServer().getPlayer(target);
        		if(targetPlayer != null) {
        			Location location = targetPlayer.getLocation();
        			location.setY(127);
        			player.teleport(location);
        		}
        		else Output.simpleError(player, "Could not find player \""+target+"\".");
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/where") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.tp")) {
        	if(split.length == 2) {
        		String target = split[1];
        		Player targetPlayer = plugin.getServer().getPlayer(target);
        		if(targetPlayer != null) {
        			Output.positiveMessage(player, targetPlayer.getName()+ " is at "+targetPlayer.getLocation().getWorld().getName()+" - ("+targetPlayer.getLocation().getBlockX()+","+targetPlayer.getLocation().getBlockY()+","+targetPlayer.getLocation().getBlockZ()+")");
        		}
        		else Output.simpleError(player, "Could not find player \""+target+"\".");
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/tpto") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.tp")) {
        	if(split.length == 2) {
        		try {
	        		String locString = split[1];
	        		String[] splitLocString = locString.split(",");
	        		int x = Integer.parseInt(splitLocString[0]);
	        		int y = Integer.parseInt(splitLocString[1]);
	        		int z = Integer.parseInt(splitLocString[2]);
	        		player.teleport(new Location(player.getWorld(), x,y,z));
        		}
        		catch(Exception e) {
        			Output.simpleError(player, "ur dum");
        		}
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/cleanlava") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.cleanlava")) {
        	Location loc = player.getLocation();
        	for(int x=loc.getBlockX()-20; x<= loc.getBlockY()+20; x++) {
        		for(int y=loc.getBlockY()-20; y<= loc.getBlockY()+20; y++) {
        			for(int z=loc.getBlockZ()-20; z<= loc.getBlockZ()+20; z++) {
        				if(player.getWorld().getBlockTypeIdAt(x,y,z) == 10 || player.getWorld().getBlockTypeIdAt(x,y,z) == 11) {
        					Block blockAt = player.getWorld().getBlockAt(new Location(player.getWorld(), (double)x,(double)y,(double)z));
        					blockAt.setType(Material.AIR);
        				}
        			}
        		}
        	}
        }
        else if(command.equalsIgnoreCase("/cleanat") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.cleanat")) {
        	if(pseudoPlayer._cleanat)
        		Output.positiveMessage(player, "cleanat off");
        	else
        		Output.positiveMessage(player, "cleanat on");
        	pseudoPlayer._cleanat = !pseudoPlayer._cleanat;
        }
        else if(command.equalsIgnoreCase("/tphere") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.tp")) {
        	if(split.length == 2) {
        		String target = split[1];
        		Player targetPlayer = plugin.getServer().getPlayer(target);
        		if(targetPlayer != null) {
        			targetPlayer.teleport(player);
        		}
        		else Output.simpleError(player, "Could not find player \""+target+"\".");
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/kill")){
        	if(pseudoPlayer.isRobot()) {
        		player.sendMessage(ChatColor.GRAY+"You reach over to your self destruct button... you pause,      just for a moment before pushing it. Every time you do this    you think \"Am I going to be the one that respawns?\"");
		    	//Output.chatLocal(player, "Beep.", false);
		    	//pseudoPlayer._killSelfTicks = 50;
        		/*Player[] onlinePlayers = player.getServer().getOnlinePlayers();
        		int numPlayers = onlinePlayers.length;
        		for(int i=0; i<numPlayers; i++) {
        			if(Utils.isWithin(onlinePlayers[i].getLocation(), player.getLocation(), 4)) {
        				PseudoPlayer hitPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(onlinePlayers[i].getName());
        				if(!hitPseudoPlayer.isCriminal()) {
        					// crim action
        					if(RPGEntityListener.canPlayerDamagePlayer(player, onlinePlayers[i])) {
        						RPGEntityListener.criminalAction(onlinePlayers[i], player);
        					}
        				}
        			}
        		}*/
        	}
        	else {
        		Output.positiveMessage(player, "You have killed yourself.");
        	}
        	pseudoPlayer._killSelfTicks = 5;
        	player.damage(100);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/tradegold")) {
        	ArrayList<Bank> banks = RPG._banks;
        	boolean found = false;
        	for(Bank bank : banks) {
        		//if(bank.getLocation().getWorld().equals(player.getWorld())) {
	        		if(Utils.isWithin(bank.getLocation(), player.getLocation(), bank.getRadius())) {
	        			tradeGold(player, split);
	        			found=true;
	                	break;
	        		}
        		//}
        	}
        	
        	if(!found) {
        		Plot plot = PlotHandler.findPlotAt(player.getLocation());
        		if(plot != null) {
        			ArrayList<PlotNPC> plotNPCs = plot.getPlotNPCs();
        			int numPlotNPCs = plotNPCs.size();
        			for(int i=0; i<numPlotNPCs; i++) {
        				PlotNPC npc = plotNPCs.get(i);
        				if(Utils.isWithin(player.getLocation(), npc.getLocation(), 10)) {
	        				if(npc.getJob().equalsIgnoreCase("banker")) {
	        					tradeGold(player, split);
	    	                	found=true;
	    	                	break;
	        				}
        				}
        			}
        		}
        	}
        	
        	if(!found) {
        		Output.simpleError(player, "You are not currently in a bank.");
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/trade")) {
        	trade(player, split);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/pay")) {
        	payPlayer(player, split);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/tpplot") && player.isOp()) {
        	String message = "";
        	for(int i=1; i<split.length; i++) {
        		message+=(split[i]+" ");
        	}
        	message = message.trim();
        	
        	ArrayList<Plot> plots = PlotHandler.getPlots();
        	for(int i=0; i<plots.size(); i++) {
        		if(plots.get(i).getName().equalsIgnoreCase(message)) {
        			player.teleport(plots.get(i).getLocation());
        			event.setCancelled(true);
        			return;
        		}
        	}
        	
        	Output.simpleError(player, "Plot not found");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/special") && player.isOp()) {
        	String message = "";
        	for(int i=1; i<split.length; i++) {
        		message+=(split[i]+" ");
        	}
        	message = message.trim();
        	Utils.getPlugin().getServer().broadcastMessage(ChatColor.DARK_GREEN+message);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/ff")) {
        	if(pseudoPlayer._allowFriendlyFire) {
        		Output.simpleError(player, "You have disabled friendly fire.");
        		pseudoPlayer._allowFriendlyFire = false;
        	}
        	else {
        		Output.simpleError(player, "You have enabled friendly fire.");
        		pseudoPlayer._allowFriendlyFire = true;
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/reloadchunk") && player.isOp()) {
        	Chunk chunk = player.getLocation().getWorld().getChunkAt(player.getLocation());
        	player.getLocation().getWorld().refreshChunk(chunk.getX(), chunk.getZ());
        	Output.positiveMessage(player, "Chunk Refreshed");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/tptoworld") && player.isOp()) {
        	if(split.length > 1) {
	        	String worldName = split[1];
	        	if(worldName.equalsIgnoreCase("normal")) {
	        		player.teleport(new Location(RPG._normalWorld,0,90,0));
	        	}
	        	else if(worldName.equalsIgnoreCase("nether")) {
	        		player.teleport(new Location(RPG._netherWorld,0,90,0));
	        	}
	        	else if(worldName.equalsIgnoreCase("extraworld")) {
	        		player.teleport(new Location(RPG._extraWorld,0,90,0));
	        	}
	        	else if(worldName.equalsIgnoreCase("theend")) {
	        		player.teleport(new Location(RPG._theEndWorld,0,90,0));
	        	}
	        	else if(worldName.equalsIgnoreCase("theend2")) {
	        		player.teleport(new Location(RPG._theEndWorld2,0,90,0));
	        	}
	        	else if(worldName.equalsIgnoreCase("farts")) {
	        		player.teleport(new Location(RPG._farts,0,90,0));
	        	}
	        	else if(worldName.equalsIgnoreCase("newmap")) {
	        		player.teleport(new Location(RPG._newmap,0,90,0));
	        	}
	        	else if(worldName.equalsIgnoreCase("tutorialworld")) {
	        		player.teleport(new Location(RPG._tutorialWorld,0,90,0));
	        	}
	        	else if(worldName.equalsIgnoreCase("hungryworld")) {
	        		player.teleport(new Location(RPG._hungryWorld,0,90,0));
	        	}
        	}
        }
        else if(command.equalsIgnoreCase("/players") || command.equalsIgnoreCase("/player")) {
        	Output.simpleError(player, "Use /who");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/fff") && player.isOp()) {
        	LivingEntity slime = player.getWorld().spawnCreature(player.getLocation(), CreatureType.SLIME);
        	Slime sl = (Slime)slime;
        	sl.setSize(15);
        }
        else if(command.equalsIgnoreCase("/smite") && player.isOp()) {
        	if(split.length > 1) {
        		String name = split[1];
        		Player target = Bukkit.getPlayer(name);
        		if(target != null) {
        			Output.positiveMessage(player, "You did a lightning at a person.");
        			target.getWorld().strikeLightning(target.getLocation());
        		}
        		else {
        			Output.simpleError(player, "Didn't find that mans");
        		}
	        	event.setCancelled(true);
        	}
        }
        else if(command.equalsIgnoreCase("/vanish&nopickup") && player.isOp()) {
        		Output.positiveMessage(player, "nopickup enabled");
        		pseudoPlayer._noPickup = true;
        		event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/nopickup") && player.isOp()) {
        	if(!pseudoPlayer._noPickup) {
        		Output.positiveMessage(player, "nopickup enabled");
        		pseudoPlayer._noPickup = true;
        		event.setCancelled(true);
        	}
        	else {
        		Output.positiveMessage(player, "nopickup disabled");
        		pseudoPlayer._noPickup = false;
        		event.setCancelled(true);
        	}
        }
        else if(command.equalsIgnoreCase("/giveexp")) {
        	if(split.length > 1) {
        		int expamount = 0;
        		try {expamount = Integer.parseInt(split[1]);}
        		catch(Exception e) {}
        		player.setExp(player.getExp()+expamount);
        		Output.positiveMessage(player, "added "+expamount+" exp.");
	        	event.setCancelled(true);
        	}
        }
        else if(command.equalsIgnoreCase("/togglenomagicplot") && player.isOp()) {
        	Plot plot = PlotHandler.findPlotAt(player.getLocation());
        	if(plot != null) {
        		if(plot.isNoMagicPlot()) {
        			plot.setIsNoMagicPlot(false);
        			Output.positiveMessage(player, "You have enabled magic in this plot.");
        		}
        		else {
        			plot.setIsNoMagicPlot(true);
        			Output.positiveMessage(player, "You have disabled magic in this plot.");
        		}
        		Database.updatePlot(plot);
        	}
        	else Output.simpleError(player, "You are not on a plot.");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/togglenopvpplot") && player.isOp()) {
        	Plot plot = PlotHandler.findPlotAt(player.getLocation());
        	if(plot != null) {
        		if(plot.isNoPvPPlot()) {
        			plot.setIsNoPvPPlot(false);
        			Output.positiveMessage(player, "You have enabled PvP in this plot.");
        		}
        		else {
        			plot.setIsNoPvPPlot(true);
        			Output.positiveMessage(player, "You have disabled PvP in this plot.");
        		}
        		Database.updatePlot(plot);
        	}
        	else Output.simpleError(player, "You are not on a plot.");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/setmurders") && player.isOp()) {
        	if(split.length == 3) {
        		String playerName = split[1];
        		int numCounts = -1;
        		try {
        			numCounts = Integer.parseInt(split[2]);
        		}
        		catch(Exception e) {}
        		
        		if(numCounts < 0) {
        			Output.simpleError(player, "Invalid murder count number, must be 0 or greater.");
        		}
        		
        		Player targetPlayer = Bukkit.getPlayer(playerName);
        		if(targetPlayer != null) {
        			PseudoPlayer targetPseudo = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
        			targetPseudo.setMurderCounts(numCounts);
        			Database.updatePlayerByPseudoPlayer(targetPseudo);
        			Output.positiveMessage(player, "You have set "+targetPlayer.getName()+"'s murder counts to "+numCounts+".");
        			Output.positiveMessage(targetPlayer, "Your murder counts have been set to "+numCounts+" by "+player.getName()+".");
        		}
        		else Output.simpleError(player, playerName+" could not be found.");
        	}
        	else Output.simpleError(player, "Use /setmurders (name) (amount)");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/secret") && player.isOp()) {
        	if(pseudoPlayer._secret) {
        		pseudoPlayer._secret = false;
        		Output.positiveMessage(player, "You have turned off /secret");
        	}
        	else {
        		pseudoPlayer._secret = true;
        		Output.positiveMessage(player, "You have turned on /secret");
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/claim")) {
        	Plot plot = PlotHandler.findPlotAt(player.getLocation());
        	if(plot != null && plot.isControlPoint()) {
        		Clan clan = pseudoPlayer.getClan();
        		if(clan == null) {
        			Output.simpleError(player, "You may only claim "+plot.getName()+" if you are in a clan.");
        			event.setCancelled(true);
        			return;
        		}
        		
        		if(!Utils.isWithin(player.getLocation(), RPG._buntCPCenter, RPG._buntCPRange) && 
        				!Utils.isWithin(player.getLocation(), RPG._gorpCPCenter, RPG._gorpCPRange) &&
        				!Utils.isWithin(player.getLocation(), RPG._vesperCPCenter, RPG._vesperCPRange)) {
        			
        			Output.simpleError(player, "You may only claim "+plot.getName()+" if you are within the claim range.");
        			event.setCancelled(true);
        			return;
        		}
        		
        		long lastCaptureTime = plot.getLastCaptureTime();
        		Date date = new Date();
        		long curTime = date.getTime();
        		long diff = (curTime - lastCaptureTime);
        		if(diff > 1000*60*60*1) {
        			if(plot.isUnderAttack()) {
        				Output.simpleError(player, plot.getName()+" is already under attack.");
        				event.setCancelled(true);
        				return;
        			}
        			if(clan.equals(plot.getOwningClan())) {
        				Output.simpleError(player, "Your clan already owns "+plot.getName());
        				event.setCancelled(true);
        				return;
        			}
	        		plot.beginCapture(player, pseudoPlayer, clan);
	        		player.sendMessage(ChatColor.GOLD+"You must stay alive and within "+plot.getName()+" for 120 seconds.");
        			/*plot.setOwningClan(clan);
	        		Output.positiveMessage(player,  "You claimed "+plot.getName()+" for "+clan.getName()+".");
	        		plot.setLastCaptureTime(curTime);
	        		Database.updatePlot(plot);*/
        		}
        		else {
        			diff = 1000*60*60*1 - diff;
        			int numHours = (int)((double)diff/(1000*60*60));
        			diff -= numHours*60*60*1000;
        			int numMinutes = (int)((double)diff/(1000*60));
        			diff -= numMinutes*60*1000;
        			int numSeconds = (int)((double)diff/(1000));
        			Output.simpleError(player, "Cannot claim "+plot.getName()+" yet, "+numHours+" hours, "+numMinutes+" minutes and "+numSeconds+" seconds remaining.");
        		}
        	}
        	else {
        		Output.simpleError(player, "Not a plot here.");
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/setcontrolpoint") && player.isOp()) {
        	Plot plot = PlotHandler.findPlotAt(player.getLocation());
        	if(plot != null) {
        		PlotHandler.addControlPoint(plot);
        		plot.setIsControlPoint(true);
        		Output.positiveMessage(player,  "You set the control point.");
        		Database.updatePlot(plot);
        	}
        	else {
        		Output.simpleError(player, "Not a plot here.");
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/check") && player.isOp()) {
        	List<World> worlds = Utils.getPlugin().getServer().getWorlds();
        	for(World w : worlds) {
        		List<LivingEntity> livingEntities = w.getLivingEntities();
        		for(LivingEntity livingEntity : livingEntities) {
        			if(livingEntity instanceof Wolf) {
        				Wolf wolf = (Wolf)livingEntity;
        				if(wolf.isTamed()) {
        					System.out.println(wolf.getUniqueId().toString());
        				}
        			}
        		}
        	}
        	
        }
        else if(command.equalsIgnoreCase("/pets")) {
        	Taming.handleCommand(player, pseudoPlayer, split);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/boat")) {
        	Fishing.callBoat(player, pseudoPlayer);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/clearsky") && player.isOp()) {
        	World world = player.getWorld();
        	world.setStorm(false);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/storm") && player.isOp()) {
        	World world = player.getWorld();
        	world.setStorm(true);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/givescroll") && player.isOp()) {
        	String targetName = split[1];
        	Player targetPlayer = Utils.getPlugin().getServer().getPlayer(targetName);
        	if(targetPlayer == null) {
        		Output.simpleError(player, "That player is not online.");
        		event.setCancelled(true);
        		return;
        	}
        	
        	String spellName = implodeSplit(split, 2);
        	Spell spell = Spell.getSpellByName(spellName);
        	if(spell == null) {
        		Output.simpleError(player, "That spell was not found.");
        		event.setCancelled(true);
        		return;
        	}
        	
        	PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
        	targetPseudoPlayer.getScrolls().add(spell);
        	Database.addScroll(targetPseudoPlayer.getId(), spell);
        	Output.positiveMessage(player, "You have given "+targetPlayer.getName()+" a scroll of "+spell.getName()+".");
        	Output.positiveMessage(targetPlayer, "You have been given a scroll of "+spell.getName()+".");
        	event.setCancelled(true);
        }
        //REMOVED FOR 1.8 TEMPORARILY
        /*else if(command.equalsIgnoreCase("/music") && player.isOp()) {
        	if(split.length > 1) {
        		String cmd = split[1];
        		if(cmd.equalsIgnoreCase("play")) {
        		}
        		else if(cmd.equalsIgnoreCase("playall")) {
        			if(split.length >= 3) {
        				String url = implodeSplit(split, 2);
	        			Player[] players = Bukkit.getServer().getOnlinePlayers();
	                	for(Player p : players) {
	                		SpoutManager.getSoundManager().playCustomMusic(this.plugin, (SpoutPlayer)p, url, true);
	                	}
        			}
        		}
        		else if(cmd.equalsIgnoreCase("stop")) {
        		}
        		else if(cmd.equalsIgnoreCase("stopall")) {
        		}
        	}
        	else {
        		Output.simpleError(player, "Music Commands: ");
        		Output.simpleError(player, "/music play (player) (url) - plays song to a player");
        		Output.simpleError(player, "/music playall (url) - plays song to everyone");
        		Output.simpleError(player, "/music stop (player) - stops music to player");
        		Output.simpleError(player, "/music stopall - stops music for all players");
        	}
        }*/
        else if(command.equalsIgnoreCase("/titles") || command.equalsIgnoreCase("/title")) {
        	if(split.length == 1) {
        		Output.positiveMessage(player, "-"+player.getDisplayName()+ChatColor.GOLD+"'s titles-");
        		if(pseudoPlayer._availableTitles.size() == 0) {
        			Output.simpleError(player, "You do not have any titles available.");
        		}
        		else {
        			int numTitles = pseudoPlayer._availableTitles.size();
        			for(int i=0; i<numTitles; i++) {
        				player.sendMessage("- "+pseudoPlayer._availableTitles.get(i));
        			}
        		}
        	}
        	else if(split[1].equalsIgnoreCase("grant") && player.isOp()) {
        		if(split.length >= 4) {
        			String playerName = split[2];
        			Player targetPlayer = Bukkit.getServer().getPlayer(playerName);
        			if(targetPlayer != null) {
        				PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
        				String title = implodeSplit(split, 3);
        				targetPseudoPlayer._availableTitles.add(title);
        				Database.updatePlayerByPseudoPlayer(targetPseudoPlayer);
        				Output.positiveMessage(player, "You have granted "+targetPlayer.getName()+" the title \""+title+"\".");
        				Output.positiveMessage(targetPlayer, "You have been granted the title \""+title+"\".");
        			}
        			else Output.simpleError(player, "That player is not online.");
        		}
        		else Output.simpleError(player, "Use /title grant (player) (title)");
        	}
        	else if(split[1].equalsIgnoreCase("remove") && player.isOp()) {
        		if(split.length >= 4) {
        			String playerName = split[2];
        			Player targetPlayer = Bukkit.getServer().getPlayer(playerName);
        			if(targetPlayer != null) {
        				PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
        				String title = implodeSplit(split, 3);
        				targetPseudoPlayer._availableTitles.remove(title);
        				targetPseudoPlayer._activeTitle = "";
        				Database.updatePlayerByPseudoPlayer(targetPseudoPlayer);
        				Output.positiveMessage(player, "You have removed "+targetPlayer.getName()+"'s title \""+title+"\".");
        				Output.positiveMessage(targetPlayer, "Your title \""+title+"\" has been removed.");
        			}
        			else Output.simpleError(player, "That player is not online.");
        		}
        		else Output.simpleError(player, "Use /title remove (player) (title)");
        	}
        	else if(split[1].equalsIgnoreCase("list") && player.isOp()) {
        		if(split.length >= 3) {
        			String playerName = split[2];
        			Player targetPlayer = Bukkit.getServer().getPlayer(playerName);
        			if(targetPlayer != null) {
        				PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
        				Output.positiveMessage(player, "-"+targetPlayer.getDisplayName()+ChatColor.GOLD+"'s titles-");
                		if(targetPseudoPlayer._availableTitles.size() == 0) {
                			Output.simpleError(player, "They do not have any titles.");
                		}
                		else {
                			int numTitles = targetPseudoPlayer._availableTitles.size();
                			for(int i=0; i<numTitles; i++) {
                				player.sendMessage("- "+targetPseudoPlayer._availableTitles.get(i));
                			}
                		}
        			}
        			else Output.simpleError(player, "That player is not online.");
        		}
        		else Output.simpleError(player, "Use /title list (player)");
        	}
        	else {
        		String message = implodeSplit(split, 1);
        		if(message.equalsIgnoreCase("clear")) {
        			pseudoPlayer._activeTitle = "";
        			Database.updatePlayerByPseudoPlayer(pseudoPlayer);
        			Output.positiveMessage(player, "You are no longer using a title.");
        		}
        		else {
	        		int numTitles = pseudoPlayer._availableTitles.size();
	        		boolean found = false;
	        		for(int i=0; i<numTitles; i++) {
	        			String title = pseudoPlayer._availableTitles.get(i);
	        			if(title.equalsIgnoreCase(message)) {
	        				pseudoPlayer._activeTitle = title;
	        				Output.positiveMessage(player, "You have set your title to "+title+".");
	        				found = true;
	        				Database.updatePlayerByPseudoPlayer(pseudoPlayer);
	        				break;
	        			}
	        		}
	        		if(!found) {
	        			Output.simpleError(player, "You do not have that title available, use /titles to see your available titles.");
	        		}
        		}
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/npccount")) {
        	//Output.positiveMessage(player, "NPC Count: " + NPCHandler._npcManager.HumanNPCList.size());
        }
        else if(command.equalsIgnoreCase("/build")) {
        	if(Utils.isWithin(player.getLocation(), RPG._orderBuilderLocation, 5) || Utils.isWithin(player.getLocation(), RPG._chaosBuilderLocation, 5)) {
        	}
        	else {
        		Output.simpleError(player, "You must do this at the proper location in your spawn city.");
        		event.setCancelled(true);
        		return;
        	}
        	
        	//CAKE
        	if(split.length >= 2) {
        		int buildNum = -1;
        		try{buildNum = Integer.parseInt(split[1]);}
        		catch(Exception e) {}
        		if(buildNum < 0) {
        			Output.simpleError(player, "Invalid build number.");
        			event.setCancelled(true);
        			return;
        		}
        		
        		if(pseudoPlayer.isLargerBank()) {
        			if(buildNum > 2) {
        				Output.simpleError(player, "You may only use builds 0, 1, and 2.");
        				return;
        			}
        		}
        		else if(buildNum > 1) {
        			Output.simpleError(player, "You may only use builds 0 and 1");
        			return;
        		}
        		
        		
        		Database.setBuild(pseudoPlayer, buildNum);
        		Database.updatePlayerByPseudoPlayer(pseudoPlayer);
        		
        		player.getWorld().strikeLightningEffect(player.getLocation());
        		player.damage(100);
        		event.setCancelled(true);
        	}
        }
        else if(command.equalsIgnoreCase("/cake") && player.isOp()) {
        	int amount = -1;
        	try{
        		amount = Integer.parseInt(split[1]);
        	}
        	catch(Exception e) {}
        	
        	if(amount <1 || amount >35) {
        		Output.simpleError(player, "Nope");
        	}
        	else {
        		Location loc = player.getLocation();
        		for(int x=-amount; x<=amount; x++) {
        			for(int z=-amount; z<=amount; z++) {
        				int y = player.getWorld().getHighestBlockYAt(loc.getBlockX()+x, loc.getBlockZ()+z);
        				Block block = player.getWorld().getBlockAt(loc.getBlockX()+x,y,loc.getBlockZ()+z);
        				if(Utils.isWithin(block.getLocation(), player.getLocation(), amount)) {
        					block.setType(Material.CAKE_BLOCK);
        					block.getWorld().playEffect(block.getLocation(), Effect.SMOKE, (int)Math.random()*3);
        				}
        			}
        		}
        		Magery.chant(player, "Pastrius Maximus");
        	}
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/meditate")) {
        	int curSkill = pseudoPlayer.getSkill("magery");
        	if(curSkill >= 1000) {
        		pseudoPlayer._isMeditating = true;
        		Output.positiveMessage(player, "You begin meditating...");
        		event.setCancelled(true);
        	}
        	else Output.simpleError(player, "You do not have enough Magery to use this ability.");
        }
        else if(command.equalsIgnoreCase("/rest")) {
        	pseudoPlayer._isResting = true;
        	Output.positiveMessage(player, "You begin resting...");
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/event")) {
        	if(split.length > 1) {
        		if(player.isOp()) {                	
                	if(split[1].equalsIgnoreCase("end")) {
                		if(RPG._worldEvent != null) {
                			RPG._worldEvent.finish();
                			RPG._worldEvent = null;
                		}
                		else {
                			Output.simpleError(player, "There is not currently an event active.");
                		}
                	}
                	else {
                		if(RPG._worldEvent != null) {
                			RPG._worldEvent.finish();
                			RPG._worldEvent = null;
                		}
                		
                		if(split[1].equalsIgnoreCase("ghastattack")) {
                			if(split.length > 4) {
                				int numberOfGhasts = -1;
                				int range = -1;
                				int timeout = -1;
                				try {
                					numberOfGhasts = Integer.parseInt(split[2]);
                					range = Integer.parseInt(split[3]);
                					timeout = Integer.parseInt(split[4]);
                				}
                				catch(Exception e) {}
                				
                				if(numberOfGhasts < 1) {
                					Output.simpleError(player, "Invalid amount, use 1 or more");
                					event.setCancelled(true);
                					return;
                				}
                				
    	                		RPG._worldEvent = new GhastAttackEvent(player.getLocation(), numberOfGhasts, range, timeout);
    	                		RPG._worldEvent.start();
                			}
                			else Output.simpleError(player, "Use /event ghastattack (number of ghasts) (range) (timeout in minutes)");
                		}
                		
                		else if(split[1].equalsIgnoreCase("meteor")) {
                			if(split.length > 2) {
                				int range = -1;
                				try {
                					range = Integer.parseInt(split[2]);
                				}
                				catch(Exception e) {}
                				
                				if(range < 3) {
                					Output.simpleError(player, "Invalid amount, use 3 or more");
                					event.setCancelled(true);
                					return;
                				}
                				
    	                		RPG._worldEvent = new MeteorLandEvent(player.getLocation(), range);
    	                		RPG._worldEvent.start();
                			}
                			else Output.simpleError(player, "Use /event meteor (range)");
                		}
                	}
        		}
        		else {
        			Output.simpleError(player, "Use /event to find out about events going on now.");
        		}
        	}
        	else {
        		if(RPG._worldEvent != null) {
        			Output.positiveMessage(player, RPG._worldEvent.getDescription());
        		}
        		else {
        			Output.simpleError(player, "There is not an event right now.");
        		}
        	}
        	event.setCancelled(true);
        }
       /* else if(command.equalsIgnoreCase("/createworld") && RPG.Permissions.has(player, "rpgplugin.createworld")) {
        	if(split.length == 2) {
        		String worldName = split[1];
        		List<World> worlds = plugin.getServer().getWorlds();
        		boolean foundExisting = false;
        		for(World world : worlds) {
        			if(world.getName().equalsIgnoreCase(worldName)) {
        				foundExisting = true;
        				break;
        			}
        		}
        		if(!foundExisting) {
        			plugin.getServer().createWorld(worldName, Environment.NETHER);
        			Output.positiveMessage(player, "Created world "+worldName);
        		}
        		else Output.simpleError(player, "World name already exists.");
        	}
        	else Output.simpleError(player, "Use /createworld (world name)");
        }*/
        /*else if(command.equals("/gotoworld")) {
        	if(split.length == 2) {
        		String worldName = split[1];
        		List<World> worlds = plugin.getServer().getWorlds();
        		World foundWorld = null;
        		for(World world : worlds) {
        			if(world.getName().equalsIgnoreCase(worldName)) {
        				foundWorld = world;
        				break;
        			}
        		}
        		if(foundWorld != null) {
        			Location loc = player.getLocation();*/
        			/*
        			Location dest;
        			if(player.getWorld().getName().equals(RPG._netherWorld.getName())) {      				
        				dest = new Location(RPG._normalWorld, loc.getX()*10, loc.getY()+.5, loc.getZ()*10);
        				dest.setPitch(player.getLocation().getPitch());
        				dest.setYaw(player.getLocation().getYaw());
        				
        				for(int x=dest.getBlockX()-3; x<= dest.getBlockX()+3; x++) {
        					for(int y=dest.getBlockY()-3; y<= dest.getBlockY()+3; y++) {
        						for(int z=dest.getBlockZ()-3; z<= dest.getBlockZ()+3; z++) {
        							RPG._normalWorld.getBlockAt(x,y,z).setType(Material.STONE);
                				}
            				}
        				}
        				for(int x=dest.getBlockX()-2; x<= dest.getBlockX()+2; x++) {
        					for(int y=dest.getBlockY()-2; y<= dest.getBlockY()+2; y++) {
        						for(int z=dest.getBlockZ()-2; z<= dest.getBlockZ()+2; z++) {
        							RPG._normalWorld.getBlockAt(x,y,z).setType(Material.AIR);
                				}
            				}
        				}
        			}
        			else {        				
        				dest = new Location(RPG._netherWorld, loc.getX()/10, loc.getY()+.5, loc.getZ()/10);
        				dest.setPitch(player.getLocation().getPitch());
        				dest.setYaw(player.getLocation().getYaw());
        				
        				for(int x=dest.getBlockX()-3; x<= dest.getBlockX()+3; x++) {
        					for(int y=dest.getBlockY()-3; y<= dest.getBlockY()+3; y++) {
        						for(int z=dest.getBlockZ()-3; z<= dest.getBlockZ()+3; z++) {
        							RPG._netherWorld.getBlockAt(x,y,z).setType(Material.STONE);
                				}
            				}
        				}
        				for(int x=dest.getBlockX()-2; x<= dest.getBlockX()+2; x++) {
        					for(int y=dest.getBlockY()-2; y<= dest.getBlockY()+2; y++) {
        						for(int z=dest.getBlockZ()-2; z<= dest.getBlockZ()+2; z++) {
        							RPG._netherWorld.getBlockAt(x,y,z).setType(Material.AIR);
                				}
            				}
        				}
        			}
        			player.teleportTo(dest);*/
        			/*player.teleportTo(new Location(foundWorld, loc.getX(), loc.getY(), loc.getZ()));
        		}
        		else Output.simpleError(player, "No world named "+worldName+" found.");
        	}
        	else Output.simpleError(player, "Use /gotoworld (world name)");
        }*/
        else if(command.equalsIgnoreCase("/party")) {
        	PartyHandler.handleCommand(player, split);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/npc") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.npc")) {
        	NPCHandler.handleCommand(player, split);
        	event.setCancelled(true);
        }
        else if(command.equalsIgnoreCase("/resetallskills")) {
        	if(split.length > 1) {
        		pseudoPlayer.setSkill("archery", 0);
        		pseudoPlayer.setSkill("blacksmithy", 0);
        		pseudoPlayer.setSkill("brawling", 0);
        		pseudoPlayer.setSkill("magery", 0);
        		pseudoPlayer.setSkill("blades", 0);
        		pseudoPlayer.setSkill("survivalism", 0);
        		pseudoPlayer.setSkill("mining", 0);
        		pseudoPlayer.setSkill("lumberjacking", 0);
        		pseudoPlayer.setSkill("taming", 0);
        		pseudoPlayer.setSkill("fishing", 0);
        		
        		if(!pseudoPlayer.setSkill(split[1], 500)) {
        			Output.simpleError(player, "Skills wiped but you chose an invalid skill to set to 50.");
        		}
        		else {
        			Output.positiveMessage(player, "Skills wiped, "+split[1]+" set to 50.0");
        		}
        		Database.updatePlayerByPseudoPlayer(pseudoPlayer);
        	}
        	else	{
        		Output.simpleError(player, "Use \"/resetallskills (skill name)\"");
        		Output.simpleError(player, "-Sets named skill to 50 and everything else to 0.-");
        	}
        	event.setCancelled(true);
        }
        else if(!event.isCancelled()) Output.simpleError(player, "Unknown Command: " + event.getMessage());
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
    	event.setDeathMessage("");
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {    	
    	Player player = event.getPlayer();
    	PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    	if(pseudoPlayer == null)
    		return;
    	
    	if(pseudoPlayer._flyTicks > 0) {
    		if(player.isSneaking()) {
    			Vector dir = player.getLocation().getDirection();
				player.setVelocity( dir.multiply(1));
				player.setFallDistance(0);
    		}
    	}
    	
    	if(pseudoPlayer._teleportTo != null)
    		return;
    	
    	if(pseudoPlayer._dieLog == 2) {
    		event.setCancelled(true);
    	}
    	
    	if(pseudoPlayer._stunTicks > 0) {
    		Vector vel = player.getVelocity();
    		player.setVelocity(new Vector(0, vel.getY(), 0));
    	}
    	
    	// Determine the location and block that the player has moved to
        //Location to = event.getTo();
        Block blockAt = player.getLocation().getBlock();
        
        if(pseudoPlayer._lastBlock == null)
        	pseudoPlayer._lastBlock = blockAt;
        
        // If we have moved to a new block
        if(!blockAt.equals(pseudoPlayer._lastBlock)) { 
        	// Handle movement disruptions:
        	if(pseudoPlayer._goToSpawnTicks > 0) {
    			pseudoPlayer._goToSpawnTicks = 0;
             	Output.simpleError(player, "Moved while casting, /spawn was disrupted.");
    		}
        	if(pseudoPlayer._isCastingDay) {
    			pseudoPlayer._isCastingDay = false;
    			RPG._castingDayPlayers.remove(player.getName());
             	Output.simpleError(player, "Moved while channeling Day, it has been disrupted.");
    		}
        	if(pseudoPlayer._isCastingClearSky) {
    			pseudoPlayer._isCastingClearSky = false;
    			RPG._castingClearSkyPlayers.remove(player.getName());
             	Output.simpleError(player, "Moved while channeling Clear Sky, it has been disrupted.");
    		}
        	if(pseudoPlayer._traitChangeTicks > 0) {
    			pseudoPlayer._traitChangeTicks = 0;
             	Output.simpleError(player, "Moved while changing trait, it was disrupted.");
    		}
        	if(pseudoPlayer.getPromptedSpell() != null) {
        		pseudoPlayer.setPromptedSpell(null);
        		pseudoPlayer.setDelayedSpell(null);
        		pseudoPlayer.setCastDelayTicks(0);
        		Output.simpleError(player, "Moved while casting a spell, it was disrupted.");
        	}
            if(pseudoPlayer.getCastDelayTicks() > 0) {
            	pseudoPlayer.setCastDelayTicks(0);
            	pseudoPlayer.setDelayedSpell(null);
            	Output.simpleError(event.getPlayer(), "Moved while casting, spell was disrupted.");
            }
            if(pseudoPlayer._isMeditating) {
            	pseudoPlayer._isMeditating = false;
            	Output.simpleError(player, "You stop meditating.");
            }
            if(pseudoPlayer._isResting) {
            	pseudoPlayer._isResting = false;
            	Output.simpleError(player, "You stop resting.");
            }
            
            // Determine the plot you are in so that you can handle the enter and leave plot stuff
        	Plot fromPlot = PlotHandler.findPlotAt(pseudoPlayer._lastBlock.getLocation());
        	Plot toPlot = PlotHandler.findPlotAt(blockAt.getLocation());
        	
        	if(pseudoPlayer._claiming) {
	            ArrayList<Plot> controlPoints = PlotHandler.getControlPoints();
	            for(Plot cP : controlPoints) {
	            	if(cP.isUnderAttack() && cP._capturingPlayerName.equals(player.getName())) {
	            		if(toPlot == null || toPlot != cP)
	            			cP.failCaptureLeft(player);
	            		else if(!Utils.isWithin(player.getLocation(), RPG._buntCPCenter, RPG._buntCPRange) && 
	            				!Utils.isWithin(player.getLocation(), RPG._gorpCPCenter, RPG._gorpCPRange) &&
	            				!Utils.isWithin(player.getLocation(), RPG._vesperCPCenter, RPG._vesperCPRange)) {
	            			
	            			cP.failCaptureLeft(player);
	            		}
	            	}
	            }
            }
        	
        	if(pseudoPlayer._fireWalkTicks > 30) {
        		//if(toPlot == null || toPlot.isOwner(player.getName()) || toPlot.isCoOwner(player.getName()) || toPlot.isFriend(player.getName())) {
        			if(blockAt.getType().equals(Material.AIR)) {
        				blockAt.setType(Material.FIRE);
        				if(pseudoPlayer._fireWalk != null) {
        					pseudoPlayer._fireWalk.addBlock(blockAt);
        				}
        			}
        		//}
        	}
        	
        	pseudoPlayer._awayTicks = 6000;
        	//Player player = event.getPlayer();
        	//PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
        	
        	if(fromPlot == null && toPlot == null) {
             	// walking in the wilderness
            }
            else {            	
             	if(fromPlot != toPlot) {
     	        	if(fromPlot == null) {
     	        		// must be entering a plot
     	        		player.sendMessage(ChatColor.GRAY+"You have entered "+toPlot.getName());
     	        	}
     	        	else if(toPlot == null) {
     	        		// must be leaving a plot
     	        		player.sendMessage(ChatColor.GRAY+"You have left "+fromPlot.getName());
     	        	}
     	        	else {
     	        		// must be moving from one plot to another
     	        		player.sendMessage(ChatColor.GRAY+"You have left "+fromPlot.getName()+" and entered "+toPlot.getName());
     	        	}
             	}
            }
        	
        	if(!pseudoPlayer._lastBlock.getType().equals(Material.PORTAL) && blockAt.getType().equals(Material.PORTAL)) {
        		//if(pseudoPlayer._enteredPortalTicks == 0) {
        			System.out.println("Stepped In Portal");
        			//pseudoPlayer._enteredPortalTicks = 20;
        			Location targetLoc = checkPortals(player);
        			if(targetLoc != null) {
        				targetLoc = new Location(targetLoc.getWorld(), targetLoc.getX()+.5, targetLoc.getY(), targetLoc.getZ()+.5);
        				Location curLoc = player.getLocation();
        				targetLoc.setPitch(curLoc.getPitch());
        				targetLoc.setYaw(curLoc.getYaw());
        				//_enteredPortalTicks = 0;
        				pseudoPlayer._recentlyTeleportedTicks = 30;
        				event.setTo(targetLoc);//player.teleport(targetLoc);
        				System.out.println("Portaling Player");
        			}
        		//}
        	}
        	/*else {
        		pseudoPlayer._enteredPortalTicks = 0;
        	}*/
        	if(player.getLocation().getY() < 0 && pseudoPlayer._recentlyLoggedInTicks > 0) {
        		player.teleport(new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getWorld().getHighestBlockYAt(player.getLocation()), player.getLocation().getZ()));
        	}
        	
        	pseudoPlayer._lastBlock = blockAt;
        }
        else {};
    }
    
    //private static boolean did = false;
    @EventHandler
    public void onPlayerAnimation(PlayerAnimationEvent event) {
    	if(RPG._debugV)
    		System.out.println("Player Animation");
    	
    	if (event.getAnimationType().equals(PlayerAnimationType.ARM_SWING)){
    		Player player = event.getPlayer();
    		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    		pseudoPlayer._swung = true;
    		if(pseudoPlayer._openedChestTicks > 0) {
    			return;
    		}
	    	/*else if(itemInHandId == 352) {
	    		Spell.hasLOSTo(player, block);
	    	}*/
	    	/*else if((itemInHandId == Material.WOOD_SWORD.getId()) || (itemInHandId == Material.STONE_SWORD.getId()) || (itemInHandId == Material.IRON_SWORD.getId()) || (itemInHandId == Material.DIAMOND_SWORD.getId()) || (itemInHandId == Material.GOLD_SWORD.getId())) {
	    		// attacking with a sword
	    		CombatAbility ability = pseudoPlayer.getSwordsmanshipAbility();
	    		if(ability != null) {
	    			if(ability.isSet())
	    				ability.swing(player, pseudoPlayer);
	    		}
	    	}*/
    	}
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
    	Block blockClicked = event.getBlockClicked();
    	Player player = event.getPlayer();
    	Plot plot = PlotHandler.findPlotAt(blockClicked.getLocation());
    	//If there is a plot there
    	if(plot != null) {
    		//And it is protected
    		if(plot.isProtected()) {
    			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    			//And you are not a plot friend/co-owner/owner
    			if((pseudoPlayer._plottest == plot) || ( !plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName()) )) {
    				//Then you can't drop lava buckets
    				Output.simpleError(player, "You cannot use that here, the plot is protected.");
    				event.setCancelled(true);
    				return;
    			}
    		}
    	}
    }
    
    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
    	Block blockClicked = event.getBlockClicked();
    	Player player = event.getPlayer();
    	PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    	Plot plot = PlotHandler.findPlotAt(blockClicked.getLocation());
    	//If there is a plot there
    	if(plot != null) {
    		//And it is protected
    		if(plot.isProtected()) {
    			//And you are not a plot friend/co-owner/owner
    			if((pseudoPlayer._plottest == plot) || ( !plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName()) )) {
    				//Then you can't drop lava buckets
    				Output.simpleError(player, "You cannot use that here, the plot is protected.");
    				event.setCancelled(true);
    				return;
    			}
    		}
    	}
    	if(pseudoPlayer.isRobot()) {
    		Output.chatLocal(player, "*Bloop*", false);
    	}
    }
    
    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
    	Player player = event.getPlayer();
    	PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    	if(pseudoPlayer._noPickup)
    		event.setCancelled(true);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {    	
    	Player player = event.getPlayer();
    	
    	ItemStack itemInHand = player.getItemInHand();
    	if(itemInHand != null) {
    		if(itemInHand.getType().equals(Material.POTION)) 
    		{
    			boolean block = false;
    			
    			short potionType = itemInHand.getDurability();
    			switch(potionType) 
    			{
    				// Regen
	    			case 16273:
	    				block = true;
	    				break;
	    			case 16305:
	    				block = true;
	    				break;
	    			case 16337:
	    				block = true;
	    				break;
	    			case 16369:
	    				block = true;
	    				break;
	    			
	    			// Splash regen
	    			case 32657:
	    				block = true;
	    				break;
	    			case 32689:
	    				block = true;
	    				break;
	    			case 32721:
	    				block = true;
	    				break;
	    			case 32753:
	    				block = true;
	    				break;
	    				
	    			// Instant health
	    			case 32725:
	    				block = true;
	    				break;
	    			case 32757:
	    				block = true;
	    				break;
	    				
	    			// Instant damage
	    			case 32732:
	    				block = true;
	    				break;
	    			case 32764:
	    				block = true;
	    				break;
	    				
	    				// Strength
	    			case 8201:
	    				block = true;
	    				break;
	    			case 8233:
	    				block = true;
	    				break;
	    			case 8265:
	    				block = true;
	    				break;
	    			case 8297:
	    				block = true;
	    				break;
	    			case 16393:
	    				block = true;
	    				break;
	    			case 16425:
	    				block = true;
	    				break;
	    			case 16457:
	    				block = true;
	    				break;
	    			case 16489:
	    				block = true;
	    				break;
    			}
    			
    			if(block) 
    			{
    				event.setCancelled(true);
    				Output.simpleError(player, "Cannot use that potion.");
    			}
    		}
    	}
    	
    	PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    	Block blockClicked = event.getClickedBlock();
    	if(event.getItem() != null && event.getItem().getType().equals(Material.ENDER_PEARL)) {
    		event.setCancelled(true);
    		Output.simpleError(player, "Ender Pearls are currently disabled.");
    		return;
    	}
    	
    	/*if(blockClicked != null && blockClicked.getType() == Material.ANVIL) {
    		event.setCancelled(true);
    		Output.simpleError(player, "Sorry, that is disabled for now.");
    		
    		Plot plot = PlotHandler.findPlotAt(new Location(blockClicked.getWorld(), blockClicked.getX() + .5, blockClicked.getY() + .5, blockClicked.getZ() + .5));
    		
    		if(plot != null) 
    		{
    			if(plot.isOwner(player.getName())) 
    			{
    				blockClicked.setType(Material.AIR);
    	    		blockClicked.getWorld().dropItemNaturally(new Location(blockClicked.getWorld(), blockClicked.getX() + .5, blockClicked.getY() + .5, blockClicked.getZ() + .5), new ItemStack(Material.ANVIL, 1));
    			}
    		}
    		else {
    			blockClicked.setType(Material.AIR);
        		blockClicked.getWorld().dropItemNaturally(new Location(blockClicked.getWorld(), blockClicked.getX() + .5, blockClicked.getY() + .5, blockClicked.getZ() + .5), new ItemStack(Material.ANVIL, 1));
    		}
    		return;
    	}*/
    	
    	if(blockClicked != null && blockClicked.getType().equals(Material.DRAGON_EGG)) {
    		boolean allowed = true;
    		Plot plot = PlotHandler.findPlotAt(blockClicked.getLocation());
    		if(plot != null) {
    			if(!plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName())) 
    			{
    				allowed = false;
    			}
    		}
    		if(allowed) {
    			blockClicked.setType(Material.AIR);
    			blockClicked.getWorld().dropItemNaturally(blockClicked.getLocation(), new ItemStack(122, 1));
				event.setCancelled(true);
				return;
    		}
    		else {
    			event.setCancelled(true);
    			Output.simpleError(player,  "You cannot do that because you are not an owner or co-owner at this plot.");
    		}
		}
    	
    	/*if(blockClicked != null && blockClicked.getType().equals(Material.ENDER_CHEST)) 
    	{
    		Output.simpleError(player, "Ender chests currently disabled until I can be sure they don't have dupe bugs.");
    		event.setCancelled(true);
    		return;
    	}*/
    	
    	// Using bonemeal
    	if(event.getItem() != null && event.getItem().getType().equals(Material.INK_SACK) && event.getItem().getDurability() == 15) {
    		if(blockClicked != null) {
    			Plot plot = PlotHandler.findPlotAt(blockClicked.getLocation());
    			if(plot != null) {
    				if(!plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName())) {
    					event.setCancelled(true);
    					Output.simpleError(player, "You cannot use that here, the plot is protected.");
    					return;
    				}
    			}
    		}
    	}
    	
    	/*if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
    		World world = blockClicked.getWorld();
    		if(blockClicked.getType().equals(Material.OBSIDIAN) && player.getItemInHand().getType().equals(Material.FLINT_AND_STEEL)) {
    			event.setCancelled(true);
    			
    			ArrayList<Block> frameBlocks = new ArrayList<Block>();
    			ArrayList<Block> airBlocks = new ArrayList<Block>();    			
    			boolean allObsidian = true;
    			boolean allAir = true;
    			boolean found = false;
    			
    			if(!found) {
    				frameBlocks.clear();
    				airBlocks.clear();
    				allObsidian = true;
        			allAir = true;
        			
	    			//Check +X
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ()));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX()+1, blockClicked.getY(), blockClicked.getZ())); 
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX()+2, blockClicked.getY()+1, blockClicked.getZ()));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX()+2, blockClicked.getY()+2, blockClicked.getZ()));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX()+2, blockClicked.getY()+3, blockClicked.getZ()));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX()+1, blockClicked.getY()+4, blockClicked.getZ()));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+4, blockClicked.getZ()));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX()-1, blockClicked.getY()+3, blockClicked.getZ()));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX()-1, blockClicked.getY()+2, blockClicked.getZ()));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX()-1, blockClicked.getY()+1, blockClicked.getZ()));
	    			
	    			for(Block b : frameBlocks) {
	    				if(!b.getType().equals(Material.OBSIDIAN)) {
	    					allObsidian = false;
	    					break;
	    				}
	    			}
	    			
	    			if(allObsidian) {
	    				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+1, blockClicked.getZ()));
	    				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+2, blockClicked.getZ()));
	    				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+3, blockClicked.getZ()));
	    				airBlocks.add(world.getBlockAt(blockClicked.getX()+1, blockClicked.getY()+1, blockClicked.getZ()));
	    				airBlocks.add(world.getBlockAt(blockClicked.getX()+1, blockClicked.getY()+2, blockClicked.getZ()));
	    				airBlocks.add(world.getBlockAt(blockClicked.getX()+1, blockClicked.getY()+3, blockClicked.getZ()));
	    				
	    				for(Block b : airBlocks) {
	    					if(!b.getType().equals(Material.AIR)) {
	    						allAir = false;
	    						break;
	    					}
	    				}
	    			}
	    			
	    			if(allObsidian && allAir) {
	    				found = true;
	    				System.out.println("+X");
	    			}
    			}
    			
    			if (!found) {
    				frameBlocks.clear();
    				airBlocks.clear();
    				allObsidian = true;
        			allAir = true;
        			
    				//Check -X
        			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ()));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX()-1, blockClicked.getY(), blockClicked.getZ()));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX()-2, blockClicked.getY()+1, blockClicked.getZ()));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX()-2, blockClicked.getY()+2, blockClicked.getZ()));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX()-2, blockClicked.getY()+3, blockClicked.getZ()));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX()-1, blockClicked.getY()+4, blockClicked.getZ()));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+4, blockClicked.getZ()));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX()+1, blockClicked.getY()+3, blockClicked.getZ()));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX()+1, blockClicked.getY()+2, blockClicked.getZ()));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX()+1, blockClicked.getY()+1, blockClicked.getZ()));
        			
        			for(Block b : frameBlocks) {
        				if(!b.getType().equals(Material.OBSIDIAN)) {
        					allObsidian = false;
        					break;
        				}
        			}
        			
        			if(allObsidian) {
        				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+1, blockClicked.getZ()));
        				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+2, blockClicked.getZ()));
        				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+3, blockClicked.getZ()));
        				airBlocks.add(world.getBlockAt(blockClicked.getX()-1, blockClicked.getY()+1, blockClicked.getZ()));
        				airBlocks.add(world.getBlockAt(blockClicked.getX()-1, blockClicked.getY()+2, blockClicked.getZ()));
        				airBlocks.add(world.getBlockAt(blockClicked.getX()-1, blockClicked.getY()+3, blockClicked.getZ()));
        				
        				for(Block b : airBlocks) {
        					if(!b.getType().equals(Material.AIR)) {
        						allAir = false;
        						break;
        					}
        				}
        			}
        			
        			if(allObsidian && allAir) {
        				System.out.println("-X");
        			}
    			}
    			
    			if(!found) {
    				frameBlocks.clear();
    				airBlocks.clear();
    				allObsidian = true;
        			allAir = true;
        			
	    			//Check +Z
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ()));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ()+1)); 
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+1, blockClicked.getZ()+2));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+2, blockClicked.getZ()+2));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+3, blockClicked.getZ()+2));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+4, blockClicked.getZ()+1));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+4, blockClicked.getZ()));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+3, blockClicked.getZ()-1));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+2, blockClicked.getZ()-1));
	    			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+1, blockClicked.getZ()-1));
	    			
	    			for(Block b : frameBlocks) {
	    				if(!b.getType().equals(Material.OBSIDIAN)) {
	    					allObsidian = false;
	    					break;
	    				}
	    			}
	    			
	    			if(allObsidian) {
	    				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+1, blockClicked.getZ()));
	    				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+2, blockClicked.getZ()));
	    				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+3, blockClicked.getZ()));
	    				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+1, blockClicked.getZ()+1));
	    				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+2, blockClicked.getZ()+1));
	    				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+3, blockClicked.getZ()+1));
	    				
	    				for(Block b : airBlocks) {
	    					if(!b.getType().equals(Material.AIR)) {
	    						allAir = false;
	    						break;
	    					}
	    				}
	    			}
	    			
	    			if(allObsidian && allAir) {
	    				found = true;
	    				System.out.println("+Z");
	    			}
    			}
    			
    			if (!found) {
    				frameBlocks.clear();
    				airBlocks.clear();
    				allObsidian = true;
        			allAir = true;
        			
    				//Check -Y
        			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ()));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ()-1));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+1, blockClicked.getZ()-2));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+2, blockClicked.getZ()-2));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+3, blockClicked.getZ()-2));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+4, blockClicked.getZ()-1));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+4, blockClicked.getZ()));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+3, blockClicked.getZ()+1));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+2, blockClicked.getZ()+1));
        			frameBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+1, blockClicked.getZ()+1));
        			
        			for(Block b : frameBlocks) {
        				if(!b.getType().equals(Material.OBSIDIAN)) {
        					allObsidian = false;
        					break;
        				}
        			}
        			
        			if(allObsidian) {
        				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+1, blockClicked.getZ()));
        				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+2, blockClicked.getZ()));
        				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+3, blockClicked.getZ()));
        				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+1, blockClicked.getZ()-1));
        				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+2, blockClicked.getZ()-1));
        				airBlocks.add(world.getBlockAt(blockClicked.getX(), blockClicked.getY()+3, blockClicked.getZ()-1));
        				
        				for(Block b : airBlocks) {
        					if(!b.getType().equals(Material.AIR)) {
        						allAir = false;
        						break;
        					}
        				}
        			}
        			
        			if(allObsidian && allAir) {
        				System.out.println("-Z");
        			}
    			}
    			
    			if(found) {
    				ArrayList<Block> otherWorldBlocks = new ArrayList<Block>();
    				boolean isValid = true;
    				
    				for(Block b : frameBlocks) {
    					Block oWB = null;
	    				if(world.equals(RPG._normalWorld)) {
	    					oWB = RPG._netherWorld.getBlockAt(b.getX(), b.getY(), b.getZ());
	    				}
	    				else if(world.equals(RPG._netherWorld)) {
	    					oWB = RPG._normalWorld.getBlockAt(b.getX(), b.getY(), b.getZ());
	    				}
	    				
	    				if(oWB != null) {
	    					Plot p = PlotHandler.findPlotAt(oWB.getLocation());
	    					if(p != null && p.isProtected()) {
	    						if(!p.isOwner(player.getName()) && !p.isCoOwner(player.getName())) {
	    							Output.simpleError(player, "Cannot open a portal a protected plot you are not an owner or co-owner on.");
	    							isValid = false;
	    						}
	    					}
	    					otherWorldBlocks.add(oWB);
	    				}
    				}
    			}    		
    		}
    	}*/
    	
    	if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
    		if(event.getClickedBlock().getType().equals(Material.JUKEBOX)) {
    			Plot plot = PlotHandler.findPlotAt(event.getClickedBlock().getLocation());
    			if(plot != null) {
    				if(plot.isProtected()) {
    					if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
    						
    					}
    					else {
    						Output.simpleError(player, "You cannot do that, "+plot.getName()+" is protected.");
    						event.setCancelled(true);
    						return;
    					}
    				}
    			}
    		}
    	}
    	
    	if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
    		if(blockClicked != null && (blockClicked.getType().equals(Material.CHEST) || blockClicked.getType().equals(Material.FURNACE) || blockClicked.getType().equals(Material.STONE_BUTTON) || blockClicked.getType().equals(Material.LEVER) || blockClicked.getType().equals(Material.WOOD_DOOR) || blockClicked.getType().equals(Material.TRAP_DOOR)))
    		{}
    		else {
    			FoodType foodType = CustomHealingItems.FoodType.getById(player.getItemInHand().getTypeId());
		    	if(foodType != null) {
		    		int stamCost = foodType.getStaminaCost();
		    		int curStam = pseudoPlayer.getStamina();
		    		if(curStam >= stamCost) {
		    			pseudoPlayer.setStamina(curStam-stamCost);
		    			Damageable damag = player;
			    		double curHealth = damag.getHealth();
			    		curHealth += foodType.getHealAmount();
			    		if(foodType.getId() == Material.PORK.getId()) {
			    			int curSkill = pseudoPlayer.getSkill("Lumberjacking");
			    			if(curSkill > 500) {
			    				curHealth += 5;
			    			}
			    		}
			    		if(curHealth > 20)
			    			curHealth = 20;
			    		player.setHealth(curHealth);
			    		
			    		if(foodType.getId() == Material.ROTTEN_FLESH.getId())
			    			player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 600, 1));
			    		
			    		int curFullness = player.getFoodLevel();
			    		double modFoodLevel = foodType.getFullnessAmount();
			    		int survSkill = pseudoPlayer.getSkill("survivalism");
						double multiplier = 1+(double)survSkill/1000;
						modFoodLevel*=multiplier;
						
						curFullness += (int)modFoodLevel;
						if(curFullness > 20)
							curFullness = 20;
						player.setFoodLevel(curFullness);
						
						float curSaturation = player.getSaturation();
						curSaturation += foodType.getSaturation();
						if(curSaturation > 20)
							curSaturation = 20;

						player.setSaturation(curSaturation);
			    		
			    		if(itemInHand.getAmount() > 1) {
			    			itemInHand.setAmount(itemInHand.getAmount()-1);
			    		}
			    		else {
			    			if(itemInHand.getType().equals(Material.MUSHROOM_SOUP))
			    				itemInHand.setType(Material.BOWL);
			    			else
			    				player.getInventory().clear(player.getInventory().getHeldItemSlot());
			    		}
			    		event.setCancelled(true);
		    		}
		    		else Output.simpleError(player, "You do not have enough stamina to eat that, it requires "+stamCost+".");
		    	}
	    	}
    		/*if(player.getItemInHand().getTypeId() == 319) {
	    		int lumberSkill = pseudoPlayer.getSkill("lumberjacking");
	    		float healAmountf = ((float)lumberSkill/1000)*8;
	    		int healAmount = (int)healAmountf;
	    		if(healAmount < 3)
	    			healAmount = 3;
	    		if(healAmount > 8)
	    			healAmount = 8;
	    		player.setHealth(player.getHealth()+healAmount);
	    		if(player.getHealth() > 20)
	    			player.setHealth(20);
	    		player.getInventory().clear(player.getInventory().getHeldItemSlot());
	    		event.setCancelled(true);
	    		return;
	    	}*/
    	}
    	//SpoutPlayer spoutPlayer = SpoutManager.getPlayer(player);
    	
    	if(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
    		int itemInHandId = player.getItemInHand().getTypeId();
	    	if(itemInHandId == 280) {
	    		// make sure we aren't waiting on a spell to cast and we aren't in cooldown
	    		if(pseudoPlayer.getCastDelayTicks() <= 0) {
		    		if(pseudoPlayer.getCantCastTicks() <= 0) {
			    		Spell attachedSpell = pseudoPlayer.getSpellInSlot(player.getInventory().getHeldItemSlot());
			    		if(attachedSpell != null) {
			    			//player.sendMessage("f");
			    			if(attachedSpell.isWandable()) {
			    				//player.sendMessage("spell");
			    				Magery.castSpell(player, attachedSpell, "");
			    			}
			    		}
		    		}
		    		else Output.simpleError(player, "You cannot cast another spell again so soon.");
	    		}
	    		else Output.simpleError(player, "You are already casting a spell.");
	    	}
    	}
    	
    	if(event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
    		if(blockClicked != null) {
    	    	ItemStack itemStack = event.getItem();
    		    if(itemStack.getType().equals(Material.LAVA_BUCKET) || itemStack.getType().equals(Material.WATER_BUCKET) || itemStack.getType().equals(Material.REDSTONE) || itemStack.getType().equals(Material.SIGN)) {
    		    	Plot plot = PlotHandler.findPlotAt(blockClicked.getLocation());
    		    	//If there is a plot there
    		    	if(plot != null) {
    		    		//And it is protected
    		    		if(plot.isProtected()) {
    		    			//And you are not a plot friend/co-owner/owner
    		    			if((pseudoPlayer._plottest == plot) || ( !plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName()) )) {
    		    				//Then you can't drop lava buckets
    		    				Output.simpleError(player, "You cannot use that here, the plot is protected.");
    		    				event.setCancelled(true);
    		    				return;
    		    			}
    		    		}
    		    	}
    		    }
    	    }
    	}
    	
    	if(player.getItemInHand().getType().equals(Material.WOOL)) {
			if(pseudoPlayer._bleedTicks > 0)
			{
				int numInStack = itemInHand.getAmount();
				if(numInStack > 1)
					itemInHand.setAmount(numInStack-1);
				else
					player.getInventory().clear(player.getInventory().getHeldItemSlot());
				
				pseudoPlayer._bleedTicks = 0;
				event.setCancelled(true);
				Output.positiveMessage(player, "You have stopped the bleeding.");
				return;
			}
	    }
    	
    	if(blockClicked!= null && blockClicked.getType().equals(Material.LEAVES)) {
    		if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
    			if(player.getItemInHand().equals(Material.SHEARS)) {
	    			Plot plot = PlotHandler.findPlotAt(blockClicked.getLocation());
	    			if(!plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName())) {
	    				event.setCancelled(true);
	    			}
    			}
    		}
    	}
    	
    	if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
    		Block block = event.getClickedBlock();
    		if(block.getType().equals(Material.IRON_DOOR_BLOCK)) {
    			
    		}
    		if(block.getType().equals(Material.GRASS)) {
    			if(itemInHand.getType().equals(Material.WOOD_HOE) ||
    					itemInHand.getType().equals(Material.STONE_HOE) ||
    					itemInHand.getType().equals(Material.IRON_HOE) ||
    					itemInHand.getType().equals(Material.DIAMOND_HOE) ||
    					itemInHand.getType().equals(Material.GOLD_HOE)) {
    				
    				if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
        				Plot plot = PlotHandler.findPlotAt(block.getLocation());
        				if(plot != null) {
        					if(plot.isProtected()) {
        						if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
        							// we good
        						}
        						else {
        							Output.simpleError(player, "You cannot do that, this plot is protected.");
        							event.setCancelled(true);
        							return;
        						}
        					}
        				}
        				int curSurvSkill = pseudoPlayer.getSkill("survivalism");
        				double percent = (double)curSurvSkill / 1000.0;
        				int hoeRange = (int)(3.0*percent);
        				
        				for(int x=block.getX()-hoeRange; x<= block.getX()+hoeRange; x++) {
        					for(int y=block.getY()-hoeRange; y<=block.getY()+hoeRange; y++) {
        						for(int z=block.getZ()-hoeRange; z<=block.getZ()+hoeRange; z++) {
        							Block blockAt = block.getWorld().getBlockAt(x,y,z);
        							if(Utils.isWithin(block.getLocation(), blockAt.getLocation(), hoeRange)) {
	        							if(block.getWorld().getBlockTypeIdAt(x,y,z) == Material.GRASS.getId()) {
	        								blockAt.setType(Material.SOIL);
	        								double rand = Math.random();
	        								if(rand < .2) {
	        									blockAt.getWorld().dropItemNaturally(new Location(blockAt.getWorld(), blockAt.getLocation().getX()+.5, blockAt.getLocation().getY()+1.5, blockAt.getLocation().getZ()+.5), new ItemStack(Material.SEEDS, 1));
	        								}
	        								else if(rand < .04) {
	        									double rand2 = Math.random();
	        									if(rand2 < .5)
	        										blockAt.getWorld().dropItemNaturally(blockAt.getLocation(), new ItemStack(Material.RED_MUSHROOM, 1));
	        									else 
	        										blockAt.getWorld().dropItemNaturally(blockAt.getLocation(), new ItemStack(Material.BROWN_MUSHROOM, 1));
	        								}
	        							}
        							}
        						}
        					}
        				}
        			}
    			}
    		}
    		if(block.getType().equals(Material.FIRE)) {
    			Plot plot = PlotHandler.findPlotAt(block.getLocation());
				if(plot != null) {
					if(plot.isProtected()) {
						// plot is protected
						if((pseudoPlayer._plottest != plot) && (plot.isOwner(player.getName()) || plot.isCoOwner(player.getName()))) {
							
						}
						else {
							// not owner/co-owner, plot is protected
							Output.simpleError(player, "You cannot do that here, the plot is protected.");
							event.setCancelled(true);
							return;
						}
					}
				}
    		}
    		if(block.getType().equals(Material.PORTAL)) {
    			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
    	    		// we right clicked on a portal
    				Plot plot = PlotHandler.findPlotAt(block.getLocation());
    				if(plot != null) {
    					if(plot.isProtected()) {
    						// plot is protected
    						if((pseudoPlayer._plottest != plot) && (plot.isOwner(player.getName()) || plot.isCoOwner(player.getName()))) {
    							
    						}
    						else {
    							// not owner/co-owner, plot is protected
    							Output.simpleError(player, "You cannot do that here, the plot is protected.");
    							event.setCancelled(true);
    							return;
    						}
    					}
    				}
    				
    				//kill the gate
    				int numMagicStructures = Magery.getMagicStructures().size();
    	    		for(int i=numMagicStructures-1; i>=0; i--) {
    	    			MagicStructure ms = Magery.getMagicStructures().get(i);
    	    			if(ms instanceof Gate) {
    	    				Gate gate = (Gate)ms;
    	    				if(gate.getSourceBlock().equals(block) || gate.getDestBlock().equals(block)) {
    	    					gate.cleanUp();
    	    					if(gate instanceof PermanentGate) {
    	    						PermanentGate pGate = (PermanentGate)gate;
    	    						Database.removePermanentGate(pGate.getId());
    	    						return;
    	    					}
    	    				}
    	    			}
    	    		}
    	    		
    	    		block.setType(Material.AIR);
    	    	}
    		} // end portal
    		
    		/*if(block.getType().equals(Material.PAINTING)) {
    			Plot plot = PlotHandler.findPlotAt(block.getLocation());
    			if(plot != null) {
    				if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
    					
    				}
    				else {
    					System.out.println("asdf");
    					event.setCancelled(true);
    				}
    			}
    		}*/
    		
	    	if(block.getTypeId() == 93 || block.getTypeId() == 94 || block.getTypeId() == 25) {
				Plot plot = PlotHandler.findPlotAt(block.getLocation());
				if(plot != null) {
					if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
						
					}
					else {
						Output.simpleError(player, "Cannot use that, this plot is protected.");
						event.setCancelled(true);
						return;
					}
				}
			}
        	
        	if(block.getType().equals(Material.CHEST) || block.getType().equals(Material.FURNACE) || block.getType().equals(Material.BURNING_FURNACE) || block.getType().equals(Material.DISPENSER) || block.getType().equals(Material.BED_BLOCK)) {
        		/*if(!player.isOp()) {
        			Output.simpleError(player,  "Chests disabled temporarily, working on it (sorry)");
	        		event.setCancelled(true);
	        		return;
        		}*/
        		pseudoPlayer._openedChestTicks = 2;
        		Block targetBlock = player.getTargetBlock(Spell.invisibleBlocks, 10);
        		if(!targetBlock.equals(block)) {
        			Output.simpleError(player, "You can't reach that.");
        			event.setCancelled(true);
        			return;
        		}
        		Plot plot = PlotHandler.findPlotAt(player.getLocation());
        		if(plot != null) {
        			if(!plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName()) && !plot.isFriend(player.getName())) {
        				System.out.println(player.getName()+" opened chest at ("+block.getLocation().getWorld().getName()+", "+block.getLocation().getBlockX()+", "+block.getLocation().getBlockY()+", "+block.getLocation().getBlockZ()+") in "+plot.getName());
        			}
        			else if(RPG._debug)
            			System.out.println(player.getName()+" opened chest at ("+block.getLocation().getWorld().getName()+", "+block.getLocation().getBlockX()+", "+block.getLocation().getBlockY()+", "+block.getLocation().getBlockZ()+")");
        		}
        		
        	}
        	
        	if(block.getType().equals(Material.BED_BLOCK)) {
        		Plot plot = PlotHandler.findPlotAt(block.getLocation());
        		if(plot != null) {
        			if(plot.isCity()) {
        				if(Database.getPlayerMurderCounts(plot.getOwner()) < 5) {
        					// blue town
        					if(plot.isNeutral() || pseudoPlayer.getMurderCounts() < 5) {
        						// pseudoPlayer is blue
        						Output.positiveMessage(player, "You have set your spawn here.");
        						pseudoPlayer.setCustomSpawn(block.getLocation());
        						Database.updatePlayerCustomSpawn(pseudoPlayer);
        					}
        					else {
        						Output.simpleError(player, "Only non-murderers may set spawn in this town.");
        					}
        				}
        				else {
        					// red town
        					if(!plot.isNeutral() && pseudoPlayer.getMurderCounts() < 5) {
        						Output.simpleError(player, "Only murderers may set spawn in this town.");
        					}
        					else {
        						Output.positiveMessage(player, "You have set your spawn here.");
        						pseudoPlayer.setCustomSpawn(block.getLocation());
        						Database.updatePlayerCustomSpawn(pseudoPlayer);
        					}
        				}
        				
        				if(player.getWorld() == RPG._netherWorld) {
                			event.setCancelled(true);
                		}
        			}
        			else Output.simpleError(player, "This bed is not in a town.");
        		}
        		else Output.simpleError(player, "This bed is not in a town.");
        		//event.setCancelled(true);
        	}
        	
        	if(block.getType().equals(Material.STONE_BUTTON) || block.getType().equals(Material.LEVER)) {
    	    	Plot plot = PlotHandler.findPlotAt(new Location(block.getWorld(), block.getX(), block.getY(), block.getZ()));
    	    	if(plot != null) {
    	    		if(plot.isLocked()) {
    	    			if((pseudoPlayer._plottest == plot) || (!plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName()) && !plot.isFriend(player.getName()))) {
    	    				Output.simpleError(player, "Cannot use that, "+plot.getName()+" is locked.");
    	    				event.setCancelled(true);
    	    				return;
    	    			}
    	    		}
    	    		else {
    	    			Location loc = block.getLocation();
    	    			String locString = block.getWorld().getName()+","+loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ();
    	    			if(plot.getLockedBlocks().containsKey(locString)) {
    	    				if((pseudoPlayer._plottest == plot) || (!plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName()) && !plot.isFriend(player.getName()))) {
    	    					Output.simpleError(player, "Cannot use that, it is locked.");
    	    					event.setCancelled(true);
    	    					return;
    	    				}
    	    				else {
    	    					// we good
    	    				}
    	    			}
    	    			else {
    	    				// we good
    	    			}
    	    		}
    	    	}
    	    	if(pseudoPlayer.isRobot()) {
		    		Output.chatLocal(player, "*Boop*", false);
		    	}
        	}
    	}
    	
    	/*if(blockClicked != null && blockClicked.getType().equals(Material.ENCHANTMENT_TABLE)) {
    		blockClicked.setType(Material.AIR);
    		Output.simpleError(player, "You cannot use that.");
    		event.setCancelled(true);
    		return;
    	}*/
    }
    
    /*@Override    
    public void onPlayerItem(PlayerItemEvent event) {
    	if(RPG._debugV)
    		System.out.println("Player Item");
    	
    	Player player = event.getPlayer();*/
    
    	/*int itemInHandId = player.getItemInHand().getTypeId();
    	if((itemInHandId == Material.WOOD_SWORD.getId()) || (itemInHandId == Material.STONE_SWORD.getId()) || (itemInHandId == Material.IRON_SWORD.getId()) || (itemInHandId == Material.DIAMOND_SWORD.getId()) || (itemInHandId == Material.GOLD_SWORD.getId())) {
    		// we are holding a sword...
    		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    		CombatAbility combatAbility = pseudoPlayer.getSwordsmanshipAbility();
    		if(combatAbility != null) {
	    		if(combatAbility.isSet())
	    			combatAbility.unSet(player, pseudoPlayer);
	    		else if(!combatAbility.isSet())
	    			combatAbility.set(player, pseudoPlayer);
    		}
    		else player.sendMessage("You have not selected an ability for this weapon.");
    	}*/
    //fffff
    	/*Block blockClicked = event.getBlockClicked();
    	if(blockClicked != null) {
    	ItemStack itemStack = event.getItem();
	    	if(itemStack.getType().equals(Material.LAVA_BUCKET) || itemStack.getType().equals(Material.WATER_BUCKET) || itemStack.getType().equals(Material.REDSTONE) || itemStack.getType().equals(Material.SIGN)) {
	    		Plot plot = PlotHandler.findPlotAt(blockClicked.getLocation());
	    		//If there is a plot there
	    		if(plot != null) {
	    			//And it is protected
	    			if(plot.isProtected()) {
	    				PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
	    				//And you are not a plot friend/co-owner/owner
	    				if((pseudoPlayer._plottest == plot) || ( !plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName()) )) {
	    					//Then you can't drop lava buckets
	    					Output.simpleError(player, "You cannot use that here, the plot is protected.");
	    					event.setCancelled(true);
	    					return;
	    				}
	    			}
	    		}
	    	}
    	}*/
    	
    	/*if(itemStack.getType().equals(Material.GLOWSTONE_DUST)) {
    		
    	}*/
    	/*else {
	    	if(blockClicked != null) {
		    	if(blockClicked.getType().equals(Material.PORTAL)) {
		    		ArrayList<MagicStructure> magicStructures = Magery.getMagicStructures();
		         	Gate gateFound = null;
		         	for(MagicStructure magicStructure : magicStructures) {
		         		if(magicStructure instanceof Gate) {
		         			gateFound = (Gate)magicStructure;
		         			break;
		         		}
		         	}
		         	if(gateFound != null) {
		         		if(gateFound.isSourceBlock(blockClicked)) {
		         			Location destLoc = gateFound.getDestBlock().getLocation();
		         			player.teleportTo(new Location(destLoc.getWorld(), destLoc.getBlockX()+.5, destLoc.getBlockY(), destLoc.getBlockZ()+.5));
		         		}
		         		else if(gateFound.isDestBlock(blockClicked)) {
		         			Location srcLoc = gateFound.getSourceBlock().getLocation();
		         			player.teleportTo(new Location(srcLoc.getWorld(), srcLoc.getBlockX()+.5, srcLoc.getBlockY(), srcLoc.getBlockZ()+.5));
		         		}
		         	}
		    	}
	    	}
    	}
    }*/
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
    	if(RPG._debugV)
    		System.out.println("Player Respawn");
    	
    	Player player = event.getPlayer();
    	PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    	
    	if(pseudoPlayer._bleedTicks > 0) {
    		player.sendMessage("Your bleeding has stopped.");
			pseudoPlayer._bleedTicks = 0;
    	}
    	pseudoPlayer._stunTicks = 0;
    	pseudoPlayer._goToSpawnTicks = 0;
    	pseudoPlayer._guardAttackTicks = 0;
    	
    	//set respawn ticks so you can't take/deal damage for a few seconds 
    	pseudoPlayer._respawnTicks = 30; // 15 seconds
    	
    	boolean locSet = false;
    	Location customSpawn = pseudoPlayer.getCustomSpawn();
    	
    	if(customSpawn != null) {
    		Plot plot = PlotHandler.findPlotAt(pseudoPlayer.getCustomSpawn());
    		if(plot == null) {
    			Output.simpleError(player, "There is no longer a town at your spawn location.");
				Output.simpleError(player, "Resetting your spawn position to the default.");
				pseudoPlayer.setCustomSpawn(null);
				Database.updatePlayerByPseudoPlayer(pseudoPlayer);
    		}
    		else if(Database.getPlayerMurderCounts(plot.getOwner()) < 5) {
    			// town is blue
    			if(plot.isNeutral() || !pseudoPlayer.isCriminal()) {
    				// valid
    				Utils.loadChunkAtLocation(customSpawn);
    				if(customSpawn.getBlock().getType().equals(Material.BED_BLOCK)) {
    					event.setRespawnLocation(new Location(customSpawn.getWorld(), customSpawn.getX()+.5, customSpawn.getY()+1.1, customSpawn.getZ()+.5));
    					locSet = true;
    					pseudoPlayer._respawnTicks = 0;
    				}
    				else {
    					Output.simpleError(player, "There is no longer a bed at your spawn location.");
    					Output.simpleError(player, "Resetting your spawn position to the default.");
    					pseudoPlayer.setCustomSpawn(null);
    					Database.updatePlayerByPseudoPlayer(pseudoPlayer);
    				}
    			}
    			else {
    				// invalid
    				Output.simpleError(player, "Criminals cannot spawn there, moved to default spawn");
    			}
    		}
    		else {
    			// town is red
    			if(plot.isNeutral() || pseudoPlayer.isCriminal()) {
    				// valid
    				Utils.loadChunkAtLocation(customSpawn);
    				if(customSpawn.getBlock().getType().equals(Material.BED_BLOCK)) {
    					event.setRespawnLocation(new Location(customSpawn.getWorld(), customSpawn.getX()+.5, customSpawn.getY()+1.1, customSpawn.getZ()+.5));
		    			locSet = true;
		    			pseudoPlayer._respawnTicks = 0;
    				}
    				else {
    					Output.simpleError(player, "There is no longer a bed at your spawn location.");
    					Output.simpleError(player, "Resetting your spawn position to the default.");
    					pseudoPlayer.setCustomSpawn(null);
    					Database.updatePlayerByPseudoPlayer(pseudoPlayer);
    				}
    			}
    			else {
    				// invalid
    				Output.simpleError(player, "Non-Criminals cannot spawn there, moved to default spawn");
    			}
    		}
    	}
    	
    	if(!locSet) {
	    	if(pseudoPlayer.isCriminal()) {
	    		event.setRespawnLocation(RPG._murdererSpawn);
	    	}
	    	else { 
	    		event.setRespawnLocation(RPG._blueSpawn);
	    	}
	    	//pseudoPlayer._noDamageTicks = 50;
    	}
    }
    
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
    	/*Entity entity = event.getRightClicked();
		if(entity instanceof Player) {
			Player playerTargetted = (Player)entity;
			PseudoPlayer playerTargettedPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(playerTargetted.getName());
			
			if(playerTargettedPseudoPlayer == null) {
				// Right clicked an NPC
				
				Player player = event.getPlayer();
				PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
				Plot plot = PlotHandler.findPlotAt(playerTargetted.getLocation());
				if(plot == null) {
					Output.simpleError(player, "Hrm, NPC isn't on a plot anymore, contact lucid.");
					event.setCancelled(true);
					return;
				}
				// Random person right clicked the NPC
				PlotNPC plotNPC = NPCName2PlotNPC(playerTargetted.getName(), plot);
				if(plotNPC != null) {
					// Found the correct plotNPC, determine what kind it is
					if(plotNPC.getJob().equals("vendor")) {
						Store store = plotNPCVendor2Store(plotNPC);
						if(store == null)
							System.out.println("nullstore");
						
						pseudoPlayer._lastStoreAccessed = store;
						store.outputList(player);
					}
				}
				event.setCancelled(true);
			}
			else {
				//playerTargetted.setVelocity(new Vector(0,2,0));
			}
		}*/
    	Entity entity = event.getRightClicked();
    	if(entity != null && entity instanceof Villager) 
    	{
    		Output.simpleError(event.getPlayer(), "Trading with villagers disabled until I can determine what to do with their enchanted stuff.");
    		event.setCancelled(true);
    		return;
    	}
    	if(entity != null && entity instanceof Wolf) {
    		Wolf wolf = (Wolf)entity;
    		Player player = event.getPlayer();
    		
    		if(player.getItemInHand().getType().equals(Material.PORK) || player.getItemInHand().getType().equals(Material.GRILLED_PORK)) {
    			if(wolf.isTamed()) {
    				Damageable damag = wolf;
    				if(damag.getHealth() < 20) {
    					PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    					int tamingSkill = pseudoPlayer.getSkill("taming");
    					double healPercent = (double)tamingSkill/1000;
    					int healAmount = (int)Math.ceil(17*healPercent+3);
    					double curHealth = damag.getHealth();
    					double modHealth = curHealth + healAmount;
    					if(modHealth > 20)
    						modHealth = 20;
    					wolf.setHealth(modHealth);
    					player.getItemInHand().setAmount(player.getItemInHand().getAmount()-1);
    					event.setCancelled(true);
    				}
    			}
    		}
    	}
	}
    
    public static PlotNPC NPCName2PlotNPC(String NPCName, Plot plot) {
    	ArrayList<PlotNPC> plotNPCs = plot.getPlotNPCs();
		for(int i=0; i<plotNPCs.size(); i++) {
			if(plotNPCs.get(i).getName().equals(NPCName)) {
				return plotNPCs.get(i);
			}
		}
		return null;
    }
    
    public static Store plotNPCVendor2Store(PlotNPC plotNPC) {
    	int storeId = -1;
		try { storeId = Integer.parseInt(plotNPC.getAdditional()); }
		catch(Exception e) {}
		
		if(storeId > 0) {
			for(int i=0; i<RPG._stores.size(); i++) {
				if(RPG._stores.get(i).getId() == storeId) {
					return RPG._stores.get(i);
				}
			}
		}
		return null;
    }
		
    
    private void payPlayer(Player player, String[] split) {
    	if(split.length == 3) {
    		String targetName = split[1];
    		Player targetPlayer = plugin.getServer().getPlayer(targetName);
    		if(targetPlayer != player) {
	    		if(targetPlayer != null) {
	    			if(targetPlayer.isOp()) {
	    				PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
	    				if(targetPseudoPlayer._secret) {
	    					Output.simpleError(player, "That player is not online.");
	    					return;
	    				}
	    			}
		    		int amount;
		    		try { amount = Integer.parseInt(split[2]); }
		    		catch(Exception e) { amount = -1; }
		    		if(amount > 0) {
		    			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		    			if(pseudoPlayer.getMoney() >= amount) {
		    				PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
		    				pseudoPlayer.setMoney(pseudoPlayer.getMoney()-amount);
		    				targetPseudoPlayer.setMoney(targetPseudoPlayer.getMoney()+amount);
		    				Output.positiveMessage(player, "You have paid "+targetPlayer.getName()+" "+amount+" gold coins.");
		    				Output.positiveMessage(targetPlayer, player.getName()+" has paid you "+amount+" gold coins.");
		    				Database.updatePlayerByPseudoPlayer(pseudoPlayer);
		    				Database.updatePlayerByPseudoPlayer(targetPseudoPlayer);
		    				if(pseudoPlayer.isRobot()) {
					    		Output.chatLocal(player, "*Bloop Ding*", false);
					    	}
		    			}
		    			else Output.simpleError(player, "You do not have that much money.");
		    		}
		    		else Output.simpleError(player, "Invalid amount, use amount greater than 0.");
	    		}
	    		else Output.simpleError(player, "That player is not online.");
    		}
    		else Output.simpleError(player, "You cannot pay yourself.");
    	}
    	else Output.simpleError(player, "Use /pay (player name) (amount)");
    }
    
    private void tradeGold(Player player, String[] split) {
    	if(split.length == 2) {
    		try {
    			int numGold = Integer.parseInt(split[1]);
    			int numGoldHave = 0; 
				for (Map.Entry<Integer,? extends ItemStack> i: player.getInventory().all(266).entrySet()) {
					numGoldHave += i.getValue().getAmount();
				}
				if(numGoldHave >= numGold) {
					player.getInventory().removeItem(new ItemStack(266, numGold));
					PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
					pseudoPlayer.setMoney(pseudoPlayer.getMoney()+(numGold*100));
					Database.updatePlayerByPseudoPlayer(pseudoPlayer);
					Output.positiveMessage(player, "You have traded "+numGold+" gold ingots for "+(numGold*100)+" gold coins.");
					if(pseudoPlayer.isRobot()) {
			    		Output.chatLocal(player, "*Bloop Ding*", false);
			    	}
				}
				else Output.simpleError(player, "You do not have that much gold.");
    		}
    		catch(Exception e) {
    			Output.simpleError(player, "Invalid amount.");
    		}
    	}
    	else Output.simpleError(player, "Use /tradegold (amount)");
    }
    
    private void trade(Player player, String[] split) {
    	if(split.length == 3) {
    		if(split[1].equalsIgnoreCase("accept")) {
    			int id;
    			try {
    				id = Integer.parseInt(split[2]);
    			}
    			catch(Exception e) {
    				id = -1;
    			}
    			if(id != -1) {
    				Trade foundTrade = null;
    				for(Trade trade : RPG._trades) {
    					if(trade.getUniqueKey() == id) {
    						foundTrade = trade;
    						break;    						
    					}
    				}
    				if(foundTrade != null) {
    					RPG._trades.remove(foundTrade);
    					Player offererPlayer = plugin.getServer().getPlayer(foundTrade.getOffererName());
    					if(offererPlayer != null) {
    						ItemStack itemInSlot = offererPlayer.getInventory().getItem(foundTrade.getItemSlot()-1);
    						ItemStack itemInTrade = foundTrade.getItemStack();
    						if(itemInTrade.getType().equals(itemInSlot.getType()) && itemInTrade.getAmount() == itemInSlot.getAmount()) {
    							PseudoPlayer offererPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(offererPlayer.getName());
    							PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    							int goldAmount = foundTrade.getAmount();
    							if(pseudoPlayer.getMoney() >= goldAmount) {
    								pseudoPlayer.setMoney(pseudoPlayer.getMoney()-goldAmount);
    								offererPseudoPlayer.setMoney(offererPseudoPlayer.getMoney()+goldAmount);
    								offererPlayer.getInventory().clear(foundTrade.getItemSlot()-1);
        							player.getInventory().addItem(foundTrade.getItemStack());
        							Output.positiveMessage(player, "Traded "+goldAmount+" gold coins for "+itemInTrade.getAmount()+" "+itemInTrade.getType().name()+".");
        							Output.positiveMessage(offererPlayer, "Traded "+itemInTrade.getAmount()+" "+itemInTrade.getType().name()+" for "+goldAmount+" gold coins.");
    							}
    							else {
    								Output.simpleError(player, "You cannot afford that trade, trade cancelled.");
        							Output.simpleError(offererPlayer, player.getName()+"Does not have enough money, trade cancelled.");
    							}
    						}
    						else {
    							Output.simpleError(player, "Offerer no longer has items for trade, trade cancelled.");
    							Output.simpleError(offererPlayer, "You no longer have the items for trade, trade cancelled.");
    						}
    					}
    					else Output.simpleError(player, "Offering player is no longer online, trade cancelled.");
    				}
    				else Output.simpleError(player, "Trade offer not found.");
    			}
    			else Output.simpleError(player, "Invalid trade id.");
    		}
    	}
    	else if(split.length == 4) {
    		if(split[1].equalsIgnoreCase("offer")) {
    			String targetName = split[2];
    			Player targetPlayer = plugin.getServer().getPlayer(targetName);
    			if(targetPlayer == null) {
    				Output.simpleError(player, targetName+" is not currently online.");
    				return;
    			}
    			
    			if(!Utils.isWithin(player.getLocation(), targetPlayer.getLocation(), 10)) {
    				Output.simpleError(player, "Too far from "+targetPlayer.getName()+" to offer trade.");
    				return;
    			}
    			
    			int itemSlot = player.getInventory().getHeldItemSlot();
    			ItemStack itemInHand = player.getItemInHand();
    			if(itemInHand == null || itemInHand.getType().equals(Material.AIR)) {
    				Output.simpleError(player, "Cannot trade nothing. Use \"/pay (amount)\" to give money.");
    				return;
    			}
    			itemSlot++;
    			
        		int amount;
        		try {
        			amount = Integer.parseInt(split[3]);
        		}
        		catch(Exception e) {
        			amount = -1;
        		}
        		
        		if(amount <= 0) {
        			Output.simpleError(player, "Invalid amount, must be greater than 0.");
        			return;
        		}
        		
        		int uniqueId = -1;
        		boolean ccontinue = true;
        		// loop until you get a unique id
        		while(ccontinue) {
        			boolean foundMatching = false;
        			uniqueId = (int)Math.floor(Math.random()*9999999);
        			for(Trade trade : RPG._trades) {
        				if(trade.getUniqueKey() == uniqueId)
        					foundMatching = true;
        			}
        			if(!foundMatching)
        				ccontinue = false;
        		}
        		Trade trade = new Trade(player.getName(), targetName, itemInHand, amount, uniqueId, itemSlot);
        		Output.positiveMessage(player, "Offered "+itemInHand.getAmount()+" "+itemInHand.getType().name()+" for "+trade.getAmount()+" gold coins to "+targetPlayer.getName()+".");
        		Output.positiveMessage(targetPlayer, player.getName()+" offers "+itemInHand.getAmount()+" "+itemInHand.getType().name()+" for "+trade.getAmount()+" gold coins.");
        		Output.positiveMessage(targetPlayer, "Use /trade accept "+trade.getUniqueKey());
        		RPG._trades.add(trade);
        	}
    	}
    	else if (split.length > 1) {
    		if(split[1].equalsIgnoreCase("offer"))
    			Output.simpleError(player, "Use \"/trade offer (target name) (gold amount)\" to offer item in hand.");
    		else if(split[1].equalsIgnoreCase("accept"))
    			Output.simpleError(player, "Use /trade accept (trade id)");
    	}
    	else {
    		Output.simpleError(player, "Use \"/help\" to view the help menu.");
    	}
    }

    
    private void handleRunebook(Player player, String[] split) {
    	if(split.length == 1 || (split.length >= 2 && split[1].equalsIgnoreCase("page"))) {
        	Output.outputRunebook(player, split);
    	}
    	else if(split.length > 1) {
    		String secondaryCommand = split[1];
    		if(secondaryCommand.equalsIgnoreCase("give")) {
    			if(split.length >= 4) {
    				String targetName = split[2];
    				Player targetPlayer = Utils.getPlugin().getServer().getPlayer(targetName);
    				if(targetPlayer != null) {
    					PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
    					
    					if(targetPseudoPlayer._secret) {
    						Output.simpleError(player, "That player is not online.");
    						return;
    					}
    					
    					PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    					Runebook runebook = pseudoPlayer.getRunebook();
    					Runebook targetRunebook = targetPseudoPlayer.getRunebook();
    					if(targetPlayer.isOp() || (targetPseudoPlayer.isLargerBank() && targetRunebook.getNumRunes() < 16) || targetRunebook.getNumRunes() < 8) {
    						
    						String runeLabel = "";
    						int splitLength = split.length;
    						for(int i=3; i<splitLength; i++) {
    							runeLabel += split[i];
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
    								Database.updateRune(targetPlayer, targetPseudoPlayer, foundRune);
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
    			if(split.length >= 3) {
    				
    				String runeLabel = "";
					int splitLength = split.length;
					for(int i=2; i<splitLength; i++) {
						runeLabel += split[i];
						if(i < splitLength-1)
							runeLabel+= " ";
					}
					runeLabel = runeLabel.trim();
    				
    				PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
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
    					Database.removeRune(foundRune);
    				}
    				else Output.simpleError(player, "Could not find a rune with that label.");
    			}
    			else Output.simpleError(player, "Use /runebook remove (rune label)");
    		}
    	}
    }
    
    public void vendor(Player player, String[] split) {
    	if(split.length == 1) {
    		Output.simpleError(player, "Use \"/vendor list\" to see a vendor's wares.");
    		return;
    	}
    	String subCommand = split[1];
    	if(subCommand.equalsIgnoreCase("list")) {
    		
    		Store storeAt = findClosestVendor(player);
    		
    		if(storeAt == null) {
				Output.simpleError(player, "You are not near a vendor.");
				return;
			}
    		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    		pseudoPlayer._lastStoreAccessed = storeAt;
    		storeAt.outputList(player);
    	}
    	else if(subCommand.equalsIgnoreCase("buy")) {
    		if(split.length == 4) {
    			Store storeAt = findClosestVendor(player);
    			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    			//System.out.println(storeAt.getName() + ", "+pseudoPlayer._lastStoreAccessed.getName());
    			if(storeAt != pseudoPlayer._lastStoreAccessed) {
    				Output.simpleError(player, "This command buys the chosen item from the closest vendor, which is different than the last vendor you used. Please stand closer to the vendor you would like to buy from and say /vendor list.");
    				return;
    			}
    			if(storeAt != null) {
    				Plot plot = PlotHandler.findPlotAt(storeAt.getLocation());
    				if(plot.isControlPoint()) {
        				if(!plot.isPlayerInOwnedClan(player, pseudoPlayer)) {
        					Output.simpleError(player,  "Only members of the clan who owns "+plot.getName()+" may use this vendor.");
        					return;
        				}
        			}
    				
    				int numItems = storeAt.getNumItems();
	    			try {
	    				int itemNumber = Integer.parseInt(split[2]);
	    				int quantity = Integer.parseInt(split[3]);
	    				if(quantity >= 1) {
		    				if(itemNumber >= 1 && itemNumber <= numItems) {
		    					itemNumber--;
		    					ItemStack itemStack = storeAt.getItem(itemNumber);
		    					int storeQuantity = storeAt.getQuantity(itemNumber);
		    					if(storeQuantity >= quantity) {
		    						int pricePerUnit = storeAt.getPrice(itemNumber);
		    						int totalPrice = pricePerUnit * quantity;
		    						int curMoney = pseudoPlayer.getMoney();
		    						if(curMoney >= totalPrice) {
		    							ItemStack itemToGive = new ItemStack(itemStack.getTypeId(), 1);
		    							itemToGive.setDurability(itemStack.getDurability());
		    							
		    							for(int i=0; i<quantity; i++) {
		    								player.getInventory().addItem(itemToGive);
		    							}
		    							pseudoPlayer.setMoney(pseudoPlayer.getMoney()-totalPrice);
		    							storeAt.setStock(itemNumber, storeQuantity-quantity);
		    							if(storeAt.getType() == 1 && storeQuantity-quantity <= 0) {
		    								storeAt.removeItem(itemNumber);
		    							}
		    							plot.setMoney(plot.getMoney()+(int)(Math.floor(totalPrice*.9)));
		    							Database.updateStore(storeAt);
		    							Database.updatePlayerByPseudoPlayer(pseudoPlayer);
		    							Database.updatePlot(plot);
		    							Output.positiveMessage(player, "Purchased "+quantity+" "+itemToGive.getType().name()+" for $"+totalPrice+".");
		    						}
		    						else Output.simpleError(player, "You cannot afford those goods.");
		    					}
		    					else Output.simpleError(player, storeAt.getName()+" does not have "+quantity+" of that item.");
		    				}
		    				else Output.simpleError(player, "That item does not exist.");
	    				}
	    				else Output.simpleError(player, "Invalid quantity.");
	    			}
	    			catch(Exception e) {
	    				Output.simpleError(player, "Use \"/vendor buy (item number) (quantity)\"");
	    			}
    			}
    			else Output.simpleError(player, "You are not near a vendor.");
    		}
    		else Output.simpleError(player, "Use \"/vendor buy (item number) (quantity)\"");
    	}
    	else if(subCommand.equalsIgnoreCase("add")) {
    		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    		if(split.length >= 3) {
    			Plot plot = PlotHandler.findPlotAt(player.getLocation());
    			if(!plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName())) {
    				Output.simpleError(player, "You may only add items to vendors in which you are the owner or co-owner.");
    				return;
    			}
    			
    			int splitNameLength = split.length;
    			String npcName = "";
    			for(int i=2; i<splitNameLength; i++) {
    				npcName += split[i];
    				if(i < splitNameLength-1)
    					npcName+= " ";
    			}
    			npcName = npcName.trim();
    			
    			PlotNPC foundNPC = null;
    			ArrayList<PlotNPC> plotNPCs = plot.getPlotNPCs();
    			for(PlotNPC p : plotNPCs) {
    				if(p.getName().equalsIgnoreCase(npcName)) {
    					foundNPC = p;
    				}
    			}
    			
    			if(foundNPC == null) {
    				Output.simpleError(player, "That NPC was not found on the plot you are in.");
    				return;
    			}
    			
    			if(!foundNPC.getJob().equalsIgnoreCase("vendor")) {
    				Output.simpleError(player, "That NPC is not a vendor, you may only add items to vendors.");
    				return;
    			}
    			
    			int storeId = -1;
    			try { storeId = Integer.parseInt(foundNPC.getAdditional()); }
    			catch(Exception e) {}
    			if(storeId < 0) {
    				Output.simpleError(player, "Something went wrong :O");
    				return;
    			}
    			
    			Store storeFound = null;    			
    			for(Store store : RPG._stores) {
    				if(store.getId() == storeId) {
    					storeFound = store;
    				}
    			}
    			
    			if(storeFound == null) {
    				Output.simpleError(player, "Something went wrong :O");
    				return;
    			}
    			
    			//int pricePerUnit = hasValue
    			ItemStack itemInHand = player.getItemInHand();
    			int itemId = itemInHand.getTypeId();
    			int amount = itemInHand.getAmount();
    			int durability = itemInHand.getDurability();
    			
    			//ItemStack foundItem = null;
				int foundIndex = -1;
				ArrayList<ItemStack> items = storeFound.getItemsForSale();
				for(int i=0; i<items.size(); i++) {
					if(items.get(i).getTypeId() == itemId && items.get(i).getDurability() == durability) {
						//foundItem = items.get(i);
						foundIndex = i;
						break;
					}
				}
				
				//if(foundItem != null) {
				if(foundIndex != -1) {
					//existing item in the vendor
					//foundItem.setAmount(foundItem.getAmount()+amount);
					storeFound.getItemStocks().set(foundIndex, storeFound.getItemStocks().get(foundIndex)+ amount);
					player.getInventory().clear(player.getInventory().getHeldItemSlot());
					Database.updateStore(storeFound);
					Output.positiveMessage(player, "You added "+amount+" "+Material.getMaterial(itemId).name()+".");
				}
				else {
					//not existing item
					if(storeFound.getItemsForSale().size() >= 8) {
						Output.simpleError(player, "That vendor already has 8 different items in it.");
						return;
					}
					Output.positiveMessage(player, "How much do you want to charge for each "+Material.getMaterial(itemId).name()+"? (Say the price or say cancel)");
					pseudoPlayer._addingSaleItem = itemInHand;
					pseudoPlayer._addingSaleStore = storeFound;
				}
    		}
    	}
    	else if(subCommand.equalsIgnoreCase("create") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.vendor.create")) {
    		int size = -1;
    		try {size = Integer.parseInt(split[2]); }
    		catch(Exception e) {}
    		if(size == -1)
    			return;
    		String storeName = implodeSplit(split, 3);
    		Store store = new Store(storeName, size, player.getLocation());
    		int id = Database.addStore(store);
    		store.setId(id);
    		RPG._stores.add(store);
    		Output.positiveMessage(player, "Added store: " + storeName);
    	}
    	else if(subCommand.equalsIgnoreCase("remove") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.vendor.remove")) {
    		int id = -1;
    		try {id = Integer.parseInt(split[2]); }
    		catch(Exception e) {}
    		if(id == -1)
    			return;
    		for(Store store : RPG._stores) {
    			if(store.getId() == id) {
    				RPG._stores.remove(store);
    				Database.removeStore(store);
    				break;
    			}
    		}
    		Output.positiveMessage(player, "Removed store");
    	}
    	else if(subCommand.equalsIgnoreCase("adminadd") && player.isOp()) {// && RPG.Permissions.has(player, "rpgplugin.add")) {
    		try {
    			int storeId = Integer.parseInt(split[2]);
    			int itemId = Integer.parseInt(split[3]);
    			int stock = Integer.parseInt(split[4]);
    			int cost = Integer.parseInt(split[5]);
    			int restockRate = Integer.parseInt(split[6]);
    			for(Store store : RPG._stores) {
    				if(store.getId() == storeId) {
    					store.addItem(itemId, stock, (short)0, cost, restockRate);
    					Database.updateStore(store);
    					break;
    				}
    			}
    		}
    		catch(Exception e) {}
    		Output.positiveMessage(player, "Added item");
    	}
    }

	private static String implodeSplit(String[] split, int startIndex) {
		String message = "";
    	int splitMessageLength = split.length;
		for(int i=startIndex; i<splitMessageLength; i++) {
			message += split[i];
			if(i < splitMessageLength-1)
				message+= " ";
		}
		message = message.trim();
		return message;
	}
    
    private Store findClosestVendor(Player player) {
    	ArrayList<Store> isWithin = new ArrayList<Store>();
    	for(Store store : RPG._stores) {
    		if(store.getLocation().getWorld().getName().equalsIgnoreCase(player.getWorld().getName())) {
	    		if(Utils.isWithin(store.getLocation(), player.getLocation(), store.getSize()))
	    			isWithin.add(store);
    		}
    	}
    	if(isWithin.size() > 0) {
	    	Store closestStore = isWithin.get(0);
	    	double closestDist = Utils.distance(isWithin.get(0).getLocation(), player.getLocation());
	    	int numWithin = isWithin.size();
	    	for(int i=1; i<numWithin; i++) {
	    		double dist = Utils.distance(isWithin.get(i).getLocation(), player.getLocation());
	    		if(dist < closestDist) {
	    			closestDist = dist;
	    			closestStore = isWithin.get(i);
	    		}
	    	}
	    	return closestStore;
    	}
    	return null;
    }
    
    public void guards(Player player, PseudoPlayer pseudoPlayer, String message) {
    	// and if the player is not a criminal
		if(!pseudoPlayer.isCriminal()) {
			Player[] players = Utils.getPlugin().getServer().getOnlinePlayers();
			// loop through the all players
			for(Player p : players) {
				if(!NPCHandler.isBeingGuardAttacked(p)) {
					// if any of the players are within 10 meters of the person who shouted guards
					if(Utils.isWithin(player.getLocation(), p.getLocation(), 10)) {
						PseudoPlayer pseudoPlayerTarget = PseudoPlayerHandler.getPseudoPlayer(p.getName());
						// if the player is a criminal
						if(pseudoPlayerTarget.isCriminal() && !pseudoPlayerTarget._convenient) {
							// determine the plot that the target is standing in
							Plot plot = PlotHandler.findPlotAt(p.getLocation());
							// if the target is even in a plot
							if(plot != null) {
	    						// loop through the guards and find one that is in the plot
	    						ArrayList<HumanNPC> guards = NPCHandler.getGuards();
	    						for(HumanNPC g : guards) {
	    							//if(!NPCHandler.isGuardAttacking(g)) {
		    							// if the guard is within the plot that the target is standing in
		    							if(Utils.isWithin(g.getBukkitEntity().getLocation(), plot.getLocation(), plot.getRadius())) {
		    								// and the guard is not null
		    		    					if(g != null) {
		    		    						p.damage(200.0);
		    		    						// sic the guard on him
		    		    						//NPCHandler.guardAttack(g, p);
		    		    						//System.out.println("guard attack");
		    		    						break;
		    		    					}
		    		    					// else guard is null
		    							}
		    							// else guard is not in the plot
	    							//}
	    							// else the guard is already attacking a guy
	    						}
	    						// end loop through guards
							}
							// else plot is null
						}
						// else target isn't a criminal
					}
					// else the player isn't within 10 blocks of the player
				}
				// else the player is already being attacked by a guard
			}
			// end loop through players
		}
		// else player is criminal, criminals can't call guards
    } 
    
    private void taunt(Player player, PseudoPlayer pseudoPlayer, String[] split) {
    	if(split.length > 1) {
    		if(split[1].equalsIgnoreCase("set")) {
    			int numSplit = split.length;
    			String message = "";
    			for(int i=2; i<numSplit; i++) {
    				message += (split[i] + " ");
    			}
    			if(message.length() > 45) {
    				Output.simpleError(player, "Taunt too long, must be 45 characters or less.");
    				return;
    			}
    			/*if(message.contains("'") || message.contains("\"")) {
    				Output.simpleError(player, "Cannot use ' or \"");
    				return;
    			}*/
    			pseudoPlayer.setTaunt(message);
    			Database.updatePlayerByPseudoPlayer(pseudoPlayer);
    			Output.positiveMessage(player, "You have set your taunt, use /taunt to use it.");
    		}
    	}
    	else {
    		Output.chatShout(player, pseudoPlayer.getTaunt());
    	}
    }
    
    private static Location checkPortals(Player player) {
		//System.out.println("Teleporting");
    	PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
    	if(pseudoPlayer._recentlyTeleportedTicks > 0)
    		return null;
    	
		Block blockAt = player.getLocation().getBlock();
		ArrayList<MagicStructure> magicStructures = Magery.getMagicStructures();
		Gate gateFound = null;
		for(MagicStructure magicStructure : magicStructures) {
			if(magicStructure instanceof Gate) {
				Gate gate = (Gate)magicStructure;
				if(gate.isSourceBlock(blockAt) || gate.isDestBlock(blockAt)) {
					gateFound = gate;
					break;
				}
			}
		}
		if(gateFound != null) {
			Location targetLoc = null;
			if(gateFound.isSourceBlock(blockAt)) {
				targetLoc = gateFound.getDestBlock().getLocation();
			}
			else if(gateFound.isDestBlock(blockAt)) {
				targetLoc = gateFound.getSourceBlock().getLocation();
			}
			return targetLoc;
		}
		return null;
	}
}