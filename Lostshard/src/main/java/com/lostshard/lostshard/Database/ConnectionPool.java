package com.lostshard.lostshard.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Main.Lostshard;

public class ConnectionPool {

	String driver = Variables.mysqlDriver;
	String mysqlHost = Variables.mysqlHost;
	
	String mysqlUsername = Variables.mysqlUsername;
	String mysqlPassword = Variables.mysqlPassword;
	
	Connection mysqlConnection = null;

	private static final int maxConnections = 15;
	private int currentConnection = 0;
	
	private ArrayList<Connection> connections = new ArrayList<Connection>();
	
	public ConnectionPool() {
		for(int i=0; i<maxConnections; i++) {
			try {
				Class.forName(driver).newInstance();
				connections.add((Connection)DriverManager.getConnection(mysqlHost, mysqlUsername, mysqlPassword));
			} catch(Exception e) {
				Lostshard.logger.log(Level.WARNING, "[MYSQL] Failed to open connection >> "+e.toString());
			}
		}
	}
	
	public Connection getConnection() {
		Connection conn = connections.get(currentConnection);
		try {
			if(conn.isClosed())
				Lostshard.logger.log(Level.INFO, "[MYSQL] A connection was closed.");
		} catch(Exception e) {
			Lostshard.logger.log(Level.WARNING, "[MYSQL] Failed to check if connection was close / open new connection >> "+e.toString());
		}
		
		currentConnection++;
		if(currentConnection >= maxConnections)
			currentConnection = 0;
		return conn;
	}
	
}
