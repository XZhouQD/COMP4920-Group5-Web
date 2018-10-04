package scpsolver.problems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import scpsolver.constraints.Constraint;
import scpsolver.constraints.Convertable;
import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.util.SparseVector;

/**
 * Basic description for a linear program.
 * 
 * In an ordinary production usage, this class usually gets instanced first. Then all
 * constraints and boundaries are added and finally, a solver is instanced to solve
 * the linear program.
 * 
 * @author  planatsc
 */
public class LinearProgram extends MathematicalProgram implements ConstrainedProblem {
	
	HashMap<String, Integer> indexmap;
	
	/**
	 * Marks a variable as boolean. Note: A variable is not directly marked as boolean but as an 
	 * integer with 0 <= x_i <= 1.
	 * @param i The index of the variable.
	 */
	public void setBinary(int i) {
		isboolean[i] = true;
		isinteger[i] = true;
	}

	/**
	 * Creates a basic maximization problem with no target function and no constraints.
	 */
	public LinearProgram() {
		super();
		minproblem = false;
		constraints = new ArrayList<Constraint>();
	}
	
	/**
	 * Creates a basic maximization problem with the given target function and no constraints.
	 * 
	 * @param pc the vector of the target function
	 */
	public LinearProgram(double[] pc) {
		super();
		this.minproblem = false;
		this.constraints = new ArrayList<Constraint>();
		this.c = (SparseVector) new SparseVector(pc);
		this.isinteger  = new boolean[pc.length];
		this.isboolean  = new boolean[pc.length];
		
	}
	
	/**
	 * Creates a basic maximization problem with the given target function and no constraints.
	 *  
	 * @param c the vector of the target function in its sparse representation
	 */
	public LinearProgram(SparseVector c) {
		super();
		this.minproblem = false;
		this.constraints = new ArrayList<Constraint>();
		this.c = (SparseVector) c;
		this.isinteger  = new boolean[c.getSize()];
		this.isboolean  = new boolean[c.getSize()];
	}

	/**
	 * Creates a shallow copy of a given linear program.
	 * 
	 * @param lp the program to be copied
	 */
	// TODO Is this a shallow or a deep copy?
	@SuppressWarnings("unchecked")
	public LinearProgram(LinearProgram lp) {
		super();
		this.minproblem = lp.minproblem;
		this.isinteger = lp.isinteger.clone();
		this.isboolean = lp.isboolean.clone();
		
		this.c = lp.c.clone();
		this.constraints = (ArrayList<Constraint>) lp.constraints.clone();
		if (lp.hasBounds()) {
			this.upperbound = lp.upperbound.clone();
			this.lowerbound = lp.lowerbound.clone();
		}
		
	}
	
	/**
	 * Sets the whole list of constraints at once. Note that the existing constraints
	 * are immediately overwritten without any further checking or copying and are
	 * simply lost. Therefore, the user might want to check, if there are any previous
	 * constraints, before he sets the complete list. Generally, it's recommended to use
	 * the <code>addConstraint</code> method for adding constraints to a problem model.
	 * 
	 * On the other hand, this method can be used to delete constraints from a model, but
	 * the user has to ensure that a valid <code>ArrayList&lt;Constraint&gt;</code> is
	 * passed. This method will accept the <code>null</code> value, but this will lead to
	 * invalid states.
	 * 
	 * @param constraints the new constraint list
	 */
	public void setConstraints(ArrayList<Constraint> constraints) {
		this.constraints = constraints;
	}  
	
	/* (non-Javadoc)
	 * @see nmi.ConstrainedProblem#addConstraint(nmi.constraints.Constraint)
	 */
	/**
	 * Adds a linear constraint to the model. If no deletion operation is needed,
	 * this method is to be preferred before using <code>setConstraints</code>.
	 * 
	 * @param c the linear constraint to be added
	 * @return <code>true</code>, if the constraint was succesfully added, 
	 * otherwise <code>false</code>
	 */
	public boolean addConstraint(LinearConstraint c) {
		return constraints.add(c);
	}
	/* (non-Javadoc)
	 * @see nmi.ConstrainedProblem#addConstraint(nmi.constraints.Constraint)
	 */
	/**
	 * Adds a list of linear constraint to the model. If no deletion operation is needed,
	 * this method is to be preferred before using <code>setConstraints</code>.
	 *   
	 * @param c the linear constraint to be added
	 * @return <code>true</code>, if the constraint was succesfully added, 
	 * otherwise <code>false</code>
	 */
	public boolean addConstraints(ArrayList<LinearConstraint> c) {
		return constraints.addAll(c);
	}

	/* (non-Javadoc)
	 * @see nmi.ConstrainedProblem#isFeasable(double[])
	 */
	/**
	 * Checks if all constraints are fulfilled for the vector <code>x</code>.
	 * 
	 * @param the vector which is to be checked
	 * @return <code>true</code>, if the vector <code>x</code> fulfills all
	 * constraints, otherwise <code>false</code>
	 */
	public boolean isFeasable(double[] x) {
		for (Constraint c : constraints) {
			if (!c.isSatisfiedBy(x)) return false;
		}	
		return true;
	}
	
	/* (non-Javadoc)
	 * @see nmi.ConstrainedProblem#getConstraints()
	 */
	/**
	 * Returns the current constraint list.
	 * 
	 * @return the list of the current constraints
	 */
	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}
	
	/**
	 * Evaluates the target function at position <code>x</code>.
	 * 
	 * @param x the position, where the target function is to be evaluated
	 * @return the value of the target function at said position
	 */
	public double evaluate(double[] x) {		
		return c.dot(x);
		
		//c.dot (new SparseMatrix(new double[][]{x})).get(0, 0);
	}

	/**
	 * Returns a short String description of the class.
	 * 
	 * @return Returns the String "Linear Program"
	 */
	public String getName() {
		return "Linear Program";
	}
	
	/*public LinearProgram getDual() {
		 Iterator<Constraint> citerator = constraints.iterator();
		 int dualsize = constraints.size();
		 SparseVector dualc =  new SparseVector(dualsize, dualsize);
		 int counter = 0;
		 while (citerator.hasNext()) {
			LinearConstraint constraint = (LinearConstraint) citerator.next();
			dualc.set(counter++, constraint.getT());
		 }
		 ArrayList<Constraint> dualconstraints = new ArrayList<Constraint>();
		 for (int i = 0; i < dualc.getSize(); i++) {
			 dualconstraints.add();
		 }
		 // TODO add dual constraints
		//LinearProgram dual = new LinearProgram();
		
		
	}
	
	public LinearProgram getStandardFormLP() {
		
		
	}*/
	
	
	/**
	 * Converts a given linear program into the CPLEX file format.
	 * @return A StringBuffer containing the linear problem specifications in the CPLEX format
	 */
	// RFC by Schobi: why should we return a StringBuffer rather than a String?
	//			      Do we want the users to be able to modify the output anyway
	public StringBuffer convertToCPLEX () {
		
		if (indexmap == null) {
			
			
		}
		
		
		StringBuffer result = new StringBuffer();
		
		result.append("\\ This file is autogenerated by the SCPSolver framework\n");
		result.append("\\ Modify at your own risk\n\n");
		
		// Writing the objective
		result.append( ( minproblem ? "MINIMIZE\n" : "MAXIMIZE\n") );
		
		// Just give the function a name ...
		result.append(" obj:");
		for (int i = 0; i < this.getC().length ; i++) {
			BigDecimal coeff = new BigDecimal(this.getC()[i]);
			
			if (coeff.signum() < 0)
				result.append(" -");
			else if (i > 0)
				result.append(" + ");
			// Just some eye-candy
			else if (i == 0)
				result.append(" ");
			
			if (coeff.abs().intValue() != 1)
				result.append(coeff.abs().toString());
			
			result.append(" x" + i);
		}
		result.append("\n");
		
		// Writing the constraints
		result.append("SUBJECT TO\n");
		for (Constraint c : constraints)
			result.append(( (Convertable) c).convertToCPLEX());
		
		// Writing the bounds
		
		if (this.hasBounds()) {
			result.append("Bounds\n");
			
			for (int i = 0; i < upperbound.length ; i++) {
			
			if (lowerbound[i] == - Double.MAX_VALUE )
				result.append( " -infinity");
			else
				result.append( " " + lowerbound[i] );
		
			result.append( " <= x" + i + " <= ");
			
			if(upperbound[i] == Double.MAX_VALUE )
				result.append( "+infinity\n");
			else
				result.append( upperbound[i] + "\n");
		}
		}
		
		
		
		// Writing the  section, if necessary
		boolean sectionwritten = false;
		for (int i = 0; i < isinteger.length; i++) {
			if (isinteger[i] && !sectionwritten) {
				result.append("GENERAL\n");
				sectionwritten = true;
			}
			if (isinteger[i])
				result.append(" x" + i + "\n");
		}
		
		if (sectionwritten)
			result.append("\n");
		
		// Finish the whole thing and we're done
		result.append("END\n");
		return result;
	}
	
	public HashMap<String, Integer> getIndexmap() {
		return indexmap;
	}

	public void setIndexmap(HashMap<String, Integer> indexmap) {
		this.indexmap = indexmap;
	}
	
	public static void main(String[] args) {
		
		LinearProgram lp = new LinearProgram(new double[]{10.0, 6.0, 4.0});
		lp.addConstraint(new LinearSmallerThanEqualsConstraint(new double[]{1.0,1.0,1.0}, 320,"p"));
		lp.addConstraint(new LinearSmallerThanEqualsConstraint(new double[]{10.0,4.0,5.0}, 650,"q"));
		lp.addConstraint(new LinearBiggerThanEqualsConstraint(new double[]{2.0,-2.0,6.0}, 100,"r1"));
		
		lp.setLowerbound(new double[]{30.0,0.0,0.0});
		
		//lp.addConstraint(new LinearEqualsConstraint(new double[]{1.0,1.0,1.0}, 100,"t"));
		
		// lp.setInteger(0);
		// lp.setInteger(1);
		lp.setInteger(2);
		
		System.out.print(lp.convertToCPLEX());
		System.out.println();
		//System.out.print(lp.convertToGMPL());
		
		lp = new LinearProgram(new double[]{25.0, 30.0});
		lp.addConstraint(new LinearSmallerThanEqualsConstraint(new double[]{(1.0 / 200.0), (1.0 / 140.0)}, 40, "Time"));
		lp.setLowerbound(new double[]{0.0, 0.0});
		lp.setUpperbound(new double[]{6000, 4000});
		lp.setInteger(0);
		lp.setInteger(1);
		
		System.out.println();
		//System.out.print(lp.convertToGMPL());
		
		LinearProgramSolver solver = SolverFactory.getSolver("GLPK");
		solver.solve(lp);
		// Note to Schobi: change from BigDecimal to this methods in the conversion options
		System.out.println("Abs von -2.0: " + Math.abs(-2.0));
		System.out.println("Signum von -2.0: " + Math.signum(-2.0));	  
	}

}

