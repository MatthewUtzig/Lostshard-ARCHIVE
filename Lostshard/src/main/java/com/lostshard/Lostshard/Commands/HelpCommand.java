package com.lostshard.Lostshard.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Range;

public class HelpCommand {

	@Command(aliases = { "", "help" }, desc = "Shows all the help commands")
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "All help commands:");
		sender.sendMessage(ChatColor.YELLOW + "/help plot");
		sender.sendMessage(ChatColor.YELLOW + "/help clan");
		sender.sendMessage(ChatColor.YELLOW + "/help party");
		sender.sendMessage(ChatColor.YELLOW + "/help skills");
		sender.sendMessage(ChatColor.YELLOW + "/help magery");
		sender.sendMessage(ChatColor.YELLOW + "/help karma");
	}
	
	@Command(aliases = { "land", "plot", "plots" }, desc = "Tells you how to create and use plots.")
	public void land(CommandSender sender, @Optional(value = "1") @Range(min = 1, max = 8) int page) {
			
		sender.sendMessage(ChatColor.GOLD + "-Land Ownership Help-");
		sender.sendMessage(ChatColor.GOLD + "Page " + page + " of 8, use \"/help land (page)\"");
			
		switch(page) {
		case 1:
			sender.sendMessage(ChatColor.GOLD + "Info:" + ChatColor.GRAY
					+ " You can protect your base, house, items, etc. from other players by creating a plot.");
			sender.sendMessage(ChatColor.GRAY + "-In a plot normal players cannot press stone buttons or break blocks;");
			sender.sendMessage(ChatColor.GRAY + "-however, they can use all types of pressure plates, wooden buttons, and wooden doors.");				
			sender.sendMessage(ChatColor.GRAY + "-Normal players can open chests if that do not have blocks placed over them.");
			sender.sendMessage(ChatColor.GRAY + "-Also keep in mind that plot commands take effect on the plot that you are standing in.");	
			sender.sendMessage(ChatColor.GOLD + "Tax:" + ChatColor.GRAY + " Each real life day tax is taken from your plot.");	
			sender.sendMessage(ChatColor.GRAY + "-tax = 10 * (the size of your plot)");
			sender.sendMessage(ChatColor.GRAY + "-If you fail to pay your tax, your plot will be shrunk by one block");
			break;	
		case 2:	
			sender.sendMessage(ChatColor.GOLD + "Commands:");
			sender.sendMessage(ChatColor.YELLOW + "/plot create (plot name)" + ChatColor.GRAY + " - This creates a plot");
			sender.sendMessage(ChatColor.GRAY + "-This costs 1000 YELLOW and 1 diamond for the first plot.");
			sender.sendMessage(ChatColor.GRAY + "-The price inceases for the more plots you create.");			
			sender.sendMessage(ChatColor.GRAY + "-The plot starts with a 10 block radius, which can be increased later.");
			sender.sendMessage(ChatColor.YELLOW + "/plot survey" + ChatColor.GRAY + " - tells you how large you could make a plot in a given area");
			sender.sendMessage(ChatColor.GRAY + "-You must be outside of a plot when executing this command.");
			sender.sendMessage(ChatColor.YELLOW + "/plot info" + ChatColor.GRAY + " - gives info about a plot");
			break;
		case 3:
			sender.sendMessage(ChatColor.GOLD + "Owner Commands:");
			sender.sendMessage(ChatColor.GRAY + "-the owner gets access to all co-owner and friend commands.");
			sender.sendMessage(ChatColor.YELLOW + "/plot co-own (player)");
			sender.sendMessage(ChatColor.GRAY + "-Gives a player the ability to use all co-owner and friend commands (shown below),");
			sender.sendMessage(ChatColor.GRAY + "-the ability to break blocks, and the ability to use stone buttons.");
			sender.sendMessage(ChatColor.YELLOW + "/plot friend (player)");
			sender.sendMessage(ChatColor.GRAY + "-Gives a player the ability to use all friend commands(shown below)");
			sender.sendMessage(ChatColor.GRAY + "-They can't break blocks, but they can use stone buttons.");
			sender.sendMessage(ChatColor.GRAY + "-Co-owners can also use this command.");
			break;
		case 4:
			sender.sendMessage(ChatColor.GOLD + "Owner Commands continued:");
			sender.sendMessage(ChatColor.YELLOW + "/plot disband" + ChatColor.GRAY + " - Deletes the plot you are standing on.");
			sender.sendMessage(ChatColor.GRAY + "-This gives you all the YELLOW in the plot");
			sender.sendMessage(ChatColor.YELLOW + "/plot transfer (player)" + ChatColor.GRAY + " - Gives owner to someone else." );
			sender.sendMessage(ChatColor.GRAY + "-This will remove you from the plot.");
			sender.sendMessage(ChatColor.YELLOW + "/plot protect/unprotect" + ChatColor.GRAY + " - protects/unprotects a plot.");
			sender.sendMessage(ChatColor.YELLOW + "/plot list" + ChatColor.GRAY + " - List all your current plots.");
			break;
		case 5:
			sender.sendMessage(ChatColor.GOLD + "Co-owner commands:");
			sender.sendMessage(ChatColor.YELLOW + "/plot withdraw/deposit");
			sender.sendMessage(ChatColor.GRAY + "-Allows you to add or remove YELLOW from your plot.");
			sender.sendMessage(ChatColor.YELLOW + "/plot expand/shrink" + ChatColor.GRAY + " - Increase/decrease the plot size by 1 block.");
			sender.sendMessage(ChatColor.GRAY + "-Expanding costs YELLOW, which increases the bigger your plot is.");
			sender.sendMessage(ChatColor.GRAY + "-Shrinking costs nothing.");
			sender.sendMessage(ChatColor.YELLOW + "/plot upgrade (upgrade)" + ChatColor.GRAY + " - Allows you to buy plot upgrades for gold coins.");
			sender.sendMessage(ChatColor.GRAY + "-upgrades are on pages 7 and 8.");
			break;
		case 6:
			sender.sendMessage(ChatColor.GOLD + "Co-owner commands continued:");
			sender.sendMessage(ChatColor.YELLOW + "/plot test" + ChatColor.GRAY + " - Toggles whether you are testing a plot.");
			sender.sendMessage(ChatColor.GRAY + "-Testing a plot prevents you from breaking blocks and using stone buttons.");
			sender.sendMessage(ChatColor.GRAY + "-Friends can also test, but this only removes the ability to press stone buttons.");
			sender.sendMessage(ChatColor.YELLOW + "/plot friend/unfriend" + ChatColor.GRAY + " - Promote a player to friend or.");
			sender.sendMessage(ChatColor.GRAY + "-demote a player to a non-member.");
			sender.sendMessage(ChatColor.GOLD + "Friends:");
			sender.sendMessage(ChatColor.YELLOW + "/plot deposit" + ChatColor.GRAY + " - Friends can donate gold to the plot, ");
			sender.sendMessage(ChatColor.GRAY + "-but they can't withdraw.");
			break;
		case 7:
			sender.sendMessage(ChatColor.GOLD + "Plot upgrades:");
			sender.sendMessage(ChatColor.YELLOW + "Town:" + ChatColor.GRAY + " Costs 100,000 gold coins.");
			sender.sendMessage(ChatColor.GRAY + "-Allows any player to set their spawn with a bed in your plot.");
			sender.sendMessage(ChatColor.GRAY + "-Allows the ability to spawn a banker or a vender in your plot ");
			sender.sendMessage(ChatColor.GRAY + "-with /plot npc hire [banker/vender] (name).");
			sender.sendMessage(ChatColor.GRAY + "-To move a npc: /plot npc move (name).");
			sender.sendMessage(ChatColor.GRAY + "-To remove a npc: /plot npc fire (name).");
			sender.sendMessage(ChatColor.YELLOW + "Neutral Alignment:" + ChatColor.GRAY + " Costs 2,000 gold coins.");
			sender.sendMessage(ChatColor.GRAY + "-Allows both criminals and non-crimals to set spawn in a bed.");
			sender.sendMessage(ChatColor.YELLOW + "AutoKick:" + ChatColor.GRAY + " Costs 5,000 gold coins. ");
			sender.sendMessage(ChatColor.GRAY + "-When a player relogs in your plot they are sent out of it.");
			break;
		case 8:
			sender.sendMessage(ChatColor.GOLD + "Plot upgrades Continued:");
			sender.sendMessage(ChatColor.YELLOW + "Dungeon:" + ChatColor.GRAY + " Costs 10,000 gold coins.");
			sender.sendMessage(ChatColor.GRAY + "-Allows hostile mobs to spawn in your plot.");
			sender.sendMessage(ChatColor.YELLOW + "/plot downgrade (upgrade)" + ChatColor.GRAY + " -removes a plot upgrade.");
			break;
		}
	}
	
	@Command(aliases = { "clan", "clans" }, desc = "Tells you how to create and manage clans.")
	public void clan(CommandSender sender, @Optional(value = "1") @Range(min = 1, max = 3) int page) {
		
		sender.sendMessage(ChatColor.GOLD + "-Clan Help-");
		sender.sendMessage(ChatColor.GOLD + "Page " + page + " of 3, use \"/help clan (page)\"");
		
		switch(page) {
		case 1:
			sender.sendMessage(ChatColor.GOLD + "Info:" + ChatColor.GRAY + " Unlike parties, Clans are permanent player groups.");
			sender.sendMessage(ChatColor.GRAY + "A clan is required to capture hostility. This can be done by doing /claim while in hostility.");
			sender.sendMessage(ChatColor.GRAY + "Capturing hostility will reward you with 5 free gold ingots from the vender.");
			sender.sendMessage(ChatColor.GRAY + "You can also teleport to your clanmates by casting Clan Teleport.");
			break;
		case 2:
			sender.sendMessage(ChatColor.YELLOW + "/clan create (plot name)" + ChatColor.GRAY + " - Create a clan.");
			sender.sendMessage(ChatColor.GRAY + "-Costs 2,000 YELLOW coins.");
			sender.sendMessage(ChatColor.YELLOW + "/clan transfer (player name)" + ChatColor.GRAY + "  Gives your clan to another player.");
			sender.sendMessage(ChatColor.YELLOW + "/clan [invite/uninvite] (player name)" + ChatColor.GRAY+ "  Invites/uninvites a player to your clan.");
			break;
		case 3:
			sender.sendMessage(ChatColor.YELLOW + "/clan promote (player name)" + ChatColor.GRAY  + " - Promotes a member to leader. This can only be used by the owner.");
			sender.sendMessage(ChatColor.GRAY + "-Leaders can use most commands and can kick players from the clan.");
			sender.sendMessage(ChatColor.YELLOW + "/clan demote (player name)" + ChatColor.GRAY + " - Demotes a clan leader back to member.");
			sender.sendMessage(ChatColor.YELLOW + "/clan kick (player name)" + ChatColor.GRAY + " - Kicks a player from your clan.");
			sender.sendMessage(ChatColor.YELLOW + "/clan leave" + ChatColor.GRAY + " - Leaves your clan. You can't be the owner");
			sender.sendMessage(ChatColor.YELLOW + "/clan disband" + ChatColor.GRAY + " - Disbands your clan. This can be used by owner only.");
			sender.sendMessage(ChatColor.YELLOW + "/clan info" + ChatColor.GRAY + " - Displays information about your clan. Leaders and owners get more information.");
			sender.sendMessage(ChatColor.YELLOW + "/c (message)" + ChatColor.GRAY + " - Send a chat message to your clan.");
			sender.sendMessage(ChatColor.YELLOW + "/c" + ChatColor.GRAY + " - Makes all future messages in clan chat. Use /g to get back.");
			break;
		}	
	}
	
	@Command(aliases = { "party", "parties", "partys" }, desc = "Tells you what parties are.")
	public void party(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "-Party Help-");
		sender.sendMessage(ChatColor.YELLOW + "Info:" + ChatColor.GRAY + " Unlike clans, a party is a temporary player group. You can only be in one party at a time.");
		sender.sendMessage(ChatColor.GRAY + "-You cannot damage party member; however, if both players toggle friendly fire with /ff they can damage each other.");
		sender.sendMessage(ChatColor.GRAY + "-When you leave the game you are automatically removed from your party.");
		sender.sendMessage(ChatColor.GOLD + "Commands:");
		sender.sendMessage(ChatColor.YELLOW + "/party join (player name)" + ChatColor.GRAY + " - Join a player's party if you have been invited by that player.");
		sender.sendMessage(ChatColor.YELLOW + "/party invite (player name)" + ChatColor.GRAY + " - Invites a player to your party. Anyone in a party can invite people.");
		sender.sendMessage(ChatColor.YELLOW + "/party leave" + ChatColor.GRAY + " - Leaves your party.");
		sender.sendMessage(ChatColor.YELLOW + "/party info" + ChatColor.GRAY + " - Displays who is in your current party.");
	}
	
	@Command(aliases = { "Skill", "skill", "Skills", "skills" }, desc = "Tell how to level and use skills")
	public void skills(CommandSender sender, @Optional(value = "1") @Range(min = 1, max = 11) int page) {
		
		sender.sendMessage(ChatColor.GOLD + "Skills Help-");
		sender.sendMessage(ChatColor.GOLD + "Page " + page + " of 11, use \"/help Skills (page)\"");
	
		switch(page) {
		case 1:
			sender.sendMessage(ChatColor.GOLD + "Info:" + ChatColor.GRAY + " Lost Shard has 10 total skills; Blades, Brawling, Magery, Mining, Survivalism, Archery, Blacksmithing, Taming, Lumberjacking, and fishing. \n");
			sender.sendMessage(ChatColor.GRAY + "-Each skill can be raised up to level 100, with a maximum of 400 levels per build.\n");
			sender.sendMessage(ChatColor.GRAY + "-You can change your build at the build change at spawn. A build is a set of skills.");
			sender.sendMessage(ChatColor.YELLOW + "/skills reduce (skills)" + ChatColor.GRAY + " Allows you to reduce a skill.");
			sender.sendMessage(ChatColor.YELLOW + "/skills [lock/unlock] (skill)" + "" + ChatColor.GRAY + " Allows you to lock a skill, which prevents it from gaining");
			sender.sendMessage(ChatColor.YELLOW + "/skills increase (skills)" + ChatColor.GRAY + " Allows you to increase a skill if you have been given skill points.");
			sender.sendMessage(ChatColor.YELLOW + "/resetallskills (skill)" + ChatColor.GRAY + " Resets all the skills in a build to 0, but sets the given skill to 50.");
			sender.sendMessage(ChatColor.GRAY + "-This is good for a head start.");
		case 2:
			
			sender.sendMessage(ChatColor.GOLD + "Blades: ");
			sender.sendMessage(ChatColor.GRAY + "-The higher this skill is the more damage you will do with a sword and the more likely it is to cause an opponent to bleed.");
			sender.sendMessage(ChatColor.GRAY + "-Bleeding causes small amounts of damage to be dealt periodically after the initial attack. ");
			sender.sendMessage(ChatColor.GRAY + "-Blades can be inceased by hitting mobs or players with a sword. ");
			
			
		case 3:	
		
			sender.sendMessage(ChatColor.GOLD + "Brawling: ");
			sender.sendMessage(ChatColor.GRAY + "-The higher this skill is the more damage you do with your fist, you also have a better chance at stunning an opponent.");
			sender.sendMessage(ChatColor.GRAY + "-Being stunned makes magic, attacking and eating unavailable for a brief period of time. ");
			sender.sendMessage(ChatColor.GRAY + "-Brawling can be increased by hitting players or mobs with your fists. ");
			
		case 4:
			sender.sendMessage(ChatColor.GOLD + "Mining: ");
			sender.sendMessage(ChatColor.GRAY + "-The higher this skill is the higher chance you have of getting frops from stone and other natural rocks.");
			sender.sendMessage(ChatColor.GRAY + "-Some of these drops include: Diamond, Diamond ore, Gold ore, Obsidian, Redstone ore, Lapis ore. ");
			sender.sendMessage(ChatColor.GRAY + "-This can be increased by mining ores and stone. ");
			
		case 5:
			sender.sendMessage(ChatColor.GOLD + "Survivalism: ");
			sender.sendMessage(ChatColor.GRAY + "-This skill lets you track animals and other players.");
			sender.sendMessage(ChatColor.YELLOW + "-/track [(player name)/(animal name)]");
			sender.sendMessage(ChatColor.GRAY + "-Tell the direction of the mob or player and how far away they are. ");
			sender.sendMessage(ChatColor.GRAY + "-This skill can be gained by tracking mobs and players. ");
			sender.sendMessage(ChatColor.GRAY + "-If you have 100 survivalism you can't be tracked. ");
			sender.sendMessage(ChatColor.YELLOW + "-/camp" + ChatColor.GRAY + "Summons a temporary campfire.");
			sender.sendMessage(ChatColor.GRAY + "-When you are near a camp your health regeneration rate is increased. ");
			sender.sendMessage(ChatColor.GRAY + "-Campfires can be removed by right clicking on them. ");
		case 6:
			sender.sendMessage(ChatColor.GOLD + "Archery: ");
			sender.sendMessage(ChatColor.GRAY + "-The higher this skill is the more damage you do with the boe and arrow.");
			sender.sendMessage(ChatColor.GRAY + "-It also increases you change of piercing armor, which does true damage. ");
			sender.sendMessage(ChatColor.GRAY + "-This can be increased by shooting animals or players with the bow and arrow. ");
		case 7:
			sender.sendMessage(ChatColor.GOLD + "Blacksmithy: ");
			sender.sendMessage(ChatColor.GRAY + "-This skill allows you to repair, enchant, and smelt items and armor using raw materials.");
			sender.sendMessage(ChatColor.GRAY + "-The ability to use the 3 different enchant levels from enchant tables is unlocked with blacksmithy.");
			sender.sendMessage(ChatColor.GRAY + "-Smelting and Repairing can never fail, if you have 100 blacksmithy.");
			sender.sendMessage(ChatColor.YELLOW + "-/smelt " + ChatColor.GRAY + "Allows you to smelt the item in your hand into raw materials." );
			sender.sendMessage(ChatColor.GRAY + "-Has a lower chance of failing the higher your blacksmithy is. ");
			sender.sendMessage(ChatColor.YELLOW + "-/repair " + ChatColor.GRAY + "Allows you to repair the item in your hand with raw materials." );
			sender.sendMessage(ChatColor.GRAY + "-Has a lower chance of failing the higher your blacksmithy is. ");
			
		case 8:
			sender.sendMessage(ChatColor.GOLD + "Blacksmithy continued: ");
			sender.sendMessage(ChatColor.YELLOW + "-/enhance " + ChatColor.GRAY + "Allows you to smelt the item in your hand into raw materials." );
			sender.sendMessage(ChatColor.GRAY + "-Can never fail. ");
			sender.sendMessage(ChatColor.GRAY + "-Uses more raw materials the higher you enhance a tool. ");
			sender.sendMessage(ChatColor.GRAY + "-The final level of enhancing must be done at a capture plot, such as: hostility.");
			sender.sendMessage(ChatColor.GOLD + "More info: " + ChatColor.GRAY + "Blacksmithy can be gained by smelting and repairing items." );
			sender.sendMessage(ChatColor.GRAY + "-The folowing tiers of items should be used with repairing to gain the folowing blacksmithy levels: Wood: levels 0-25, Stone: Levels 25-50, Iron or Gold levels 50-75, Diamond: Levels 75-100. ");
			sender.sendMessage(ChatColor.GRAY + "-You can only get to level 50 from smelting items.");
			
		case 9:
			sender.sendMessage(ChatColor.GOLD + "Taming: ");
			sender.sendMessage(ChatColor.GRAY + "-This skill allows you to summon more wolves the higher it is.");
			sender.sendMessage(ChatColor.RED + "-This skill is currently broken, but more information will be added to this skill when it is fixed.");
			
		case 10:
			sender.sendMessage(ChatColor.GOLD + "Lumberjacking: ");
			sender.sendMessage(ChatColor.GRAY + "-This skill allows more wood to fall from trees when breaking them with an axe.");
			sender.sendMessage(ChatColor.GRAY + "-This skill can be increased by breaking tree logs with an axe.");
			sender.sendMessage(ChatColor.GOLD + "Magery: ");
			sender.sendMessage(ChatColor.GRAY + "-Use '/help magic' for a detailed guide.");
		case 11:	
			sender.sendMessage(ChatColor.GOLD + "Fishing: ");
			sender.sendMessage(ChatColor.GRAY + "-This skill allows you to get special items from fishing.");
			sender.sendMessage(ChatColor.GRAY + "-The higher your fishing the lever the greater chance you have of getting an item from fishing.");
			sender.sendMessage(ChatColor.GRAY + "-Fishing can be increased by catching fish or items with a fishing rod");
			sender.sendMessage(ChatColor.YELLOW + "-/boat " + ChatColor.GRAY + "Summons a boat out of thin air." );
			sender.sendMessage(ChatColor.GRAY + "-Requires 50 fishing.");
		}
		
	}
	
	@Command(aliases = { "magic", "magery", "scrolls", "spells" }, desc = "Explains how to use spells")
	public void magery(CommandSender sender, @Optional(value = "1") @Range(min = 1, max = 12) int page) {
		sender.sendMessage(ChatColor.GOLD + "Magery Help-");
		sender.sendMessage(ChatColor.GOLD + "Page " + page + " of 12, use \"/help Magery (page)\"");
			
		switch(page) {
		case 1:
			sender.sendMessage(ChatColor.GOLD + "Info:" + ChatColor.GRAY + " Magery allows the ability to cast spells, which do various things.");
			sender.sendMessage(ChatColor.GRAY + "-First you must find some scrolls. Scrolls are easily obtained from killing mobs, but can also be obtained from other players.");
			sender.sendMessage(ChatColor.YELLOW + "/scrolls" + ChatColor.GRAY + " - Allows you to see your scrolls.");				
			sender.sendMessage(ChatColor.GRAY + "-From here you can cast low level spells using a scroll, but this will consume the scroll.");
			sender.sendMessage(ChatColor.GRAY + "-You can get infinite uses by transfering a scroll to you spellbook, but you will need a high enough magery to cast the spell.");	
			sender.sendMessage(ChatColor.YELLOW + "/spellbook" + ChatColor.GRAY  + " - Allows you to see your scrolls that you have transfered. Higher pages require a higher minimun magery.");	
			sender.sendMessage(ChatColor.GRAY + "-Each page requires a different amount of minimum magery to cast. The first page requires 0.");
			sender.sendMessage(ChatColor.GRAY + "-You can cast spells from page 1 until you hit the minimum magery for page 2. Then you can do page 2 until you have enough for page 3.");
			sender.sendMessage(ChatColor.GRAY + "-You can keep doing this until you have maximum magery.");
			break;	
		case 2:	
			sender.sendMessage(ChatColor.GOLD + "Commands:");
			sender.sendMessage(ChatColor.YELLOW + "/cast (spell)" + ChatColor.GRAY + " - Casts a spell.");
			sender.sendMessage(ChatColor.GRAY + "-If you fail to cast a spell you will get the message: 'The spell fizzles'.");
			sender.sendMessage(ChatColor.GRAY + "-Whether a spell fails or not is baised on your magery and which spellbook page it is on.");			
			sender.sendMessage(ChatColor.GRAY + "-The higher level the spell the higher chance it has of increasing your magery. At 100 magery you never fail at casting a spell.");
			sender.sendMessage(ChatColor.GRAY + "-Spell require curtain items to cast, which are listed in future pages.");
			sender.sendMessage(ChatColor.YELLOW + "/bind (spell)" + ChatColor.GRAY + " - You must be holding a stick");
			sender.sendMessage(ChatColor.GRAY + "-Allows a player to cast a spell by left clicking the stick.");
			
			break;
		case 3:
			sender.sendMessage(ChatColor.GOLD + "Spellbook page 1:" + ChatColor.GRAY + " Minumum magery: 0");
			sender.sendMessage(ChatColor.YELLOW + "Grass" + ChatColor.GRAY + " Required items: seeds");
			sender.sendMessage(ChatColor.GRAY + "-Changes dirt into grass.");
			sender.sendMessage(ChatColor.YELLOW + "Flowers" + ChatColor.GRAY + " Required items: seeds");
			sender.sendMessage(ChatColor.GRAY + "-Spawns flowers on grass.");
			sender.sendMessage(ChatColor.YELLOW + "Flare" + ChatColor.GRAY + " Required items: Gunpowder");
			sender.sendMessage(ChatColor.GRAY + "-Spawns a red firework.");
			
			break;
		case 4:
			sender.sendMessage(ChatColor.GOLD + "Spellbook page 2:" + ChatColor.GRAY + " Minumum magery: 12");
			sender.sendMessage(ChatColor.YELLOW + "Light" + ChatColor.GRAY + " Required items: sugar cane");
			sender.sendMessage(ChatColor.GRAY + "-Spawns a torch.");
			sender.sendMessage(ChatColor.YELLOW + "Create Food" + ChatColor.GRAY + " Required items: seeds");
			sender.sendMessage(ChatColor.GRAY + "-Summons a piece of food.");
			
			break;
		case 5:
			sender.sendMessage(ChatColor.GOLD + "Spellbook page 3:" + ChatColor.GRAY + " Minumum magery: 24");
			sender.sendMessage(ChatColor.YELLOW + "Arrow Blast"  + ChatColor.GRAY + " Required items: arrow and gundpowder");
			sender.sendMessage(ChatColor.GRAY + "-Shoot arrows in all directions. These cannot be picked up.");
			sender.sendMessage(ChatColor.YELLOW + "Teleport" + ChatColor.GRAY + " Required items: feather");
			sender.sendMessage(ChatColor.GRAY + "-Teleports you about 20 blocks forward.");
			break;
		case 6:
			sender.sendMessage(ChatColor.GOLD + "Spellbook page 4:" + ChatColor.GRAY + " Minumum magery: 36");
			sender.sendMessage(ChatColor.YELLOW + "Mark"  + ChatColor.GRAY + " Required items: feather and redstone");
			sender.sendMessage(ChatColor.GRAY + "-Creates a rune that can be teleported to using recall.");
			sender.sendMessage(ChatColor.YELLOW + "Recall" + ChatColor.GRAY + " Required items: feather");
			sender.sendMessage(ChatColor.GRAY + "-Allows a player to teleport back to a rune that they have created.");
			sender.sendMessage(ChatColor.GRAY + "-This works like how home and sethome does on other servers.");
			sender.sendMessage(ChatColor.YELLOW + "/runebook" + ChatColor.GRAY + "-Runes created by mark are stored in your runebook.");
			sender.sendMessage(ChatColor.GRAY + "-From you runebook you can delete runes and give them to other players.");
			sender.sendMessage(ChatColor.GRAY + "-There is a maximum amount of runes you can have.");
			break;
		case 7:
			sender.sendMessage(ChatColor.GOLD + "Spellbook page 4 continued:" + ChatColor.GRAY + " Minumum magery: 36");
			sender.sendMessage(ChatColor.YELLOW + "Bridge"  + ChatColor.GRAY + " Required items: sugar cane");
			sender.sendMessage(ChatColor.GRAY + "-Creates a temporary bridge of leaves in the direction the player is pointing.");
			sender.sendMessage(ChatColor.YELLOW + "Wall of Stone" + ChatColor.GRAY + " Required items: stone and redstone");
			sender.sendMessage(ChatColor.GRAY + "-Creates a temporary wall of stone, which can be used to block people in pvp.");
			break;
		case 8:
			sender.sendMessage(ChatColor.GOLD + "Spellbook page 5:" + ChatColor.GRAY + " Minumum magery: 48");
			sender.sendMessage(ChatColor.YELLOW + "Chronoport"  + ChatColor.GRAY + " Required items: feather and redstone");
			sender.sendMessage(ChatColor.GRAY + "-After 5 seconds teleports the player back to where they casted the spell");
			sender.sendMessage(ChatColor.YELLOW + "Summon Animal" + ChatColor.GRAY + " Required items: feather and redstone");
			sender.sendMessage(ChatColor.GRAY + "-Summons a passive mob.");
			sender.sendMessage(ChatColor.YELLOW + "Slow Field"  + ChatColor.GRAY + " Required items: string");
			sender.sendMessage(ChatColor.GRAY + "-Spawns a temporary field of cobwebs, which can be used to trap people in pvp.");
			sender.sendMessage(ChatColor.YELLOW + "DetectHidden"  + ChatColor.GRAY + " Required items: feather and redstone");
			sender.sendMessage(ChatColor.GRAY + "-Reveals nearby players with invisibility.");
			sender.sendMessage(ChatColor.YELLOW + "FireField"  + ChatColor.GRAY + " Required items: gunpowder");
			sender.sendMessage(ChatColor.GRAY + "-Spawns a field of fire, which can be good for pvp.");
			
			break;
		case 9:
			sender.sendMessage(ChatColor.GOLD + "Spellbook page 6:" + ChatColor.GRAY + " Minumum magery: 60");
			sender.sendMessage(ChatColor.YELLOW + "Force Push"  + ChatColor.GRAY + " Required items: feather and redstone");
			sender.sendMessage(ChatColor.GRAY + "-Pushes a player away from you. ");
			sender.sendMessage(ChatColor.YELLOW + "Heal Self" + ChatColor.GRAY + " Required items: string and redstone");
			sender.sendMessage(ChatColor.GRAY + "-Heals 5 hearts of the player that casts it..");
			sender.sendMessage(ChatColor.YELLOW + "Fire Walk"  + ChatColor.GRAY + " Required items: gunpowder and redstone");
			sender.sendMessage(ChatColor.GRAY + "-Spawns temporary fire under the casters feet. Also give fire resistance.");
			sender.sendMessage(ChatColor.YELLOW + "Heal Other"  + ChatColor.GRAY + " Required items: seeds");
			sender.sendMessage(ChatColor.GRAY + "-Heals a nearby player.");
			break;
		case 10:
			sender.sendMessage(ChatColor.GOLD + "Spellbook page 7:" + ChatColor.GRAY + " Minumum magery: 72");
			sender.sendMessage(ChatColor.YELLOW + "Lightning"  + ChatColor.GRAY + " Required items: string and redstone");
			sender.sendMessage(ChatColor.GRAY + "-Summons lighting");
			sender.sendMessage(ChatColor.YELLOW + "Clan teleport" + ChatColor.GRAY + " Required items: feather and redstone");
			sender.sendMessage(ChatColor.GRAY + "-Teleports the caster to a clan member if they have done /public.");
			sender.sendMessage(ChatColor.YELLOW + "Gate Travel"  + ChatColor.GRAY + " Required items: string and redstone");
			sender.sendMessage(ChatColor.GRAY + "-Spawns a temporary portal to one of your runes.");
			sender.sendMessage(ChatColor.YELLOW + "Stone Skin"  + ChatColor.GRAY + " Required items: stone and redstone");
			sender.sendMessage(ChatColor.GRAY + "-Gives you resistance 3, but also slowness 4 for 15 seconds.");
			break;
		case 11:
			sender.sendMessage(ChatColor.GOLD + "Spellbook page 8:" + ChatColor.GRAY + " Minumum magery: 84");
			sender.sendMessage(ChatColor.YELLOW + "Moon Jump"  + ChatColor.GRAY + " Required items: feather and redstone");
			sender.sendMessage(ChatColor.GRAY + "-Gives you a high level of jump boost.");
			sender.sendMessage(ChatColor.YELLOW + "Permanent Gate Travel" + ChatColor.GRAY + " Required items: obsidian, string, and redstone");
			sender.sendMessage(ChatColor.GRAY + "-Summons a permanent portal to one of your runes.");
			sender.sendMessage(ChatColor.YELLOW + "Day"  + ChatColor.GRAY + " Required items: Glowstone and redstone");
			sender.sendMessage(ChatColor.GRAY + "-Changes the time to day if someone else is casting it with you.");
			sender.sendMessage(ChatColor.YELLOW + "ClearSkys"  + ChatColor.GRAY + " Required items: seeds and redstone");
			sender.sendMessage(ChatColor.GRAY + "-Changes the weather to clear if someone else is casting it with you.");
			break;
		case 12:
			sender.sendMessage(ChatColor.GOLD + "Spellbook page 9:" + ChatColor.GRAY + " Minumum magery: 100");
			sender.sendMessage(ChatColor.GRAY + "-These page contains dragon spells");
			sender.sendMessage(ChatColor.GRAY + "-These are not currently in the game, but will probably be added in the future.");
			sender.sendMessage(ChatColor.GRAY + "-These spells are only dropped by the enderdragon.");
			sender.sendMessage(ChatColor.GRAY + "-They also all require a dragon egg to cast.");
			sender.sendMessage(ChatColor.GRAY + "-The enderdragon spawns every day in the end");
			sender.sendMessage(ChatColor.GRAY + "-The dragon doesn't spawn a portal, but it still drops a dragon egg and xp.");
			break;
	}
// don't know why this error is here. Please fix then remove this comment.	
	@Command(aliases = { "Karma", "guard", "guards" }, desc = "gives infomation about player roles and guards." );
	public void karma(CommandSender sender, @Optional(value = "1") @Range(min = 1, max = 2) int page) {
		sender.sendMessage(ChatColor.GOLD + "-Karma Help-");
		sender.sendMessage(ChatColor.GOLD + "Page " + page + " of 2, use \"/help karma (page)\"");
	
		switch(page) {
		case 1:
			sender.sendMessage(ChatColor.GOLD + "Info:" + ChatColor.GRAY + " If you attack a non-criminal player (a player with a blue nametag), you become a criminal (a player with a gray nametag) for five minutes. ");
			sender.sendMessage(ChatColor.GRAY + "-If you kill a non-criminal player, you gain a murder count." );
			sender.sendMessage(ChatColor.GRAY + "-If you gain five or more murder counts you become a murderer (a player with a red nametag).");
			sender.sendMessage(ChatColor.GRAY + "-Non-criminals spawn at a different place than murderers and criminals do.");
			sender.sendMessage(ChatColor.GRAY + "-The place where non-criminals spawn is protected by guards,");
			sender.sendMessage(ChatColor.GRAY + "-These guards instantly kill any criminal or murderer, who gets within 5 blocks of the guard.");
			sender.sendMessage(ChatColor.GRAY + "-A non-criminal can type 'guards' to kill a player that isn't within the 5 block range.");
			break;
		case 2:
			sender.sendMessage(ChatColor.GOLD + "Murderers:" + ChatColor.GRAY + " Gaining 5 or more murder counts will make you a murderer.");
			sender.sendMessage(ChatColor.GRAY + "Every day you will lose one murder count.");
			sender.sendMessage(ChatColor.GRAY + "If you get 20 or more murder counts you will become a permanent murderer");
			sender.sendMessage(ChatColor.GRAY + "If you are a permanent murderer you can go to the Shrine of Atonement to lower your murder counts.");
			sender.sendMessage(ChatColor.GOLD + "Commands:");
			sender.sendMessage(ChatColor.YELLOW + "/whois (player name) or /who (player name)" + ChatColor.GRAY + " - Displays a player's criminal status and their murder counts.");
			break;
		
		
		
		}
		
	}
}	

