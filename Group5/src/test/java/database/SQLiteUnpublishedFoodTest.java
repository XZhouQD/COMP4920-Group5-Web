package database;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import food.Food;

public class SQLiteUnpublishedFoodTest {

	@Test
	public void test() {
		SQLiteNewUnpublishedFoodTable.newTable();
		SQLiteInsertFood.insertUnpublishedFood(new Food("Chicken Mac", 2320, 26.9, 28.6, 11.3, 37.4, 6.4, 993, 5.5), "LapisLazuli");
		ArrayList<Food> foodList = SQLiteFoodSelect.selectAllUnpublishedFood();
		System.out.println(foodList);
		System.out.println("=============");
		SQLiteInsertFood.insertUnpublishedFood(new Food("Magic Crop", 1076, 26.9, 28.6, 11.3, 37.4, 6.4, 993, 5.5), "LapisLazuli");
		foodList = SQLiteFoodSelect.selectAllUnpublishedFood();
		System.out.println(foodList);
		System.out.println("=============");
		SQLiteFoodSelect.deleteFoodByNameUnpublished("Chicken Mac");
		foodList = SQLiteFoodSelect.selectAllUnpublishedFood();
		System.out.println(foodList);		
	}

}
