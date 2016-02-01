package com.lostshard.Lostshard.Main;

import org.bukkit.scheduler.BukkitRunnable;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.lostshard.Lostshard.Handlers.DamageHandler;
import com.lostshard.Lostshard.Manager.ChestRefillManager;
import com.lostshard.Lostshard.Manager.ClanManager;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Objects.Groups.Clan;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.Recorders.DamageRecord;

public class AsyncGameLoop extends BukkitRunnable {

	PlayerManager pm = PlayerManager.getManager();
	PlotManager ptm = PlotManager.getManager();
	ClanManager cm = ClanManager.getManager();
	ChestRefillManager crm = ChestRefillManager.getManager();

	@Override
	public void run() {
		// long time = System.nanoTime();
		final Session s = Lostshard.getSession();
		try {
			Transaction t;
			for (final PseudoPlayer p : this.pm.getPlayers())
				try {
					t = s.beginTransaction();
					t.begin();
					if (p.isUpdate()) {
						s.update(p);
						p.setUpdate(false);
					}
					t.commit();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			for (final Plot p : this.ptm.getPlots())
				try {
					t = s.beginTransaction();
					t.begin();
					if (p.isUpdate()) {
						s.update(p);
						p.setUpdate(false);
					}
					t.commit();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			for (final Clan c : this.cm.getClans())
				try {
					t = s.beginTransaction();
					t.begin();
					if (c.isUpdate()) {
						s.update(c);
						c.setUpdate(false);
					}
					t.commit();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			for (final DamageRecord r : DamageHandler.records)
				try {
					t = s.beginTransaction();
					t.begin();
					s.save(r);
					t.commit();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			DamageHandler.records.clear();
			s.close();
		} catch (final Exception e) {
			e.printStackTrace();
			s.close();
		}
		if (Lostshard.isDebug()) {
			// long delay = System.nanoTime()-time;
			// if(delay >= 10000)
			// Lostshard.log.warning("AsyncGameloop: "+delay);
		}
	}
}
