package tests;

import exceptions.DroneCrashException;
import exceptions.OutOfBatteryException;
import helpers.CityData;
import helpers.CsvReader;
import helpers.DroneData;
import world.World;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public abstract class Test {

    protected ArrayList<DroneData> droneData;
    protected World world;

    protected String defaultWorld = "Default";

    public Test(){}


    // This class should set up requirements for a specific test then always call run(ArrayList<DroneData>)
    public abstract Boolean run();


    protected void run(ArrayList<DroneData> droneData) throws DroneCrashException{
        world.addDrones(droneData);

        try {
            // loop through world.tick() until all drones are in finish destination.
            while (world.tick()) {
                // TODO: Could gather stats here?

                World.time++;
            }
        } catch (OutOfBatteryException e){
            e.printStackTrace();
            System.exit(16);
        }

        // finish and print log
        world.printWorldStats();
    }


    protected ArrayList<DroneData> readInDrones(String droneFileName){
        String currentDir = System.getProperty("user.dir").toString();
        Path droneJourneysPath = Paths.get(currentDir, "info", droneFileName);
        ArrayList<DroneData> drones = CsvReader.getDroneData(droneJourneysPath);

        return drones;
    }


    protected void setupWorld(){
        String city = defaultWorld;

        String currentDir = System.getProperty("user.dir").toString();
        Path citiesDataPath = Paths.get(currentDir, "info", "citiesData.csv");

        // set up world
        CityData cd = CsvReader.getCityData(city, citiesDataPath);
        world = new World(cd);
    }
}


