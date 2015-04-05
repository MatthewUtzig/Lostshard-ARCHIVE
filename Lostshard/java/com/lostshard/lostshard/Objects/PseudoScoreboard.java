package com.lostshard.lostshard.Objects;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.lostshard.lostshard.Manager.PlayerManager;

public class PseudoScoreboard {
	
	static PlayerManager pm = PlayerManager.getManager();
	
	protected static ScoreboardManager manager = Bukkit.getScoreboardManager();
	
	protected Scoreboard board = manager.getNewScoreboard();
	
	private UUID playerUUID;
	
	protected Team murder = board.registerNewTeam("murder");
	protected Team criminal = board.registerNewTeam("criminal");
	protected Team lawfull = board.registerNewTeam("lawfull");
	
	protected Objective stats = board.registerNewObjective("stats", "dummy");
	
	protected Score gc = stats.getScore(ChatColor.YELLOW+"Gold Coins:");
	protected Score mana = stats.getScore(ChatColor.YELLOW+"Mana:");
	protected Score stamina = stats.getScore(ChatColor.YELLOW+"Stamina:");
	protected Score build = stats.getScore(ChatColor.YELLOW+"Build:");
	protected Score mc = stats.getScore(ChatColor.YELLOW+"Murder Counts:");
	protected Score rank = stats.getScore(ChatColor.YELLOW+"Rank:");
	
	public PseudoScoreboard(UUID uuid) {
		
		this.playerUUID = uuid;
		
		Player player = Bukkit.getPlayer(uuid);
		if(player == null)
			return;
		
		PseudoPlayer pPlayer = pm.getPlayer(player);
		
		stats.setDisplayName(ChatColor.GOLD+"-"+"Your"+" Statistics-");
		
		gc.setScore(pPlayer.getMoney());
		
		mana.setScore(pPlayer.getMana());
		
		stamina.setScore(pPlayer.getStamina());
		
		build.setScore(pPlayer.getCurrentBuildId());
		
		mc.setScore(pPlayer.getMurderCounts());
		
		rank.setScore(pPlayer.getRank());
		
		player.setScoreboard(board);
		stats.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		murder.setPrefix(ChatColor.RED + "");
		criminal.setPrefix(ChatColor.GRAY + "");
		lawfull.setPrefix(ChatColor.BLUE + "");
		
		lawfull.setCanSeeFriendlyInvisibles(false);
		criminal.setCanSeeFriendlyInvisibles(false);
		murder.setCanSeeFriendlyInvisibles(false);
		
		lawfull.setAllowFriendlyFire(true);
		criminal.setAllowFriendlyFire(true);
		murder.setAllowFriendlyFire(true);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			PseudoPlayer tpPlayer = pm.getPlayer(p);
			if(tpPlayer.isMurderer())
				murder.addPlayer(p);
			else if(tpPlayer.isCriminal())
				criminal.addPlayer(p);
			else
				lawfull.addPlayer(p);
			if(tpPlayer.getScoreboard() != null) {
				if(pPlayer.isMurderer())
					tpPlayer.getScoreboard().murder.addPlayer(player);
				if(pPlayer.isCriminal())
					tpPlayer.getScoreboard().criminal.addPlayer(player);
				else
					tpPlayer.getScoreboard().lawfull.addPlayer(player);
			}
		}
	}
	
	public void updateTeams() {
		PseudoPlayer pPlayer = pm.getPlayer(playerUUID);
		Player player = Bukkit.getPlayer(playerUUID);
		if(pPlayer.isMurderer()) {
			for(PseudoPlayer pP : pm.getPlayers()) {
				if(pP.getScoreboard() != null) {
					pP.getScoreboard().murder.addPlayer(player);
				}
			}
		}else if(pPlayer.isCriminal()){
			for(PseudoPlayer pP : pm.getPlayers()) {
				if(pP.getScoreboard() != null) {
					pP.getScoreboard().criminal.addPlayer(player);
				}
			}
		}else{
			for(PseudoPlayer pP : pm.getPlayers()) {
				if(pP.getScoreboard() != null) {
					pP.getScoreboard().lawfull.addPlayer(player);
				}
			}
		}
	}
	
	public void updateMoney(int money) {
		gc.setScore(money);
	}
	
	public void updateMana(int mana) {
		this.mana.setScore(mana);
	}
	
	public void updateStamina(int stamina) {
		this.stamina.setScore(stamina);
	}
	
	public void updateBuild(int build) {
		this.build.setScore(build);
	}
	
	public void updateMurderCounts(int murderCounts) {
		this.mc.setScore(murderCounts);
	}
	
	public void updateRank(int rank) {
		this.rank.setScore(rank);
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID) {
		this.playerUUID = playerUUID;
	}
	
}
