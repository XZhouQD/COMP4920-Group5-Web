package linearProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import food.*;
import masterAccess.*;
import scpsolver.problems.*;

/*
 * This class provide LP Solving for minimum variable meal combo
 * initialise by a foodList (from SQLite database), the variable want to be minimum in String format, A reserve food hashmap with foodName->serves
 * Call Solver by getLowestCombo method, it will return a hashmap of foodName->Number
 */
public class LpWizardTry {
	ArrayList<Food> fList;
	HashMap<String, Double> dailyNeed;
	HashMap<String, Integer> reserve;
	String lowest;
	ArrayList<String> variables;
	
	public LpWizardTry(ArrayList<Food> fList, String lowest, HashMap<String, Integer> reserve) {
		this.fList = fList;
		this.dailyNeed = MasterAccess.getDailyneedvalue();
		this.lowest = lowest;
		this.reserve = reserve;
		this.variables = new ArrayList<String>(Arrays.asList("Energy", "Protein", "Fat", "Sfa", "Carb", "Sugar", "Sodium", "Cost"));
	}
	
	public HashMap<String, Integer> getLowestCombo() {
		System.out.println("==================");
		System.out.println(fList);
		System.out.println(lowest);
		System.out.println(reserve);
		System.out.println("==================");
		
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		HashMap<String, LPWizardConstraint> lpwCList = new HashMap<String, LPWizardConstraint>();
		LPWizard lpw = new LPWizard();
		lpw.plus(lowest, 1.0);
		for(String variable : variables) {
			if(lowest.equals(variable))
				continue;
			//System.out.print("Connstraint " + variable + ": " + dailyNeed.get(variable) + "<= 0 ");
			LPWizardConstraint lpwC = lpw.addConstraint(variable, dailyNeed.get(variable), "<=");
			for(Food f : fList) {
				lpwC = lpwC.plus(f.getName(), f.getByString(variable));
				//System.out.print("+ " + f.getName() + " * " + f.getByString(variable) + " ");
			}
			lpwC.setAllVariablesInteger();
			lpwCList.put(variable, lpwC);
			System.out.println();
		}
		
		LPWizardConstraint lpwC = lpw.addConstraint(lowest, 0, "=");
		//System.out.print("Connstraint " + lowest + ": 0 = 0 ");
		for(Food f : fList) {
			lpwC = lpwC.plus(f.getName(), f.getByString(lowest));
			//System.out.print("+ " + f.getName() + " * " + f.getByString(lowest) + " ");
		}
		lpwC = lpwC.plus(lowest, -1.0);
		//System.out.println("- " + lowest + " ");
		lpwCList.put(lowest, lpwC);
		
		for(Food f : fList) {
			int reserveNum = 0;
			if(reserve.containsKey(f.getName()))
				reserveNum = reserve.get(f.getName());
			LPWizardConstraint lpwCN = lpw.addConstraint("Number"+f.getName(), reserveNum, "<=");
			lpwCN.plus(f.getName(), 1.0);
			lpwCList.put("Number"+f.getName(), lpwCN);
			//System.out.println("Number"+f.getName()+" : " + reserveNum + "<= " + f.getName());
		}
		
		lpw.addConstraints(lpwCList);
		LPSolution sol = lpw.solve();
		for(Food f : fList) {
			result.put(f.getName(), (int) sol.getInteger(f.getName()));
		}
		return result;
	}
	
}
