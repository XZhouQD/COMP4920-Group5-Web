package scpsolver.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;

import scpsolver.graph.DotPlot;

/**
 * Implementation of a sparse matrix. The actual values of the matrix are saved
 * in a SparseVector.
 * 
 * @author planatsc
 *
 */
public class SparseMatrix implements  Matrix {

	/**
	 * Contains all rows with non-zero elements as SparseVectors.
	 */
	HashMap<Integer, SparseVector> vvectorlist;
	/**
	 * The number of rows of the matrix
	 */
	int rownum;
	/**
	 * The number of columns of the matrix
	 */
	int colnum;
	

	int initrowdim = 4;
	/**
	 * Creates a new empty SparseMatrix
	 * 
	 * @param rows The number of rows of the new matrix
	 * @param columns The number of columns of the new matrix
	 */
	public SparseMatrix(int rows, int columns) {
		//System.out.println("Creating new Matrix " + rows +"x" + columns);
		rownum = rows;
		colnum = columns;
		initrowdim = 4;
		vvectorlist = new HashMap<Integer,SparseVector>();  
	}
	
	public SparseMatrix(int rows, int columns, int initrowdim) {
		rownum = rows;
		colnum = columns;
		this.initrowdim = initrowdim;
		vvectorlist = new HashMap<Integer,SparseVector>();  
	}
	
	
	public static SparseMatrix readMTX(String filename) {
		try {
			FileReader fr = new FileReader(filename);
			//BufferedReader br = new BufferedReader(fr);
			StreamTokenizer st = new StreamTokenizer(new BufferedReader(fr));
			st.commentChar('%');
			st.nextToken();
			int nrow = (int) st.nval;
			st.nextToken();
			int ncol = (int) st.nval;
			st.nextToken();
//			int nonzero = (int) st.nval;
			SparseMatrix result = new SparseMatrix(nrow,ncol);
			int token = st.nextToken();
			while (token != StreamTokenizer.TT_EOF) {
				int i = (int)st.nval;
				st.nextToken();
				int j = (int)st.nval;
				st.nextToken();
				double value = st.nval;
				result.set(i, j, value);
				token = st.nextToken();
				//System.out.println(i + " " + j+ " " + value);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
		
		
		
	}
	
	/**
	 * Copy constructor for the SparseMatrix class
	 * @param m The copied matrix
	 */
	public SparseMatrix(SparseMatrix m) {
		this.rownum = m.rownum;
		
		
		this.colnum = m.colnum;
		this.vvectorlist = new HashMap<Integer,SparseVector>();
		

		Iterator<Entry<Integer, SparseVector>> entryiterator = m.vvectorlist.entrySet().iterator();
		
		while (entryiterator.hasNext()) {
			Entry<Integer, SparseVector> e = entryiterator.next();
			int i = e.getKey();
			SparseVector row = e.getValue();
			this.vvectorlist.put(i, row.clone());
		}
	}
	
	/**
	 * Returns a deep copy of the matrix
	 */
	public SparseMatrix clone() {
		return new SparseMatrix(this);

	} 
	
	/**
	 * Creates a new matrix with elements specified in q
	 * @param q The elements of the new matrix
	 */
	public SparseMatrix(double[][] q) {
		this.set(q);
	}
	
	/**
	 * Sets all the entries of the matrix to the values supplied by the array
	 * @param q The new entries of the matrix
	 */
	public void set(double[][] q) {
		vvectorlist = new HashMap<Integer,SparseVector>();
		for (int i = 0; i < q.length; i++) {
			this.vvectorlist.put(i, new SparseVector(q[i]));
		}
		this.rownum = q.length;
		this.colnum = q[0].length;
	}

	/* (non-Javadoc)
	 * @see scpsolver.util.Matrix#get(int, int)
	 */
	/**
	 * Returns a given element of the matrix
	 * @param row Specifies the row of the given element
	 * @param column Specifies the column of the given element
	 */
	public double get(int row, int column) {
		if (!vvectorlist.containsKey(row)) return 0;
		return vvectorlist.get(row).get(column);
	}

	/**
	 * Sets a single entry of the matrix to the given value
	 * @param row Specifies the row of the altered element
	 * @param column Specifies the column of the altered element
	 * @param value The new value of the entry
	 */
	public void set(int row, int column, double value) {
		SparseVector mrow = vvectorlist.get(row);
		if (mrow == null) mrow = initRow(row);
		mrow.set(column, value);
	}
	
	/**
	 * Initializes a new row in the matrix, if the row didn't contain non-zero elements before
	 * @param row The row that now contains new data
	 * @return The new row
	 */
	// TODO: No one touches my data! Check if setting the PS to 'private' won't affect any other code ...
	private SparseVector initRow(int row) {
		SparseVector newrow = new SparseVector(this.getColNum(),initrowdim);
		vvectorlist.put(row, newrow);
		return newrow;
	}
	
	/* (non-Javadoc)
	 * @see scpsolver.util.Matrix#getRowNum()
	 */
	public int getRowNum() {
		return rownum;
	}
	
	/* (non-Javadoc)
	 * @see scpsolver.util.Matrix#getColNum()
	 */
	public int getColNum() {
		return colnum;
	}
		
	/* (non-Javadoc)
	 * @see scpsolver.util.Matrix#transpose()
	 */
	/**
	 * Calculates the transpose matrix
	 * @return The transposed matrix
	 */
	public Matrix transpose() {
		SparseMatrix trans = new SparseMatrix(this.getColNum(), this.getRowNum());
		
//		ListIterator<SparseVector> rowIterator = this.vvectorlist.listIterator();
		Iterator<Entry<Integer, SparseVector>> entryiterator = this.vvectorlist.entrySet().iterator();
		
		while (entryiterator.hasNext()) {
			Entry<Integer, SparseVector> e = entryiterator.next();
			int i = e.getKey();
			SparseVector row = e.getValue();
			int[] indices = row.getIndex();
			double[] data = row.getData();
			for (int j = 0; j < row.getUsed(); j++) {
				trans.set(indices[j], i, data[j]);
			}
			
		}

		return trans;
	}

	/* (non-Javadoc)
	 * @see scpsolver.util.Matrix#times(scpsolver.util.Matrix)
	 */
	/**
	 * Multiplies a matrix with another matrix
	 * @param multiplier The matrix with which this matrix should be multiplied
	 * @return this*multiplier
	 */
	public Matrix times(Matrix multiplier) {
		Matrix result = null;
		
		if (multiplier instanceof SparseMatrix)  {
			//System.out.println("Multiplier is a SparseMatrix");
			
			SparseMatrix transmultiplier = (SparseMatrix)  multiplier.transpose();
			if (this.getSparsity() < 0.2) {

			result = new SparseMatrix(this.getRowNum(), multiplier.getColNum());
			
			Iterator<Entry<Integer, SparseVector>> entryiterator = this.vvectorlist.entrySet().iterator();
			
			while (entryiterator.hasNext()) {
				Entry<Integer, SparseVector> e = entryiterator.next();
				int i = e.getKey();
				SparseVector row = e.getValue();
	
				Iterator<Entry<Integer, SparseVector>> innerentryiterator = transmultiplier.vvectorlist.entrySet().iterator();
				
				while (innerentryiterator.hasNext()) {
					Entry<Integer, SparseVector> ie = innerentryiterator.next();
					int j = ie.getKey();
					SparseVector col =  ie.getValue();
					result.set(i, j, row.dot(col));
				}
	
			
			}
			} else {
				//System.out.println("Deflating sparse matrices");
				NonSparseMatrix m1 = this.getNonSparseMatrix();
				NonSparseMatrix m2 = ((SparseMatrix) multiplier).getNonSparseMatrix();
				result = m1.times(m2);
			
			}
			
		} else if (multiplier instanceof SparseVector) {
			SparseVector multiplierv = (SparseVector) multiplier;
			if (multiplierv.linevector) {
				// mx1 * 1xn => mxn 
				
				//result = vvector[0].times(multiplierv);
				result = this.vvectorlist.get(0).times(multiplierv);
			} else {
				result = new SparseVector(this.getRowNum(),4);
				
				Iterator<Entry<Integer, SparseVector>> entryiterator = this.vvectorlist.entrySet().iterator();
				
				while (entryiterator.hasNext()) {
					Entry<Integer, SparseVector> e = entryiterator.next();
					int i = e.getKey();
					SparseVector row = e.getValue();
					result.set(i, 0,  row.dot(multiplierv));
				}
				
				
			}
			
			
		}
		
		return result;
		
	}
	
	/**
	 * Adds a matrix to the matrix
	 * @param m The matrix which should be added to this matrix
	 * @return this+m
	 */
	// TODO: Survival of the fittest - which addition code will last?
	public Matrix plus(Matrix m) {
		SparseMatrix result = this.clone();
		if (m instanceof SparseMatrix) {
			SparseMatrix ms = (SparseMatrix)  m;
			Iterator<Entry<Integer, SparseVector>> entryiterator = ms.vvectorlist.entrySet().iterator();
			
			while (entryiterator.hasNext()) {
				Entry<Integer, SparseVector> e = entryiterator.next();
				int i = e.getKey();
				SparseVector row = e.getValue();
				SparseVector thisrow = result.vvectorlist.get(i);
				if (thisrow != null) {
					thisrow.add(row);
				} else {
					result.vvectorlist.put(i, row.clone());
				}
			}
		} else {
			// dumb 
			for (int i = 0; i < this.rownum; i++) {
				for (int j = 0; j < this.colnum; j++) {
					result.set(i, j, result.get(i, j) + m.get(i, j));
				}
			}
		}
		return result;
	}

	/**
	 * Compares two matrices. Notice: if the Object o isn't a probable SparseMatrix, it can't
	 * be compared yet.
	 * @param o The matrix which should be compared
	 * @return True, if this matrix and o provide the same values, false otherwise.
	 */
	public boolean equals (Object o) {
		if (!(o instanceof SparseMatrix))
			throw new IllegalArgumentException("Illegal argument: object isn't of type SparseMatrix");
		return equals((SparseMatrix) o);
	}
	
	/*
	 * TODO: Maybe a getRow(int) and getCol(int) are helpful, so we could complete SpareVectors
	 */
	/**
	 * Compares two SparseMatrices
	 * 
	 * @param A
	 * 			The matrix to be compared to
	 * @return
	 * 			True, if the second matrix provides the same values, false otherwise. Note that the
     * 			matrices don't need to be identical in the data-arrays, so it's okay when one matrix
     * 			explicitly safes a zero argument and the other doesn't.
	 */
	public boolean equals (SparseMatrix A) {
		if (this.getRowNum() != A.getRowNum() || 
			this.getColNum() != A.getColNum())
			return false;
		
		for (int i = 0; i < getRowNum(); i++)
			for (int j = 0; j < getColNum(); j++)
				if (Math.abs(this.get(i, j) - A.get(i, j)) > 0.0001) {
					/*
					System.out.println("!equal at i: " + i + ", j: " + j +
									   ", Value A: " + A.get(i, j) + ", Value this: " + this.get(i,j));
					*/
					return false;
				}
		
		return true;
		
	}
	
	/**
	 * Returns a quadratic submatrix with range [from, to)
	 * 
	 * @param from
	 * 			First row and column to be copied
	 * @param to
	 * 			First row and column not to be copied anymore
	 * @return
	 * 		The submatrix
	 */
	public SparseMatrix submatrix (int from, int to) {
		
		SparseMatrix result = new SparseMatrix(to-from, to-from);
		
		Iterator<Entry<Integer, SparseVector>> entryiterator = this.vvectorlist.entrySet().iterator();
		
		while (entryiterator.hasNext()) {
			Entry<Integer, SparseVector> e = entryiterator.next();
			int i = e.getKey();
			if (from <= i && i < to) {
				SparseVector row = e.getValue();
				int[] indices = row.getIndex();
				double[] data = row.getData();
				for (int j = 0; j < row.getUsed(); j++)
					if (from <= indices[j] && indices[j] < to)
						result.set(i-from, j-from, data[j]);
			}
			
		}
		
		return result;
	}
	
	/**
	 * Adds two matrices
	 * 
	 * @param B
	 * 		The added matrix
	 * @return
	 * 		This+B
	 */
	/*
	 * TODO: Add some error checking in matrix addition
	 * 
	 * TODO: Is it faster with the new iterator? Maybe so ...
	 */
	// TODO: Survival of the fittest - which addition code will last?
	public Matrix add (Matrix B) {
		if (!(B instanceof SparseMatrix))
			throw new IllegalArgumentException("Adding matrices is yet only supported for SparseMatrices");
		
		SparseMatrix result = new SparseMatrix ((SparseMatrix) B);
		
		Iterator<Entry<Integer, SparseVector>> entryiterator = this.vvectorlist.entrySet().iterator();
		while(entryiterator.hasNext()) {
			Entry<Integer, SparseVector> e = entryiterator.next();
			int i = e.getKey();
			if (!result.vvectorlist.containsKey(i)) {
				result.vvectorlist.put(i, new SparseVector(e.getValue()));
			}
			else {
				result.vvectorlist.put(i, e.getValue().add(result.vvectorlist.get(i)));
			}	
		}
		
		return result;
	}
	
	/**
	 * Grows the matrix with zero-elements
	 * @param size
	 * 			The new size of the matrix
	 * @param direction
	 * 			Determines where the new zero-elements going to be located. Enumeration according to the
	 * 			Cartesian coordinate system. So, if you want to grow the matrix on the right and on the
	 * 			bottom side, direction is 2.
	 */
	public void grow(int size, int direction) {
		
		if (this.rownum != size || this.colnum != size) {
			
			boolean bottom = (direction == 3 || direction == 4);
			
			Iterator<Entry<Integer, SparseVector>> entryiterator = this.vvectorlist.entrySet().iterator();
			while(entryiterator.hasNext()) {
				Entry<Integer, SparseVector> e = entryiterator.next();
				e.getValue().grow(size, bottom);
			}
			
			this.rownum = size;
			this.colnum = size;
		}
	}
	
	/**
	 * Grows the matrix on one side with another matrix
	 * 
	 */
	public void grow(Matrix M, int direction) {
		if (!(M instanceof SparseMatrix))
			throw new IllegalArgumentException("Matrix-growing is yet only supported for SparseMatrices");
		SparseMatrix B = new SparseMatrix((SparseMatrix) M);
		
		if (direction == 1) {	
			Iterator<Entry<Integer, SparseVector>> entryiterator = B.vvectorlist.entrySet().iterator();
			while(entryiterator.hasNext()) {
				Entry<Integer, SparseVector> e = entryiterator.next();
				this.vvectorlist.put(e.getKey() + this.colnum, new SparseVector(e.getValue()));
			}	
			this.colnum += B.colnum;
		}
		else if (direction == 2) {
			Iterator<Entry<Integer, SparseVector>> entryiterator = this.vvectorlist.entrySet().iterator();
			while(entryiterator.hasNext()) {
				Entry<Integer, SparseVector> e = entryiterator.next();
				this.vvectorlist.put(e.getKey() + this.colnum, e.getValue());
			}
			
			entryiterator = B.vvectorlist.entrySet().iterator();
			while(entryiterator.hasNext()) {
				Entry<Integer, SparseVector> e = entryiterator.next();
				this.vvectorlist.put(e.getKey(), new SparseVector(e.getValue()));
			}
			this.colnum += B.colnum;
		}
		else if (direction == 3) {
			
		}
	}
	
	public int getNumberOfNonZeroElements() {
		int sum = 0;
		for (SparseVector row : vvectorlist.values()) {
			sum += row.getUsed();
		}
		return sum;
	} 
	
	public double getSparsity() {
		return  ((double) this.getNumberOfNonZeroElements())/((double)(this.getRowNum()*this.getColNum()));
	}
	
	public NonZeroElementIterator getNonZeroElementIterator() {
		return new SparseMatrixNonZeroElementIterator(this);
	}
	
	public int getNumberOfNonZeroRows() {
		return vvectorlist.size();
	}
	
	public NonSparseMatrix getNonSparseMatrix() {
		double[][] nonsparse =  new double[this.getRowNum()][this.getColNum()];
		Iterator<Entry<Integer, SparseVector>> entryiterator = this.vvectorlist.entrySet().iterator();
		while(entryiterator.hasNext()) {
			Entry<Integer, SparseVector> e = entryiterator.next();
			nonsparse[e.getKey()] =e.getValue().get();
		}
		
		return new NonSparseMatrix(nonsparse);
	}
	
	
	
	

}

