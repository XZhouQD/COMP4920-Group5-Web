package scpsolver.problems;

import java.util.ArrayList;

import scpsolver.constraints.Constraint;
import scpsolver.constraints.LinearConstraint;
import scpsolver.constraints.StochasticConstraint;
import scpsolver.util.SparseVector;

/**
 * This class represents a stochastic program, that is, a linear program that
 * additionally includes different scenarios with given probabilities. This implementation
 * assumes that the variables can be split into disjoint sets: a deterministic set
 * of variables, which are not influenced by scenarios of certain probabilities (i.e.
 * variables, which have the same values in all scenarios), and another set of variables
 * which weights are not pre-determined.
 * 
 * The theory of stochastic programming shows that the optimal solution of a stochastic
 * optimization problem is not merely a weighted average of the underlying linear
 * optimization problems, and is therefore entitled a representation of its own. On the
 * other hand, a stochastic optimization problem can be transformed into a linear one.
 * 
 * Although there are additional algorithms, which exploit the specific problem structure
 * of stochastic optimization problems, the SCPSolver-suite does not take them into account
 * yet. However, this class and the corresponding constraint classes help to easily formulate
 * a stochastic problem and transform it automatically into its linear representation.
 * 
 * @author schober
 *
 */
public class StochasticProgram extends MathematicalProgram implements
		ConstrainedProblem {
	
	/**
	 * Probability vector for the set of scenarios
	 */
	private double[] probabilities;
	
	/**
	 * The weights of each non-deterministic variable in the different scenarios.
	 */
	private SparseVector[] scenarios;
	
	/**
	 * Creates a new stochastic problem with given deterministic weights, probabilities
	 * of different scenarios and variable weights in these scenarios.
	 * 
	 * @param pc variable weights for the deterministic variables
	 * @param probs probabilites of the different scenarios
	 * @param scens variable weights for the non-deterministic variables
	 */
	public StochasticProgram (double[] pc, double[] probs, double[][] scens) {
		super();
		minproblem = false;
		constraints = new ArrayList<Constraint>();
		this.c = new SparseVector(pc);
		this.probabilities = probs;
		
		this.scenarios = new SparseVector[scens.length];
		for (int i = 0; i < scens.length; i++)
			scenarios[i] = new SparseVector(scens[i]);
	}
	
	public StochasticProgram (SparseVector c, double[] probs, SparseVector[] scens) {
		super();
		minproblem = false;
		constraints = new ArrayList<Constraint>();
		this.c = c;
		this.probabilities = probs;
		this.scenarios = scens;
	}
	
	public boolean addConstraint(LinearConstraint c) {
		return constraints.add(c);
	}
	
	public boolean addConstraint(StochasticConstraint c) {
		return constraints.add(c);
	}

	//@Override
	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}
	
	//@Override
	public void setUpperbound(double[] bounds) {
		double[] real_bounds = new double[c.getSize() + probabilities.length*scenarios[0].getSize()];
		System.arraycopy(bounds, 0, real_bounds, 0, c.getSize());
		for (int i = 0; i < scenarios.length; i++)
			System.arraycopy(bounds, c.getSize(), real_bounds, c.getSize() + i*scenarios[i].getSize(), scenarios[i].getSize());
		
		super.setUpperbound(real_bounds);
	}
	
	//@Override
	public void setLowerbound(double[] bounds) {
		double[] real_bounds = new double[c.getSize() + probabilities.length*scenarios[0].getSize()];
		System.arraycopy(bounds, 0, real_bounds, 0, c.getSize());
		for (int i = 0; i < scenarios.length; i++)
			System.arraycopy(bounds, c.getSize(), real_bounds, c.getSize() + i*scenarios[i].getSize(), scenarios[i].getSize());
		
		super.setLowerbound(real_bounds);
	}
	


	//@Override
	public boolean isFeasable(double[] x) {
		for (Constraint c : constraints) {
			if (c instanceof LinearConstraint) {
				double[] real_x = new double[this.c.getSize()];
				System.arraycopy(x, 0, real_x, 0, this.c.getSize());
				if (!c.isSatisfiedBy(real_x)) return false;
			}
			else if (c instanceof StochasticConstraint)
				if (!c.isSatisfiedBy(x)) return false;
		}	
		return true;
	}

	////@Override
	public double evaluate(double[] x) {
		double[][] real_y = new double[probabilities.length][];
		for (int i = 0; i < real_y.length; i++)
			System.arraycopy(x, c.getSize() + i*scenarios[i].getSize(), real_y[i], 0, scenarios[i].getSize());
		
		double[] real_x = new double[c.getSize()];
		System.arraycopy(x, 0, real_x, 0, c.getSize());
		
		double result = c.dot(real_x);
		for (int i = 0; i < real_y.length; i++)
			result += probabilities[i]*scenarios[i].dot(real_y[i]);
		
		return result;
	}

	////@Override
	public String getName() {
		return "Stochastic Program";
	}
	
	public LinearProgram getExtensiveForm() {
		int K = scenarios[0].getSize();
		double[] weights = new double[c.getSize() + probabilities.length*K];
		System.arraycopy(c.get(), 0, weights, 0, c.getSize());
		for (int i = 0; i < probabilities.length; i++)
			for (int j = 0; j < K; j++)
				weights[c.getSize()+i*K+j] = probabilities[i]*scenarios[i].get(j);
		
		LinearProgram ef = new LinearProgram(weights);
		
		for (Constraint c : constraints) {
			if (c instanceof LinearConstraint)
				ef.addConstraint((LinearConstraint) c);
			else if (c instanceof StochasticConstraint)
				for (LinearConstraint l : ((StochasticConstraint) c).getExtensiveForm())
					ef.addConstraint(l);
		}
		
		ef.setLowerbound(this.getLowerbound());
		ef.setUpperbound(this.getUpperbound());
		ef.setMinProblem(this.isMinProblem());
		
		return ef;
	}

}
