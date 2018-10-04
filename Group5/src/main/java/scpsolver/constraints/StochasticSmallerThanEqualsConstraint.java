package scpsolver.constraints;

import scpsolver.util.SparseVector;

public class StochasticSmallerThanEqualsConstraint extends
		StochasticAbstractConstraint {

	public StochasticSmallerThanEqualsConstraint(double[][] t, double[] pc, double[] h,
			String name) {
		super(t, pc, h, name);
	}
	
	public StochasticSmallerThanEqualsConstraint(SparseVector[] t, SparseVector c, double[] h,
			String name) {
		super(t, c, h, name);
	}

	@Override
	public LinearConstraint[] getExtensiveForm() {
		LinearConstraint[] l = new LinearConstraint[h.length];
		for (int i = 0; i < l.length; i++) {
			double[] weights = new double[w.getSize()*l.length + t[i].getSize()];
			System.arraycopy(t[i].get(), 0, weights, 0, t[i].getSize());
			System.arraycopy(w.get(), 0, weights, t[i].getSize() + i*w.getSize(), w.getSize());
			
			l[i] = new LinearSmallerThanEqualsConstraint(weights, h[i], name + " - Scenario " + (i+1));
		}
		return l;
	}

	@Override
	public boolean isSatisfiedBy(double[] x) {
		double[] real_x = new double[w.getSize()];
		for (int i = 0; i < h.length; i++) {
			double[] real_y = new double[t[i].getSize()];
			System.arraycopy(x, w.getSize() + i*t[i].getSize(), real_y, 0, t[i].getSize());
			if (!(w.dot(real_x) + t[i].dot(real_y) - (Math.pow(10, -6)) <= h[i]))
				return false;
		}
		return true;
	}
	
	@Override
	public double getRHS() {
		// TODO Auto-generated method stub
		return 0;
	}

}
