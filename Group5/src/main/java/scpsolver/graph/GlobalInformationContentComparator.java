package scpsolver.graph;



import java.util.Comparator;

public class GlobalInformationContentComparator implements Comparator<Node> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Node o1, Node o2) {
		double sumcard1 = 0;
		double sumcard2 = 0;
		
		for (Node node: o1.getActiveAdjacentNodes()) {
			sumcard1 += 1.0/Math.pow(node.getActiveCardinality(),2);				
		}
		
		for (Node node: o2.getActiveAdjacentNodes()) {
			sumcard2 += 1.0/Math.pow(node.getActiveCardinality(),2);				
		}
		
		double infcont1 = sumcard1; 
		double infcont2 = sumcard2; 
		//System.out.println(infcont1 + " " + infcont2);
		int cardinalitydistance = (infcont1 > infcont2)? 1:-1;
		return cardinalitydistance;
	}		
	
}
