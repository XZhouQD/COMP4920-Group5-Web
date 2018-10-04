package scpsolver.problems;

/**
 * Basic optimization problem format.
 * 
 * @author schober
 */
public interface Problem {

	/**
	 * Gets a string containing the type of problem that is formulated with a given instance.
	 * E. g., the <code>LinearProgram</code> class returns "Linear Program".
	 * <p>
	 * Please note, that this method will not return the actual name of a given instance. If
	 * you have two instances of linear programs, a method call will yield the same result.
	 * Therefore, two different linear programs cannot be distinguish via this method and must
	 * be kept apart somehow differently.
	 * 
	 * @return the type of problem this instance describes
	 */
	public String getName();
	
	/**
	 * Returns the value of the function at position <code>x</code>. The length of the array
	 * must be the same as the value returned by {@link getDimension}.
	 * 
	 * @param x input variable vector for the target function
	 * @return the value of the function at position <code>x</code>
	 */
	public double evaluate(double x[]);
	
	/**
	 * Returns the dimension of the input variable space.
	 * 
	 * @return the dimension of the input space
	 */
	public int getDimension();
	
	/**
	 * Specifies whether the target function of this instance is to be minimized or maximized.
	 * The default is maximization.
	 * 
	 * @return <code>true</code> if the target function is to be maximized, <code>false</code>
	 * otherwise
	 */
	public boolean isMinProblem();
	
	/**
	 * Controls if the target function is to be maximized or minimized. If no value is given
	 * prior to optimization, the function will be maximized.
	 * 
	 * @param bool <code>true</code> if this function is to be minimized, <code>false</code>
	 * otherwise
	 */
	public void setMinProblem(boolean bool);
	
	
	
}

