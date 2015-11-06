package com.lostshard.Lostshard.Database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.lostshard.Lostshard.Objects.Groups.Clan;
import com.lostshard.Lostshard.Objects.Player.Bank;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;

public class hibernate {

	public static void main(String[] args) {
		Configuration cfg = new Configuration();
		cfg.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		cfg.setProperty("show_sql", "true");
		cfg.setProperty("hibernate.hbm2ddl.auto", "update");
		
		//C3P0
//		cfg.setProperty("hibernate.c3p0.min_size", "5");
//		cfg.setProperty("hibernate.c3p0.max_size", "20");
//		cfg.setProperty("hibernate.c3p0.timeout", "300");
//		cfg.setProperty("hibernate.c3p0.max_statements", "50");
//		cfg.setProperty("hibernate.c3p0.idle_test_period", "3000");
		
		
		cfg.setProperty("hibernate.connection.url", "jdbc:mysql://51.254.209.47/lostshardtest");
		cfg.setProperty("hibernate.connection.username", "test");
		cfg.setProperty("hibernate.connection.password", "00101000");
		
		cfg.addAnnotatedClass(PseudoPlayer.class);
		cfg.addAnnotatedClass(Clan.class);
		cfg.addAnnotatedClass(Bank.class);
		
		SessionFactory sf = cfg.buildSessionFactory();
		Session s = sf.openSession();
		Transaction t = s.beginTransaction();
		t.commit();
	}
	
}
