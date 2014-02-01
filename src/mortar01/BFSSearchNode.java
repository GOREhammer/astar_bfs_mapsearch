package mortar01;

import mapsearch.StateGraphEdge;
import mapsearch.StateNode;

public class BFSSearchNode{
	
	private double f;
	private StateNode state;
	private BFSSearchNode parent;
	private StateGraphEdge action;
	private long depth;

	public BFSSearchNode(StateNode state, BFSSearchNode parent, StateGraphEdge action, long depth) {
		if(null == parent){	// root node corner case
			this.f = 0.0;
		}else{
			this.f = parent.f + 1; //uniform action cost			
		}
		this.state = state;
		this.parent = parent;
		this.action = action;
	}
	
	public double getF(){return this.f;}
	public StateNode getState(){return this.state;}
	public BFSSearchNode getParent(){return this.parent;}
	public StateGraphEdge getAction(){return this.action;}
	public long getDepth(){return this.depth;}
}
