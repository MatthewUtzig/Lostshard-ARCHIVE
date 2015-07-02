package com.lostshard.lostshard.Tasks;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Manager.TaskManager;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Recent.RecentAttacker;

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
	
	@Override
	public void run() {
		if(this.target == null) {
			guard.teleport(guard.getLocation(), TeleportCause.PLUGIN);
			return;
		}
		Player target = Bukkit.getPlayer(this.target);
		if(ptm.findPlotAt(target.getLocation()) == guard.getPlot()) {
			PseudoPlayer pPlayer = pm.getPlayer(target);
			if((pPlayer.isMurderer() || pPlayer.isCriminal()) && target.getGameMode().equals(GameMode.SURVIVAL) && !target.isDead()) {
				guard.teleport(target.getLocation(), TeleportCause.PLUGIN);
				pPlayer.addRecentAttacker(new RecentAttacker(guard.getUuid(), 100));
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
	
	public UUID getTarget() {
		return target;
	}
	
	public NPC getGuard() {
		return guard;
	}
}
