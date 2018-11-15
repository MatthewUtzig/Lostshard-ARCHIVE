package com.lostshard.RPG;
import java.awt.Color;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TimerTask;

import me.neodork.npclib.NPCManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.lostshard.RPG.Events.WorldEvent;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Groups.Clans.ClanHandler;
import com.lostshard.RPG.Groups.Parties.Party;
import com.lostshard.RPG.Listeners.RPGBlockListener;
import com.lostshard.RPG.Listeners.RPGEntityListener;
import com.lostshard.RPG.Listeners.RPGPlayerListener;
import com.lostshard.RPG.Listeners.RPGServerListener;
import com.lostshard.RPG.Listeners.RPGVehicleListener;
import com.lostshard.RPG.Listeners.RPGWorldListener;
import com.lostshard.RPG.Plots.Bank;
import com.lostshard.RPG.Plots.NPCHandler;
import com.lostshard.RPG.Plots.PlotHandler;
import com.lostshard.RPG.Plots.Store;
import com.lostshard.RPG.Skills.Camp;
import com.lostshard.RPG.Skills.Fishing;
import com.lostshard.RPG.Skills.Healing;
import com.lostshard.RPG.Skills.Magery;
import com.lostshard.RPG.Skills.Survivalism;
import com.lostshard.RPG.Spells.MagicStructure;
import com.lostshard.RPG.Spells.Spell;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

import it.sauronsoftware.cron4j.Scheduler;

import com.ensifera.animosity.craftirc.*;

/**
 * RPG for Bukkit
 *
 * @author luciddream
 */
public class RPG extends JavaPlugin implements EndPoint{
    private RPGPlayerListener playerListener;// = new RPGPlayerListener(this);
    private RPGBlockListener blockListener;// = new RPGBlockListener(this);
    public RPGEntityListener entityListener;// = new RPGEntityListener(this);
    public RPGWorldListener worldListener;// = new RPGWorldListener();
    public RPGVehicleListener vehicleListener;// = new RPGVehicleListener();
    public RPGServerListener serverListener;// = new RPGServerListener(this);
    public MapListener mapListener;// = new MapListener(this);
    
    // CraftIRC
    private final String craftIRCPluginTag = "plugin";
    //private final String adminChannelTag = "synirclostshard-admin";
    
    //private Timer _tickTimer;
    private static Server _plugin;
    //public static PermissionHandler Permissions = null;
    public static ArrayList<Trade> _trades = new ArrayList<Trade>();
    public static ArrayList<Party> _parties = new ArrayList<Party>();
    public static ArrayList<Bank> _banks = new ArrayList<Bank>();
    public static ArrayList<Store> _stores = new ArrayList<Store>();
    
    //public static HashSet<Integer> _summonedMobsHashSet = new HashSet<Integer>();
    public static HashMap<Integer, String> _summondMobsOwnerHashSet = new HashMap<Integer, String>();
    
    public static HashSet<String> _placedLogStrings = new HashSet<String>();
    
    public static ArrayList<RandChest> _randChests;
    
    public static HashSet<String> _permChunks = new HashSet<String>();
    public static ArrayList<Block> _doomedBlocks = new ArrayList<Block>();
    public static ArrayList<RoboExplosion> _roboExplosions = new ArrayList<RoboExplosion>();
    
    public static ArrayList<Integer> _primedPlotTNT = new ArrayList<Integer>();
    
    private int _tickCount = 0;
    private int _curBank = 0;
    public static String _kick = null;
    public static boolean _damage = false;
    public static boolean _canLogin = true;
    
    public static Location _murdererSpawn;
    public static Location _blueSpawn;
    
    public static World _normalWorld;
    public static World _theEndWorld;
    public static World _theEndWorld2;
    public static World _netherWorld;
    public static World _extraWorld;
    public static World _hungryWorld;
    public static World _farts;
    public static World _newmap;
    public static World _tutorialWorld;
    
    public static boolean _portalPhysics = true;
    
    public static ArrayList<String> _castingDayPlayers = new ArrayList<String>();
    public static ArrayList<String> _castingClearSkyPlayers = new ArrayList<String>();
    
    public static WorldEvent _worldEvent = null;
    
    public static boolean _debug = true;
    public static boolean _debugV = false;
    
    public static Location _orderBuilderLocation;
    public static Location _chaosBuilderLocation;
    
    public static Location _tutorialLocation;
    
    public static Location _murderShrineLocation;
    public static final int COST_PER_MURDER = 250;
    
    public static CraftIRC craftircHandle;
    
    private long _lastTickTime = 0;
    
    public static boolean _shuttingDown = false;
    
    public static Location _buntCPCenter;
    public static Location _gorpCPCenter;
    public static Location _vesperCPCenter;
    public static int _buntCPRange;
    public static int _gorpCPRange;
    public static int _vesperCPRange;
    
    public static NPCManager _npcManager;
    
    public static Server getPlugin() {
    	return _plugin;
    }
    
    class Tick extends TimerTask {
		public void run() {	
			
			if(_shutdownTicks > 0) {
				_shutdownTicks--;
				if(_shutdownTicks == 0) {
					System.out.println("SHUTTINGDOWN");
					shutDown();
				}
			}
			
			Date date = new Date();
			double delta = 1;
			if(_lastTickTime == 0)
				_lastTickTime = date.getTime();
			else {
				double diff = (double)(date.getTime() - _lastTickTime);
				delta = diff/100;
				_lastTickTime = date.getTime();
			}
			
			//Date d1 = new Date();
			_tickCount++;
			
			if(_tickCount >= 1000) {
				Database.rewardVotes();
				System.out.println("HandleVotes");
				//System.out.println("Number of NPCs Loaded: " +NPCHandler._npcManager.HumanNPCList.size());
				_tickCount = 0;
				
				if(_debugV) {
					System.out.println("-Chunks-");
					System.out.println("# NormalWorld Chunks Loaded: " + RPG._normalWorld.getLoadedChunks().length);
					//System.out.println("# NetherWorld Chunks Loaded: " + RPG._netherWorld.getLoadedChunks().length);
					System.out.println("-LivingEntities-");
					System.out.println("# NormalWorld LivingEntities: " + RPG._normalWorld.getLivingEntities().size());
					//System.out.println("# NetherWorld LivingEntities: " + RPG._netherWorld.getLivingEntities().size());
					System.out.println("-Entities-");
					System.out.println("# NormalWorld Entities Loaded: " + RPG._normalWorld.getEntities().size());
					//System.out.println("# NetherWorld Entities Loaded: " + RPG._netherWorld.getEntities().size());
					System.out.println("--------");
				}
				
				//System.out.println("Average time over 1000 ticks: " + time/1000);
				//time=0;
			}
			
			// This will be run every 10th of a second
			PseudoPlayerHandler.tick(delta);
			Survivalism.tick();
			Magery.tick();
			Healing.tick(delta);
			
			try {
				NPCHandler.tick();
			}
			catch(Exception e) {
				System.out.println("NPCBUG");
			}
			
			PlotHandler.tick(delta);
			
			int numCastingDayPlayers = _castingDayPlayers.size();
			if(numCastingDayPlayers > 0) {
				if(numCastingDayPlayers >= 2) {
					for(int i=numCastingDayPlayers-1; i>=0; i--) {
						String pName = _castingDayPlayers.get(i);
						Player p = Utils.getPlugin().getServer().getPlayer(pName);
						if(p != null) {
							PseudoPlayer pP = PseudoPlayerHandler.getPseudoPlayer(p.getName());
							pP._isCastingDay = false;
							
							// Tell the casters they succeeded
							String castSuccessText = "";
							
							for(int f=0; f<numCastingDayPlayers; f++) {
								String castingName = _castingDayPlayers.get(f);
								if(castingName.equalsIgnoreCase(p.getName()))
									castSuccessText += "You";
								else
									castSuccessText += castingName;
								if(f<numCastingDayPlayers-2)
									castSuccessText += ", ";
								if(f<numCastingDayPlayers-1)
									castSuccessText += " and ";
								if(f==numCastingDayPlayers-1)
									castSuccessText += " successfully cast Day.";
							}
							
							for(int f=0; f<20; f++) {
								double castX = Math.random() * 30;
								if(Math.random() < .5)
									castX = -castX;
								castX = p.getLocation().getX()+castX;
								double castZ = Math.random() * 30;
								if(Math.random() < .5)
									castZ = -castZ;
								castZ = p.getLocation().getZ()+castZ;
								
								Location strikeLocation = new Location(p.getWorld(), castX, p.getWorld().getHighestBlockYAt((int)Math.floor(castX), (int)Math.floor(castZ)), castZ);
								p.getWorld().strikeLightningEffect(strikeLocation);
							}
							Output.positiveMessage(p, castSuccessText);
						}
					}
					_castingDayPlayers.clear();
					RPG._normalWorld.setTime((long)600);
				}
				else {
					for(int i=numCastingDayPlayers-1; i>=0; i--) {
						String playerName = _castingDayPlayers.get(i);
						Player p = Utils.getPlugin().getServer().getPlayer(playerName);
						
						if(p == null) {
							_castingDayPlayers.remove(i);
							continue;
						}
						
						PseudoPlayer pp = PseudoPlayerHandler.getPseudoPlayer(p.getName());
						if(!pp._isCastingDay) {
							_castingDayPlayers.remove(i);
							continue;
						}
						
						// Dude is definitely still casting day.
						
						pp.setMana(pp.getMana()-1);
						if(pp.getMana() <= 0) {
							pp.setMana(0);
							pp._isCastingDay = false;
							Output.simpleError(p, "You have run out of mana and stopped casting Day.");
							_castingDayPlayers.remove(i);
							continue;
						}
						
						// Mana handled, lets do the effects
						if(Math.random() < .2) {
							double castX = Math.random() * 30;
							if(Math.random() < .5)
								castX = -castX;
							castX = p.getLocation().getX()+castX;
							double castZ = Math.random() * 30;
							if(Math.random() < .5)
								castZ = -castZ;
							castZ = p.getLocation().getZ()+castZ;
							
							Location strikeLocation = new Location(p.getWorld(), castX, p.getWorld().getHighestBlockYAt((int)Math.floor(castX), (int)Math.floor(castZ)), castZ);
							p.getWorld().strikeLightningEffect(strikeLocation);
						}
					}
				}
			}
			
			int numCastingClearSkyPlayers = _castingClearSkyPlayers.size();
			if(numCastingClearSkyPlayers > 0) {
				if(numCastingClearSkyPlayers >= 2) {
					for(int i=numCastingClearSkyPlayers-1; i>=0; i--) {
						String pName = _castingClearSkyPlayers.get(i);
						Player p = Utils.getPlugin().getServer().getPlayer(pName);
						if(p != null) {
							PseudoPlayer pP = PseudoPlayerHandler.getPseudoPlayer(p.getName());
							pP._isCastingClearSky = false;
							
							// Tell the casters they succeeded
							String castSuccessText = "";
							
							for(int f=0; f<numCastingClearSkyPlayers; f++) {
								String castingName = _castingClearSkyPlayers.get(f);
								if(castingName.equalsIgnoreCase(p.getName()))
									castSuccessText += "You";
								else
									castSuccessText += castingName;
								if(f<numCastingClearSkyPlayers-2)
									castSuccessText += ", ";
								if(f<numCastingClearSkyPlayers-1)
									castSuccessText += " and ";
								if(f==numCastingClearSkyPlayers-1)
									castSuccessText += " successfully cast Clear Sky.";
							}
							
							for(int f=0; f<20; f++) {
								double castX = Math.random() * 30;
								if(Math.random() < .5)
									castX = -castX;
								castX = p.getLocation().getX()+castX;
								double castZ = Math.random() * 30;
								if(Math.random() < .5)
									castZ = -castZ;
								castZ = p.getLocation().getZ()+castZ;
								
								Location strikeLocation = new Location(p.getWorld(), castX, p.getWorld().getHighestBlockYAt((int)Math.floor(castX), (int)Math.floor(castZ)), castZ);
								p.getWorld().strikeLightningEffect(strikeLocation);
							}
							Output.positiveMessage(p, castSuccessText);
						}
					}
					_castingClearSkyPlayers.clear();
					RPG._normalWorld.setStorm(false);
				}
				else {
					for(int i=numCastingClearSkyPlayers-1; i>=0; i--) {
						String playerName = _castingClearSkyPlayers.get(i);
						Player p = Utils.getPlugin().getServer().getPlayer(playerName);
						
						if(p == null) {
							_castingClearSkyPlayers.remove(i);
							continue;
						}
						
						PseudoPlayer pp = PseudoPlayerHandler.getPseudoPlayer(p.getName());
						if(!pp._isCastingClearSky) {
							_castingClearSkyPlayers.remove(i);
							continue;
						}
						
						// Dude is definitely still casting day.
						
						pp.setMana(pp.getMana()-1);
						if(pp.getMana() <= 0) {
							pp.setMana(0);
							pp._isCastingClearSky = false;
							Output.simpleError(p, "You have run out of mana and stopped casting Clear Sky.");
							_castingClearSkyPlayers.remove(i);
							continue;
						}
						
						// Mana handled, lets do the effects
						if(Math.random() < .2) {
							double castX = Math.random() * 30;
							if(Math.random() < .5)
								castX = -castX;
							castX = p.getLocation().getX()+castX;
							double castZ = Math.random() * 30;
							if(Math.random() < .5)
								castZ = -castZ;
							castZ = p.getLocation().getZ()+castZ;
							
							Location strikeLocation = new Location(p.getWorld(), castX, p.getWorld().getHighestBlockYAt((int)Math.floor(castX), (int)Math.floor(castZ)), castZ);
							p.getWorld().strikeLightningEffect(strikeLocation);
						}
					}
				}
			}
			
			int numTrades = _trades.size();
			for(int i=numTrades-1; i>=0; i--) {
				Trade trade = _trades.get(i);
				trade.tick();
				if(trade.isDead()) {
					Player offererPlayer = _plugin.getPlayer(trade.getOffererName());
					if(offererPlayer != null) {
						Output.simpleError(offererPlayer, "Trade timed out, please try again.");
					}
					_trades.remove(i);
				}
			}
			
			int numParties = _parties.size();
			for(int i=numParties-1; i>=0; i--) {
				if(_parties.get(i).isDead())
					_parties.remove(i);
			}
			if(_tickCount % 25 == 0) { //2.5 seconds
				Player[] onlinePlayers = Bukkit.getOnlinePlayers();
				int numPlayers = onlinePlayers.length;
				if(numPlayers > 0) {
					_curBank++;
					if(_curBank >= numPlayers)
						_curBank = 0;
					Player p = onlinePlayers[_curBank];
					PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(p.getName());
					
					if(p == null) {
						System.out.println("FartA");
					}
					
					else if(pseudoPlayer == null) {
						System.out.println("FartB");
					}
					else 
						Database.updateBank(p, pseudoPlayer);
				}
			}
			if(entityListener._recentDeathsTicks > 0) {
				entityListener._recentDeathsTicks--;
				if(entityListener._recentDeathsTicks <= 0) {
					entityListener._recentDeaths.clear();
					//System.out.println("Clearing recent deaths, "+entityListener._recentDeaths.size()+" deaths");
				}
			}
			
			RPGBlockListener.tick();
			
			/*Object[] things = entityListener._lastAttackers.keySet().toArray();
			for(Object thing : things) {
				if(thing instanceof LivingEntity) {
					LivingEntity m = (LivingEntity)thing;
					m.
					if(m.getHealth() <= 0)
						entityListener._lastAttackers.remove(thing);
				}
			}*/
			if(_tickCount % 500 == 0) {
				entityListener._lastAttackers.clear();
				//System.out.println("Cleared last attackers.");
			}
			
			if(_kick != null) {
				try {
					Player player = Utils.getPlugin().getServer().getPlayer(_kick);
					player.kickPlayer("Kicked for being AFK - Relog if this was in error.");
					System.out.println("Kicked "+player.getName()+" for inactivity.");
				}
				catch(Exception e) {e.printStackTrace();}
				_kick = null;
			}
			
			if(_tickCount % 3000 == 0) { // every 5 minutes
				//System.out.println("checktick");
				int numRandChests = _randChests.size();
				for(int i=numRandChests-1; i>=0; i--) {
					_randChests.get(i).tick5Minutes();
					if(_randChests.get(i)._dead) {
						_randChests.remove(i);
					}
				}
			}
			
			//doomedBlocks();
			
			if(_roboExplosions.size() > 0) {
				int numExplosions = _roboExplosions.size();
				for(int i=numExplosions-1; i>=0; i--) {
					_roboExplosions.get(i).tick();
					if(_roboExplosions.get(i).isDead())
						_roboExplosions.remove(i);
				}
			}
			
			if(_worldEvent != null) {
				_worldEvent.tick();
			}
			
			_damage = false;
			
			/*Date d2 = new Date();
			time += (d2.getTime() - d1.getTime());*/	
		}
    }
    
    public static void addParty(Party party) {
    	_parties.add(party);
    }

    public void onEnable() {
    	_npcManager = new NPCManager(this);
    	
        // TODO: Place any custom enable code here including the registration of any events
    	
    	// Get/create worlds
    	_normalWorld = this.getServer().getWorlds().get(0);
    	_normalWorld.setKeepSpawnInMemory(false);
    	
    	WorldCreator endWorldCreater = new WorldCreator("world_the_end");
    	endWorldCreater.environment(Environment.THE_END);
    	_theEndWorld = this.getServer().createWorld(endWorldCreater);
    	_theEndWorld.setKeepSpawnInMemory(true);
    	
    	WorldCreator endWorldCreater2 = new WorldCreator("world_the_end_2");
    	endWorldCreater2.environment(Environment.THE_END);
    	_theEndWorld2 = this.getServer().createWorld(endWorldCreater2);
    	_theEndWorld2.setKeepSpawnInMemory(true);
    	
    	WorldCreator newMapCreator = new WorldCreator("nextNewMap");
    	newMapCreator.environment(Environment.NORMAL);
    	_newmap = this.getServer().createWorld(newMapCreator);
    	_newmap.setKeepSpawnInMemory(false);
    	
    	WorldCreator tutorialWorldCreator = new WorldCreator("tutorialworld");
    	tutorialWorldCreator.environment(Environment.NORMAL);
    	_tutorialWorld = this.getServer().createWorld(tutorialWorldCreator);
    	_tutorialWorld.setKeepSpawnInMemory(true);
    	
    	WorldCreator netherWorldCreator = new WorldCreator("nether");
    	netherWorldCreator.environment(Environment.NETHER);
        _netherWorld = this.getServer().createWorld(netherWorldCreator);
        _netherWorld.setKeepSpawnInMemory(false);
        _extraWorld = this.getServer().createWorld(new WorldCreator("extraworld"));
        _extraWorld.setKeepSpawnInMemory(false);
        _farts = this.getServer().createWorld(new WorldCreator("farts"));
        _farts.setKeepSpawnInMemory(false);
        
        _hungryWorld = this.getServer().createWorld(new WorldCreator("Disneyland"));
        _hungryWorld.setKeepSpawnInMemory(false);
        /*WorldCreator skylandWorldCreator = new WorldCreator("skylands");
        skylandWorldCreator.environment(Environment.THE_END);
        _skyWorld = this.getServer().createWorld(skylandWorldCreator);*/
        
        // build npcs
        _orderBuilderLocation = new Location(_normalWorld, 263.5,75,-120.5);
        _chaosBuilderLocation = new Location(_normalWorld, 771.5,53,165.5);
        
        _tutorialLocation = new Location(_tutorialWorld, -39.5,91,-.5);
       
        // CP
        _buntCPCenter = new Location(_normalWorld, 3051.5,69,-3004);
        _buntCPRange = 10;
        _gorpCPCenter = new Location(_normalWorld, -1759.5,156,1023.5);
        _gorpCPRange = 10;
        _vesperCPCenter = new Location(_normalWorld, 473,67.5,287.5);
        _vesperCPRange = 10;
        
        // Define default spawns
        //_murdererSpawn = new Location(_normalWorld, 755.5, 51.5, -1438.5);
        //_blueSpawn = new Location(_normalWorld, 498.5,80.5,-278.5);
        
        _murdererSpawn = new Location(_normalWorld, 741.5,28,113.5);
        _blueSpawn = new Location(_normalWorld, 271.5,78,-84.5);
        
        // Murder shrine
        _murderShrineLocation = new Location(_normalWorld, 2945,71,-2975);
        
        Utils.init(this);
        //NPCs
 		Database.loadNPCs();
        
        // Register our events
        PluginManager pm = getServer().getPluginManager();
       
        playerListener = new RPGPlayerListener(this);
        entityListener = new RPGEntityListener(this);
        blockListener = new RPGBlockListener(this);
        serverListener = new RPGServerListener(this);
        vehicleListener = new RPGVehicleListener(this);
        worldListener = new RPGWorldListener(this);
        mapListener = new MapListener(this);
        
        /*pm.registerEvent(Event.Type.PLAYER_LOGIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
        //pm.registerEvent(Event.Type.PLAYER_ANIMATION, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_TELEPORT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_BUCKET_EMPTY, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_BUCKET_FILL, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_KICK, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_FISH, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, playerListener, Priority.Normal, this);
        //pm.registerEvent(Event.Type.PROJECTILE_HIT, entityListener, Priority.Normal, this);
        //pm.registerEvent(Event.Type.PLAYER_VELOCITY, playerListener, Priority.Normal, this);
        
        pm.registerEvent(Event.Type.EXPLOSION_PRIME, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_EXPLODE, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_TARGET, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.CREATURE_SPAWN, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_TAME, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.FOOD_LEVEL_CHANGE, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.ENDERMAN_PICKUP, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.ENDERMAN_PLACE, entityListener, Priority.Normal, this);
        
        pm.registerEvent(Event.Type.MAP_INITIALIZE, mapListener, Priority.Normal, this);
        
        //pm.registerEvent(Event.Type.ENTITY_REGAIN_HEALTH, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_FADE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Lowest, this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_IGNITE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PHYSICS, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_FROMTO, blockListener, Priority.Normal, this); // flow to this
        pm.registerEvent(Event.Type.BLOCK_BURN, blockListener, Priority.Normal, this);
        
        pm.registerEvent(Event.Type.PORTAL_CREATE, worldListener, Priority.Normal, this);
        
        pm.registerEvent(Event.Type.PAINTING_BREAK, entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PAINTING_PLACE, entityListener, Priority.Normal, this);
        
        pm.registerEvent(Event.Type.VEHICLE_ENTER, vehicleListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.VEHICLE_MOVE, vehicleListener, Priority.Normal, this);
        
        //System.out.println("Registering Event");
        pm.registerEvent(Event.Type.CHUNK_UNLOAD, worldListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.CHUNK_LOAD, worldListener, Priority.Normal, this);*/
        //pm.registerEvent(Event.Type.MAP_INITIALIZE, serverListener, Priority.Normal, this);
        //System.out.println("Event Registered");
        
        //Spout
        //pm.registerEvent(Event.Type.CUSTOM_EVENT, new SpoutRPGInputListener(), Priority.Normal, this);
        //pm.registerEvent(Event.Type.CUSTOM_EVENT, new SpoutRPGScreenListener(), Priority.Normal, this);
        ////pm.registerEvent(Event.Type.CUSTOM_EVENT, new SpoutRPGListener(), Priority.Normal, this);

        _plugin = this.getServer();
        
        //setupPermissions();
        
        Output.init(this);
        ClanHandler.loadClans();
        PlotHandler.loadPlots();
        _banks = Database.getBanks();
        if(_banks == null)
     	   _banks = new ArrayList<Bank>();
        _randChests = Database.getRandChests();
        if(_randChests == null)
        	_randChests = new ArrayList<RandChest>();
        
        
        
        _stores = Database.getStores();
        //_shrines = Database.getShrines();
        // Initialize skills that need it:
        // end initialization of skills
        /*_tickTimer = new Timer();
        _tickTimer.schedule(new Tick(),100,100); // sets up a timer that will run Tick.run every 10th of a second*/
        /*Scheduler s = new Scheduler();
 		// Schedule a once-a-day task.
 		s.schedule("0 6 * * *", new Runnable() {
 			public void run() {
 				System.out.println("Performing Maintenance");
 				_plugin.broadcastMessage("Performing Maintenance");
 				PlotHandler.doTax();
 				Database.dailyMaintenance();
 				System.out.println("Maintenance Complete");
 				_plugin.broadcastMessage("Maintenance Complete");
 			}
 		});
 		
 		s.schedule("0 * * * *", new Runnable() {
 			public void run() {
 				Player[] onlinePlayers = Bukkit.getOnlinePlayers();
 				for(Player p : onlinePlayers) {
 					PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(p.getName());
 					if(!pseudoPlayer.isPremium())
 						p.sendMessage(ChatColor.GOLD + "Enjoying the server? Consider subscribing for $10 a month. Visit http://www.lostshard.com/subscription.html for information on subscription benefits.");
 				}
 				
 				System.out.println("Restocking vendors...");
 				for(Store store : _stores) {
 					if(store.getType() != 1) {
	 					store.restock();
	 					Database.updateStore(store);
 					}
 				}
 				System.out.println("Vendors have restocked.");
 				
 				//RespawnDragon();
 				//_plugin.broadcastMessage("Vendors have re-stocked.");
 			}
 		});
 		
 		s.start();*/
 		
 		
 		
 		Spell.init();
 		Fishing.init();
 		Database.loadPermanentGates();
 		
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
    	
    	Database.loadPseudoWolves();
 		
 		/*List<LivingEntity> livingEntities = _normalWorld.getLivingEntities();
 		for(LivingEntity livingEntity : livingEntities) {
 			if(livingEntity instanceof Wolf) {
 				Wolf wolf = (Wolf)livingEntity;
 				if(wolf.isTamed()) {
 					System.out.println("####Found tamed wolf: "+wolf.getEntityId());
 				}
 			}
 		}*/

 		/*System.out.println("Num entities:" + _netherWorld.getLivingEntities().size());
 		System.out.println("n: " + _normalWorld.getLivingEntities().size());*/
 		/*Player[] onlinePlayers = Utils.getPlugin().getServer().getOnlinePlayers();
 		for(Player player : onlinePlayers) {
 			playerListener.onPlayerJoin(new PlayerEvent(Type.PLAYER_JOIN, player));
 		}*/
 		
 		// determine illegally placed plots
 		/*for(Plot p : PlotHandler.getPlots()) {
 			for(Plot p2 : PlotHandler.getPlots()) {
 				if(p != p2) {
 					if(Utils.isWithin(p.getLocation(), p2.getLocation(), (p.getRadius()+p2.getRadius()+5))) {
 						System.out.println("ILLEGALLY PLACED PLOT: " + p2.getName());
 					}
 				}
 			}
 		}*/
 		
    	final Plugin plugin = this.getServer().getPluginManager().getPlugin("CraftIRC");
        if ((plugin == null) || !plugin.isEnabled() || !(plugin instanceof CraftIRC)) {
            this.getServer().getPluginManager().disablePlugin((this));
        } else {
            final CraftIRC craftirc = (CraftIRC) plugin;
            craftircHandle = craftirc;
            /*craftirc.registerEndPoint(this.craftIRCPluginTag, this);
            final RelayedMessage rm = craftirc.newMsg(this, null, "generic");
            rm.setField("message", "I'm aliiive!");
            rm.post();*/
        }
    	
    	//initCraftIRC();
 		//initMonsterIRC();
 		_plugin.getScheduler().scheduleSyncRepeatingTask(this, new Tick(), 2, 2);
 		
 		
 		RespawnDragon();
    }
    
    public void RespawnDragon() {
    	List<LivingEntity> livingEntities = _theEndWorld.getLivingEntities();
    	int numLivingEntities = livingEntities.size();
    	boolean foundDragon = false;
    	for(int i=0; i<numLivingEntities; i++) {
    		LivingEntity lE = livingEntities.get(i);
    		if(lE instanceof EnderDragon) {
    			lE.setHealth(0);
    			//foundDragon = true;
    		}
    	}
    	
    	if(!foundDragon) {
    		_theEndWorld.spawnEntity(new Location(_theEndWorld, 0, 90, 0), EntityType.ENDER_DRAGON);
    		Player[] players = Bukkit.getOnlinePlayers();
			for(Player p : players) {
				p.sendMessage("The Enderdragon has returned to The End...");
			}
    	}
    }
    
    public void onDisable() {
        // TODO: Place any custom disable code here

        // NOTE: All registered events are automatically unregistered when a plugin is disabled

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        System.out.println("Goodbye world!");
        /*ArrayList<BasicHumanNpc> npcs = NPCHandler.getNpcs();
        int numNPCs = npcs.size();
        for(int i=numNPCs-1; i>=0; i--) {
        	RPG._HumanNPCList.re(npcs.get(i)..getUniqueId());
        }*/
        shutDown();
    }
    
    public ArrayList<Bank> getBanks() {
    	return _banks;
    }
    
    public static int _shutdownTicks = 0;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    	if(sender instanceof ConsoleCommandSender) {
    		if(command.getName().equalsIgnoreCase("shutdown")) {
    			shutDown();
    		}
    		
    		if(command.getName().equalsIgnoreCase("endcombatlog")) {
    			RPG._shuttingDown = true;
    			//System.out.println("DID");
    			//_shutdownTicks = 2700;
    			return true;
    		}
    		
    		if(command.getName().equalsIgnoreCase("maintenance")) {
    			System.out.println("Performing Maintenance");
 				_plugin.broadcastMessage("Performing Maintenance");
 				PlotHandler.doTax();
 				Database.dailyMaintenance();
 				System.out.println("Maintenance Complete");
 				_plugin.broadcastMessage("Maintenance Complete");
    			return true;
    		}
    		
    		if(command.getName().equalsIgnoreCase("restock")) {
    			Player[] onlinePlayers = Bukkit.getOnlinePlayers();
 				for(Player p : onlinePlayers) {
 					PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(p.getName());
 					if(!pseudoPlayer.isPremium())
 						p.sendMessage(ChatColor.GOLD + "Enjoying the server? Consider subscribing for $10 a month. Visit http://www.lostshard.com/subscription.html for information on subscription benefits.");
 				}
 				
 				System.out.println("Restocking vendors...");
 				for(Store store : _stores) {
 					if(store.getType() != 1) {
	 					store.restock();
	 					Database.updateStore(store);
 					}
 				}
 				System.out.println("Vendors have restocked.");
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /*private void setupPermissions() {
        Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");

        if (Permissions == null) {
            if (test != null) {
                this.getServer().getPluginManager().enablePlugin(test); // This line.
               Permissions = ((Permissions)test).getHandler();
            } else {
                //log.info("Permission system not detected, defaulting to OP");
            }
        }
    }*/
    
    private void initCraftIRC() {
    	/*Plugin checkplugin = this.getServer().getPluginManager().getPlugin("CraftIRC");
        if (checkplugin == null) {
            System.out.println("MyPlugin's cannot be loaded because CraftIRC is not enabled on the server!");
            getServer().getPluginManager().disablePlugin(((org.bukkit.plugin.Plugin) (this)));
        } else {
            try {
            	System.out.println("MyPlugin loading...");
            	// Get handle to CraftIRC, add&register your custom listener
            	craftircHandle = (CraftIRC) checkplugin;
            	craftircHandle.sendMessageToTag("TEST", "synirclostshard");
            } catch (ClassCastException ex) {
            	ex.printStackTrace();
              	System.out.println("MyPlugin can't cast plugin handle as CraftIRC plugin!");
              	getServer().getPluginManager().disablePlugin(((org.bukkit.plugin.Plugin) (this)));
            }
        }*/
    	System.out.println("Loading MyPlugin...");
        Plugin checkplugin = this.getServer().getPluginManager().getPlugin("CraftIRC");
        if (checkplugin == null) {
        	System.out.println("MyPlugin requires CraftIRC. Please install CraftIRC before using this plugin.");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            try {
                // Get handle to CraftIRC, add and register your custom listener
                craftircHandle = (CraftIRC) checkplugin;
                /*MyCraftIRCListener ircListener = new MyCraftIRCListener(craftircHandle);
                this.getServer().getPluginManager()
                    .registerEvent(Event.Type.CUSTOM_EVENT, ircListener, Priority.Normal, this);*/
            } catch (ClassCastException ex) {
                ex.printStackTrace();
                System.out.println("MyPlugin cannot recognize this version of CraftIRC."
                           + " Make sure you are using a compatible version.");
                getServer().getPluginManager().disablePlugin(this);
            }
        }
        
        //RPG.craftircHandle.registerEndPoint("synirclostshard", RPG.craftircHandle.getEndPoint("plugin"));
    }
    
    /*private void initMonsterIRC() {
    	Plugin checkplugin = this.getServer().getPluginManager().getPlugin("MonsterIRC");
        if (checkplugin == null) {
            System.out.println("MyPlugin's cannot be loaded because Monster is not enabled on the server!");
            getServer().getPluginManager().disablePlugin(((org.bukkit.plugin.Plugin) (this)));
        } else {
            try {
            	System.out.println("MyPlugin loading...");
            	// Get handle to CraftIRC, add&register your custom listener
            	_monsterIrcPlugin = (MonsterIRC) checkplugin;
            } catch (ClassCastException ex) {
            	ex.printStackTrace();
              	System.out.println("MyPlugin can't cast plugin handle as MonsterIRC plugin!");
              	getServer().getPluginManager().disablePlugin(((org.bukkit.plugin.Plugin) (this)));
            }
        }

    }*/
    
    public void shutDown() {
    	_shuttingDown = true;
    	System.out.println("Cleaning Up");
    	System.out.println("-Disabling Login");
    	_canLogin = false;
    	System.out.println("-Kicking Players");
    	Player[] players = Utils.getPlugin().getServer().getOnlinePlayers();
    	for(int i=0; i<players.length; i++) {
    		Player p = players[i];
    		p.kickPlayer("Server Probably Rebooting");
    	}
    	System.out.println("-"+players.length+" Players Kicked");
    	System.out.println("-Cleaning up camps");
    	ArrayList<Camp> camps = Survivalism.getCamps();
    	for(int i=0; i<camps.size(); i++) {
    		Camp c = camps.get(i);
    		c._isDead = true;
    		Utils.loadChunkAtLocation(c._logBlock.getLocation());
    		c._logBlock.setType(Material.AIR);
    		c._fireBlock.setType(Material.AIR);
    	}
    	System.out.println("-"+camps.size()+" camps cleaned up");
    	System.out.println("-Cleaning up magic structures");
    	ArrayList<MagicStructure> magicStructures = Magery.getMagicStructures();
    	for(int i=0; i<magicStructures.size(); i++) {
    		MagicStructure ms = magicStructures.get(i);
    		ms.cleanUp();
    	}
    	System.out.println("-"+magicStructures.size()+" magic structures cleaned up");
    	System.out.println("Cleaned Up");
    }
    
    @Override
    public Type getType() {
        return EndPoint.Type.MINECRAFT;
    }

    @Override
    public void messageIn(RelayedMessage msg) {
        if (msg.getEvent() == "join") {
            this.getServer().broadcastMessage(msg.getField("sender") + " joined da game!");
        }
    }

    @Override
    public boolean userMessageIn(String username, RelayedMessage msg) {
        return false;
    }

    @Override
    public boolean adminMessageIn(RelayedMessage msg) {
        return false;
    }

    @Override
    public List<String> listUsers() {
        return null;
    }

    @Override
    public List<String> listDisplayUsers() {
        return null;
    }
    
    /*private static long timeForNextRandomEvent;
    public void doRandomEvent() {
    	
    }*/
}