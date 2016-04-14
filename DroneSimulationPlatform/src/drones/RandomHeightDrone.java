package drones;

import com.company.Main;
import exceptions.InvalidWaypointException;
import helpers.DroneData;
import world.World;

import java.util.Random;

public class RandomHeightDrone extends Drone {

    private int cellSize;
    private Random rand = new Random();

    public RandomHeightDrone(DroneData droneData, int diameter, int height) throws InvalidWaypointException {
        super(droneData, diameter, height);

        // plan route
        planRoute();

        if(checkWaypointsAreValid() == false){
            Main.logger.log("Invalid waypoints.  Check that input start and finish destinations are within " +
                    "legal boundaries, or update route planning method.  ");
            throw new InvalidWaypointException("Caused by RandomHeightDrone");
        }
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
    }

    public void avoid(){
    }
}
