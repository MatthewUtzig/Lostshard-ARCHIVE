package me.lostshardtutorial2.tutorials;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


import me.lostshardtutorial2.methods.Tutorialmethods;





public class Tutorials {



	public Tutorials(Main plugin){
		
	}
		public static void moneyTutorial (final Player tPlayer, final Plugin p, boolean chat) {
			
			tPlayer.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 10));
			
			Tutorialmethods.displayTutorialDelayTwoPage ( 5, tPlayer, "GOLD" , "GOLD" , false, true, 10196, 39, 10000,false, 0, 0, 0, false, 0, 0, 0,
					  "LostShard uses a virtual \"gold coins\" currency for most things.", " ", "Coins are earned from gold bars in game.",""
					  		+ "You can not exchange real life currency for gold coins.", " ", "You can trade gold into gold coins at the bank.", ""
					  				+ "This can be done by typing: /tradegold (amount), or by right clicking the banker", " ", "You can right click a banker"
					  						+ " to access your bank inventory.", " ", "This acts as an enderchest.", " ", "You have been given some gold bars.", "Use these to right click the banker for gold.",
					  						" ", "Also you will not be able to keep these gold coins :).", p, chat);
				 
			
			
		}
		//testTutorial is the main tutorial
		public static void testTutorial (final Player tPlayer, final Plugin p, boolean ClearInventory, boolean chat)  {
			
			 Tutorialmethods.tutorialCompass(tPlayer);
			 Tutorialmethods.firstTutorialBarrier(tPlayer);
			
			 int yBefore = 34;
				int zBefore = 10075;
				if (chat == false) {
					yBefore = 19;
					zBefore = 10091;
				}
				
			 
			 
			Tutorialmethods.displayTutorialDelayThreePage (5,tPlayer, "DARK_GREEN", "GOLD", false, true, 10134,yBefore, zBefore, false, 200, 200, 200,
			true,10134 ,19 , 10075 ,
			"Welcome to Lostshard!", " ","This is a basic tutorial that will only take a minute."," ","If you already know how to play, you can skip the tutorial", "by relogging."," "
			,"The area below you is the tutorial hub.", 
			"From here, you can teleport to all of the sub tutorials."," ","You have also been given a compass and a barrier."," ", "The barrier will allow you to clear your inventory."," ", "The compass will allow you to remotely start any subtutorial.", " "
			,"It is recommended that you folow the tutorials in the order"
			,"that they appear in the compass and the room.", " " , "Also, remember the rules:", "1: No exploiting (hacks, glitches, macros, etc.)" , "2: Respect the admins and do not impersonate an admin." , " " , "Typing /tutorial will bring you back to the tutorial from the main server.", p, false, 0 ,0 ,0 , false);
		
		if (ClearInventory = true) {	
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
				 
			    public void run() {
			    	Tutorialmethods.defaultTutorialWorldInventory(tPlayer, p);
			    }
			 
			}, 460L );
		}
	}
		public static void clearWarningTutorial (Player p) {
			Tutorialmethods.displayTutorialNoDelay(p,"DARK_RED","DARK_RED", true, 
					false, 0, 0, 0, " ", "Warning! This will clear your inventory!" , " ", "If you wish to do this, click the barrier again within the next ten seconds.", " ", null, null, null, true);
		}
		
		public static void clearTutorial (Player p) {
			Tutorialmethods.displayTutorialNoDelay(p,"DARK_RED","DARK_RED", true, 
					false, 0, 0, 0, " ", "Your inventory has been cleared!" , " ", null, null, null, null, null, true);
		}
}	