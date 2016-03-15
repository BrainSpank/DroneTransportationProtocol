package drones;

import exceptions.InvalidWaypointException;
import helpers.DroneData;

public class DroneFactory {
    public DroneFactory(){}

    public Drone getDrone(DroneData dd, int diameter, int height, int cellSize){
        try {
            switch (dd.droneType) {
                case BASICDRONE:
                    return new BasicDrone(dd, diameter, height);
                case RANDOMHEIGHTDRONE:
                    return new RandomHeightDrone(dd, diameter, height, cellSize);
                case HUBDRONE:
                    return new HubDrone(dd, diameter, height, cellSize);
                default:
                    return null;
            }
        } catch (InvalidWaypointException e){
            System.out.println(e.getMessage());
            e.printStackTrace();

            System.exit(13);
            return null;
        }
    }
}
