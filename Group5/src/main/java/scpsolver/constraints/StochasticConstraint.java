package scpsolver.constraints;

import scpsolver.util.SparseVector;

public interface StochasticConstraint extends Constraint {
	
	public LinearConstraint[] getExtensiveForm();
	
	public double[] getC();
	// FIXME: generalize to Matrix?
	public SparseVector getCSparse();
	
	public double[] getH();
	public double[][] getT();
	public SparseVector[] getTSparse();
}
