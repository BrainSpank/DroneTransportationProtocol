package tests;

import drones.Drone;
import exceptions.DroneCrashException;
import exceptions.OutOfBatteryException;
import helpers.CityData;
import helpers.CsvReader;
import helpers.DroneData;
import helpers.Logger;
import world.World;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public abstract class Test {

    protected ArrayList<DroneData> droneData;
    protected World world;

    protected String defaultWorld = "Default";

    public Test(){
        setupWorld();
    }


    // This method should set up requirements for a specific test then always call run(ArrayList<DroneData>)
    public abstract Boolean run();


    protected void run(ArrayList<DroneData> droneData) throws DroneCrashException {
        if(droneData != null) {
            world.addDrones(droneData);
        }

        try {
            // loop through world.tick() until all drones are in finish destination.
            while (world.tick()) {
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
        ArrayList<DroneData> droneData = CsvReader.getDroneData(droneJourneysPath);

        return droneData;
    }


    protected void setupWorld(){
        String city = defaultWorld;

        String currentDir = System.getProperty("user.dir").toString();
        Path citiesDataPath = Paths.get(currentDir, "info", "citiesData.csv");

        // set up world
        CityData cd = CsvReader.getCityData(city, citiesDataPath);
        world = new World(cd);
    }

    protected void outputFlightDataInGraphableFormat(String fileName){

        StringBuilder x = new StringBuilder("x = [");
        StringBuilder y = new StringBuilder("y = [");
        StringBuilder z = new StringBuilder("z = [");
        /// get all completed drone journeys
        for(Drone drone : world.completedJourneyDrones){
            String prefix = "";
            for(Integer[] waypoint : drone.visitedWaypoints){
                x.append(prefix + waypoint[0].toString());
                y.append(prefix + waypoint[1].toString());
                z.append(prefix + waypoint[2].toString());
                prefix = ",";
            }
            x.append(";");
            y.append(";");
            z.append(";");
        }

        x.append("]';");
        y.append("]';");
        z.append("]';");

        Logger graphLogger = new Logger(fileName, false);

        graphLogger.log(x.toString());
        graphLogger.log(y.toString());
        graphLogger.log(z.toString());
        graphLogger.log("plot3(x,y,z)");

        graphLogger.close();
    }
}


