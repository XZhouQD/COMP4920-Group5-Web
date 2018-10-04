package scpsolver.constraints;

import scpsolver.qpsolver.QuadraticProgramSolver;
import scpsolver.util.Matrix;
import scpsolver.util.SparseMatrix;
import scpsolver.util.SparseVector;


/**
 * @author  planatsc
 */
public class QuadraticSmallerThanEqualsContraint implements QuadraticConstraint {
	
	Matrix Q;
	SparseVector c;
	double t;
	String name;
	
	public QuadraticSmallerThanEqualsContraint(Matrix q, SparseVector c, double t, String name) {
		super();
		Q = q;
		this.c = c;
		this.t = t;
		this.name = name;
	}
	
	public QuadraticSmallerThanEqualsContraint(double[][] pq, double[] c, double t, String name) {
		super();
		Q = new SparseMatrix(pq);
		this.c = new SparseVector(c);
		this.t = t;
		this.name = name;
	}

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return "name";
	}

	// TODO Is this really the definition of a quadratic constraint? See Wikipedia ...
	public boolean isSatisfiedBy(double[] x) {
		
		Matrix px = new SparseVector(x);
		Matrix result = px.transpose().times(Q).times(px).plus(c.transpose().times(px));
				
		return (result.get(0, 0)<=t);
	}

	public void addToQuadraticProgramSolver(QuadraticProgramSolver solver) {
		solver.addQuadraticSmallerThanEqualsContraint(this);
	}

	public double[] getC() {
		return c.get();
	}

	public Matrix getQ() {
		return Q;
	}

	public double getT() {
		return t;
	}

	public SparseVector getCSparse() {
		return c;
	}
	
	public double getRHS() {
		// TODO Auto-generated method stub
		return t;
	}

}
