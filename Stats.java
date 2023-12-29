import java.util.ArrayList;
import java.util.Arrays;

public class Stats {
	
	// creates a new "trimmed" array that only contains values of graphs that have an existing TSP solution
	public double[]trimedArray(double[]arr, int size, boolean[]resultsExist) {
		double[]trimmedArr;
		ArrayList<Double> TrimmedList = new ArrayList<>();
		int index=0;
		
		//iterates through results exist array and whenever there is a 'true' element, add the element into trimmed array
		for(boolean x : resultsExist) {
			if(x)TrimmedList.add(arr[index]);
			index++;
		}
		//converting the arraylist into an array 
		trimmedArr = TrimmedList.stream().mapToDouble(i -> i).toArray();
		return trimmedArr;
	}
	
	// calculates average of elements in an array 
	public double calcAverage(double[]arr, int size){
		double sum = 0; 
		for(double i : arr) {
			sum+= i;
		}
		return sum/size;
	}
	
	// calculate standard deviation of elements in an array
	public double calcSTDDEV(double[]arr, int size ) {
		
		//if there is 2 or more elements in the array, calculate the st Dev else return 0
		if(arr.length > 1) {
			double avg = calcAverage(arr,size);
			double std_dev =0;
			
			for(double i : arr) {
				std_dev+= Math.pow(i - avg, 2);
			}
			
			return Math.sqrt(std_dev/(size-1));
		}
		else return 0;
	}
	
	// calculate min of elements in an array
	public double getMin(double[]arr) {
		if(arr.length > 0 ) {
			Arrays.sort(arr);
			return arr[0];
		}
		else return 0;
		
	}
	
	// calculate max of elements in an array
	public double getMax(double[]arr) {
		if(arr.length > 0) {
			Arrays.sort(arr);
			return arr[arr.length-1];
		}
		else return 0;
		
	}
}
