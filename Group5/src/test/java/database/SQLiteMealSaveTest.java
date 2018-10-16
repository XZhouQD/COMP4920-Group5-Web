package database;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import food.Food;

public class SQLiteMealSaveTest {

	@Before
	public void setup() {
		//SQLiteNewMealSaveTable.NewUserTable();
	}
	
	@Test
	public void test() {
		String username = "LapisLazuli";
		ArrayList<Food> foodList= SQLiteFoodSelect.selectAllFood();
		HashMap<String, Integer> toSave = new HashMap<String, Integer>();
		for(Food f : foodList) {
			toSave.put(f.getName(), 3);
		}
		SQLiteMealSaveControl.insertUser(username, toSave);
		
		HashMap<String, Integer> toGet = SQLiteMealSaveControl.selectUser("LapisLazuli");
		System.out.println(toSave);
		System.out.println(toGet);
		
		toSave.clear();
		for(Food f : foodList) {
			toSave.put(f.getName(), 5);
		}
		SQLiteMealSaveControl.insertUser(username, toSave);
		toGet = SQLiteMealSaveControl.selectUser("LapisLazuli");
		System.out.println(toSave);
		System.out.println(toGet);
	}

}
