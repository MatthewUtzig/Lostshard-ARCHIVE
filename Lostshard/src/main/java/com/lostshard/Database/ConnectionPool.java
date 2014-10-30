package com.lostshard.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.lostshard.Data.Variables;

public class ConnectionPool {

	String driver = Variables.mysqlDriver;
	String mysqlHost = Variables.mysqlHost;
	
	String mysqlUsername = Variables.mysqlUsername;
	String mysqlPassword = Variables.mysqlPassword;
	
	Connection mysqlConnection = null;
	
	public ConnectionPool() {
		try {
			mysqlConnection = DriverManager.getConnection(mysqlHost, mysqlUsername, mysqlPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return mysqlConnection;
	}
	
}
