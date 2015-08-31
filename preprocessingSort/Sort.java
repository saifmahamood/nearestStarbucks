package preprocessingSort;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nearestStarbucks.Location;

public class Sort {
    private static final String COLUMN_SEPARATOR = ",";
    
    public static void main(String[] args) throws Exception
    {
        InputStream inputStream = new FileInputStream("/home/saif/Documents/development/interviewTests/insikt/nearestStarbucks/Starbucks.csv");
        List<List<String>> lines = readCsv(inputStream);
        System.out.println("hello?");
        
        List<Location> locs = getLocationList(lines);
        Collections.sort(locs, new Location());
        Collections.sort(locs, new Comparator<Location>() {
            public int compare(Location l1, Location l2) {
                
                double dlon = l2.getLon() - l1.getLon();
                double dlat = l2.getLat() - l1.getLat();
                //System.out.println(dlon);
                //System.out.println(dlat);
                /*
                if((Math.abs(dlon) > 0.1) ||
                  ((Math.abs(dlon) < 0.1) && (Math.abs(dlat) < 0.1))){
                    return Double.compare(dlon,0.0);
                    //return dlon > 0.0 ? 1 : -1;
                }
                
                else if((Math.abs(dlon) < 0.1) && (Math.abs(dlat) > 0.1))
                    return Double.compare(dlat,0.0);
                    //return dlat > 0.0 ? 1:-1;
            
                else return 0; 
                */
                double dist = Location.calcDist(l2, l1);
                return Double.compare(dist, 0.0);
            }

        });


        // Create a comparator that sorts primarily by column 0,
        // and if these values are equal, by column 1
        //Comparator<List<String>> comparator = createComparator(0, 1);
        //Collections.sort(lines, comparator);

        OutputStream outputStream = new FileOutputStream("/home/saif/Documents/development/interviewTests/insikt/output.csv");
        String header = "Longitude, Latitude, Address";
        writeCsv(header, lines, outputStream,locs);        
    }
    
    private static List<Location> getLocationList(List<List<String>> lines) {
        List<Location> locs = new ArrayList<Location>();
        for (List<String> list : lines)
        {
            double lon = Double.parseDouble(list.get(0));
            double lat = Double.parseDouble(list.get(1));
            String add = "";
            for(int i = 2;i < list.size(); i++)
                add = add.concat(list.get(i));
            Location loc = new Location(lon,lat,add);
            locs.add(loc);
        }
        return locs;
    }
    
    private static List<List<String>> readCsv(
            InputStream inputStream) throws IOException
        {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream));
            List<List<String>> lines = new ArrayList<List<String>>();

            // Skip header
            String line = reader.readLine();

            while (true)
            {
                line = reader.readLine();
                if (line == null)
                {
                    break;
                }
                List<String> list = Arrays.asList(line.split(COLUMN_SEPARATOR));
                lines.add(list);
            }
            return lines;
        }
    
    private static void writeCsv(
            String header, List<List<String>> lines, OutputStream outputStream, List<Location> locs) 
            throws IOException
        {
            Writer writer = new OutputStreamWriter(outputStream);
            writer.write(header+"\n");
            
            for(Location loc: locs) {
                writer.write(String.valueOf(loc.getLon()));
                writer.write(COLUMN_SEPARATOR);
                writer.write(String.valueOf(loc.getLat()));
                writer.write(COLUMN_SEPARATOR);
                writer.write(loc.getAddress());
                writer.write("\n");
                
            }
            /*
            for (List<String> list : lines)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    writer.write(list.get(i));
                    if (i < list.size() - 1)
                    {
                        writer.write(COLUMN_SEPARATOR);
                    }
                }
                writer.write("\n");
            }
            */
            writer.close();

        }
        private static <T extends Comparable<? super T>> Comparator<List<T>> 
        createComparator(int... indices)
    {
        return createComparator(Sort.<T>naturalOrder(), indices);
    }
    
    private static <T extends Comparable<? super T>> Comparator<T>
        naturalOrder()
    {
        return new Comparator<T>()
        {
            @Override
            public int compare(T t0, T t1)
            {
                return t0.compareTo(t1);
            }
        };
    }
    
    private static <T> Comparator<List<T>> createComparator(
        final Comparator<? super T> delegate, final int... indices)
    {
        return new Comparator<List<T>>()
        {
            @Override
            public int compare(List<T> list0, List<T> list1)
            {
                
                for (int i = 0; i < indices.length; i++)
                {
                    double element0 = Double.parseDouble((String) list0.get(indices[i]));
                    double element1 = Double.parseDouble((String) list1.get(indices[i]));
                    double c = element1 - element0;
                    //if(c < 0.1) return 0;
                    int n = c > 0.0? 1: -1;
                    //int n = delegate.compare(element0, element1);
                    if (n != 0)
                    {
                        return n;
                    }
                }
                return 0;
            }
        };
    }

    
}
