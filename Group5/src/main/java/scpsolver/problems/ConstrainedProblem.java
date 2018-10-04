package scpsolver.problems;

import java.util.ArrayList;

import scpsolver.constraints.Constraint;

/**
 * Description of a constrained optimization problem.
 * 
 * @author schober
 *
 */
public interface ConstrainedProblem extends Problem {
	
	/**
	 * Determines whether all constrains are fulfilled at input value <code>x</code>.
	 * <code>isFeasable</code> will iterate over all constrains of an optimization problem
	 * and check if all of them hold. <code>isFeasable</code> will then return
	 * <code>true</code>, otherwise it will return <code>false</code>.
	 * <p>
	 * The length of the array must be the same as the value returned by {@link getDimension}.
	 * 
	 * @param x input variable vector
	 * @return <code>true</code> if all constrains hold, <code>false</code> otherwise
	 */
	public boolean isFeasable(double x[]);
	
	/**
	 * Returns a list of all constrains a valid solution must keep.
	 * 
	 * @return the constrains a valid solution must keep
	 */
	public ArrayList<Constraint> getConstraints();
	
	
}
