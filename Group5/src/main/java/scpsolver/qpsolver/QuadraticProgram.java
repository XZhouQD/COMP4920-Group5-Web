package scpsolver.qpsolver;

import java.util.ArrayList;

import scpsolver.constraints.Constraint;
import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.ConstrainedProblem;
import scpsolver.problems.MathematicalProgram;
import scpsolver.util.Matrix;
import scpsolver.util.SparseMatrix;
import scpsolver.util.SparseVector;

/* Definition of Quadratic Program as described in: http://en.wikipedia.org/wiki/Quadratic_programming
 * 
 * 
 */

/**
 * @author  planatsc
 */
public class QuadraticProgram extends MathematicalProgram implements ConstrainedProblem {

	public Matrix getQ() {
		return Q;
	}



	public void setQ(Matrix q) {
		Q = q;
	}

	Matrix Q;
 
	// TODO: Should this be a SparseVector or a general vector?
	public QuadraticProgram(Matrix q, SparseVector c) {
		super();		
		Q = q;
		this.c = c;
		this.isinteger = new boolean[this.getDimension()];
	}
	
	

	public QuadraticProgram(double[][] q, double[] pc) {
		constraints = new ArrayList<Constraint>();
		this.setQ(q);
		//this.setC(pc);
		this.c = (SparseVector) new SparseVector(pc);
		this.isinteger = new boolean[this.getDimension()];
	}

	public QuadraticProgram(double[][] q) {
		constraints = new ArrayList<Constraint>();
		this.setQ(q);
		this.c = new SparseVector(q.length,1);	
		this.isinteger = new boolean[this.getDimension()];
	}


	// TODO: Quick'n'dirty fix
	public double evaluate(double[] x) {
		SparseVector px = new SparseVector(x);
		
		// Latest version - makes it even worse, but should be a slightly bit more correct ;-)
		// Error is to be searched at SparseMatrix ...
		// Matrix result = (Matrix) px.transpose().times(Q).times(px).plus(c.transpose().times(px));
		
		Matrix partone = px.transpose().times(Q).times(px);
		partone = ((SparseVector) partone).times(0.5);
		
		Matrix parttwo = c.transpose().times(px);
		System.out.println("Here:" + parttwo.get(0, 0));
		Matrix result = partone.plus(parttwo);
		return result.get(0, 0);
		
	}
	
	public void setQ(double[][] q) {
		Q = new SparseMatrix(q);
	}
	
	
	
	/* (non-Javadoc)
	 * @see nmi.ConstrainedProblem#addConstraint(nmi.constraints.Constraint)
	 */
	public boolean addConstraint(Constraint c) {
		return constraints.add(c);
	}

	/* (non-Javadoc)
	 * @see nmi.ConstrainedProblem#isFeasable(double[])
	 */
	public boolean isFeasable(double[] x) {
		for (Constraint c : constraints) {
			if (!c.isSatisfiedBy(x)) return false;
		}	
		return true;
	}
	
	/* (non-Javadoc)
	 * @see nmi.ConstrainedProblem#isFeasable(double[])
	 */
	public ArrayList<Constraint> getViolatedContraints(double[] x) {
		ArrayList<Constraint> resultArrayList = new ArrayList<Constraint>();
		for (Constraint c : constraints) {
			if (!c.isSatisfiedBy(x)) resultArrayList.add(c);
		}	
		return resultArrayList;
	}
	
	public int getDimension() {
		return c.getSize();
	}
	
	/* (non-Javadoc)
	 * @see nmi.ConstrainedProblem#getConstraints()
	 */
	/**
	 * @return
	 * @uml.property  name="constraints"
	 */
	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}

	public String getName() {
		return "Quadratic Progam";
	}
	
	
	public boolean isMinProblem() {
		return minproblem;
	}

	public void setMinProblem(boolean bool) {
		minproblem= bool;
		
	}
	
	public static void main(String[] args) {
		 QuadraticProgram qp = new QuadraticProgram(new double[][]{{-1,0},{0,-1}},new double[]{0.0,0.0});
		 qp.addConstraint(new LinearSmallerThanEqualsConstraint(new double[]{3.0,16.0},20,"A"));
		 qp.addConstraint(new LinearBiggerThanEqualsConstraint(new double[]{1.0,0.0},2,"B"));
		 qp.addConstraint(new LinearBiggerThanEqualsConstraint(new double[]{1.0,0.0},2,"B"));
		 
		 //double[] solution = new double[]{1.0,1.0};
		 //System.out.println("Feasable: " + qp.isFeasable(solution));
		 /*ArrayList<Constraint>  violArrayList = qp.getViolatedContraints(solution);
		 for (Iterator<Constraint> iterator = violArrayList.iterator(); iterator.hasNext();) {
			Constraint constraint = (Constraint) iterator.next();
			System.out.println(constraint.getName()+ " violated!");
			
		}*/
		 qp.setMinProblem(false);
		 //qp.evaluate(new double[]{1.0,1.0});
		 
		 // TODO This might lead to problems ...
		 QuadraticProgramSolver solver = (QuadraticProgramSolver) SolverFactory.newDefault();
		 double[] sol = solver.solve(qp);
		 for (int i = 0; i < sol.length; i++) {
			System.out.println(i+" "+sol[i]);
		}
		 
	}
	
	

	
}