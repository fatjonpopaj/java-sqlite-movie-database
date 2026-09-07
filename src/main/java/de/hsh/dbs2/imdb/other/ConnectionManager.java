package de.hsh.dbs2.imdb.other;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

	private static Connection connection;
	
	public static Connection getConnection() throws SQLException {
		
		try {
			
			if (connection == null) {
				
				connection = DriverManager.getConnection("jdbc:sqlite:datenbank.db"); 
				// "datenbank.db" ist der Name der Datenbank Datei ggf. ändern.
				connection.setAutoCommit(false);
			}
			
		} catch (Exception e) {
			 
			throw new SQLException("Error while connecting to database");
		} 
			
		return connection;
	}
}
