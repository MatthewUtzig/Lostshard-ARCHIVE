package com.lostshard.RPG.External;

import java.sql.*;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;

public class ConnectionPool {
	//private static String _user = "root";
	private static String _user = "USERNAME HERE";
	private static String _url = "jdbc:mysql://localhost";

	private ArrayList<Connection> _connections = new ArrayList<Connection>();
	private static final int NUM_CONNECTIONS = 15;
	private int _curConnection = 0;
	
	public ConnectionPool() {
		for(int i=0; i<NUM_CONNECTIONS; i++) {
			try {
				Class.forName ("com.mysql.jdbc.Driver").newInstance ();
				_connections.add((Connection)DriverManager.getConnection (_url, _user, _pass));
			}
			catch(Exception e) {
				System.out.println("(SEVERE) Failed to open connection >> "+e.toString());
			}
		}
	}
	
	public Connection getConnection() {
		Connection conn = _connections.get(_curConnection);
		try {
			if(conn.isClosed()) {
				System.out.println("A_CONNECTION_WAS_CLOSED");
			}
		}
		catch(Exception e) {
			System.out.println("Failed to check if connection was close / open new connection >> " + e.toString());
		}
			
		_curConnection++;
		if(_curConnection >= NUM_CONNECTIONS)
			_curConnection = 0;
		
		return conn;
	}
}
