package scpsolver.constraints;

/**
 * Basic format for a constraint of a constrained mathematical optimization problem.
 * <p>
 * The most common constraints are the <code>LinearConstraint</code>s, valid for the
 * <code>LinearProgram</code> problem description.
 * 
 * @author schober
 *
 */
public interface Constraint {
	

	/**
	 * Returns the name of a specific constraint.
	 * <p>
	 * When creating a new constraint for a problem, a name can be given to this constraint
	 * to identify it later and see, which specific constrains hold and maybe which contradict
	 * each other.
	 * <p>
	 * Please note: unlike {@link Problem}, where the name describes the type of problem
	 * instead of the actual instance, here the name the describes the actual instance and not
	 * the generic type.
	 * 
	 * @return the name of the given constraint
	 */

	public String getName();	
	
	/**
	 * Evaluates the constraint at input value <code>x</code> and checks if the constraint
	 * holds. Returns <code>true</code> if input <code>x</code> satisfies the constraint,
	 * <code>false</code> otherwise. The input dimension (i.e. the <code>array.length</code>), must be the same
	 * as the target function.
	 * 
	 * @param x the position, where the constraint is to be evaluated
	 * @return <code>true</code> if input value <code>x</code> satisfies this constraint,
	 * <code>false</code> otherwise
	 */

	public boolean isSatisfiedBy(double x[]);
	
	/**
	 * Returns the right hand side.
	 * @return  right hand side
	 */
	public double getRHS();

}
