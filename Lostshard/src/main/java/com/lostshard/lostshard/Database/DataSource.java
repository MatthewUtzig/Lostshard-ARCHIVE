package com.lostshard.lostshard.Database;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Main.Lostshard;

public class DataSource {

    private static DataSource datasource;
    private BasicDataSource ds;

    private DataSource() throws IOException, SQLException, PropertyVetoException {
        ds = new BasicDataSource();
        ds.setDriverClassName(Variables.mysqlDriver);
        ds.setUsername(Variables.mysqlUsername);
        ds.setPassword(Variables.mysqlPassword);
        ds.setUrl(Variables.mysqlUrl);
        
        ds.setMinIdle(5);
        ds.setMaxIdle(20);
        ds.setMaxOpenPreparedStatements(180);
    }

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

    public Connection getConnection() throws SQLException {
    	Connection conn = this.ds.getConnection();
    	if(conn.isClosed())
    		Lostshard.mysqlError();
    	return conn;
    }

}