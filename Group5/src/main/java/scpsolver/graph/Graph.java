package scpsolver.graph;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import scpsolver.util.SparseMatrix;
//import sun.tools.tree.ThisExpression;

/**
 * @author  planatsc
 */
public class Graph implements GraphInterface {

	private HashMap<String, Node> nodes;
	


	/* (non-Javadoc)
	 * @see scpsolver.graph.GraphInterface#getNodes()
	 */
	public HashMap<String, Node> getNodes() {
		return nodes;
	}

	/* (non-Javadoc)
	 * @see scpsolver.graph.GraphInterface#getNode(java.lang.String)
	 */
	public Node getNode(String label) {
			
			return nodes.get(label);
	}
	
	/**
	 * returns the instance of the edge defined by its nodes. throws runtimeexpections if errors occur.
	 * @param leftlabel
	 * @param rightlabel
	 * @return
	 */
	public Edge getEdge(String leftlabel, String rightlabel){
		if (!hasEdge(leftlabel, rightlabel) && !hasEdge(rightlabel, leftlabel))
			throw new RuntimeException("Edge not found: "+leftlabel+" , "+rightlabel);
		
		Node node1=nodes.get(leftlabel);
		Node node2=nodes.get(rightlabel);
		
		for (Edge e: node1.getEdgeList()){
			if (e.getNode1().equals(node2) || e.getNode2().equals(node2)){
				return e;
			}
		}
		throw new RuntimeException("unexpected error searching for edge");
		
	}

	public Graph() {
		nodes = new HashMap<String, Node>();
	}
	
	private Graph(Graph g){
		nodes = new HashMap<String, Node>();
		
		this.addGraph(g);
	}
	
	public Graph clone(){
		return new Graph(this);
	}
	
	
	public void reset() {
		for (Node node: nodes.values()) {
			node.activateAllEdges();
		}		
	}
	
	public Node addNode(Node node) {
		Node cnode = nodes.get(node.getLabel());
		
		if (cnode == null) {		
			nodes.put(node.getLabel(), node);	
			cnode = node;
		} 

		return cnode;
	}
	
	
	public void addGraph(Graph g) {
		for (Node n: g.getNodes().values()){
			this.addNode(new Node(n.getLabel()));
			
		}
		
		for (Node n: g.getNodes().values()){
			ArrayList<Edge> edgelist = n.getEdgeList();
			for (Edge edge : edgelist) {
				if (edge.getNode1() == n) {
					Edge copyedge = this.addEdgeSecure(edge.node1.getLabel(), edge.node2.getLabel());
					copyedge.setLabel(new String(edge.getLabel()));
					copyedge.setWeight(edge.getWeight());
					copyedge.setProperties(new Hashtable<String, String>(edge.getProperties()));
				}
			}
			
		}
		
	} 
	/**
	 * @param n
	 * @param result
	 * @return
	 */
	private HashSet<Node> getComponent(Node n, HashSet<Node> result) {		
		for (Node node: n.getActiveAdjacentNodes()) 
			if (result.add(node))	getComponent(node,result);					
		return result;	
	}

	
	public HashSet<Node> getComponent(Node n) {
		HashSet<Node> result= new HashSet<Node>();
		result.add(n);
		return getComponent(n, result);
	}
	
	private HashSet<Node> getComponentDL(Node n, HashSet<Node> result, HashSet<Node> todeep,  int depth) {		
		for (Node node: n.getActiveAdjacentNodes()) 
			if (result.add(node))	{
				if (depth>100) {
					todeep.add(node);
				}
				getComponentDL(node,result, todeep,depth+1);					
			}
		return result;	
	}

	
	public HashSet<Node> getComponentDL(Node n, HashSet<Node> result) {
		if (result.add(n)) {
			HashSet<Node> todeep = new HashSet<Node>();
			getComponentDL(n, result, todeep, 0);
			for (Node node : todeep) {
				getComponentDL(node, result);
			}
		}
		return result;
	}
	
	public ArrayList<HashSet<Node>> getAllComponentsDL(){
		ArrayList<HashSet<Node>> result = new ArrayList<HashSet<Node>>();
		ArrayList<Node> allnodes = new ArrayList<Node>(getNodes().values());		
		
		while (!allnodes.isEmpty()) {
			
			
			HashSet<Node> clique =  new HashSet<Node>();
				
			getComponentDL(allnodes.get(0),clique);
			//System.out.println("found clique of size " + clique.size());
			result.add(clique);
			allnodes.removeAll(clique);		
		}
		return result;		
	}
	
	public ArrayList<HashSet<Node>> getAllComponents(){
		ArrayList<HashSet<Node>> result = new ArrayList<HashSet<Node>>();
		ArrayList<Node> allnodes = new ArrayList<Node>(getNodes().values());		
		
		while (!allnodes.isEmpty()) {
			HashSet<Node> clique = getComponent(allnodes.get(0));
			//System.out.println("found clique of size " + clique.size());
			result.add(clique);
			allnodes.removeAll(clique);		
		}
		return result;		
	}
	
		
	public void removeNode(Node node) {
		node.removeAllEdges();
		nodes.remove(node.getLabel());
	}
	
	/**
	 * removes an edge from the graph.
	 * @param e
	 */
	public void removeEdge(Edge e){
		Node n1 = this.nodes.get(e.getNode1().getLabel());
		Node n2 = this.nodes.get(e.getNode2().getLabel());
		
		for (int i = 0; i<n1.getEdgeList().size(); i++){
			if (n1.getEdgeList().get(i).toNormalizedString().equals(e.toNormalizedString())) {
				n1.getEdgeList().remove(i);
				break;
			}
		}
		
		for (int i = 0; i<n2.getEdgeList().size(); i++){
			if (n2.getEdgeList().get(i).toNormalizedString().equals(e.toNormalizedString())) {
				n2.getEdgeList().remove(i);
				break;
			}
		}
		n2.getEdgeList().remove(e);		
	}
	
	/**
	 * removes nodes with cardinality zero
	 */
	public void removeCards(){
		ArrayList<Node> cards = this.getNodeWithCardinality(0, 0);
		for (Node n: cards){
			this.removeNode(n);
		}		
	}
	
	/**
	 * is this graph empty?
	 */
	public boolean isEmpty(){
		return (this.getNodes().isEmpty());
	}
	
	/* (non-Javadoc)
	 * @see scpsolver.graph.GraphInterface#getNumberNodes()
	 */
	public int getNumberNodes() {
		return nodes.size();		
	}
	
	public int getNumberEdges(){
		int number = 0;
		for (Node n : this.nodes.values()){
			number += n.getCardinality();
		}
		return number/2;
	}

	
	/* (non-Javadoc)
	 * @see scpsolver.graph.GraphInterface#hasEdge(java.lang.String, java.lang.String)
	 */
	public boolean hasEdge(String leftlabel, String rightlabel) {
		if (!nodes.containsKey(leftlabel)) return false;
		if (!nodes.containsKey(rightlabel)) return false;
		Node node1=nodes.get(leftlabel);
		Node node2=nodes.get(rightlabel);
		if (node1.getOutboundNodes().contains(node2)) return true;
		
		return false;
		
	}
	
	/*
	 *  Edge adding methods
	 */
	
	public Edge addEdgeSecure(String leftlabel, String rightlabel, boolean reverse) {
		Node node1;
		Node node2;
		node1 = this.addNode(new Node(leftlabel));
		node2 = this.addNode(new Node(rightlabel));							
		if (reverse) {			
			return node2.addEdgeto(node1);
		} else {
			return node1.addEdgeto(node2);
					
		}
	}
	
	/* (non-Javadoc)
	 * @see scpsolver.graph.GraphInterface#addEdgeSecure(java.lang.String, java.lang.String)
	 */
	public Edge addEdgeSecure(String leftlabel, String rightlabel) {
		return addEdgeSecure(leftlabel, rightlabel, false);
	}
	
	public Edge addEdgeSecure(String leftlabel, String rightlabel, String edgelabel, boolean reverse) {
		Edge e = addEdgeSecure( leftlabel,  rightlabel, reverse);
		e.setLabel(edgelabel);
		return e;
	}

	
	

	
	public String toGML() {
		StringBuffer buf = new StringBuffer();
		Hashtable<Node, Integer> idtable = new Hashtable<Node, Integer>();
		buf.append("graph [ \n comment \"no comment\"  \n directed 1  \n id 42  \n label \"Graph\"\n");		
		int idcounter = 1;
		//double leftpos = 1.0;
		int mincard = 0;
	
		
		for (Node node: this.getNodes().values()) {			
			if (node.getCardinality() ==0) System.out.println(node.getLabel()+" has  card");
			if (node.getCardinality() > mincard) {
				
			buf.append("node [ \n id " + idcounter  + "\n label \"" +  node.getLabel()  + "\"\n") ;			
			buf.append("]\n") ;						
			idtable.put(node, idcounter);
			idcounter++;
			}
		}
		for (Node node:this.getNodes().values()) {			
			if (node.getCardinality() > mincard) {
			for (Edge edge:node.getEdgeList()) {	
				if (edge.getNode1() == node)
				buf.append("edge [\n source "+ idtable.get(edge.node1)+ "\n target "+idtable.get(edge.node2)+"\n label \"" + edge.getLabel() + "\"\n graphics\n[\n\tfill\t\"#000000\"\n\ttargetArrow\t\"standard\"\n\t]\n  ]\n");
				
			}
			}
			
		}
		
		return buf.toString();		
	}
	
	
	public String toGMLwithGrouping(ArrayList<HashSet<Node>> groups, ArrayList<String> labels) {
		StringBuffer buf = new StringBuffer();
		Hashtable<Node, Integer> idtable = new Hashtable<Node, Integer>();
		buf.append("graph [ \n comment \"no comment\"  \n directed 1  \n id 42  \n label \"Graph\"\n");		
		
		int groupidcounter = 1;
		Hashtable<Node, Integer> grouptable = new Hashtable<Node, Integer>();
		for (HashSet<Node> nodeset : groups) {
			for (Node node : nodeset) {
				grouptable.put(node, groupidcounter);
			}
			groupidcounter++;
		}
		
		int idcounter = 1;
		for (String string : labels) {
			buf.append("node [ \n id " + idcounter  + "\n label \"" +  string  + "\"\n") ;			
			buf.append("		graphics \n" + 
					"		[ \n" + 
					"			type	\"roundrectangle\" \n" + 
					"			fill	\"#CAECFF84\" \n" + 
					"			outline	\"#666699\" \n" + 
					"			outlineStyle	\"dotted\" \n" + 
					"			topBorderInset	0.0 \n" + 
					"			bottomBorderInset	0.0 \n" + 
					"			leftBorderInset	0.0 \n" + 
					"			rightBorderInset	0.0 \n" + 
					"		] \n" + 
					"		LabelGraphics \n" + 
					"		[ \n" + 
					"			text	\"" + string +"\" \n" + 
					"			fill	\"#99CCFF\" \n" + 
					"			fontSize	15 \n" + 
					"			fontName	\"Dialog\" \n" + 
					"			alignment	\"right\" \n" + 
					"			autoSizePolicy	\"node_width\" \n" + 
					"			anchor	\"t\" \n" + 
					"			borderDistance	0.0 \n" + 
					"		] \n" + 
					"		isGroup	1 \n");
			buf.append("]\n") ;
			idcounter++;
		}
		
		//double leftpos = 1.0;
		int mincard = 0;
	
		
		for (Node node: this.getNodes().values()) {			
			if (node.getCardinality() ==0) System.out.println(node.getLabel()+" has  card");
			if (node.getCardinality() > mincard) {
				
			buf.append("node [ \n id " + idcounter  + "\n label \"" +  node.getLabel()  + "\"\n") ;		
			if (grouptable.containsKey(node)) buf.append(" gid " + grouptable.get(node)+"\n");
			buf.append("]\n") ;						
			idtable.put(node, idcounter);
			idcounter++;
			}
		}
		for (Node node:this.getNodes().values()) {			
			if (node.getCardinality() > mincard) {
			for (Edge edge:node.getEdgeList()) {	
				if (edge.getNode1() == node) {
					
				buf.append("edge [\n source "+ idtable.get(edge.node1)+ "\n target "+idtable.get(edge.node2)+"\n label \"" + edge.getLabel() + "\"\n");
				buf.append(" graphics [\n");
				Hashtable<String, String> properties = edge.getProperties();
				if (properties != null) {
					Iterator<String> iterator =properties.keySet().iterator();
					while (iterator.hasNext()) {
						String key = iterator.next();
						String value = properties.get(key);
						try {
						   int intval = Integer.parseInt(value);
						   buf.append("\t"+key+"\t"+intval+"\n");
						} catch (Exception e) {
						   buf.append("\t"+key+"\t\""+value+"\"\n");
						}
					}
				}
				buf.append("\ttargetArrow\t\"standard\"\n]");
				buf.append("\n  ]\n");
				}
			}
			}
			
		}
		
		return buf.toString();		
	}
	
	//TODO
	public String toMTX() {
		StringBuffer buf = new StringBuffer();
		Hashtable<Node, Integer> idtable = new Hashtable<Node, Integer>();
		//buf.append("graph [ \n comment \"no comment\"  \n directed 1  \n id 42  \n label \"Graph\"\n");		
		int idcounter = 1;
		//double leftpos = 1.0;
		int mincard = 0;
		int numnodes = this.getNumberNodes();
		int numedges = this.getNumberEdges();
		buf.append(numnodes + "\t" + numnodes + "\t" + numedges + "\n");
		for (Node node: this.getNodes().values()) {			
			if (node.getCardinality() > mincard) {					
				idtable.put(node, idcounter);
				idcounter++;
			}
		}
	//	int edgecount = 0;
		for (Node node:this.getNodes().values()) {			
			if (node.getCardinality() > mincard) {
			for (Edge edge:node.getEdgeList()) {	
				if (edge.getNode1() == node) {
					buf.append("\t"+idtable.get(edge.getNode1())+ "\t"+idtable.get(edge.getNode2()) + "\t1.0\n");
			//		edgecount++;
				}
			}
			}
		}
		
		return buf.toString();
	}
	
	
	public ArrayList<Node> getNodeWithCardinality(int min, int max) {
		ArrayList<Node> result = new ArrayList<Node>();
		for (Node node: this.nodes.values()) {			
			int card = node.getCardinality();
			if ((card >= min) && (card <= max)) result.add(node);
		}
		return result;
	}
	

	
	public ArrayList<Node> getNodeWithActiveCardinality(int min, int max) {
		ArrayList<Node> result = new ArrayList<Node>();
		for (Node node: this.nodes.values()) {			
			int card = node.getActiveCardinality();
			if ((card >= min) && (card <= max)) result.add(node);
		}
		return result;
	}



	
	/**
	 * @param filename
	 */
	public void toGML(String filename) {
		try {
			FileWriter fwriter = new FileWriter(new File(filename));
			fwriter.write(toGML());
			fwriter.flush();
			fwriter.close();
		} catch (Exception e) {
			System.out.println("Could not write GML-file "+ filename +" because" + e.getMessage());
		}
	}
	
	public void toGML(String filename, ArrayList<HashSet<Node>> groups, ArrayList<String> labels) {
		try {
			FileWriter fwriter = new FileWriter(new File(filename));
			fwriter.write(toGMLwithGrouping(groups, labels));
			fwriter.flush();
			fwriter.close();
		} catch (Exception e) {
			System.out.println("Could not write GML-file "+ filename +" because" + e.getMessage());
		}
	}
	
	
	
	
	/**
	 * 
	 * @param nodestring
	 * @return
	 */
	public ArrayList<Node> getNodeSet(String nodestring) {
		ArrayList<Node> result = new ArrayList<Node>();
		String [] labels  = nodestring.split(" ");
		for (String label:labels   ) {
			if (nodes.containsKey(label)) {
				result.add(nodes.get(label));
			}
		}
		return result;		
	}

	/**
	 * 
	 * @param nodestring
	 * @return
	 */
	public ArrayList<Node> getNodeSetPipe(String nodestring) {
		ArrayList<Node> result = new ArrayList<Node>();
		String [] labels  = nodestring.split("\n");
		for (String label:labels   ) {
			if (nodes.containsKey(label)) {
				result.add(nodes.get(label));
			}
		}
		return result;		
	}
	public SparseMatrix getAdjMatrix() {
		return getAdjMatrix(new ArrayList<Node>(getNodes().values()));
	}
	
	
	public SparseMatrix getAdjMatrix(ArrayList<Node> nodeorder) {
		SparseMatrix ad = new SparseMatrix(nodeorder.size(),nodeorder.size());
		HashMap<Node, Integer> fastlookup = new HashMap<Node, Integer>();
		int idx = 0;
		for (Iterator<Node> iterator = nodeorder.iterator(); iterator.hasNext();) {
			Node node = (Node) iterator.next();
			fastlookup.put(node, idx++);
			
		}
		for (Iterator<Node> iterator = nodeorder.iterator(); iterator.hasNext();) {
			Node node = (Node) iterator.next();
			ArrayList<Node> adj = node.getActiveAdjacentNodes();
			int thisindex = fastlookup.get(node);
			for (Iterator<Node> iterator2 = adj.iterator(); iterator2.hasNext();) {
				Node anode = (Node) iterator2.next();
				Integer  otherindex = fastlookup.get(anode);
				if (otherindex != null)
				ad.set(thisindex, otherindex, 1.0);
			}
		}
		return ad;
	}
	
	public void activateAllEdges() {
		for (Node node : this.getNodes().values()) {
			node.activateAllEdges();
		}
	}
	
	
	public static void main(String[] args) {
				
		Graph graph = new Graph();
		graph.addEdgeSecure("A", "U");
		graph.addEdgeSecure("U", "A");
			
//		graph.addEdgeSecure("A", "Y");
		graph.addEdgeSecure("B", "X");
		graph.addEdgeSecure("B", "Y");
		graph.addEdgeSecure("C", "X");
	//	graph.addEdgeSecure("C", "U");		
		graph.addEdgeSecure("D", "Z");
		graph.addEdgeSecure("E", "Y");
		graph.addEdgeSecure("F", "K");
		graph.addEdgeSecure("F", "O");
		graph.addEdgeSecure("D", "X");
		
//	
//		ArrayList<HashSet<Node>> cliquedecomp =  graph.getAllComponentsDL();
//		for (HashSet<Node> clique : cliquedecomp) {
//			System.out.println(clique.size());
//			for (Node node : clique) {
//				System.out.print(node+" ");
//			}
//			
//			System.out.println();
//		}
//		graph.toGML("test.gml");
//		System.out.println(graph.toMTX());
		graph.clone();
	}
	
}
