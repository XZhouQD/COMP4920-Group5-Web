package database;

import java.sql.*;
import java.util.ArrayList;

import food.Food;

public class SQLiteInsertFood {
	
	public static void insertFoodList(ArrayList<Food> fList) {
		for(Food f : fList) {
			if(SQLiteFoodSelect.getFoodByName(f.getName()) != null)
				SQLiteInsertFood.updateFood(f);
			else
				SQLiteInsertFood.insertFood(f);
		}
	}

	public static void insertFood(Food food) {
		Connection c = SQLiteAccess.buildConnection("food.db");
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			String command = "INSERT INTO FOOD (NAME,ENERGY,PROTEIN,FAT,SFA,CARB,SUGAR,SODIUM,COST) " + 
						 "VALUES ('" + food.getName() + "', " + food.getEnergy() + ", " + food.getProtein() + ", " + food.getFat() + ", " + food.getSfa() + ", " + food.getCarb() + ", " + food.getSugar() + ", " + food.getSodium() + ", " + food.getCost() + " );";
			stmt.executeUpdate(command);
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Food "+food.getName()+"Inserted successfully");
	}
	
	public static void updateFood(Food food) {
		Connection c = SQLiteAccess.buildConnection("food.db");
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			String command = "UPDATE FOOD SET energy=" + food.getEnergy() + ", protein=" + food.getProtein() + ", fat=" + food.getFat() + ", sfa=" + food.getSfa() + ", carb=" + food.getCarb() + ", sugar=" + food.getSugar() + ", sodium=" + food.getSodium() + ", cost=" + food.getCost() +
						" WHERE name=" + '"' + food.getName() + '"';
			stmt.executeUpdate(command);
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Food "+food.getName()+"Updated successfully");
	}
}
