package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import user.User;

public class SQLiteUserSelect {

	public static void selectAll () {
		Connection c = SQLiteAccess.buildConnection("user.db");
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM USER;");
			while(rs.next()) {
				System.out.println("Username = " + rs.getString("username"));
				System.out.println("Password = " + rs.getString("password"));
				System.out.println("Name = " + rs.getString("name"));
				System.out.println("Type = " + rs.getString("type"));
			}
			rs.close();
			stmt.close();
			c.close();		
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("User List print Complete");
	}
	
	public static ArrayList<User> selectAllUser() {
		ArrayList<User> resultList = new ArrayList<User>();
		Connection c = SQLiteAccess.buildConnection("user.db");
		System.out.println("user.db connected");
		Statement stmt = null;
		String username = null;
		String password = null;
		String name = null;
		String type = null;
		try {
			stmt = c.createStatement();
			System.out.println("Statement Created");
			ResultSet temp = stmt.executeQuery("SELECT * FROM sqlite_master WHERE type='table'");
			while(temp.next()) {
				System.out.println(temp.getString("name"));
			}
			ResultSet rs = stmt.executeQuery("SELECT * FROM USER");
			System.out.println("SQL Select complete");
			while(rs.next()) {
				username = rs.getString("username");
				password = rs.getString("password");
				name = rs.getString("name");
				type = rs.getString("type");
				resultList.add(new User(username, password, name, type));
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return resultList;
	}

}
