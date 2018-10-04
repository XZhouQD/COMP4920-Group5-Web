package scpsolver.graph;

import java.awt.Color;

public class ColoredEdge extends Edge {
	Color color;
	
	public ColoredEdge(Node node1, Node node2) {
		super(node1, node2);
		color = Color.BLACK;
	}
	
	public ColoredEdge(Node node1, Node node2, Color c) {
		super(node1, node2);
		color = c;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
