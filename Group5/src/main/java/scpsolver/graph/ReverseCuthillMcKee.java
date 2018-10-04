package scpsolver.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;


public class ReverseCuthillMcKee {

	
	public ArrayList<Node> getReverseCuthillMcKeeOrdering(GraphInterface g) {
		ArrayList<Node> allnodes = new ArrayList<Node>( g.getNodes().values());
		ArrayList<Node>  result = new ArrayList<Node>();
		HashSet<Node>  queued = new HashSet<Node>();
		HashSet<Node>  added = new HashSet<Node>();
		
		
		ArrayList<Node> Q = new ArrayList<Node>();
		while (allnodes.size() != 0) {
			System.out.println(allnodes.size()+" size");
			
			Collections.sort(allnodes);
			
			Node P =allnodes.get(0);
			
			
			Q.add(P);
			while (Q.size() > 0) {
				//System.out.println("remaining nodes " + allnodes.size()+ "  / queued nodes " +  Q.size());
				P = Q.get(0);
				//System.err.println(P);
				result.add(P);
				added.add(P);
				allnodes.remove(P);
				Q.remove(0);
				ArrayList<Node> C = P.getActiveAdjacentNodes();
				Collections.sort(C);
				for (Iterator<Node> iterator = C.iterator(); iterator.hasNext();) {
					Node c = (Node) iterator.next();
					if ((!added.contains(c)) && (!queued.contains(c))) {
						Q.add(c);
						queued.add(c);
					}
				}
				
			}
		
		}
		//Collections.reverse(result);
		
		return result;
	} 
	
	
	
	
}
