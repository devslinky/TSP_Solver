import java.util.ArrayList;

public class NISolver extends TSPSolver {
	public int[] nn;// array that contains nodes nearest neighbour and its insertion position

	public NISolver() {
		super(); // calls superclass constructor
		nn = new int[2];
		setName("node insertion");
		setName_abbrev("NI");
	}

	public NISolver(ArrayList<Graph> G) {
		super(G); // calls superclass constructor
		setName_abbrev("NI");
		setName("node insertion");
		nn = new int[2];
	}

	// setters and getters for first and last
	public int getnn() {
		return this.nn[0];
	}

	public void setnn(int i, int j) {
		this.nn[0] = i;
		this.nn[1] = j;
	}

	@Override
	public int[] nextNode(Graph G, ArrayList<Integer> visited) {
		int NN, NN_final, node_connecting_toNN; // contains nearest neighbour, the nearest NN and the node that connects to the nearest NN
		NN_final = 0;
		node_connecting_toNN = 0;
		double cost_of_NN = Double.MAX_VALUE; // contains cost of nearest neighbour

		if (visited.isEmpty()) {
			setnn(1, 0);
			return nn;
		}
		
		// parses through visited arraylist and finds shortest path, nearest neighbour
		for (int i : visited) {
			NN = runNearestNeighbour(G, visited, i);
			if (NN == -1)continue; // if no NN was found, skip to next
			
			if(visited.indexOf(i)> 0 && visited.indexOf(i)< visited.size()-1) { // checks if node can be inserted into middle of arraylist
				 if(canBeInserted(G,visited,visited.indexOf(i)+1,NN) == false) { // if node can not be added to middle of arraylist then get next NN of node i
					 NN = getvalidNearestNeighbour(G,visited,i,NN);
					 } 
				}
			
			if(NN == -1)continue; // of no NN was found, skip to next
			
			double cost = G.getCost(i - 1, NN - 1);

			if (cost < cost_of_NN) {
				cost_of_NN = cost;
				NN_final = NN;
				node_connecting_toNN = i;
			}
		}
		
		if (cost_of_NN == Double.MAX_VALUE)return null; // no NN was found for any node in arraylist
		
		if (node_connecting_toNN == visited.get(0)) {
			setnn(NN_final, 0); // adds node to front of arraylist if it is NN of node 1
		} else {
			setnn(NN_final, visited.indexOf(node_connecting_toNN) + 1); // adds NN to node i
		}
		return nn;
	}
	
	public int getvalidNearestNeighbour(Graph G, ArrayList<Integer> visited, int i, int NN) {
		ArrayList<Integer> visited_temp = new ArrayList<Integer>(); 
		
		for(int j : visited)visited_temp.add(j);
		
		int n_n =NN;
		
		 // basically do NN algorithm for node i until a nearest neighbour is found
		 while(canBeInserted(G,visited_temp,visited_temp.indexOf(i)+1,n_n) == false){
			 visited_temp.add(n_n); 
			 n_n = runNearestNeighbour(G,visited_temp,i);
			 if(n_n == -1)break;
			 } 
		 return n_n;
	}
	
	
	public int runNearestNeighbour(Graph G, ArrayList<Integer> visited, int node) {
		int i = nearestNeighbor(G, visited, node); // gets the next node to be added
		return i;
	}

	// check if node k can be inserted at position i
	public boolean canBeInserted(Graph G, ArrayList<Integer> visited, int i, int k) {
		return G.existsArc(k - 1, visited.get(i) - 1);
	}
}
