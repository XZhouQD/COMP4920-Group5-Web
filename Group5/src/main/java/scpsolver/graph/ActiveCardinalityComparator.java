package scpsolver.graph;

import java.util.Comparator;

public class ActiveCardinalityComparator implements Comparator<Node> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Node o1, Node o2) {
		int cardinalitydistance = o1.getActiveCardinality() - o2.getActiveCardinality();
		return cardinalitydistance;
	}		
	
}