package scpsolver.constraints;

import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.util.SparseVector;

public class LinearEqualsConstraint implements LinearConstraint, Convertable {

	SparseVector c;
	double t;
	String name;
	
	public LinearEqualsConstraint(SparseVector c, double t, String name) {
		super();
		this.c = c;
		this.t = t;
		this.name = name;
	}
	
	public LinearEqualsConstraint(double[] pc, double t, String name) {
		super();
		this.c = new SparseVector(pc);
		this.t = t;
		this.name = name;
	}

	public void setC(double[] pc) {
		this.c = new SparseVector(pc);		
	}
	
	public double[] getC() {
		return c.get();		
	}
	

	/**
	 * @return the t
	 */
	public double getT() {
		return t;
	}
	/**
	 * @param t the t to set
	 */
	public void setT(double t) {
		this.t = t;
	}
	
	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	public boolean isSatisfiedBy(double[] x) {
		return (Math.abs(c.dot(x) - t) < Math.pow(10, -6));
	}
	
	public void addToLinearProgramSolver(LinearProgramSolver solver) {
		solver.addEqualsConstraint(this);
	}
	
	// Converts the constraint into the CPLEX LP format
	public StringBuffer convertToCPLEX() {
	
		StringBuffer result = new StringBuffer(" " + this.name.replace(" ", "_") + ": ");
		
		boolean firstOne = Boolean.TRUE;
		
		for (int i : c.getIndex()) {
			
			if (firstOne) {
				firstOne = Boolean.FALSE;
				if (c.get(i) == 1)
					result.append("x" + i);
				else if (c.get(i) < 0) {
					result.append( c.get(i) + " x" + i);
				}
				else
					result.append( c.get(i) + " x" + i); 
			}
			else {
				if (c.get(i) == 1)
					result.append(" + " + "x" + i);
				else if (c.get(i) < 0) {
					result.append( " " + c.get(i) + " x" + i);
				}
				else
					result.append( " + " + c.get(i) + " x" + i); 
			}

		}
		
		result.append(" = " + this.t);
		
		result.append("\n");
		return result;
	}
	
//	 Converts the constraint into the GMPL format
	public StringBuffer convertToGMPL() {
		StringBuffer result = new StringBuffer("subject to " + this.name + ": ");
		
		for (int i : c.getIndex())
			result.append( c.get(i) + " * x" + i + " + ");
		result.delete(result.lastIndexOf("+"), result.length());
		
		result.append(" = " + this.t + ";\n");
		return result;
	}
	
	public SparseVector getCSparse() {
		// TODO Auto-generated method stub
		return c.clone();
	}

	public double getRHS() {
		// TODO Auto-generated method stub
		return t;
	}
}
