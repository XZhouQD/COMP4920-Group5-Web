package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQLiteNewUserTable {

	public static void SQLiteNewUserTable() {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:user.db");
			System.out.println("User database opened successfully");
			
			stmt = c.createStatement();
			String sql = "CREATE TABLE USER (USERNAME TEXT NOT NULL, PASSWORD TEXT NOT NULL, NAME TEXT NOT NULL, TYPE TEXT NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Table created successfully");
	}
}
