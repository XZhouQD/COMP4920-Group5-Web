package scpsolver.problems;

import java.util.ArrayList;

import scpsolver.util.Matrix;
import scpsolver.util.SparseVector;
import scpsolver.constraints.Constraint;

/**
 * Generalization of a mathematical optimization problem.
 * 
 * @author schober
 *
 */
public abstract class MathematicalProgram {

	/**
	 * Vector of the coefficients of the linear target function
	 */
	protected SparseVector c;
	
	/**
	 * The list of constraints for the given problem.
	 */
	protected ArrayList<Constraint> constraints;
	
	/**
	 * Controls, which variable is a double or an integer/boolean. Note: if a variable
	 * is boolean, then it's only marked integer and special boolean constraints are added.
	 */
	protected boolean[] isinteger;
	
	protected boolean[] isboolean;
	
	/**
	 * Indicates wether the linear program is a minimize or maximize problem.
	 */
	protected boolean minproblem;
	
	/**
	 * An array containing the upper bounds of the variables. If no upper bound is set, then
	 * Double.MAX_VALUE is used.
	 */
	protected double[] upperbound;
	
	/**
	 * An array containing the lower bounds of the variables. If no lower bound is set, then
	 * Double.MIN_VALUE is used.
	 */
	protected double[] lowerbound;

	// TODO: Is an initialization of the SparseVector c reasonable?
	/**
	 * Creates an empty mathematical problem.
	 */
	public MathematicalProgram() {
		super();
		constraints = new ArrayList<Constraint>();
	}

	/**
	 * Marks a variable as integer.
	 * 
	 * @param i The index of the variable
	 */
	public void setInteger(int i) {		
		getIsinteger()[i] = true;
	}

	/**
	 * Marks a variable as real number.
	 * 
	 * @param i The index of the variable.
	 */
	public void setContinous(int i) {		
		getIsinteger()[i] = false;
	}

	/**
	 * Gets the coefficient vector for a linear target function.
	 * 
	 * @return c the coefficients of the target function
	 */
	public double[] getC() {
		return c.get();
	}

	/**
	 * Sets the coefficients of the linear target function.
	 * 
	 * @param c  the c to set
	 */
	public void setC(Matrix pc) {
		c = new SparseVector(pc);
		
		isinteger  = new boolean[c.getRowNum()];
		isboolean  = new boolean[c.getRowNum()];
	}

	/**
	 * Returns the dimension of the mathematical problem, i.e. the number
	 * of free variables of the problem.
	 * 
	 * @return the number of target variables
	 */
	public int getDimension() {
		return c.getRowNum();
	}

	/**
	 * Checks if this model represents a mixed integer problem or real problem. A mixed
	 * integer problem is mathematical problem, in which at least one variable may only
	 * take integer values.
	 * 
	 * @return <code>true</code>, if at least one variable has to be an integer, otherwise
	 * <code>false</code>
	 */
	public boolean isMIP() {
		if (getIsinteger()==null) return false;
		for (int i = 0; i < getIsinteger().length; i++) {
			if (getIsinteger()[i]) return true;
		}
		return false;
	}

	/**
	 * Checks, whether the target function of this problem is to be minimized or maximized.
	 * 
	 * @return <code>true</code>, if the target function is to be minimized, otherwise
	 * <code>false</code>
	 */
	public boolean isMinProblem() {
		return minproblem;
	}

	/**
	 * Determines if the target function of this problem is to be minimized or maximized.
	 * 
	 * @param bool <code>true</code>, if the target function is to be minimized, otherwise
	 * <code>false</code>
	 */
	public void setMinProblem(boolean bool) {
		minproblem= bool;	
	}

	/**
	 * Checks if lower or upper bounds are set for the target variables of this problem.
	 * 
	 * @return <code>true</code>, if any of the lower or upper bounds are set, otherwies
	 * <code>false</code>
	 */
	public boolean hasBounds() {
		return ((lowerbound != null) && (upperbound != null));
	}

	/**
	 * Returns the vector containing the lower bounds for all target variables.
	 * 
	 * @return the lower bounds of the target variables
	 */
	public double[] getLowerbound() {
		return lowerbound;
	}

	/**
	 * Sets the lower bounds off all target variables. The length of the parameter array
	 * must be equal to the result of <code>getDimension</code>. Note also, that this
	 * sets the lower bound for all target variables at once. If no lower bound for a given
	 * variable is desired, then the array should contain the value <code>Double.MIN_VALUE</code>
	 * at this postion.
	 * 
	 * @param lowerbound an array containing the lower bounds of all free variables 
	 */
	public void setLowerbound(double[] lowerbound) {
		this.lowerbound = lowerbound;
		if (upperbound == null) setUpperbound(makeDoubleArray(lowerbound.length, Double.MAX_VALUE));
	}

	/**
	 * Returns the vector containing the upper bounds for all target variables.
	 * 
	 * @return the lower bounds of the target variables
	 */
	public double[] getUpperbound() {
		return upperbound;
		
	}

	/**
	 * Creates an array of length <code>l</code> containing the value <code>d</code> at
	 * every position.
	 * 
	 * @param l the length of the desired array
	 * @param d the value at every position of the array
	 * @return an array of length l containing all <code>d</code> values
	 */
	public static double[] makeDoubleArray(int l, double d) {
		double[] res = new double[l];
		for (int i = 0; i < res.length; i++) {
			res[i] =d;
		}
		return res;
		
	}

	/**
	 * Sets the upper bounds off all target variables. The length of the parameter array
	 * must be equal to the result of <code>getDimension</code>. Note also, that this
	 * sets the upper bound for all target variables at once. If no upper bound for a given
	 * variable is desired, then the array should contain the value <code>Double.MAX_VALUE</code>
	 * at this postion.
	 * 
	 * @param upperbound an array containing the upper bounds of all free variables 
	 */
	public void setUpperbound(double[] upperbound) {
		this.upperbound = upperbound;
		if (lowerbound == null) setLowerbound(makeDoubleArray(upperbound.length, - Double.MAX_VALUE));
	}

	/**
	 * Marks target variables to be integer, i.e. variables that must take integer values for
	 * valid solutions. All variables are set at one method call. Variables that are required
	 * to take integer values only for solutions should be marked <code>true</code>, whereas
	 * positions of variables which may also take real values in possible solutions are to
	 * be marked <code>false</code>.
	 * 
	 * @param isinteger an array of the length equal to the dimension with values indicating
	 * which variables shall take integer values and which can take real values
	 */
	public void setIsinteger(boolean[] isinteger) {
		this.isinteger = isinteger;
	}

	/**
	 * Returns an array indicating integer variables and real variables. Positions of integer
	 * variables are marked <code>true</code>, positions of real variables are marked <code>false</code>.
	 * 
	 * @return an array containing indicators, which variables ought to be integer and which
	 * may take real values
	 */
	public boolean[] getIsinteger() {
		return isinteger;
	}
	
	public boolean[] getIsboolean() {
		return isboolean;
	}

}