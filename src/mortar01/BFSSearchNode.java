package mortar01;

import mapsearch.StateGraphEdge;
import mapsearch.StateNode;

public class BFSSearchNode extends SearchNode {
	
	private double g;

	public BFSSearchNode(StateNode state, BFSSearchNode parent, StateGraphEdge action, double prevG, long depth) {
		super(state, (SearchNode)parent, action, prevG + 1, depth);
		this.g = prevG + 1;	// ignore action.distance: BFS assumes unit action cost
	}
	
	public double getG() {
		return g;
	}

}
