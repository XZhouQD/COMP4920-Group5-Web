package scpsolver.constraints;

import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.util.SparseVector;

/**
 * @author  planatsc
 */
public class LinearSmallerThanEqualsConstraint implements LinearConstraint, Convertable {

	SparseVector c;
	double t;
	String name;
	
	/**
	 * Generic constructor for LinearSmallerThanEqualsConstraint 
	 * @param c c-Vector
	 * @param t right hand side
	 * @param name name of the constraint
	 */
	public LinearSmallerThanEqualsConstraint(SparseVector c, double t, String name) {
		super();
		this.c = c;
		this.t = t;
		this.name = name;
	}
	
	/**
	 * Generic constructor for LinearSmallerThanEqualsConstraint 
	 * @param pc c-Vector
	 * @param t right hand side
	 * @param name name of the constraint
	 */	
	public LinearSmallerThanEqualsConstraint(double[] pc, double t, String name) {
		super();
		this.c = new SparseVector(pc);
		this.t = t;
		this.name = name;
	}

	/**
	 * Set c-Vector
	 * @param pc c-vector
	 */
	public void setC(double[] pc) {
		this.c = new SparseVector(pc);		
	}
	
	/**
	 * @return c-vector
	 */
	public double[] getC() {
		return c.get();
	}
	

	/**
	 * @return the right hand side
	 */
	public double getT() {
		return t;
	}
	/**
	 * @param t the right hand side to set
	 */
	public void setT(double t) {
		this.t = t;
	}
	
	/**
	 * @return right hand side
	 */
	public String getName() {
		return name;
	}

	public boolean isSatisfiedBy(double[] x) {
		return (c.dot(x) - (Math.pow(10, -6))<= t);
	}
	
	public void addToLinearProgramSolver(LinearProgramSolver solver) {
		solver.addLinearSmallerThanEqualsConstraint(this);
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
		
		result.append(" <= " + this.t);
		
		result.append("\n");
		return result;
	}
	
	// Converts the constraint into the GMPL format
	public StringBuffer convertToGMPL() {
		StringBuffer result = new StringBuffer("subject to " + this.name + ": ");
		
		for (int i : c.getIndex()) 
			result.append( c.get(i) + " * x" + i + " + ");
		
		result.delete(result.lastIndexOf("+"), result.length());
		
		result.append(" <= " + this.t + ";\n");
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
