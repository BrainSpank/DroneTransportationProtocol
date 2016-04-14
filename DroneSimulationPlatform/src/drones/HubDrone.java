package drones;

import com.company.Main;
import exceptions.InvalidWaypointException;
import helpers.DroneData;
import world.World;

import java.util.Random;

// HubDrone fly at a random height.  The main unique feature is that they start and finish at predefined Hubs.
public class HubDrone extends Drone{


    // TODO: Override move() method so that it allows for non crashing in hubs and non completed journeys at destination
    // current method of checking if journey has completed does not work for HubDrones as their start position is
    // their final destination

    private Random rand = new Random();

    public HubDrone(DroneData dd, int diameter, int height) throws InvalidWaypointException {
        super(dd, diameter, height);

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
        // Select random height to fly at between 200 feet (60 metres) and 400 feet (120 metres)
        // returns random number between 0 and 60
        int height = rand.nextInt(61);
        // makes height between range 60-120
        height += 60;
        // Convert height from metres into cells
        height = height/ World.cellSize;

        waypointUp(height);
        waypointGoTo(destinationPosition[0], destinationPosition[1]);
        waypointLand();

        waypointUp(height);
        waypointGoTo(currentPosition[0], currentPosition[1]);
        waypointLand();

        // Remove first waypoint to account for being released from Hub
        waypoints.removeFirst();
        // Update destination to be the finial position because the Drone must return to the original hub.
        destinationPosition = currentPosition;
    }
}
