package scpsolver.lpsolver;

import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.problems.LinearProgram;

/**
 * Common interface for available linear program solvers.
 * 
 * @author schober
 */
public interface LinearProgramSolver {

	/**
	 * Tries to solve a linear program.
	 * 
	 * @param lp the linear program to be solved
	 * @return an array of the found solutions for the variables
	 */
	public double[] solve(LinearProgram lp);
	
	/**
	 * Adds a <code>LinearBiggerThanEqualsConstraint</code> to the solver.
	 * 
	 * Note that all existing constraints of a <code>LinearProgram</code> are
	 * automatically transfered by the <code>solve</code> method. Therefore, this
	 * method is only needed explicitly, if an additional constrained is to be
	 * added to the model without wanting to alter the <code>LinearProgram</code> itself.
	 * Usually, this method is needed and used by the programers in deeper parts
	 * of the code, which the user has not worry about.  
	 * 
	 * @param c the constraint to be added
	 */
	 void addLinearBiggerThanEqualsConstraint(LinearBiggerThanEqualsConstraint c);
	
	/**
	 * Adds a <code>LinearSmallerThanEqualsConstraint</code> to the solver.
	 * 
	 * Note that all existing constraints of a <code>LinearProgram</code> are
	 * automatically transfered by the <code>solve</code> method. Therefore, this
	 * method is only needed explicitly, if an additional constrained is to be
	 * added to the model without wanting to alter the <code>LinearProgram</code> itself.
	 * Usually, this method is needed and used by the programers in deeper parts
	 * of the code, which the user has not worry about.  
	 * 
	 * @param c the constraint to be added
	 */
	
	 void addLinearSmallerThanEqualsConstraint(LinearSmallerThanEqualsConstraint c);
	
	/**
	 * Adds a <code>LinearEqualsConstraint</code> to the solver.
	 * 
	 * Note that all existing constraints of a <code>LinearProgram</code> are
	 * automatically transfered by the <code>solve</code> method. Therefore, this
	 * method is only needed explicitly, if an additional constrained is to be
	 * added to the model without wanting to alter the <code>LinearProgram</code> itself.
	 * Usually, this method is needed and used by the programers in deeper parts
	 * of the code, which the user has not worry about.  
	 * 
	 * @param c the constraint to be added
	 */
	 void addEqualsConstraint(LinearEqualsConstraint c);
	
    /**
     * Returns the name of this linear program solver. This method is needed if a specific solver is requested from the solver factory.
     *
     * @return the name of the solver
     */
    public String getName();
    /**
     * Returns the names of any system library that the solver depends on. Before returning an instance of the requested solver,
     * the {@link SolverFactory} ensures that the required libraries are loaded. If the solver factory failed to load any library,
     * it will not return an instance of this solver.
     *
     * @return all libary names that this solver depends on
     */
    public String[] getLibraryNames();
    
    
    public void setTimeconstraint(int t);
    
    
}
