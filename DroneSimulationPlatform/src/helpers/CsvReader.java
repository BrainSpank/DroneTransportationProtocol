package helpers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    public CsvReader(){}

    public static CityData getCityData(String city, Path p){
        ArrayList<String> allCityData = getLinesFromCsv(p);
        String[] data = null;
        for(String cityData : allCityData){
            data = cityData.split(",");
            if(data[0].equalsIgnoreCase(city)) {
                try {
                    CityData returnVal = new CityData(data[0], data[1], data[2], data[3], data[4]);
                    return returnVal;
                } catch (IndexOutOfBoundsException e){
                    System.out.println("The City Data is malformed.  Please check the citiesData.csv file.");
                    e.printStackTrace();
                    System.exit(11);
                }
            }
        }

        // No City Data found
        System.out.println("No City Data was found for the city: " + city);
        System.out.println("Add the data to the citiesData.csv file");
        return null;
    }

    public static ArrayList<DroneData> getDroneData(Path p){
        ArrayList<DroneData> returnData = new ArrayList<>();
        ArrayList<String> lines = getLinesFromCsv(p);
        for(String line : lines){
            try {
                String[] data = line.split(",");
                returnData.add(new DroneData(data[0], Integer.parseInt(data[1]), Integer.parseInt(data[2]),
                        Integer.parseInt(data[3]), Integer.parseInt(data[4])));
            } catch (IndexOutOfBoundsException e){
                System.out.println("The Drone Data is malformed.  Please check the file containing the drone data.");
                e.printStackTrace();
                System.exit(12);
            }
        }
        return returnData;
    }

    private static ArrayList<String> getLinesFromCsv(Path path){
        ArrayList<String> returnValue = new ArrayList<String>();
        try{
            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
            for(String line : lines){
                // Skip commented or empty lines
                if(line.isEmpty() || line.charAt(0) == '#') {
                    continue;
                }

                returnValue.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
