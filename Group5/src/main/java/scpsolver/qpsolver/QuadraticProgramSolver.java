package scpsolver.qpsolver;

import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.constraints.QuadraticSmallerThanEqualsContraint;

public interface QuadraticProgramSolver {

	public double[] solve(QuadraticProgram qp);
	public void addLinearBiggerThanEqualsConstraint(LinearBiggerThanEqualsConstraint c);
	public void addLinearSmallerThanEqualsConstraint(LinearSmallerThanEqualsConstraint c);
	public void addQuadraticSmallerThanEqualsContraint(QuadraticSmallerThanEqualsContraint c);
	public void addEqualsConstraint(LinearEqualsConstraint c);
	

}
