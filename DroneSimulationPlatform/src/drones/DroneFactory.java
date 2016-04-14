package drones;

import com.company.Main;
import exceptions.InvalidWaypointException;
import helpers.DroneData;

public class DroneFactory {
    public DroneFactory(){}

    public Drone getDrone(DroneData dd, int diameter, int height){
        try {
            switch (dd.droneType) {
                case BASICDRONE:
                    return new BasicDrone(dd, diameter, height);
                case RANDOMHEIGHTDRONE:
                    return new RandomHeightDrone(dd, diameter, height);
                case HUBDRONE:
                    return new HubDrone(dd, diameter, height);
                default:
                    return null;
            }
        } catch (InvalidWaypointException e){
            Main.logger.log(e.getMessage());
            e.printStackTrace();

            System.exit(13);
            return null;
        }
    }
}
