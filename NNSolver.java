import java.util.ArrayList;

public class NNSolver extends TSPSolver {
	public int [] nn;// array that contains the nearest neighbour and its insertion position
	
	public NNSolver () {
		super(); // calls superclass constructor
		setName_abbrev("NN");
		setName("nearest neighbor");
		nn = new int[2];
		this.nn[0] = 1;
		this.nn[1] = 0; // initial city that will be visited is the first city
	}
	
	public NNSolver (ArrayList < Graph > G ) {
		super(G); // calls superclass constructor
		setName_abbrev("NN");
		setName("nearest neighbor");
		nn = new int[2];
		this.nn[0] = 1;
		this.nn[1] = 0; // initial city that will be visited is the first city
	}
	
	// setters and getters for nn
	public int getNearestNeighbour() {return this.nn[0];}
	public void setNN(int i,int j) {
		this.nn[0] = i;
		this.nn[1] = j;
	}

	@Override
	public int [] nextNode (Graph G, ArrayList <Integer > visited ) {
		if(visited.isEmpty()) { setNN(1,0); return nn ;}
		
		int i = nearestNeighbor(G, visited,getNearestNeighbour()); // gets the next node to be added
		if (i == -1)return null; // if there is no next node, return null
		else{setNN(i,visited.size());return nn;} // else input next node
	}
}
