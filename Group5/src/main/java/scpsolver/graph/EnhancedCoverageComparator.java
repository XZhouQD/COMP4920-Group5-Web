package scpsolver.graph;

import java.util.Comparator;

public class EnhancedCoverageComparator implements Comparator<Node>  {
		
	    int smcov = 1;
	    int scov = 100;
	
	    public EnhancedCoverageComparator(int scov, int smcov) {
	    	this.scov = scov;
	    	this.smcov =smcov;
	    }
	    
		public int compare(Node o1, Node o2) {
			int cardinalitydistance =			
				(o1.getActiveCardinality() - o2.getActiveCardinality())*scov+				
				(((o1.getActiveCardinality()>0) && (o2.getActiveCardinality()>0)) ? ((o1.getCardinality() - o1.getActiveCardinality()) - (o2.getCardinality() -o2.getActiveCardinality()))*smcov:0);
			return cardinalitydistance;
		}		
		
}
