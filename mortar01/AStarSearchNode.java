package mortar01;

import mapsearch.*;

public class AStarSearchNode extends SearchNode{
	
	private double h;
		
	public AStarSearchNode(StateNode state, AStarSearchNode parent, StateGraphEdge action, long depth){
		super(state, (SearchNode)parent, action, (EuclidianHeuristic.eval(state, Searcher.goalState)) + (parent.getG() + action.distance), depth);
		this.h = EuclidianHeuristic.eval(state, Searcher.goalState); //recalculate the h, java is stupid
	}

	public double getH(){return this.h;}
	public double getG(){return this.g;}	
}
