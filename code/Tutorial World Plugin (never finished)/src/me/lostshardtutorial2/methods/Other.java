package me.lostshardtutorial2.methods;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Other {
	
	
	
	
	//blankLines
	
			//For when I want to display multiple blank lines to the player.
			//For example, one part of displayTutorialDelay need 10 blank lines
			//It shortens the code.
			
	public static void blankLines( int amount, Player tPlayer) {
		
			while (amount > 0) {
				tPlayer.sendMessage(" ");
			--amount;
			}
	}
			
	
	
	
	//I did not want to type "===========================================" each time
	public static void displayTutorialBorder(String color, Player tPlayer, boolean bold) {
		displayText(color,"===========================================",tPlayer, bold);	
	}
	
	
	
	
	
	
	public static void displayText (String textColor, String text, Player tPlayer, boolean bold) {
		//I wanted to have an argument in my method that displays the tutorial text that allows the color to be set.
		//I tried doing ChatColor.( A sting name ), but it did not work. 
		//I also added the ability to make the text bold or not.
				
				
				
				if (bold == true) {
					if (textColor.equalsIgnoreCase("AQUA")) {
						tPlayer.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + text);
					} else if (textColor.equalsIgnoreCase("BLACK")) {
						tPlayer.sendMessage(ChatColor.BLACK + "" + ChatColor.BOLD + text);
					} else if (textColor.equalsIgnoreCase("BLUE")) {
						tPlayer.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + text);
					} else if (textColor.equalsIgnoreCase("DARK_BLUE")) {
						tPlayer.sendMessage(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + text);
					} else if (textColor.equalsIgnoreCase("DARK_AQUA")) {
						tPlayer.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + text);
					} else if (textColor.equalsIgnoreCase("DARK_GRAY")) {
						tPlayer.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + text);
					} else if (textColor.equalsIgnoreCase("DARK_GREEN")) {
						tPlayer.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + text);
					} else if (textColor.equalsIgnoreCase("DARK_PURPLE")) {
						tPlayer.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + text);
					} else if (textColor.equalsIgnoreCase("DARK_RED")) {
						tPlayer.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + text);
					} else if (textColor.equalsIgnoreCase("GOLD")) {
						tPlayer.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + text);
					} else if (textColor.equalsIgnoreCase("GRAY")) {
						tPlayer.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + text);
					} else if (textColor.equalsIgnoreCase("GREEN")) {
						tPlayer.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + text);
					} else if (textColor.equalsIgnoreCase("LIGHT_PURPLE")) {
						tPlayer.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + text);
					} else if (textColor.equalsIgnoreCase("RED")) {
						tPlayer.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + text);
					} else if (textColor.equalsIgnoreCase("WHITE")) {
						tPlayer.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + text);
					} else if (textColor.equalsIgnoreCase("YELLOW")) {
						tPlayer.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + text);
					}	else {
						tPlayer.sendMessage(ChatColor.RED + "" + ChatColor.UNDERLINE + text);
						tPlayer.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "The underlined text above has a specified color that is invalid. Please tell an admin about this.");
					}
				} else {	
					
					if (textColor.equalsIgnoreCase("AQUA")) {
						tPlayer.sendMessage(ChatColor.AQUA + text);
					} else if (textColor.equalsIgnoreCase("BLACK")) {
						tPlayer.sendMessage(ChatColor.BLACK + text);
					} else if (textColor.equalsIgnoreCase("BLUE")) {
						tPlayer.sendMessage(ChatColor.BLUE + text);
					} else if (textColor.equalsIgnoreCase("DARK_BLUE")) {
						tPlayer.sendMessage(ChatColor.DARK_BLUE + text);
					} else if (textColor.equalsIgnoreCase("DARK_AQUA")) {
						tPlayer.sendMessage(ChatColor.DARK_AQUA + text);
					} else if (textColor.equalsIgnoreCase("DARK_GRAY")) {
						tPlayer.sendMessage(ChatColor.DARK_GRAY + text);
					} else if (textColor.equalsIgnoreCase("DARK_GREEN")) {
						tPlayer.sendMessage(ChatColor.DARK_GREEN + text);
					} else if (textColor.equalsIgnoreCase("DARK_PURPLE")) {
						tPlayer.sendMessage(ChatColor.DARK_PURPLE + text);
					} else if (textColor.equalsIgnoreCase("DARK_RED")) {
						tPlayer.sendMessage(ChatColor.DARK_RED + text);
					} else if (textColor.equalsIgnoreCase("GOLD")) {
						tPlayer.sendMessage(ChatColor.GOLD + text);
					} else if (textColor.equalsIgnoreCase("GRAY")) {
						tPlayer.sendMessage(ChatColor.GRAY + text);
					} else if (textColor.equalsIgnoreCase("GREEN")) {
						tPlayer.sendMessage(ChatColor.GREEN + text);
					} else if (textColor.equalsIgnoreCase("LIGHT_PURPLE")) {
						tPlayer.sendMessage(ChatColor.LIGHT_PURPLE + text);
					} else if (textColor.equalsIgnoreCase("RED")) {
						tPlayer.sendMessage(ChatColor.RED + text);
					} else if (textColor.equalsIgnoreCase("WHITE")) {
						tPlayer.sendMessage(ChatColor.WHITE + text);
					} else if (textColor.equalsIgnoreCase("YELLOW")) {
						tPlayer.sendMessage(ChatColor.RED + text);
					} else {
						tPlayer.sendMessage(ChatColor.RED + "" + ChatColor.UNDERLINE + text);
						tPlayer.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "The underlined text above has a specified color that is invalid. Please tell an admin about this.");
					}
					
				}	
					
			}
}
