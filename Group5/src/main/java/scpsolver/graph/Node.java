package scpsolver.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * @author  planatsc
 */
public class Node implements Comparable<Node>  {

	String label;
	private ArrayList<Edge> edgeList;
	private boolean debug = false;
	Comparator<Node> comparator;
	
	/**
	 * @param comparator  the comparator to set
	 * @uml.property  name="comparator"
	 */
	public void setComparator(Comparator<Node> comparator) {
		this.comparator = comparator;
	}

	public Node(String label) {
		this.setLabel(label);
		edgeList = new ArrayList<Edge>();
		comparator = new CardinalityComparator();
	}
	
	public Edge addEdgeto(Node node) {
		//if (!this.getAdjacentNodes().contains(node)) {	
		return new Edge(this, node);			
		//}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Node) {
			return ((Node) obj).getLabel().equals(this.label);
		}
		return super.equals(obj);
	}
	
	public Edge addEdgeto(Node node, String label) {
		//if (!this.getAdjacentNodes().contains(node)) {	
		Edge e= addEdgeto(node);	
		e.setLabel(label);
		return e;
		//}
	}
	
	public void addEdge(Edge edge) {
		if ((edge.node1 == this) || (edge.node2 == this)) {
			this.edgeList.add(edge);	
			if (debug) System.out.println("Edge added:" + edge);
		} else {
			System.out.println(edge + " does not contain this node " + this);			
		}
	}
	
	public ArrayList<Node> getAdjacentNodes() {
		ArrayList<Node> nodelist = new ArrayList<Node>();			
		for (Edge edge: edgeList) {
			if (edge.node1 == this) 
				nodelist.add(edge.node2);			
			else
				nodelist.add(edge.node1);				
		} 
		return nodelist;
		
	}

	public ArrayList<Node> getActiveAdjacentNodes() {
		
		HashSet<Node> nodelist = new HashSet<Node>();			
		for (Edge edge: edgeList) {
			if (edge.isActive()) {
			if (edge.node1 == this) {				
				nodelist.add(edge.node2);							
			} else {
				nodelist.add(edge.node1);
			}
			}
		} 
		return new ArrayList<Node>(nodelist);
		
	}
	
	public ArrayList<Node> getOutboundNodes() {
		ArrayList<Node> nodelist = new ArrayList<Node>();			
		for (Edge edge: edgeList) {
			if (edge.isActive()) {
				if (edge.node1 == this) 
					nodelist.add(edge.node2);			
			}
		} 
		return nodelist;
		
	}

	
	public ArrayList<Node> getInboundNodes() {
		ArrayList<Node> nodelist = new ArrayList<Node>();			
		for (Edge edge: edgeList) {
			if (edge.isActive()) {
				if (edge.node2 == this) 
					nodelist.add(edge.node1);			
			}
		} 
		return nodelist;
		
	}
	/**
	 * @return  the edgeList
	 * @uml.property  name="edgeList"
	 */
	public ArrayList<Edge> getEdgeList() {
		return edgeList;
	}
	
	public void removeAllEdges() {
		while (this.getCardinality() != 0) {
			if (debug) System.out.println("Edge removed:" + this.edgeList.get(0));
			this.edgeList.get(0).removeEdge();
		} 		
	}
	
	
	
	public void deactivateAllEdges() {
		for (Edge edge: edgeList) {
			edge.setActive(false);			
		} 
	}
	
	public void activateAllEdges() {
		for (Edge edge: edgeList) {
			edge.setActive(true);			
		} 
	}
	
	public String toString() {
		return label;	
	}
	
	/**
	 * @return  the label
	 * @uml.property  name="label"
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label  the label to set
	 * @uml.property  name="label"
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	public int getCardinality() {
		return edgeList.size();		
	}
	
	public int getActiveCardinality() {
		int result = 0;
		for (Edge edge: edgeList) {
			if (edge.isActive()) result++;			
		} 
		return result;		
	}
	
	public int compareTo(Node node) {		
		return comparator.compare(this, node) ;
	}
	
	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Comparator<Node> getComparator() {
		return comparator;
	}

	public void setEdgeList(ArrayList<Edge> edgeList) {
		this.edgeList = edgeList;
	}
	
	/**
	 * equals method checking for the labels. the nodes are uniquely defined by their label in the graph.
	 * @param n
	 * @return
	 */
	public boolean equals(Node n){
		return label.equals(n.getLabel());
	}
	
	
	public static void main(String[] args) {				
		Node node1 = new Node("Node 1");
		Node node2 = new Node("Node 2");
		Node node3 = new Node("Node 3");
		Node node4 = new Node("Node 4");
		//node2.addEdgeto(node1);
		node1.addEdgeto(node4);	
		node4.addEdgeto(node2);
		node2.addEdgeto(node3);
		
		HashSet<Node> set = new HashSet<Node>();
	
		if(set.add(node1)) System.out.println(node1 + " ADDED" + node1.getCardinality() );
		if(set.add(node2)) System.out.println(node2 + " ADDED" + node2.getCardinality() );
		if(set.add(node3)) System.out.println(node3 + " ADDED" + node3.getCardinality() );
		if(set.add(node4)) System.out.println(node4 + " ADDED" + node4.getCardinality() );
		
				
		System.out.println(Collections.max(set));
		node2.addEdgeto(node3);

		System.out.println(Collections.max(set));

		
	}
	


	
	
	
}
