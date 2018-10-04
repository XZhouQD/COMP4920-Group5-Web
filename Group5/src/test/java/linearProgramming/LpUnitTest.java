package linearProgramming;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.SQLiteFoodSelect;
import food.Food;

public class LpUnitTest {

	ArrayList<Food> fList;
	HashMap<String, Integer> reserve;
	LpWizardTry lpwT;
	
	@Before
	public void setup() {
		fList = SQLiteFoodSelect.selectAllFood();
		reserve = new HashMap<String, Integer>();
	}
	
	@Test
	public void testLowestCost() {
		lpwT = new LpWizardTry(fList, "Cost", reserve);
	}
	
	@Test
	public void testLowestCostReserve() {
		reserve.put("Big Mac", 2);
		reserve.put("Rep Food 2", 6);
		lpwT = new LpWizardTry(fList, "Cost", reserve);
		HashMap<String, Integer> result = lpwT.getLowestCombo();
		System.out.println(result.toString());
	}
	
	@Test
	public void testLowestEnergy() {
		lpwT = new LpWizardTry(fList, "Energy", reserve);
		HashMap<String, Integer> result = lpwT.getLowestCombo();
		System.out.println(result.toString());
	}
	
	@Test
	public void testLowestSodium() {
		lpwT = new LpWizardTry(fList, "Sodium", reserve);
		HashMap<String, Integer> result = lpwT.getLowestCombo();
		System.out.println(result.toString());
	}

}
