package scpsolver.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;
import java.util.Map.Entry;

/**
 * Implementation of a sparse vector
 * @author planatsc
 *
 */
public class SparseVector implements Matrix {	
	
	double[] data;	
	int[] index;
	int used;
	int size;
	boolean linevector;
	
	
	public double[] getData() {
		return data;
	}

	public void setData(double[] data) {
		this.data = data;
	}

	public int[] getIndex() {
		return index;
	}

	public void setIndex(int[] index) {
		this.index = index;
	}

	public int getUsed() {
		return used;
	}

	public void setUsed(int used) {
		this.used = used;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	/**
	 * Grows or shrinks the vector to the given size
	 * 
	 * @param newsize
	 * 			The new size of the vector
	 * @param bottom
	 * 			Boolean indicating wether the elements are added on the top or bottom
	 */
	public void grow(int newsize, boolean bottom) {
		this.size = newsize;
		if (!bottom)
			for (int i = 0; i < used; i++)
				index[i] += (newsize - size);
	}
	
	/**
	 * Grows a vector with another vector
	 * 
	 * @param v
	 * 			The vector to be concatinated
	 * @param bottom
	 * 			Wether v is added before or after the vector 
	 */
	//Maybe get this working later
	public void grow(SparseVector v) {
		SparseVectorNonZeroElementIterator iter = new SparseVectorNonZeroElementIterator(v);
		int oldsize = this.size;		
		this.size =this.size+ v.size;

		while(iter.hasNext()) {
			double value = iter.next();
			int col = iter.getActualj();
			this.set(col+oldsize, value);
		}
		
	}
	
	
	/**
	 * Transposes a vector
	 * @return Returns a new transposed vector
	 */
	public Matrix transpose() {
		SparseVector clone = new SparseVector(this);
		clone.linevector = !linevector;
		return clone;
	}
	
	
	/**
	 * Constructs a new SparseVector of a given size and with a given starting capacity.  
	 * @param size The actual size of the vector
	 * @param nonzerosize The starting capacity of the vector
	 */
	public  SparseVector(int size, int nonzerosize) {
		data = new double[nonzerosize];
		index = new int[nonzerosize];
		for (int i = 0; i < index.length; i++) {
			index[i] = Integer.MAX_VALUE;
		}
		used = 0;
		this.size = size;
		this.linevector = false;
	}
	
	
	/**
	 * Copy-constructor for the SparseVector
	 * @param v The vector to be copied
	 */
	
	public  SparseVector(SparseVector v) {
		data = (double[]) v.data.clone() ;
		index = (int []) v.index.clone();
		used = v.used;
		size = v.size;
		linevector = v.linevector;
	}
	
	
	/**
	 * Creates a new vector with entries taken from the array
	 * @param x The entries of the new vector
	 */
	public  SparseVector(double[] x) {
		data = new double[x.length];
		index = new int[x.length];
		used = 0;
		for (int i = 0; i < x.length; i++) {			
			if (x[i] != 0) {
				data[used] = x[i];
				index[used] = i;
				used++;								
			} 
		}
		
		double[] newdata = new double[used ];
		int[] newindex  = new int[used];		
		System.arraycopy(data,0, newdata, 0, used);
		System.arraycopy(index,0, newindex, 0, used);
		data = newdata;
		index = newindex;
		size = x.length;
		this.linevector = false;

	}
	
	public SparseVector(int index[], double[] data) {
		this.setIndex(index);
		this.setData(data);
		this.setSize(data.length);
		this.setUsed(data.length);
		this.linevector = false;
	}
	
	/**
	 * Copies the matrix c. Attention: this may only work if the matrix is a nx1 or a 
	 * 1xn matrix.
	 * @param c The matrix to be copied
	 */
	public SparseVector(Matrix c) {
		if (c instanceof SparseVector) {
			SparseVector v = (SparseVector) c;
			data = (double[]) v.data.clone() ;
			index = (int []) v.index.clone();
			used = v.used;
			size = v.size;
			linevector = v.linevector;
		} else {
			NonZeroElementIterator nze = c.getNonZeroElementIterator();
			while (nze.hasNext()) {
				this.set(nze.getActuali(), nze.getActualj());
			}
		}
	}

	/**
	 * Creates a deep copy of the vector
	 * @return A deep copy of the vector
	 */
	public SparseVector clone() {
		return new SparseVector(this);
		
	}
	
	/**
	 * String representation of this sparse vector.
	 * 
	 */
	
	public String toString() {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < index.length; i++) {
			result.append("["+index[i]+","+data[i]+"]");
		}
		return result.toString();
	}
	
	/**
	 * Sets the index of a sparse vector
	 * inpired by http://cvs.berlios.de/cgi-bin/viewcvs.cgi/ressim/mtj/src/no/uib/cipr/matrix/sparse/SparseVector.java?rev=HEAD&content-type=text/vnd.viewcvs-markup
	 * ;)
	 * 
	 * @param ind
	 * @param val
	 * @return
	 */
	public int getIndex(int ind) {
		
		 int i = Arrays.binarySearch(index, ind); 
	   if (i >= 0) {
        	if (i < used && index[i] == ind)
        		return i;
        } else {
        	i = -i-1; 
        }
        
        int[] newIndex = index;
        double[] newData = data;

        // Check available memory
        if (++used >= data.length) {
            int newLength = (data.length != 0) ? Math.min(data.length  << 1,this.size) : 1;
            newIndex = new int[newLength];
            for (int l = 0; l < newIndex.length; l++) {
    			newIndex[l] = Integer.MAX_VALUE;
    		}
            newData = new double[newLength];
            System.arraycopy(index, 0, newIndex, 0, i);
            System.arraycopy(data, 0, newData, 0, i);
            
        }
        
       if (i!=used) {
        try {
        	System.arraycopy(index, i, newIndex, i + 1, used-i-1);
            System.arraycopy(data, i, newData, i + 1, used-i-1);	
		} catch (Exception e) {
			System.out.println(index.length + " " + newIndex.length + "  "+ i + " " + used);
			e.printStackTrace();
			System.exit(0);
		}
       }

		// All ok, make room for insertion
        // Put in new structure
        newIndex[i] = ind;
        newData[i] = 0.;

        // Update pointers
        index = newIndex;
        data = newData;
        // Return insertion index
        return i;
	
	}
	
	/**
	 * Sets the entry of the vector to a given value
	 * @param in The entry of the vector to be altered
	 * @param value The new value
	 */
	public void set(int in, double value) {
		int i = this.getIndex(in);
		data[i] = value;
	}
	
	/**
	 * Gets the value at index
	 * inpired by http://cvs.berlios.de/cgi-bin/viewcvs.cgi/ressim/mtj/src/no/uib/cipr/matrix/sparse/SparseVector.java?rev=HEAD&content-type=text/vnd.viewcvs-markup
	 * ;)
	 * @param index
	 * @return
	 */
	public double get(int index) {
		    int in = Arrays.binarySearch(this.index, index);
		        
	        if (in >= 0)
	            return data[in];
	        return 0;
	}
	
	/**
	 * Returns the vector as a double array
	 * @return The representation of the vector as array
	 */
	
	public double[] get() {
        double[] result = new double[size];
        for (int i = 0; i < used; i++) {
			result[index[i]] = data[i];
		}
        return result;
	}


	/**
	 * Calculates the dot product (or scalar product, inner product) of two vectors
	 * @param y The second vector for the dot product
	 * @return The dot product of this and the vector y
	 */
	 public double dot(SparseVector y) {
	    	double sum = 0.0;
			int[] index1 = index;
	    	int[] index2 = y.getIndex();
	    	int c1 = 0;
	    	int c2 = 0;
	    	boolean oneahead = (index1[c1]>=index2[c2]);
	    	int yused = y.getUsed();
	    
	    	while ((c1 < used) && (c2 < yused)) {
	    		oneahead = (index1[c1]>=index2[c2]);
	    		if (index1[c1] == index2[c2]) {
	    			sum += data[c1]* y.data[c2];
	    			c1++;
	    		} else {
	    			if (oneahead) {
		    			c2++;
		    		} else {
		    			c1++;
		    		}	
	    		} 	    		
	    	}	    		
	    	return sum;
	   }
	 
	 /**
	  * Adds a second vector to the vector
	  * @param y The second vector
	  * @return this+y
	  */
	 // TODO: Does the else-case actually make any sense?
	 public SparseVector plus(Matrix y) {
		
		SparseVector result = new SparseVector(this);
		
		
		
		if (y instanceof SparseVector) {
			SparseVector ys = (SparseVector) y; 
			int[] index2 = ys.getIndex();
		    for (int i = 0; i < ys.getUsed(); i++) {
		    	result.set(index2[i], this.get(index2[i]) + ys.data[i]);
			}
			
		    return result;
		} else {
			for (int i = 0; i < this.getRowNum(); i++) {
				for (int j = 0; j < this.getColNum(); j++) {
					result.set(i, j, result.get(i, j) + y.get(i, j));
				}
			}
		}
		return result;
	 }

	 /**
	  * Calculates the dot product of this vector and the vector y
	  * @param y The second vector for the dot product
	  * @return The dot product of this and the second vector
	  */
	 public double dotdumb(SparseVector y) {
	    	double[] ydata = y.get();               
	    	return dot(ydata);
	  }
	   
	/**
	 * Calculates the dot product of this vector and the vector y
	 * @param ydata The second vector implicitly given as double array
	 * @return The dot product of this and y
	 */
	 
    public double dot(double ydata[]) {
        double ret = 0;
        for (int i = 0; i < used; i++)
            ret += data[i] * ydata[index[i]];
        return ret;
    }
    
    /**
     * Multiplies the vector with a scalar
     * @param c The scalar to be multiplied with the vector
     * @return Returns the vector multiplied with the scalar
     */
    
    public SparseVector times(double c) {
    	SparseVector vc = new SparseVector(this);
    	for (int i = 0; i < vc.data.length; i++) {
			vc.data[i] *= c; 
		}
    	return vc;
    }

    /**
     * Gets the value of the vector at the position donated by row and column. Note: if the 
     * vector is not a linevector, column must be equal to zero, otherwise an exception will
     * be thrown. If the vector is a linevector on the other hand, the row must be equal to
     * zero likewise.
     * @param row The row of the entry
     * @param column The column of the entry
     * @return The value at the entry
     */
  
	public double get(int row, int column) {
		if (this.linevector) {
			if ((row!=0) || (this.size <= column)) {
				System.out.println("out of bounds " + row + " " + column);
			}
			return this.get(column);
		} else {
			if ((column!=0) || (this.size <= row)) {
				System.out.println("out of bounds " + row + " " + column + " " + this.size);
			}
			return this.get(row);
		}
	}

	/**
	 * Returns the number of columns of the vector. Note: if this vector is not a linevector
	 * the value is, of course, always 1.
	 * @return The number of columns
	 */
	public int getColNum() {
		return (this.linevector)? this.size: 1;
	}

	/**
	 * Returns the number of rows of the vector. Note: if this vector is a linevector
	 * the value is, of course, always 1.
	 * @return The number of rows of the vector
	 */
	public int getRowNum() {
		return (this.linevector)? 1: this.size;
	}

	/**
	 * Multiplies the vector with a matrix
	 * @param multiplier The matrix with which the vector should be multiplied
	 * @return this*multiplier
	 */
	public Matrix times(Matrix multiplier) {
		if (this.getColNum() != multiplier.getRowNum()) {
			System.out.print("Matrix dimensons must agree " + this.getRowNum() + " " + this.getColNum() +" " + multiplier.getRowNum() + " " + multiplier.getColNum() );
			return null;
		}
		Matrix result = null;
		
		if (multiplier instanceof SparseVector) {
			SparseVector multiplierv = (SparseVector) multiplier;

			if (this.linevector == multiplierv.linevector) {
				if (this.linevector) {
					// 1xm * mx1 =>  1x1 * 1x1 => 1x1  both must be 1x1 otherwise the matrix-dimensions wouldn't agree
					result = new SparseVector(new double[]{this.data[0],multiplierv.data[0]});
				} else {
					// mx1 * 1x1 => mx1  multiplier must be 1x1 otherwise the matrix-dimensions wouldn't agree
					result = this.times(multiplierv.data[0]);
					
				}
			} else {	
				if (this.linevector) {
					// 1xn * nx1 => 1x1, equals dot-product
					System.out.println("1xn * nx1 => 1x1, equals dot-product");
					// TODO: Quick'n'dirty fix ... replace for a proper method!
					result = new SparseVector(new double[]{this.dotdumb(multiplierv)});
				} else {
					// mx1 * 1xp => mxp
					// TODO: introduce special case mx1 * 1x1
					result = new SparseMatrix(this.getRowNum(),multiplier.getColNum());
					
					for (int i = 0; i <  this.used; i++) {
						for (int j = 0; j <  multiplierv.used; j++) {
							result.set(index[i], multiplierv.index[j], data[i]*multiplierv.data[j]);
						}	
					}
				}
			}
			
		} else if (multiplier instanceof SparseMatrix) {
			SparseMatrix multiplierm = (SparseMatrix) multiplier;
			if (this.linevector) {
				//System.out.println("1xn *  nxp  => 1xp");
				result = new SparseVector(multiplierm.getColNum(),this.used);
				
				SparseMatrix multipliermtrans = (SparseMatrix) multiplierm.transpose();

				Iterator<Entry<Integer, SparseVector>> entryiterator = multipliermtrans.vvectorlist.entrySet().iterator();
				((SparseVector)result).linevector = true;
				while (entryiterator.hasNext()) {
					Entry<Integer, SparseVector> e = entryiterator.next();
					int i = e.getKey();
					SparseVector row = e.getValue();
					result.set(0, i, this.dot(row));
				}
			} else {
				// mx1 *  1xp  => mxp
				result = this.times(multiplierm.vvectorlist.get(0));
				
			} 
			
		}
		
		return result;
	}

	
	/**
	 * Sets a certain entry of the vector to the given value.
	 */
	// TODO: Add exceptions
	public void set(int row, int column, double value) {
		if (this.linevector) {
			if ((row!=0) || (this.size <= column)) {
				System.out.println("out of bounds " + row + " " + column);
			}
			this.set(column, value);
		} else {
			if ((column!=0) || (this.size <= row)) {
				System.out.println("out of bounds " + row + " " + column);
			}
			this.set(row, value);
		}
		
	}
	
    
    /**
     * Adds two sparse vectors
     * @param v
     * 			The vector to be added
     * @return The sum of the two vectors
     */
    public SparseVector add(SparseVector v) {
    	SparseVector uv = new SparseVector(v);
    	for (int i = 0; i < size; i++)
    		uv.set(i, uv.get(i) + this.get(i));
    	return uv;
    }
    
    /**
	 * Compares two vectors. Notice: if the Object o isn't a probable SparseVector, it can't
	 * be compared yet.
	 * @param v
     * 			The vector to be compared to
     * @return
     * 			True, if the second vector provides the same values, false otherwise. Note that the
     * 			vectors don't need to be identical in the data-arrays, so it's okay when one vector
     * 			explicitly safes a zero argument and the other doesn't.
     */
    public boolean equals(Object o) {
    	if (!(o instanceof SparseVector))
    		throw new IllegalArgumentException("Illegal Argument: object isn't of type SparseVector");
    	return equals((SparseVector) o);
    }
    
    /*
     * TODO: Implement it for more types
     *
     * TODO: Maybe we want the accuracy to be a constant?
     */
    /**
     * Compares two SparseVectors
     * @param v
     * 			The vector to be compared to
     * @return
     * 			True, if the second vector provides the same values, false otherwise. Note that the
     * 			vectors don't need to be identical in the data-arrays, so it's okay when one vector
     * 			explicitly safes a zero argument and the other doesn't.
     */
    public boolean equals(SparseVector v) {
    	if (this.size != v.getSize())
    		return false;
    	
    	for (int i = 0; i < size; i++)
    		if (Math.abs(this.get(i) - v.get(i)) > 0.0001)
    			return false;
    	
    	return true;
    	
    }

	
	 public static void main(String[] args) {
	    	Random rng = new Random();
	    	/*
	    	int max = 2000;
	    	
	    	
	    	
	    for (int j = 0; j < 100; j++) {
	    		
	    		int maxindex = rng.nextInt(max)+1;
	    		System.out.println("Identitiy check index " + j + " dimension " + maxindex);
	    		SparseVector sv1= new SparseVector(maxindex,4);
	    		double[] xcheck = new double[maxindex];
	    		for (int i = 0; i < rng.nextInt(maxindex); i++) {
	    				int index = rng.nextInt(maxindex);
	    				double value = rng.nextDouble();
	    			//	System.out.println(i+" "+index);
	    				xcheck[index] = value;
	    				sv1.set(index, value);
	    		}
	    		double[] x1 = sv1.get();
	    		for (int i = 0; i < maxindex; i++) {
					if (x1[i]!=xcheck[i]) {
						System.out.println("Test failed");
			    		System.out.println("Difference " + i + " " + x1[i]+" "+xcheck[i]);
					} 
				}
				
	    		
	    	
	    	}
	    	
	    	for (int j = 0; j < 100; j++) {
	    		
	    		int maxindex = rng.nextInt(max)+1;
	    		System.out.println("Dot product test " + j + " dimension " + maxindex);
	    		SparseVector sv1= new SparseVector(maxindex,4);
	    		SparseVector sv2= new SparseVector(maxindex,4);
	    		for (int i = 0; i < rng.nextInt(maxindex); i++) {
	    				int index = rng.nextInt(maxindex);
	    				double value = rng.nextDouble();
	    				sv1.set(index, value);
	    		}
	    		for (int i = 0; i < rng.nextInt(maxindex); i++) {
	    			int index = rng.nextInt(maxindex);
	    			double value = rng.nextDouble();
	    			sv2.set(index, value);
	    		}
	    		
	    		
	    		if (sv1.dot(sv2) != sv1.dotdumb(sv2)) {
	    			System.out.println("Test failed");
	    			double difference = Math.abs(sv1.dot(sv2)  - sv1.dotdumb(sv2));
	    			double[] x1 = sv1.get();
	    			double[] x2 = sv2.get();
	    			double sum =  0;
	    			for (int i = 0; i < x2.length; i++) {
						if (difference == Math.abs(x1[i]*x2[i])) {
							System.out.println("Missing: " + i);
							for (int ind : sv1.index) {
								System.out.println(ind);
							}
						}
						sum += x1[i]*x2[i];
					}
	    			
	    		}
	    		System.out.println(sv1.dot(sv2) +" "+ sv1.dotdumb(sv2));
    			
			}*/
	    	
	    	/*for (int j = 0; j < 100; j++) {
	    		int maxindex = rng.nextInt(max)+1;
	        	
	    		SparseVector sv1= new SparseVector(maxindex,4);
	    		SparseVector sv2= new SparseVector(maxindex,4);
	    		for (int i = 0; i < rng.nextInt(maxindex); i++) {
	    				int index = rng.nextInt(maxindex);
	    				double value = rng.nextDouble();
	    				sv1.set(index, value);
	    		}
	    		for (int i = 0; i < rng.nextInt(maxindex); i++) {
	    			int index = rng.nextInt(maxindex);
	    			double value = rng.nextDouble();
	    			sv2.set(index, value);
	    		}
	    		
	    		Matrix multiplicator = sv2.transpose();
	    		Matrix multiplicand = sv1;
	    		Matrix res = multiplicand.times(multiplicator);
	    		//System.out.println(res.getRowNum() + " " + res.getColNum());
	    		for (int k = 0; k < res.getRowNum(); k++) {
	    			for (int l = 0; l < res.getColNum(); l++) {
	    				// dumb 
	    				double sum = 0;
	    				for (int ii = 0; ii < multiplicand.getColNum(); ii++) {
								sum += multiplicand.get(k,ii)*multiplicator.get(ii, l);
						}
	    				//assert (sum == res.get(k, l));
					}
				}
	    	}*/
	    	
	    	int dim = 400;
    		int rows1 = dim;
    		int cols1  = dim;
    		int rows2 = dim;
    		int cols2 = dim;
	    	
	    	ArrayList<Integer> intlist1 = new ArrayList<Integer>();
	    	ArrayList<Integer> intlist2 = new ArrayList<Integer>();
	    	for (int i = 0; i < rows1*cols1; i++) {
	    		intlist1.add(i);intlist2.add(i);
			}
    		
	    	int nz1 = 0;
	    	
	    	int nz2 = 0;
    		SparseMatrix matrix1 = new SparseMatrix(rows1, cols1);
    		SparseMatrix matrix2 = new SparseMatrix(rows2, cols2);
    		double[][] nonsparsematrix1 = new double[rows1][cols1];
    		double[][] nonsparsematrix2 = new double[rows2][cols2];
	    	for (int j = 1; j < 100; j++) {
	    		//System.out.println("Matrix multiplication test " + j);
	    		/*int rows1 = rng.nextInt(max)+1;
	    		int cols1  = rng.nextInt(max)+1;
	    		int rows2 = cols1;
	    		int cols2 = rng.nextInt(max)+1;
	    		*/

	    		

	    		double[][] nonsparseresult = new double[rows1][cols2];
	    		
	    		//int nonzero = rng.nextInt(rows1*cols2/10);
	    		int nonzero = (int)Math.round( rows1*cols1*((double)j)/100) ;
	    	//	System.out.println("number nonzero:" + rows1*cols1 +  " *  " + ((double)j)/100+ " = " + nonzero);
	    		
	    		
	    		for (; nz1 < nonzero; nz1++) {
	    			int intindex = rng.nextInt(intlist1.size());
	    			Integer index = intlist1.get(intindex);
	    			intlist1.remove(index);
	    			int row = index / cols1;
	    			int col = index % rows1;
	    			double val = rng.nextDouble();
	    			matrix1.set(row, col, val);
	    			nonsparsematrix1[row][col] = val;
	    		}
	    		
	    		//nonzero = rng.nextInt(rows1*cols2/2);
	    	
	    		for (; nz2 < nonzero; nz2++) {
	    			int intindex = rng.nextInt(intlist2.size());
	    			Integer index = intlist2.get(intindex);
	    			intlist2.remove(index);
	    			int row = index / cols2;
	    			int col = index % rows2;
	    			double val = rng.nextDouble();
	    			matrix2.set(row, col, val);
	    			nonsparsematrix2[row][col] = val;
	    			
	    		}
	    		
	    		long time = System.currentTimeMillis();
	    		for (int row = 0; row < rows1; row++) {
	    			for (int column = 0; column < cols2; column++) {
		    			double sum = 0;
		    			for (int ii =0; ii < cols1;ii++) {
		    				sum += nonsparsematrix1[row][ii]*nonsparsematrix2[ii][column];
		    			}
		    			nonsparseresult[row][column] = sum;
		    		}
	    		}
	    		time =  System.currentTimeMillis() - time;
	    		
	    		//System.out.println("normal multiplication took:" + time);
	    		
	    		long sparsetime = System.currentTimeMillis();
	    		Matrix res = matrix1.times(matrix2);
	    		sparsetime =  System.currentTimeMillis() - sparsetime;
	    		
	    		//System.out.println("sparse multiplication took:" + time);
	    		System.out.println(nonzero + " " + sparsetime  + " "+ time);
	    	//	System.out.println("Checking result");
	    		
	    		for (int row = 0; row < rows1; row++) {
	    			for (int column = 0; column < cols2; column++) {
	    				if (nonsparseresult[row][column] != res.get(row, column)) {
	    					System.out.println("Error in " + row + " " + column + " : " +nonsparseresult[row][column] + " " + res.get(row, column));
	    					System.exit(0);
	    				} 	    			
	    			}
	    		}
	    		
	    	}
		}

	public NonZeroElementIterator getNonZeroElementIterator() {
		// TODO Auto-generated method stub
		return new SparseVectorNonZeroElementIterator(this);
	}

}


