package scpsolver.util;

import java.util.Iterator;
import java.util.Map.Entry;

public class SparseMatrixNonZeroElementIterator implements NonZeroElementIterator {
	
	SparseMatrix m;
	Iterator<Entry<Integer, SparseVector>> entryiterator;
	SparseVectorNonZeroElementIterator actualrowit;
	int i;
	int j;
	int index;
	
	/**
	 * @param m
	 */
	public SparseMatrixNonZeroElementIterator(SparseMatrix m) {
		super();
		this.m = m;
		entryiterator = m.vvectorlist.entrySet().iterator();
		
	}

	public int getActuali() {
		// TODO Auto-generated method stub
		return i;
	}

	public int getActualj() {
		// TODO Auto-generated method stub
		return j;
	}

	public boolean hasNext() {
		// TODO Auto-generated method stub
		while (((actualrowit==null) || !actualrowit.hasNext()) && entryiterator.hasNext()) {
			Entry<Integer, SparseVector> e = entryiterator.next();
			i = e.getKey();
			actualrowit = new SparseVectorNonZeroElementIterator(e.getValue());				
		}
		if (actualrowit.hasNext()) {
			return true;
		}
		return false;
	}

	public Double next() {
		// TODO: stirbt wenn ihm ne Zeile unterkommt die nix enthält..
		if ((actualrowit==null)  || !actualrowit.hasNext()) {
			Entry<Integer, SparseVector> e = entryiterator.next();
			i = e.getKey();
			actualrowit = new SparseVectorNonZeroElementIterator(e.getValue());
			
		} 
		double val = actualrowit.next();
		j = actualrowit.getActualj();
		//System.out.println("VAL:" + val);
		return val;	
	
	}

	public void remove() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
			SparseMatrix sm = new SparseMatrix(1000,1000);
			sm.set(23, 42, 3.14);
			sm.set(42, 23, 121.14);	    
			NonZeroElementIterator nze = new SparseMatrixNonZeroElementIterator(sm);
		    while (nze.hasNext()) {
		    	double val = nze.next();
		    	System.out.println(val +  " " + nze.getActuali() + " "  + nze.getActualj() );
		    }
	}

}
