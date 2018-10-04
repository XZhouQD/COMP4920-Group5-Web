package scpsolver.graph;

import scpsolver.util.SparseMatrix;
import java.util.Random;

/**
 * Building a graph out of a SparseMatrix
 * 
 * @author Sebastian Rheinnecker
 *
 */
public class GraphMiner {

	private Random random;
	
	public GraphMiner(){
		this.random = new Random();
	}
	
	/**
	 * reads the matrix as an adjacency matrix and produces a graph. 
	 * @param matrix TODO
	 * @return
	 */
	public static Graph getGraph(SparseMatrix matrix){
		Graph g = new Graph();
		for (int row = 0; row<matrix.getRowNum(); row++){
			for (int column = 0; column<matrix.getColNum(); column++){
				if (matrix.get(row, column) == 0 
						|| row == column
						|| g.hasEdge(((Integer)row).toString(), ((Integer)column).toString())
						|| g.hasEdge(((Integer)column).toString(), ((Integer)row).toString()))
					continue;
				g.addEdgeSecure(((Integer)row).toString(), ((Integer)column).toString());
			}				
		}
		return g;
	}
	
	/**
	 * creates a random graph
	 * @param nodecount - the number of nodes in the graph
	 * @return a random graph
	 */
	public Graph createRandomGraph(int nodecount){
		SparseMatrix matrix = this.createRandomMatrix(nodecount);
		return GraphMiner.getGraph(matrix);
	}
	
	/**
	 * create quadratic random sparse matrix
	 * @param row and col count
	 */
	public SparseMatrix createRandomMatrix(int rowcolCount){
		SparseMatrix matrix = new SparseMatrix(rowcolCount, rowcolCount);
		for (int row = 0; row<matrix.getRowNum(); row++){
			for (int column = 0; column<matrix.getColNum(); column++){
					matrix.set(row, column, this.randomInt(rowcolCount));
			}				
		}
		return matrix;
	}
	
	/**
	 * Creates randomly 0 and 1.
	 * @return
	 */
	private double randomInt(int freq){
		if (this.random.nextInt() % freq/2 == 0){
			return 1.0;
		}
		return 0.0;
	}
}
