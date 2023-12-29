import java.io.IOException;
import java.io.*;
import java.lang.Math;

public class Node {
	private String name ; // node name
	private double lat ; // latitude coordinate
	private double lon ; // longitude coordinate
	private int number;

// constructors
	public Node () {
		this.name = "";
		this.lat = 0;
		this.lon = 0;
		this.number = 1;
	} 
	public Node ( String name , double lat , double lon ) {
		this.name = name;
		this.lat = lat;
		this.lon = lon;
	}

// setters
	public void setName ( String name ) {
		this.name = name; 
	}
	public void setLat ( double lat ) {
		this.lat = lat; 
	}
	public void setLon ( double lon ) {
		this.lon = lon;
	}

// getters
	public String getName () {
		return this.name;
	}
	
	public double getLat () {
		return this.lat;
	}
	public double getLon () {
		return this.lon;
	}
	public int getNumber () {
		return this.number;
	}
	
// get user info and edit node
	public void userEdit () throws IOException {
		String newname = Pro5_solank58.getuserStringinput("   Name: ");
		setName(newname);
		   
		double newLat = Pro5_solank58.getDouble("   latitude: ", -90.0, 90.0);
		setLat(newLat);
		
		double newLon = Pro5_solank58.getDouble("   longitude: ", -180.0, 180.0);
		setLon(newLon);
		
		return;
	} 
	
// print node info as a table row
	public void print () {
		String LatLon_toString = "(" + getLat() + "," + getLon()+ ")";
		System.out.format("%19s %18s",getName(),LatLon_toString);
	}
	
	public static double distance ( Node i , Node j ) {
		// calc distance btwn two nodes
		
		// initializing variables
		double xi = Math.toRadians(i.getLat());
		double xj = Math.toRadians(j.getLat());
		double delta_x = xj - xi;
		
		double yi = Math.toRadians(i.getLon());
		double yj = Math.toRadians(j.getLon());
		double delta_y = yj - yi; 
		
		int R = 6371;
		
		// some calculations
		double a = (Math.sin(delta_x / 2) * Math.sin(delta_x / 2)) + (Math.cos(xi) *
				Math.cos(xj) * Math.sin(delta_y/2)* Math.sin(delta_y/2));
		
		double b = 2* Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		
		return (R*b);
	} 
}
