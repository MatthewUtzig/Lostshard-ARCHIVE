package com.lostshard.Lostshard.Main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import com.lostshard.Lostshard.Database.Mappers.ClanMapper;
import com.lostshard.Lostshard.Database.Mappers.PlayerMapper;
import com.lostshard.Lostshard.Database.Mappers.PlotMapper;
import com.lostshard.Lostshard.Manager.ChestRefillManager;
import com.lostshard.Lostshard.Manager.ClanManager;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Groups.Clan;
import com.lostshard.Lostshard.Objects.Plot.Plot;

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
