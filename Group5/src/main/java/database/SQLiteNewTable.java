package database;

import java.sql.*;

public class SQLiteNewTable {
	
	//TODO build a table contains: Energy, Protein, Fat, Saturated Fatty Acids, Carbohydrates, Sugars, Sodium, Cost
	public static void newTable() {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:user.db");
			System.out.println("Food database opened successfully");
			
			stmt = c.createStatement();
			String sql = "CREATE TABLE FOOD (NAME TEXT NOT NULL, ENERGY FLOAT NOT NULL, PROTEIN FLOAT NOT NULL, FAT FLOAT NOT NULL, SFA FLOAT NUT NULL, CARB FLOAT NOT NULL, SUGAR FLOAT NOT NULL, SODIUM FLOAT NOT NULL, COST FLOAT NOT NULL)";
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
