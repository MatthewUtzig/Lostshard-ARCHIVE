package com.lostshard.RPG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.lostshard.RPG.CombatAbilities.*;
import com.lostshard.RPG.Groups.Clans.Clan;
import com.lostshard.RPG.Groups.Parties.Party;
import com.lostshard.RPG.Plots.BankBox;
import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Plots.PlotHandler;
import com.lostshard.RPG.Plots.Store;
import com.lostshard.RPG.Skills.*;
import com.lostshard.RPG.Spells.*;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;


public class PseudoPlayer {
	private static final int NEW_PLAYER_TIME = 14400;
	private String _playerName;
	
	////public PlayerHUD _playerHud = null;
	
	private String _taunt = "Der im taunts~";
	private long _traitChangeTime;
	
	/*private int _curHealth = 2000;
	private int _maxHealth = 2000;*/
	private int _id;
	
	public boolean _justLoggedIn = false;
	
	public boolean _vanished = false;
	public int _killSelfTicks = 0;
	public boolean _isCastingDay = false;
	public boolean _isCastingClearSky = false;
	public ItemStack _addingSaleItem = null;
	public Store _addingSaleStore = null;
	public Store _lastStoreAccessed = null;
	public boolean _allowFriendlyFire = false;
	
	public boolean _secret = false;
	public boolean _swung = false;
	
	public int _money;
	private double _mana;
	private double _stamina;
	public int _maxMana = 100;
	public int _maxStamina = 100;
	private HashMap<String, Integer> _skillHashMap;
	private HashMap<String, Boolean> _lockedSkillHashMap;
	private HashMap<Integer, Spell> _spellSlotHashMap;
	private int _tickCount = 0;
	private boolean _globalEnabled = true;
	private Party _party = null;
	public BankBox _bankBox;
	private boolean _largerbank;
	private boolean _premium;
	private int _globals;
	private HashSet<String> _ignoreList;
	private int _playTime;
	private Location _customSpawn;
	private boolean _vampirism;
	private boolean _robot;
	private int _premiumDays;
	private boolean _isPrivate;
	private int _freeSkillPointsRemaining;
	//private ArrayList<StatusEffect> _statusEffects;
	public int _engageInCombatTicks = 0;
	public boolean _ljExausted = false;
	
	public String _lastAttackerName = null;
	public int _lastAttackTicks = 0;
	
	private int _plotCreatePoints;
	
	public int _awayTicks = 6000;
	public int _kickOutTicks = 0;
	
	public Block _lastBlock = null;
	//private int _tpTicks = 0;
	
	public int _stunTicks = 0;
	public int _bleedTicks = 0;
	
	public boolean _instaBuild = false;
	public boolean _convenient = false;
	public Plot _plottest = null;
	public Location _teleportTo = null;
	private int _spawnTicks = 0;
	
	public int _lockingTicks = 0;
	public int _unlockingTicks = 0;
	
	public float _noCommandTicks = 0;
	public int _openedChestTicks = 0;
	
	public int _pvpTicks = 0;
	public int _respawnTicks = 0;
	public int _loggedInRecentlyTicks = 0;
	public String _lastExtraString = null;
	
	public int _dieLog = 0;
	
	public boolean _cleanat = false;
	
	public String _lastPlayerNameMsg = null;
	
	public int _noFireTicks = 0;
	public int _clearTicks = 0;
	
	private Spell _promptedSpell = null;
	
	public int _goToSpawnTicks = 0;
	public int _traitChangeTicks = 0;
	public String _newTraitName = "";
	
	//clan
	private Clan _clan = null;
	private String _clanName;
	
	//karma
	private int _murderCounts = 0;
	public int _criminalTicks = 0;
	private final int _countsBeforeMurderer = 5;
	private ArrayList<RecentAttacker> _recentAttackers = new ArrayList<RecentAttacker>();
	
	//magery
	private float _cantCastTicks = 0;
	private float _castDelayTicks = 0;
	private Spell _delayedSpell = null;
	private Spellbook _spellbook;
	private Runebook _runebook;
	
	private ArrayList<Spell> _scrolls;
	
	//survivalism
	private int _compassResetTicks = 0;
	
	//combat skills
	private CombatAbility _swordsmanshipAbility = new PowerAttack();
	private CombatAbility _archeryAbility;
	
	public static int _maxSkillValTotal = 4000;
	
	//magery spell effects
	public int _fireWalkTicks = 0;
	public FireWalk _fireWalk = null;
	
	public int _slowFallTicks = 0;
	public int _flyTicks = 0;
	
	public int _recentlyTeleportedTicks = 0;
	public int _enteredPortalTicks = 0;
	
	public boolean _isMeditating = false;
	public boolean _isResting = false;
	
	public long _lastLogout = 0;
	
	public int _punishTicks = 0;
	public int _lastChanceTicks = 0;
	
	public int _guardAttackTicks = 0;
	
	private int _buildNumber;
	
	public ArrayList<String> _availableTitles;
	public String _activeTitle;
	
	public long _lastAttackTime = 0;
	
	private int _repairTicks = 0;
	public int _moonJumpTicks = 0;
	public int _stoneSkinTicks = 0;
	
	public int _activeChannel = ChatHandler.CHAT_CHANNEL_GLOBAL;
	
	public int _otherChatChannel = -1;
	
	private ArrayList<PseudoWolf> _tamedWolves;
	
	public int _vanishedTicks = 0;
	
	public ActiveBandage _activeBandage = null;
	
	public String _msgPlayer = null;
	
	public boolean _claiming = false;
	
	private Location _lastLogoutLocation;
	
	public boolean _noPickup = false;
	
	public boolean _ignoreDelay = false;
	
	public int _noPierceTicks = 0;
	
	public int _recentlyLoggedInTicks = 50;
	
	public int _slothTicks = 0;
	public int _wrathTicks = 0;
	
	public int _noTalkTicks = 0;
	
	public int _chronoPortTicks = 0;
	public Location _chronoPortLocation;
	
	public int getMaxSkillValTotal() {
			/*if(this.isPremium())
				return _maxSkillValTotal+500;*/
			return _maxSkillValTotal;
	}
	
	public PseudoPlayer(int Id, String playerName, String clanName, int money, int mana, int stamina, HashMap<String, Integer> skillHashMap, int murderCounts, Spellbook spellbook, Runebook runebook, ArrayList<Spell> scrolls, BankBox bankBox, boolean largerbank, boolean premium, int globals, HashSet<String> ignoreList, HashMap<Integer, Spell> spellSlotHashMap, HashMap<String, Boolean> lockedSkillHashMap, int playTime, Location customSpawn, int premiumDays, int plotCreatePoints, int freeSkillPointsRemaining, long traitChangeTime, int crimTicksRemaining, long lastLogout, ArrayList<String> availableTitles, String activeTitle) {
		_id = Id;
		_playerName = playerName;
		_clanName = clanName;
		_mana = mana;
		_stamina = stamina;
		_money = money;
		_skillHashMap = skillHashMap;
		_murderCounts = murderCounts;
		_bankBox = bankBox;
		_largerbank = largerbank;
		_premium = premium;
		_globals = globals;
		_ignoreList = ignoreList;
		_playTime = playTime;
		_customSpawn = customSpawn;
		_premiumDays = premiumDays;
		_plotCreatePoints = plotCreatePoints;
		_freeSkillPointsRemaining = freeSkillPointsRemaining;
		_spellSlotHashMap = spellSlotHashMap;
		_lockedSkillHashMap = lockedSkillHashMap;
		_spellbook = spellbook;
		_runebook = runebook;
		_scrolls = scrolls;
		_traitChangeTime = traitChangeTime;
		_criminalTicks = crimTicksRemaining;
		_lastLogout = lastLogout;
		_availableTitles = availableTitles;
		_activeTitle = activeTitle;
		
		//_maxMana = Meditation.getMaxMana(this);
		//_maxStamina = Resting.getMaxStamina(this);
	}
	
	//private int _hudInitTicks = 20;
	private int _recentChatTicks = 50;
	public int _recentChatMessages = 0;
	// tick run once per second from RPGPlayerListener timer thread 
	public void tick(double delta) {
	
		if(_vanishedTicks > 0) {
			_vanishedTicks--;
			if(_vanishedTicks == 0) {
				if(this._clan != null && this._clan.getCloakURL() != null && !_clan.getCloakURL().equals("")) {
					Player player = Utils.getPlugin().getServer().getPlayer(_playerName);
					////SpoutManager.getAppearanceManager().setGlobalCloak(player, _clan.getCloakURL());
				}
			}
		}
		
		if(_otherChatChannel != -1)
			_otherChatChannel = -1;
		
		if(_recentChatTicks > 0) {
			_recentChatTicks--;
			if(_recentChatTicks == 0) {
				_recentChatMessages = 0;
				_recentChatTicks = 50;
			}
		}
		
		_tickCount++;
		if(_tickCount >= 1000) {
			_tickCount = 0;
			// big tick
		}
		
		/*if(_justLoggedIn) {
			if(Math.random() < .01) {
				_justLoggedIn = false;
				Player player = Utils.getPlugin().getServer().getPlayer(_playerName);
				if(player != null) {
					player.chat("/vanish&nopickup");
				}
			}
		}*/
		
		if(_punishTicks > 0) {
			_punishTicks--;
			if(_punishTicks <=0) {
				Player player = Utils.getPlugin().getServer().getPlayer(_playerName);
				if(player != null) {
					player.teleport(RPG._murdererSpawn);
	    			player.setNoDamageTicks(0);
	    			_loggedInRecentlyTicks = 0;
	    			player.damage(20);
	    			_dieLog = 0;
	    			String message = player.getDisplayName()+ChatColor.WHITE+" logged out in combat and has been executed at the ";
	    			if(isCriminal())
	    				player.getServer().broadcastMessage(message+=" Order spawn.");
	    			else
	    				player.getServer().broadcastMessage(message+=" Chaos spawn.");
				}
			}
		}
		
		if(this._claiming) {
            ArrayList<Plot> controlPoints = PlotHandler.getControlPoints();
            for(Plot cP : controlPoints) {
            	if(cP.isUnderAttack() && cP._capturingPlayerName.equals(_playerName)) {
            		Player player = Bukkit.getPlayer(_playerName);
            		Plot plot = PlotHandler.findPlotAt(player.getLocation());
            		if(plot == null || plot != cP)
            			cP.failCaptureLeft(player);
            	}
            }
        }
		
		if(_lastChanceTicks > 0) {
			_lastChanceTicks--;
		}
		
		/*if(_enteredPortalTicks > 0) {
			_enteredPortalTicks--;
			if(_enteredPortalTicks <= 0) {
				checkPortals();
			}
		}*/
		
		if(_recentlyTeleportedTicks > 0) {
			_recentlyTeleportedTicks--;
		}
		
		if(_killSelfTicks > 0) {
			_killSelfTicks--;
		}
		
		if(_kickOutTicks > 0) {
			_kickOutTicks--;
			if(_kickOutTicks <=0) {
				Player player = Utils.getPlugin().getServer().getPlayer(_playerName);
				if(player != null) {
					Plot plot = PlotHandler.findPlotAt(player.getLocation());
			        if(plot != null && !player.getWorld().getName().equalsIgnoreCase("nether")) {
			        	if(plot.isLocked() && plot.hasKickUpgrade()) {
			        		if(!plot.isMember(player.getName())) {
			        			int x = player.getLocation().getBlockX();
			        			int y;
			        			int z = player.getLocation().getBlockZ();
			        			
			        			for(y=127; y>=0; y--) {
			        				if(player.getWorld().getBlockTypeIdAt(x,y,z) == 0)
			        					continue;
			        				else
			        					break;
			        			}
			        			player.teleport(new Location(player.getWorld(), x+.5, y+3, z+.5));
			        			Output.simpleError(player, "You logged in at a locked plot and have been moved.");
			        		}
			        	}
			        }
				}
				else
					return;
			}
		}
		
		if(_awayTicks > 0) {
			_awayTicks--;
			if(_awayTicks <= 0)
				RPG._kick = _playerName;
		}
		
		if(_tickCount % 10 == 0) { // one second passed 
			updateMana(delta);
			updateStamina(delta);
			
			////spout
			/*if(_playerHud != null) {
				_playerHud.updateMana();
			}*/
			
			_playTime++;
			if(_playTime == NEW_PLAYER_TIME)
				Output.positiveMessage(Utils.getPlugin().getServer().getPlayer(_playerName), "You are no longer a new player.");
		}
		
		if(_castDelayTicks > 0) {
			_castDelayTicks-=delta;
			if(_castDelayTicks < 0)
				_castDelayTicks = 0;
			if(_castDelayTicks <= 0) {
				// just ran out of ticks...
				_delayedSpell.doAction(RPG.getPlugin().getPlayer(_playerName));
			}
		}
		
		if(_cantCastTicks > 0) {
			_cantCastTicks-=delta;
			if(_cantCastTicks <= 0)
				_cantCastTicks = 0;
		}
		
		if(_compassResetTicks > 0) {
			_compassResetTicks--;
			if(_compassResetTicks <= 0) {
				Player p = Utils.getPlugin().getServer().getPlayer(_playerName);
				p.setCompassTarget(p.getWorld().getSpawnLocation());
				p.sendMessage("Your compass has reset");
			}
		}
		
		if(_criminalTicks > 0) {
			_criminalTicks--;
			if(_criminalTicks <= 0) {
				Player p = Utils.getPlugin().getServer().getPlayer(_playerName);
				if(p != null) {
					if(_murderCounts < _countsBeforeMurderer) {
						p.sendMessage(ChatColor.GRAY+"You are no longer a criminal.");
						if(!isCriminal()) {
							p.setDisplayName(ChatColor.BLUE+p.getName());
							Utils.setPlayerTitle(p);
							//SpoutManager.getAppearanceManager().setGlobalTitle(p, p.getDisplayName());
							//BukkitContrib.getAppearanceManager().setGlobalTitle(p, p.getDisplayName());
						}
					}
				}
			}
		}
		
		// Cycle through all the recent attackers, remove them if they have timed out
		if(_recentAttackers.size() > 0) {
			int numRecentAttackers = _recentAttackers.size();
			for(int i=numRecentAttackers-1; i>=0; i--) {
				RecentAttacker recentAttacker = _recentAttackers.get(i);
				recentAttacker.tick();
				if(recentAttacker.isDead())
					_recentAttackers.remove(i);
			}
		}
		
		/*if(_tpTicks > 0) {
			_tpTicks--;
			if(_tpTicks <= 0) {
				if(isCriminal())
					Utils.getPlugin().getServer().getPlayer(_playerName).teleportTo(RPG._murdererSpawn);
				else
					Utils.getPlugin().getServer().getPlayer(_playerName).teleportTo(RPG._normalWorld.getSpawnLocation());
			}
		}*/
		
		if(_noFireTicks > 0) {
			_noFireTicks--;
		}
		
		if(_noCommandTicks > 0) {
			_noCommandTicks--;
		}
		
		if(_openedChestTicks > 0) {
			_openedChestTicks--;
		}
		
		if(_lockingTicks > 0) {
			_lockingTicks--;		
		}
		
		if(_unlockingTicks > 0) {
			_unlockingTicks--;		
		}
		
		if(_engageInCombatTicks > 0) {
			_engageInCombatTicks--;
		}
		
		if(_teleportTo != null) {
			Utils.getPlugin().getServer().getPlayer(_playerName).teleport(_teleportTo);
			_teleportTo = null;
		}
		
		if(_clearTicks > 0) {
			_clearTicks--;
			if(_clearTicks <= 0) {
				Utils.getPlugin().getServer().getPlayer(_playerName).getInventory().clear();
			}
		}
		
		if(_spawnTicks > 0)
			_spawnTicks--;
		
		if(_stunTicks > 0)
			_stunTicks--;
		
		if(_slothTicks > 0)
			_slothTicks--;

		if(_wrathTicks > 0) {
			_wrathTicks--;
			Player player = Utils.getPlugin().getServer().getPlayer(_playerName);
			Location loc = player.getLocation();
			int offsetBlocksX = (int)Math.floor(Math.random() * 50);
			if(Math.random() < .5)
				offsetBlocksX *= -1;
			
			int offsetBlocksZ = (int)Math.floor(Math.random() * 50);
			if(Math.random() < .5)
				offsetBlocksZ *= -1;
			
			int targetBlockX = loc.getBlockX() + offsetBlocksX;
			int targetBlockZ = loc.getBlockZ() + offsetBlocksZ;
			
			int highestBlockY = loc.getWorld().getHighestBlockYAt(targetBlockX, targetBlockZ);
			
			Location strikeLoc = new Location(loc.getWorld(), targetBlockX + .5, highestBlockY + .5, targetBlockZ + .5);
			
			strikeLoc.getWorld().strikeLightning(strikeLoc);
		}

		
		if(_bleedTicks > 0) {
			if(_tickCount % 10 == 0) {
				Player p = Utils.getPlugin().getServer().getPlayer(_playerName);
				_bleedTicks--;
				
				if(_bleedTicks <= 0) {
					p.sendMessage("Your bleeding has stopped.");
					_bleedTicks = 0;
				}
				else
				{
					Damageable damag = p;
						double newHealth = damag.getHealth() - 1;
						if(newHealth > 20)
							newHealth = 20;
						if(newHealth < 0)
							newHealth = 0;
						p.setHealth(newHealth);
				}
			}
		}
		
		if(_respawnTicks > 0) {
			_respawnTicks--;
		}
		
		if(_pvpTicks > 0) {
			_pvpTicks--;
		}
		
		if(_lastAttackTicks > 0) {
			_lastAttackTicks--;
			if(_lastAttackTicks <= 0)
				_lastAttackerName = null;
		}
		
		if(_loggedInRecentlyTicks > 0) {
			_loggedInRecentlyTicks--;
		}
		
		if(_noTalkTicks > 0)
			_noTalkTicks--;
		
		// Magery spell effects
		if(_fireWalkTicks > 0) {
			_fireWalkTicks--;
			if(_fireWalkTicks == 0) {
				Utils.getPlugin().getServer().getPlayer(_playerName).sendMessage(ChatColor.GRAY+"The flames on your feet have extinguished.");
				Utils.getPlugin().getServer().getPlayer(_playerName).setFireTicks(0);
				_fireWalk = null;
			}
		}
		
		if(_flyTicks > 0) {
			_flyTicks--;
			if(_flyTicks == 0) {
				Player player = Utils.getPlugin().getServer().getPlayer(_playerName);
				player.sendMessage(ChatColor.GRAY+"The effects of Fly have worn off.");
			}
		}
		
		if(_goToSpawnTicks > 0) {
			_goToSpawnTicks--;
			if(_goToSpawnTicks == 0) {
				Player player = Utils.getPlugin().getServer().getPlayer(_playerName);
				player.getWorld().strikeLightningEffect(player.getLocation());
				if(isCriminal())
					player.teleport(RPG._murdererSpawn);
				else 
					player.teleport(RPG._blueSpawn);
	        	setSpawnTicks(36000);
	        	setMana(0);
				setStamina(0);
				player.getWorld().strikeLightningEffect(player.getLocation());
				player.sendMessage(ChatColor.GRAY+"Teleporting without a rune has exausted you.");
			}
			else if(_goToSpawnTicks % 10 == 0) {
				Player player = Utils.getPlugin().getServer().getPlayer(_playerName);
				Output.simpleError(player, "Returning to spawn in "+(_goToSpawnTicks/10)+" seconds.");
			}
		}
		
		if(_repairTicks > 0)
			_repairTicks--;
		
		////spout
		/*
		if(_moonJumpTicks > 0) {
			_moonJumpTicks--;
			if(_moonJumpTicks == 50) {
				SpoutPlayer sPlayer = SpoutManager.getPlayer(Utils.getPlugin().getServer().getPlayer(_playerName));
				sPlayer.sendMessage(ChatColor.GRAY+"You start to feel heavier.");
				sPlayer.setGravityMultiplier(.4);
			}
			if(_moonJumpTicks <= 0) {
				SpoutPlayer sPlayer = SpoutManager.getPlayer(Utils.getPlugin().getServer().getPlayer(_playerName));
				sPlayer.sendMessage(ChatColor.GRAY+"Slow jump has worn off.");
				sPlayer.setGravityMultiplier(1);
				if(!sPlayer.isOp())
					sPlayer.setCanFly(false);
			}
		}
		
		if(_stoneSkinTicks > 0) {
			_stoneSkinTicks--;
			if(_stoneSkinTicks == 50) {
				SpoutPlayer sPlayer = SpoutManager.getPlayer(Utils.getPlugin().getServer().getPlayer(_playerName));
				sPlayer.setGravityMultiplier(1.2);
				sPlayer.setWalkingMultiplier(.5);
				sPlayer.setJumpingMultiplier(.8);
				sPlayer.setAirSpeedMultiplier(1);
			}
			if(_stoneSkinTicks <= 0) {
				SpoutPlayer sPlayer = SpoutManager.getPlayer(Utils.getPlugin().getServer().getPlayer(_playerName));
				sPlayer.sendMessage(ChatColor.GRAY+"Stone Skin has worn off.");
				sPlayer.setGravityMultiplier(1);
				sPlayer.setWalkingMultiplier(1);
				sPlayer.setJumpingMultiplier(1);
				sPlayer.setAirSpeedMultiplier(1);
				SpoutManager.getAppearanceManager().resetGlobalSkin(sPlayer);
			}
		}*/
		/*if(_traitChangeTicks > 0) {
			_traitChangeTicks--;
			
			if(_traitChangeTicks == 0) {
				Player player = Utils.getPlugin().getServer().getPlayer(_playerName);
				PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(_playerName);
				if(_newTraitName.equalsIgnoreCase("Vampirism")) {
					if(pseudoPlayer.isVampire()) {
						Output.positiveMessage(player, "You have disabled Vampirism");
		        		pseudoPlayer.setVampire(false);
		        		Database.setVampirism(player, false);
					}
					else {
						Output.positiveMessage(player, "You have enabled Vampirism");
		        		pseudoPlayer.setVampire(true);
		        		Database.setVampirism(player, true);
					}
					Date dateNow = new Date();
					_traitChangeTime = dateNow.getTime();
					Database.updatePlayerByPseudoPlayer(this);
				}
				else if(_newTraitName.equalsIgnoreCase("Robotism")) {
					if(pseudoPlayer.isRobot()) {
						Output.positiveMessage(player, "You have disabled Robotism");
		        		pseudoPlayer.setRobot(false);
		        		Database.setRobot(player, false);
					}
					else {
						Output.positiveMessage(player, "You have enabled Robotism");
		        		pseudoPlayer.setRobot(true);
		        		Database.setRobot(player, true);
					}
					Date dateNow = new Date();
					_traitChangeTime = dateNow.getTime();
					Database.updatePlayerByPseudoPlayer(this);
				}
			}
			else if(_traitChangeTicks % 10 == 0) {
				Player player = Utils.getPlugin().getServer().getPlayer(_playerName);
				PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(_playerName);
				boolean acquiring = true;
				if(_newTraitName.equalsIgnoreCase("Vampirism")) {
					if(pseudoPlayer.isVampire())
						acquiring = false;
				}
				else if(_newTraitName.equalsIgnoreCase("Robotism")) {
					if(pseudoPlayer.isRobot())
						acquiring = false;
				}
				if(acquiring)
					Output.simpleError(player, "Acquiring "+_newTraitName+" in "+(_traitChangeTicks/10)+" seconds.");
				else
					Output.simpleError(player, "Removing "+_newTraitName+" in "+(_traitChangeTicks/10)+" seconds.");
			}
		}*/
		
		if(_noPierceTicks > 0)
			_noPierceTicks--;
		
		if(_recentlyLoggedInTicks > 0)
			_recentlyLoggedInTicks--;
		
		if(_chronoPortTicks > 0) {
			_chronoPortTicks--;
			if(_chronoPortTicks <= 0) {
				Player player = Bukkit.getPlayer(_playerName);
				if(player != null) {
					player.setFallDistance(0);
					player.setVelocity(new Vector(0,0,0));
					player.teleport(_chronoPortLocation);
				}
			}
		}
		
		_swung = false;
	}
	
	public void setSkills(HashMap<String, Integer> skillsHashMap) {
		_skillHashMap = skillsHashMap;
	}
	
	public void setLockedSkills(HashMap<String, Boolean> skillsHashMap) {
		_lockedSkillHashMap = skillsHashMap;
	}
	
	public int getId() {
		return _id;
	}
	
	public void setPromptedSpell(Spell promptedSpell) {
		_promptedSpell = promptedSpell;
	}
	
	public Spell getPromptedSpell() {
		return _promptedSpell;
	}
	
	private void updateMana(double delta) {
		if(_mana < _maxMana) {
			double manaRegenMultiplier = Meditation.getManaRegenMultiplier(this);
			if(_isMeditating)
				_mana+=(2*manaRegenMultiplier*delta);
			else
				_mana+=(1*manaRegenMultiplier*delta);
			
			if(_mana >= _maxMana) {
				_mana = _maxMana;
				// just reached max...
				Player p = RPG.getPlugin().getPlayer(_playerName);
				if(p != null) {
					Output.positiveMessage(p, "Your mana has fully regenerated.");
				}
			}
		}
	}
	
	public void setMana(int mana) {
		if(mana > _maxMana)
			mana = _maxMana;
		if(mana < 0)
			mana = 0;
		_mana = mana;
	}
	
	private void updateStamina(double delta) {
		if(_stamina < _maxStamina) {
			double staminaRegenMultiplier = 1;
			if(_isResting)
				_stamina+=(2*staminaRegenMultiplier*delta);
			else
				_stamina+=(1*staminaRegenMultiplier*delta);
			if(_stamina >= _maxStamina) {
				_stamina = _maxStamina;
				// just reached max...
				Player p = RPG.getPlugin().getPlayer(_playerName);
				if(p != null) {
					Output.positiveMessage(p, "Your stamina has fully regenerated.");
				}
			}
		}
		/*if(_stamina < _maxStamina) {
			_stamina += 1*delta;
			if(_stamina >= _maxStamina) {
				_stamina = _maxStamina;
				// just reached max...
				Player p = RPG.getPlugin().getPlayer(_playerName);
				if(p != null) {
					Output.positiveMessage(p, "Your stamina has fully regenerated.");
				}
			}
		}*/
	}
	
	public void setStamina(int stamina) {
		if(stamina > _maxStamina)
			stamina = _maxStamina;
		if(stamina < 0)
			stamina = 0;
		_stamina = stamina;
	}
	
	public String getName() {
		return _playerName;
	}
	
	public int getMoney() {
		return _money;
	}
	
	public void setMoney(int money) {
		_money = money;
	}
	
	public int getMana() {
		return (int)Math.round(_mana);
	}
	
	public int getMaxMana() {
		return _maxMana;
	}
	
	public void setMaxMana(int mana) {
		_maxMana = mana;
		if(_mana > _maxMana)
			_mana = _maxMana;
	}
	
	public int getStamina() {
		return (int)Math.round(_stamina);
	}
	
	public int getMaxStamina() {
		return _maxStamina;
	}
	
	public int getSkill(String skillName) {
		String skillNameLower = skillName.toLowerCase();
		if(_skillHashMap.containsKey(skillNameLower)) {
			return _skillHashMap.get(skillNameLower);
		}
		return 0;	
	}
	
	public boolean setSkill(String skillName, int skillVal) {
		String skillNameLower = skillName.toLowerCase();
		if(_skillHashMap.containsKey(skillNameLower)) {
			_skillHashMap.put(skillNameLower, skillVal);
			return true;
		}
		else {
			System.out.println("failed to set skill "+skillNameLower);
			return false;
		}
	}
	
	public Spell getSpellInSlot(int slot) {
		if((slot >= 0) && (slot <= 8)) {
			return _spellSlotHashMap.get(slot);
		}
		else return null;
	}
	
	public void setSpellInSlot(int slot, Spell spell) {
		if((slot >= 0) && (slot <= 8)) {
			_spellSlotHashMap.put(slot, spell);
		}
	}
	
	public int getCantCastTicks() {
		return (int)_cantCastTicks;
	}
	
	public void setCantCastTicks(int numTicks) {
		_cantCastTicks = numTicks/2;
	}
	
	public Spellbook getSpellbook() {
		return _spellbook;
	}
	
	public int getCastDelayTicks() {
		return (int)_castDelayTicks;
	}
	
	public void setCastDelayTicks(int castDelayTicks) {
		_castDelayTicks = castDelayTicks;
	}
	
	public void setDelayedSpell(Spell delayedSpell) {
		_delayedSpell = delayedSpell;
	}
	
	public Spell getDelayedSpell() {
		return _delayedSpell;
	}
	
	public boolean isGlobalEnabled() {
		return _globalEnabled;
	}
	
	public void setGlobalEnabled(boolean enabled) {
		_globalEnabled = enabled;
	}
	
	public CombatAbility getSwordsmanshipAbility() {
		return _swordsmanshipAbility;
	}
	
	public void setSwordsmanshipAbility(CombatAbility swordsmanshipAbility) {
		_swordsmanshipAbility = swordsmanshipAbility;
	}
	
	public CombatAbility getArcheryAbility() {
		return _archeryAbility;
	}
	
	public void setArcheryAbility(CombatAbility archeryAbility) {
		_archeryAbility = archeryAbility;
	}
	
	public void setCompassResetTicks(int ticks) {
		_compassResetTicks = ticks;
	}
	
	public boolean isCriminal() {
		if((_murderCounts >= _countsBeforeMurderer) || (_criminalTicks > 0))
			return true;
		return false;
	}
	
	public boolean isMurderer() {
		if(_murderCounts >= 5)
			return true;
		return false;
	}
	
	public void setCriminalTicks(int ticks) {
		_criminalTicks = ticks;
	}
	
	public void addRecentAttacker(RecentAttacker recentAttacker) {
		boolean found = false;
		for(RecentAttacker rA : _recentAttackers) {
			if(rA.getName().equals(recentAttacker.getName())) {
				rA.resetTicks();
				found = true;
				break;
			}
		}
		if(!found)
			_recentAttackers.add(recentAttacker);
	}
	
	public ArrayList<RecentAttacker> getRecentAttackers() {
		return _recentAttackers;
	}
	
	public void clearRecentAttackers() {
		_recentAttackers.clear();
	}
	
	public Clan getClan() {
		return _clan;
	}
	
	public void setClan(Clan clan) {
		_clan = clan;
	}
	
	public String getClanName() {
		return _clanName;
	}
	
	public int getMurderCounts() {
		return _murderCounts;
	}
	
	public void setMurderCounts(int murderCounts) {
		_murderCounts = murderCounts;
	}
	
	public Runebook getRunebook() {
		return _runebook;
	}
	
	public ArrayList<Spell> getScrolls() {
		return _scrolls;
	}
	
	public void addScroll(Spell spell) {
		_scrolls.add(spell);
	}
	
	public int getTotalSkillVal() {
		int total = 0;
		total += getSkill("archery");
		total += getSkill("blacksmithy");
		total += getSkill("brawling");
		total += getSkill("magery");
		total += getSkill("blades");
		total += getSkill("survivalism");
		total += getSkill("mining");
		total += getSkill("lumberjacking");
		total += getSkill("taming");
		total += getSkill("fishing");
		//System.out.println("t:"+total);
		return total;
	}
	
	public Party getParty() {
		return _party;
	}
	
	public void setParty(Party party) {
		_party = party;
	}
	
	public BankBox getBankBox() {
		return _bankBox;
	}
	
	/*public void setTPTicks(int ticks) {
		_tpTicks = ticks;
	}*/
	
	public void setSpawnTicks(int ticks) {
		_spawnTicks = ticks;
	}
	
	public int getSpawnTicks() {
		return _spawnTicks;
	}
	
	public boolean isLargerBank() {
		return _largerbank;
	}
	
	public boolean isPremium() {
		return _premium;
	}
	
	public void setPremium(boolean premium) {
		_premium = premium;
	}
	
	public int getGlobals() {
		return _globals;
	}
	
	public void setGlobals(int globals) {
		_globals = globals;
	}
	
	public boolean isIgnoring(String playerName) {
		if(_ignoreList.contains(playerName))
			return true;
		return false;
	}
	
	public boolean isIgnoringLoose(String playerName) {
		Object[] ignoreListSplit = _ignoreList.toArray();
		for(Object s : ignoreListSplit) {
			String st = (String)s;
			if(st.equalsIgnoreCase(playerName))
				return true;
		}
		return false;
	}
	
	public HashSet<String> getIgnoreList() {
		return _ignoreList;
	}
	
	public void addIgnored(String playerName) {
		if(!_ignoreList.contains(playerName)) {
			_ignoreList.add(playerName);
		}
	}
	
	public void removeIgnored(String playerName) {
		if(_ignoreList.contains(playerName)) {
			_ignoreList.remove(playerName);
		}
	}
	
	public void removeIgnoredLoose(String playerName) {
		Object[] ignoreListSplit = _ignoreList.toArray();
		int numIgnored = ignoreListSplit.length;
		for(int i=numIgnored-1; i>=0; i--) {
			String check = (String)ignoreListSplit[i];
			if(check.equalsIgnoreCase(playerName)) {
				_ignoreList.remove(check);
			}
		}
	}
	
	public boolean isSkillLocked(String skillName) {
		String skillNameLower = skillName.toLowerCase();
		if(_lockedSkillHashMap.containsKey(skillNameLower)) {
			return _lockedSkillHashMap.get(skillNameLower);
		}
		return false;	
	}
	
	public boolean setSkillLocked(String skillName, boolean locked) {
		String skillNameLower = skillName.toLowerCase();
		if(_lockedSkillHashMap.containsKey(skillNameLower)) {
			_lockedSkillHashMap.put(skillNameLower, locked);
			return true;
		}
		else {
			System.out.println("failed to set skill "+skillNameLower);
			return false;
		}
	}
	
	public void setPlayTime(int playTime) {
		_playTime = playTime;
	}
	
	public int getPlayTime() {
		return _playTime;
	}
	
	public boolean isNewPlayer() {
		if(_playTime < NEW_PLAYER_TIME)
			return true;
		return false;
	}
	
	public void setCustomSpawn(Location spawn) {
		_customSpawn = spawn;
	}
	
	public Location getCustomSpawn() {
		return _customSpawn;
	}
	
	public void setVampire(boolean vamp) {
		_vampirism = vamp;
	}
	
	public boolean isVampire() {
		return _vampirism;
	}
	
	public void setRobot(boolean robot) {
		_robot = robot;
	}
	
	public boolean isRobot() {
		return _robot;
	}
	
	public int getPremiumDays() {
		return _premiumDays;
	}
	
	public void setPremiumDays(int premiumDays) {
		_premiumDays = premiumDays;
	}
	
	public void setPrivate(boolean priv) {
		_isPrivate = priv;
	}
	
	public boolean isPrivate() {
		return _isPrivate;
	}
	
	public int getPlotCreatePoints() {
		return _plotCreatePoints;
	}
	
	public void setPlotCreatePoints(int plotCreatePoints) {
		_plotCreatePoints = plotCreatePoints;
	}
	
	public int getFreeSkillPointsRemaining() {
		return _freeSkillPointsRemaining;
	}
	
	public void setFreeSkillPointsRemaining(int freeSkillPointsRemaining) {
		_freeSkillPointsRemaining = freeSkillPointsRemaining;
	}
	
	public String getTaunt() {
		return _taunt;
	}
	
	public void setTaunt(String taunt) {
		_taunt = taunt;
	}
	
	public long getLastTraitChangeTime() {
		return _traitChangeTime;
	}
	
	public void setLastTraitChangeTime(long traitChangeTime) {
		_traitChangeTime = traitChangeTime;
	}
	
	public int getBuildNumber() {
		return _buildNumber;
	}
	
	public void setBuildNumber(int buildNumber) {
		_buildNumber = buildNumber;
	}
	
	public int getRepairTicks() {
		return _repairTicks;
	}
	
	public void setRepairTicks(int ticks) {
		_repairTicks = ticks;
	}
	
	////spout
	/*public PlayerHUD getHUD() {
		return _playerHud;
	}*/
	
	/*private void checkPortals() {
		System.out.println("Teleporting");
		Player player = Utils.getPlugin().getServer().getPlayer(_playerName);
		
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
			if(targetLoc != null) {
				targetLoc = new Location(targetLoc.getWorld(), targetLoc.getX()+.5, targetLoc.getY(), targetLoc.getZ()+.5);
				Location curLoc = player.getLocation();
				targetLoc.setPitch(curLoc.getPitch());
				targetLoc.setYaw(curLoc.getYaw());
				_enteredPortalTicks = 0;
				player.teleport(targetLoc);
			}
		}
	}*/
	
	public ArrayList<PseudoWolf> getTamedWolves() {
		return _tamedWolves;
	}
	
	public void setTamedWolves(ArrayList<PseudoWolf> tamedWolves) {
		_tamedWolves = tamedWolves;
	}
	
	public void setLastLogoutLocation(Location location) {
		_lastLogoutLocation = location;
	}
	
	public Location getLastLogoutLocation() {
		return _lastLogoutLocation;
	}
}
