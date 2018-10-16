package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQLiteNewUnpublishedFoodTable {

	public static void newTable() {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:C:/Users/Public/food.db");
			c.setAutoCommit(false);
			System.out.println("User database opened successfully");
			
			stmt = c.createStatement();
			String sql = "CREATE TABLE UNFOOD (USERNAME TEXT NOT NULL, NAME TEXT NOT NULL, ENERGY FLOAT NOT NULL, PROTEIN FLOAT NOT NULL, FAT FLOAT NOT NULL, SFA FLOAT NUT NULL, CARB FLOAT NOT NULL, SUGAR FLOAT NOT NULL, SODIUM FLOAT NOT NULL, COST FLOAT NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
			c.close();
			System.out.println("Food table created");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Table created successfully");
	}
}
