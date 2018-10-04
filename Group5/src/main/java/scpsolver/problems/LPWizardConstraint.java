package scpsolver.problems;

import java.util.ArrayList;

public class LPWizardConstraint {
	
	static final byte BIGGERTHANEQUALS = 0;
	static final byte SMALLERTHANEQUALS = 1;
	static final byte EQUALS = 2;
	LPWizard lpw;

	byte type = 0;
	ArrayList<String> variables;
	ArrayList<Double> weights;
	
	double t;
	
	public LPWizardConstraint(byte type, double t, LPWizard lpw) {
		this.type = type;
		this.lpw = lpw;
		this.t = t;
		variables = new ArrayList<String>();
		weights = new ArrayList<Double>();
		
	}

	public LPWizardConstraint plus(String variable, double weight) {
		int idx = variables.indexOf(variable);
		if (idx == -1) {
			variables.add(variable);
			weights.add(weight);
		} else {
			weights.set(idx, weight);
		}
		return this;
	}
	
	public LPWizardConstraint plus(String variable) {
		return this.plus(variable, 1.0);
	}
	
	public LPWizardConstraint setAllVariablesBoolean() {
		for (String var: variables) {
			lpw.setBoolean(var);
		}
		return this;
	}
	
	public LPWizardConstraint setAllVariablesInteger() {
		for (String var: variables) {
			lpw.setInteger(var);
		}
		return this;
	}
	
}
