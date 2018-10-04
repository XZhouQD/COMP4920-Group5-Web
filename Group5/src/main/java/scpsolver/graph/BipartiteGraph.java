package scpsolver.graph;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;
import scpsolver.problems.MathematicalProgram;
import scpsolver.util.SparseVector;

/**
 * @author  hannes
 */
public class BipartiteGraph extends Graph {
	private HashMap<String, Node> nodesleft;
	private  HashMap<String, Node> nodesright;
	
	/**
	 * @return  the nodesleft
	 * @uml.property  name="nodesleft"
	 */
	public HashMap<String, Node> getNodesleft() {
		return nodesleft;
	}


	/**
	 * @return  the nodesright
	 * @uml.property  name="nodesright"
	 */
	public HashMap<String, Node> getNodesright() {
		return nodesright;
	}

	public Node getNode(String label) {
			if (nodesleft.containsKey(label)) return nodesleft.get(label);
			return nodesright.get(label);
	}

	public BipartiteGraph() {
		nodesleft = new HashMap<String, Node>();
		nodesright = new HashMap<String,Node>();		
	}
	
	
	public void reset() {
		for (Node node: nodesleft.values()) {
			node.activateAllEdges();
		}
		for (Node node: nodesright.values()) {
			node.activateAllEdges();
		}		
	}
	
	public Node addLeftNode(Node node) {
		Node cnode = nodesleft.get(node.getLabel());
		if (cnode == null) {
			nodesleft.put(node.getLabel(), node);	
			cnode = node;
		} else {
			if (cnode.getLabel().compareTo(node.getLabel()) != 0) {
				System.out.println("Collision!? " + cnode.getLabel() +  " " + cnode.getLabel().hashCode() + " " + node.getLabel() + " " +node.getLabel().hashCode());
				System.out.println("This is very serious!!! Please check the hash function of the String labels!");
				System.exit(-1);
			} else {
			//	System.out.println("No Collision! " + cnode.getLabel() +  " " + cnode.getLabel().hashCode() + " " + node.getLabel() + " " +node.getLabel().hashCode());
				
			}
			
		}
		return cnode;
	}
	

	public Node addRightNode(Node node) {
		Node cnode = nodesright.get(node.getLabel());
		if (cnode == null) {
			nodesright.put(node.getLabel(), node);
			cnode = node;
		} else {
			if (cnode.getLabel().compareTo(node.getLabel()) != 0) {
				System.out.println("Collision!? " + cnode.getLabel() +  " " + cnode.getLabel().hashCode() + " " + node.getLabel() + " " +node.getLabel().hashCode());
				System.out.println("This is very serious!!! Please check the hash function of the String labels!");
				System.exit(-1);
			} else {
			//	System.out.println("No Collision! " + cnode.getLabel() +  " " + cnode.getLabel().hashCode() + " " + node.getLabel() + " " +node.getLabel().hashCode());
				
			}
			
		}
		return cnode;				
	}
	
	
	
	
	
	/**
	 * 
	 * @param n
	 * @param result
	 * @return
	 */

	private HashSet<Node> getComponent(Node n, HashSet<Node> result) {		
		for (Node node: n.getActiveAdjacentNodes()) 
			if (result.add(node)) 
				getComponent(node,result);					
		return result;	
	}

	
	/**
	 * Returns all nodes that are in the same graph component as n 
	 * 
	 * @param n Node
	 * @return
	 */
	
	public HashSet<Node> getComponent(Node n) {
		HashSet<Node> result= new HashSet<Node>();
		result.add(n);
		return getComponent(n, result);
	}
	
	/** 
	 * Returns all components of the graph
	 * 
	 * @return
	 */
	public ArrayList<HashSet<Node>> getAllComponents(){
		ArrayList<HashSet<Node>> result = new ArrayList<HashSet<Node>>();
		ArrayList<Node> allnodes = new ArrayList<Node>(getNodesleft().values());		
		allnodes.addAll(getNodesright().values());
		
		while (!allnodes.isEmpty()) {
			HashSet<Node> clique = getComponent(allnodes.get(0));
			System.out.println("found clique of size " + clique.size());
			result.add(clique);
			allnodes.removeAll(clique);		
		}
		return result;		
	}
	
	
	public String toTKZLatex(int minimumnodesize, int xrightnodes, double distance, String leftnodecolor, String rightnodecolor) {
		StringBuffer result = new StringBuffer();
		result.append(
				"\\begin{tikzpicture} \n" + 
				"    \\tikzstyle{VertexStyle}=[shape        = circle,\n" + 
				"                             fill         = " + leftnodecolor +",\n" + 
				"                             minimum size = 40pt,\n" + 
				"                             text         = white,\n" + 
				"                             draw]" + 
				"  \\SetUpEdge[lw         = 1.5pt,\n" + 
				"             color      = black,\n" + 
				"             labelcolor = white,\n" + 
				"             labeltext  = red,\n" + 
				"             labelstyle = {sloped,draw,text=black}],\n" +
				"			  style = {->}");
		ArrayList<Node> leftnodes = new ArrayList<Node>( this.nodesleft.values());
		ArrayList<Node> rightnodes = new ArrayList<Node>( this.nodesright.values());
		int i = 0;
		for (Node node : leftnodes) {
			result.append("\\Vertex[x=" + (i++)*distance + ", y=0]{" + node.getLabel() + "}\n");
		}
		result.append(
				"\\begin{tikzpicture}\n" + 
				"    \\tikzstyle{VertexStyle}=[shape        = circle,\n" + 
				"                             fill         = " + rightnodecolor +",\n" + 
				"                             minimum size = 40pt,\n" + 
				"                             text         = black,\n" + 
				"                             draw]\n" );
		i = 0;
		for (Node node : leftnodes) {
			result.append("\\Vertex[x=" + (i++)*distance + ", y=8]{" + node.getLabel() + "}\n");
		}
		for (Node node : leftnodes) {
			ArrayList<Edge> el =   node.getEdgeList() ;
			for (Edge edge : el) {
				if (edge.getNode1() == node) {
					result.append("\\Edges[label=\""+ edge.getLabel() +"\"](" + edge.getNode1().getLabel()+  "," + edge.getNode2().getLabel()+ ")\n");
				}
			}
		}
		for (Node node : rightnodes) {
			ArrayList<Edge> el =   node.getEdgeList() ;
			for (Edge edge : el) {
				if (edge.getNode1() == node) {
					result.append("\\Edges[label=\""+ edge.getLabel() +"\"](" + edge.getNode1().getLabel()+  "," + edge.getNode2().getLabel()+ ")\n");
				}
			}
		}
		result.append("\\end{tikzpicture}");
		return result.toString();
	}
	
	/**
	 * Remove a left node
	 * 
	 * @param node
	 */
	public void removeLeftNode(Node node) {
		node.removeAllEdges();
		nodesleft.remove(node.getLabel());
	}
	
	/**
	 * Returns the number of left nodes
	 * 
	 * @return
	 */
	
	public int getNumberOfLeftNodes() {
		return nodesleft.size();		
	}

	/**
	 * Returns the number of rigth nodes
	 * 
	 * @return
	 */
	
	public int getNumberOfRightNodes() {
		return nodesright.size();		
	}
	
	/**
	 * Checks if there is a edge between left node labeled leftlabel and a right node labeled rightlabel
	 * 
	 * @param leftlabel
	 * @param rightlabel
	 * @return
	 */
	public boolean hasEdge(String leftlabel, String rightlabel) {
		if (!nodesleft.containsKey(leftlabel)) return false;
		if (!nodesright.containsKey(rightlabel)) return false;
		Node node1=nodesleft.get(leftlabel);
		Node node2=nodesright.get(rightlabel);
		if (node1.getOutboundNodes().contains(node2)) return true;
		return false;
		
	}
	
	/*
	 *  Edge adding methods
	 */
	
	/**
	 * Adds a directed edge between a left and right node. 
	 * If reverse is true the edge is directed from right to left.,
	 * 
	 * @param leftlabel
	 * @param rightlabel
	 * @param reverse
	 * @return
	 */
	public Edge addEdgeSecure(String leftlabel, String rightlabel, boolean reverse) {
		Node node1;
		Node node2;
		node1 = this.addLeftNode(new Node(leftlabel));
		node2 = this.addRightNode(new Node(rightlabel));							
		if (reverse) {			
			return node2.addEdgeto(node1);
		} else {
			return node1.addEdgeto(node2);					
		}
	}
	
	
	/** 
	 * adds a directed edge between a left and right node. 
	 * 
	 * @param leftlabel
	 * @param rightlabel
	 * @return
	 */
	
	public Edge addEdgeSecure(String leftlabel, String rightlabel) {
		return addEdgeSecure(leftlabel, rightlabel, false);
	}
	
	/**
	 * Adds a directed labeled edge between a left and right node. 
	 * If reverse is true the edge is directed from right to left.,
	 * 
	 * 
	 * @param leftlabel
	 * @param rightlabel
	 * @param edgelabel
	 * @param reverse
	 * @return
	 */
	
	public Edge addEdgeSecure(String leftlabel, String rightlabel, String edgelabel, boolean reverse) {
		Edge e = addEdgeSecure( leftlabel,  rightlabel, reverse);
		e.setLabel(edgelabel);
		return e;
	}

	public LinearProgram getDenseConveringWithSizeLinearProgram(int k) {
		int problemdimension = this.getNumberOfLeftNodes()+this.getNumberOfRightNodes();
		SparseVector c = new SparseVector(problemdimension,this.getNumberOfRightNodes());
		for (int i = 0; i < this.getNumberOfRightNodes(); i++) {
			c.set(i, 1);
		}
	//	c = (SparseVector) c.transpose();
		
		LinearProgram lp = new LinearProgram(c);
		
		SparseVector sum = new SparseVector(problemdimension, this.getNumberOfLeftNodes());
		for (int i = this.getNumberOfRightNodes(); i < problemdimension; i++) {
		//	System.out.println(i);
			sum.set(i, 1);
		}
		LinearEqualsConstraint sumc = new LinearEqualsConstraint(sum,k,"sum of selected set equals k");
		lp.addConstraint(sumc);
		
		Iterator<Node> rnodeit = this.nodesright.values().iterator();
		System.out.println("Adding constraints..");
		
		ArrayList<Node> leftnodes =  new ArrayList<Node>(this.nodesleft.values()); 	
		HashMap<Node, Integer> fastlookup = new HashMap<Node, Integer>();
		for (int index = 0; index < leftnodes.size(); index++ ) {
			fastlookup.put(leftnodes.get(index), index);
		}
		
		int rindex = 0;
		while (rnodeit.hasNext()) {
			
			Node setnode = rnodeit.next();
			//System.out.println("Element " +  setnode);
			ArrayList<Node> set = setnode.getActiveAdjacentNodes();
			String constraintlabel = "completeness("+setnode.label+")";		
			
			SparseVector sump = new SparseVector(problemdimension, set.size()+1);			
			for (Node node : set) {
				sump.set(this.getNumberOfRightNodes() + fastlookup.get(node), 1);
			}
			sump.set(rindex++, -1);
			lp.addConstraint(new LinearBiggerThanEqualsConstraint(sump,0,constraintlabel));					
		}	
		lp.setMinProblem(false);
		for (int i = 0; i < problemdimension; i++) {
			lp.setBinary(i);		
		}	
		return lp;
	}
	
	
	
	/**
	 * Greedy heuristic for the set covering problem represented by the bipartite graph.
	 * Subsets are on the left. Universe elements on the right.
	 * 
	 * @return
	 */
	
	public ArrayList<Node> solveSetCoveringProblemGreedy() {
		for (Node node: nodesleft.values()) node.setComparator(new CardinalityComparator());
		for (Node node: nodesright.values()) node.setComparator(new CardinalityComparator());
		
		ArrayList<Node> solution = new ArrayList<Node>();
		while (true) {
			Node maxcover = Collections.max(nodesleft.values());
			if (maxcover.getCardinality() == 0) break;
			solution.add(maxcover);	
			System.out.println("Node: " + maxcover.getLabel() + " score: " + maxcover.getCardinality());
			for (Node node: maxcover.getAdjacentNodes()) {
				node.removeAllEdges();
			}		
			maxcover.removeAllEdges();						
		}
		return solution;		
	}
	
	/**
	 * Greedy heuristic for the set covering problem represented by the bipartite graph.
	 * Subsets are on the left. Universe elements on the right. The comparator directly influences
	 * te result.
	 * 
	 * @param comp
	 * @return
	 */
	
	public ArrayList<Node> solveSetCoveringProblemGreedyActive(Comparator<Node> comp) {
		for (Node node: nodesleft.values()) node.setComparator(comp);
		for (Node node: nodesright.values()) node.setComparator(comp);
		
		ArrayList<Node> solution = new ArrayList<Node>();
		while (!this.isValidCovering(solution)) {
			Node maxcover = Collections.max(nodesleft.values());
			solution.add(maxcover);	
			for (Node node: maxcover.getAdjacentNodes()) {
				node.deactivateAllEdges();
			}		
			maxcover.deactivateAllEdges();						
		}
		this.reset();
		return solution;		
	}
	
	/**
	 * Greedy heuristic for the set covering problem represented by the bipartite graph.
	 * Subsets are on the left. Universe elements on the right. The comparator directly influences
	 * te result.
	 * 
	 * @param comp
	 * @return
	 */
	
	public ArrayList<Node> solveSetCoveringProblemGreedyActive(Comparator<Node> comp, int max) {
		for (Node node: nodesleft.values()) node.setComparator(comp);
		for (Node node: nodesright.values()) node.setComparator(comp);
		
		ArrayList<Node> solution = new ArrayList<Node>();
		while (solution.size() < max) {
			Node maxcover = Collections.max(nodesleft.values());
			solution.add(maxcover);	
			for (Node node: maxcover.getAdjacentNodes()) {
				node.deactivateAllEdges();
			}		
			maxcover.deactivateAllEdges();						
		}
		this.reset();
		return solution;		
	}
	
	
	/* Formulates the set cover problem as a linear integer program. 
	 * 
	 * 
	 * */
	
	public LinearProgram getSetCoverLinearProgram(int mincover) {
		double[] x = new double[this.getNumberOfLeftNodes()];
	    
		ArrayList<Node> leftnodes =  new ArrayList<Node>(this.nodesleft.values()); 		
		for (int i = 0; i < x.length; i++) {
			x[i] = 1;
		}
		System.out.println("Instanciating LP");
		LinearProgram lp = new LinearProgram(x);
		System.out.println("Setting all to binary..");
		
		for (int i = 0; i < x.length; i++) {
			lp.setBinary(i);
		}
		
		Iterator<Node> rnodeit = this.nodesright.values().iterator();
		System.out.println("Adding constraints..");
	//	int counter = 0;
		
		HashMap<Node, Integer> fastlookup = new HashMap<Node, Integer>();
		for (int index = 0; index < leftnodes.size(); index++ ) {
			fastlookup.put(leftnodes.get(index), index);
		}
		
		while (rnodeit.hasNext()) {
			
			Node setnode = rnodeit.next();
			
			ArrayList<Node> set = setnode.getActiveAdjacentNodes();
			String constraintlabel = "completeness("+setnode.label+")";			
			SparseVector c = new SparseVector(x.length, set.size());			
			for (Node node : set) {
				c.set(fastlookup.get(node), 1);
			}
			lp.addConstraint(new LinearBiggerThanEqualsConstraint(c,mincover,constraintlabel));					
		}	
		lp.setMinProblem(true);
		System.out.println("LP formulation ready!");
		return lp;
		
	}
	
	/* Formulates the set cover problem as a linear integer program. 
	 * 
	 * 
	 * */
	
	public LinearProgram getDualCoverLinearProgram(int maxcost) {
		double[] x = new double[this.getNumberOfLeftNodes()+this.getNumberOfRightNodes()];
	    
		ArrayList<Node> leftnodes =  new ArrayList<Node>(this.nodesleft.values()); 		
		ArrayList<Node> rightnodes =  new ArrayList<Node>(this.nodesright.values()); 
		for (int i = 0; i < x.length; i++) {
			x[i] = 0;
		}
		for (int i = this.nodesleft.size(); i < x.length; i++) {
			x[i] = 1;
		}
		
		System.out.println("Instanciating LP");
		LinearProgram lp = new LinearProgram(x);
		System.out.println("Setting all to binary..");
		
		for (int i = 0; i < x.length; i++) {
			lp.setBinary(i);
		}
		
		Iterator<Node> rnodeit = this.nodesright.values().iterator();
		System.out.println("Adding constraints..");
	//	int counter = 0;
		
		HashMap<Node, Integer> fastlookup = new HashMap<Node, Integer>();
		int index = 0;
		for (index = 0; index < leftnodes.size(); index++ ) {
			fastlookup.put(leftnodes.get(index), index);
		}
		
		while (rnodeit.hasNext()) {
			
			Node setnode = rnodeit.next();
			
			ArrayList<Node> set = setnode.getActiveAdjacentNodes();
			String constraintlabel = "completeness("+setnode.label+")";			
			SparseVector c = new SparseVector(x.length, set.size());			
			for (Node node : set) {
				c.set(fastlookup.get(node), 1);
			}
			c.set(this.getNumberOfLeftNodes()+ rightnodes.indexOf(setnode), -1);
			lp.addConstraint(new LinearBiggerThanEqualsConstraint(c,1,constraintlabel));					
		}	
		
		double[] xx = new double[x.length];
		for (int i = 0; i < this.getNumberOfLeftNodes(); i++) {
			xx[i] = 1;
		}
		
		lp.addConstraint(new LinearSmallerThanEqualsConstraint(xx, maxcost, "Maximum cost"));
		
		
		lp.setMinProblem(false);
		System.out.println("LP formulation ready!");
		return lp;
		
	}
	
	/* Formulates the set cover problem as a linear integer program. 
	 * 
	 * 
	 * */
	
	public LinearProgram getSetCoverLinearProgramMulticover() {
		double[] x = new double[this.getNumberOfLeftNodes()];
	    
		ArrayList<Node> leftnodes =  new ArrayList<Node>(this.nodesleft.values()); 		
		for (int i = 0; i < x.length; i++) {
			x[i] = 1;
		}
		System.out.println("Instanciating LP");
		LinearProgram lp = new LinearProgram(x);
		System.out.println("Setting all to binary..");
		
		for (int i = 0; i < x.length; i++) {
			lp.setBinary(i);
		}
		
		Iterator<Node> rnodeit = this.nodesright.values().iterator();
		System.out.println("Adding constraints..");
	//	int counter = 0;
		
		HashMap<Node, Integer> fastlookup = new HashMap<Node, Integer>();
		for (int index = 0; index < leftnodes.size(); index++ ) {
			fastlookup.put(leftnodes.get(index), index);
		}
		
		while (rnodeit.hasNext()) {
			
			Node setnode = rnodeit.next();
			
			ArrayList<Node> set = setnode.getActiveAdjacentNodes();
			String constraintlabel = "completeness("+setnode.label+")";			
			SparseVector c = new SparseVector(x.length, set.size());			
			for (Node node : set) {
				c.set(fastlookup.get(node), 1);
			}
			if (c.getUsed() > 1) { 				
				lp.addConstraint(new LinearBiggerThanEqualsConstraint(c,2,constraintlabel));					
			} else {
				
				for (Node node : set) {
					System.out.println(node);
				}
				System.out.println(setnode.getLabel() + " can't be multicovered");
				lp.addConstraint(new LinearBiggerThanEqualsConstraint(c,1,constraintlabel));
			}
		
		}	
		lp.setMinProblem(true);
		System.out.println("LP formulation ready!");
		return lp;
	}
	
	
	/* Formulates the set cover problem as a linear integer program. 
	 * 
	 * 
	 * */
	
	public LinearProgram getSetCoverLinearProgramRelaxed(int mincover) {
		double[] x = new double[this.getNumberOfLeftNodes()];
	    
		ArrayList<Node> leftnodes =  new ArrayList<Node>(this.nodesleft.values()); 		
		for (int i = 0; i < x.length; i++) {
			x[i] = 1;
		}
		System.out.println("Instanciating LP");
		LinearProgram lp = new LinearProgram(x);
		/*System.out.println("Setting all to binary..");
		
		for (int i = 0; i < x.length; i++) {
			lp.setBinary(i);
		}*/
		
		lp.setLowerbound(MathematicalProgram.makeDoubleArray(lp.getDimension(), 0));
		lp.setUpperbound(MathematicalProgram.makeDoubleArray(lp.getDimension(), 1));
		
		Iterator<Node> rnodeit = this.nodesright.values().iterator();
		System.out.println("Adding constraints..");
	//	int counter = 0;
		
		HashMap<Node, Integer> fastlookup = new HashMap<Node, Integer>();
		for (int index = 0; index < leftnodes.size(); index++ ) {
			fastlookup.put(leftnodes.get(index), index);
		}
		
		while (rnodeit.hasNext()) {
			
			Node setnode = rnodeit.next();
			
			ArrayList<Node> set = setnode.getActiveAdjacentNodes();
			String constraintlabel = "completeness("+setnode.label+")";			
			SparseVector c = new SparseVector(x.length, set.size());			
			for (Node node : set) {
				c.set(fastlookup.get(node), 1);
			}
			lp.addConstraint(new LinearBiggerThanEqualsConstraint(c,1,constraintlabel));					
		}	
		lp.setMinProblem(true);
		System.out.println("LP formulation ready!");
		return lp;
		
	}
	
	public ArrayList<Node> solveCoveringProblemLPApproximation(LinearProgramSolver solver) {
		ArrayList<Node> result = new ArrayList<Node>();
		LinearProgram lp = this.getSetCoverLinearProgramRelaxed(1);
		double dresult[] = solver.solve(lp);
		
		/*ArrayList<Constraint> constraints = lp.getConstraints();
		
		for (Iterator<Constraint> iterator = constraints.iterator(); iterator.hasNext();) {
			Constraint constraint = iterator.next();
			if (constraint.isSatisfiedBy(dresult)) System.out.println(constraint.getName() + " satisfied");
		}*/
		
		ArrayList<Node> leftnodes =  new ArrayList<Node>(this.nodesleft.values());
		for (int i = 0; i < dresult.length; i++) {
			
			
			ArrayList<Node> rn =  leftnodes.get(i).getActiveAdjacentNodes();
			int delta = 0;
			
			for (Iterator<Node> iterator = rn.iterator(); iterator.hasNext();) {
				Node node = (Node) iterator.next();
				int card = node.getCardinality();
				delta = (card>delta)?card:delta;
			}
			
			
			
			if (dresult[i] >= (1.0/delta)) {
				//System.out.println(leftnodes.get(i) + " "+ dresult[i] + " " +(1.0/delta));
				result.add(leftnodes.get(i));
			} else {
				if (dresult[i] > 0) {
					System.out.println(leftnodes.get(i) + " "+ dresult[i] + " " +(1.0/delta));
				}
				
			}
		}
		return result;
	}
	
	
	public ArrayList<Node> solveLinearProgramForSubProblem(ArrayList<Node> proteinsublist, ArrayList<Node> epitopes, ArrayList<Node> solution, LinearProgramSolver solver) {
	
		return solution;
	
	}
	
	public ArrayList<Node> solveDenseCoveringProblemLP(LinearProgramSolver solver, int max) {
		
		ArrayList<Node> result = new ArrayList<Node>();
		LinearProgram lp = this.getDenseConveringWithSizeLinearProgram(max);
		double sol[] = solver.solve(lp);
		ArrayList<Node> nodesleft = new ArrayList<Node>(this.getNodesleft().values());
		for (int i = this.getNumberOfRightNodes(); i < sol.length; i++) {
			if (sol[i]  > 0.99) {
				result.add(nodesleft.get(i-this.getNumberOfRightNodes()));
				
			}
		}
		
		
		return result;
		
	}
	
	public ArrayList<Node> solveCoveringProblemLP(LinearProgramSolver solver, int mincover) {
		
		ArrayList<Node> result = new ArrayList<Node>();
		LinearProgram lp = this.getSetCoverLinearProgram(mincover);
		double dresult[] = solver.solve(lp);
		
		ArrayList<Node> leftnodes =  new ArrayList<Node>(this.nodesleft.values());
		for (int i = 0; i < dresult.length; i++) {
			if (dresult[i] >= 0.999) {
				result.add(leftnodes.get(i));
			}
		}
		return result;
		
	}
	
	public ArrayList<Node> solveDualCoveringProblemLP(LinearProgramSolver solver, int maxcost) {
		
		ArrayList<Node> result = new ArrayList<Node>();
		LinearProgram lp = this.getDualCoverLinearProgram(maxcost);
		double dresult[] = solver.solve(lp);
		
		ArrayList<Node> leftnodes =  new ArrayList<Node>(this.nodesleft.values());
		for (int i = 0; i < leftnodes.size(); i++) {
			if (dresult[i] >= 0.999) {
				result.add(leftnodes.get(i));
			}
		}
		return result;
		
	}
	
	public ArrayList<Node> solveCoveringProblemLPMulticover(LinearProgramSolver solver) {
		
		ArrayList<Node> result = new ArrayList<Node>();
		LinearProgram lp = this.getSetCoverLinearProgramMulticover();
		double dresult[] = solver.solve(lp);
		
		ArrayList<Node> leftnodes =  new ArrayList<Node>(this.nodesleft.values());
		for (int i = 0; i < dresult.length; i++) {
			if (dresult[i] >= 0.999) {
				result.add(leftnodes.get(i));
			}
		}
		return result;
		
	}

	/**
	 * Writes the linear program in Mathprog format 
	 * for the set sovering problem represented by the bipartite graph
	 * to a string.
	 * Subsets are on the left. Universe elements on the right.
	 * 
	 * @param mincover
	 * @return
	 * @deprecated
	 */
	
	public String toSetCoverGPML(int mincover) {
		
		StringBuffer buf = new StringBuffer();
		buf.append("# SET COVER PROBLEM\n");
		buf.append("set E;\n");
		buf.append("set S;\n");
		buf.append("param  M{s in S, e in E} binary;\n");				
		buf.append("var x{s in S} binary;\n");
		buf.append("minimize covering: sum{s in S} x[s];\n");
		buf.append("subject to completeness{e in E}: sum{s in S} M[s,e] * x[s] >= " + mincover +";\n");
		buf.append("solve;\n");		
		buf.append("printf \"SET: \\n\";");

		buf.append("for{s in S} {\n");
	    buf.append("    printf '%s ', if  x[s] then 'INSET' else 'OUTSET';\n");
	    buf.append("    printf '%s\\n', s;\n");
	    buf.append(" }\n");


		
	    buf.append("data;\n");
		buf.append("set E ");
		Iterator<Node> rnodeit = this.nodesright.values().iterator();
		while (rnodeit.hasNext()) {
			Node node = rnodeit.next();
			buf.append("\""+node+"\"");
			if (rnodeit.hasNext()) buf.append(", ");
		}		
		buf.append(";\n");
		buf.append("set S ");
		Iterator<Node> lnodeit = this.nodesleft.values().iterator();
		while (lnodeit.hasNext()) {
			Node node = lnodeit.next();
			buf.append("\""+node+"\"");
			if (lnodeit.hasNext()) buf.append(", ");
		}		
		buf.append(";\n");
		
		
		buf.append("param M:\n\t");
		rnodeit = this.nodesright.values().iterator();
		while (rnodeit.hasNext()) {
			buf.append("\""+rnodeit.next()+"\"\t"); 			
		}
		buf.append(":=\n");
		lnodeit = this.nodesleft.values().iterator();
		while (lnodeit.hasNext()) {
			Node setnode = lnodeit.next();
			ArrayList<Node> set = setnode.getActiveAdjacentNodes();
			
			buf.append("\""+setnode+"\"\t");			
			rnodeit = this.nodesright.values().iterator();
			while (rnodeit.hasNext()) {
				Node element = (Node) rnodeit.next();				
				boolean setcontainselement = set.contains(element);
				buf.append(((setcontainselement)?"1":"0")+"\t"); 			
			}
			buf.append("\n");
		}
		buf.append(";\n");
		
				buf.append("end;");
		
		return buf.toString();	
	}
	
	
	/**
	 * Generates the GML-graph representation of the Bipartite Graph
	 * A recommended viewer for GML files is yED
	 * 
	 * @return
	 */
	
	public String toGML() {
		StringBuffer buf = new StringBuffer();
		Hashtable<Node, Integer> idtable = new Hashtable<Node, Integer>();
		buf.append("graph [ \n comment \"no comment\"  \n directed 1  \n id 42  \n label \"Bipartite Graph\"\n");		
		int idcounter = 1;
		double leftpos = 1.0;
		int mincard = 0;
		LinkedList<Node> nodeleftsort = new LinkedList<Node>();
		nodeleftsort.addAll(nodesleft.values());
		//System.out.println(nodeleftsort.size());		
		Collections.sort(nodeleftsort);
		
		for (Node node: nodeleftsort) {			
			if (node.getCardinality() ==0) System.out.println(node.getLabel()+" has  card");
			if (node.getCardinality() > mincard) {
				
			buf.append("node [ \n id " + idcounter  + "\n label \"" +  node.getLabel()  + "\"\n") ;			
			buf.append("graphics\n [\n x       35.0\n y       " + leftpos  + " \n fill    \"#FFCC00\"\n]\n");
			buf.append("]\n") ;						
			idtable.put(node, idcounter);
			idcounter++;
			leftpos += 70.0;
			}
		}
		double rightpos = 1.0;
		LinkedList<Node> noderightsort = new LinkedList<Node>();
		noderightsort.addAll(nodesright.values());
		Collections.sort(noderightsort);
		for (Node node: noderightsort) {			
			if (node.getCardinality() ==0) System.out.println(node.getLabel()+" has 0 card");			
		
			buf.append("node [\n id " + idcounter  + "\n label \"" +  node.getLabel()  + "\"\n");
			buf.append("graphics\n [\n x       10000.0\n y       " + rightpos  + " \n fill    \"#00CC00\"\n]\n");
			buf.append("]\n") ;			
			
			idtable.put(node, idcounter);
			idcounter++;
			rightpos += 70.0;
		}
		for (Node node:nodesleft.values()) {			
			if (node.getCardinality() > mincard) {
			for (Edge edge:node.getEdgeList()) {						
				buf.append("edge [\n source "+ idtable.get(edge.node1)+ "\n target "+idtable.get(edge.node2)+"\n label \"\"\n]\n");
			}
			}
			
		}
		
		return buf.toString();
		
	}
	
	/**
	 * Writes the linear program in Mathprog format 
	 * for the set sovering problem represented by the bipartite graph
	 * to a file.
	 * Subsets are on the left. Universe elements on the right.
	 * 
	 * @param filename
	 * @param mincover
	 * 
	 * @return
	 * @deprecated
	 */
	
	public void toSetCoverGPML(String filename,int mincover) {
		try {
			System.out.println("Writing MathProg file " +filename);
			FileWriter fwriter = new FileWriter(new File(filename));
			fwriter.write(toSetCoverGPML(mincover));
			fwriter.flush();
			fwriter.close();
		} catch (Exception e) {
			System.out.println("Could not write GPML-file "+ filename +" because" + e.getMessage());
		}
	
	}
	
	/**
	 * Writes the linear program in Mathprog format 
	 * for the set sovering problem represented by the bipartite graph
	 * directly to a file.
	 * Subsets are on the left. Universe elements on the right.
	 * 
	 * @param filename
	 * @param mincover
	 * 
	 * @return
	 */
	
	public void toSetCoverGPMLStream(String filename,int mincover) {
		try {
			System.out.println("Writing MathProg file " +filename);
			FileWriter fwriter = new FileWriter(new File(filename));
			
			
			fwriter.write("# SET COVER PROBLEM\n");
			fwriter.write("set E;\n");
			fwriter.write("set S;\n");
			fwriter.write("param  M{s in S, e in E} binary;\n");				
			fwriter.write("var x{s in S} binary;\n");
			fwriter.write("minimize covering: sum{s in S} x[s];\n");
			fwriter.write("subject to completeness{e in E}: sum{s in S} M[s,e] * x[s] >= " + mincover +";\n");
			fwriter.write("solve;\n");		
			fwriter.write("printf \"SET: \\n\";");
			
			fwriter.write("for{s in S} {\n");
		    fwriter.write("    printf '%s ', if  x[s] then 'INSET' else 'OUTSET';\n");
		    fwriter.write("    printf '%s\\n', s;\n");
		    fwriter.write(" }\n");

			fwriter.write("data;\n");
			fwriter.write("set E ");
			Iterator<Node> rnodeit = this.nodesright.values().iterator();
			while (rnodeit.hasNext()) {
				Node node = rnodeit.next();
				fwriter.write("\""+node+"\"");
				if (rnodeit.hasNext()) fwriter.write(", ");
			}		
			fwriter.write(";\n");
			fwriter.write("set S ");
			Iterator<Node> lnodeit = this.nodesleft.values().iterator();
			while (lnodeit.hasNext()) {
				Node node = lnodeit.next();
				fwriter.write("\""+node+"\"");
				if (lnodeit.hasNext()) fwriter.write(", ");
			}		
			fwriter.write(";\n");
			
			
			fwriter.write("param M:\n\t");
			rnodeit = this.nodesright.values().iterator();
			while (rnodeit.hasNext()) {
				fwriter.write("\""+rnodeit.next()+"\"\t"); 			
			}
			fwriter.write(":=\n");
			lnodeit = this.nodesleft.values().iterator();
			while (lnodeit.hasNext()) {
				Node setnode = lnodeit.next();
				ArrayList<Node> set = setnode.getActiveAdjacentNodes();
				
				fwriter.write("\""+setnode+"\"\t");			
				rnodeit = this.nodesright.values().iterator();
				while (rnodeit.hasNext()) {
					Node element = (Node) rnodeit.next();				
					boolean setcontainselement = set.contains(element);
					fwriter.write(((setcontainselement)?"1":"0")+"\t"); 			
				}
				fwriter.write("\n");
			}
			fwriter.write(";\n");
			
			fwriter.write("end;");
			
			
				
			fwriter.flush();
			fwriter.close();
		
		
		
		} catch (Exception e) {
			System.out.println("Could not write GPML-file "+ filename +" because" + e.getMessage());
		}
	
	}
	
	/**
	 * Writes the linear program in OPB format 
	 * for the set sovering problem represented by the bipartite graph
	 * directly to a file.
	 * Subsets are on the left. Universe elements on the right.
	 * 
	 * See http://www.mpi-inf.mpg.de/departments/d2/software/opbdp/README 
	 * 
	 * @param filename
	 * @param mincover
	 * 
	 * @return
	 */
	

	public void toSetCoverOPBStream(String filename,int mincover) {
		try {		
		
			System.out.println("Writing OPB file " +filename);
			FileWriter fwriter = new FileWriter(new File(filename));
			

			
			fwriter.write("min: ");
			
			Iterator<Node> lnodeit = this.nodesleft.values().iterator();
			while (lnodeit.hasNext()) {
				Node node = lnodeit.next();
				fwriter.write(node.label.replace('|', 'X'));
				if (lnodeit.hasNext()) fwriter.write(" + ");				
			}		
			fwriter.write(" ;\n");
			
			
			Iterator<Node> rnodeit = this.nodesright.values().iterator();				
			
			
			while (rnodeit.hasNext()) {
				Node setnode = rnodeit.next();
				ArrayList<Node> set = setnode.getActiveAdjacentNodes();
				
				fwriter.write("R"+setnode.label+" :");
				for (Node node : set) {
					fwriter.write(" + 1 " + node.label.replace('|', 'X') );
				}
				fwriter.write(" >= 1;\n");							
			}						
			fwriter.flush();
			fwriter.close();			
		
		} catch (Exception e) {
			System.out.println("Could not write OPB-file "+ filename +" because" + e.getMessage());
		}
	
	}

	/**
	 * Writes the linear program in CPLEX LP format 
	 * for the set sovering problem represented by the bipartite graph
	 * directly to a file.
	 * Subsets are on the left. Universe elements on the right.
	 * 	  
	 * @param filename
	 * @param mincover
	 * 
	 * @return
	 */
	
	public void toSetCoverCPLEXStream(String filename,int mincover) {
		try {		
		
			System.out.println("Writing LP file " +filename);
			FileWriter fwriter = new FileWriter(new File(filename));
			

			fwriter.write("Minimize \n");
			
			fwriter.write(" covering: ");
			
			Iterator<Node> lnodeit = this.nodesleft.values().iterator();
			while (lnodeit.hasNext()) {
				Node node = lnodeit.next();
				fwriter.write("x('"+node.label.replace('|', 'X')+"')");
				if (lnodeit.hasNext()) fwriter.write(" + ");				
			}		
			fwriter.write("\n");
			
			fwriter.write("\n");
			Iterator<Node> rnodeit = this.nodesright.values().iterator();				
			
			fwriter.write("Subject To\n");
			while (rnodeit.hasNext()) {
				Node setnode = rnodeit.next();
				ArrayList<Node> set = setnode.getActiveAdjacentNodes();
				System.out.println(set.size());
				fwriter.write(" completeness("+setnode.label+") :");
				for (Node node : set) {
					fwriter.write(" + x('"+node.label.replace('|', 'X')+"')"  );
				}
				fwriter.write(" >= 1\n");							
			}						
			fwriter.write("\n");
			fwriter.write("BINARY\n");
			lnodeit = this.nodesleft.values().iterator();
			while (lnodeit.hasNext()) {
				Node node = lnodeit.next();
				fwriter.write("	 x('"+node.label.replace('|', 'X')+"')\n" );
				;
			}		
			
			fwriter.write("end\n");
			fwriter.flush();
			fwriter.close();			
		
		} catch (Exception e) {
			System.out.println("Could not write LP-file "+ filename +" because" + e.getMessage());
		}
	
	}
	
	/**
	 *  Checks if a set of left nodes (subset) covers all right nodes (universe elements).
	 * 
	 * @param set
	 * @return
	 */
	public boolean isValidCovering(ArrayList<Node> set) {
		ArrayList<Node> covered =new ArrayList<Node>();
		for (Node node: set) {
			covered.addAll(node.getAdjacentNodes());
		}		
		
		boolean result = covered.containsAll(nodesright.values());
	
		return result;			
	}
	
	/**
	 *  Sorts a covering by means of the greedy heuristic.
	 * 
	 * @param set
	 * @return
	 */
	
	public ArrayList<Node> sortCovering(ArrayList<Node> pset, String filename) {
		ArrayList<Node> set = new ArrayList<Node>(pset);
		
		HashSet<Node> covered =new HashSet<Node>();
		ActiveCardinalityComparator comp = new ActiveCardinalityComparator();
		for (Node node: set) node.setComparator(comp);		
		
		ArrayList<Node> solution = new ArrayList<Node>();
		
		try {
			FileWriter fw = new FileWriter(new File(filename));
				
			while (!set.isEmpty()) {
				Node maxcover = Collections.max(set);
				covered.addAll(maxcover.getAdjacentNodes());
				fw.write(maxcover.label + "\t" + covered.size() +"\t"+maxcover.getActiveCardinality()+"\n");	
				//System.out.println(maxcover.label + ";" + covered.size() +";"+maxcover.getActiveCardinality() );
				solution.add(maxcover);	
				for (Node node: maxcover.getAdjacentNodes()) {
					node.deactivateAllEdges();
				}		
				set.remove(maxcover);
				maxcover.deactivateAllEdges();						
			}
			fw.flush();
			fw.close();
		} catch (Exception e) {
			// TODO: handle exception
		
		}
		
		this.reset();
		return solution;	
				
	}
	
	
	
	/**
	 * Returns all nodes on left side that have a cardinality  bigger than min and lower than max. 
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	
	public ArrayList<Node> getLeftNodeWithCardinality(int min, int max) {
		ArrayList<Node> result = new ArrayList<Node>();
		for (Node node: this.nodesleft.values()) {			
			int card = node.getCardinality();
			if ((card >= min) && (card <= max)) result.add(node);
		}
		return result;
	}
	
	/**
	 * Returns all nodes on right side that have cardinality bigger than min and lower than max. 
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	
	public ArrayList<Node> getRightNodeWithCardinality(int min, int max) {
		ArrayList<Node> result = new ArrayList<Node>();
		for (Node node: this.nodesright.values()) {			
			int card = node.getCardinality();
			if ((card >= min) && (card <= max)) result.add(node);
		}
		return result;
	}	
	
	/**
	 * Returns all nodes on left side that have active cardinality bigger than min and lower than max. 
	 * 
	 * @param min
	 * @param max
	 * @return
	 */

	
	public ArrayList<Node> getLeftNodeWithActiveCardinality(int min, int max) {
		ArrayList<Node> result = new ArrayList<Node>();
		for (Node node: this.nodesleft.values()) {			
			int card = node.getActiveCardinality();
			if ((card >= min) && (card <= max)) result.add(node);
		}
		return result;
	}
	
	/**
	 * Returns all nodes on right side that have active cardinality bigger than min and lower than max. 
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	
	public ArrayList<Node> getRightNodeWithActiveCardinality(int min, int max) {
		ArrayList<Node> result = new ArrayList<Node>();
		for (Node node: this.nodesright.values()) {			
			int card = node.getActiveCardinality();
			if ((card >= min) && (card <= max)) result.add(node);
		}
		return result;
	}	
	
	
	

	
	/**
	 * @param node
	 * @return
	 */
	public ArrayList<Node> findSimilarLeftNodes(Node node) {
		ArrayList<Node> result = new ArrayList<Node>();
		ArrayList<Node> anodes = node.getActiveAdjacentNodes();
		for (Node nodeinner: this.nodesleft.values()) {
			if (nodeinner.getActiveAdjacentNodes().containsAll(anodes) && anodes.containsAll(nodeinner.getActiveAdjacentNodes())) result.add(nodeinner);
		}		
		result.remove(node);
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
	
	/**
	 * 
	 * @param nodestring
	 * @return
	 */
	public ArrayList<Node> getNodeSet(String nodestring) {
		ArrayList<Node> result = new ArrayList<Node>();
		String [] labels  = nodestring.split(" ");
		for (String label:labels   ) {
			if (nodesleft.containsKey(label)) {
				result.add(nodesleft.get(label));
			}
		}
		return result;		
	}
	
	
	public ArrayList<Node> getNodeSet(ArrayList<String> nodelist) {
		ArrayList<Node> result = new ArrayList<Node>();
		for (String label : nodelist) {	
			//System.out.println(label);
			if (nodesleft.containsKey(label)) {
				result.add(nodesleft.get(label));
			} else {
				//System.out.println("NOT IN SET!"  + label);				
			}
			if (nodesright.containsKey(label)) {
				result.add(nodesright.get(label));
			} else {
				//System.out.println("NOT IN SET!"  + label);				
			}
			
			
		}
		return result;		
	}
	

       

    
    
    public int getIndex(Node node) {
    	ArrayList<Node> L = new ArrayList<Node>(nodesleft.values());
    	ArrayList<Node> R = new ArrayList<Node>(nodesright.values());    	
    	if (L.contains(node)) { 
    		return L.indexOf(node);
    	} else {
    		return L.size() + R.indexOf(node); 
    		
    	}
    	
    } 
    
    public Node getNode (int index) {
    	ArrayList<Node> L = new ArrayList<Node>(nodesleft.values());
    	ArrayList<Node> R = new ArrayList<Node>(nodesright.values());    	
    	//System.out.println(L.size());
    	//System.out.println(R.size());
    	
    	if (index< L.size()) { 
    		return  L.get(index);
    	} else {  
    		//System.out.println(index);
    		return R.get(index - L.size());    		
    	}
        
    }

	
    
    
	public static void main(String[] args) {
				
		BipartiteGraph graph = new BipartiteGraph();
		/*int[]  test = new int[200000];
		Random rand = new Random();
		for (int i = 0; i < test.length; i++) {
			test[i] = Math.abs(rand.nextInt());			
		}
		int[] rounded = graph.round(test);
		long time = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			int[] rounded = graph.round(test);
			double norm = graph.norm(rounded);			
		}
		
		//System.out.println(System.currentTimeMillis()-time);
		for (int i = 0; i < rounded.length; i++) {
			System.out.println(test[i]+" > "+rounded[i]);
		}
		*/
		
		//graph.addEdgeSecure("A", "U");
		
		graph.addEdgeSecure("A", "X");
		graph.addEdgeSecure("B", "X");
		graph.addEdgeSecure("A", "Y");
		
//		graph.addEdgeSecure("B", "Y");
		
		graph.addEdgeSecure("B", "Y");
		graph.addEdgeSecure("B", "U");
		graph.addEdgeSecure("C", "X");
		graph.addEdgeSecure("C", "U");		
    	graph.addEdgeSecure("C", "Y");
    	graph.addEdgeSecure("D", "V");
    	
    	
		//int numgroups=200;
    	//graph.populate(numgroups,10,10,3000,0.6);
		//System.out.println("\nTEST:"+graph.getNode(0));
		//System.out.println("\nLEFTNODES:" + graph.nodesleft.values());
		
		
	/*	for (int i =0; i < numgroups;i++) {
			
			System.out.println(graph.getLocalDenseSubgraph(graph.getNode(i+"_2_L"), 10000));	
		}*/
		
	
		/*	
		ArrayList<HashSet<Node>> cliquedecomp =  graph.getAllComponents();
		for (HashSet<Node> clique : cliquedecomp) {
			System.out.println(clique.size());
			for (Node node : clique) {
				System.out.print(node+" ");
			}
			
			System.out.println();
		}
		*/
		System.out.println(graph.getSetCoverLinearProgram(1).convertToCPLEX());
		ArrayList<Node> result = graph.solveCoveringProblemLP(SolverFactory.newDefault(),1);
		if (graph.isValidCovering(result)) System.out.print("Is valid solution!!");
		
		//System.out.print(graph.toSetCoverGPML(2));
		//	System.out.print(graph.toGML());
		//graph.toGML("test.gml");
		//graph.solveSetCoveringProblemGreedy();		
	}


	public HashMap<String, Node> getNodes() {
		HashMap<String, Node> result = new HashMap<String, Node>();
		result.putAll(this.nodesleft);
		result.putAll(nodesright);
		
		return result;
	}


	public int getNumberNodes() {
		return nodesleft.size() +  nodesright.size();
	}
		
}
