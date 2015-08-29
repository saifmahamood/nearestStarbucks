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

		  public List<Location> run(final Location addr, double miles, String csvFile) {

			List<Location> nearestStarbucks = new ArrayList<Location>();
			//String csvFile = "/Users/mkyong/Downloads/GeoIPCountryWhois.csv";
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";

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
					
					double dist = Location.calcDist(addr, loc);
					if(dist < miles){
						nearestStarbucks.add(loc);
					}
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

			//System.out.println("Done");
			Collections.sort(nearestStarbucks, new Location());
		    Collections.sort(nearestStarbucks, new Comparator<Location>() {
		        public int compare(Location l1, Location l2) {
		        	double dis = (Location.calcDist(addr, l1) - Location.calcDist(addr, l2)); 
		        	if (dis == 0.0)
		        		return 0;
		        	else return dis > 0.0 ? 1 : -1;
		        	//System.out.println(ret);
		        	//return ret;
		            		//p1.points- p2.points;
		        }

		    });

			for(Location loc: nearestStarbucks) {
				System.out.println(loc.getAddress());
			}
			return nearestStarbucks;
		  }

}
