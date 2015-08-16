package com.lostshard.Lostshard.Handlers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class HelpHandler {

	public static void handle(CommandSender sender, String[] split) {
		if (split.length < 1) {
			if (sender instanceof Player) {
				final ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
				final BookMeta bookMeta = (BookMeta) book.getItemMeta();
				bookMeta.setAuthor(ChatColor.GOLD + "Lostshard guide");
				bookMeta.setDisplayName(ChatColor.GOLD + "Lostahrd guide");
				bookMeta.setTitle(ChatColor.GOLD + "Lostahrd guide");

				// Add pages
				bookMeta.addPage(ChatColor.GOLD + "" + ChatColor.BOLD
						+ "Lostshard guide\n\n\n" + ChatColor.GOLD + "- "
						+ ChatColor.DARK_PURPLE + "Rules\n"
						+ ChatColor.GOLD + "- " + ChatColor.DARK_PURPLE
						+ "Banking & Currency\n"+ ChatColor.GOLD + "- " + ChatColor.DARK_PURPLE
						+ "Land Ownership\n"+ ChatColor.GOLD + "- " + ChatColor.DARK_PURPLE
						+ "Skills\n"+ ChatColor.GOLD + "- " + ChatColor.DARK_PURPLE
						+ "Magic\n"+ ChatColor.GOLD + "- " + ChatColor.DARK_PURPLE
						+ "Karma\n"+ ChatColor.GOLD + "- " + ChatColor.DARK_PURPLE
						+ "Chat\n"+ ChatColor.GOLD + "- " + ChatColor.DARK_PURPLE
						+ "Groups\n"+ ChatColor.GOLD + "- " + ChatColor.DARK_PURPLE
						+ "Stuff\n");

				bookMeta.addPage(ChatColor.GOLD + "" + ChatColor.BOLD 
				        + "Rules\n\n\n" + ChatColor.DARK_PURPLE + "> Do not Exploit \n" + ChatColor.DARK_PURPLE
				        + "> Do not use other third party programs or mods that give you an unfair advantage\n" 
				        + ChatColor.DARK_PURPLE + "   Ex.\n"
				        + ChatColor.DARK_PURPLE + "   - Flying\n"  + ChatColor.DARK_PURPLE + "   - X-Ray\n"  
				        + ChatColor.DARK_PURPLE + "   - Force Field, Auto Eat and other Pvp Mods\n\n"  
				        + ChatColor.DARK_PURPLE + "   - If you have any questions do not be afraid to ask an admin\n");

				bookMeta.addPage(ChatColor.GOLD + "" + ChatColor.BOLD 
				        + "Banking & Currency\n\n\n" + ChatColor.DARK_PURPLE  
				        + "1 Gold Ingot = 100 Gold Coins\n" + ChatColor.DARK_PURPLE  
				        + "you can trade gold ingots at any bank using the /tradegold (Amount) Command\n" 
				        + ChatColor.DARK_PURPLE 
				        + " Along with that you can also access your bank, a secure storage container using the command /bank \n"); 

				bookMeta.addPage(ChatColor.GOLD + "" + ChatColor.BOLD 
				        + "Land Owndership\n\n\n" + ChatColor.DARK_PURPLE + "Plots of land can be purchased using the command /plot create (Plot Name) \n" + ChatColor.DARK_PURPLE + "Your first plot will cost you 1000 gold Coins and 1 diamond\n" + ChatColor.DARK_PURPLE + "You can use the command /plot survey to see what other players plots are near you. this will help you pick a good location\n" + ChatColor.DARK_PURPLE + "Plots also have taxes. These are taken daily from your plots gold coin balance. The taax is based on the radius of your plot multiplied by 5 gold coins.\n\n" + ChatColor.GOLD 
				                 + "    Plot Radius = 10 \n" 
				                 + "    10 x 5 gold coins = 50 gold coins \n\n"
				                + ChatColor.DARK_PURPLE + "In order to change the gold coin balance of your plot you can use the commands /plot desposit (Amount) & /plot withdraw (amount) \n"
				                + ChatColor.DARK_PURPLE + "Plots also have upgrades including the dungeon upgrade (/plot upgrade dungeon) which costs 10,000 gold coins and allows monsters to spawn in the plot and the town upgrade (/plot upgrade town) which costs 100,000 gold coins and allows players to set their spawns in beds in the plot \n");

				bookMeta.addPage(ChatColor.GOLD + "" 
				                 + ChatColor.BOLD 
				        + "Skills\n\n\n" + ChatColor.DARK_PURPLE 
				                 + "Lost Shard has 9 total skills; Blades, Brawling, Magery, Mining, Survivalism, Archery, Blacksmithing, Taming, Lumberjacking \n" + ChatColor.DARK_PURPLE + "Each skill can be raised up to level 100, with a maximum of 400 levels per build.\n" 
				                 + ChatColor.DARK_PURPLE + "Ex. 100 Blades\n" 
				                 + ChatColor.DARK_PURPLE + "    100 Magic\n"   
				                 + ChatColor.DARK_PURPLE + "    100 Survivalism\n" 
				                 + ChatColor.DARK_PURPLE + "    100 Arhery\n\n"
				                 + ChatColor.DARK_PURPLE + "You can check your skills with the command /skills\n"
				                + ChatColor.DARK_PURPLE + "You can level one skills up to level 50 and delete other skills with the command /resetallskills (skill name) \n"
				                + ChatColor.DARK_PURPLE + "Ex   Skills - 10 Blades, 4 Brawling 19 Archery\n"
				                + ChatColor.DARK_PURPLE + "     '/resetallskills Magery' \n" 
				                + ChatColor.DARK_PURPLE + "     Skills - 50 Magery \n\n" 
				                + ChatColor.GOLD + "Blades\n" + ChatColor.DARK_PURPLE + "The higher this skill is the more damage you will do with a sword and the more likely it is you can cause an opponent to bleed\n"
				                + ChatColor.DARK_PURPLE + "Bleeding causes small amounts of damage to incure periodically after being initially attacked \n"
				                + ChatColor.GOLD + "Brawling\n" + ChatColor.DARK_PURPLE + "The higher this skill is the more damage you do with your fist, you also have a better chance at stunning an opponent\n"
				                + ChatColor.DARK_PURPLE + "Being stunned makes magic, attacking and eating unavailable for a brief period of time \n"
				                + ChatColor.GOLD + "Mining\n" + ChatColor.DARK_PURPLE + "The higher this skill is the more likely it is to get random drops from mining smooth stone and other natural rock types\n"
				                + ChatColor.GOLD + "Survivalism\n" + ChatColor.DARK_PURPLE + "This skill allows you to track animals and at higher levels players\n"
				                + ChatColor.GOLD + "Archery\n" + ChatColor.DARK_PURPLE + "The higher this skill is the more damage you do with a Bow and Arrow\n"
				                + ChatColor.GOLD + "BlackSmithing\n" + ChatColor.DARK_PURPLE + "This skill allows you to repair and enhance items and armor using raw materials\n"
				                + ChatColor.DARK_PURPLE + "Ex   Repair tools and armor with the command /repair \n"
				                + ChatColor.DARK_PURPLE + "     Enchant tools and armor with the command /enhance \n"
				                + ChatColor.DARK_PURPLE + "     Cobblestone repairs/enhances Chain armor and stone tools\n"
				                + ChatColor.DARK_PURPLE + "     Iron ingots repairs/enhances Iron armor and tools\n"
				                + ChatColor.DARK_PURPLE + "     Gold ingots repairs/enhances Gold armor and tools\n"
				                + ChatColor.DARK_PURPLE + "     Diamonds repairs/enhances Diamond armor and tools\n"
				                + ChatColor.GOLD + "Taming \n" + ChatColor.DARK_PURPLE + "This skill allows you to tame more wolves the higher it is\n"
				                + ChatColor.GOLD + "Lumberjacking \n" + ChatColor.DARK_PURPLE + "the higher this skill is the more damage you do with an axe and the more planks fall when chopping down a tree\n"
				                );

				bookMeta.addPage(ChatColor.GOLD + "" + ChatColor.BOLD
				        + "Magic\n\n\n" + ChatColor.DARK_PURPLE + "When you kill a monster they may randomly drop a scroll\n"
				                 + ChatColor.DARK_PURPLE + "These scrolls can be used to cast spells using the command /scrolls use (scroll name) \n"
				                 + ChatColor.DARK_PURPLE + "Alternatively, you can put them in your spellbook by typing /scrolls and clicking on the available scrolls\n"
				                 + ChatColor.DARK_PURPLE + "doing this allows you to cast spells with the command /cast (spell name) or binding the spell to a stick using the command /bind (spell name)\n"
				                 + ChatColor.DARK_PURPLE + "Casting a spell will use reagents such as feathers, string or redstone\n"
				                 + ChatColor.DARK_PURPLE + "there are 8 pages of scrolls, the larger the page number the higher your magic skills must be to cast the spell reliably \n"
				                 + ChatColor.DARK_PURPLE + "you can determine what level and reagents spells require with the command /spellbook page (numbers 1-8)\n"
				                
				                
				                
				                );

				bookMeta.addPage(ChatColor.GOLD + "" + ChatColor.BOLD 
				                + "Karma\n\n\n" + ChatColor.DARK_PURPLE + "Lostshard has a karma system that keeps track of your violent actions." 
				                + ChatColor.DARK_PURPLE + "This means if you attack or kill a non-criminal player you be marked as a criminal\n"
				                + ChatColor.DARK_PURPLE + "Criminal status lasts for 5 minutes\n"
				                + ChatColor.DARK_PURPLE + "if you kill 5 or more players you will be marked as a murder \n"
				                + ChatColor.DARK_PURPLE + "murder counts decay at a rate of 1 per IRL day \n"
				                + ChatColor.DARK_PURPLE + "If you have 20 or more murder counts you will become a murder permanently\n"
				                + ChatColor.DARK_PURPLE + "the only way to eliminate these murder counts is through the shrine of atonement\n\n"
				                + ChatColor.DARK_PURPLE + "You can see the moral standing of any player with the command /whois (Player) \n");
				    
				bookMeta.addPage(ChatColor.GOLD + "" + ChatColor.BOLD 
				        + "Chat\n\n\n" + ChatColor.DARK_PURPLE + "Theres are different forms of communication on lost shard\n"
				                + ChatColor.DARK_PURPLE + "You may select at any time your perferred method of the following forms with their connected command;\n"
				                + ChatColor.DARK_PURPLE + "Global - /g (message) \n"
				                + ChatColor.DARK_PURPLE + "Shout - /s (message) \n"
				                + ChatColor.DARK_PURPLE + "Local - /l (message) \n"
				                + ChatColor.DARK_PURPLE + "Whisper - /w (message) \n"
				                + ChatColor.DARK_PURPLE + "Clan Chat - /c (message) \n"
				                + ChatColor.DARK_PURPLE + "Party Chat - /p (message) \n\n"
				                
				                 + ChatColor.DARK_PURPLE + "You can also personally message players with the command /msg (player) (message) and reply to them with the command /r (message) \n"
				                );
				    
				bookMeta.addPage(ChatColor.GOLD + "" + ChatColor.BOLD 
				        + "Groups\n\n\n" + ChatColor.DARK_PURPLE + "You can create two types of groups on LostShard; a party and a clan"
				                + ChatColor.DARK_PURPLE + "These groups allow you to communicate with each other privately\n"
				                + ChatColor.DARK_PURPLE + "When in a group you cannot damage other group members\n"
				                + ChatColor.DARK_PURPLE + "A party can be created by inviting players with the command /party invite (player) \n\n"
				                + ChatColor.DARK_PURPLE + "To create a clan you must use the command /clan create (Clan Name) \n"
				                + ChatColor.DARK_PURPLE + "This will cost the Clan Owner 10,000 gold coins \n"
				                + ChatColor.DARK_PURPLE + "Clans are a more permanent version of a group \n"
				                + ChatColor.DARK_PURPLE + "Players can be invited to a clan by the owner and leaders and promoted to a leader by the owner"
				                + ChatColor.DARK_PURPLE + "Players are invited and promoted in the clan with the commands /clan invite (player) and /clan promote (player), respectively \n"
				                
				                
				                );
				    
				bookMeta.addPage(ChatColor.GOLD + "" + ChatColor.BOLD 
				                 + "Stuff\n\n\n" + ChatColor.DARK_PURPLE + "Along with those previously outlined, LostShard also has many other unique features...\n\n"
				                 + ChatColor.DARK_PURPLE + "Lapis Lazuli blocks stop you from using magic near them\n"
				                 + ChatColor.DARK_PURPLE + "Gold swords no more damage then diamond\n"
				                 + ChatColor.DARK_PURPLE + "Gold boots eliminate fall damage\n" 
				                 + ChatColor.DARK_PURPLE + "Gold Helmets allow you to breath under water\n"
				                 + ChatColor.DARK_PURPLE + "Gold Leggings eliminate fire/lava damage\n"
				                 + ChatColor.DARK_PURPLE + "Gold Chestplates eliminate blast damage\n"
				                 + ChatColor.DARK_PURPLE + "Other Miscalaneous commands include /kill, /help, /rules and /spawn(returns you to spawn and is only usable once per house) \n");
				                 
				                 
				                 
				                 
				book.setItemMeta(bookMeta);
				final Player player = (Player) sender;
				player.getInventory().addItem(book);
				return;
			}
			sender.sendMessage(ChatColor.GOLD + "-Help-");
			sender.sendMessage(ChatColor.GOLD
					+ "For more detailed information you can view the guide at");
			sender.sendMessage(ChatColor.GOLD + "http://www.lostshard.com.");
			sender.sendMessage(ChatColor.GOLD
					+ "Use \"/help (topic)\" to get more information.");
			sender.sendMessage(ChatColor.YELLOW + "Topics:" + ChatColor.GRAY
					+ " chat, land, money, scrolls, clan, party, karma, misc");
		} else {
			final String topic = split[0];

			if (topic.equalsIgnoreCase("chat"))
				helpChat(sender);
			else if (topic.equalsIgnoreCase("land")
					|| topic.equalsIgnoreCase("plot")
					|| topic.equalsIgnoreCase("plots"))
				helpLandOwnership(sender, split);
			else if (topic.equalsIgnoreCase("money"))
				helpMoney(sender);
			else if (topic.equalsIgnoreCase("skills"))
				helpSkills(sender);
			else if (topic.equalsIgnoreCase("scrolls"))
				helpScrolls(sender);
			else if (topic.equalsIgnoreCase("clan"))
				helpClan(sender, split);
			else if (topic.equalsIgnoreCase("party"))
				helpParty(sender);
			else if (topic.equalsIgnoreCase("karma"))
				helpKarma(sender);
			else if (topic.equalsIgnoreCase("misc"))
				helpMisc(sender);
		}
	}

	public static void helpChat(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "-Chat Help-");
		sender.sendMessage(ChatColor.YELLOW + "Info:" + ChatColor.GRAY
				+ " Default chat is global.");
		sender.sendMessage(ChatColor.YELLOW + "/l (message)" + ChatColor.GRAY
				+ " - Global chat, talks in global chat.");
		sender.sendMessage(ChatColor.YELLOW + "/l (message)" + ChatColor.GRAY
				+ " - Local chat, talks to nearby players.");
		sender.sendMessage(ChatColor.YELLOW + "/s (message)" + ChatColor.GRAY
				+ " - Shouts a message");
		sender.sendMessage(ChatColor.YELLOW + "/w (message)" + ChatColor.GRAY
				+ " - Whispers to very close players");
		sender.sendMessage(ChatColor.YELLOW + "/msg (player name) (message)"
				+ ChatColor.GRAY + " - Private message");
	}

	public static void helpClan(CommandSender sender, String[] split) {
		if (split.length < 2) {
			sender.sendMessage(ChatColor.GOLD + "-Clan Help-");
			sender.sendMessage(ChatColor.GOLD
					+ "Page 1 of 2, use \"/help clan (page)\"");
			sender.sendMessage(ChatColor.YELLOW
					+ "Info:"
					+ ChatColor.GRAY
					+ " Clans are permanent player groups. You can only be in one clan at a time.");

			sender.sendMessage(ChatColor.YELLOW + "/clan create (plot name)"
					+ ChatColor.GRAY + " - Create a clan.");
			sender.sendMessage(ChatColor.GRAY + "-Costs 2000 gold coins.");
			sender.sendMessage(ChatColor.YELLOW
					+ "/clan transfer (player name)"
					+ ChatColor.GRAY
					+ " - Transfers ownership of a         clan to another player.");
			sender.sendMessage(ChatColor.YELLOW + "/clan invite (player name)"
					+ ChatColor.GRAY + " - Invites a player to your clan.");
			sender.sendMessage(ChatColor.YELLOW
					+ "/clan uninvite (player name)" + ChatColor.GRAY
					+ " - Uninvites a player.");
		} else {
			final String page = split[1];
			if (page.equalsIgnoreCase("1")) {
				sender.sendMessage(ChatColor.GOLD + "-Clan Help-");
				sender.sendMessage(ChatColor.GOLD
						+ "Page 1 of 2, use \"/help clan (page)\"");
				sender.sendMessage(ChatColor.YELLOW
						+ "Info:"
						+ ChatColor.GRAY
						+ " Clans are permanent player groups. You can only be in one clan at a time.");

				sender.sendMessage(ChatColor.YELLOW
						+ "/clan create (plot name)" + ChatColor.GRAY
						+ " - Create a clan.");
				sender.sendMessage(ChatColor.GRAY + "-Costs 2000 gold coins.");
				sender.sendMessage(ChatColor.YELLOW
						+ "/clan transfer (player name)"
						+ ChatColor.GRAY
						+ " - Transfers ownership of a         clan to another player.");
				sender.sendMessage(ChatColor.YELLOW
						+ "/clan invite (player name)" + ChatColor.GRAY
						+ " - Invites a player to your clan.");
				sender.sendMessage(ChatColor.YELLOW
						+ "/clan uninvite (player name)" + ChatColor.GRAY
						+ " - Uninvites a player.");
			} else if (page.equalsIgnoreCase("2")) {
				sender.sendMessage(ChatColor.GOLD + "-Clan Help-");
				sender.sendMessage(ChatColor.YELLOW
						+ "Page 2 of 2, use \"/help land (page)\"");

				sender.sendMessage(ChatColor.YELLOW
						+ "/clan promote (player name)" + ChatColor.GRAY
						+ " - Promotes a member to leader.");
				sender.sendMessage(ChatColor.YELLOW
						+ "/clan demote (player name)" + ChatColor.GRAY
						+ " - Demotes a clan leader.");
				sender.sendMessage(ChatColor.YELLOW
						+ "/clan kick (player name)" + ChatColor.GRAY
						+ " - Kicks a player from your clan.");
				sender.sendMessage(ChatColor.YELLOW + "/clan leave"
						+ ChatColor.GRAY + " - Leaves your clan.");
				sender.sendMessage(ChatColor.YELLOW + "/clan dsiband"
						+ ChatColor.GRAY + " - Disbands your clan.");
				sender.sendMessage(ChatColor.YELLOW + "/clan info"
						+ ChatColor.GRAY
						+ " - Displays information about your clan.");
				sender.sendMessage(ChatColor.YELLOW + "/c (message)"
						+ ChatColor.GRAY
						+ " - Send a chat message to your clan.");
			}
		}
	}

	public static void helpKarma(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "-Karma Help-");
		sender.sendMessage(ChatColor.YELLOW
				+ "Info:"
				+ ChatColor.GRAY
				+ " If you attack a non-criminal player, you become a criminal. If that player dies, you get a murder count. If you get 5 murder counts you become a murderer. Criminal status lasts 2 minutes. Every day (real time) you lose 1 murder count. If a criminal or murderer enters a plot with guards a non-criminal player can say \"guards\" near them and the guards will kill the criminal/murderer. Criminals and murderers also respawn in a different town located in the nether.");
		sender.sendMessage(ChatColor.YELLOW + "/whois (player name)"
				+ ChatColor.GRAY + " - Displays a player's status.");
	}

	public static void helpLandOwnership(CommandSender sender, String[] split) {
		if (split.length < 2) {
			sender.sendMessage(ChatColor.GOLD + "-Land Ownership Help-");
			sender.sendMessage(ChatColor.GOLD
					+ "Page 1 of 3, use \"/help land (page)\"");
			sender.sendMessage(ChatColor.YELLOW + "Info:" + ChatColor.GRAY
					+ " You may purchase and manage land by creating plots.");
			sender.sendMessage(ChatColor.GRAY
					+ "For more information see the guide at http://www.lostshard.com");

			sender.sendMessage(ChatColor.YELLOW + "/plot create (plot name)"
					+ ChatColor.GRAY + " - Create a plot.");
			sender.sendMessage(ChatColor.GRAY
					+ "-Costs 1000 + 1 diamond for the first plot.");
			sender.sendMessage(ChatColor.GRAY
					+ "-Starts with a size of 10 block radius.");
			sender.sendMessage(ChatColor.YELLOW + "/plot survey"
					+ ChatColor.GRAY + " Helps finding a place for a plot.");
			sender.sendMessage(ChatColor.YELLOW + "/plot info" + ChatColor.GRAY
					+ " Information about current plot.");
		} else {
			final String page = split[1];
			if (page.equalsIgnoreCase("1")) {
				sender.sendMessage(ChatColor.GOLD + "-Land Ownership Help-");
				sender.sendMessage(ChatColor.GOLD
						+ "Page 1 of 3, use \"/help land (page)\"");
				sender.sendMessage(ChatColor.YELLOW
						+ "Info:"
						+ ChatColor.GRAY
						+ " You may purchase and manage land by creating plots.");
				sender.sendMessage(ChatColor.GRAY
						+ "For more information see the guide at http://www.lostshard.com");

				sender.sendMessage(ChatColor.YELLOW
						+ "/plot create (plot name)" + ChatColor.GRAY
						+ " - Create a plot.");
				sender.sendMessage(ChatColor.GRAY
						+ "-Costs 1000 + 1 diamond for the first plot.");
				sender.sendMessage(ChatColor.GRAY
						+ "-Starts with a size of 10 block radius.");
				sender.sendMessage(ChatColor.YELLOW + "/plot survey"
						+ ChatColor.GRAY + " Helps finding a place for a plot.");
				sender.sendMessage(ChatColor.YELLOW + "/plot info"
						+ ChatColor.GRAY + " Information about current plot.");
			} else if (page.equalsIgnoreCase("2")) {
				sender.sendMessage(ChatColor.GOLD + "-Land Ownership Help-");
				sender.sendMessage(ChatColor.GOLD
						+ "Page 2 of 3, use \"/help land (page)\"");

				sender.sendMessage(ChatColor.YELLOW
						+ "/plot friend/co-own/unfriend (player name)");
				sender.sendMessage(ChatColor.GRAY
						+ "- Add or remove a friend or co-owner");
				sender.sendMessage(ChatColor.YELLOW + "/plot protect/unprotect"
						+ ChatColor.GRAY
						+ " - Protects or unprotects your plot.");
				// player.sendMessage(ChatColor.GRAY+"- Protects or unprotects a plot.");
				sender.sendMessage(ChatColor.YELLOW + "/plot private/public"
						+ ChatColor.GRAY + " - Locks or unlocks your plot.");
				sender.sendMessage(ChatColor.YELLOW + "/plot deposit/withdraw"
						+ ChatColor.GRAY
						+ " - Deposits or withdraws plot funds.");
				sender.sendMessage(ChatColor.YELLOW + "/plot expand"
						+ ChatColor.GRAY + " - Expands your plot.");
				sender.sendMessage(ChatColor.GRAY
						+ "-Costs plot size times 100, so size 11 to 12 would cost 110.");
			} else if (page.equalsIgnoreCase("3")) {
				sender.sendMessage(ChatColor.GOLD + "-Land Ownership Help-");
				sender.sendMessage(ChatColor.GOLD
						+ "Page 3 of 3, use \"/help land (page)\"");

				sender.sendMessage(ChatColor.YELLOW + "/plot test"
						+ ChatColor.GRAY
						+ " - Lets you test the protection/locking.");
				sender.sendMessage(ChatColor.YELLOW + "/plot endtest"
						+ ChatColor.GRAY
						+ " - Stops testing the protection/locking.");
				sender.sendMessage(ChatColor.YELLOW + "/plot disband"
						+ ChatColor.GRAY + " - Deletes your plot.");
				sender.sendMessage(ChatColor.YELLOW + "/plot disband (name)"
						+ ChatColor.GRAY + " - Deletes another plot you own.");
				sender.sendMessage(ChatColor.YELLOW + "/plot list"
						+ ChatColor.GRAY + " - Lists your current plots.");
			}
		}
	}

	public static void helpMisc(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "-Misc Help-");
		sender.sendMessage(ChatColor.YELLOW + "/who" + ChatColor.GRAY
				+ " - See a list of online players.");
		sender.sendMessage(ChatColor.YELLOW + "/kill" + ChatColor.GRAY
				+ " - Kill yourself.");
	}

	public static void helpMoney(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "-Money Help-");
		sender.sendMessage(ChatColor.YELLOW
				+ "Info:"
				+ ChatColor.GRAY
				+ " The currency is gold based, at any time you can trade gold ingots for gold coins.");
		sender.sendMessage(ChatColor.YELLOW + "/stats" + ChatColor.GRAY
				+ " - See various stats including your money.");
		sender.sendMessage(ChatColor.YELLOW + "/tradegold (amount)"
				+ ChatColor.GRAY + " - Trades gold ingots for gold coins.");
		sender.sendMessage(ChatColor.GRAY
				+ "-Exchange rate of 1 gold ingot for 100 gold coins.");
		sender.sendMessage(ChatColor.YELLOW + "/pay (player name) (amount)"
				+ ChatColor.GRAY + " - Pays a player some coins.");
	}

	public static void helpParty(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "-Party Help-");
		sender.sendMessage(ChatColor.YELLOW
				+ "Info:"
				+ ChatColor.GRAY
				+ " A party is a temporary player group. You can only be in one party at a time. While in a party, you can't damage party members with weapons.");
		sender.sendMessage(ChatColor.YELLOW + "/party invite (player name)"
				+ ChatColor.GRAY + " - Invites a player to your party.");
		sender.sendMessage(ChatColor.GRAY
				+ "-Creates a party if you are not already in one.");
		sender.sendMessage(ChatColor.YELLOW
				+ "/party join (player name)"
				+ ChatColor.GRAY
				+ " - Join a player's party if you            have been invited.");
		sender.sendMessage(ChatColor.YELLOW + "/party leave" + ChatColor.GRAY
				+ " - Leaves your party.");
		sender.sendMessage(ChatColor.YELLOW + "/party info" + ChatColor.GRAY
				+ " - Displays information about your party.");
	}

	public static void helpScrolls(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "-Scrolls Help-");
		sender.sendMessage(ChatColor.YELLOW
				+ "Info:"
				+ ChatColor.GRAY
				+ " When you kill monsters, there is a chance they will drop a scroll. Scrolls can be used to cast spells or you can add them to your spellbook and cast them with the magery skill.");
		sender.sendMessage(ChatColor.YELLOW + "/scrolls" + ChatColor.GRAY
				+ " - See the scrolls you have collected.");
		sender.sendMessage(ChatColor.YELLOW + "/scrolls use (spell name)"
				+ ChatColor.GRAY + " - Use a scroll.");
		sender.sendMessage(ChatColor.YELLOW + "/scrolls spellbook (spell name)"
				+ ChatColor.GRAY
				+ " - Add a scroll to your              spellbook.");
	}

	public static void helpSkills(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "-Skills Help-");
		sender.sendMessage(ChatColor.GOLD
				+ "For an in-depth explation of each skill, view the guide at www.lostshard.com.");
		sender.sendMessage(ChatColor.YELLOW
				+ "Info:"
				+ ChatColor.GRAY
				+ " You can gain skills by performing actions like fighting monsters and casting spells. Each skill can go up to 100 and you can have up to 300 skill points total.");
		sender.sendMessage(ChatColor.YELLOW + "/skills" + ChatColor.GRAY
				+ " - See the skills you currently have.");
		sender.sendMessage(ChatColor.YELLOW
				+ "/skills reduce (skill name) (amount)" + ChatColor.GRAY
				+ " - Reduces a skill.");
	}
}
