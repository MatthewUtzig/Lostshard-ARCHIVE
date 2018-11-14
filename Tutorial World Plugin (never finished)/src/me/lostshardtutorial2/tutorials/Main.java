package me.lostshardtutorial2.tutorials;





import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.lostshardtutorial.util.ListStore;
import me.lostshardtutorial2.methods.Other;
import me.lostshardtutorial2.methods.Tutorialmethods;
import me.lostshardtutorial2.tutorials.Tutorials;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Main extends JavaPlugin implements Listener {

	
	public ListStore tutorialChoice;
	
	public void onEnable(){
		
		
		getServer().getPluginManager().registerEvents(this, this);
		
		String pluginFolder = this.getDataFolder().getAbsolutePath();
		(new File(pluginFolder)).mkdirs();
		this.tutorialChoice = new ListStore(new File(pluginFolder + File.separator + "tutorialChoice.txt")); 
		
		this.tutorialChoice.load();
		
	}	

	
		
	
	public void onDisable(){
		this.tutorialChoice.save();
	}	
	
	
	private void openGUI(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.BLUE 
				+ "Left click to start a tutorial!");
		
		ItemStack mainTutorial = new ItemStack (Material.GRASS);
		ItemMeta mainTutorialMeta = mainTutorial.getItemMeta();
		
		ItemStack moneyTutorial = new ItemStack (Material.GOLD_BLOCK);
		ItemMeta moneyTutorialMeta = moneyTutorial.getItemMeta();
		
		 ArrayList<String> moneyLore = new ArrayList<String>();
		 moneyLore.add(ChatColor.GOLD + "Explains banks and gold coins.");
		moneyTutorialMeta.setLore(moneyLore);
		moneyTutorialMeta.setDisplayName(ChatColor.GOLD + "Money tutorial." );
		moneyTutorial.setItemMeta(moneyTutorialMeta);
		
		
		ArrayList<String> tutorialLore = new ArrayList<String>();
		 tutorialLore.add(ChatColor.BLUE + "The tutorial that is displayed when you logged it.");
		 mainTutorialMeta.setLore(tutorialLore);
		mainTutorialMeta.setDisplayName(ChatColor.BLUE + "Main Tutorial" );
		mainTutorial.setItemMeta(mainTutorialMeta);
		
		inv.setItem(0, mainTutorial);
		inv.setItem(1, moneyTutorial);
		
		p.openInventory(inv);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void HungerChanceEvent (FoodLevelChangeEvent event) {
		Entity e = event.getEntity();
		if (e instanceof Player) {
			((Player) e).setFoodLevel(20);
			event.setCancelled(true);
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void OnPlayerDropItem(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void OnPlayerDamage(EntityDamageEvent event) {
		Entity e = event.getEntity();
		if (e instanceof Player) {
			event.setCancelled(true);
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	
	@EventHandler (priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent event) {
		if(!ChatColor.stripColor(event.getInventory().getName()).equalsIgnoreCase("Left click to start a tutorial!")) return;
		
		
		Player player = (Player) event.getWhoClicked();
		event.setCancelled(true);
		
		if(event.getCurrentItem()==null || event.getCurrentItem().getType() == Material.AIR || !event.getCurrentItem().hasItemMeta()) {
			player.closeInventory();
			return;
		}
		
		switch (event.getCurrentItem().getType()) {
		case GRASS:
		Bukkit.getServer().dispatchCommand(player, "maintutorialcommand");
		player.closeInventory();
		player.sendMessage(ChatColor.BLUE + "Starting the main tutorial...");
		break;
		case GOLD_BLOCK:
		Bukkit.getServer().dispatchCommand(player, "moneytutorialcommand");
		player.closeInventory();
		player.sendMessage(ChatColor.BLUE + "Starting the money tutorial...");	
		}
	}
	
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void OnPlayerJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		Tutorialmethods.clearInventory(p);	
		p.setGameMode(GameMode.SURVIVAL);
		final Plugin thisplugin = this;
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			
		    public void run() {
		    	
		    	Other.blankLines(25, p);
		    	
				p.sendMessage(ChatColor.GOLD + "Would you like to skip the tutorial? If you do not click anything within the next 45 seconds the tutorial will start automatically!");
				TextComponent joinMessage = new TextComponent( ChatColor.DARK_BLUE + "Click to start the tutorial.   |" );
			
				joinMessage.setClickEvent(new ClickEvent ( ClickEvent.Action.RUN_COMMAND, "/secondquestiononjoin" ));
				joinMessage.setHoverEvent(new HoverEvent ( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here.").create() ) );
			
			
				TextComponent joinMessage2 = new TextComponent( ChatColor.DARK_RED + "|   Click to skip the tutorial." );
			
				joinMessage2.setClickEvent(new ClickEvent ( ClickEvent.Action.RUN_COMMAND, "/kickself" ));
				joinMessage2.setHoverEvent(new HoverEvent ( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here.").create() ) );
			
				joinMessage.addExtra( joinMessage2 );
			
				p.spigot().sendMessage( joinMessage );
		    }
			 
		}, 10L );	
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			
		    public void run() {
				if (!p.getInventory().contains(Material.COMPASS)) {
					Tutorials.testTutorial(p, thisplugin, true, useChat(p));
					
				}
		    }
			 
		}, 910L );	
	}
	
	
	
	@EventHandler (priority = EventPriority.HIGH)
	public void OnPlayerInteract(PlayerInteractEvent event){
	try {
		final Player p = event.getPlayer();
	
		if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR &&  p.getInventory().getItemInMainHand().getType() != Material.BARRIER ||  p.getInventory().getItemInMainHand().getType() != Material.COMPASS) {
			Action action = event.getAction();
			ItemStack itemStack = event.getItem();
			ItemMeta itemMeta = itemStack.getItemMeta();
			
		
		if (action == Action.PHYSICAL || itemStack == null || itemStack.getType() == Material.AIR) {
			return;
		} else {
		
		
		
		
		
		if (itemStack.getType()== Material.COMPASS) {
			openGUI(p);
	    }
		if (itemStack.getType() == Material.BARRIER && ChatColor.stripColor(itemMeta.getDisplayName()).equalsIgnoreCase("CLEAR INVENTORY")) {
			Tutorialmethods.secondClearBarrier(p, this);
			Tutorials.clearWarningTutorial(p);
			
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				
			    public void run() {
			    	if (p.getInventory().contains(Material.BARRIER)) {
			    		Tutorialmethods.firstTutorialBarrier(p);
			    	}
			    }
			 
			}, 240L );
			
	    }
		if (itemStack.getType()== Material.BARRIER && ChatColor.stripColor(itemMeta.getDisplayName()).equalsIgnoreCase("CLICK AGAIN TO CLEAR YOUR INVENTORY!")) {
			Tutorialmethods.defaultTutorialWorldInventory(p, this);
			Tutorials.clearTutorial(p);
	    }
		}
	}
	}
	catch (Exception e) {
		
	}
	}	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCommand(CommandSender Player, Command cmd, String commandLabel, String[] args) {
		
		final Plugin plugin = this;
		final Player tPlayer = (Player) Player;
		String sPlayer = tPlayer.getName();
		String chat = sPlayer + " - chat";
		String sign = sPlayer + " - sign";
		
		if(commandLabel.equalsIgnoreCase("maintutorialcommand")){
			
			
		Tutorials.testTutorial(tPlayer, this, false, useChat(tPlayer));
			
			
			return true;
			} else if(commandLabel.equalsIgnoreCase("howmanylines")) {
			
				if (tPlayer.isOp() == true) {
					if (args.length < 1) {
						tPlayer.sendMessage(ChatColor.DARK_RED + "You must specify text with /howmanylines (text)");
					} else {
					
					String message = "";
				
					for(int i = 0; i < args.length; i++) {
						String arg = args[i] + " ";
						message = message + arg;
					}
					tPlayer.sendMessage(message);	
						
					}
				} else {
					tPlayer.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform this command!");
				}
				return true;
			} else if (commandLabel.equalsIgnoreCase("moneytutorialcommand")) {
				Tutorials.moneyTutorial (tPlayer,this, useChat(tPlayer));
				return true;
			} else if (commandLabel.equalsIgnoreCase("kickself")) {
				if (Player instanceof Player) {
					tPlayer.kickPlayer(ChatColor.GREEN + "Please rejoin to be able to connect to the main server.");
				} else {
					Player.sendMessage(ChatColor.RED + "You are not a Player! How would I kick you from the server?");
				}
				return true;
			} else if (commandLabel.equalsIgnoreCase("secondquestiononjoin")) {
				Other.blankLines(25, tPlayer);
		    	
				tPlayer.sendMessage(ChatColor.GOLD + "How would you like to view the tutorial?");
				TextComponent joinMessage3 = new TextComponent( ChatColor.DARK_BLUE + "I would like to view the tutorials in the chat (recommended).   |" );
			
				joinMessage3.setClickEvent(new ClickEvent ( ClickEvent.Action.RUN_COMMAND, "/usetextfortutorials" ));
				joinMessage3.setHoverEvent(new HoverEvent ( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here.").create() ) );
			
			
				TextComponent joinMessage4 = new TextComponent( ChatColor.DARK_RED + "|   I would like to view the tutorials on signs (not recommended)." );
			
				joinMessage4.setClickEvent(new ClickEvent ( ClickEvent.Action.RUN_COMMAND, "/usesignsfortutorials" ));
				joinMessage4.setHoverEvent(new HoverEvent ( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here.").create() ) );
			
				tPlayer.spigot().sendMessage(joinMessage3);
			
				tPlayer.spigot().sendMessage( joinMessage4 );
				return true;
			} else if (commandLabel.equalsIgnoreCase("usetextfortutorials")) {
				this.tutorialChoice.remove(sign);
				this.tutorialChoice.remove(chat);
				
				this.tutorialChoice.add(chat);
				tPlayer.sendMessage(ChatColor.GOLD + "You have selected to view the tutorial in chat!");
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					
				    public void run() {
						
						Tutorials.testTutorial(tPlayer, plugin, true, useChat(tPlayer));
						
				    }
					 
				}, 60L );	
				return true;
			} else if (commandLabel.equalsIgnoreCase("usesignsfortutorials")) {
				
				this.tutorialChoice.remove(sign);
				this.tutorialChoice.remove(chat);
				
				this.tutorialChoice.add(sign);
				tPlayer.sendMessage(ChatColor.GOLD + "You have selected to view the tutorial on signs!");
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					
				    public void run() {
						
						Tutorials.testTutorial(tPlayer, plugin, true, useChat(tPlayer));
						
				    }
					 
				}, 60L );	
				return true;
			}
		return false;
	}
 
	public boolean useChat (Player player){
		
		String sPlayer = player.getName();
		String chat = sPlayer + " - chat";
		String sign = sPlayer + " - sign";
		
		if (this.tutorialChoice.contains(sign)){
			return false;
		} else if (this.tutorialChoice.contains(chat)) {
			return true;
		} else {
			this.tutorialChoice.remove(sign);
			this.tutorialChoice.remove(chat);
			this.tutorialChoice.add(chat);
		return true;
		}
	}

}		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	