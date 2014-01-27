package mortar01;

import mapsearch.*;

public class AStarSearchNode extends SearchNode{
	
	private double h , g;
		
	public AStarSearchNode(StateNode state, AStarSearchNode parent, StateGraphEdge action, long depth){
		super(state, (SearchNode)parent, action, (EuclideanHeuristic.eval(state, Searcher.goalState)) + 
				(parent.getG() + action.distance), depth);
		
		this.h = EuclideanHeuristic.eval(state, Searcher.goalState); //recalculate the h and g for the class
		this.g = parent.getG() + action.distance;					 //because java is stupid
	}
	public double getH(){return this.h;}
	public double getG(){return this.g;}	
}
