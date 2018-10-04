package scpsolver.constraints;

import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.util.SparseVector;

/**
 * Constraint format of a linear program.
 * 
 * @author schober
 *
 */
// TODO Wouldn't this be a better abstract super class?
public interface LinearConstraint extends Constraint {


	/**
	 * Adds one line representing this constraint to the constraint matrix of
	 * a given solver.
	 * 
	 * @param solver an instance of a linear program solver
	 */
	public void addToLinearProgramSolver(LinearProgramSolver solver);
	
	/**
	 * Returns the weight vector of this constraint.
	 * <p>
	 * For easier constraint manipulation, SCPSolver saves not one big
	 * constraint matrix as the formal description of a linear problem
	 * suggests, but creates a line for line representation of this
	 * matrix. Therefore, a formal description of this vector is the
	 * <code>i</code>-th line of <code>A</code>, if this constraint
	 * is the <code>i</code>-th constraint.
	 * 
	 * @return the weight vector of this constraint
	 */
	public double[] getC();
	
	/**
	 * Returns the target value of this constraint.
	 * 
	 * @return the target value of this constraint
	 */
	public double getT();
	
	/**
	 * Returns a sparse representation of the weight vector.
	 * 
	 * @return a sparse representation of the weight vector
	 * @see getC
	 */
	public SparseVector getCSparse();

}
