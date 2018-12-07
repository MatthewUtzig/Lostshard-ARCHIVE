package com.lostshard.Lostshard.Database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.lostshard.Economy.Wallet;
import com.lostshard.Lostshard.Data.Variables;
import com.lostshard.Lostshard.Objects.ChestRefill;
import com.lostshard.Lostshard.Objects.Groups.Clan;
import com.lostshard.Lostshard.Objects.Player.OfflineMessage;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Recorders.ConnectionRecord;
import com.lostshard.Lostshard.Objects.Recorders.DamageRecord;
import com.lostshard.Lostshard.Objects.Recorders.DeathRecord;
import com.lostshard.Lostshard.Objects.Recorders.SkillGainRecord;
import com.lostshard.Lostshard.Objects.Recorders.UsernameUUIDRecord;
import com.lostshard.Lostshard.Objects.Store.Store;
import com.lostshard.Lostshard.Spells.Structures.PermanentGate;
import com.lostshard.Plots.Models.Plot;

public class Hibernate {

	private static SessionFactory factory;

	public Hibernate() {
		final Configuration cfg = new Configuration();
		cfg.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		cfg.setProperty("show_sql", "true");
		cfg.setProperty("hibernate.hbm2ddl.auto", "update");

		// C3P0
		cfg.setProperty("hibernate.c3p0.min_size", "5");
		cfg.setProperty("hibernate.c3p0.max_size", "20");
		cfg.setProperty("hibernate.c3p0.timeout", "300");
		cfg.setProperty("hibernate.c3p0.max_statements", "50");
		cfg.setProperty("hibernate.c3p0.idle_test_period", "3000");

		cfg.setProperty("hibernate.connection.url", Variables.mysqlUrl);
		cfg.setProperty("hibernate.connection.username", Variables.mysqlUsername);
		cfg.setProperty("hibernate.connection.password", Variables.mysqlPassword);

		cfg.addAnnotatedClass(PseudoPlayer.class);
		cfg.addAnnotatedClass(Wallet.class);
		cfg.addAnnotatedClass(Clan.class);
		cfg.addAnnotatedClass(Plot.class);
		cfg.addAnnotatedClass(OfflineMessage.class);
		cfg.addAnnotatedClass(PermanentGate.class);
		cfg.addAnnotatedClass(Store.class);
		cfg.addAnnotatedClass(ChestRefill.class);
		cfg.addAnnotatedClass(DamageRecord.class);
		cfg.addAnnotatedClass(ConnectionRecord.class);
		cfg.addAnnotatedClass(UsernameUUIDRecord.class);
		cfg.addAnnotatedClass(SkillGainRecord.class);
		cfg.addAnnotatedClass(DeathRecord.class);
		

		factory = cfg.buildSessionFactory();
	}

	public Session getSession() {
		return factory.openSession();
	}
}
