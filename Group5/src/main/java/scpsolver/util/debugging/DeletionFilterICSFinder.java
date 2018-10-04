package scpsolver.util.debugging;

import java.util.ArrayList;

import scpsolver.constraints.Constraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;

/**
 * @author hannes
 * 
 * Deletion filtering algorithm as described in "Locating Minimal Infeasible Constraint Sets in Linear Programs" 
 * Chinnek/Dravnieks, ORSA Journal on Computing Vol.3, No. 2,Spring 1991 
 */
public class DeletionFilterICSFinder implements InfeasibleContraintSetFinder {

	LinearProgramSolver solver;
	
	public DeletionFilterICSFinder() {
		solver = SolverFactory.newDefault();
	}
	
	public ArrayList<Constraint> getMinimalInfeasibleConstraintSet(LinearProgram lp, ArrayList<Constraint> constraintset) {
		
		ArrayList<Constraint> ISFC = constraintset;
		
		ArrayList<Constraint> IIS = new ArrayList<Constraint>();
		
		ArrayList<Constraint> Q = (ArrayList<Constraint>) ISFC.clone(); 
		
		for (Constraint constraint : ISFC) {
			ArrayList<Constraint> Qp = (ArrayList<Constraint>) Q.clone(); 
			Qp.remove(constraint);
			LinearProgram lpp = new LinearProgram(lp);
			lp.setConstraints(Qp);
			
			System.out.println("Checking set: ");
			for (Constraint constraint2 : Qp) {
				System.out.println(constraint2.getName());
			}
			
			double[] solution = solver.solve(lpp);
			if (solution == null) {
				//System.out.println("NOT FEASIBLE");
				IIS.add(constraint);
				Q.remove(constraint);
				
			} else {
				//System.out.println("IS FEASIBLE");
				IIS.add(constraint);
				return IIS;
			}
		}
		
		// TODO Auto-generated method stub
		return IIS;
	}

	public ArrayList<Constraint> getMinimalInfeasibleConstraintSet(LinearProgram lp) {
		
		return getMinimalInfeasibleConstraintSet(lp, lp.getConstraints());
	}
	
	public void setSolver(LinearProgramSolver solver) {
		this.solver = solver;
	}


}
