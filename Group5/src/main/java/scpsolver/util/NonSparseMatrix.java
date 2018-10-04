package scpsolver.util;

public class NonSparseMatrix implements Matrix {

	double[][] m;
	
	public NonSparseMatrix(double[][] m) {
		this.m = m;
	}
	@Override
	public double get(int row, int column) {
		
		return m[row][column];
	}

	@Override
	public int getColNum() {
		// TODO Auto-generated method stub
		return m[0].length;
	}

	@Override
	public NonZeroElementIterator getNonZeroElementIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRowNum() {
		// TODO Auto-generated method stub
		return m.length;
	}

	@Override
	public Matrix plus(Matrix toadd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set(int row, int column, double value) {
		m[row][column] = value;	
	}

	@Override
	public Matrix times(Matrix multiplier) {
		
		double[][] result = new double[this.getRowNum()][multiplier.getColNum()];
		for (int row = 0; row < this.getRowNum(); row++) {
			for (int column = 0; column < multiplier.getColNum(); column++) {
    			double sum = 0;
    			for (int ii =0; ii < this.getColNum();ii++) {
    				sum += m[row][ii]* multiplier.get(ii,column);
    			}
    			result[row][column] = sum;
    		}
		}
		// TODO Auto-generated method stub
		return new NonSparseMatrix(result);
	}

	@Override
	public Matrix transpose() {
		double[][] result = new double[this.getColNum()][this.getRowNum()];
		for (int row = 0; row < this.getRowNum(); row++) {
			for (int column = 0; column < this.getColNum(); column++) {
				result[column][row] = m[row][column]; 
			}
		}
		return new NonSparseMatrix(m);
	}
	
	

}
