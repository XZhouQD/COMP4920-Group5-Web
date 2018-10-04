package database;
import java.util.ArrayList;

import food.*;
import user.User;

public class SQLiteMasterAccessTest {
	/*
	public static void main (String[] Args) {
		
		SQLiteNewDatabase.newDatabase();
		SQLiteNewTable.newTable();
		ArrayList<Food> fList = new ArrayList<Food>();
		Food bigMac = new Food("Big Mac", 2180, 26.9, 28.6, 11.3, 37.4, 6.4, 993, 5.5);
		Food test2 = new Food("Test Food 1", 460, 11.1, 10.3, 5.1, 10.9, 5.6, 104, 1.3);
		Food test3 = new Food("Rep Food 2", 1036, 9.1, 5.3, 10.6, 22.9, 3.1, 406, 2.8);
		fList.add(bigMac);
		fList.add(test2);
		fList.add(test3);
		SQLiteInsertFood.insertFoodList(fList);
		//Basic structure completed, run this code to see inserted food big mac.
		SQLiteFoodSelect.selectAll();
		
	}
	*/
	
	public static void main (String[] Args) {
		SQLiteNewUserTable.SQLiteNewUserTable();
		User lapis = new User("LapisLazuli", "123456", "Xiaowei", "ADMIN");
		lapis.addToDB();
		new User("Emerald", "TestPassWord", "Abhilasha", "ADMIN").addToDB();
		new User("Diamond", "Pass&symbol", "John Doe", "USER").addToDB();
		SQLiteUserSelect.selectAll();
		lapis.setPassword("NewPassword");
		lapis.updatePassword();
		SQLiteUserSelect.selectAll();
	}
	
}
