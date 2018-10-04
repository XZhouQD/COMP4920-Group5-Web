package masterAccess;

import java.util.ArrayList;
import java.util.HashMap;

import food.Food;

public class MasterAccess {

	private ArrayList<Food> foodList;
	private static final HashMap<String, String> dailyNeed;
	static {
		dailyNeed = new HashMap<String, String>();
		dailyNeed.put("Energy", "8700 kJ");
		dailyNeed.put("Protein", "50 g");
		dailyNeed.put("Fat", "70 g");
		dailyNeed.put("Saturated Fat", "24 g");
		dailyNeed.put("Carbohydrates", "310 g");
		dailyNeed.put("Sugar", "90 g");
		dailyNeed.put("Sodium", "2300 mg");
	}
	
	private static final HashMap<String, Double> dailyNeedValue;
	static {
		dailyNeedValue = new HashMap<String, Double>();
		dailyNeedValue.put("Energy", 8700.0);
		dailyNeedValue.put("Protein", 50.0);
		dailyNeedValue.put("Fat", 70.0);
		dailyNeedValue.put("Sfa", 24.0);
		dailyNeedValue.put("Carb", 310.0);
		dailyNeedValue.put("Sugar", 90.0);
		dailyNeedValue.put("Sodium", 2300.0);
		dailyNeedValue.put("Cost", 0.0);
	}
	
	public MasterAccess () {
		foodList = new ArrayList<Food>();
	}
	
	public void addFood(Food food) {
		foodList.add(food);
		//food.insertFood();
	}
	
	public void deleteFood(Food food) {
		foodList.remove(food);
		//food.deleteFood();
	}

	public ArrayList<Food> getFoodList() {
		return foodList;
	}

	public static HashMap<String, String> getDailyNeed() {
		return dailyNeed;
	}

	public static HashMap<String, Double> getDailyneedvalue() {
		return dailyNeedValue;
	}

}
