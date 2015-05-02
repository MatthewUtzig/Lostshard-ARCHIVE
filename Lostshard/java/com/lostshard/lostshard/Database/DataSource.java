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
		} else {
			return datasource;
		}
	}
	private static DataSource datasource;

	private ComboPooledDataSource ds;

	private DataSource() throws IOException, SQLException,
			PropertyVetoException {
		ds = new ComboPooledDataSource();
		ds.setDriverClass(Variables.mysqlDriver);
		ds.setJdbcUrl(Variables.mysqlUrl);
		ds.setUser(Variables.mysqlUsername);
		ds.setPassword(Variables.mysqlPassword);

		ds.setMaxStatements(180);
	}

	public void closeConnection() {
		ds.close();
		Lostshard.log.info("[DS] Connection close.");
	}

	public Connection getConnection() throws SQLException {
		Connection conn = this.ds.getConnection();
		if (conn.isClosed())
			Lostshard.mysqlError();
		return conn;
	}

}