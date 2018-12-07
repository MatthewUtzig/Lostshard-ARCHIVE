package com.lostshard.RPG.External;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import me.neodork.npclib.entity.HumanNPC;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.PseudoWolf;
import com.lostshard.RPG.PseudoWolfHandler;
import com.lostshard.RPG.RPG;
import com.lostshard.RPG.RandChest;
import com.lostshard.RPG.Groups.Clans.Clan;
import com.lostshard.RPG.Groups.Clans.ClanHandler;
import com.lostshard.RPG.Plots.Bank;
import com.lostshard.RPG.Plots.BankBox;
import com.lostshard.RPG.Plots.LockedBlock;
import com.lostshard.RPG.Plots.NPCHandler;
import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Plots.PlotNPC;
import com.lostshard.RPG.Plots.RPGNPC;
import com.lostshard.RPG.Plots.Store;
import com.lostshard.RPG.Plots.SubPlot;
import com.lostshard.RPG.Skills.Magery;
import com.lostshard.RPG.Skills.Rune;
import com.lostshard.RPG.Skills.Runebook;
import com.lostshard.RPG.Skills.Spellbook;
import com.lostshard.RPG.Spells.PermanentGate;
import com.lostshard.RPG.Spells.Spell;
import com.lostshard.RPG.Utils.IntPoint;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class Database {	
	private static ConnectionPool _connPool = new ConnectionPool();
	
	public static void rewardVotes() {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("select * from votes;");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while(rs.next()) {
				int id = rs.getInt("id");
				String playerName = rs.getString("name");
				String serviceName = rs.getString("service");
				
				Player player = Bukkit.getServer().getPlayer(playerName.trim());
				if(playerName.equals("Test Notification")) {
					System.out.println(playerName+" was rewarded 100 gold coins for voting for the server on "+serviceName+", thanks!");
					Bukkit.getServer().broadcastMessage(playerName+" was rewarded 100 gold coins for voting for the server on "+serviceName+", thanks!");
					PreparedStatement prep2 = conn.prepareStatement("DELETE FROM votes WHERE id="+id);
					prep2.execute();
				}
				else if(player != null) {
					System.out.println(playerName+" was rewarded 100 gold coins for voting for this server on "+serviceName+", thanks!");
					PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
					pseudoPlayer.setMoney(pseudoPlayer.getMoney()+100);
					Database.updatePlayerByPseudoPlayer(pseudoPlayer);
					if(serviceName.equalsIgnoreCase("mcserverstatus"))
						serviceName = "MCServerStatus.com";
					else if(serviceName.equalsIgnoreCase("minestatus"))
						serviceName = "MineStatus.net";
					Bukkit.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE+playerName+" was rewarded 100 gold coins for voting for this server on "+serviceName+", thanks!");
					PreparedStatement prep2 = conn.prepareStatement("DELETE FROM votes WHERE id="+id);
					prep2.execute();
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean doesPlayerDataExist(String playerName) throws Exception {
		try {
			Connection conn = _connPool.getConnection();
            /*Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            Connection conn = DriverManager.getConnection (_url, _user, _pass);*/
			PreparedStatement prep = conn.prepareStatement("select COUNT(*) as rowcount from players where name=\""+playerName+"\";");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			rs.next();
			int count = rs.getInt("rowcount");
			rs.close();
			if(count > 0) {
				return true;
			}
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to get the player's data
			System.out.println(e.toString());
			throw new Exception();
		}
		return false;
	}
	
	public static boolean setBuild(PseudoPlayer pseudoPlayer, int buildNum) {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep;
			
			// If the buildnum is 0 we are getting the build from the player table
			if(buildNum == 0)
				prep = conn.prepareStatement("SELECT * FROM players where name=\""+pseudoPlayer.getName()+"\";");
			else // Otherwise we are going to look in the builds table
				prep = conn.prepareStatement("SELECT * FROM builds where name=\""+pseudoPlayer.getName()+"\" AND buildnum="+buildNum+";");
			prep.execute();
			
			// set up variables to the default in case this is a new build;
			int archery=0;
			boolean archeryLock=false;
			int blacksmithy=0;
			boolean blacksmithyLock=false;
			int brawling=0;
			boolean brawlingLock=false;
			int magery=0;
			boolean mageryLock=false;
			int blades=0;
			boolean bladesLock=false;
			int survivalism=0;
			boolean survivalismLock=false;
			int mining=0;
			boolean miningLock=false;
			int lumberjacking=0;
			boolean lumberjackingLock=false;
			int taming=0;
			boolean tamingLock=false;
			int fishing=0;
			boolean fishingLock=false;
			
			ResultSet rs = prep.getResultSet();
			if(rs.next()) {
				// If the database returned something it must have included the build data
				archery = rs.getInt("archery");
				archeryLock = rs.getBoolean("archeryLock");
				blacksmithy = rs.getInt("blacksmithy");
				blacksmithyLock = rs.getBoolean("blacksmithyLock");
				brawling = rs.getInt("brawling");
				brawlingLock = rs.getBoolean("brawlingLock");
				magery = rs.getInt("magery");
				mageryLock = rs.getBoolean("mageryLock");
				blades = rs.getInt("blades");
				bladesLock = rs.getBoolean("bladesLock");
				survivalism = rs.getInt("survivalism");
				survivalismLock = rs.getBoolean("survivalismLock");
				mining = rs.getInt("mining");
				miningLock = rs.getBoolean("miningLock");
				lumberjacking = rs.getInt("lumberjacking");
				lumberjackingLock = rs.getBoolean("lumberjackingLock");
				taming = rs.getInt("taming");
				tamingLock = rs.getBoolean("tamingLock");
				fishing = rs.getInt("fishing");
				fishingLock = rs.getBoolean("fishingLock");
			}
			else {
				// Didn't have a build with that number we must not have one, put it in
				prep = conn.prepareStatement("INSERT INTO builds (buildnum,name) VALUES('"+buildNum+"','"+pseudoPlayer.getName()+"');");
				prep.execute();
			}
			
			// start skills
			HashMap<String, Integer> skillHashMap = new HashMap<String, Integer>();
			HashMap<String, Boolean> lockedSkillHashMap = new HashMap<String, Boolean>();
			
			//archery
			skillHashMap.put("archery", archery);
			lockedSkillHashMap.put("archery", archeryLock);
			
			//blacksmithy
			skillHashMap.put("blacksmithy", blacksmithy);
			lockedSkillHashMap.put("blacksmithy", blacksmithyLock);
			
			//brawling
			skillHashMap.put("brawling", brawling);
			lockedSkillHashMap.put("brawling", brawlingLock);
			//skillHashMap.put("lumberjacking", rs.getInt("lumberjacking"));
			
			//magery
			skillHashMap.put("magery", magery);
			lockedSkillHashMap.put("magery", mageryLock);
			
			//blades
			skillHashMap.put("blades", blades);
			lockedSkillHashMap.put("blades", bladesLock);
			
			//survivalism
			skillHashMap.put("survivalism", survivalism);
			lockedSkillHashMap.put("survivalism", survivalismLock);
			// end skills
			
			//mining
			skillHashMap.put("mining", mining);
			lockedSkillHashMap.put("mining", miningLock);
			
			//lumberjacking
			skillHashMap.put("lumberjacking", lumberjacking);
			lockedSkillHashMap.put("lumberjacking", lumberjackingLock);
			// end skills
			
			//taming
			skillHashMap.put("taming", taming);
			lockedSkillHashMap.put("taming", tamingLock);
			
			//fishing
			skillHashMap.put("fishing", fishing);
			lockedSkillHashMap.put("fishing", fishingLock);
			// end skills
			
			pseudoPlayer.setSkills(skillHashMap);
			pseudoPlayer.setLockedSkills(lockedSkillHashMap);
			pseudoPlayer.setBuildNumber(buildNum);
			return true;
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to get the player's data
			System.out.println(e.toString());
		}
		return false;
	}
	
	public static boolean isPlayerPremium(String playerName) {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT premium FROM players where name=\""+playerName+"\";");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			if(rs.next()) {
				boolean premium = rs.getBoolean("premium");
				rs.close();
				return premium;
			}
			return false;
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to get the player's data
			System.out.println(e.toString());
		}
		return false;
	}
	
	public static void addNewPlayer(String playerName) throws Exception {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("insert into players (name,clan,spells,bankdata,ignorelist,bind1,bind2,bind3,bind4,bind5,bind6,bind7,bind8,bind9) values (\""+playerName+"\",'','','0@266@5@0,1@297@5@0,2@357@8@0,3@360@5@0','','','','','','','','','','');");
			prep.execute();
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to add a new player
			System.out.println(e.toString());
			throw new Exception();
		}
	}
	
	public static int getPlayerMurderCounts(String playerName) {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT murdercounts FROM players WHERE name=\""+playerName+"\";");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			int murderCounts = 0;
			if(rs.next()) {
				murderCounts = rs.getInt("murdercounts");
			}
			return murderCounts;
		}
		catch(Exception e) {
			return 0;
		}
	}
	
	public static void updatePlayerCustomSpawn(PseudoPlayer pseudoPlayer) {
		try {
			Connection conn = _connPool.getConnection();
			String customSpawnString = "0";
			if(pseudoPlayer.getCustomSpawn() == null) {
				customSpawnString = "0";
			}
			else {
				Location sLoc = pseudoPlayer.getCustomSpawn();
				customSpawnString = sLoc.getWorld().getName()+","+sLoc.getX()+","+sLoc.getY()+","+sLoc.getZ();
			}
			PreparedStatement prep = conn.prepareStatement("UPDATE players SET customspawn='"+customSpawnString+"' WHERE name=\""+pseudoPlayer.getName()+"\";");
			prep.execute();
		}
		catch(Exception e) {
		}
	}
			
	public static PseudoPlayer createPseudoPlayer(String playerName) {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM players WHERE name=\""+playerName+"\";");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			if(rs.next()) {
				int playerId = rs.getInt("id");
				String name = rs.getString("name");
				int money = rs.getInt("money");
				int mana = rs.getInt("mana");
				int stamina = rs.getInt("stamina");
				String clanName = rs.getString("clan");
				int murderCounts = rs.getInt("murdercounts");
				int spawnTicks = rs.getInt("spawnticks");
				boolean largerbank = rs.getBoolean("largerbank");
				boolean premium = rs.getBoolean("premium");
				int globals = rs.getInt("globals");
				String ignoreListString = rs.getString("ignorelist");
				int playTime = rs.getInt("playtime");
				int dielog = rs.getInt("dielog");
				String customSpawnString = rs.getString("customspawn");
				int premiumDays = rs.getInt("premiumdays");
				boolean priv = rs.getBoolean("private");
				int plotCreatePoints = rs.getInt("plotcreatepoints");
				int freeSkillPointsRemaining = rs.getInt("freeskillpointsremaining");
				String taunt = rs.getString("taunt");
				long traitChangeTime = rs.getLong("lasttraitchangetime");
				int crimTicks = rs.getInt("crimticksremaining");
				long lastLogout = rs.getLong("lastlogout");
				int buildNum = rs.getInt("buildnum");
				String titleCSV = rs.getString("availabletitles");
				String activeTitle = rs.getString("activetitle");
				String tamedWolvesString = rs.getString("tamedwolves");
				String lastLogoutLocationString = rs.getString("lastlogoutlocation");
				//VAMP
				boolean vampirism = false;//rs.getBoolean("vampirism");
				boolean robot = false;//rs.getBoolean("robot");
				
				Location customSpawnLocation = null;
				try {
					String[] splitCustomSpawnString = customSpawnString.split(",");
					if(splitCustomSpawnString.length >= 4) {
						String worldName = splitCustomSpawnString[0];
						Double locX = Double.parseDouble(splitCustomSpawnString[1]);
						Double locY = Double.parseDouble(splitCustomSpawnString[2]);
						Double locZ = Double.parseDouble(splitCustomSpawnString[3]);
						customSpawnLocation = new Location(Utils.getPlugin().getServer().getWorld(worldName), locX, locY, locZ);
					}
				}
				catch (Exception e) {e.printStackTrace();}
				
				Location lastLogoutLocation = null;
				try {
					String[] splitLastLogoutString = lastLogoutLocationString.split(",");
					if(splitLastLogoutString.length >= 4) {
						String worldName = splitLastLogoutString[0];
						Double locX = Double.parseDouble(splitLastLogoutString[1]);
						Double locY = Double.parseDouble(splitLastLogoutString[2]);
						Double locZ = Double.parseDouble(splitLastLogoutString[3]);
						lastLogoutLocation = new Location(Utils.getPlugin().getServer().getWorld(worldName), locX, locY, locZ);
					}
				}
				catch (Exception e) {e.printStackTrace();}
				
				//parse ignore list
				HashSet<String> ignoreList = new HashSet<String>();
				ignoreListString = ignoreListString.trim();
				if(!ignoreListString.equals("")) {
					String[] split = ignoreListString.split(",");
					for(String s : split) {
						if(!ignoreList.contains(s))
							ignoreList.add(s);
					}
				}
				// end parse ignore list
				
				// begin binds
				HashMap<Integer, Spell> spellSlotHashMap = new HashMap<Integer, Spell>();
				for(int i=0; i<9; i++) {
					String bindName = rs.getString("bind"+(i+1));
					Spell spell = Spell.getSpellByName(bindName);
					spellSlotHashMap.put(i, spell);
				}
				// end binds
				
				int archery=0;
				boolean archeryLock=false;
				int blacksmithy=0;
				boolean blacksmithyLock=false;
				int brawling=0;
				boolean brawlingLock=false;
				int magery=0;
				boolean mageryLock=false;
				int blades=0;
				boolean bladesLock=false;
				int survivalism=0;
				boolean survivalismLock=false;
				int mining=0;
				boolean miningLock=false;
				int lumberjacking=0;
				boolean lumberjackingLock=false;
				int taming=0;
				boolean tamingLock=false;
				int fishing=0;
				boolean fishingLock=false;
				
				if(buildNum == 0) {
					archery = rs.getInt("archery");
					blacksmithy = rs.getInt("blacksmithy");
					brawling = rs.getInt("brawling");
					magery = rs.getInt("magery");
					blades = rs.getInt("blades");
					survivalism = rs.getInt("survivalism");
					mining = rs.getInt("mining");
					lumberjacking = rs.getInt("lumberjacking");
					taming = rs.getInt("taming");
					fishing = rs.getInt("fishing");
					
					archeryLock = rs.getBoolean("archeryLock");
					blacksmithyLock = rs.getBoolean("blacksmithyLock");
					brawlingLock = rs.getBoolean("brawlingLock");
					mageryLock = rs.getBoolean("mageryLock");
					bladesLock = rs.getBoolean("bladesLock");
					survivalismLock = rs.getBoolean("survivalismLock");
					miningLock = rs.getBoolean("miningLock");
					lumberjackingLock = rs.getBoolean("lumberjackingLock");
					tamingLock = rs.getBoolean("tamingLock");
					fishingLock = rs.getBoolean("fishingLock");
				}
				else {
					PreparedStatement prep2 = conn.prepareStatement("SELECT * FROM builds WHERE name=\""+playerName+"\" AND buildnum="+buildNum+";");
					prep2.execute();
					ResultSet rs2 = prep2.getResultSet();
					if(rs2.next()) {
						archery = rs2.getInt("archery");
						blacksmithy = rs2.getInt("blacksmithy");
						brawling = rs2.getInt("brawling");
						magery = rs2.getInt("magery");
						blades = rs2.getInt("blades");
						survivalism = rs2.getInt("survivalism");
						mining = rs2.getInt("mining");
						lumberjacking = rs2.getInt("lumberjacking");
						taming = rs2.getInt("taming");
						fishing = rs2.getInt("fishing");
						
						archeryLock = rs2.getBoolean("archeryLock");
						blacksmithyLock = rs2.getBoolean("blacksmithyLock");
						brawlingLock = rs2.getBoolean("brawlingLock");
						mageryLock = rs2.getBoolean("mageryLock");
						bladesLock = rs2.getBoolean("bladesLock");
						survivalismLock = rs2.getBoolean("survivalismLock");
						miningLock = rs2.getBoolean("miningLock");
						lumberjackingLock = rs2.getBoolean("lumberjackingLock");
						tamingLock = rs2.getBoolean("tamingLock");
						fishingLock = rs2.getBoolean("fishingLock");
					}
					else throw new Exception();
				}
				
				// start skills
				HashMap<String, Integer> skillHashMap = new HashMap<String, Integer>();
				HashMap<String, Boolean> lockedSkillHashMap = new HashMap<String, Boolean>();
				
				//archery
				skillHashMap.put("archery", archery);
				lockedSkillHashMap.put("archery", archeryLock);
				
				//blacksmithy
				skillHashMap.put("blacksmithy", blacksmithy);
				lockedSkillHashMap.put("blacksmithy", blacksmithyLock);
				
				//brawling
				skillHashMap.put("brawling", brawling);
				lockedSkillHashMap.put("brawling", brawlingLock);
				//skillHashMap.put("lumberjacking", rs.getInt("lumberjacking"));
				
				//magery
				skillHashMap.put("magery", magery);
				lockedSkillHashMap.put("magery", mageryLock);
				
				//blades
				skillHashMap.put("blades", blades);
				lockedSkillHashMap.put("blades", bladesLock);
				
				//survivalism
				skillHashMap.put("survivalism", survivalism);
				lockedSkillHashMap.put("survivalism", survivalismLock);
				// end skills
				
				//mining
				skillHashMap.put("mining", mining);
				lockedSkillHashMap.put("mining", miningLock);
				
				//lumberjacking
				skillHashMap.put("lumberjacking", lumberjacking);
				lockedSkillHashMap.put("lumberjacking", lumberjackingLock);
				
				//taming
				skillHashMap.put("taming", taming);
				lockedSkillHashMap.put("taming", tamingLock);
				
				//fishing
				skillHashMap.put("fishing", fishing);
				lockedSkillHashMap.put("fishing", fishingLock);
				
				// end skills
				
				//begin bank
				BankBox bankBox = new BankBox(rs.getString("bankdata"), largerbank);
				//end bank
				
				// parse spells
				String spellNamesString = rs.getString("spells");
				ArrayList<String> spellNames;
				if(spellNamesString.equalsIgnoreCase("") || spellNamesString.equalsIgnoreCase(" ")) {
					spellNames = new ArrayList<String>();
				}
				else {
					String[] spellNamesArray = spellNamesString.split(",");
					spellNames = new ArrayList<String>(Arrays.asList(spellNamesArray));
				}
				
				Spellbook spellbook = new Spellbook();
				
				for(String spellName : spellNames) {
					Spell spell = Spell.getSpellByName(spellName);
					if(spell != null)
						spellbook.addSpell(spell);
				}
				// end spells
				
				// parse runes
				//prep = conn.prepareStatement("SELECT * FROM runes WHERE owner=\""+playerName+"\";");
				prep = conn.prepareStatement("SELECT * FROM runes WHERE ownerid=\""+playerId+"\";");
				prep.execute();
				rs = prep.getResultSet();
				Runebook runebook = new Runebook();
				while(rs.next()) {
					int id = rs.getInt("id");
					try {
						String label = rs.getString("label");
						String locationString = rs.getString("location");
						
						String[] splitLocationString = locationString.split(",");
						String worldName = splitLocationString[0];
						World world = Utils.getPlugin().getServer().getWorld(worldName);
						double locX = Double.parseDouble(splitLocationString[1]);
						double locY = Double.parseDouble(splitLocationString[2]);
						double locZ = Double.parseDouble(splitLocationString[3]);
						Location location = new Location(world, locX, locY, locZ);
						Rune rune = new Rune(label, location, id);
						runebook.addRune(rune);
					}
					catch(Exception e) {
						System.out.println("ERROR: Failed to load "+name+"'s rune: "+id);
					}
				}
				// end runes
				
				// parse scrolls
				prep = conn.prepareStatement("SELECT * FROM scrolls WHERE ownerid=\""+playerId+"\";");
				prep.execute();
				rs = prep.getResultSet();
				ArrayList<Spell> scrolls = new ArrayList<Spell>();
				while(rs.next()) {
					String spellName = rs.getString("name");
					Spell spell = Spell.getSpellByName(spellName);
					if(spell != null) {
						spell.setId(rs.getInt("id"));
						scrolls.add(spell);
					}
				}
				// end scrolls
				
				// parse titles
				ArrayList<String> availableTitles = new ArrayList<String>();
				if(titleCSV != null && !titleCSV.equals("") && !titleCSV.equals(" ")) {
					String[] titles = titleCSV.split(",");
					for(String title : titles) {
						availableTitles.add(title);
					}
				}
				if(!availableTitles.contains(activeTitle)) {
					activeTitle = "";
				}
				// end titles
				
				
				PseudoPlayer pseudoPlayer = new PseudoPlayer(playerId, name, clanName, money, mana, stamina, skillHashMap, murderCounts, spellbook, runebook, scrolls, bankBox, largerbank, premium, globals, ignoreList, spellSlotHashMap, lockedSkillHashMap, playTime, customSpawnLocation, premiumDays, plotCreatePoints, freeSkillPointsRemaining, traitChangeTime, crimTicks, lastLogout, availableTitles, activeTitle);
				pseudoPlayer.setSpawnTicks(spawnTicks);
				pseudoPlayer._dieLog = 0;
				pseudoPlayer.setVampire(vampirism);
				pseudoPlayer.setRobot(robot);
				pseudoPlayer.setPrivate(priv);
				pseudoPlayer.setTaunt(taunt);
				pseudoPlayer.setBuildNumber(buildNum);
				
				pseudoPlayer.setLastLogoutLocation(lastLogoutLocation);
				
				ArrayList<PseudoWolf> playerPseudoWolves = new ArrayList<PseudoWolf>();
				if(tamedWolvesString != null) {
					tamedWolvesString = tamedWolvesString.trim();
					if(tamedWolvesString != "") {
						String[] splitTamedWolvesString = tamedWolvesString.split(",");
						for(String wolfIdString : splitTamedWolvesString) {
							try {
								int wolfId = Integer.parseInt(wolfIdString);
								ArrayList<PseudoWolf> pseudoWolves = PseudoWolfHandler.getPseudoWolves();
								for(PseudoWolf pW : pseudoWolves) {
									if(pW.getId() == wolfId)
										playerPseudoWolves.add(pW);
								}
							}
							catch(Exception e) {e.printStackTrace();}
						}
					}
				}
				pseudoPlayer.setTamedWolves(playerPseudoWolves);
				
				return pseudoPlayer;
			}
			else {
				// failed to find the pseudoplayer, 
				System.out.println("No pseudoplayer data found in the database");
			}
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to get the pseudoplayer data
			System.out.println(e.toString());
		}
		return null;
	}
	
	public static PseudoWolf loadPseudoWolf(int uniqueId) {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM players WHERE entityid=\""+uniqueId+"\";");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			if(rs.next()) {
				String myUniqueId = rs.getString("uniqueid");
				String name = rs.getString("name");				
				
				PseudoWolf pseudoWolf = new PseudoWolf(name, myUniqueId);
				return pseudoWolf;
			}
			else {
				// failed to find the pseudoplayer, 
				System.out.println("No pseudowolf data found in the database");
				return null;
			}
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to get the pseudoplayer data
			System.out.println(e.toString());
		}
		return null;
	}
	
	public static int addPseudoWolf(PseudoWolf pseudoWolf) {
		try {
			Connection conn = _connPool.getConnection();	
			String sql = "INSERT into tamedwolves (name, uniqueid) values (\""+pseudoWolf.getName()+"\", \""+pseudoWolf.getUniqueId()+"\");";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			int id = -1;
			ResultSet rs = prep.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			return id;
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to add the pseudowolf data
			System.out.println(e.toString());
		}
		return -1;
	}
	
	public static void removePseudoWolf(PseudoWolf pseudoWolf) {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("DELETE FROM tamedwolves WHERE uniqueid='"+pseudoWolf.getUniqueId()+"';");
			prep.execute();
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to add the pseudowolf data
			System.out.println(e.toString());
		}
	}
	
	public static void loadPseudoWolves() {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM tamedwolves;");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String uniqueId = rs.getString("uniqueid");
				PseudoWolf pseudoWolf = new PseudoWolf(uniqueId, name);
				pseudoWolf.setId(id);
				PseudoWolfHandler.add(pseudoWolf);
				System.out.println("Added "+pseudoWolf.getName());
			}
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to add the pseudowolf data
			System.out.println(e.toString());
		}
	}
	
	public static void updatePseudoWolf(PseudoWolf pseudoWolf) {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("UPDATE tamedwolves SET "+
					"name=\""+pseudoWolf.getName()+"\","+
					"uniqueid=\""+pseudoWolf.getUniqueId()+"\" "+
					"WHERE id="+pseudoWolf.getId()+";");
			prep.execute();
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to update money
			System.out.println(e.toString());
		}
	}
	
	public static void updatePseudoPlayerTamedWolves(PseudoPlayer pseudoPlayer) {
		ArrayList<PseudoWolf> tamedWolves = pseudoPlayer.getTamedWolves();
		String tamedWolfString = "";
		int numTamedWolves = tamedWolves.size();
		for(int i=0; i<numTamedWolves; i++) {
			PseudoWolf pW = tamedWolves.get(i);
			tamedWolfString += pW.getId();
			if(i < numTamedWolves-1)
				tamedWolfString+=",";
		}
		
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("UPDATE players SET "+
					"tamedwolves=\""+tamedWolfString+"\" "+
					"WHERE id="+pseudoPlayer.getId()+";");
			prep.execute();
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to update money
			System.out.println(e.toString());
		}
	}

	public static ArrayList<Plot> getPlots() throws Exception{
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM plots;");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			ArrayList<Plot> plots = new ArrayList<Plot>();
			while(rs.next()) {
				String name = rs.getString("name");
				try {
					int id = rs.getInt("id");
					String locationString = rs.getString("location");
					String[] splitLocationString = locationString.split(",");
					String worldName = splitLocationString[0];
					World world = Utils.getPlugin().getServer().getWorld(worldName);
					double locX = Double.parseDouble(splitLocationString[1]);
					double locY = Double.parseDouble(splitLocationString[2]);
					double locZ = Double.parseDouble(splitLocationString[3]);
					Location location = new Location(world, locX, locY, locZ);
					int radius = rs.getInt("radius");
					String ownerName = rs.getString("owner");
					String coOwnerNamesString = rs.getString("coowners");
					ArrayList<String> coOwnerNames;
					boolean isControlPoint = rs.getBoolean("iscontrolpoint");
					String ownedClanString = rs.getString("owningclan");
					Clan clan;
					if(ownedClanString == null)
						clan = null;
					else
						clan = ClanHandler.findClanByHashmap(ownedClanString);
					boolean isNoMagicPlot = rs.getBoolean("isnomagicplot");
					boolean isNoPvPPlot = rs.getBoolean("isnopvpplot");
					
					// make sure stuff is do
					if(coOwnerNamesString.equalsIgnoreCase("") || coOwnerNamesString.equalsIgnoreCase(" ")) {
						coOwnerNames = new ArrayList<String>();
					}
					else {
						String[] coOwnerNamesArray = coOwnerNamesString.split(",");
						coOwnerNames = new ArrayList<String>(Arrays.asList(coOwnerNamesArray));
					}
					
					String friendNamesString = rs.getString("friends");
					// verify that thing is do 
					ArrayList<String> friendNames;
					if(friendNamesString.equalsIgnoreCase("") || friendNamesString.equalsIgnoreCase(" ")) {
						friendNames = new ArrayList<String>();
					}
					else {
						String[] friendNamesArray = friendNamesString.split(",");
						friendNames = new ArrayList<String>(Arrays.asList(friendNamesArray));
					}
					int money = rs.getInt("money");
					boolean protect = rs.getBoolean("protected");
					boolean locked = rs.getBoolean("locked");
					boolean city = rs.getBoolean("city");
					boolean dungeon = rs.getBoolean("dungeon");
					boolean friendBuild = rs.getBoolean("friendbuild");
					boolean kickupgrade = rs.getBoolean("kickupgrade");
					boolean neutral = rs.getBoolean("neutral");
					int forSaleAmount = rs.getInt("forsalecost");
					boolean explosionAllowed = rs.getBoolean("allowexplosions");
					long lastCaptureTime = 0;
					if(isControlPoint) {
						lastCaptureTime = rs.getLong("lastcapturetime");
					}
					//lockblocks
					PreparedStatement prep2 = conn.prepareStatement("SELECT * FROM lockedblocks;");
					prep2.execute();
					ResultSet rs2 = prep2.getResultSet();
					HashMap<String, LockedBlock> lockedBlocks = new HashMap<String, LockedBlock>();
					while(rs2.next()) {
						int lbid = rs2.getInt("id");
						String locString = rs2.getString("location");
						int key = rs2.getInt("bkey");
						LockedBlock lockedBlock = new LockedBlock(lbid, locString, key);
						lockedBlocks.put(locString, lockedBlock);
					}
					
					ArrayList<PlotNPC> plotNPCs = new ArrayList<PlotNPC>();
					
					//plotnpcs
					prep2 = conn.prepareStatement("SELECT * FROM plotnpcs WHERE plotid="+id+";");
					prep2.execute();
					rs2 = prep2.getResultSet();
					while(rs2.next()) {
						int npcId = rs2.getInt("id");
						int npcPlotId = rs2.getInt("plotid");
						String npcName = rs2.getString("name");
						String npcLocString = rs2.getString("location");
						String npcType = rs2.getString("type");
						String additional = rs2.getString("additional");
						
						String[] splitNPCLocationString = npcLocString.split(",");
						String npcWorldName = splitNPCLocationString[0];
						World npcWorld = Utils.getPlugin().getServer().getWorld(npcWorldName);
						double npcLocX = Double.parseDouble(splitNPCLocationString[1]);
						double npcLocY = Double.parseDouble(splitNPCLocationString[2]);
						double npcLocZ = Double.parseDouble(splitNPCLocationString[3]);
						Float npcPitch = Float.parseFloat(splitNPCLocationString[4]);
						Float npcYaw = Float.parseFloat(splitNPCLocationString[5]);
						Location npcLocation = new Location(npcWorld, npcLocX, npcLocY, npcLocZ);
						npcLocation.setPitch(npcPitch);
						npcLocation.setYaw(npcYaw);
						
						PlotNPC rpgNpc;
						/*if(world.isChunkLoaded(npcLocation.getBlockX()/16, npcLocation.getBlockZ()/16)) {
							BasicHumanNpc npcEntity = NPCHandler._npcManager.spawnNPC(npcName, npcLocation, npcName);
							rpgNpc = new PlotNPC(npcId, npcPlotId, npcName, npcLocation, npcType, npcEntity, additional);
							System.out.println("Spawning "+rpgNpc.getName());
						}
						else {*/
							rpgNpc = new PlotNPC(npcId, npcPlotId, npcName, npcLocation, npcType, null, additional);
							System.out.println("Not spawning: " +rpgNpc.getName());
						//}
						
						//NPCHandler.addRPGNPC(rpgNpc);
						plotNPCs.add(rpgNpc);
						/*NPCHandler.deSpawnNPC(npcName);
						PlotNPC plotNPC = new PlotNPC(npcId, npcPlotId, npcName, npcLocation, npcType, null, additional);
						plotNPCs.add(plotNPC);
						NPCHandler.addPlotNPC(plotNPC);
						NPCHandler.spawnNPC(npcName, npcName, 0, npcType, npcLocation);*/
						
					}
					Plot plot = new Plot(name, location, radius, ownerName, coOwnerNames, friendNames, money, protect, locked, city, lockedBlocks, friendBuild, plotNPCs, forSaleAmount, kickupgrade, neutral, explosionAllowed);
					plot.setLastCaptureTime(lastCaptureTime);
					plot.setIsControlPoint(isControlPoint);
					plot.setOwningClan(clan);
					plot.setIsDungeon(dungeon);
					plot.setId(id);
					plots.add(plot);
					plot.setIsNoMagicPlot(isNoMagicPlot);
					plot.setIsNoPvPPlot(isNoPvPPlot);
					
					if(plot.getName().equalsIgnoreCase(Plot.GORP_PLOT_NAME))
						ClanHandler.SetGorpControlClan(clan);
					if(plot.getName().equalsIgnoreCase(Plot.BUNT_PLOT_NAME))
						ClanHandler.SetBuntControlClan(clan);
				}
				catch(Exception e) {
					System.out.println("(CRITICAL ERROR) Exception when generating \""+name+"\" plot: "+e.toString());
				}
			}
			return plots;
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to get the plots
			System.out.println(e.toString());
			throw new Exception();
		}
	}
	
	public static ArrayList<SubPlot> getSubPlots() throws Exception{
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM subplots;");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			ArrayList<SubPlot> subPlots = new ArrayList<SubPlot>();
			while(rs.next()) {
				String name = rs.getString("name");
				try {
					int id = rs.getInt("id");
					String locationString = rs.getString("location");
					String[] splitLocationString = locationString.split(",");
					String worldName = splitLocationString[0];
					World world = Utils.getPlugin().getServer().getWorld(worldName);
					double locX = Double.parseDouble(splitLocationString[1]);
					double locY = Double.parseDouble(splitLocationString[2]);
					double locZ = Double.parseDouble(splitLocationString[3]);
					Location location = new Location(world, locX, locY, locZ);
					int radius = rs.getInt("size");
					String ownerName = rs.getString("owner");
					int owningPlotId = rs.getInt("owningplotid");
					String type = rs.getString("type");
					SubPlot subPlot = new SubPlot(name, location, radius, ownerName, owningPlotId, type);
					subPlot.setId(id);
					subPlots.add(subPlot);
				}
				catch(Exception e) {
					System.out.println("(CRITICAL ERROR) Exception when generating \""+name+"\" plot: "+e.toString());
				}
			}
			return subPlots;
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to get the plots
			System.out.println(e.toString());
			throw new Exception();
		}
	}

	/*public static void updateMoney(String playerName, int curMoney) throws Exception {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("UPDATE players SET money="+curMoney+" WHERE name=\""+playerName+"\";");
			prep.execute();
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to update money
			System.out.println(e.toString());
			throw new Exception();
		}
	}*/
	
	public static int addPlot(Plot plot) throws Exception {
		try {
			Connection conn = _connPool.getConnection();
            String locationString = plot.getLocation().getWorld().getName()+","+plot.getLocation().getX()+","+plot.getLocation().getY()+","+plot.getLocation().getZ();
			//PreparedStatement prep = conn.prepareStatement("insert into plots (name,location,radius,owner,coowners,friends) values (\""+plot.getName()+"\", \""+locationString+"\", \""+plot.getRadius()+"\", \""+plot.getOwner()+"\", \"\", \"\");");
            String sql = "insert into plots (name,location,radius,owner,coowners,friends,protected,locked) values (\""+plot.getName()+"\", \""+locationString+"\", \""+plot.getRadius()+"\", \""+plot.getOwner()+"\", \"\", \"\",'1','1');";
            PreparedStatement prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prep.executeUpdate();
			int id = -1;
			ResultSet rs = prep.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			return id;
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to add a new plot
			System.out.println(e.toString());
			throw new Exception();
		}
	}
	
	public static void removePlot(Plot plot) {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("DELETE FROM plots WHERE name='"+plot.getName()+"';");
			prep.execute();
			prep = conn.prepareStatement("DELETE FROM lockedblocks WHERE plotid='"+plot.getId()+"';");
			prep.execute();
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to add a new clan
			System.out.println("(ERROR) Failed to delete plot "+plot.getName()+">>"+e.toString());
		}
	}
	
	public static void updatePlot(Plot plot) {
		try {
			Connection conn = _connPool.getConnection();
            String coOwnersString = Utils.implode(plot.getCoOwners());
            String friendsString = Utils.implode(plot.getFriends());
            int plotProtect = 0;
            if(plot.isProtected())
            	plotProtect = 1;
            int plotLocked = 0;
            if(plot.isLocked())
            	plotLocked = 1;
            int plotIsCity = 0;
            if(plot.isCity())
            	plotIsCity = 1;
            int plotIsDungeon = 0;
            if(plot.isDungeon())
            	plotIsDungeon = 1;
            int plotIsFriendBuild = 0;
            if(plot.isFriendBuild())
            	plotIsFriendBuild = 1;
            int plotHasAutoKick = 0;
            if(plot.hasKickUpgrade())
            	plotHasAutoKick = 1;
            int plotNeutral = 0;
            if(plot.isNeutral())
            	plotNeutral = 1;
            int explosionAllowed = 0;
            if(plot.isExplosionAllowed())
            	explosionAllowed = 1;
            int isControlPoint = 0;
            if(plot.isControlPoint())
            	isControlPoint = 1;
            int isNoMagicPlot = 0;
            if(plot.isNoMagicPlot())
            	isNoMagicPlot = 1;
            int isNoPvPPlot = 0;
            if(plot.isNoPvPPlot())
            	isNoPvPPlot = 1;
            String clantext = "";
            Clan clan = plot.getOwningClan();
            if(clan != null)
            	clantext = plot.getOwningClan().getName();
            else clantext = "";
            
			PreparedStatement prep = conn.prepareStatement("UPDATE plots SET "+
					"name=\""+plot.getName()+"\","+
					"radius="+plot.getRadius()+","+
					"owner=\""+plot.getOwner()+"\","+
					"coowners=\""+coOwnersString+"\","+
					"friends=\""+friendsString+"\","+
					"money=\""+plot.getMoney()+"\","+
					"protected=\""+plotProtect+"\","+
					"city=\""+plotIsCity+"\","+
					"dungeon=\""+plotIsDungeon+"\","+
					"friendbuild=\""+plotIsFriendBuild+"\","+
					"kickupgrade=\""+plotHasAutoKick+"\","+
					"neutral=\""+plotNeutral+"\","+
					"allowexplosions=\""+explosionAllowed+"\","+
					"forsalecost=\""+plot.getSaleCost()+"\","+
					"iscontrolpoint="+isControlPoint+","+
					"lastcapturetime="+plot.getLastCaptureTime()+","+
					"owningclan=?,"+//\""+clantext+"\","+
					"isnomagicplot="+isNoMagicPlot+","+
					"isnopvpplot="+isNoPvPPlot+","+
					"locked=\""+plotLocked+"\" WHERE id="+plot.getId()+";");
			prep.setString(1, clantext);
			prep.execute();
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to update a plot
			System.out.println("(CRITICAL ERROR): Failed to update plot:"+plot.getName()+ " >> "+e.toString());
		}
	}
	
	public static boolean updatePlayer(String playerName) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(playerName);
		updatePlayerByPseudoPlayer(pseudoPlayer);
		/*try {
			Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            Connection conn = DriverManager.getConnection (_url, _user, _pass);
            PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(playerName);
            String clanName = "";
            Clan clan = pseudoPlayer.getClan();
            if(clan != null)
            	clanName = clan.getName();
			PreparedStatement prep = conn.prepareStatement("UPDATE players SET "+
				"money="+pseudoPlayer.getMoney()+","+
				"clan=\""+clanName+"\","+
				"mana="+pseudoPlayer.getMana()+","+
				"archery="+pseudoPlayer.getSkill("archery")+","+
				"blacksmithy="+pseudoPlayer.getSkill("blacksmithy")+","+
				"brawling="+pseudoPlayer.getSkill("brawling")+","+
				"lumberjacking="+pseudoPlayer.getSkill("lumberjacking")+","+
				"magery="+pseudoPlayer.getSkill("magery")+","+
				"swordsmanship="+pseudoPlayer.getSkill("blades")+","+
				"murdercounts="+pseudoPlayer.getMurderCounts()+" "+
				"WHERE name=\""+playerName+"\";");
			prep.execute();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to update player "+playerName+">>"+e.toString());
			return false;
		}*/
		return true;
	}
	
	public static boolean updatePlayerByPseudoPlayer(PseudoPlayer pseudoPlayer) {
		try {
			Connection conn = _connPool.getConnection();
            String clanName = "";
            Clan clan = pseudoPlayer.getClan();
            if(clan != null)
            	clanName = clan.getName();
            ArrayList<Spell> spells = pseudoPlayer.getSpellbook().getSpells();
            String spellString = "";
            int numSpells = spells.size();
            for(int i=0; i<numSpells; i++) {
            	spellString+=spells.get(i).getName();
            	if(i < numSpells-1)
            		spellString+=",";
            }
            
            HashSet<String> ignoreList = pseudoPlayer.getIgnoreList();
            String ignoreListString = "";
            if(ignoreList.size() > 0) {
            	Object[] ignoreListSplit = ignoreList.toArray();
            	int numItems = ignoreListSplit.length;
            	for(int i=0; i<numItems; i++) {
            		String s = (String)ignoreListSplit[i];
            		ignoreListString += s;
            		if(i < numItems-1)
            			ignoreListString += ",";
            	}
            }
            
            int priv = 0;
            if(pseudoPlayer.isPrivate())
            	priv = 1;
            
            int buildNum = pseudoPlayer.getBuildNumber();
            if(buildNum == 0) {            
				PreparedStatement prep = conn.prepareStatement("UPDATE players SET "+
					"money="+pseudoPlayer._money+","+
					"clan=\""+clanName+"\","+
					"mana="+pseudoPlayer.getMana()+","+
					"stamina="+pseudoPlayer.getStamina()+","+
					"archery="+pseudoPlayer.getSkill("archery")+","+
					"blacksmithy="+pseudoPlayer.getSkill("blacksmithy")+","+
					"brawling="+pseudoPlayer.getSkill("brawling")+","+
					"magery="+pseudoPlayer.getSkill("magery")+","+
					"blades="+pseudoPlayer.getSkill("blades")+","+
					"survivalism="+pseudoPlayer.getSkill("survivalism")+","+
					"mining="+pseudoPlayer.getSkill("mining")+","+
					"lumberjacking="+pseudoPlayer.getSkill("lumberjacking")+","+
					"taming="+pseudoPlayer.getSkill("taming")+","+
					"fishing="+pseudoPlayer.getSkill("fishing")+","+
					"spells=\""+spellString+"\","+
					"murdercounts="+pseudoPlayer.getMurderCounts()+", "+
					"globals="+pseudoPlayer.getGlobals()+", "+
					"ignorelist='"+ignoreListString+"', "+
					"playtime="+pseudoPlayer.getPlayTime()+", "+
					"dielog="+pseudoPlayer._dieLog+", "+
					"lastlogout="+pseudoPlayer._lastLogout+", "+
					"private="+priv+", "+
					"plotcreatepoints="+pseudoPlayer.getPlotCreatePoints()+", "+
					"lasttraitchangetime="+pseudoPlayer.getLastTraitChangeTime()+", "+
					"taunt=?, "+
					"freeskillpointsremaining="+pseudoPlayer.getFreeSkillPointsRemaining()+", "+
					"crimticksremaining="+pseudoPlayer._criminalTicks+", "+
					"buildnum="+buildNum+", "+
					"availabletitles=?, "+
					"activetitle=?, "+
					"spawnticks="+pseudoPlayer.getSpawnTicks()+" "+
					"WHERE name=\""+pseudoPlayer.getName()+"\";");
				prep.setString(1, pseudoPlayer.getTaunt());
				
				String titleCSV = "";
				int numAvailableTitles = pseudoPlayer._availableTitles.size();
				for(int i=0; i<numAvailableTitles; i++) {
					titleCSV += pseudoPlayer._availableTitles.get(i);
					if(i < numAvailableTitles-1)
						titleCSV += ",";
				}
				prep.setString(2, titleCSV);
				
				prep.setString(3, pseudoPlayer._activeTitle);
				
				prep.execute();
            }
            else {
            	PreparedStatement prep = conn.prepareStatement("UPDATE players SET "+
    				"money="+pseudoPlayer._money+","+
    				"clan=\""+clanName+"\","+
    				"mana="+pseudoPlayer.getMana()+","+
    				"stamina="+pseudoPlayer.getStamina()+","+
    				"spells=\""+spellString+"\","+
    				"murdercounts="+pseudoPlayer.getMurderCounts()+", "+
    				"globals="+pseudoPlayer.getGlobals()+", "+
    				"ignorelist='"+ignoreListString+"', "+
    				"playtime="+pseudoPlayer.getPlayTime()+", "+
    				"dielog="+pseudoPlayer._dieLog+", "+
    				"lastlogout="+pseudoPlayer._lastLogout+", "+
    				"private="+priv+", "+
    				"plotcreatepoints="+pseudoPlayer.getPlotCreatePoints()+", "+
    				"lasttraitchangetime="+pseudoPlayer.getLastTraitChangeTime()+", "+
    				"taunt=?, "+
    				"freeskillpointsremaining="+pseudoPlayer.getFreeSkillPointsRemaining()+", "+
    				"crimticksremaining="+pseudoPlayer._criminalTicks+", "+
    				"buildnum="+buildNum+", "+
    				"availabletitles=?, "+
					"activetitle=?, "+
    				"spawnticks="+pseudoPlayer.getSpawnTicks()+" "+
    				"WHERE name=\""+pseudoPlayer.getName()+"\";");
    			prep.setString(1, pseudoPlayer.getTaunt());
    			
    			String titleCSV = "";
				int numAvailableTitles = pseudoPlayer._availableTitles.size();
				for(int i=0; i<numAvailableTitles; i++) {
					titleCSV += pseudoPlayer._availableTitles.get(i);
					if(i < numAvailableTitles-1)
						titleCSV += ",";
				}
				prep.setString(2, titleCSV);
				
				prep.setString(3, pseudoPlayer._activeTitle);
				
    			prep.execute();
    			
    			prep = conn.prepareStatement("UPDATE builds SET "+
	    			"archery="+pseudoPlayer.getSkill("archery")+","+
					"blacksmithy="+pseudoPlayer.getSkill("blacksmithy")+","+
					"brawling="+pseudoPlayer.getSkill("brawling")+","+
					"magery="+pseudoPlayer.getSkill("magery")+","+
					"blades="+pseudoPlayer.getSkill("blades")+","+
					"survivalism="+pseudoPlayer.getSkill("survivalism")+","+
					"lumberjacking="+pseudoPlayer.getSkill("lumberjacking")+","+
					"taming="+pseudoPlayer.getSkill("taming")+","+
					"fishing="+pseudoPlayer.getSkill("fishing")+","+
					"mining="+pseudoPlayer.getSkill("mining")+" "+
					"WHERE name=\""+pseudoPlayer.getName()+"\" AND buildnum="+buildNum+";");
    			prep.execute();	
            }
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to update player "+pseudoPlayer.getName()+">>"+e.toString());
			return false;
		}
		return true;
	}
	
	public static boolean updateSpellBind(PseudoPlayer pseudoPlayer, int slot, Spell spell) {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("UPDATE players SET bind"+(slot+1)+"='"+spell.getName()+"' WHERE id="+pseudoPlayer.getId()+";");
			prep.execute();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to update bind for "+pseudoPlayer.getName()+", slot"+slot+">>"+e.toString());
			return false;
		}
		return true;
	}
	
	public static void resetGlobals() {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("UPDATE players SET globals=100");
			prep.execute();
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to add a new clan
			System.out.println("(ERROR) Failed to reset globals >> "+e.toString());
		}
	}
	
	public static void addClan(Clan clan) {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("insert into clans (name,owner,leaders,members) values (\""+clan.getName()+"\", \""+clan.getOwnerName()+"\", \"\", \"\");");
			prep.execute();
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to add a new clan
			System.out.println("(ERROR) Failed to create clan "+clan.getName()+">>"+e.toString());
		}
	}
	
	public static void removeClan(Clan clan) {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("DELETE FROM clans WHERE name='"+clan.getName()+"';");
			prep.execute();
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to add a new clan
			System.out.println("(ERROR) Failed to delete clan "+clan.getName()+">>"+e.toString());
		}
	}
	
	public static boolean updateClan(Clan clan) {
		try {
			String leadersString = Utils.implode(clan.getLeaders());
			String membersString = Utils.implode(clan.getMembers());
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("UPDATE clans SET "+
				"owner=\""+clan.getOwnerName()+"\","+
				"leaders=\""+leadersString+"\","+
				"cloakurl=?,"+
				"members=\""+membersString+"\" "+
				"WHERE name=\""+clan.getName()+"\";");
			prep.setString(1, clan.getCloakURL());
			prep.execute();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to update clan "+clan.getName()+">>"+e.toString());
			return false;
		}
		return true;
	}
	
	public static ArrayList<Clan> getClans() throws Exception{
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM clans;");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			ArrayList<Clan> clans = new ArrayList<Clan>();
			while(rs.next()) {
				String name = rs.getString("name");
				try {
					String ownerName = rs.getString("owner");
					String leaderNamesString = rs.getString("leaders");
					String memberNamesString = rs.getString("members");
					ArrayList<String> leaderNames;
					ArrayList<String> memberNames;
					String cloakURL = rs.getString("cloakURL");
					
					if(leaderNamesString.equalsIgnoreCase("") || leaderNamesString.equalsIgnoreCase(" "))
						leaderNames = new ArrayList<String>();
					else {
						String[] leaderNamesArray = leaderNamesString.split(",");
						leaderNames = new ArrayList<String>(Arrays.asList(leaderNamesArray));
					}
					
					if(memberNamesString.equalsIgnoreCase("") || memberNamesString.equalsIgnoreCase(" "))
						memberNames = new ArrayList<String>();
					else {
						String[] memberNamesArray = memberNamesString.split(",");
						memberNames = new ArrayList<String>(Arrays.asList(memberNamesArray));
					}
					
					Clan clan = new Clan(name, ownerName, leaderNames, memberNames);
					if(cloakURL != null && cloakURL != "")
						clan.setCloakURL(cloakURL);
					clans.add(clan);
					
				}
				catch(Exception e) {
					System.out.println("(CRITICAL ERROR) Exception when generating \""+name+"\" plot: "+e.toString());
					throw new Exception();
				}
			}
			return clans;
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to get the plots
			System.out.println("(CRITICAL ERROR) Failed to get clans: >>"+e.toString());
			throw new Exception();
		}
	}
	
	public static int addScroll(int ownerId, Spell spell) {
		String spellName = spell.getName();
		try {
			Connection conn = _connPool.getConnection();
           // PreparedStatement prep = conn.prepareStatement("insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");");
			Statement stmt = conn.createStatement();
			String sql = "insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			int id = -1;
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			return id;
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to add "+ownerId+"'s "+spellName+">>"+e.toString());
		}
		return -1;
	}
	
	public static int addRune(int ownerId, String ownerName, String label, Location loc) {
		try {
			String locString = loc.getWorld().getName()+","+loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
			Connection conn = _connPool.getConnection();
           // PreparedStatement prep = conn.prepareStatement("insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");");
            String sql = "insert into runes (ownerid,owner,label,location) values ("+ownerId+",'"+ownerName+"','"+label+"','"+locString+"');";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			int id = -1;
			ResultSet rs = prep.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			return id;
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to add "+ownerId+"'s rune "+label+" >>"+e.toString());
		}
		return -1;
	}
	
	public static void updateRune(Player player, PseudoPlayer pseudoPlayer, Rune rune) {
		try {
			Connection conn = _connPool.getConnection();
           // PreparedStatement prep = conn.prepareStatement("insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");");
            String sql = "UPDATE runes SET owner='"+player.getName()+"', ownerid="+pseudoPlayer.getId()+" WHERE id="+rune.getId()+";";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to update "+pseudoPlayer.getId()+"'s rune "+rune.getId()+" >>"+e.toString());
		}
	}
	
	public static void removeRune(Rune rune) {
		try {
			Connection conn = _connPool.getConnection();
           // PreparedStatement prep = conn.prepareStatement("insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");");
            String sql = "DELETE FROM runes WHERE id="+rune.getId()+";";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to remove rune "+rune.getId()+" >>"+e.toString());
		}
	}
	
	public static void removeScroll(Spell spell, int ownerId) {
		String spellName = spell.getName();
		try {
			Connection conn = _connPool.getConnection();
            PreparedStatement prep = conn.prepareStatement("DELETE FROM scrolls WHERE id="+spell.getId()+";");
			prep.execute();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to remove "+ownerId+"'s "+spellName+">>"+e.toString());
		}
	}
	
	public static void updateScrollOwner(Spell spell, int newOwnerId) {
		String spellName = spell.getName();
		try {
			Connection conn = _connPool.getConnection();
            PreparedStatement prep = conn.prepareStatement("UPDATE scrolls SET ownerid="+newOwnerId+" WHERE id="+spell.getId()+";");
			prep.execute();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to trade scroll to "+newOwnerId+" > "+spellName+">>"+e.toString());
		}
	}
	
	public static void dailyMaintenance() {
		try {
			Connection conn = _connPool.getConnection();
			
			PreparedStatement prep = conn.prepareStatement("SELECT name,murdercounts,plotcreatepoints,premiumdays,premium FROM players WHERE murdercounts > 0 OR plotcreatepoints > 0 OR premiumdays > 0;");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while(rs.next()) {
				String name = rs.getString("name");
				try {
					int murderCounts = rs.getInt("murdercounts");
					int premiumDays = rs.getInt("premiumdays");
					int premium = rs.getInt("premium");
					int plotCreatePoints = rs.getInt("plotcreatepoints");
					
					// reduce murder counts by one if there are any at all
					if(murderCounts > 0 && murderCounts < 20) {
						murderCounts -= 1;
					}
					
					// if a player is not an auto bill, but has days remaining
					if((premiumDays != -1) && (premiumDays >= 1)) {
						// remove one day
						premiumDays -= 1;
						
						// if the player runs out of premium days
						if(premiumDays <= 0) {
							// set premium to off
							premium = 0;
						}
					}
					
					// if the player has plot create points, remove one
					if(plotCreatePoints > 0)
						plotCreatePoints--;
					
					// set the proper values in the database
					PreparedStatement modified = conn.prepareStatement("UPDATE players SET "+
							"murdercounts="+murderCounts+
							",premiumdays="+premiumDays+
							",premium="+premium+
							",plotcreatepoints="+plotCreatePoints+
							"  WHERE name='"+name+"';");
					modified.execute();
					
					// Determine if the player is online
					Player player = Utils.getPlugin().getServer().getPlayer(name);
					if(player != null) {
						PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
						pseudoPlayer.setMurderCounts(murderCounts);
						pseudoPlayer.setPremiumDays(premiumDays);
						pseudoPlayer.setPlotCreatePoints(plotCreatePoints);
						if(premium == 0)
							pseudoPlayer.setPremium(false);
						else
							pseudoPlayer.setPremium(true);
					}
				}
				catch(Exception e) {
					System.out.println("Failed Maint on "+name);
					e.printStackTrace();
				}
			}
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to get the pseudoplayer data
			System.out.println("Failed to do daily murder counts.>>"+e.toString());
		}
	}
	
	public static void updateBank(Player player, PseudoPlayer pseudoPlayer) {
		try {
			Connection conn = _connPool.getConnection();
           // PreparedStatement prep = conn.prepareStatement("insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");");
			String sql = "UPDATE players SET bankdata='"+pseudoPlayer._bankBox.getSerialized()+"' WHERE id="+pseudoPlayer.getId()+";";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to update "+pseudoPlayer.getId()+"'s bank "+pseudoPlayer.getBankBox().getSerialized()+" >>"+e.toString());
		}
	}
	
	public static int addBank(Location location, int size) {
		try {
			String locString = location.getWorld().getName()+","+location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
			Connection conn = _connPool.getConnection();
           // PreparedStatement prep = conn.prepareStatement("insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");");
            String sql = "insert into banks (location,size) values ('"+locString+"','"+size+"');";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			int id = -1;
			ResultSet rs = prep.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			return id;
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to add a bank >>"+e.toString());
		}
		return -1;
	}
	
	public static void removeBank(Bank bank) {
		try {
			Connection conn = _connPool.getConnection();
            String sql = "DELETE FROM banks WHERE id="+bank.getId()+";";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to remove bank "+bank.getId()+" >>"+e.toString());
		}
	}
	
	public static ArrayList<Bank> getBanks() {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM banks;");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			ArrayList<Bank> banks = new ArrayList<Bank>();
			while(rs.next()) {
				String locationString = rs.getString("location");
				int id = rs.getInt("id");
				try {
					String[] splitLocationString = locationString.split(",");
					String worldName = splitLocationString[0];
					double locX = Double.parseDouble(splitLocationString[1]);
					double locY = Double.parseDouble(splitLocationString[2]);
					double locZ = Double.parseDouble(splitLocationString[3]);
					World world = Utils.getPlugin().getServer().getWorld(worldName);
					Location location = new Location(world, locX, locY, locZ);
					
					int size = rs.getInt("size");
					
					Bank bank = new Bank(id, location, size);
					banks.add(bank);
					
				}
				catch(Exception e) {
					System.out.println("(CRITICAL ERROR) Exception when generating a bank at: "+locationString+" >>"+e.toString());
				}
			}
			return banks;
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to get the plots
			System.out.println("(CRITICAL ERROR) Failed to get banks: >>"+e.toString());
			return null;
		}
	}
	
	public static void addNPC(HumanNPC npc, String npcName, String job) {
		try {
			Location loc = npc.getBukkitEntity().getLocation();
			String locString = loc.getWorld().getName()+","+loc.getX()+","+loc.getY()+","+loc.getZ()+","+loc.getPitch()+","+loc.getYaw();
			Connection conn = _connPool.getConnection();
           // PreparedStatement prep = conn.prepareStatement("insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");");
            String sql = "insert into npcs (uniqueid,name,location,job,iteminhandid) values ('"+npcName+"','"+npcName+"','"+locString+"','"+job+"',"+1+");";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate(sql);
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to add npc "+npcName+" >>"+e.toString());
		}
	}
	
	public static void removeNPC(String npcName) {
		try {
			Connection conn = _connPool.getConnection();
            String sql = "DELETE FROM npcs WHERE uniqueid='"+npcName+"';";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to remove npc "+npcName+" >>"+e.toString());
		}
	}
	
	public static void updateNPC(HumanNPC npc, String npcName) {
		try {
			Location loc = npc.getBukkitEntity().getLocation();
			String locString = loc.getWorld().getName()+","+loc.getX()+","+loc.getY()+","+loc.getZ()+","+loc.getPitch()+","+loc.getYaw();
			Connection conn = _connPool.getConnection();
           // PreparedStatement prep = conn.prepareStatement("insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");");
            String sql = "UPDATE npcs SET name='"+npcName+"', location='"+locString+"' WHERE uniqueid='"+npcName+"';";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to update NPC: "+npcName+" >>"+e.toString());
		}
	}
	
	public static int addPlotNPC(int npcPlotId, String npcName, Location npcLocation, String npcType, String additional) {
		try {
			String locString = npcLocation.getWorld().getName()+","+npcLocation.getX()+","+npcLocation.getY()+","+npcLocation.getZ()+","+npcLocation.getPitch()+","+npcLocation.getYaw();
			Connection conn = _connPool.getConnection();
            String sql = "insert into plotnpcs (plotid,name,location,type,additional) values ("+npcPlotId+",'"+npcName+"','"+locString+"','"+npcType+"','"+additional+"');";
            PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			int id = -1;
			ResultSet rs = prep.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			return id;
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to add plotnpcf "+npcName+" >>"+e.toString());
		}
		return -1;
	}
	
	public static void removePlotNPC(int id) {
		try {
			Connection conn = _connPool.getConnection();
            String sql = "DELETE FROM plotnpcs WHERE id="+id+";";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to remove plotnpc "+id+" >>"+e.toString());
		}
	}
	
	public static void updatePlotNPC(PlotNPC plotNPC) {
		try {
			Location loc = plotNPC.getLocation();
			String locString = loc.getWorld().getName()+","+loc.getX()+","+loc.getY()+","+loc.getZ()+","+loc.getPitch()+","+loc.getYaw();
			Connection conn = _connPool.getConnection();
           // PreparedStatement prep = conn.prepareStatement("insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");");
            String sql = "UPDATE plotnpcs SET name='"+plotNPC.getName()+"', location='"+locString+"' WHERE id="+plotNPC.getId()+";";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to update plotnpc: "+plotNPC.getName()+" >>"+e.toString());
		}
	}
	
	public static void loadNPCs() {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM npcs;");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while(rs.next()) {
				String uniqueId = rs.getString("uniqueid");
				try {
					String name = rs.getString("name");
					int itemInHandId = rs.getInt("iteminhandid");
					String job = rs.getString("job");
					if(job.equalsIgnoreCase("guard") || job.equalsIgnoreCase("banker") || job.equalsIgnoreCase("vendor")) {}
					else continue;
					String locationString = rs.getString("location");
					String[] splitLocationString = locationString.split(",");
					String worldName = splitLocationString[0];
					double locX = Double.parseDouble(splitLocationString[1]);
					double locY = Double.parseDouble(splitLocationString[2]);
					double locZ = Double.parseDouble(splitLocationString[3]);
					float pitch = Float.parseFloat(splitLocationString[4]);
					float yaw = Float.parseFloat(splitLocationString[5]);
					World world = Utils.getPlugin().getServer().getWorld(worldName);
					Location location = new Location(world, locX, locY, locZ);
					
					location.setPitch(pitch);
					location.setYaw(yaw);
					
					//RPG._permChunks.add(location.getWorld().getChunkAt(location).toString());
			        
					Utils.loadChunkAtLocation(location);
					HumanNPC npcEntity = NPCHandler._npcManager.spawnNPC(name, location, name);
					RPGNPC rpgNpc = new RPGNPC(name, name, itemInHandId, job, location, npcEntity);
					NPCHandler.equipNPC(rpgNpc);
			        RPG._permChunks.add(location.getWorld().getChunkAt(location).toString());

				}
				catch(Exception e) {
					System.out.println("(CRITICAL ERROR) Exception when generating a npc: "+uniqueId+" >>"+e.toString());
					e.printStackTrace();
				}
			}
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to get the plots
			System.out.println("(CRITICAL ERROR) Failed to get NPCs: >>"+e.toString());
		}
	}

	public static void addOfflineMessage(String playerName, String message) {
		try {
			Connection conn = _connPool.getConnection();
            String sql = "insert into offlinemessages (message,playername) values ('"+message+"','"+playerName+"');";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to add "+playerName+"'s message: " +message+" >>"+e.toString());
		}
	}
	
	public static ArrayList<String> getOfflineMessages(String playerName) {
		ArrayList<String> offlineMessages = new ArrayList<String>();
		
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM offlinemessages WHERE playername='"+playerName+"';");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while(rs.next()) {
				String message = rs.getString("message");
				offlineMessages.add(message);
			}
			
			prep = conn.prepareStatement("DELETE FROM offlinemessages WHERE playername='"+playerName+"';");
			prep.execute();
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to get the plots
			System.out.println("(CRITICAL ERROR) Failed to get "+playerName+"'s offline messages. >>"+e.toString());
		}
		
		return offlineMessages;
	}
	
	public static LockedBlock addLockedBlock(int plotid, String locString) {		
		try {
			Connection conn = _connPool.getConnection();
           // PreparedStatement prep = conn.prepareStatement("insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");");
			Statement stmt = conn.createStatement();
			int randKey = (int)(Math.random()*99999999);
			String sql = "insert into lockedblocks (plotid,location,bkey) values ("+plotid+",\""+locString+"\","+randKey+");";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			int id = -1;
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			LockedBlock lockedBlock = new LockedBlock(id, locString, randKey);
			return lockedBlock;
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to add lockedBlock at "+locString+" >>"+e.toString());
		}
		return null;
	}
	
	public static void removeLockedBlock(LockedBlock lockedBlock) {
		try {
			Connection conn = _connPool.getConnection();
           // PreparedStatement prep = conn.prepareStatement("insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");");
			Statement stmt = conn.createStatement();

			String sql = "DELETE FROM lockedblocks WHERE id="+lockedBlock.getId()+";";
			stmt.executeUpdate(sql);
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to remove lockedBlock at "+lockedBlock.getLocString()+" >>"+e.toString());
		}
	}
	
	public static int addStore(Store store) {
		try {
			int size = store.getSize();
			Location loc = store.getLocation();
			String locString = loc.getWorld().getName()+","+loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
			Connection conn = _connPool.getConnection();
           // PreparedStatement prep = conn.prepareStatement("insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");");
            String sql = "insert into stores (name,size,location,storedata,type) values (?,'"+size+"','"+locString+"','',"+store.getType()+");";
            PreparedStatement prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, store.getName());
			prep.executeUpdate();
			int id = -1;
			ResultSet rs = prep.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			return id;
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to add a store >>"+e.toString());
		}
		return -1;
	}
	
	public static void removeStore(Store store) {
		try {
			Connection conn = _connPool.getConnection();
            String sql = "DELETE FROM stores WHERE id="+store.getId()+";";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to remove store "+store.getId()+" >>"+e.toString());
		}
	}
	
	public static void removeStore(int storeId) {
		try {
			Connection conn = _connPool.getConnection();
            String sql = "DELETE FROM stores WHERE id="+storeId+";";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to remove store "+storeId+" >>"+e.toString());
		}
	}
	
	public static ArrayList<Store> getStores() {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM stores;");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			ArrayList<Store> stores = new ArrayList<Store>();
			while(rs.next()) {
				int id = rs.getInt("id");
				String storeName = rs.getString("name");
				int size = rs.getInt("size");
				String locationString = rs.getString("location");
				String storeData = rs.getString("storedata");
				int type = rs.getInt("type");
				try {
					String[] splitLocationString = locationString.split(",");
					String worldName = splitLocationString[0];
					double locX = Double.parseDouble(splitLocationString[1]);
					double locY = Double.parseDouble(splitLocationString[2]);
					double locZ = Double.parseDouble(splitLocationString[3]);
					World world = Utils.getPlugin().getServer().getWorld(worldName);
					Location location = new Location(world, locX, locY, locZ);
					
					ArrayList<ItemStack> items = new ArrayList<ItemStack>();
					ArrayList<Integer> stocks = new ArrayList<Integer>();
					ArrayList<Integer> prices = new ArrayList<Integer>();
					ArrayList<Integer> restockRates = new ArrayList<Integer>();
					
					if(storeData.trim() != "") {
						String[] splitStoreData = storeData.split(",");
						for(String singleStoreData : splitStoreData) {
							String[] splitSingleStoreData = singleStoreData.split("-");
							if(type == 1)
								splitSingleStoreData = singleStoreData.split("#");
							int itemId;
							int stock;
							int price;
							int restockRate = 0;
							try {
								itemId = Integer.parseInt(splitSingleStoreData[0]);
								stock = Integer.parseInt(splitSingleStoreData[1]);
								price = Integer.parseInt(splitSingleStoreData[2]);
								if(type == 0) {
									restockRate = Integer.parseInt(splitSingleStoreData[3]);
									items.add(new ItemStack(itemId, 1));
									stocks.add(stock);
									prices.add(price);
									restockRates.add(restockRate);
								}
								else if(type == 1) {
									if(stock <= 0)
										continue;
									ItemStack item = new ItemStack(itemId, 1);
									item.setDurability((short)Integer.parseInt(splitSingleStoreData[3]));
									items.add(item);
									stocks.add(stock);
									prices.add(price);
									restockRates.add(0);
								}
							}
							catch(Exception e) {}
						}
					}
					
					Store store = new Store(id,storeName,size,location,items,stocks,prices,restockRates);
					store.setType(type);
					stores.add(store);
					
				}
				catch(Exception e) {
					System.out.println("(CRITICAL ERROR) Exception when generating a store at: "+locationString+" >>"+e.toString());
				}
			}
			return stores;
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to get the plots
			System.out.println("(CRITICAL ERROR) Failed to get banks: >>"+e.toString());
			return null;
		}
	}

	public static void updateStore(Store store) {
		try {
			Connection conn = _connPool.getConnection();
			String storeLocationString = store.getLocation().getWorld().getName()+","+store.getLocation().getX()+","+store.getLocation().getY()+","+store.getLocation().getZ();
			PreparedStatement prep = conn.prepareStatement("UPDATE stores SET location='"+storeLocationString+"', storedata='"+store.getSerializedStoreData()+"' WHERE id='"+store.getId()+"';");
			prep.execute();
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to update money
			System.out.println(e.toString());
		}
	}
	
	public static int addPermanentGate(PermanentGate gate) {
		try {
			Block srcBlock = gate.getSourceBlock();
			Block dstBlock = gate.getDestBlock();
			
			String srcLocString = srcBlock.getWorld().getName()+","+srcBlock.getX() + "," + srcBlock.getY() + "," + srcBlock.getZ();
			String dstLocString = dstBlock.getWorld().getName()+","+dstBlock.getX() + "," + dstBlock.getY() + "," + dstBlock.getZ();
			Connection conn = _connPool.getConnection();
           // PreparedStatement prep = conn.prepareStatement("insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");");
            String sql = "insert into permanentgates (entranceloc,exitloc) values ('"+srcLocString+"','"+dstLocString+"');";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			int id = -1;
			ResultSet rs = prep.getGeneratedKeys();
			if (rs.next()){
			    id=rs.getInt(1);
			}
			return id;
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to add a permgate >>"+e.toString());
		}
		return -1;
	}
	
	public static void removePermanentGate(int id) {
		try {
			Connection conn = _connPool.getConnection();
            String sql = "DELETE FROM permanentgates WHERE id="+id+";";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to remove bank "+id+" >>"+e.toString());
		}
	}
	
	public static void loadPermanentGates() {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM permanentgates;");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			int numLoaded = 0;
			while(rs.next()) {
				try {
					int id = rs.getInt("id");
					String srcLocString = rs.getString("entranceloc");
					String dstLocString = rs.getString("exitloc");
					String[] splitSrcLocString = srcLocString.split(",");
					String[] splitDstLocString = dstLocString.split(",");
					World srcWorld = Utils.getPlugin().getServer().getWorld(splitSrcLocString[0]);
					World dstWorld = Utils.getPlugin().getServer().getWorld(splitDstLocString[0]);
					IntPoint srcIntPoint = new IntPoint(Integer.parseInt(splitSrcLocString[1]), Integer.parseInt(splitSrcLocString[2]), Integer.parseInt(splitSrcLocString[3]));
					IntPoint dstIntPoint = new IntPoint(Integer.parseInt(splitDstLocString[1]), Integer.parseInt(splitDstLocString[2]), Integer.parseInt(splitDstLocString[3]));
					Block srcBlock = srcWorld.getBlockAt(srcIntPoint.x, srcIntPoint.y, srcIntPoint.z);
					Block dstBlock = dstWorld.getBlockAt(dstIntPoint.x, dstIntPoint.y, dstIntPoint.z);
					//Utils.loadChunkAtLocation(srcBlock.getLocation());
					//Utils.loadChunkAtLocation(dstBlock.getLocation());
					ArrayList<Block> blocks = new ArrayList<Block>();
					blocks.add(srcBlock);
					blocks.add(dstBlock);
					blocks.add(srcBlock.getWorld().getBlockAt(srcBlock.getX(), srcBlock.getY()+1, srcBlock.getZ()));
					blocks.add(dstBlock.getWorld().getBlockAt(dstBlock.getX(), dstBlock.getY()+1, dstBlock.getZ()));
					
					/*boolean failed = false;
					for(Block b : blocks) {
						if(!b.getType().equals(Material.PORTAL)) {
							removePermanentGate(id);
							failed = true;
							break;
						}
					}
					
					if(!failed) {*/
						PermanentGate gate = new PermanentGate(blocks, "dangles");
						gate.setId(id);
						Magery.addMagicStructure(gate);
					//}
					numLoaded++;
				}
				catch(Exception e) {
					System.out.println("(CRITICAL ERROR) Exception when generating a permgate: >>"+e.toString());
				}
			}
			System.out.println("Loaded "+(numLoaded+1)+" Permanent Gates");
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to get the plots
			System.out.println("(CRITICAL ERROR) Failed to get permgates: >>"+e.toString());
		}
	}
	
	public static void updateSkillLocked(Player player, PseudoPlayer pseudoPlayer, String skillName, boolean locked) {
		try {
			Connection conn = _connPool.getConnection();
			int lockedInt = 0;
			if(locked)
				lockedInt = 1;
			String sql;
			if(pseudoPlayer.getBuildNumber() == 0)
				sql = "UPDATE players SET "+skillName.toLowerCase()+"Lock="+lockedInt+" WHERE name='"+player.getName()+"';";
			else
				sql = "UPDATE builds SET "+skillName.toLowerCase()+"Lock="+lockedInt+" WHERE name='"+player.getName()+"' AND buildnum="+pseudoPlayer.getBuildNumber()+";";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.execute();
		}
		catch(Exception e) {
			System.out.println("Failed to lock skill "+skillName+" >> "+e.toString());
		}
	}
	
	public static void setVampirism(Player player, boolean isVamp) {
		try {
			Connection conn = _connPool.getConnection();
			int vampInt = 0;
			if(isVamp)
				vampInt = 1;
			String sql = "UPDATE players SET vampirism="+vampInt+" WHERE name='"+player.getName()+"';";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.execute();
		}
		catch(Exception e) {
			System.out.println("Failed to set vampirism on "+player.getName()+" >> "+e.toString());
		}
	}
	
	public static void setRobot(Player player, boolean isRobot) {
		try {
			Connection conn = _connPool.getConnection();
			int roboInt = 0;
			if(isRobot)
				roboInt = 1;
			String sql = "UPDATE players SET robot="+roboInt+" WHERE name='"+player.getName()+"';";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.execute();
		}
		catch(Exception e) {
			System.out.println("Failed to set robot on "+player.getName()+" >> "+e.toString());
		}
	}
	
	private static HashSet<Integer> prohibitedItems = new HashSet<Integer>();
	public static void cleanBanks() {
		prohibitedItems.clear();
		prohibitedItems.add(7); //bedrock
		prohibitedItems.add(14); //gold ore
		prohibitedItems.add(15); //iron ore
		prohibitedItems.add(41); //gold block
		prohibitedItems.add(42); //iron block
		prohibitedItems.add(46); //tnt
		prohibitedItems.add(52); //spawner
		prohibitedItems.add(56); //diamond ore
		prohibitedItems.add(57); //diamond block
		prohibitedItems.add(256); //iron shovel
		prohibitedItems.add(257); //iron pick
		prohibitedItems.add(258); //iron axe
		prohibitedItems.add(261); //bow
		prohibitedItems.add(262); //arrows
		prohibitedItems.add(264); //diamond
		prohibitedItems.add(265); //iron
		prohibitedItems.add(266); //gold
		prohibitedItems.add(267); //iron sword
		prohibitedItems.add(276); //diamond sword
		prohibitedItems.add(277); //diamond shovel
		prohibitedItems.add(278); //diamond pick
		prohibitedItems.add(279); //diamond axe
		prohibitedItems.add(283); //gold sword
		prohibitedItems.add(284); //gold shovel
		prohibitedItems.add(285); //gold pick
		prohibitedItems.add(286); //gold axe
		prohibitedItems.add(287); //string
		prohibitedItems.add(288); //feather
		prohibitedItems.add(289); //gunpowder
		prohibitedItems.add(306); //iron helmet
		prohibitedItems.add(307); //iron chestplate
		prohibitedItems.add(308); //iron leggings
		prohibitedItems.add(309); //iron boots
		prohibitedItems.add(310); //diamond helmet
		prohibitedItems.add(311); //diamond chestplate
		prohibitedItems.add(312); //diamond leggings
		prohibitedItems.add(313); //diamond boots
		prohibitedItems.add(314); //gold helmet
		prohibitedItems.add(315); //gold chestplate
		prohibitedItems.add(316); //gold leggings
		prohibitedItems.add(317); //gold boots
		prohibitedItems.add(322); //golden apple
		prohibitedItems.add(325); //bucket
		prohibitedItems.add(326); //water bucket
		prohibitedItems.add(327); //lava bucket
		prohibitedItems.add(331); //redstone
		prohibitedItems.add(335); //milk bucket
		prohibitedItems.add(338); //sugar cane
		prohibitedItems.add(73); //redstone ore
		prohibitedItems.add(74); //glowing redstone ore
		prohibitedItems.add(74); //glowing redstone ore
		prohibitedItems.add(295); //seeds
		prohibitedItems.add(292); //iron hoe
		prohibitedItems.add(293); //diamond hoe
		prohibitedItems.add(294); //golden hoe
		
		try {
			Connection conn = _connPool.getConnection();
			Connection conn2 = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT id,bankdata FROM players;");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while(rs.next()) {
				int id = rs.getInt("id");
				String bankData = rs.getString("bankdata");
				String[] splitSerializedString = bankData.split(",");
				String serializedString = "";
				for(String itemData : splitSerializedString) {
					if(itemData.equalsIgnoreCase("") || itemData.equalsIgnoreCase(" "))
						continue;
					try {
						String[] splitData = itemData.split("-");
						int slot = Integer.parseInt(splitData[0]);
						int itemId = Integer.parseInt(splitData[1]);
						int count = Integer.parseInt(splitData[2]);
						int damage = Integer.parseInt(splitData[3]);
						if(!prohibitedItems.contains(itemId)) {
							if(serializedString.length() != 0) {
								serializedString += ",";
							}
							serializedString += (slot+"-"+itemId+"-"+count+"-"+damage);
						}
						
					}
					catch(Exception e) {
						System.out.println("Failed to read item: " + itemData+" >>"+e.toString());
					}
				}
				
				conn2 = _connPool.getConnection();
				PreparedStatement prep2 = conn2.prepareStatement("UPDATE players SET bankdata='"+serializedString+"' WHERE id="+id+";");
				prep2.execute();
				
			}
		}
		catch(Exception e) {
		}
	}
	
	public static int addRandChest(RandChest randChest) {
		try {
			Location loc = randChest.getLocation();
			String locString = loc.getWorld().getName()+","+loc.getX()+","+loc.getY()+","+loc.getZ();
			long nextFill = randChest.getNextFill();
			int minRand = randChest.getMinRand();
			int maxRand = randChest.getMaxRand();
			ArrayList<ItemStack[]> allContents = randChest.getContents();
			int numAllContents = allContents.size();
			String contentsString = "";
			for(int i=0; i<numAllContents; i++) {
				ItemStack[] contents = allContents.get(i);
				int numContents = contents.length;
				ArrayList<ItemStack> validItems = new ArrayList<ItemStack>();
				for(int j=0; j<numContents; j++) {
					ItemStack c = contents[j];
					if(c==null)
						continue;
					//if(c.getType().equals(Material.AIR))
						//continue;
					validItems.add(c);
				}
				
				int numValidItems = validItems.size();
				for(int j=0; j<numValidItems; j++) {
					ItemStack c = validItems.get(j);
					int amount = c.getAmount();
					if(amount <= 0)
						amount = 1;
					contentsString += (c.getTypeId()+","+amount+","+c.getDurability());
					if(j < numValidItems-1)
						contentsString += "@";
				}
				
				if(i < numAllContents-1) {
					contentsString += "#";
				}
				
			}
			
			Output.sendToAdminIRC(null, "DC_Debug", "Created Dungeon Chest, contents: "+contentsString);
			
			Connection conn = _connPool.getConnection();
           // PreparedStatement prep = conn.prepareStatement("insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");");
            String sql = "insert into autofillchests (location,contents,nextfill,minseconds,maxseconds) values ('"+locString+"','"+contentsString+"',"+nextFill+","+minRand+","+maxRand+");";		
			PreparedStatement prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			prep.executeUpdate();
			int id = -1;
			ResultSet rs = prep.getGeneratedKeys();
			if (rs.next()){
				id=rs.getInt(1);
			}
			return id;
			
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to add chest at "+randChest.getLocation()+" >>"+e.toString());
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void removeRandChest(RandChest randChest) {
		try {
			Connection conn = _connPool.getConnection();
            String sql = "DELETE FROM autofillchests WHERE id="+randChest.getId()+";";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to remove chest "+randChest.getId()+" >>"+e.toString());
		}
	}
	
	public static void updateRandChestFillTime(RandChest randChest) {
		try {
			Connection conn = _connPool.getConnection();
	            String sql = "UPDATE autofillchests SET nextfill="+randChest.getNextFill()+" WHERE id="+randChest.getId()+";";
				PreparedStatement prep = conn.prepareStatement(sql);
				prep.executeUpdate();
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public static void updateRandChest(RandChest randChest) {
		try {
			Location loc = randChest.getLocation();
			String locString = loc.getWorld().getName()+","+loc.getX()+","+loc.getY()+","+loc.getZ();
			
			ArrayList<ItemStack[]> allContents = randChest.getContents();
			int numAllContents = allContents.size();
			
			String contentsString = "";
			for(int i=0; i<numAllContents; i++) {
				ItemStack[] contents = allContents.get(i);
				int numContents = contents.length;
				ArrayList<ItemStack> validItems = new ArrayList<ItemStack>();
				for(int j=0; j<numContents; j++) {
					ItemStack c = contents[j];
					if(c==null)
						continue;
					//if(c.getType().equals(Material.AIR))
						//continue;
					validItems.add(c);
				}
				
				int numValidItems = validItems.size();
				for(int j=0; j<numValidItems; j++) {
					ItemStack c = validItems.get(j);
					int amount = c.getAmount();
					if(amount <= 0)
						amount = 1;
					contentsString += (c.getTypeId()+","+amount+","+c.getDurability());
					if(j < numValidItems-1)
						contentsString += "@";
				}
				
				if(i < numAllContents-1) {
					contentsString += "#";
				}
				
			}
			
			Output.sendToAdminIRC(null, "DC_Debug", "Updated Dungeon Chest, contents: "+contentsString);
			
			Connection conn = _connPool.getConnection();
           // PreparedStatement prep = conn.prepareStatement("insert into scrolls (ownerid,name) values ("+ownerId+",\""+spellName+"\");");
            String sql = "UPDATE autofillchests SET "+
            	"location='"+locString+"',"+
            	"contents='"+contentsString+"',"+
            	"nextfill="+randChest.getNextFill()+","+
            	"minseconds="+randChest.getMinRand()+","+
            	"maxseconds="+randChest.getMaxRand()+" "+
            	"WHERE id="+randChest.getId()+";";
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.executeUpdate();
		}
		catch(Exception e) {
			// some exception was thrown when attempting to update the player
			System.out.println("(ERROR) Failed to chest: "+randChest.getId()+" >>"+e.toString());
		}
	}
	
	public static ArrayList<RandChest> getRandChests() {
		try {
			Connection conn = _connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM autofillchests;");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			ArrayList<RandChest> randChests = new ArrayList<RandChest>();
			while(rs.next()) {
				String locationString = rs.getString("location");
				int id = rs.getInt("id");
				try {
					String[] splitLocationString = locationString.split(",");
					String worldName = splitLocationString[0];
					double locX = Double.parseDouble(splitLocationString[1]);
					double locY = Double.parseDouble(splitLocationString[2]);
					double locZ = Double.parseDouble(splitLocationString[3]);
					World world = Utils.getPlugin().getServer().getWorld(worldName);
					Location location = new Location(world, locX, locY, locZ);
					
					ArrayList<ItemStack[]> contents = new ArrayList<ItemStack[]>();
					String allContentsString = rs.getString("contents");
					allContentsString = allContentsString.trim();
					if(allContentsString.equalsIgnoreCase("")) {
					}
					else {
						String[] splitAllContents = allContentsString.split("#");
						int numAllContents = splitAllContents.length;
						for(int i=0; i<numAllContents; i++) {
							String[] splitContents = splitAllContents[i].split("@");
							int numContents = splitContents.length;
							ItemStack[] items = new ItemStack[numContents];
							for(int j=0; j<numContents; j++) {
								String[] splitProperties = splitContents[j].split(",");
								int typeId = Integer.parseInt(splitProperties[0]);
								int amount = Integer.parseInt(splitProperties[1]);
								int durability = Integer.parseInt(splitProperties[2]);
								ItemStack itemStack = new ItemStack(typeId, amount);
								itemStack.setDurability((short)durability);
								items[j] = itemStack;
							}
							contents.add(items);
						}
					}
					
					long nextFill = rs.getLong("nextfill");
					int minRand = rs.getInt("minseconds");
					int maxRand = rs.getInt("maxseconds");
					
					RandChest randChest = new RandChest(id, location, contents, nextFill, minRand, maxRand);
					randChests.add(randChest);
					
				}
				catch(Exception e) {
					System.out.println("(CRITICAL ERROR) Exception when generating a randchest ("+id+") at: "+locationString+" >>"+e.toString());
					e.printStackTrace();
				}
			}
			return randChests;
		}
		catch(Exception e) {
			// Some exception was thrown when attempting to get the plots
			System.out.println("(CRITICAL ERROR) Failed to get RandChest: >>"+e.toString());
			return null;
		}
	}
	
	public static void updateLastLogoutLocation(PseudoPlayer pseudoPlayer) {
		try {
			Connection conn = _connPool.getConnection();      
			PreparedStatement prep = conn.prepareStatement("UPDATE players SET lastlogoutlocation=? WHERE name=?");
			Location loc = pseudoPlayer.getLastLogoutLocation();
			String lastLogoutLocationString = loc.getWorld().getName()+","+loc.getX()+","+loc.getY()+","+loc.getZ();
			prep.setString(1, lastLogoutLocationString);
			prep.setString(2, pseudoPlayer.getName());				
			prep.execute();
        }
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}