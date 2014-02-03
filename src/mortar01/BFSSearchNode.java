package mortar01;

import java.util.ArrayList;

import mapsearch.EuclideanHeuristic;
import mapsearch.StateGraphEdge;
import mapsearch.StateNode;

public class BFSSearchNode implements Comparable<BFSSearchNode>{
	
	private StateNode state;
	private BFSSearchNode parent;
	private StateGraphEdge action;
	private long depth; // f in this case
	private double totalDistance;

	public BFSSearchNode(StateNode state, BFSSearchNode parent, StateGraphEdge action, long depth) {
		if(null == parent){	// root node corner case
			this.depth = 0;
			this.totalDistance = 0.0;
		}else{
			this.depth = parent.getDepth() + 1; //uniform action cost
			this.totalDistance = parent.getTotalDistance() + action.distance;
		}
		this.state = state;
		this.parent = parent;
		this.action = action;
	}
	
	public ArrayList<BFSSearchNode> getNeighbors(){
		ArrayList<BFSSearchNode> neighbors = new ArrayList<BFSSearchNode>();
		for(StateGraphEdge e: Searcher.theRoads.getOutgoingEdges(this.state.id)){
			neighbors.add(new BFSSearchNode(Searcher.theRoads.getNode(e.id2), this, e, this.depth+1));
		}
		if(neighbors.size() != 0){
			return neighbors;
		}else{		
			return null;
		}
	}
	
	public StateNode getState(){return this.state;}
	public BFSSearchNode getParent(){return this.parent;}
	public StateGraphEdge getAction(){return this.action;}
	public long getDepth(){return this.depth;}
	public double getTotalDistance(){return this.totalDistance;}
	
	@Override
	public int compareTo(BFSSearchNode o) {
		if(this.depth < o.getDepth()){
			return -1;
		}else if(this.depth > o.getDepth()){
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
		return (this.state.id == ((BFSSearchNode)o).getState().id);
	}
}
