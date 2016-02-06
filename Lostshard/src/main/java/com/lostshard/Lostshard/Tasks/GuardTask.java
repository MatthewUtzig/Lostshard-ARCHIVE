package com.lostshard.Lostshard.Tasks;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Manager.TaskManager;
import com.lostshard.Lostshard.NPC.NPC;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.Recent.RecentAttacker;

public class GuardTask extends DelayedTask {

	private NPC guard;
	private UUID target;

	PlotManager ptm = PlotManager.getManager();
	PlayerManager pm = PlayerManager.getManager();

	public GuardTask(NPC guard, UUID target, int delay) {
		super(delay);
		this.guard = guard;
		this.target = target;
		TaskManager.getManager().add(this);
	}

	public NPC getGuard() {
		return this.guard;
	}

	public UUID getTarget() {
		return this.target;
	}

	@Override
	public void run() {
		if (this.target == null) {
			this.guard.teleport(this.guard.getLocation(), TeleportCause.PLUGIN);
			return;
		}
		final Player target = Bukkit.getPlayer(this.target);
		if (Lostshard.isVanished(target)) {
			this.guard.teleport(this.guard.getLocation(), TeleportCause.PLUGIN);
			return;
		}
		Plot plot = this.ptm.findPlotAt(target.getLocation());
		if(plot == null) {
			this.guard.teleport(this.guard.getLocation(), TeleportCause.PLUGIN);
			return;
		}
		if (plot == this.guard.getPlot()) {
			final PseudoPlayer pPlayer = this.pm.getPlayer(target);
			if ((pPlayer.isMurderer() || pPlayer.isCriminal()) && target.getGameMode().equals(GameMode.SURVIVAL)
					&& !target.isDead()) {
				this.guard.teleport(target.getLocation(), TeleportCause.PLUGIN);
				pPlayer.addRecentAttacker(new RecentAttacker(this.guard.getUUID(), 100));
				target.damage(0d);
				target.setHealth(0);
			}
		}
	}

	public void setGuard(NPC guard) {
		this.guard = guard;
	}

	public void setTarget(UUID target) {
		this.target = target;
	}
}
