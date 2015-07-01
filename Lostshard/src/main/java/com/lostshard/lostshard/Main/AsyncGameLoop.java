package com.lostshard.lostshard.Main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import com.lostshard.lostshard.Database.Mappers.ClanMapper;
import com.lostshard.lostshard.Database.Mappers.PlayerMapper;
import com.lostshard.lostshard.Database.Mappers.PlotMapper;
import com.lostshard.lostshard.Manager.ChestRefillManager;
import com.lostshard.lostshard.Manager.ClanManager;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Plot.Plot;

public class AsyncGameLoop extends BukkitRunnable {

	PlayerManager pm = PlayerManager.getManager();
	PlotManager ptm = PlotManager.getManager();
	ClanManager cm = ClanManager.getManager();
	ChestRefillManager crm = ChestRefillManager.getManager();
	
	public static List<PseudoPlayer> playerUpdates = new ArrayList<PseudoPlayer>();

	public static List<Plot> plotUpdates = new ArrayList<Plot>();
	public static List<Clan> clanUpdates = new ArrayList<Clan>();
	
	@Override
	public void run() {
		long time = System.nanoTime();
		for (final PseudoPlayer p : pm.getPlayers())
			if (p.isUpdate())
				playerUpdates.add(p);
		if (!playerUpdates.isEmpty())
			PlayerMapper.updatePlayers(playerUpdates);
		playerUpdates.clear();
		for (final Plot p : ptm.getPlots())
			if (p.isUpdate())
				plotUpdates.add(p);
		if (!plotUpdates.isEmpty())
			PlotMapper.updatePlots(plotUpdates);
		plotUpdates.clear();
		for (final Clan c : cm.getClans())
			if (c.isUpdate())
				clanUpdates.add(c);
		if (!clanUpdates.isEmpty())
			ClanMapper.updateClans(clanUpdates);
		clanUpdates.clear();
		if(Lostshard.isDebug())
			Lostshard.log.info("AsyncGameloop: "+(System.nanoTime()-time));
	}
}
