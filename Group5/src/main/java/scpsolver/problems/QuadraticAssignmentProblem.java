package scpsolver.problems;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import scpsolver.constraints.Constraint;
import scpsolver.constraints.LinearEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
// import scpsolver.lpsolver.CPLEXSolver;

/**
 * Quadratic Assignment Problem as defined in
 * 
 * http://en.wikipedia.org/wiki/Quadratic_assignment_problem
 * 
 * 
 * @author hannes
 *
 */

public class QuadraticAssignmentProblem {

	double[][] A; // flow matrix 
	double[][] B; // distance matrix
	double[][] C; // cost matrix
	
	
	public QuadraticAssignmentProblem(double[][] a, double[][] b) {
		super();
		A = a;
		B = b;
		C = new  double[a.length][a.length];
	}
	
	public QuadraticAssignmentProblem(double[][] a, double[][] b, double[][] c) {
		super();
		A = a;
		B = b;
		C = c;
	}
	
	public double evaluate(int[][] assignment) {
		double sum = 0;
		int n = this.getDimension();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < n; k++) {
					for (int l = 0; l < n; l++) {
						sum += A[i][k] * B[j][l] * assignment[i][j] * assignment[k][l]; 
						//if (A[i][k] * B[j][l] * assignment[i][j] * assignment[k][l]>0) System.out.println(i +" " + j + " " + k +" "+ l +": "+ sum);
						
					}
				}
			}
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				sum += C[i][j] *  assignment[i][j];
			}
		}
		return sum;
	}

	public QuadraticAssignmentProblem(String filename) {
		BufferedReader buffer;
		try {
			buffer = new BufferedReader(new FileReader(filename));
	//		if (buffer == null) System.exit(-1);
			String line = "";
			line = buffer.readLine();
			
			/*first line contains n*/
			int n = Integer.parseInt(line.trim());
			A = new double[n][n];
			B = new double[n][n];
			C =  new double[n][n];
			buffer.readLine();
			
			for (int i = 0; i < n; i++) {
				line = buffer.readLine().trim();
				String[] vals = line.split("\\s+");
				for (int j = 0; j < vals.length; j++) {
					A[i][j] = Double.parseDouble(vals[j].trim());
					System.out.print(A[i][j] +" ");
				}
				System.out.println();
				for (int j = vals.length; j < n; j++) {
					A[i][j] = Double.POSITIVE_INFINITY;
					System.out.println("assymetry in flow matrix");
				}
			}
			
			buffer.readLine();
			
			for (int i = 0; i < n; i++) {
				line = buffer.readLine().trim();
				String[] vals = line.split("\\s+");
				for (int j = 0; j < vals.length; j++) {
					B[i][j] = Double.parseDouble(vals[j].trim());
					System.out.print(B[i][j] +" ");
				}
				System.out.println();
				for (int j = vals.length; j < n; j++) {
					B[i][j] = Double.POSITIVE_INFINITY;
					System.out.println("assymetry in distance matrix");
				}
			}
			
			for (int i = 0; i < n; i++) {
				//	A[i][i] = 100;
				//	B[i][i] = 100;
					
			}
			} catch (Exception e) {
			System.out.print("Cannot read line in file " + filename);
			e.printStackTrace();
		}
		
		
	}

	public double[][] getA() {
		return A;
	}

	public void setA(double[][] a) {
		A = a;
	}

	public double[][] getB() {
		return B;
	}

	public void setB(double[][] b) {
		B = b;
	}
	
	public int getDimension() {
		return A.length;
	}
	
	
	public LinearProgram getAdamJohnsonLinearization() {
		int n = this.getDimension();
		double[]  c = new double[n*n + n*n*n*n];
		for (int i = 0; i < n*n; i++) {
			c[i] = 1.0;
		}
		LinearProgram lp = new LinearProgram(c);
		lp.setMinProblem(true);
		
		
		
		return null;
	}
	
	/**
	 * Kaufmann Broeckx Linearization see
	 * L. Kaufman and F. Broeckx, An algorithm for the quadratic assignment problem using Benders decomposition, 
	 * European Journal of Operational Research 2 (1978), 204--211.
	 * http://citeseer.ist.psu.edu/context/628716/0
	 * 
	 * @return a Linear Program equivalent to this QAP 
	 */
	
	public LinearProgram getKaufmannBroeckxLinearization() {
		
		int n = this.getDimension();
		/* we need 2 n^2 variables in the linearization */
		double[] c = new double[2*n*n];
		for (int i = 0; i < n*n; i++) {
			c[i] = 1;
		}
		/* Cost matrix -- is this the right place ?*/ 
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				c[n*n + i*n + j] = C[i][j];
			}	
		}
		/* the first n^2 variables are the y_ij, these are organized
		 * rowwise y_00, .. ,y_0n, .., y_11, .., y_nn, x_00, .., x_nn */
		
		LinearProgram lp = new LinearProgram(c);
		
		/* Objective: min \sum_{i,j} y_ij */
		lp.setMinProblem(true);
		
		/* the x_s are binary */
		for (int i = n*n; i < c.length; i++) {
			lp.setBinary(i);
		}
		
		/* add constraints */
		
		for (int i = 0; i < n; i++) {
			double[] a = A[i].clone();
			java.util.Arrays.sort(a);
			for (int j = 0; j < n; j++) {
			   
				/* calc u_ij */
				double[] b = B[j].clone();

				java.util.Arrays.sort(b);
				double sum = 0;
				for (int k = 0; k < b.length; k++) {
					sum += a[k]*b[k];
				}
				double u = sum;
				
				double[] cv = new double[c.length];
				cv[ i*n + j] = -1.0; // - y_ij
				
				cv[n*n+i*n+j] = u; // + x_ij * u_ij
				for (int k = 0; k < n; k++) {
					for (int l = 0; l < n; l++) {
						//System.out.println(i +" "+ j+ " " + k + " " + l+": " + " "+A[i][k] + " * " + B[j][l] +": " +A[i][k] * B[j][l]);
						cv[n*n + k*n + l] += A[i][k] * B[j][l]; // + x_kl * a_ik * b_jl
					}
				}
				
				/* add constraint:  x_ij * u_ij +  \sum_{k,l} x_kl * a_ik * b_jl - y_ij <= u_ij */
				
				System.out.println(i+ " "+ j + ":" + u);
				lp.addConstraint(new LinearSmallerThanEqualsConstraint(cv, u, "special u_"+i+"_"+j)); 
			}
		}
		
		/* y_ij positivity.. bounds not implemented yet, using normal constraints instead   */
		double[] lowerbound = new double[c.length];
		
		for (int i = 0; i < this.getDimension(); i++) {
			for (int j = 0; j < this.getDimension(); j++) {
				
				lowerbound[i*n+j] = 0.0;
				/* add constraint: y_ij >= 0 */
			}
		}
		lp.setLowerbound(lowerbound); 
		
		/* X is in \Pi_n  (a permutation matrix of size n)*/
		
		for (int i = 0; i < this.getDimension(); i++) {
			double[] cv = new double[c.length];
			for (int j = 0; j < this.getDimension(); j++) {
				cv[n*n + i*n + j] = 1.0;
			}
			/* add constraint: \sum_{j} x_ij = 1 */
			lp.addConstraint(new LinearEqualsConstraint(cv, 1.0, "x_"+i+"j")); 
		}
		
		for (int j = 0; j < this.getDimension(); j++) {
			double[] cv = new double[c.length];
			for (int i = 0; i < this.getDimension(); i++) {
				cv[n*n + i*n + j] = 1.0;
			}
			/* add constraint: \sum_{i} x_ij = 1 */
			lp.addConstraint(new LinearEqualsConstraint(cv, 1.0, "x_i"+j)); 
		}
		
		return lp;
	}
	
	public LinearProgram getPlanatscherContraintHeurstic(double flow, double distance) {
		int n = this.getDimension();
		double[] c = new double[n*n];		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {	
				c[i*n+j] = C[i][j];
				
			}
		}
		LinearProgram lp = new LinearProgram(c);		
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {	
				lp.setBinary(i*n+j);
				
			}
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {	
				if (A[i][j] > flow) {
					for (int k = 0; k < n; k++) {
						for (int l = 0; l < n; l++) {
							if (B[k][l] > distance) {
								double[] cn = new double[n*n];
								cn[i*n+l] = 1.0;
								cn[j*n+k] = 1.0;
								
								LinearSmallerThanEqualsConstraint constraint = new LinearSmallerThanEqualsConstraint(cn,1.0, "crossing "+i+" ("+","+k+"),(" + j + ","+l+")");
								lp.addConstraint(constraint);
							}
						}
					}	
				}
			}
		}
		
		for (int i = 0; i < this.getDimension(); i++) {
			double[] cv = new double[c.length];
			for (int j = 0; j < this.getDimension(); j++) {
				cv[ i*n + j] = 1.0;
			}
			/* add constraint: \sum_{j} x_ij = 1 */
			lp.addConstraint(new LinearEqualsConstraint(cv, 1.0, "x_"+i+"j")); 
		}
		
		for (int j = 0; j < this.getDimension(); j++) {
			double[] cv = new double[c.length];
			for (int i = 0; i < this.getDimension(); i++) {
				cv[ i*n + j] = 1.0;
			}
			/* add constraint: \sum_{i} x_ij = 1 */
			lp.addConstraint(new LinearEqualsConstraint(cv, 1.0, "x_i"+j)); 
		}
		double[] lowerbound = new double[c.length];
		
		for (int i = 0; i < c.length; i++) {
				
				lowerbound[i] = 0.0;
	
		}
		lp.setLowerbound(lowerbound) ;
		lp.setMinProblem(true);
		return lp;
	}
	
	public static void main(String[] args) {
		//QuadraticAssignmentProblem qap = new QuadraticAssignmentProblem("/Users/hannes/Desktop/nug12.dat");
		QuadraticAssignmentProblem qap = new QuadraticAssignmentProblem("/home/planatsc/Desktop/nug12.dat");
		LinearProgram lp = qap.getKaufmannBroeckxLinearization();
		//GLPKSolver solver = new GLPKSolver();
		// CPLEXSolver solver = new CPLEXSolver();
		LinearProgramSolver solver = SolverFactory.newDefault();
		double[] res = solver.solve(lp);
		int n = qap.getDimension();
		int[][] assignment = new int[n][n];
	
		/* get assignment */
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
			   
				assignment[i][j] = (int) res[n*n+i*n+j];
				if (assignment[i][j] == 1) {
									
					System.out.print((i+1) +"=>"+ (j+1) + " ("+ res[n*n+i*n+j]+", ");
					System.out.println(+ res[i*n+j]+")");
						
				}
			}
		}
		
		/* check */
		for (int i = 0; i < n; i++) {
			double[] a = qap.A[i].clone();
			java.util.Arrays.sort(a);
			for (int j = 0; j < n; j++) {
			   
				assignment[i][j] = (int) res[n*n+i*n+j];
				if (assignment[i][j] == 1) {
					/* calc u_ij */
					double[] b = qap.B[j].clone();

					java.util.Arrays.sort(b);
					double sum = 0;
					for (int k = 0; k < b.length; k++) {
						sum += a[k]*b[k];
					}
					double u = sum;
					
					double checksum = 0;
					for (int k = 0; k < n; k++) {
						for (int l = 0; l < n; l++) {
							checksum += qap.A[i][k]*qap.B[i][l]*assignment[k][l];
						}
					}
					
					System.out.println( u + " + " + checksum + " - " + res[i*n+j] + " <= " + u);
								
					//System.out.print((i+1) +"=>"+ (j+1) + " ("+ res[n*n+i*n+j]+", ");
					//System.out.println(+ res[i*n+j]+")");
						
				}
			}
		}
				
		ArrayList<Constraint> carr = lp.getConstraints();
		for (Constraint constraint : carr) {
			if (!(constraint.isSatisfiedBy(res))) System.out.println(constraint.getName() + " not satisfied!");  
		}
		System.out.println();
		System.out.println("optimum: " + qap.evaluate(assignment));
	}
	
}
