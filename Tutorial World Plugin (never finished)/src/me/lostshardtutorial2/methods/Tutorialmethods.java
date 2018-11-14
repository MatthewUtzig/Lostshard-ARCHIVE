package me.lostshardtutorial2.methods;



import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Tutorialmethods {
		public static void clearInventory (Player p) {
			p.getInventory().clear();
		}
		
		public static void firstTutorialBarrier (Player p) {
			while (p.getInventory().contains(Material.BARRIER)) {	
				p.getInventory().remove(Material.BARRIER);
				p.updateInventory();
			}
			
				ItemStack clearInventory = new ItemStack (Material.BARRIER);
				ItemMeta clearInventoryMeta  = clearInventory.getItemMeta();
	
				clearInventoryMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.DARK_RED + "CLEAR INVENTORY");
	
				String[] lore = {ChatColor.DARK_RED + "WARNING! CLICKING THIS WILL CLEAR YOUR INVENTORY!"}; 
				clearInventoryMeta.setLore(Arrays.asList(lore)); 
	
				clearInventory.setItemMeta(clearInventoryMeta);
	
				p.getInventory().addItem(clearInventory); 
			
		}
		public static void tutorialCompass (Player p) {
			while (p.getInventory().contains(Material.COMPASS)) {	
				p.getInventory().remove(Material.COMPASS);
				p.updateInventory();
			}
			ItemStack tutorialCompass = new ItemStack(Material.COMPASS, 1);
			ItemMeta tutorialCompassMeta = tutorialCompass.getItemMeta();
	
			String[] lore = {ChatColor.DARK_PURPLE + "Click to view all of the tutorials."}; 
			tutorialCompassMeta.setLore(Arrays.asList(lore)); 
	
			tutorialCompassMeta.setDisplayName(ChatColor.BLUE + "Tutorials");
	
			tutorialCompass.setItemMeta(tutorialCompassMeta);

			p.getInventory().addItem(tutorialCompass);
		}
		public static void defaultTutorialWorldInventory (final Player p, Plugin plugin) {
			clearInventory (p);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run () {
				firstTutorialBarrier (p);
				}
			}, 40L );
					tutorialCompass (p);
				
		}
		
		public static void secondClearBarrier (final Player p, Plugin plugin) {
			while (p.getInventory().contains(Material.BARRIER)) {	
				p.getInventory().remove(Material.BARRIER);
				p.updateInventory();
			}
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run () {
			
					ItemStack clearInventory = new ItemStack (Material.BARRIER);
					ItemMeta clearInventoryMeta  = clearInventory.getItemMeta();
			
					String[] lore3 = {ChatColor.DARK_RED + "CLICKING AGAIN WILL CLEAR YOUR INVENTORY"}; 
					clearInventoryMeta.setLore(Arrays.asList(lore3)); 
			
					clearInventoryMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.DARK_RED + "CLICK AGAIN TO CLEAR YOUR INVENTORY!");
					clearInventory.setItemMeta(clearInventoryMeta);
					p.getInventory().addItem(clearInventory);
					}
				}, 40L );	
		}
		
		public static void displayTutorialNoDelay(Player tPLayer, String borderColor, String mainColor, boolean boldText, 
				boolean teleportPLayer, int X, int Y, int Z, String text1, String text2, String text3,String text4, String text5, String text6, String text7, String text8, boolean chat) {
			
		
			
			if (teleportPLayer == true) {
					if (X > -30000000 && X < 30000000 && Y > -30000000 && Y < 30000000 && Z > -30000000 && Z < 30000000 ){
					Location location = new Location(tPLayer.getWorld(), X, Y, Z);
					tPLayer.teleport(location);
					} else {
						tPLayer.sendMessage(ChatColor.RED + "The server attempted to teleport you to cords that were invalid, or to cords that were out of the world. Please tell an admin about this");
						
					}

				}
			if (chat == true) {	
			
				Other.blankLines(10, tPLayer);
			
			
				Other.displayTutorialBorder(borderColor,tPLayer,boldText );
			
				if (text1 != null) {
					Other.displayText(mainColor,text1,tPLayer, boldText );
				}
			
				if (text2 != null) {
					Other.displayText(mainColor,text2,tPLayer, boldText );
					}
			
				if (text3 != null) {
					Other.displayText(mainColor,text3,tPLayer, boldText );
				}
			
				if (text4 != null) {
					Other.displayText(mainColor,text4,tPLayer, boldText );
				}
			
				if (text5 != null) {
					Other.displayText(mainColor,text5,tPLayer, boldText );
					}
			
				if (text6 != null) {
					Other.displayText(mainColor,text6,tPLayer, boldText );
					}
			
				if (text7 != null) {
					Other.displayText(mainColor,text7,tPLayer, boldText );
					}
			
				if (text8 != null) {
					Other.displayText(mainColor,text8,tPLayer, boldText );
					}
			
				tPLayer.playSound(tPLayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 2);
			 
				Other.displayTutorialBorder(borderColor,tPLayer,boldText );
			}
			
		}
		
		
		
		
		
		public static void displayTutorialDelay (int delaySeconds, final Player tPLayer, final String borderColor, final String mainColor, final boolean boldText, boolean teleportPLayerBeforeText, int Xbefore, int Ybefore, int Zbefore, boolean teleportPLayerAfterText, int XAfter, int YAfter, int ZAfter,
				  final String text1, final String text2, final String text3,final String text4,final String text5, final String text6,final String text7, final String text8, Plugin p, boolean chat) {
			
			
				if (teleportPLayerBeforeText == true) {
					if (Xbefore > -30000000 && Xbefore < 30000000 && Ybefore > -30000000 && Ybefore < 30000000 && Zbefore > -30000000 && Zbefore < 30000000 ){
						Location location = new Location(tPLayer.getWorld(), Xbefore, Ybefore, Zbefore);
						tPLayer.teleport(location);
						} else {
							tPLayer.sendMessage(ChatColor.RED + "The server attempted to teleport you to cords that were invalid, or to cords that were out of the world. Please tell an admin about this.");
							
						}
					} 
				
				
				if (chat == true) {	
				
				
				
					int delay = delaySeconds * 20;
					int textLoop = 8;
					int delayAmmount = 0;
					int delayTotal = 1;
					
				
					
					while (textLoop > 0) {
						@SuppressWarnings("unused")
						boolean displayText = false;	
						if (text1 == " " && textLoop == 8) {
							displayText = true;
						} 
						else if (text2 == " " && textLoop == 7) {
							displayText = true;
						}
						
						else if (text3 == " " && textLoop == 6) {
							displayText = true;
						} 
						
						else if (text4 == " " && textLoop == 5) {
							displayText = true;
						} 
						else if (text5 == " " && textLoop == 4) {
							displayText = true;
						} 
						else if (text6 == " " && textLoop == 3) {
							displayText = true;
						} 
						else if (text7 == " " && textLoop == 2) {
							displayText = true;
						} 
						else if (textLoop == 1) {
							displayText = true;
						} else { --textLoop; }
						
						if (displayText = true) {
							displayText = false;
							++delayAmmount;
							delayTotal = delay * delayAmmount;
							if (textLoop == 8) {
								--textLoop;
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
									 
								    public void run() {
								    	tPLayer.sendMessage(ChatColor.RED + "The first line of a tutorial cannot be blank! Please tell an admin about this.");
								    }
								 
								}, delayTotal);
							}
							else if (textLoop == 7) {
								--textLoop;
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
									 
								    public void run() {
								    	Other.blankLines (15, tPLayer);
										Other.displayTutorialBorder(borderColor,tPLayer,boldText );
										Other.displayText (mainColor, text1 , tPLayer , boldText);
										Other.blankLines (7, tPLayer);
										Other.displayTutorialBorder(borderColor,tPLayer,boldText );
										tPLayer.playSound(tPLayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 2);
										
								    }
								 
								}, delayTotal);
							}
							else if (textLoop == 6) {
								--textLoop;
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
									 
								    public void run() {
								    	Other.blankLines (15, tPLayer);
								    	Other.displayTutorialBorder(borderColor,tPLayer,boldText );	
										Other.displayText (mainColor, text1 , tPLayer , boldText);
										Other.displayText (mainColor, text2 , tPLayer , boldText);
										Other.blankLines (6, tPLayer);
										Other.displayTutorialBorder(borderColor,tPLayer,boldText );
										tPLayer.playSound(tPLayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 2);
								    }
								 
								}, delayTotal);
							}
							else if (textLoop == 5) {
								--textLoop;
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
									 
								    public void run() {
								    	Other.blankLines (15, tPLayer);
										Other.displayTutorialBorder(borderColor,tPLayer,boldText );	
										Other.displayText (mainColor, text1 , tPLayer , boldText);
										Other.displayText (mainColor, text2 , tPLayer , boldText);
										Other.displayText (mainColor, text3 , tPLayer , boldText);
										Other.blankLines (5, tPLayer);
										Other.displayTutorialBorder(borderColor,tPLayer,boldText );
										tPLayer.playSound(tPLayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 2);
								    }
								 
								}, delayTotal);
							}
							else if (textLoop == 4) {
								--textLoop;
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
									 
								    
								    public void run() {
								    	Other.blankLines (15, tPLayer);
										Other.displayTutorialBorder(borderColor,tPLayer,boldText );	
										Other.displayText (mainColor, text1 , tPLayer , boldText);
										Other.displayText (mainColor, text2 , tPLayer , boldText);
										Other.displayText (mainColor, text3 , tPLayer , boldText);
										Other.displayText (mainColor, text4 , tPLayer , boldText);
										Other.blankLines (4, tPLayer);
										Other.displayTutorialBorder(borderColor,tPLayer,boldText );
										tPLayer.playSound(tPLayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 2);
										
								    }
								 
								}, delayTotal);
							}
							else if (textLoop == 3) {
								--textLoop;
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
									 
								   
								    public void run() {
								    	Other.blankLines (15, tPLayer);
										Other.displayTutorialBorder(borderColor,tPLayer,boldText );
										Other.displayText (mainColor, text1 , tPLayer , boldText);
										Other.displayText (mainColor, text2 , tPLayer , boldText);
										Other.displayText (mainColor, text3 , tPLayer , boldText);
										Other.displayText (mainColor, text4 , tPLayer , boldText);
										Other.displayText (mainColor, text5 , tPLayer , boldText);
										Other.blankLines (3, tPLayer);
										Other.displayTutorialBorder(borderColor,tPLayer,boldText );	
										tPLayer.playSound(tPLayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 2);
								    }
								 
								}, delayTotal);
							}
							else if (textLoop == 2) {
								--textLoop;
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
									 
								   
								    public void run() {
								    	Other.blankLines (15, tPLayer);
										Other.displayTutorialBorder(borderColor,tPLayer,boldText );
										Other.displayText (mainColor, text1 , tPLayer , boldText);
										Other.displayText (mainColor, text2 , tPLayer , boldText);
										Other.displayText (mainColor, text3 , tPLayer , boldText);
										Other.displayText (mainColor, text4 , tPLayer , boldText);
										Other.displayText (mainColor, text5 , tPLayer , boldText);
										Other.displayText (mainColor, text6 , tPLayer , boldText);
										Other.blankLines (2, tPLayer);
										Other.displayTutorialBorder(borderColor,tPLayer,boldText );	
										tPLayer.playSound(tPLayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 2);
								    }
								 
								}, delayTotal);
							}
							else if (textLoop == 1) {
								--textLoop;
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
									 
								    
								    public void run() {
								    	Other.blankLines (15, tPLayer);
										Other.displayTutorialBorder(borderColor,tPLayer,boldText );
										Other.displayText (mainColor, text1 , tPLayer , boldText);
										Other.displayText (mainColor, text2 , tPLayer , boldText);
										Other.displayText (mainColor, text3 , tPLayer , boldText);
										Other.displayText (mainColor, text4 , tPLayer , boldText);
										Other.displayText (mainColor, text5 , tPLayer , boldText);
										Other.displayText (mainColor, text6 , tPLayer , boldText);
										Other.displayText (mainColor, text7 , tPLayer , boldText);
										Other.displayText (mainColor, text8 , tPLayer , boldText);
										Other.displayTutorialBorder(borderColor,tPLayer,boldText );	
										tPLayer.playSound(tPLayer.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 2);
								    }
								 
								}, delayTotal);
							}

						} 	
					}
				
				if (teleportPLayerAfterText == true) {
					if (XAfter > -30000000 && XAfter < 30000000 && YAfter > -30000000 && YAfter < 30000000 && ZAfter > -30000000 && Zbefore < 30000000 ){
						Location location = new Location(tPLayer.getWorld(), XAfter, YAfter, ZAfter);
						tPLayer.teleport(location);
						} else {
							tPLayer.sendMessage(ChatColor.RED + "The server attempted to teleport you to cords that were invalid, or to cords that were out of the world. Please tell an admin about this.");
							
						}
					} 	
				}
		}
		
	

		 public static void displayTutorialDelayTwoPage ( final int delaySeconds, final Player tPLayer, final String borderColor, final String mainColor, final boolean boldText, boolean teleportPLayerBeforeText, int Xbefore, int Ybefore, int Zbefore,boolean teleportPLayerAfterFirstPage, int X1, int Y1, int Z1, final boolean teleportPLayerAfterText, final int XAfter, final int YAfter, final int ZAfter,
				  String text1, String text2, String text3,String text4, String text5, String text6, String text7, String text8, final String text9, final String text10, final String text11, final String text12, final String text13, final String text14,  final String text15, final String text16, final Plugin p, final boolean chat)  {
			 
			 
			 displayTutorialDelay (delaySeconds, tPLayer, borderColor, mainColor, boldText, teleportPLayerBeforeText, Xbefore, Ybefore, Zbefore, teleportPLayerAfterFirstPage, X1, Y1, Z1,
					  text1, text2, text3, text4, text5, text6, text7, text8, p, chat);
			 
			 int delayAmount = 0;
			  
				 if (text1 == " ") {
					 ++delayAmount;
				 }
				 if (text2 == " ") {
					 ++delayAmount;
				 }
				 if (text3 == " ") {
					 ++delayAmount;
				 }
				 if (text4 == " ") {
					 ++delayAmount;
				 }
				 if (text5 == " ") {
					 ++delayAmount;
				 }
				 if (text6 == " ") {
					 ++delayAmount;
				 }
				 if (text7 == " ") {
					 ++delayAmount;
				}
				 ++delayAmount;
			 
			  int totalDelay = delaySeconds * delayAmount * 20;
			 
			  Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable() {	
				  
			  public void run(){
			 	displayTutorialDelay (delaySeconds, tPLayer, borderColor, mainColor, boldText, false, 0, 0, 0, teleportPLayerAfterText, XAfter, YAfter, ZAfter,
			 			text9, text10, text11, text12, text13, text14, text15, text16, p, chat);
			  }
			  
			  }, totalDelay);
		 }
		
		 
		 public static void displayTutorialDelayThreePage ( final int delaySeconds, final Player tPLayer, final String borderColor, final String mainColor, final boolean boldText, 
				 boolean teleportPLayerBeforeText, int Xbefore, int Ybefore, int Zbefore,boolean teleportPLayerAfterFirstPage, int X1, int Y1, int Z1, final boolean teleportPLayerAfterText, final int XAfter, final int YAfter, final int ZAfter,
				  String text1, String text2, String text3,String text4, String text5, String text6, String text7, String text8, 
				  final String text9, final String text10, final String text11, final String text12, final String text13, final String text14,  
				  final String text15, final String text16, final String text17, final String text18, final String text19, final String text20, final String text21, 
				  final String text22,  final String text23, final String text24, final Plugin p,final boolean teleportPlayerAfterSecondPage,final int X2,final int Y2,final int Z2, final boolean chat)  {
			
			
			 displayTutorialDelay (delaySeconds, tPLayer, borderColor, mainColor, boldText, teleportPLayerBeforeText, Xbefore, Ybefore, Zbefore, teleportPLayerAfterFirstPage, X1, Y1, Z1,
					  text1, text2, text3, text4, text5, text6, text7, text8, p, chat);
			 
			 int delayAmount = 0;
			  
				 if (text1 == " ") {
					 ++delayAmount;
				 }
				 if (text2 == " ") {
					 ++delayAmount;
				 }
				 if (text3 == " ") {
					 ++delayAmount;
				 }
				 if (text4 == " ") {
					 ++delayAmount;
				 }
				 if (text5 == " ") {
					 ++delayAmount;
				 }
				 if (text6 == " ") {
					 ++delayAmount;
				 }
				 if (text7 == " ") {
					 ++delayAmount;
				}
				 ++delayAmount;
			 
			  int totalDelay = delaySeconds * delayAmount * 20;
			 
			  Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable() {	
				  
			  public void run(){
			 	displayTutorialDelay (delaySeconds, tPLayer, borderColor, mainColor, boldText, false, 0, 0, 0, teleportPlayerAfterSecondPage, X2, Y2, Z2,
			 			text9, text10, text11, text12, text13, text14, text15, text16, p, chat);
			  }
			  
			  }, totalDelay);
			  
			  if (text9 == " ") {
					 ++delayAmount;
				 }
				 if (text10 == " ") {
					 ++delayAmount;
				 }
				 if (text11 == " ") {
					 ++delayAmount;
				 }
				 if (text12 == " ") {
					 ++delayAmount;
				 }
				 if (text13 == " ") {
					 ++delayAmount;
				 }
				 if (text14 == " ") {
					 ++delayAmount;
				 }
				 if (text15 == " ") {
					 ++delayAmount;
				}
				 ++delayAmount;
			  
				 totalDelay = delaySeconds * delayAmount * 20;
			  Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable() {	
				  
				  public void run(){
				 	displayTutorialDelay (delaySeconds, tPLayer, borderColor, mainColor, boldText, false, 0, 0, 0, teleportPLayerAfterText, XAfter, YAfter, ZAfter,
				 			text17, text18, text19, text20, text21, text22, text23, text24, p, chat);
				  }
				  
				  }, totalDelay);
		 }
}

