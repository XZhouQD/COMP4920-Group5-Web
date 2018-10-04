package database;

import java.sql.Connection;
import java.sql.Statement;

import user.User;

public class SQLiteUserControl {

	public static void insertUser(User user) {
		Connection c = SQLiteAccess.buildConnection("user.db");
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			String command = "INSERT INTO USER (USERNAME,PASSWORD,NAME,TYPE) " + 
						 "VALUES ('" + user.getUsername() + "', '" + user.getPassword() + "', '" + user.getName() + "', '" + user.getType() + "' );";
			stmt.executeUpdate(command);
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("User "+user.getName()+" Inserted successfully");
	}
	
	public static void updateUser(User user) {
		Connection c = SQLiteAccess.buildConnection("user.db");
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			String command = "UPDATE USER SET password='" + user.getPassword() + "' WHERE username='" + user.getUsername() + "';";
			stmt.executeUpdate(command);
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("User "+user.getName()+" Updated successfully");
	}
}
