package com.lostshard.lostshard.Objects.Plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Manager.ClanManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Utils.Output;

public class PlotCapturePoint extends Plot {

	ClanManager cm = ClanManager.getManager();
	
	// CapturePoint
	private boolean capturePoint = false;
	private Clan owningClan = null;
	private long lastCaptureDate = 0;
	private UUID capturingPlayer = null;
	private Clan capturingClan = null;
	private List<UUID> recentCaptureFails = new ArrayList<UUID>();
	private int claimSecRemaining = 0;
	private int timeoutSecRemaining = 0;
	private double refractoryPeriod = 0;
	private boolean capturedRecently = false;
	private int recentClaims = 0;
	
	public PlotCapturePoint(int id, String name, UUID owner, Location location) {
		super(id, name, owner, location);
	}

	public void beginCapture(Player player, PseudoPlayer pPlayer, Clan clan) {
		if(player == null)
			return;
		else if(pPlayer == null || clan == null) {
			Output.simpleError(player,  "Something went wrong.");
		} else {
			if(recentCaptureFails.contains(player.getUniqueId())) {
				Output.simpleError(player, "You failed to capture "+getName()+" recently, you must wait until the next time it is vulnerable to attempt to capture it again.");
				return;
			}
			capturingPlayer = player.getUniqueId();
			setClaimSecRemaining(120);
			setTimeoutSecRemaining(8*60); // seconds
			capturingClan = clan;
			pPlayer.setClaming(true);
			
			if(getOwningClan() != null)
				getOwningClan().sendMessage(getName()+" is being claimed by "+player.getName()+", from the clan "+capturingClan.getName()+"!");
			capturingClan.sendMessage(player.getName()+" is claiming "+this.getName()+" for your clan!");
    		player.sendMessage(ChatColor.GOLD+"You must stay alive and within "+this.getName()+" for 120 seconds.");
		}
	}

	public void failCaptureDied(Player player) {
		if(!isUnderAttack())
			return;
		if(!capturingPlayer.equals(player.getUniqueId()))
			return;
		PseudoPlayer capturingPseudoPlayer = pm.getPlayer(player);
		capturingPlayer = null;
		capturingPseudoPlayer.setClaming(false);
		if(owningClan != null)
			getOwningClan().sendMessage(player.getName()+" died and thus failed to claim "+this.getName()+".");
		capturingClan.sendMessage(player.getName()+" died and thus failed to claim "+this.getName()+".");
		setClaimSecRemaining(0);
		recentCaptureFails.add(player.getUniqueId());
		capturingClan = null;
	}
	
	public void failCaptureLeft(Player player) {
		if(!isUnderAttack())
			return;
		if(!capturingPlayer.equals(player.getUniqueId()))
			return;
		capturingPlayer = null;
		PseudoPlayer capturingPseudoPlayer = pm.getPlayer(player);
		capturingPseudoPlayer.setClaming(false);
		if(owningClan != null)
			getOwningClan().sendMessage(player.getName()+" left "+this.getName()+" and thus failed to claim it.");
		capturingClan.sendMessage(player.getName()+" left "+this.getName()+" and thus failed to claim it.");
		claimSecRemaining = 0;
		recentCaptureFails.add(player.getUniqueId());
		capturingClan = null;
	}

	public Clan getCapturingClan() {
		return capturingClan;
	}

	public UUID getCapturingPlayer() {
		return capturingPlayer;
	}

	public int getClaimSecRemaining() {
		return claimSecRemaining;
	}

	public long getLastCaptureDate() {
		return lastCaptureDate;
	}

	public Clan getOwningClan() {
		for(Clan c : cm.getClans())
			if(c.equals(capturingClan))
				return c;
		this.owningClan = null;
		return null;
	}

	public List<UUID> getRecentCaptureFails() {
		return recentCaptureFails;
	}

	public int getRecentClaims() {
		return recentClaims;
	}

	public double getRefractoryPeriod() {
		return refractoryPeriod;
	}

	public int getTimeoutSecRemaining() {
		return timeoutSecRemaining;
	}

	public boolean isCapturedRecently() {
		return capturedRecently;
	}

	public boolean isCapturePoint() {
		return capturePoint;
	}

	public boolean isUnderAttack() {
		return capturingPlayer != null;
	}

	public void setCapturedRecently(boolean capturedRecently) {
		this.capturedRecently = capturedRecently;
	}

	public void setCapturePoint(boolean capturePoint) {
		this.capturePoint = capturePoint;
	}

	public void setCapturingClan(Clan capturingClan) {
		this.capturingClan = capturingClan;
	}

	public void setCapturingPlayer(UUID capturingPlayer) {
		this.capturingPlayer = capturingPlayer;
	}

	public void setClaimSecRemaining(int claimSecRemaining) {
		this.claimSecRemaining = claimSecRemaining;
	}

	public void setLastCaptureDate(long lastCaptureDate) {
		this.lastCaptureDate = lastCaptureDate;
	}

	public void setOwningClan(Clan owningClan) {
		this.owningClan = owningClan;
		update();
	}
	
	public void setRecentCaptureFails(List<UUID> recentCaptureFails) {
		this.recentCaptureFails = recentCaptureFails;
	}
	
	public void setRecentClaims(int recentClaims) {
		this.recentClaims = recentClaims;
	}
	
	public void setRefractoryPeriod(double refractoryPeriod) {
		this.refractoryPeriod = refractoryPeriod;
	}
	
	public void setTimeoutSecRemaining(int timeoutSecRemaining) {
		this.timeoutSecRemaining = timeoutSecRemaining;
	}
	
	public void tick(double delta) {
		delta /= 10;
		
		if(refractoryPeriod > 0) {
			refractoryPeriod -= delta;
			if(refractoryPeriod <= 0) {
				for(Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage(ChatColor.GREEN+this.getName()+" is now vulnerable to capture.");
				}
				capturedRecently = false;
				recentCaptureFails.clear();
			}
		}
		// Handle the timeout. When the time runs out we clear the recent claims
		if(timeoutSecRemaining > 0) {
			timeoutSecRemaining -= delta;
			if(timeoutSecRemaining <= 0) {
				recentClaims = 0;
			}
		}
		
		if(capturingPlayer != null) {					
			claimSecRemaining -= delta;
			if(claimSecRemaining <= 0) {
				capturingPlayer = null;
				recentClaims++;
				if(recentClaims >= 3) {						
					for(Player p : Bukkit.getOnlinePlayers()) {
						p.sendMessage(ChatColor.GREEN+this.getName()+" has been captured by "+capturingClan.getName()+".");
					}
					recentClaims = 0;
					timeoutSecRemaining = 0;
					Player ocapturingPlayer = Bukkit.getPlayer(capturingPlayer);
					capturingPlayer = null;
					setOwningClan(capturingClan);
					capturingClan = null;
					
//					if(this.getName().equalsIgnoreCase(GORP_PLOT_NAME))
//						ClanHandler.SetGorpControlClan(_owningClan);
//					if(this.getName().equalsIgnoreCase(BUNT_PLOT_NAME))
//						ClanHandler.SetBuntControlClan(_owningClan);
					
					if(capturingPlayer != null) {
						PseudoPlayer capturingPseudoPlayer = pm.getPlayer(ocapturingPlayer);
						capturingPseudoPlayer.setClaming(false);
					}
					capturedRecently = true;
					refractoryPeriod = 60*60;
					Date date = new Date();
					this.lastCaptureDate = date.getTime();
					Database.updatePlot(this);
				}
				else {
					int timesLeft = 3 - recentClaims;
					if(owningClan != null) {
						if(recentClaims == 1) {
							if(timesLeft == 1)
								getOwningClan().sendMessage(this.getName()+" has been claimed "+recentClaims+" time. It will be captured if it is claimed "+timesLeft+" more time. If it is not claimed again in the next 8 minutes it will reset.");
							else
								getOwningClan().sendMessage(this.getName()+" has been claimed "+recentClaims+" time. It will be captured if it is claimed "+timesLeft+" more times. If it is not claimed again in the next 8 minutes it will reset.");
						}
						else {
							if(timesLeft == 1)
								getOwningClan().sendMessage(this.getName()+" has been claimed "+recentClaims+" times. It will be captured if it is claimed "+timesLeft+" more time. If it is not claimed again in the next 8 minutes it will reset.");
							else
								getOwningClan().sendMessage(this.getName()+" has been claimed "+recentClaims+" times. It will be captured if it is claimed "+timesLeft+" more times. If it is not claimed again in the next 8 minutes it will reset.");
						}
					}
					
					if(capturingClan != null) {
						if(recentClaims == 1) {
							if(timesLeft == 1)
								capturingClan.sendMessage(this.getName()+" has been claimed "+recentClaims+" time. It will be captured if it is claimed "+timesLeft+" more time. If it is not claimed again in the next 8 minutes it will reset.");
							else
								capturingClan.sendMessage(this.getName()+" has been claimed "+recentClaims+" time. It will be captured if it is claimed "+timesLeft+" more times. If it is not claimed again in the next 8 minutes it will reset.");
						}
						else {
							if(timesLeft == 1)
								capturingClan.sendMessage(this.getName()+" has been claimed "+recentClaims+" times. It will be captured if it is claimed "+timesLeft+" more time. If it is not claimed again in the next 8 minutes it will reset.");
							else
							capturingClan.sendMessage(this.getName()+" has been claimed "+recentClaims+" times. It will be captured if it is claimed "+timesLeft+" more times. If it is not claimed again in the next 8 minutes it will reset.");
						}
					}
					Player ocapturingPlayer = Bukkit.getPlayer(capturingPlayer);
					if(capturingPlayer != null) {
						PseudoPlayer capturingPseudoPlayer = pm.getPlayer(ocapturingPlayer);
						capturingPseudoPlayer.setClaming(false);
					}
				}
			}
		}
	}
}
