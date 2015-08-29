package nearestStarbucks;

import java.util.Comparator;

public class Location  implements Comparator<Location>{
	
	private double lat;
	private double lon;
	private String address;
	
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Location(double lon, double lat, String address) {
		this.lon = lon;
		this.lat = lat ;
		this.address  = address;
	}
	
	public Location() {}
	
	public double getLat() {
		return lat;
	}
	
	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}
	
	public static double calcDist(Location a, Location b) {
	    double earthRadius = 3958.75; // miles (or 6371.0 kilometers)
	    double dLat = Math.toRadians(b.getLat()-a.getLat());
	    double dLng = Math.toRadians(b.getLon()-a.getLon());
	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);
	    double dist = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	            * Math.cos(Math.toRadians(a.getLat())) * Math.cos(Math.toRadians(b.getLat()));
	    dist = 2 * Math.atan2(Math.sqrt(dist), Math.sqrt(1-dist));
	    dist = earthRadius * dist;
	    //System.out.println(dist);
	    return dist;
	}
	
	public int compare(Location a, Location b) {
		return (int) Location.calcDist(a, b); 
	}
	
}
