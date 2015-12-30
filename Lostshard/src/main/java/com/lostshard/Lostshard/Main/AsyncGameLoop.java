package com.lostshard.Lostshard.Main;

import org.bukkit.scheduler.BukkitRunnable;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.lostshard.Lostshard.Manager.ChestRefillManager;
import com.lostshard.Lostshard.Manager.ClanManager;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Objects.Groups.Clan;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Plot.Plot;

public class AsyncGameLoop extends BukkitRunnable {

	PlayerManager pm = PlayerManager.getManager();
	PlotManager ptm = PlotManager.getManager();
	ClanManager cm = ClanManager.getManager();
	ChestRefillManager crm = ChestRefillManager.getManager();
	
	@Override
	public void run() {
//		long time = System.nanoTime();
		Session s = Lostshard.getSession();
		Transaction t = s.beginTransaction();
		t.begin();
		for (final PseudoPlayer p : pm.getPlayers())
			if (p.isUpdate()) {
				s.update(p);
				p.setUpdate(false);
			}
		for (final Plot p : ptm.getPlots())
			if (p.isUpdate()) {
				s.update(p);
				p.setUpdate(false);
			}
		for (final Clan c : cm.getClans())
			if (c.isUpdate()) {
				s.update(c);
				c.setUpdate(false);
			}
		t.commit();
		s.close();
		if(Lostshard.isDebug()) {
//			long delay = System.nanoTime()-time;
//			if(delay >= 10000)
//				Lostshard.log.warning("AsyncGameloop: "+delay);
		}
	}
}
