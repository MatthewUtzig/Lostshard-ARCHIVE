package com.lostshard.Lostshard.Objects.Plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Parent;

import com.lostshard.Lostshard.Data.Variables;
import com.lostshard.Lostshard.Manager.ClanManager;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.Groups.Clan;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Utils.Output;

import me.confuser.barapi.BarAPI;

@Embeddable
@Access(AccessType.FIELD)
public class PlotCapturePoint {

	@Transient
	ClanManager cm = ClanManager.getManager();

	// CapturePoint
	private boolean capturePoint = false;
	@ManyToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	private Clan owningClan = null;
	private long lastCaptureDate = 0;
	@Transient
	private UUID capturingPlayer = null;
	@Transient
	private Clan capturingClan = null;
	@Transient
	private List<UUID> recentCaptureFails = new ArrayList<UUID>();
	@Transient
	private int claimSecRemaining = 0;
	@Transient
	private int timeoutSecRemaining = 0;
	@Transient
	private double refractoryPeriod = 0;
	@Transient
	private boolean capturedRecently = false;
	@Transient
	private int recentClaims = 0;
	@Parent
	private Plot plot;
	
	public PlotCapturePoint() {
		
	}
	
	public PlotCapturePoint(Plot plot){
		this.plot = plot;
	}

	public void beginCapture(Player player, PseudoPlayer pPlayer, Clan clan) {
		if (player == null)
			return;
		else if (pPlayer == null || clan == null)
			Output.simpleError(player, "Something went wrong.");
		else {
			if (this.recentCaptureFails.contains(player.getUniqueId())) {
				Output.simpleError(
						player,
						"You failed to capture "
								+ this.plot.getName()
								+ " recently, you must wait until the next time it is vulnerable to attempt to capture it again.");
				return;
			}
			this.capturingPlayer = player.getUniqueId();
			this.setClaimSecRemaining(Variables.claimTime);
			this.setTimeoutSecRemaining(60*8);
			this.capturingClan = clan;
			pPlayer.setClaming(true);
			BarAPI.setMessage(player, ChatColor.GREEN+"Claiming", Variables.claimTime);

			if (this.getOwningClan() != null)
				this.getOwningClan().sendMessage(
						this.plot.getName() + " is being claimed by "
								+ player.getName() + ", from the clan "
								+ this.capturingClan.getName() + "!");
			this.capturingClan.sendMessage(player.getName() + " is claiming "
					+ this.plot.getName() + " for your clan!");
			player.sendMessage(ChatColor.GOLD
					+ "You must stay alive and within " + this.plot.getName()
					+ " for "+Variables.claimTime+" seconds.");
		}
	}

	public void failCaptureDied(Player player) {
		if (!this.isUnderAttack())
			return;
		if (!this.capturingPlayer.equals(player.getUniqueId()))
			return;
		final PseudoPlayer capturingPseudoPlayer = PlayerManager.getManager().getPlayer(player);
		this.capturingPlayer = null;
		capturingPseudoPlayer.setClaming(false);
		if (this.owningClan != null)
			this.getOwningClan().sendMessage(
					player.getName() + " died and thus failed to claim "
							+ this.plot.getName() + ".");
		this.capturingClan.sendMessage(player.getName()
				+ " died and thus failed to claim " + this.plot.getName() + ".");
		this.setClaimSecRemaining(0);
		BarAPI.removeBar(player);
		this.recentCaptureFails.add(player.getUniqueId());
		this.capturingClan = null;
	}

	public void failCaptureLeft(Player player) {
		if (!this.isUnderAttack())
			return;
		if (!this.capturingPlayer.equals(player.getUniqueId()))
			return;
		this.capturingPlayer = null;
		final PseudoPlayer capturingPseudoPlayer = PlayerManager.getManager().getPlayer(player);
		capturingPseudoPlayer.setClaming(false);
		if (this.owningClan != null)
			this.getOwningClan().sendMessage(
					player.getName() + " left " + this.plot.getName()
							+ " and thus failed to claim it.");
		this.capturingClan.sendMessage(player.getName() + " left "
				+ this.plot.getName() + " and thus failed to claim it.");
		this.claimSecRemaining = 0;
		BarAPI.removeBar(player);
		this.recentCaptureFails.add(player.getUniqueId());
		this.capturingClan = null;
	}

	public Clan getCapturingClan() {
		return this.capturingClan;
	}

	public UUID getCapturingPlayer() {
		return this.capturingPlayer;
	}

	public int getClaimSecRemaining() {
		return this.claimSecRemaining;
	}

	public long getLastCaptureDate() {
		return this.lastCaptureDate;
	}

	public Clan getOwningClan() {
		return owningClan;
	}

	public List<UUID> getRecentCaptureFails() {
		return this.recentCaptureFails;
	}

	public int getRecentClaims() {
		return this.recentClaims;
	}

	public double getRefractoryPeriod() {
		return this.refractoryPeriod;
	}

	public int getTimeoutSecRemaining() {
		return this.timeoutSecRemaining;
	}

	public boolean isCapturedRecently() {
		return this.capturedRecently;
	}

	public boolean isCapturePoint() {
		return this.capturePoint;
	}

	public boolean isUnderAttack() {
		return this.capturingPlayer != null;
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
		this.plot.update();
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
	
	public Plot getPlot() {
		return this.plot;
	}
	
	public void setPlot(Plot plot) {
		this.plot = plot;
	}

	public void tick(double delta) {
		delta /= 10;

		if (this.refractoryPeriod > 0) {
			this.refractoryPeriod -= delta;
			if (this.refractoryPeriod <= 0) {
				for (final Player p : Bukkit.getOnlinePlayers())
					p.sendMessage(ChatColor.GREEN + this.plot.getName()
							+ " is now vulnerable to capture.");
				this.capturedRecently = false;
				this.recentCaptureFails.clear();
			}
		}
		// Handle the timeout. When the time runs out we clear the recent claims
		if (this.timeoutSecRemaining > 0) {
			this.timeoutSecRemaining -= delta;
			if (this.timeoutSecRemaining <= 0)
				this.recentClaims = 0;
		}

		if (this.capturingPlayer != null) {
			this.claimSecRemaining -= delta;
			if (this.claimSecRemaining <= 0) {
				this.capturingPlayer = null;
				this.recentClaims++;
				if (this.recentClaims >= 3) {
					for (final Player p : Bukkit.getOnlinePlayers())
						p.sendMessage(ChatColor.GREEN + this.plot.getName()
								+ " has been captured by "
								+ this.capturingClan.getName() + ".");
					this.recentClaims = 0;
					this.timeoutSecRemaining = 0;
					final Player ocapturingPlayer = Bukkit
							.getPlayer(this.capturingPlayer);
					this.capturingPlayer = null;
					this.setOwningClan(this.capturingClan);
					this.capturingClan = null;

					// if(this.getName().equalsIgnoreCase(GORP_PLOT_NAME))
					// ClanHandler.SetGorpControlClan(_owningClan);
					// if(this.getName().equalsIgnoreCase(BUNT_PLOT_NAME))
					// ClanHandler.SetBuntControlClan(_owningClan);

					if (this.capturingPlayer != null) {
						final PseudoPlayer capturingPseudoPlayer = PlayerManager.getManager()
								.getPlayer(ocapturingPlayer);
						capturingPseudoPlayer.setClaming(false);
					}
					this.capturedRecently = true;
					this.refractoryPeriod = 60 * 60;
					final Date date = new Date();
					this.lastCaptureDate = date.getTime();
				} else {
					final int timesLeft = 3 - this.recentClaims;
					if (this.owningClan != null)
						this.getOwningClan()
								.sendMessage(
										this.plot.getName()
												+ " has been claimed "
												+ this.recentClaims
												+ " time. It will be captured if it is claimed "
												+ timesLeft
												+ " more time. If it is not claimed again in the next 8 minutes it will reset.");

					if (this.capturingClan != null)
						this.getCapturingClan()
						.sendMessage(
								this.plot.getName()
										+ " has been claimed "
										+ this.recentClaims
										+ " time. It will be captured if it is claimed "
										+ timesLeft
										+ " more time. If it is not claimed again in the next 8 minutes it will reset.");
							
							
					final Player ocapturingPlayer = Bukkit
							.getPlayer(this.capturingPlayer);
					if (this.capturingPlayer != null) {
						final PseudoPlayer capturingPseudoPlayer = PlayerManager.getManager()
								.getPlayer(ocapturingPlayer);
						capturingPseudoPlayer.setClaming(false);
					}
				}
			}
		}
	}
}
