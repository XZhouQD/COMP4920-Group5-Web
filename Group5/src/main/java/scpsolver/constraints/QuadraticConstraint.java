package scpsolver.constraints;


import scpsolver.qpsolver.QuadraticProgramSolver;
import scpsolver.util.Matrix;
import scpsolver.util.SparseVector;

public interface QuadraticConstraint extends Constraint {
	
	public void addToQuadraticProgramSolver(QuadraticProgramSolver solver);
	public Matrix getQ();
	public double[] getC();

	public SparseVector getCSparse();
	public double getT();
	
	
	
}
