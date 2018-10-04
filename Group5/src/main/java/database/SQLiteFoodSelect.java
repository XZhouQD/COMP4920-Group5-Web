package database;

import java.sql.*;
import java.util.ArrayList;

import food.Food;

public class SQLiteFoodSelect {

	public static void selectAll () {
		Connection c = SQLiteAccess.buildConnection("food.db");
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM FOOD;");
			while(rs.next()) {
				System.out.println("Name = " + rs.getString("name"));
				System.out.println("Energy = " + rs.getFloat("energy") + " kJ");
				System.out.println("Protein = " + rs.getFloat("protein") + " g");
				System.out.println("Fat = " + rs.getFloat("fat") + " g");
				System.out.println("Saturated Fatty Acids = " + rs.getFloat("sfa") + " g");
				System.out.println("Carbohydrates = " + rs.getFloat("carb") + " g");
				System.out.println("Sugar = " + rs.getFloat("sugar") + " g");
				System.out.println("Sodium = " + rs.getFloat("sodium") + " mg");
				System.out.println("Cost = $" + rs.getFloat("cost"));
			}
			rs.close();
			stmt.close();
			c.close();		
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Food List print Complete");
	}
	
	public static ArrayList<Food> selectAllFood() {
		ArrayList<Food> resultList = new ArrayList<Food>();
		Connection c = SQLiteAccess.buildConnection("food.db");
		Statement stmt = null;
		String name = null;
		double energy = 0;
		double protein = 0;
		double fat = 0;
		double sfa = 0;
		double carb = 0;
		double sugar = 0;
		double sodium = 0;
		double cost = 0;
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM FOOD;");
			while(rs.next()) {
				name = rs.getString("name");
				energy = rs.getFloat("energy");
				protein = rs.getFloat("protein");
				fat = rs.getFloat("fat");
				sfa = rs.getFloat("sfa");
				carb = rs.getFloat("carb");
				sugar = rs.getFloat("sugar");
				sodium = rs.getFloat("sodium");
				cost = rs.getFloat("cost");
				resultList.add(new Food(name, energy, protein, fat, sfa, carb, sugar, sodium, cost));
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
	
	public static Food getFoodByName(String input) {
		Connection c = SQLiteAccess.buildConnection("food.db");
		Statement stmt = null;
		String name = null;
		double energy = 0;
		double protein = 0;
		double fat = 0;
		double sfa = 0;
		double carb = 0;
		double sugar = 0;
		double sodium = 0;
		double cost = 0;
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM FOOD WHERE NAME=" + '"' + input + '"' + ";");
			while(rs.next()) {
				name = rs.getString("name");
				energy = rs.getFloat("energy");
				protein = rs.getFloat("protein");
				fat = rs.getFloat("fat");
				sfa = rs.getFloat("sfa");
				carb = rs.getFloat("carb");
				sugar = rs.getFloat("sugar");
				sodium = rs.getFloat("sodium");
				cost = rs.getFloat("cost");
				/*
				System.out.println("ID = " + rs.getInt("id"));
				System.out.println("Name = " + rs.getString("name"));
				System.out.println("Energy = " + rs.getFloat("energy") + " kJ");
				System.out.println("Protein = " + rs.getFloat("protein") + " g");
				System.out.println("Fat = " + rs.getFloat("fat") + " g");
				System.out.println("Saturated Fatty Acids = " + rs.getFloat("sfa") + " g");
				System.out.println("Carbohydrates = " + rs.getFloat("carb") + " g");
				System.out.println("Sugar = " + rs.getFloat("sugar") + " g");
				System.out.println("Sodium = " + rs.getFloat("sodium") + " mg");
				System.out.println("Cost = $" + rs.getFloat("cost"));
				*/
			}
			rs.close();
			stmt.close();
			c.close();		
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		//System.out.println("Search Complete");
		Food result = new Food(name, energy, protein, fat, sfa, carb, sugar, sodium, cost);
		if (result.getName() == null)
			return null;
		return result;
	}
	
	public static void deleteFoodByName(String input) {
		Connection c = SQLiteAccess.buildConnection("food.db");
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			String command = "DELETE FROM FOOD WHERE NAME=" + '"' + input + '"' + ";";
			stmt.executeUpdate(command);
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Food " + input + " is deleted");
	}
}
