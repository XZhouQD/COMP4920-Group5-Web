package linearProgramming;
import java.util.ArrayList;
import java.util.HashMap;

import database.*;
import food.Food;

public class LpTest {
	
	public static void main (String[] args) {
		ArrayList<Food> fList = SQLiteFoodSelect.selectAllFood();
		HashMap<String, Integer> reserve = new HashMap<String, Integer>();
		
		reserve.put("Big Mac", 1);
		reserve.put("Rep Food 2", 4);
		
		LpWizardTry lpwT = new LpWizardTry(fList, "Cost", reserve);
		HashMap<String, Integer> result = lpwT.getLowestCombo();
		System.out.println(result.toString());
	}
	
}
