package scpsolver.graph;

import java.util.HashMap;

public interface GraphInterface {

	/**
	 * @return  the nodesright
	 * @uml.property  name="nodes"
	 */
	public abstract HashMap<String, Node> getNodes();

	public abstract Node getNode(String label);

	public abstract int getNumberNodes();

	public abstract boolean hasEdge(String leftlabel, String rightlabel);

	public abstract Edge addEdgeSecure(String leftlabel, String rightlabel);

}