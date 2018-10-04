package food;

import database.*;

public class FoodTest {
	public static void main(String[] args) {

		SQLiteNewDatabase.newDatabase();
		SQLiteNewTable.newTable();
		Food BigMac = new Food("Big Mac", 2180, 26.9, 28.6, 11.3, 37.4, 6.4, 993, 5.5);
		BigMac.insertFood();
		Food test1 = new Food("Test1", 107.6, 21.9, 10.8, 9, 0.7, 3, 5, 10);
		test1.insertFood();
		SQLiteFoodSelect.selectAll();
		Food rt1 = SQLiteFoodSelect.getFoodByName("Test1");
		System.out.println(rt1.toString());
		rt1.deleteFood();
		SQLiteFoodSelect.selectAll();
		Food test2 = new Food("Test2", 2, 5, 1, 3, 6, 1, 6, 3);
		test2.insertFood();
		System.out.println(SQLiteFoodSelect.getFoodByName("Test2").toString());
		test2.setCost(7.2);
		test2.updateFood();
		System.out.println(SQLiteFoodSelect.getFoodByName("Test2").toString());
		SQLiteFoodSelect.selectAll();
		test2.deleteFood();
		
	}
}
