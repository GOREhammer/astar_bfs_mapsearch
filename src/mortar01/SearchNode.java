package mortar01;

import mapsearch.*;

public class SearchNode implements Comparable<SearchNode>{
	
	private final StateNode state;
	private final SearchNode parent;	
	private final StateGraphEdge action;

	private final double f;
	private final long depth;
	
	public SearchNode(StateNode state, SearchNode parent, StateGraphEdge action, double f, long depth){
		this.state = state;
		this.parent = parent;
		this.action = action;
		this.f = f;
		this.depth = depth;
	}

	public StateNode getState(){return this.state;}
	public SearchNode getParent(){return this.parent;}
	public StateGraphEdge getAction(){return this.action;}
	public double getF(){return this.f;}
	public long getDepth(){return this.depth;}

	@Override
	public int compareTo(SearchNode other) {
		if(this.f < other.f){
			return -1;
		}else if (this.f > other.f){
			return 1;
		}else{
			if(this.state.id < other.getState().id){
				return -1;
			}else return 1;
		}
	}
	
	public boolean equals(SearchNode other){
		return (this.state.id == other.getState().id);
	}

	
}
