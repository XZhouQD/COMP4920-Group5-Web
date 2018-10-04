package scpsolver.infeas;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import scpsolver.constraints.Constraint;
import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;
import scpsolver.problems.SolutionRenderable;
import scpsolver.util.SparseVector;
import scpsolver.util.debugging.LPDebugger;

public class LibLinearFile {

	String filename;
	ArrayList<SparseVector> features;
	ArrayList<Double> labels;
	
	
	public LibLinearFile(String filename, int dimension) {
		features = new ArrayList<SparseVector>();
		labels = new ArrayList<Double>();
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line !=  null) {
				//System.out.println(line);
				SparseVector c =  new SparseVector(dimension+1, 2);
				// bias
				c.set(dimension, 1.0);				
				String[] fields = line.split("\\s");
				//System.out.println("Field: "+fields[0]);
				double label = Double.parseDouble(fields[0]);
				labels.add(label);
				for (int i = 1; i < fields.length; i++) {
					String[] index = fields[i].split(":");
					c.set(Integer.parseInt(index[0]), Double.parseDouble(index[1]));
				}
				features.add(c);
				line = br.readLine();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public ArrayList<LinearConstraint> getConstraints(double marginsize) {
		ArrayList<LinearConstraint> lc = new ArrayList<LinearConstraint>();
		for (int i = 0; i < features.size(); i++) {
			if (labels.get(i) < 0.5) {
				lc.add(new LinearSmallerThanEqualsConstraint(features.get(i), -marginsize, "negative label " + i));
			} else {
				lc.add(new LinearBiggerThanEqualsConstraint(features.get(i), +marginsize, "positive label " + i));
			}
		}
		return lc;
	}
	
	public static void main(String[] args) {
		int dimension = 4096;
		double marginsize = 1;
		SparseVector c = new SparseVector(dimension+1,2);
		//c.set(1, 1);
		LinearProgram lp = new LinearProgram(c);
		
		for (int i = 0; i < args.length; i++) {
			String file = args[i];
			LibLinearFile lf = new LibLinearFile(file, dimension);
			lp.addConstraints(lf.getConstraints(marginsize));
			System.out.println("Anzahl: "+ lp.getConstraints().size());
		}
		
		
		

		
		//lp.setLowerbound(lp.makeDoubleArray(lp.getDimension(), Double.NEGATIVE_INFINITY));
		//lp.setUpperbound(lp.makeDoubleArray(lp.getDimension(), Double.POSITIVE_INFINITY));
		
		LinearProgramSolver solver = SolverFactory.getSolver("LPSOLVE");
		//((CPLEXSolver) solver).setLicensefile("/Users/hannes/access.ilm");
		//new LPDebugger(lp);
		//solver.setTimeconstraint(10000);
		double[] solution = solver.solve(LPDebugger.getFeasOptLPBinary(lp, 0,10));
		double[] sol = new double[lp.getDimension()];
		System.arraycopy(solution, 0, sol,0, sol.length);
		for (int i = 0; i < solution.length; i++) {
			if (Math.abs(solution[i]) > 0)
			System.out.println(i + " "+ solution[i]);
		}
		
		for (int i = lp.getDimension(); i < solution.length; i++) {
			
			if (solution[i]>0.99) {
			//	int index = i % (dimension+1);
				System.out.println(i + "\t" + solution[i]);
			}
			
		}
		for (Constraint lc : lp.getConstraints()) {
			//System.out.println( ((LinearConstraint) lc).getT() + " " + ((LinearConstraint) lc).getCSparse().dot(sol));
			if (!lc.isSatisfiedBy(sol)) {
				System.out.println(lc.getName() + " not satisfied!!");
			}
		}
	}
	
}
