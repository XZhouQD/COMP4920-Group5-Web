package scpsolver.graph;

import java.util.Arrays;
import java.util.Hashtable;

/**
 * @author  planatsc
 */
public class Edge {
	Node node1;
	Node node2;
	boolean active;
	String label;
	int weight;
	Hashtable<String,String> properties;
	
	public void setProperty(String key, String value) {
		if (properties == null)  properties = new Hashtable<String, String>();
		properties.put(key, value);
	}
	
	public String getProperty(String key) {
		if (properties == null)  return null;
		return properties.get(key);
	}
	
	public Hashtable<String,String>  getProperties() {
		return properties;
	}
	
	
	public void setProperties(Hashtable<String, String> properties) {
		this.properties = properties;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * @return
	 * @uml.property  name="label"
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 * @uml.property  name="label"
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return  the active
	 * @uml.property  name="active"
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active  the active to set
	 * @uml.property  name="active"
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	public Edge(Node node1, Node node2) {
		this.setNode1(node1);
		this.setNode2(node2);
		node1.addEdge(this);
		node2.addEdge(this);
		label = "";
		this.active = true;
	}
	
	/**
	 * @return  the node1
	 * @uml.property  name="node1"
	 */
	public Node getNode1() {
		return node1;
	}
	/**
	 * @param node1  the node1 to set
	 * @uml.property  name="node1"
	 */
	public void setNode1(Node node1) {
		this.node1 = node1;
	}
	/**
	 * @return  the node2
	 * @uml.property  name="node2"
	 */
	public Node getNode2() {
		return node2;
	}
	/**
	 * @param node2  the node2 to set
	 * @uml.property  name="node2"
	 */
	public void setNode2(Node node2) {
		this.node2 = node2;
	}
	
	public String toString() {
		return "(" + node1.getLabel() + "-" + label  + " -> " + node2.getLabel() +")"	;
		//return "(" + node1.getLabel() +" -> " + node2.getLabel() +")"	;
	} 
	
	public void removeEdge() {
		//System.out.println(node1 + " removing: " + this);
		node1.getEdgeList().remove(this);
		//System.out.println(node2 + " removing: " + this);
		node2.getEdgeList().remove(this);
		
	}
	
	/**
	 * equals method checking for the node labels. 
	 * the edges are uniquely defined by their nodes label in the graph.
	 * @param n
	 * @return
	 */
	public boolean equals(Edge e){
		return this.toNormalizedString().equals(e.toNormalizedString());
	}
	
	/**
	 * returns both node labels in an array, typographically sorted
	 * @return
	 */
	public String[] getNodeLabels(){
		String[] nodeLabels = new String[2];
		nodeLabels[0] = this.node1.getLabel();
		nodeLabels[1] = this.node2.getLabel();
		Arrays.sort(nodeLabels);
		return nodeLabels;		
	}
	
	/**
	 * returns a string representation of the edge, independent of the edge direction
	 * @return
	 */
	public String toNormalizedString(){
		String[] labels = getNodeLabels();
		String label1 = labels[0];
		String label2 = labels[1];
		return "(" + label1 + " --> " + label2 +")";
	}
		
}
