package mortar01;

import java.io.*;
import java.util.*;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;
import mapsearch.*;
import mortar01.*;

public class Searcher{

	public static final int ASTAR = 0;
	public static final int BFS = 1;
	public static RoadNetwork theRoads;
	public static StateNode startState;
	public static StateNode goalState;
	
public static void main(String [] args){

	if(args.length != 7){
	    System.err.println("USAGE: java Searcher nodesFile linksFile startNodeID endNodeID resultFile " +
	    		"useGraphSearch(0 or 1) algorithm(astar or bfs)");
	    return;
	}

	// create the RoadNetwork
	Searcher.theRoads = null;
	try{
	    theRoads = new RoadNetwork(args[0], args[1]);
	}
	catch(IOException ioe){
	    System.err.println(ioe.getMessage());
	}

	// set up the means to write the OSM file
	PrintWriter osmOut = null;
	try {
	    final PipedWriter osmOutPipe = new PipedWriter();
	    final PipedReader in = new PipedReader(osmOutPipe);

	    Runnable runOSM = new Runnable() {
		    public void run() {
			CreateOSM cosm = new CreateOSM();
			cosm.writeOSMFile("results.osm", new BufferedReader(in), 20);
		    }
		};

	    Thread osmWritingThread = new Thread(runOSM, "OSM Writing Thread");
	    osmWritingThread.start();

	    osmOut = new PrintWriter(new BufferedWriter(osmOutPipe));
	}
	catch (IOException x) {
	    x.printStackTrace();
	}

	// set if we do graph search(1 or not 0) or not(0)
	final boolean useGraphSearch = (Integer.parseInt(args[5]) != 0);

	int algorithm = 0;
	String temp = args[6];
	if("astar".equals(temp))
		algorithm = Searcher.ASTAR;
	else if("bfs".equals(temp)){
		algorithm = Searcher.BFS;
	}	
	temp=null;
	
	// TODO: During the search, write to osmOut:
	//  for each node searched: id latitude longitude
	//     	osmOut.println(newSearchNode.state.id + " " + newSearchNode.state.lat + " " + newSearchNode.state.lon);
	//
	//  for each edge searched: startID endID
	//      osmOut.println(e.id1 + " " + e.id2);
	Searcher.startState = theRoads.getNode(Long.parseLong(args[2]));
	Searcher.goalState= theRoads.getNode(Long.parseLong(args[3]));
	SearchResult searchResult = null;
	if(algorithm == Searcher.ASTAR){
		 searchResult = astarSearch(useGraphSearch);
	}else if(algorithm == Searcher.BFS){
		searchResult = bfsSearch(useGraphSearch);
	}
	
	osmOut.close();

	// write to the results file given as an argument(args[4])
	try{
	    BufferedWriter output = new BufferedWriter(new FileWriter(args[4]));
	    output.write("Number of nodes enqueued: " + searchResult.numNodesEnqueued + "\n");
	    output.write("Number of nodes dequeued: " + searchResult.numNodesDequeued + "\n");
	    output.write("Was solution found? " + (searchResult.solutionWasFound ? "yes" : "no") + "\n");
	    if(searchResult.solutionWasFound){
	    	output.write("Solution distance: " + searchResult.solutionDistance + "\n");
	    	output.write("Number of steps in solution: " + (searchResult.solutionPath.size()-1) + "\n");
		while(!searchResult.solutionPath.empty()){
		    output.write(searchResult.solutionPath.pop() + "\n");
		}
	    }
	    output.flush();
	    output.close();
	}
	catch(IOException ioe){
	    System.err.println(ioe.getMessage());
	}

    }
    
    public static SearchResult astarSearch(final boolean useGraphSearch){
    	PriorityQueue<AStarSearchNode> fringe = new PriorityQueue<AStarSearchNode>();
    	SearchResult searchResult = new SearchResult();
    	fringe.add(new AStarSearchNode(startState, null, null, 0));
    	searchResult.numNodesEnqueued++;
    	
    	while(!fringe.isEmpty()){
    		AStarSearchNode node = fringe.poll();
    		searchResult.numNodesDequeued++;
    		if(useGraphSearch)
    			node.getState().isClosed = true;
    		
      		if(node.getState().id == goalState.id){	// a goal node!
    			searchResult.solutionWasFound = true;
       			searchResult.solutionDistance = node.getG();
    			searchResult.solutionPath = new Stack<Long>();
    			while(node != null){ // unravel the path
    				searchResult.solutionPath.push(node.getState().id);
    				node = node.getParent();
    			}
    			break;
     		}
      		
      		// add the neighbors to the fringe
      		ArrayList<AStarSearchNode> neighbors = node.getNeighbors();
      		if(null != neighbors){
      			if(!useGraphSearch){
      				fringe.addAll(neighbors);
      				searchResult.numNodesEnqueued += neighbors.size();
      			}else{
      				for(AStarSearchNode n : neighbors){
      					if(!n.getState().isClosed){
	  						if(fringe.contains(n)){
	  							if(n.getF() < n.getState().bestCostSoFar){
	  								fringe.remove(n);
	  								n.getState().bestCostSoFar = n.getF();
	  	      						fringe.add(n);
	  							}
	  						}else{
	  							n.getState().bestCostSoFar = n.getF();
	      						fringe.add(n);
	      						searchResult.numNodesEnqueued++;
	  						}
      					}
      				}
      			}	
      		}
    	} // end while
    	return searchResult;
    }
    
    
    public static SearchResult bfsSearch(final boolean useGraphSearch){
    	PriorityQueue<BFSSearchNode> fringe = new PriorityQueue<BFSSearchNode>();
    	SearchResult searchResult = new SearchResult();
    	fringe.add(new BFSSearchNode(startState, null, null, 0)); // add root node
     	searchResult.numNodesEnqueued++;
    	
    	while(!fringe.isEmpty()){
    		BFSSearchNode node = fringe.poll();
    		searchResult.numNodesDequeued++;
    		if(useGraphSearch)
    			node.getState().isClosed = true;
    		
      		if(node.getState().id == goalState.id){	// a goal node!
    			searchResult.solutionWasFound = true;
       			searchResult.solutionDistance = node.getTotalDistance();
       			searchResult.solutionPath = new Stack<Long>();
    			while(node != null){ // push path onto stack in reverse
    				searchResult.solutionPath.push(node.getState().id);
    				node = node.getParent();
    			}
    			break;
     		}
      		// add the neighbors to the fringe
      		ArrayList<BFSSearchNode> neighbors = node.getNeighbors();
      		if(null != neighbors){
      			if(!useGraphSearch){
      				fringe.addAll(neighbors);
      				searchResult.numNodesEnqueued += (long) neighbors.size();
      			}else{
      				for(BFSSearchNode n : neighbors){
      					if(!n.getState().isClosed && !fringe.contains(n)){
      						fringe.add(n);
      						searchResult.numNodesEnqueued++;
      					}
      				}
      			}	
      		}
     	} // end while
    	return searchResult;
    }
}
