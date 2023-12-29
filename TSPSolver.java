import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TSPSolver {
	private ArrayList <int [] > solnPath ; // ArrayList *or* array of solution paths
	private double [] solnCost ; // ArrayList *or* array of solution costs
	private double [] compTime ; // ArrayList *or* array of computation times
	private boolean [] solnFound ; // ArrayList *or* array of T/F solns found
	private boolean resultsExist ; // whether or not results exist
	private String name ; // name of this solver
	private String name_abbrev; // gets abbreviated name of this solver
	Stats stats = new Stats(); // stats object used for statistical calculations
	
	 // constructors
	public TSPSolver () { 
		this.solnPath = new ArrayList<int[]>();
		this.solnCost = new double[0];
		this.compTime = new double[0];
		this.solnFound = new boolean[0];
		this.resultsExist = false;
	}
	
	public TSPSolver ( ArrayList < Graph > G ) { 
		this.solnPath = new ArrayList<int[]>();
		this.solnCost = new double[G.size()];
		this.compTime = new double[G.size()];
		this.solnFound = new boolean[G.size()];
		this.resultsExist = false;
	}
	
	 // getters
	public int [] getSolnPath ( int i ) {
		return this.solnPath.get(i);
	}
	public double getSolnCost ( int i ) {
		return this.solnCost[i];
	}
	public double getCompTime ( int i ) {
		return this.compTime[i];
	}
	public boolean getSolnFound ( int i ) {
		return this.solnFound[i];
	}
	public boolean hasResults () {
		return this.resultsExist;
	}
	public String getName () {
		return this.name;
	}
	public String getName_abbrev () {
		return this.name_abbrev;
	}
	
	// setters
	public void setSolnPath ( int i , int [] solnPath ) {
		this.solnPath.set(i, solnPath);
	}
	public void setSolnCost ( int i , double solnCost ) {
		this.solnCost[i] = solnCost;
	}
	public void setCompTime ( int i , double compTime ) {
		this.compTime[i] = compTime;
	}
	public void setSolnFound ( int i , boolean solnFound ) {
		this.solnFound[i] = solnFound;
	}
	public void setHasResults ( boolean b ) {
		this.resultsExist = b;
	}
	public void setName ( String name ) {
		this.name = name;
	}
	public void setName_abbrev ( String name_abbrev ) {
		this.name_abbrev = name_abbrev;
	}
	
	// initialize variables and arrays
	public void init ( ArrayList < Graph > G ) {
		Arrays.fill(solnCost, 0);
		Arrays.fill(compTime, 0);
		Arrays.fill(solnFound, false);
	} 
	
	 // reset variables and arrays
	public void reset () {
		solnPath.clear();
		Arrays.fill(solnCost, 0);
		Arrays.fill(compTime, 0);
		Arrays.fill(solnFound, false);
		setHasResults(false);
	}
	
//  variables are set to specific values depending on if a solution is found
	public void setVariables(ArrayList < Integer > visited, Graph G, int j, long p, boolean b ) {
		setSolnFound(j,b);
		if(b) {
			setHasResults(b);
	
			int[] solnpath = new int[G.getN() + 1]; //finds solution path
			solnpath = visited.stream().mapToInt(i -> i).toArray(); // converts int arraylist to int array
			solnPath.add(solnpath);
			
			//finds solution path cost
			double cost = G.pathCost(solnpath);
			setSolnCost(j, cost);
			
			// adds solution computation time
			setCompTime(j,p);
		}
		else solnPath.add(null);
		return;
	}
	
	 // run nearest neighbor on Graph i
	public void run ( ArrayList < Graph > G , int i , boolean suppressOutput ) {
		ArrayList<Integer> visited = new ArrayList<Integer>();
		int []nextnode = new int [2]; // will store values for next node
		
		long start = System . currentTimeMillis () ; // Get current time
		
		while(visited.size() < G.get(i).getN()) {
			nextnode = nextNode(G.get(i),visited);
			if(nextnode == null) { // if next node is null then there is no solution for graph
				setSolnFound(i,false);
				setVariables(visited,G.get(i),i,1000,getSolnFound(i));
				return;
			}
			else visited.add(nextnode[1], nextnode[0]); // adds next node in insertion position 
		}
		
		long elapsedTime = System.currentTimeMillis () - start ; // Get elapsed time in ms
		
		// Checks if the last node connects to the first node and sets variables accordingly
		if((G.get(i).existsArc(visited.get(visited.size()-1)-1,visited.get(0)-1))) {
			setSolnFound(i,true);
			visited.add(visited.get(0));
		}
		else setSolnFound(i,false);
		setVariables(visited,G.get(i),i,elapsedTime,getSolnFound(i)); 
		
		return;
	}
	
	
	
	// find next node( return 2D array with [0]= next node , [1]= insertion position )
	public int [] nextNode ( Graph G , ArrayList < Integer > visited ) {
		return null;
	} 
	
	// find node k’s nearest unvisited neighbor
	public int nearestNeighbor ( Graph G , ArrayList < Integer > visited , int k ) {
		// one array will contain all costs and the other will contain the cities associated with the cost
		double[] indexsort = new double[G.getN()]; 
		double[] costsort = new double[G.getN()]; 
		
		for(int i = 0; i < costsort.length; i++ ) {
			indexsort[i] = i+1;
			// only nodes that have not been visited with paths that exist will be considered
			if(G.existsArc(i, k - 1) && visited.contains(i+1) == false) costsort[i] = G.getCost(k - 1, i);
			else costsort[i] = Double.MAX_VALUE;
		}
		
		// quicksort array to find smallest cost and arc associated with it 
		quicksort(costsort,0,costsort.length -1,indexsort);
		
		if(costsort[0] == Double.MAX_VALUE) return -1;// if the minimum value of the sorted array only contains Double Max value, then no new nodes were available, thus no solution found
		else return((int)indexsort[0]); // returns index of smallest cost
	} 
	
	public static void quicksort(double[]array, int lowIndex, int highIndex, double[]arrayindex) {
		// if array is only 1 element
		if(lowIndex >= highIndex) {
			return;
		}
		
		double pivot = array[highIndex];
		int leftpointer = lowIndex; 
		int rightpointer = highIndex; 
		
		// checks if array is sorted accorded to the pivot
		while(leftpointer < rightpointer) {
			// checks when left-side array values are greater than pivot
			while(array[leftpointer]<= pivot && leftpointer < rightpointer) {
				leftpointer++;
			}
			//checks when right-side array values are less than pivot
			while (array[rightpointer] >= pivot && leftpointer < rightpointer) {
				rightpointer--;
			}
			// when both while conditions are met, right-side and left-side array values are swapped
			swap(array,leftpointer,rightpointer);
			
			//also need to swap the array with indexes to keep track of what index corresponds to each cost value in the previous array
			swap(arrayindex,leftpointer,rightpointer);
		}
		// when array is sorted according to pivot, the last array value is swapped with pivot
		swap(array,leftpointer,highIndex);
		
		//also need to swap the array with indexes to keep track of what index corresponds to each cost value in the previous array
		swap(arrayindex,leftpointer,highIndex);
		
		// recursively sorts the sub arrays to the left and right of the pivot
		quicksort(array,lowIndex,leftpointer-1,arrayindex);
		quicksort(array,rightpointer+1,highIndex,arrayindex);
	}
	
	// swaps values of 2 indexes in an array
	public static void swap(double[] array, int index1, int index2) {
		double temp = array[index1];
		array[index1] = array[index2];
		array[index2] = temp;
	}
	
	// calculate success rate
	public double successRate () {
		int countTrue=0;
		
		// counts the number of true values in solnFound
		for(boolean i : solnFound) {
			if(i== true)countTrue++;
		}
		double successrate = (double)(countTrue *100 )/solnPath.size();
		return successrate;
		
	} 
	
	// gives trimmed arrays that removes any graphs where no tsp solution was found
	public double[] TrimmedSolnCost() {return stats.trimedArray(solnCost,solnPath.size(), solnFound);}
	public double[] TrimmedCompTime() {return stats.trimedArray(compTime, solnPath.size(), solnFound); }
	
	 // calculate average cost of successful routes
	public double avgCost () {
		double[]trimmedsolnCost = TrimmedSolnCost();
		double cost_average = stats.calcAverage(trimmedsolnCost,trimmedsolnCost.length);
		return cost_average;
	}
	
	// calculate average comp time of successful routes
	public double avgTime () {
		double[]trimmedcompTime = TrimmedCompTime();
		double time_average = stats.calcAverage(trimmedcompTime, trimmedcompTime.length);
		return time_average;
	} 
	
	// print results for a single graph
	public void printSingleResult ( int i , boolean rowOnly ) {
		if(rowOnly) {
			System.out.format("%3d %16.2f %18.3f",i+1,getSolnCost(i),getCompTime(i));
			System.out.format("   ");
			
			// converts the ith solution path to an array
			int []path = solnPath.get(i);
			
			for(int k = 0; k < path.length-1; k++) {
				System.out.printf("%d-",path[k]);
			}
			System.out.printf("%d",path[path.length - 1]);
		}
		else{
			System.out.format("%3d %16s %18s %3s",i+1,"-","-","-");
		}
		return;
	} 
	
	// print results for all graphs
	public void printAll () {
		System.out.println();
		System.out.printf("Detailed results for %s:", getName());
		System.out.format("\n-----------------------------------------------\n"+ "No.        Cost (km)     Comp time (ms)   Route   \n"+ "-----------------------------------------------\n");
		for(int k = 0; k < solnPath.size() ; k++) {
			printSingleResult(k,getSolnFound(k));
			System.out.println();
		}
	} 
	
	// print statistics
	public void printStats () {
		double[]trimmedsolnCost = TrimmedSolnCost();
		double[]trimmedcompTime = TrimmedCompTime();
		
		System.out.printf("Statistical summary for %s:",getName());
		System.out.format("\n---------------------------------------\n"+ "           Cost (km)     Comp time (ms)\n"+ "---------------------------------------\n");
		                     
		// print averages
		double cost_average = avgCost();
		double time_average = avgTime();
		
		// if the average cost is 0 then no graphs had a valid TSP solution
		if(cost_average != 0)System.out.format("%3s %12.2f %18.3f","Average",cost_average,time_average);
		else System.out.format("%3s %12s %18s","Average","NaN","NaN");
		
		//calculate and print st dev
		double cost_dev = stats.calcSTDDEV(trimmedsolnCost,trimmedsolnCost.length);
		double time_dev = stats.calcSTDDEV(trimmedcompTime, trimmedcompTime.length);
		
		// if the standard deviated cost is 0 then no graphs had a valid TSP solution
		if(cost_dev != 0)System.out.format("\n%3s %13.2f %18.3f","St Dev",cost_dev,time_dev);
		else System.out.format("\n%3s %13s %18s","St Dev","NaN","NaN");
		
		//calculate and print min
		double cost_min = stats.getMin(trimmedsolnCost);
		double time_min = stats.getMin(trimmedcompTime);
		
		// if the minimum cost is 0 then no graphs had a valid TSP solution
		if(cost_min != 0)System.out.format("\n%3s %16.2f %18.3f","Min",cost_min,time_min);
		else System.out.format("\n%3s %16s %18s","Min","NaN","NaN");
		
		//calculate and print min
		double cost_max = stats.getMax(trimmedsolnCost);
		double time_max = stats.getMax(trimmedcompTime);
		
		// if the maximum cost is 0 then no graphs had a valid TSP solution
		if(cost_max != 0)System.out.format("\n%3s %16.2f %18.3f","Max",cost_max,time_max);
		else System.out.format("\n%3s %16s %18s","Max","NaN","NaN");
		
		System.out.format("\n\nSuccess rate: %.1f%%",successRate());
		System.out.println();
		} 
}
