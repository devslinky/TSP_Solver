import java.io.*;
import java.util.ArrayList;


public class Graph {

   private int n ; // number of nodes
   private int m ; // number of arcs
   private ArrayList < Node > node ; // ArrayList *or* array of nodes
   private boolean [][] A ; // adjacency matrix
   private double [][] C; // cost matrix
   private boolean isvalid; // checks if graph does not have any errors
   
   
// constructors
   public Graph () {
	   this.n = 0;
	   this.m =0;
	   this.node = new ArrayList<Node>();
	   this.A = new boolean[0][0];
	   this.C = new double[0][0];
	   this.isvalid = true;
	   
   }
   public Graph ( int n ) {
	   this.n = n;
	   this.m = 0;
	   this.node = new ArrayList<Node>(n);
	   this.A = new boolean[n][n];
	   this.C = new double[n][n];
	   this.isvalid = true;
   }

// setters
   public void setN ( int n ) {
	   this.n = n;
   }
   public void setM ( int m ) {
	   this.m = m;
   }
   public void setArc ( int i , int j , boolean b ) {
	   this.A[i][j] = b;
	   this.A[j][i] = b;
   }
   public void setCost ( int i , int j , double c ) {
	   this.C[i][j]= c;
	   this.C[j][i] = c;
   }
   public void setValid(boolean z) {
	   isvalid = z;
   }

   // getters
   public int getN () {
	   return this.n;
   }
   public int getM () {
	   return this.m;
   }
   public boolean getArc ( int i , int j ) {
	   return this.A[i][j];
   }
   public double getCost ( int i , int j ) {
	   return this.C[i][j];
   }
   public Node getNode ( int i ) {
	   return node.get(i);
   }
   public boolean getValid (){
	   return this.isvalid;
   }

   public void init ( int n ) {
	   // initialize values and arrays
	   for (int i = 0; i < n; i++) {
		   for (int j = 0; j < n ; j++) {
			   this.A[i][j] = false;
			   this.C[i][j] = 0;
		   }
	   }
   }
   
   public void reset () {
	   // reset the graph
	   setN(0);
	   init(0); 
	   this.node = null;
   }
   
   public boolean existsArc ( int i , int j ) {
	   // check if arc exists
	   return(A[i][j]); 
   }
   
   public boolean existsNode ( Node t ) {
	   // creates a temporary node "temp" and sets it equal to nodes in arraylist
	   // then checks if temp and inputed note have same name or coordinates
	   for (Node temp : node) {
		   if((t.getName().equals(temp.getName())) == true) {
			   return false;
		   }
		   else if (t.getLat() == temp.getLat()  && (t.getLon() == temp.getLon())  ){
			   return false;
		   }
	   }
	   return true;
   }
   
   public boolean addArc ( int i , int j ) {
	   // add an arc    
	   A[i][j] = true;
	   A[j][i] = true;
	   return true;
   }
   
   public void removeArc ( int k ) {
	   // remove an arc
	   int searchsize = getN();
	   int i, i_set =0;
	   int counter = 1;
	   
	   for (int j = 0; j<searchsize;j++) {
		   i = i_set;
		   while(i < searchsize) {
			   if(A[i][j] == true) {
				   if(counter == k) {
					   A[i][j]= false;
					   A[j][i]= false;
				   }
				   counter++;
			   }
			   i++;
		   }
		   i_set++;                                                                                               
	   } 
   }
   
   public boolean addNode ( Node t ) throws IOException {
	// if node is not a novel node, or has incorrect coordinates, then it is "flagged" as false
	   if ((existsNode(t) == false) || ((t.getLat()< -90.0 || t.getLat() > 90.0)) || ((t.getLon()< -180.0 || t.getLon() > 180.0))) {
		   node.add(t);
		   return false;
	   }	
	   else { 
		   node.add(t);
		   return true;
	   } 
   }
   
   public void print () {
	   // print all graph info
	   System.out.format("Number of nodes: %d",getN());
	   System.out.format("\nNumber of arcs: %d\n\n",getM());
	   
	   printNodes();
	   System.out.println();
	   printArcs();
	   System.out.println("\n");
	   
	   return;
   }
   
   public void printNodes () {
	   // print node list
	   System.out.format("NODE LIST\n"+ "No.               Name        Coordinates\n"+ "-----------------------------------------");
	   
	   int counter = 1;
	   for(Node temp: node) {
		   String count = String.format("%d",counter);
		   System.out.format("\n%3s",count);
		   temp.print();  
		   counter++;
	   }
	   System.out.println();
   }
   
   public  void printArcs () {
	   // print arc list
	   System.out.format("ARC LIST\n");
	   System.out.format("No.    Cities       Distance\n"+ "----------------------------");
	   
	   // basically the size of each row that will be searched to print arcs (don't want to print repeated arcs)
	   int searchsize = getN();
	   int i = 0, i_set = 0,Number_count =1;
	   
	   for (int j = 0; j<searchsize;j++) {
		   i = i_set;
		   while(i < searchsize) {
			   if(A[i][j] == true) {
				   int i_city = i+1, j_city = j+1;
				   String city_arc = j_city +"-"+ i_city;
				   double distance = Node.distance(node.get(i), node.get(j));
				   System.out.format("\n%3d%10s%15.2f",Number_count,city_arc,distance);
				   Number_count++;
				   }
			   i++;
		   }
		   i_set++;                                                                                               
	   }
	  }
   
   public boolean checkPath ( int [] P ) {
	   // check feasibility of path P
	   
	   if(P[0] != P[getN()]) {
		   System.out.print("\nERROR: Start and end cities must be the same!\n");
		   return false;
	   }
	   else if (checkPathRepition(P) == true) {
		   System.out.format("\nERROR: Cities cannot be visited more than once!\n"+ "ERROR: Not all cities are visited!\n");
		   return false;
	   }
	   else {
		// checks if all arcs in path exist
		   int i_value, i_nextvalue;
		   for (int i = 0 ; i < getN(); i++) {
			   i_value = P[i];
			   i_nextvalue = P[i+1];
			   if(existsArc(i_value -1, i_nextvalue-1) == false){
				   System.out.format("\nERROR: Arc %d-%d does not exist!\n",i_value ,i_nextvalue);
				   return false;
			   }
		   }
	   }
	   // run pathcost 
	   System.out.format("\nThe total distance traveled is %.2f km.\n",pathCost(P));
	   return true;
   }
   
   // checks if path entered repeats a city
   public boolean checkPathRepition(int[]P) {
	   for(int i = 1; i <= getN(); i++) {
		   for(int j = 1; j <= getN(); j++) {
			   if(i == j) {
				   continue;
			   }
			   if(P[i] == P[j]) {
				   return true;
			   }
		   }
	   }
	   return false;
   }
   
   public double pathCost ( int [] P ) {
	   double cost = 0;
	   // calculate cost of path P
	   int i_cost, i_nextcost;
	   
	   for (int i = 0; i < getN(); i++) {
		   i_cost = P[i];
		   i_nextcost =P[i+1];
		   
		   cost += Node.distance(node.get(i_cost -1), node.get(i_nextcost - 1));
	   }
	   return cost;
   }
   
   // checks if arraylist "node" is empty
   public boolean check_if_empty(){
	   return (this.node == null);
   	}
}