import java.util.ArrayList;

public class NNFLSolver extends TSPSolver {
	public int [] nn;// array that contains nodes nearest neighbour and its insertion position
	
	public NNFLSolver () {
		super(); // calls superclass constructor
		setName_abbrev("NN-FL");
		setName("nearest neighbor first-last");
		nn = new int[2];
	}
	
	public NNFLSolver (ArrayList < Graph > G ) {
		super(G); // calls superclass constructor
		setName_abbrev("NN-FL");
		setName("nearest neighbor first-last");
		nn = new int[2];
	}
	
	// setters and getters for first and last
		public int getnn() {return this.nn[0];}
		public void setnn(int i,int j) {
			this.nn[0] = i;
			this.nn[1] = j;
		}
		
	@Override
	public int [] nextNode ( Graph G , ArrayList < Integer > visited ) {
		int first_index_value, last_index_value; // corresponds to first and last index of visited arraylist
		int next_node, next_node_first, next_node_last; // containts next node
		
		if(visited.isEmpty()) {
			setnn(1,0); 
			return nn ;
		}
		
		first_index_value = visited.get(0); // gets first value in visited arraylist
		last_index_value = visited.get(visited.size()-1); // gets last value in visited arraylist
		
		if(first_index_value == last_index_value) { // if there is only the first node in arraylist then do NN for first node
			next_node = runNearestNeighbour(G,visited,first_index_value);
			if(next_node != -1) {
				setnn(next_node,1);
				return nn;
			}
			else return null;
		}
		
		next_node_first = runNearestNeighbour(G,visited,first_index_value);
		next_node_last = runNearestNeighbour(G,visited,last_index_value);
		
		
		if(next_node_first == -1 && next_node_last == -1) {
			return null; // no next node was found for either first and last node
		}
		else if(next_node_first != -1 && next_node_last != -1) {
			// next node was found for both first and last node
			double costfirst = G.getCost(first_index_value -1,next_node_first-1);
			double costlast =  G.getCost(last_index_value -1,next_node_last-1);
			
			if(costfirst < costlast) {
				// next node is the first nodes NN
				setnn(next_node_first,0);
				return nn;
			}
			else {
				// next node is the last nodes NN
				setnn(next_node_last,visited.size());
				return nn;
			}
		}
		else {
			//  next node was found for either the first or last node
			if(next_node_first == -1) {
				// next node is the last nodes NN
				setnn(next_node_last,visited.size());
				return nn;
			}
			else {
				// next node is the first nodes NN
				setnn(next_node_first,0);
				return nn;
			}
		}		
	}
	
	public int runNearestNeighbour(Graph G, ArrayList < Integer > visited, int node) {
		int i = nearestNeighbor(G, visited,node); // gets the next node to be added
		return i;
	}

}

