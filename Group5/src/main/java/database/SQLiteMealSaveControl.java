package database;

import java.sql.*;
import java.util.HashMap;

public class SQLiteMealSaveControl {
	
	public static HashMap<String, Integer> selectUser(String username) {
		HashMap<String, Integer> foodSave = new HashMap<String, Integer>();
		Connection c = SQLiteAccess.buildConnection("user.db");
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM MEALSAVE WHERE USERNAME='" + username + "';");
			while(rs.next()) {
				System.out.println(rs.getString("meal") + rs.getInt("serve"));
				foodSave.put(rs.getString("meal"), rs.getInt("serve"));
			}
			rs.close();
			stmt.close();
			c.close();		
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return foodSave;
	}
	
	public static void insertUser(String username, HashMap<String, Integer> foodSave) {
		Connection c = SQLiteAccess.buildConnection("user.db");
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			String deleteCommand = "DELETE FROM MEALSAVE WHERE USERNAME='" + username + "';";
			stmt.executeUpdate(deleteCommand);
			for(String foodName : foodSave.keySet()) {
				System.out.println(foodName + foodSave.get(foodName));
				String command = "INSERT INTO MEALSAVE (USERNAME,MEAL,SERVE) " + 
								"VALUES ('" + username + "', '" + foodName + "', " + foodSave.get(foodName) + " );";
				stmt.executeUpdate(command);
			}
			stmt.close();
			c.commit();
			c.close();		
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
}
