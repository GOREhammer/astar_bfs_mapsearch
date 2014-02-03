package mortar01;

import java.util.ArrayList;

import mapsearch.*;

public class AStarSearchNode implements Comparable<AStarSearchNode>{
	
	private double h, g, f;
	private long depth;
	private StateNode state;
	private AStarSearchNode parent;
	private StateGraphEdge action;
	
		
	public AStarSearchNode(StateNode state, AStarSearchNode parent, StateGraphEdge action, long depth){
		this.h = EuclideanHeuristic.eval(state, Searcher.goalState);
		if(null == parent){	// root node corner case
			this.g = 0.0;
		}else{
			this.g = parent.g + action.distance;			
		}
		this.f = this.g + this.h;
		this.state = state;
		this.parent = parent;
		this.action = action;
		this.depth = depth;
	}
	
	public double getH(){return this.h;}
	public double getG(){return this.g;}
	public double getF(){return this.f;}
	public StateNode getState(){return this.state;}
	public AStarSearchNode getParent(){return this.parent;}
	public StateGraphEdge getAction(){return this.action;}
	public long getDepth(){return this.depth;}
	
	public ArrayList<AStarSearchNode> getNeighbors(){
		ArrayList<AStarSearchNode> neighbors = new ArrayList<AStarSearchNode>();
		for(StateGraphEdge e: Searcher.theRoads.getOutgoingEdges(this.state.id)){
			neighbors.add(new AStarSearchNode(Searcher.theRoads.getNode(e.id2), this, e, this.depth+1));
		}
		if(neighbors.size() != 0){
			return neighbors;
		}else{		
			return null;
		}
	}

	@Override
	public int compareTo(AStarSearchNode o) {
		if(this.f < o.getF()){
			return -1;
		}else if(this.f > o.getF()){
			return 1;
		}else{
			if(this.state.id < o.getState().id){
				return -1;
			}else if(this.state.id > o.getState().id){
				return 1;
			}else{
				return 0;
			}
		}
	}
	
	public boolean equals(Object o){
		return (this.state.id == ((AStarSearchNode) o).getState().id);
	}
}
