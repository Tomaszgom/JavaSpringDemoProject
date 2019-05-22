package com.logreader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class DbConnection {
		
	private final static Logger logger = Logger.getLogger(DbConnection.class.getName());
	static Connection conn;
			
	public void LoadJDBCDriver() {		
		try {
			logger.info("Attempting to load HSQLDB JDBC driver.");
			Class.forName(AppMain.properties.getProperty("hsqldbJDBCDriver"));
		} catch (Exception e) {
			logger.debug("Failed to load HSQLDB JDBC driver.", e);
		}		
	}
	
	public void CreateEventTable() {
		
		LoadJDBCDriver();
				
		// Load query			    
		String createTable = "create table if not exists Event"
							+ "(id varchar(50), duration numeric, Type varchar(100), Host varchar(50), alert boolean);";
		
	   			
		// Create DB and table if does not exist		
		try {	
			logger.info("Attempting to connect to database and execute create table query.");
			conn = DriverManager.getConnection(AppMain.properties.getProperty("dbConnectionString"), "SA", "");
    		conn.createStatement().executeQuery(createTable);   					   			       
		}catch (SQLException e) {
			logger.debug("SQL Exception failed.", e);
		}finally{
			try {
				conn.close();
			}catch (SQLException e) {
				logger.debug("Failed to close database connection.", e);
			}
		}				
	}
	
	public void InsertData(HashMap<String, Event> eventsMap) {
	
		LoadJDBCDriver();
		
		try {
			logger.info("Attempting to connect to database and execute insert query.");
			conn = DriverManager.getConnection(AppMain.properties.getProperty("dbConnectionString"), "SA", "");		   			
			String query = "INSERT INTO event (id, duration, Type, Host, alert) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement insertStmnt = conn.prepareStatement(query);    
		   					
			conn.setAutoCommit(false);
		
			// Build insert query from events map						
			for (Map.Entry<String, Event> event :eventsMap.entrySet()){   				
				insertStmnt.setString(1, event.getKey());
				insertStmnt.setLong(2, event.getValue().getDuration());
				insertStmnt.setString(3, event.getValue().getType());
				insertStmnt.setString(4, event.getValue().getHost());
				insertStmnt.setBoolean(5, event.getValue().getAlert());    					
				insertStmnt.addBatch();
			}
		   		
			insertStmnt.executeBatch();
			conn.commit(); 
      
		}catch (SQLException e) {
			logger.debug("SQL Exception failed.", e);
		}finally{
			try {
				conn.close();
			}catch (SQLException e) {
				logger.debug("Failed to close database connection.", e);
			}
		}

	}   
}