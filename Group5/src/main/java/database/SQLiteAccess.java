package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteAccess {
	
	public static Connection buildConnection(String databaseName) {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
			c.setAutoCommit(false);;
			System.out.println("Food database opened successfully");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return c;
	}
	
}
