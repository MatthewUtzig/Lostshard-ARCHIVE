package com.lostshard.Data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Variables {

	//Plot
	private static ItemStack plotItemPrice = new ItemStack(Material.DIAMOND, 1);
	private static int plotCreatePrice = 1000;
	private static int plotTownPrice = 100000;
	private static int plotDungeonPrice = 20000;
	private static int plotAutoKickPrice = 10000;
	private static int plotNeutralAlignmentPrice = 10000;
	private static int plotExpandPrice = 10;
	//Vote
	private static int voteMoney = 1000;
	//Bank
	private static int bankRadius = 10;
	private static int goldIngotValue = 100;
	//Database
	private static String mysqlHost = "jdbc:mysql://";
	private static String mysqlUsername = "username";
	private static String mysqlPassword = "password";
	//Server
	private static String motd = "MOTD, forgot to insert!";
	private static int maxPlayers = 30;
	//Map
	private static Location criminalSpawn;
	private static Location lawfullSpawn;
	//Karma
	private static int murderPoint = 5;
	//NPC
	private static int guardRange = 15;
	
	
	public static int getPlotCreatePrice() {
		return plotCreatePrice;
	}
	public static void setPlotCreatePrice(int plotMoneyPrice) {
		Variables.plotCreatePrice = plotMoneyPrice;
	}
	public static ItemStack getPlotItemPrice() {
		return plotItemPrice;
	}
	public static void setPlotItemPrice(ItemStack plotItemPrice) {
		Variables.plotItemPrice = plotItemPrice;
	}
	public static int getPlotTownPrice() {
		return plotTownPrice;
	}
	public static void setPlotTownPrice(int plotTownPrice) {
		Variables.plotTownPrice = plotTownPrice;
	}
	public static int getPlotDungeonPrice() {
		return plotDungeonPrice;
	}
	public static void setPlotDungeonPrice(int plotDungeonPrice) {
		Variables.plotDungeonPrice = plotDungeonPrice;
	}
	public static int getPlotAutoKickPrice() {
		return plotAutoKickPrice;
	}
	public static void setPlotAutoKickPrice(int plotAutoKickPrice) {
		Variables.plotAutoKickPrice = plotAutoKickPrice;
	}
	public static int getPlotNeutralAlignmentPrice() {
		return plotNeutralAlignmentPrice;
	}
	public static void setPlotNeutralAlignmentPrice(int plotNeutralAlignment) {
		Variables.plotNeutralAlignmentPrice = plotNeutralAlignment;
	}
	public static int getVoteMoney() {
		return voteMoney;
	}
	public static void setVoteMoney(int voteMoney) {
		Variables.voteMoney = voteMoney;
	}
	public static int getPlotExpandPrice() {
		return plotExpandPrice;
	}
	public static void setPlotExpandPrice(int plotExpandPrice) {
		Variables.plotExpandPrice = plotExpandPrice;
	}
	public static String getMysqlHost() {
		return mysqlHost;
	}
	public static void setMysqlHost(String mysqlHost) {
		Variables.mysqlHost = mysqlHost;
	}
	public static String getMysqlUsername() {
		return mysqlUsername;
	}
	public static void setMysqlUsername(String mysqlUsername) {
		Variables.mysqlUsername = mysqlUsername;
	}
	public static String getMysqlPassword() {
		return mysqlPassword;
	}
	public static void setMysqlPassword(String mysqlPassword) {
		Variables.mysqlPassword = mysqlPassword;
	}
	public static int getBankRadius() {
		return bankRadius;
	}
	public static void setBankRadius(int bankRadius) {
		Variables.bankRadius = bankRadius;
	}
	public static int getGoldIngotValue() {
		return goldIngotValue;
	}
	public static void setGoldIngotValue(int goldIngotValue) {
		Variables.goldIngotValue = goldIngotValue;
	}
	public static String getMotd() {
		return motd;
	}
	public static void setMotd(String motd) {
		Variables.motd = motd;
	}
	public static int getMaxPlayers() {
		return maxPlayers;
	}
	public static void setMaxPlayers(int maxPlayers) {
		Variables.maxPlayers = maxPlayers;
	}
	public static int getMurderPoint() {
		return murderPoint;
	}
	public static void setMurderPoint(int murderPoint) {
		Variables.murderPoint = murderPoint;
	}
	public static Location getCriminalSpawn() {
		return criminalSpawn;
	}
	public static void setCriminalSpawn(Location criminalSpawn) {
		Variables.criminalSpawn = criminalSpawn;
	}
	public static Location getLawfullSpawn() {
		return lawfullSpawn;
	}
	public static void setLawfullSpawn(Location lawfullSpawn) {
		Variables.lawfullSpawn = lawfullSpawn;
	}
	public static int getGuardRange() {
		return guardRange;
	}
	public static void setGuardRange(int guardRange) {
		Variables.guardRange = guardRange;
	}

}
