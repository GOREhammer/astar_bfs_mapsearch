package mortar01;

import java.io.*;
import java.util.*;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

import mapsearch.*;
import mortar01.*;

public class Searcher{

	public static final int ASTAR = 0;
	public static final int BFS = 1; 
	public static  StateNode startState;
	public static  StateNode goalState;


    public static void main(String [] args){

	if(args.length != 7){
	    System.err.println("USAGE: java Searcher nodesFile linksFile startNodeID endNodeID resultFile useGraphSearch(0 or 1) algorithm(astar or bfs)");
	    return;
	}

	// create the RoadNetwork
	RoadNetwork theRoads = null;
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

	final boolean useGraphSearch = (Integer.parseInt(args[5]) != 0);

	int algorithm = 0;
	String temp = args[6];
	if("astar".equals(temp))
		algorithm = ASTAR;
	else if("bfs".equals(temp)){
		algorithm = BFS;
	}	
	temp=null;

	
	// TODO: actually do the search

	// TODO: During the search, write to osmOut:
	//  for each node searched: id latitude longitude
	//     	osmOut.println(newSearchNode.state.id + " " + newSearchNode.state.lat + " " + newSearchNode.state.lon);
	//
	//  for each edge searched: startID endID
	//      osmOut.println(e.id1 + " " + e.id2);
	startState = theRoads.getNode(Long.parseLong(args[2]));
	goalState= theRoads.getNode(Long.parseLong(args[3]));
	
	PriorityQueue<SearchNode> fringe = new PriorityQueue<SearchNode>();
	if(BFS == algorithm){
		fringe.add(new BFSSearchNode(startState, null, null, -1, 0 )); // -1 to offset for root 
	}else if(ASTAR == algorithm){
		//TODO		
	}
	
	osmOut.close();

	/*
	  TODO: write the results file (which is in addition to the OSM file)

	  Here is an example:
	try{
	    BufferedWriter output = new BufferedWriter(new FileWriter(args[4]));
	    output.write("Number of nodes enqueued: " + result.numNodesEnqueued + "\n");
	    output.write("Number of nodes dequeued: " + result.numNodesDequeued + "\n");
	    output.write("Was solution found? " + (result.solutionWasFound ? "yes" : "no") + "\n");
	    if(result.solutionWasFound){
		output.write("Solution distance: " + result.solutionDistance + "\n");
		output.write("Number of steps in solution: " + (result.solutionPath.size()-1) + "\n");
		for(int i=0; i<result.solutionPath.size(); i++){
		    output.write(result.solutionPath.get(i).toString() + "\n");
		}
	    }
	    output.flush();
	    output.close();
	}
	catch(IOException ioe){
	    System.err.println(ioe.getMessage());
	}

*/	
    }
    
    public Result astarSearch(){
    	return null;
    }
    
    public Result bfsSearch(){
    	return null;
    	
    }

}
