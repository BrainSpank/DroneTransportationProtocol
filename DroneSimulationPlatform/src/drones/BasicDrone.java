package drones;

import com.company.Main;
import exceptions.InvalidWaypointException;
import helpers.DroneData;

public class BasicDrone extends Drone {

    public BasicDrone(DroneData droneData, int diameter, int height) throws InvalidWaypointException {
        super(droneData, diameter, height);

        // plan route
        planRoute();

        if(checkWaypointsAreValid() == false){
            Main.logger.log("Invalid waypoints.  Check that input start and finish destinations are within " +
                    "legal boundaries, or update route planning method.  ");
            throw new InvalidWaypointException("Caused by HubDrone");
        }
    }


    public void avoid(){
    }


    private void planRoute(){
        waypointUp(worldHeight/2);
        waypointGoTo(destinationPosition[0], destinationPosition[1]);
        waypointLand();
    }
}
