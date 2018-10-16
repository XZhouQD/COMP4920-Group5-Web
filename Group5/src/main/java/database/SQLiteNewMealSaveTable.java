package database;

import java.sql.*;

public class SQLiteNewMealSaveTable {

		//Build a table contains user info
		public static void NewUserTable() {
			Connection c = null;
			Statement stmt = null;
			try {
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:C:/Users/Public/user.db");
				c.setAutoCommit(false);
				System.out.println("User database opened successfully");
				
				stmt = c.createStatement();
				String sql = "CREATE TABLE MEALSAVE (USERNAME TEXT NOT NULL, MEAL TEXT NOT NULL, SERVE INT NOT NULL)";
				stmt.executeUpdate(sql);
				stmt.close();
				c.commit();
				c.close();
				System.out.println("User table created");
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				System.exit(0);
			}
			System.out.println("Table created successfully");
		}
	
}
