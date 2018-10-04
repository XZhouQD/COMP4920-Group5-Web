package scpsolver.graph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

public class SubsetActiveCardinalityComparator implements Comparator<Node> {

	HashSet<Node> subset;
	HashMap<Node,Integer> cache;
	
	
	public SubsetActiveCardinalityComparator(HashSet<Node> subset) {
		this.subset = subset;
		this.cache = new HashMap<Node, Integer>();
	}
	
	
	
	public int compare(Node o1, Node o2) {
		int cardinalitydistance = numberOfActiveAdjacentNodesInSubset(o1) - numberOfActiveAdjacentNodesInSubset(o2);
		return cardinalitydistance;
	}
	
	public int numberOfActiveAdjacentNodesInSubset(Node node) {
		if (cache.containsKey(node)) return cache.get(node);
		int result = 0;
		for (Node anode : node.getActiveAdjacentNodes()) {
			if (subset.contains(anode)) result++;
		}
		cache.put(node, result);
		return result;
	}

}
