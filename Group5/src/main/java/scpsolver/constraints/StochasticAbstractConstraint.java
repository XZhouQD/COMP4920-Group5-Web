package scpsolver.constraints;

import scpsolver.util.SparseVector;

public abstract class StochasticAbstractConstraint implements
		StochasticConstraint {
	
	protected SparseVector w;
	protected SparseVector[] t;
	protected double[] h;
	protected String name;
	
	protected StochasticAbstractConstraint (SparseVector[] t, SparseVector c, double[] h, String name) {
		super();
		this.w = c;
		this.t = t;
		this.h = h;
		this.name = name;
	}
	
	protected StochasticAbstractConstraint (double[][] t, double[] pc, double[] h, String name) {
		super();
		this.w = new SparseVector(pc);
		this.t = new SparseVector[t.length];
		for (int i = 0; i < t.length; i++)
			this.t[i] = new SparseVector(t[i]);
		this.h = h;
		this.name = name;
	}

	//@Override
	public double[] getC() {
		return w.get();
	}

	//@Override
	public SparseVector getCSparse() {
		return w;
	}

	//@Override
	public abstract LinearConstraint[] getExtensiveForm();

	//@Override
	public double[] getH() {
		return h;
	}

	//@Override
	public double[][] getT() {
		double[][] result = new double[t.length][];
		for (int i = 0; i < result.length; i++)
			result[i] = t[i].get();
		
		return result;
	}

	//@Override
	public SparseVector[] getTSparse() {
		return t;
	}

	//@Override
	public String getName() {
		return name;
	}

	//@Override
	public abstract boolean isSatisfiedBy(double[] x);
	
	public double getRHS() {
		// TODO Auto-generated method stub
		return 0;
	}

}
