package com.lostshard.Lostshard.Objects.Plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Database.Mappers.PlotMapper;
import com.lostshard.Lostshard.Manager.ClanManager;
import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Groups.Clan;
import com.lostshard.Lostshard.Utils.Output;

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
		if (player == null)
			return;
		else if (pPlayer == null || clan == null)
			Output.simpleError(player, "Something went wrong.");
		else {
			if (this.recentCaptureFails.contains(player.getUniqueId())) {
				Output.simpleError(
						player,
						"You failed to capture "
								+ this.getName()
								+ " recently, you must wait until the next time it is vulnerable to attempt to capture it again.");
				return;
			}
			this.capturingPlayer = player.getUniqueId();
			this.setClaimSecRemaining(120);
			this.setTimeoutSecRemaining(8 * 60); // seconds
			this.capturingClan = clan;
			pPlayer.setClaming(true);

			if (this.getOwningClan() != null)
				this.getOwningClan().sendMessage(
						this.getName() + " is being claimed by "
								+ player.getName() + ", from the clan "
								+ this.capturingClan.getName() + "!");
			this.capturingClan.sendMessage(player.getName() + " is claiming "
					+ this.getName() + " for your clan!");
			player.sendMessage(ChatColor.GOLD
					+ "You must stay alive and within " + this.getName()
					+ " for 120 seconds.");
		}
	}

	public void failCaptureDied(Player player) {
		if (!this.isUnderAttack())
			return;
		if (!this.capturingPlayer.equals(player.getUniqueId()))
			return;
		final PseudoPlayer capturingPseudoPlayer = this.pm.getPlayer(player);
		this.capturingPlayer = null;
		capturingPseudoPlayer.setClaming(false);
		if (this.owningClan != null)
			this.getOwningClan().sendMessage(
					player.getName() + " died and thus failed to claim "
							+ this.getName() + ".");
		this.capturingClan.sendMessage(player.getName()
				+ " died and thus failed to claim " + this.getName() + ".");
		this.setClaimSecRemaining(0);
		this.recentCaptureFails.add(player.getUniqueId());
		this.capturingClan = null;
	}

	public void failCaptureLeft(Player player) {
		if (!this.isUnderAttack())
			return;
		if (!this.capturingPlayer.equals(player.getUniqueId()))
			return;
		this.capturingPlayer = null;
		final PseudoPlayer capturingPseudoPlayer = this.pm.getPlayer(player);
		capturingPseudoPlayer.setClaming(false);
		if (this.owningClan != null)
			this.getOwningClan().sendMessage(
					player.getName() + " left " + this.getName()
							+ " and thus failed to claim it.");
		this.capturingClan.sendMessage(player.getName() + " left "
				+ this.getName() + " and thus failed to claim it.");
		this.claimSecRemaining = 0;
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
		for (final Clan c : this.cm.getClans())
			if (c.equals(this.capturingClan))
				return c;
		this.owningClan = null;
		return null;
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
		this.update();
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

		if (this.refractoryPeriod > 0) {
			this.refractoryPeriod -= delta;
			if (this.refractoryPeriod <= 0) {
				for (final Player p : Bukkit.getOnlinePlayers())
					p.sendMessage(ChatColor.GREEN + this.getName()
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
						p.sendMessage(ChatColor.GREEN + this.getName()
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
						final PseudoPlayer capturingPseudoPlayer = this.pm
								.getPlayer(ocapturingPlayer);
						capturingPseudoPlayer.setClaming(false);
					}
					this.capturedRecently = true;
					this.refractoryPeriod = 60 * 60;
					final Date date = new Date();
					this.lastCaptureDate = date.getTime();
					PlotMapper.updatePlot(this);
				} else {
					final int timesLeft = 3 - this.recentClaims;
					if (this.owningClan != null)
						if (this.recentClaims == 1) {
							if (timesLeft == 1)
								this.getOwningClan()
										.sendMessage(
												this.getName()
														+ " has been claimed "
														+ this.recentClaims
														+ " time. It will be captured if it is claimed "
														+ timesLeft
														+ " more time. If it is not claimed again in the next 8 minutes it will reset.");
							else
								this.getOwningClan()
										.sendMessage(
												this.getName()
														+ " has been claimed "
														+ this.recentClaims
														+ " time. It will be captured if it is claimed "
														+ timesLeft
														+ " more times. If it is not claimed again in the next 8 minutes it will reset.");
						} else if (timesLeft == 1)
							this.getOwningClan()
									.sendMessage(
											this.getName()
													+ " has been claimed "
													+ this.recentClaims
													+ " times. It will be captured if it is claimed "
													+ timesLeft
													+ " more time. If it is not claimed again in the next 8 minutes it will reset.");
						else
							this.getOwningClan()
									.sendMessage(
											this.getName()
													+ " has been claimed "
													+ this.recentClaims
													+ " times. It will be captured if it is claimed "
													+ timesLeft
													+ " more times. If it is not claimed again in the next 8 minutes it will reset.");

					if (this.capturingClan != null)
						if (this.recentClaims == 1) {
							if (timesLeft == 1)
								this.capturingClan
										.sendMessage(this.getName()
												+ " has been claimed "
												+ this.recentClaims
												+ " time. It will be captured if it is claimed "
												+ timesLeft
												+ " more time. If it is not claimed again in the next 8 minutes it will reset.");
							else
								this.capturingClan
										.sendMessage(this.getName()
												+ " has been claimed "
												+ this.recentClaims
												+ " time. It will be captured if it is claimed "
												+ timesLeft
												+ " more times. If it is not claimed again in the next 8 minutes it will reset.");
						} else if (timesLeft == 1)
							this.capturingClan
									.sendMessage(this.getName()
											+ " has been claimed "
											+ this.recentClaims
											+ " times. It will be captured if it is claimed "
											+ timesLeft
											+ " more time. If it is not claimed again in the next 8 minutes it will reset.");
						else
							this.capturingClan
									.sendMessage(this.getName()
											+ " has been claimed "
											+ this.recentClaims
											+ " times. It will be captured if it is claimed "
											+ timesLeft
											+ " more times. If it is not claimed again in the next 8 minutes it will reset.");
					final Player ocapturingPlayer = Bukkit
							.getPlayer(this.capturingPlayer);
					if (this.capturingPlayer != null) {
						final PseudoPlayer capturingPseudoPlayer = this.pm
								.getPlayer(ocapturingPlayer);
						capturingPseudoPlayer.setClaming(false);
					}
				}
			}
		}
	}
}
