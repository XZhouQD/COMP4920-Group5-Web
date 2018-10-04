package scpsolver.util;

public interface Matrix {

	public abstract double get(int row, int column);

	public abstract int getRowNum();

	public abstract int getColNum();

	public abstract Matrix transpose();

	public abstract Matrix times(Matrix multiplier);
	
	public abstract Matrix plus(Matrix toadd);  
	
	/*
	 * TODO: Shouldn't that be abstract, too?
	 */
	public void set(int row, int column, double value);

	
	public abstract NonZeroElementIterator getNonZeroElementIterator();
	
	
	/*
	 * TODO: Following operations could be reasonable for this interface:
	 * 		 - getRow(int) and getCol(int)
	 * 		 - add(Matrix)
	 */

}