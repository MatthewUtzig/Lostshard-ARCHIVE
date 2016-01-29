package com.lostshard.Lostshard.Database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.lostshard.Lostshard.Data.Variables;
import com.lostshard.Lostshard.Objects.CustomObjects.SavableInventory;
import com.lostshard.Lostshard.Objects.CustomObjects.SavableItem;
import com.lostshard.Lostshard.Objects.Groups.Clan;
import com.lostshard.Lostshard.Objects.Player.OfflineMessage;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Spells.Structures.PermanentGate;

public class Hibernate {
	
	private static SessionFactory factory;
	
	public Hibernate() {
		Configuration cfg = new Configuration();
		cfg.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		cfg.setProperty("show_sql", "true");
		cfg.setProperty("hibernate.hbm2ddl.auto", "update");
		
		//C3P0
		cfg.setProperty("hibernate.c3p0.min_size", "5");
		cfg.setProperty("hibernate.c3p0.max_size", "20");
		cfg.setProperty("hibernate.c3p0.timeout", "300");
		cfg.setProperty("hibernate.c3p0.max_statements", "50");
		cfg.setProperty("hibernate.c3p0.idle_test_period", "3000");
		
		
		cfg.setProperty("hibernate.connection.url", Variables.mysqlUrl);
		cfg.setProperty("hibernate.connection.username", Variables.mysqlUsername);
		cfg.setProperty("hibernate.connection.password", Variables.mysqlPassword);
		
		cfg.addAnnotatedClass(PseudoPlayer.class);
		cfg.addAnnotatedClass(Clan.class);
		cfg.addAnnotatedClass(Plot.class);
		cfg.addAnnotatedClass(OfflineMessage.class);
		cfg.addAnnotatedClass(PermanentGate.class);
		cfg.addAnnotatedClass(SavableInventory.class);
		cfg.addAnnotatedClass(SavableItem.class);
		
		
		factory = cfg.buildSessionFactory();
	}
	
	public Session getSession() {
		return factory.openSession();
	}
}
