package mortar01;

import java.util.ArrayList;
import mapsearch.*;

class SearchResult{
	
	public long numNodesEnqueued, numNodesDequeued;
	boolean solutionWasFound;
	public double solutionDistance;
	ArrayList<Long> solutionPath;
	
	public SearchResult(){
		this.numNodesEnqueued = this.numNodesDequeued = 0;
		this.solutionWasFound = false;
		this.solutionPath = null;
		this.solutionDistance = -1.0;
	}
	
}