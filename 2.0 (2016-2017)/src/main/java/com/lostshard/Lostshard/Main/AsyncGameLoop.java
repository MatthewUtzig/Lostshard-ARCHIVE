package com.lostshard.Lostshard.Main;

import org.bukkit.scheduler.BukkitRunnable;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.lostshard.Lostshard.Manager.ChestRefillManager;
import com.lostshard.Lostshard.Manager.ClanManager;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.RecordManager;
//import com.lostshard.Lostshard.Objects.Groups.Clan;
//import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
//import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.Recorders.Record;
import com.lostshard.Plots.PlotManager;

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
//			try {
//				t = s.beginTransaction();
//				t.begin();
//				for (final PseudoPlayer p : this.pm.getPlayers()) {
//					if (p.isUpdate()) {
//						s.update(p);
//						p.setUpdate(false);
//					}
//				}
//				t.commit();
//			} catch (final Exception e) {
//				e.printStackTrace();
//			}
//			try {
//				t = s.beginTransaction();
//				t.begin();
//				for (final Plot p : this.ptm.getPlots()) {
//						if (p.isUpdate()) {
//							s.update(p);
//							p.setUpdate(false);
//						}
//				}
//				t.commit();
//			} catch (final Exception e) {
//				e.printStackTrace();
//			}
//			try {
//				t = s.beginTransaction();
//				t.begin();
//				for (final Clan c : this.cm.getClans()) {
//						if (c.isUpdate()) {
//							s.update(c);
//							c.setUpdate(false);
//						}
//				}
//				t.commit();
//			} catch (final Exception e) {
//				e.printStackTrace();
//			}
			try {
				for (final Record r : RecordManager.getManager().getRecords()) {
					t = s.beginTransaction();
					t.begin();
					s.save(r);
					t.commit();
				}
				} catch (final Exception e) {
					e.printStackTrace();
				}
				RecordManager.getManager().getRecords().clear();
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
