import java.io.*;
import java.util.ArrayList;

public class Pro5_solank58 {
	public static BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
	static TSPSolver Solver;
	static NISolver NI;
	static NNFLSolver FL;
	static NNSolver NN;
	
	public static void main (String[] args) throws IOException {
		ArrayList < Graph > graph = new ArrayList<Graph>(); // arraylist of graph objects
		RunMethod(graph,Solver,NN,FL,NI);
		scanner.close();
	}
	
	public static void displayMenu() {
		System.out.println("   JAVA TRAVELING SALESMAN PROBLEM V3");
		System.out.println("L - Load graphs from file");
		System.out.println("I - Display graph info");
		System.out.println("C - Clear all graphs");
		System.out.println("R - Run all algorithms");
		System.out.println("D - Display algorithm performance");
		System.out.println("X - Compare average algorithm performance");
		System.out.println("Q - Quit");
	}
	
	// checks if user input is valid, and then runs desired menu function
	public static void RunMethod(ArrayList<Graph> G,TSPSolver Solver, NNSolver NN , NNFLSolver FL, NISolver NI) throws IOException {
		displayMenu();
		String input = getuserStringinput("\nEnter choice: ");
			
		if (input.equalsIgnoreCase("L")) {
			System.out.println();
			loadFile(G);
			// initialized Solvers to size G
			NN = new NNSolver(G);
			FL = new NNFLSolver(G);
			NI = new NISolver(G);
		}
		else if (input.equalsIgnoreCase("I")) {
			displayGraphs(G);
			System.out.println();
		}
		else if (input.equalsIgnoreCase("C")) {
			clearGraphs(G,NN,FL,NI);
			System.out.println();
		}
		else if (input.equalsIgnoreCase("R")) {
			runAll(G,NN,FL,NI);
			System.out.println();
		}
		else if (input.equalsIgnoreCase("D")) {
			printAll(NN,FL,NI);
			
		}
		else if (input.equalsIgnoreCase("X")) {
			compare(NN,FL,NI);
			System.out.println();
			System.out.println();
		}
		else if (input.equalsIgnoreCase("Q")) {
			Quit();
		}
		else {
			System.out.format("\nERROR: Invalid menu choice!");
			System.out.println("\n");
		}
		RunMethod(G,Solver,NN, FL,NI);
	}
		
	// checks if user entered a valid integer input
	public static int getInteger(String prompt, int LB, int UB) {
			int userInput = 0;
			do {
				System.out.print(prompt);
				try {
					userInput = Integer.parseInt(scanner.readLine());
				}
				catch (NumberFormatException e) {
					System.out.format("ERROR: Input must be an integer in [%d, %d]!\n\n", LB, UB);
					continue;
				}
				catch (IOException e) {
					System.out.format("ERROR: Input must be an integer in [%d, %d]!\n\n", LB, UB);
					continue;
				}
				if (userInput < LB || userInput > UB) {
					System.out.format("ERROR: Input must be an integer in [%d, %d]!\n\n", LB, UB);
					continue;
				}
				break;
			} while (true);
			return userInput;
		}
		
	// checks if user entered a valid double input
	public static double getDouble(String prompt, double LB, double UB) {
		double userInput = 0;		
		do {
			System.out.print(prompt);
			try {
				userInput = Double.parseDouble(scanner.readLine());
			}
			catch (NumberFormatException e) {
				System.out.format("ERROR: Input must be a real number in [%.2f, %.2f]!\n\n", LB, UB);
				continue;
			}
			catch (IOException e) {
				System.out.format("ERROR: Input must be a real number in [%.2f, %.2f]!\n\n", LB, UB);
				continue;
			}
			if (userInput < LB || userInput > UB) {
				System.out.format("ERROR: Input must be a real number in [%.2f, %.2f]!\n\n", LB, UB);
				continue;
			}
			break;
		} while (true);
		return userInput;
	}

	// gets users input 
	public static String getuserStringinput(String prompt) throws IOException {
			System.out.format(prompt);
			String userInput = scanner.readLine();	
			return userInput;
		}
	
	
	// checks if TSP object was "initialized" (graphs were loaded into the program) 
	public static boolean checkTSP_init(ArrayList<Graph> G) {
		if(G.isEmpty()) {
			System.out.print("\nERROR: No graphs have been loaded!\n");
			return false;
		}
		else return true;
	}
	
	//Read in graphs from a user-specified file
	public static  boolean loadFile(ArrayList<Graph> G) throws IOException{
		String filename = getuserStringinput("Enter file name (0 to cancel): ");
		
		// checks if user entered 0 to exit out of program
		if(filename.equals("0")) {
			System.out.println("\nFile loading process canceled.");
			System.out.println();
			return false;
		}
		
		// checks if file exists or not 
		File file = new File(filename);
		if (file.exists() == false) {
			System.out.print("\nERROR: File not found!\n\n");
		//System.out.print(System.getProperty("user.dir"));
			return false; 	
		}
				
		// reads each graph
		BufferedReader reader = new BufferedReader (new FileReader (filename)) ;
		
		// declared variables
		String readline, cityinfo, citysplit[], arcinfo, arcsplit[];
		double lat, lon;
		int numofGraphs=0,numofvalidgraphs = 0,arcsplit_int;
		ArrayList <Boolean> valid = new ArrayList<Boolean>(); 
		
		// translates file input into graphs
		while ((readline = reader.readLine()) != null) {
			if(!(readline.isEmpty())){	
					int numOfCities = Integer.parseInt(readline);
					
					// initialize graph
					Graph graph = new Graph(numOfCities);
					numofGraphs++;
					graph.init(numOfCities);
					
					// add nodes to graph
					for (int i = 1; i <= numOfCities; i++ ) {
					   Node newNode = new Node();
					   
					   cityinfo = reader.readLine();
					   citysplit = cityinfo.split(",");
					   
					   lat = Double.parseDouble(citysplit[1]);
					   lon = Double.parseDouble(citysplit[2]);
					   
					   newNode.setName(citysplit[0]);
					   newNode.setLat(lat);
					   newNode.setLon(lon);
					   
					   // creates an array which records if any nodes have "errors"
					   valid.add(graph.addNode(newNode));
				   }
				   
				   // adds arcs to graph
				   for(int i = 1; i <= numOfCities - 1; i++) {
					   arcinfo = reader.readLine();
					   arcsplit = arcinfo.split(",");
					   
					   for (int j = 0; j < arcsplit.length; j++) {
						   arcsplit_int = Integer.parseInt(arcsplit[j]);
						   
						// if arc is repeated, don't add to total arc count
						   
						   if((graph.existsArc(arcsplit_int-1, i-1)==false))graph.setM(graph.getM()+1);
						   
						   graph.addArc(i-1, arcsplit_int-1);
						   
						// enters cost in C matrix once an arc is added
						   graph.setCost(i-1, arcsplit_int-1,Node.distance(graph.getNode(i-1),graph.getNode(arcsplit_int -1)));
				        }
				   }
				   
				   // all graphs without errors will be added to arraylist of graphs
				   for(boolean x: valid) {
					   if(x == false) {
						   graph.setValid(x);
					   }
				   }
				   if(graph.getValid() == true) {
					   G.add(graph);
					   numofvalidgraphs++;
				   }  
			}
			else if(readline.isEmpty()== true) { // reads new graph
				continue;	
			}
		}
		reader.close();
		System.out.format("\n%d of %d graphs loaded!\n\n",numofvalidgraphs,numofGraphs);
			return true;
		}
	
	// displays information regarding each graph read in from file
	public static void displayGraphs(ArrayList<Graph> G) {
		// checks if graphs were loaded before using program
		if(!(checkTSP_init(G))) return;
		
		System.out.println();
		
		//prints initial table listing graph information
		System.out.format("GRAPH SUMMARY\n"+ "No.    # nodes    # arcs\n"+ "------------------------");
		for (int i = 1; i<= G.size(); i++) {
			System.out.format("\n%3d %10d %9d",i,G.get(i-1).getN(),G.get(i-1).getM());
		}
		
		//displays detailed information on each graph
		System.out.println("\n");
		int userinput = getInteger("Enter graph to see details (0 to quit): ",0,G.size());
		
		if(userinput == 0) {
			return;
			}
		else{
			System.out.println("");
			G.get(userinput-1).print();
		}
		displayGraphs(G);
	}
	
	// clears all graph information
	public static void clearGraphs(ArrayList<Graph> G,NNSolver NN, NNFLSolver FL, NISolver NI) {
		if(!(checkTSP_init(G))) return;
		G.clear();
		resetAll(NN,FL,NI);
		System.out.print("\nAll graphs cleared.\n");
		return;
	}
	
	// resets all solvers
	public static void resetAll(NNSolver NN, NNFLSolver FL, NISolver NI) {
		NN.reset();
		FL.reset();
		NI.reset();
	}

	// runs algorithms 
	public static void runAll(ArrayList<Graph> G, NNSolver NN, NNFLSolver FL, NISolver NI) {
		if(!(checkTSP_init(G))) return;// checks if graphs were loaded before using program
		
		//removes previously loaded graphs
		resetAll(NN,FL,NI);
		
		System.out.println();
		
		//run each algorithm
		runAlgorithm(G,NN);
		System.out.println("Nearest neighbor algorithm done.");
		
		System.out.println();
		
		runAlgorithm(G,FL);
		System.out.println("Nearest neighbor first-last algorithm done.");
		
		System.out.println();
		
		runAlgorithm(G,NI);
		System.out.println("Node insertion algorithm done.");
		
		return;
	}
	
	// runs each algorithm
	public static void runAlgorithm(ArrayList<Graph> G, TSPSolver solver) {
		for(int i  = 0; i < G.size(); i++ ) {
			solver.run(G, i, false);	
			if(!(solver.getSolnFound(i))) {
				System.out.printf("ERROR: %s did not find a TSP route for Graph %d!",solver.getName_abbrev(),i+1);
				System.out.print("\n");
			}
		}
		return;
	}
	
	// prints all info for each algorithm
	public static void printAll(NNSolver NN, NNFLSolver FL, NISolver NI) {
		// checks if runAlgorithm function has been run
		if((NN == null ) || ((!(NN.hasResults()) ) && (!(FL.hasResults()) )&& (!(NI.hasResults()) ) )) { 
			System.out.print("\nERROR: Results do not exist for all algorithms!\n\n"); 
			return;
		}
		printAlgorithm(NN);
		printAlgorithm(FL);
		printAlgorithm(NI);
		return;
	}
	
	// prints one algorithms information
	public static void printAlgorithm(TSPSolver solver) {
		solver.printAll();
		System.out.println("");
		solver.printStats();
		System.out.println("");
		return;	
	}
	
	public static void compare(NNSolver NN, NNFLSolver FL, NISolver NI) {
		// checks if runAlgorithm function has been run
		if((NN == null ) || ((!(NN.hasResults()) ) && (!(FL.hasResults()) )&& (!(NI.hasResults()) ) )) { 
			System.out.print("\nERROR: Results do not exist for all algorithms!"); 
			return;
		}
		System.out.println();		// prints comparision title
		System.out.println("------------------------------------------------------------\n"
				+ "           Cost (km)     Comp time (ms)     Success rate (%)\n"
				+ "------------------------------------------------------------");
		
		// prints each algorithm's comparisional statistics 
		System.out.format("%s%18.2f%19.3f%21.1f","NN",NN.avgCost(),NN.avgTime(),NN.successRate());
		System.out.format("\n%s%15.2f%19.3f%21.1f","NN-FL",FL.avgCost(),FL.avgTime(),FL.successRate());
		System.out.format("\n%s%18.2f%19.3f%21.1f","NI",NI.avgCost(),NI.avgTime(),NI.successRate());
		
		//calculates winner of each category (set to NN initially)
		String costwinner = "NN"; 
		String timewinner = "NN";
		String sucessrate_winner = "NN";
		
		// cost
		double NN_cost = compareCost(NN);
		double FL_cost = compareCost(FL);
		double NI_cost = compareCost(NI);
		
		if(FL_cost < NN_cost && FL_cost < NI_cost)costwinner = "NN-FL";
		else if(NI_cost < NN_cost && NI_cost < FL_cost)costwinner = "NI";
		
		//time
		double NN_time = compareTime(NN);
		double FL_time = compareTime(FL);
		double NI_time = compareTime(NI);
		
		if(FL_time < NN_time && FL_time < NI_time)timewinner = "NN-FL";
		else if(NI_time < NN_time && NI_time < FL_time)timewinner = "NI";
		
		//success rate
		if(FL.successRate() > NN.successRate() && FL.successRate() > NI.successRate())sucessrate_winner = "NN-FL";
		else if(NI.successRate() > NN.successRate() && NI.successRate() > FL.successRate())sucessrate_winner = "NI";
		
		//prints winner
		System.out.println("\n------------------------------------------------------------");
		System.out.format("Winner%14s%19s%21s",costwinner,timewinner,sucessrate_winner);
		System.out.println("\n------------------------------------------------------------");
		
		//prints overall winner
		if (costwinner.equalsIgnoreCase(sucessrate_winner) && costwinner.equalsIgnoreCase(timewinner)) {
			// if all winners are the same, print out winner
			System.out.format("Overall winner: %s",costwinner);
		}
		else System.out.format("Overall winner: Unclear");
		return;
	}
	
	// gets cost for each TSP Solver to compare
	public static double compareCost(TSPSolver solver) {
		double cost = solver.avgCost();
		if(cost == 0) {
			cost = Double.MAX_VALUE; // If algorithm could not solve any graphs, it should not be considered when comparing graphs hence the large double value for cost
		}
		return cost;
	}
	
	// gets time for each TSP Solver to compare
	public static double compareTime(TSPSolver solver) {
		double time = solver.avgTime();
		if(time == 0) {
			time = Double.MAX_VALUE; // If algorithm could not solve any graphs, it should not be considered when comparing graphs hence the large double value for cost
		}
		return time;
	}
	
   // quits entire program
   public static void Quit() {
	   System.out.print("\nCiao!\n\n");
	   System.exit(0); 
   }	
}
