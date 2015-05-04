package com.lostshard.lostshard.Database;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Main.Lostshard;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSource {

	public static DataSource getInstance() {
		if (datasource == null) {
			try {
				datasource = new DataSource();
			} catch (IOException | SQLException | PropertyVetoException e) {
				e.printStackTrace();
			}
			return datasource;
		} else
			return datasource;
	}

	private static DataSource datasource;

	private final ComboPooledDataSource ds;

	private DataSource() throws IOException, SQLException,
	PropertyVetoException {
		this.ds = new ComboPooledDataSource();
		this.ds.setDriverClass(Variables.mysqlDriver);
		this.ds.setJdbcUrl(Variables.mysqlUrl);
		this.ds.setUser(Variables.mysqlUsername);
		this.ds.setPassword(Variables.mysqlPassword);

		this.ds.setMaxStatements(180);
	}

	public void closeConnection() {
		this.ds.close();
		Lostshard.log.info("[DS] Connection close.");
	}

	public Connection getConnection() throws SQLException {
		final Connection conn = this.ds.getConnection();
		if (conn.isClosed())
			Lostshard.mysqlError();
		return conn;
	}

}