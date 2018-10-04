package scpsolver.util.debugging;

import java.util.ArrayList;

import scpsolver.constraints.Constraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.problems.LinearProgram;

public interface InfeasibleContraintSetFinder {

	public ArrayList<Constraint> getMinimalInfeasibleConstraintSet(LinearProgram lp);
	public ArrayList<Constraint> getMinimalInfeasibleConstraintSet(LinearProgram lp, ArrayList<Constraint>  constraintset);
	public void setSolver(LinearProgramSolver lp);
	
}
