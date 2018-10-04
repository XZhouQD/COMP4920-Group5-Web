package scpsolver.problems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.util.SparseVector;

public class LPWizard {

	ArrayList<String> variables;
	ArrayList<Double> weights;
	boolean minProblem = true;
	HashMap<String, HashSet<String>> vargroups;
	HashMap<String, LPWizardConstraint> constraints;
	HashMap<String, HashSet<LPWizardConstraint>> constraintgroups;
	HashSet<String> integervariables;
	HashSet<String> booleanvariables;
	boolean allvariablesinteger = false;
	


	public boolean isMinProblem() {
		return minProblem;
	}

	public void setMinProblem(boolean minProblem) {
		this.minProblem = minProblem;
	}

	public LPWizard() {
		variables = new ArrayList<String>();
		weights = new ArrayList<Double>();
		vargroups = new HashMap<String, HashSet<String>>();
		constraints = new HashMap<String, LPWizardConstraint>();
		constraintgroups = new HashMap<String, HashSet<LPWizardConstraint>>();
		integervariables = new HashSet<String>();
		booleanvariables = new HashSet<String>();
		
	}
	
	public LPWizard plus(String variable, double weight) {
		int idx = variables.indexOf(variable);
		if (idx == -1) {
			variables.add(variable);
			weights.add(weight);
		} else {
			weights.set(idx, weight);
		}
		return this;
	}
	
	public LPWizard plus(String variable, double weight, String group) { 
		if (vargroups.containsKey(group)) {
			vargroups.get(group).add(variable);
		} else {
			HashSet<String> newgroup = new HashSet<String>();
			newgroup.add(variable);
			vargroups.put(group, newgroup);
		}
		return plus(variable, weight);
	}
	
	public LPWizard plus(String variable) {
		return plus(variable, 1.0);
	}
	
	public LPWizard plus(String variable, String group) {
		return plus(variable, 1.0, group);
	}
	
	//Method Add by Xiaowei Zhou
	public void addConstraints(HashMap<String, LPWizardConstraint> lpwCList) {
		for(String lpwC : lpwCList.keySet())
			constraints.put(lpwC, lpwCList.get(lpwC));
	}
	
	//Method Changed by Xiaowei Zhou
	public LPWizardConstraint addConstraint(String name, double value, String stype) {
		byte type = LPWizardConstraint.BIGGERTHANEQUALS;
		if (stype.equalsIgnoreCase("<=")) type = LPWizardConstraint.BIGGERTHANEQUALS;
		if (stype.equalsIgnoreCase(">=")) type = LPWizardConstraint.SMALLERTHANEQUALS;
		if (stype.equalsIgnoreCase("=")) type = LPWizardConstraint.EQUALS;
		LPWizardConstraint c = new LPWizardConstraint(type,value, this);
		//constraints.put(name, c);
		return c;	
	}
	
	public void addAlternative(String alternative1, String alternative2) {
		
		this.addConstraint(alternative1+"VS"+alternative2, 1,"=").plus(alternative1).plus(alternative2).setAllVariablesBoolean();
		this.addConstraint(alternative1+"VS"+"not"+alternative1, 1,"=").plus(alternative1).plus("not_"+alternative1).setAllVariablesBoolean();
		this.addConstraint(alternative2+"VS"+"not"+alternative2, 1,"=").plus(alternative2).plus("not_"+alternative2).setAllVariablesBoolean();
		
	}
	
	public void setBoolean(String var) {
		booleanvariables.add(var);
	}
	
	public void setInteger(String var) {
		integervariables.add(var);
	}
	
	public void setAllVariablesInteger() {
		allvariablesinteger = true;	
	}
	
	public LPSolution solve(LinearProgramSolver solver) {
		LinearProgram lp = this.getLP();
		double[] solution = solver.solve(lp);
		LPSolution sol = new LPSolution(solution, lp);
		return sol;
	}
	
	public LPSolution solve() {
		return solve(SolverFactory.newDefault());
	} 
	
	public LinearProgram getLP() {
		HashSet<String> allvars = new HashSet<String>();
		allvars.addAll(this.variables);
		for (LPWizardConstraint con : constraints.values()) {
			allvars.addAll(con.variables);
		} 
		
		HashMap<String, Integer> indexmap = new HashMap<String, Integer>();
		
		int index = 0;
		for (String string : allvars) {
			indexmap.put(string, index++);
		}
		
		
		int dimension = allvars.size();
		// normalize LPWizard (collect all variables from contraints, etc.)
		
		SparseVector c = new SparseVector(dimension, this.variables.size());
		for (int i = 0; i < variables.size(); i++) {
			c.set(indexmap.get(variables.get(i)), weights.get(i));			
		}
		LinearProgram lp = new LinearProgram(c);
		lp.setIndexmap(indexmap);
		
		for (Entry<String, LPWizardConstraint> entry : constraints.entrySet()) {
			LPWizardConstraint con = entry.getValue();
			String label = entry.getKey();
			
			SparseVector conc = new SparseVector(dimension,con.variables.size());
			for (int i = 0; i < con.variables.size(); i++) {
				conc.set(indexmap.get(con.variables.get(i)), con.weights.get(i));				
			}
			
			switch (con.type) {
			case LPWizardConstraint.BIGGERTHANEQUALS:
				lp.addConstraint(new LinearBiggerThanEqualsConstraint(conc, con.t, label));
				break;
			case LPWizardConstraint.SMALLERTHANEQUALS:
				lp.addConstraint(new LinearSmallerThanEqualsConstraint(conc, con.t, label));
				break;
			case LPWizardConstraint.EQUALS:
				lp.addConstraint(new LinearEqualsConstraint(conc, con.t, label));
				break;
			default:
				break;
			}
		}
		
		if (allvariablesinteger) {
			this.integervariables.addAll(variables);
		}
		
		for (String string :integervariables) {
			lp.setInteger(indexmap.get(string));
		}		
		
		for (String string :booleanvariables) {
			lp.setBinary(indexmap.get(string));
		}
		
		lp.setMinProblem(this.minProblem);
		
		return lp;
		// for all constraint create c-vector
		
	}
	
	
	public static void main(String[] args) {
		LPWizard lpw = new LPWizard();
		lpw.plus("Wheat",226.0).plus("Rye",198.0);
		lpw.addConstraint("Area", 50, ">=").plus("Wheat",0.15).plus("Rye",0.2).setAllVariablesBoolean();
		lpw.addConstraint("Labour", 5000,">=").plus("Wheat", 80).plus("Rye",50);
		lpw.addConstraint("positiveW",1, "<=").plus("Wheat");
		lpw.addConstraint("positiveR",1, "<=").plus("Rye");
		lpw.addAlternative("wheatbarn_1", "wheatbarn_2");
		lpw.addConstraint("storewheat", 0, "<=").plus("wheatbarn_1", 30).plus("wheatbarn_2",6).plus("Wheat",-1);
		lpw.addConstraint("storerye", 0, "<=").plus("not_wheatbarn_1", 30).plus("not_wheatbarn_2",65).plus("Rye",-1);
		
		lpw.setMinProblem(false);
		LPSolution solution = lpw.solve();
		System.out.println(solution);
		
		
		
		/*LinearProgram lp = lpw.getLP();
		
		
		double[] solution = lpsolver.solve(lp);	
		System.out.println(lp.evaluate(solution));
		
		for (String variable : lp.getIndexmap().keySet()) {
			System.out.println(variable + " " + solution[lp.getIndexmap().get(variable)]);
		}
		
		System.out.println( lp.convertToCPLEX());
		
		LPDebugger lpd = new LPDebugger(lp);*/
		
	}
	
	
}
