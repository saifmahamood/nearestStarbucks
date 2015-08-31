package nearestStarbucks;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class readCSV {
	
	  public static void main(String[] args) {

			//System.out.println("check");

			readCSV obj = new readCSV();
			Location addr = new Location
				(Float.valueOf(args[0]),Float.valueOf(args[1]), args[2]);
			
			double miles = Double.parseDouble(args[3]);
			String csvFile = args[4];
			Location a = new Location(-122.4756191,37.72785445,"f");
			//System.out.println(Location.calcDist(addr, a));
			obj.run(addr,miles,csvFile);
			//Location loc = new Location(0.0,0.0,"test");

		  }
	  
	  public int binarySearch(List<Location> locs, int start, int end, final double miles, final Location addr) {
		  int mid = (start + end)/2;
		  if(start >= end)
			  return -1;
		  else if(Location.calcDist(addr, locs.get(mid)) < miles){
			  //System.out.println(Location.calcDist(addr, locs.get(mid)));
			  return mid;
		  }
			  
		  else {
			  int left = binarySearch(locs, start,mid,miles,addr);
			  if(left != -1)
				  return left;
			  else if(end - start == 1)
				  return -1;
			  else return binarySearch(locs, mid,end,miles,addr);
		  }
	  }
	  
	  public int boundaryIndex(List<Location> locs,int direction, int start, int end, final double miles,final Location addr) {
		  int index;
		  if(direction == 1) {
			  
			  while(start < end) {
				  index = binarySearch(locs,start,end,miles,addr);
				  if(index != -1)
					  start = index+1;
				  else return start-1;
			  }
			  /*
			  if(index == start)
				  return index;
			  else {
				  if(Location.calcDist(locs.get(index - 1), addr) > miles)
					  return index;
				  else return boundaryIndex(locs.subList(start, index), 0, start, index, miles,addr);
			  }
			  */
		  }
		  else{
			  while(start < end) {
				  index = binarySearch(locs,start,end,miles,addr);
				  if(index != -1)
					  end = index;
				  else return end;
			  }
			  /*
			  if(index == end)
				  return index;
			  else{
				  if(Location.calcDist(locs.get(index + 1), addr) > miles)
					  return index;
				  else return boundaryIndex(locs.subList(index, end), 0, index, end, miles,addr);
			  }
			  */
		  }
		  return -1;
	  }
	  
	  public List<Location> nearestLocs(List<Location> locs, final double miles, final Location addr) {
		  
		  int index = binarySearch(locs, 0,locs.size()-1, miles,addr);
		  //System.out.println(index);
		  //System.out.println(locs.get(index).getAddress());

		  int leftBoundaryIndex = boundaryIndex(locs, 0, 0, index, miles, addr);
		  int rightBoundaryIndex = boundaryIndex(locs,1,index, locs.size()-1 , miles, addr);
		  
		  //System.out.println(locs.get(leftBoundaryIndex).getAddress());
		  //System.out.println(locs.get(rightBoundaryIndex).getAddress());
		  //System.out.println(rightBoundaryIndex);
		  List<Location> nearestlocs = new ArrayList<Location>();
		  
		  nearestlocs.add(locs.get(leftBoundaryIndex)); 
		  nearestlocs.add(locs.get(rightBoundaryIndex));
		  
		  while(leftBoundaryIndex < rightBoundaryIndex){
			  index = binarySearch(locs,leftBoundaryIndex + 1, rightBoundaryIndex, miles,addr);
			  if(index == -1)
				  break;
			  else{
				  nearestlocs.add(locs.get(index));
				  leftBoundaryIndex = index;
			  }
		  }
		  		  
		  return nearestlocs;
		  
	  }
	  

	  public List<Location> run(final Location addr, double miles, String csvFile) {

		List<Location> nearestStarbucks = new ArrayList<Location>();
		//String csvFile = "/Users/mkyong/Downloads/GeoIPCountryWhois.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		List<Location> nearestlocs = new ArrayList<Location>();
		try {

			br = new BufferedReader(new FileReader(csvFile));
			br.readLine();
			while ((line = br.readLine()) != null) {

			        // use comma as separator
				//String[] address = line.split("\"");
				String[] coords = line.split(cvsSplitBy);
				//System.out.println(address[0]);
				String address = "";
				int i = 0;
				for (String str: coords) {
					if(i > 1){
						address = address.concat(str.concat(" ,"));
						//System.out.println(address);
					}
						
					i++;
				}
				i = 0;
				//System.out.println(address);

				Location loc = new Location
				 (Float.valueOf(coords[0]),
						 Float.valueOf(coords[1]),address);
				
				nearestStarbucks.add(loc);
				
				//double dist = Location.calcDist(addr, loc);
				//if(dist < miles){
				//	nearestStarbucks.add(loc);
				//}
				
				
				//System.out.println("Country [code= " + country[4] 
	              //                   + " , name=" + country[5] + "]");

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		nearestlocs = nearestLocs(nearestStarbucks,miles,addr);
		
		
		//System.out.println("Done");
		Collections.sort(nearestlocs, new Location());
	    Collections.sort(nearestlocs, new Comparator<Location>() {
	        public int compare(Location l1, Location l2) {
	        	double dis = (Location.calcDist(addr, l2) - Location.calcDist(addr, l1)); 
	        	if (dis == 0.0)
	        		return 0;
	        	else return dis > 0.0 ? 1 : -1;
	        	//System.out.println(ret);
	        	//return ret;
	            		//p1.points- p2.points;
	        }

	    });

		for(Location loc: nearestlocs) {
			System.out.println(loc.getAddress());
		}
		return nearestStarbucks;
	  }

}
