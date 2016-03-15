package drones;

import exceptions.InvalidWaypointException;
import helpers.DroneData;

import java.util.Random;

// HubDrone fly at a random height.  The main unique feature is that they start and finish at predefined Hubs.
public class HubDrone extends Drone{


    // TODO: Override move() method so that it allows for non crashing in hubs and non comleted journeys at destination
    // current method of checking if journey has completed does not work for HubDrones as their start position is
    // their final destination

    private Random rand = new Random();
    private int cellSize;

    public HubDrone(DroneData dd, int diameter, int height, int inCellSize) throws InvalidWaypointException {
        super(dd, diameter, height);
        cellSize = inCellSize;

        planRoute();

        if(checkWaypointsAreValid() == false){
            System.out.println("Invalid waypoints.  Check that input start and finish destinations are within " +
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
        height = height/cellSize;
        if(height == 0){
            System.out.println("CellSize is set too large.  Make it much smaller.");
            System.exit(5);
        }

        waypointUp(height);
        waypointGoTo(destinationPosition[0], destinationPosition[1]);
        waypointLand();

        waypointUp(height);
        waypointGoTo(currentPosition[0], currentPosition[1]);
        waypointLand();

    }
}
