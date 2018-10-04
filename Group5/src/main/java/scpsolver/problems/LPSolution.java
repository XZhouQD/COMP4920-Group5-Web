package scpsolver.problems;

import java.util.HashMap;
import java.util.Map.Entry;

public class LPSolution {
	
	public LPSolution(double[] solution, LinearProgram lp) {
		super();
		this.solution = solution;
		this.lp = lp;
		this.indexmap = lp.indexmap;
	}

	double[] solution;
	HashMap<String, Integer> indexmap;
	LinearProgram lp;
	
	public double getDouble(String variable) {
		return solution[indexmap.get(variable)];
	}
	
	public long getInteger(String variable) {		
		return Math.round(solution[indexmap.get(variable)]);
	}	
	
	public boolean getBoolean(String variable) {		
		return (solution[indexmap.get(variable)] > 0.5);
	}
	
	public double getObjectiveValue() {
		return lp.evaluate(solution);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Integer> entry : indexmap.entrySet()) {
		  sb.append(entry.getKey() + "\t"+ solution[entry.getValue()]+"\n");			
		}
		sb.append("Objective Value \t" + this.getObjectiveValue()+"\n");		
		return sb.toString();
	}
}
