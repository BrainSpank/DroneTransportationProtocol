package drones;

import exceptions.OutOfBatteryException;
import helpers.ArrayPrinter;
import helpers.DroneData;
import world.World;

import java.util.*;

public abstract class Drone {

    // The format is:  DroneType, StartXCoord, StartYCoord, EndXCoord, EndYCoord

    public int startTime = World.time;
    public int endTime;
    public Integer[] currentPosition = new Integer[3];

    protected ArrayDeque<Integer[]> waypoints = new ArrayDeque<>();
    protected final Integer[] destinationPosition = new Integer[3];
    protected final int worldDiameter;
    protected final int worldHeight;

    // Battery is shown in seconds that it can power a drone before running out.
    // A Solo battery last for 25 minutes = 1500 seconds
    // After each 'tick' the battery will reduce by 0.5, the amount of seconds in a 'tick'
    private double battery = 1500.0;


    public Drone(DroneData droneData, int inWorldDiameter, int inWorldHeight) {
        currentPosition[0] = droneData.startX;
        currentPosition[1] = droneData.startY;
        currentPosition[2] = 0;

        destinationPosition[0] = droneData.endX;
        destinationPosition[1] = droneData.endY;
        destinationPosition[2] = 0;

        worldDiameter = inWorldDiameter;
        worldHeight = inWorldHeight;

        waypoints.addFirst(currentPosition);
    }


    // Returns the battery life of the Drone as a percentage.
    public double getBatteryLife(){
        return 100*(battery/1500.0);
    }

    public abstract void avoid();


    // Moves the drone by one movement.  Returns true is still travelling, false if journey is completed.
    public Boolean move() throws OutOfBatteryException{

        // Check if waypoint has been reached
        if(Arrays.equals(currentPosition, waypoints.peekFirst())){
            waypoints.removeFirst();

            // If journey has been completed return false
            if(Arrays.equals(currentPosition, destinationPosition)){
                endTime = World.time;
                return false;
            }
        }

        Integer xDistanceToWaypoint = Math.abs(currentPosition[0] - waypoints.peekFirst()[0]);
        Integer yDistanceToWaypoint = Math.abs(currentPosition[1] - waypoints.peekFirst()[1]);
        Integer zDistanceToWaypoint = Math.abs(currentPosition[2] - waypoints.peekFirst()[2]);
        Integer maxDistance = Math.max(xDistanceToWaypoint, Math.max(yDistanceToWaypoint, zDistanceToWaypoint));

        ArrayList<Integer> updateElements = new ArrayList<>();

        // Add the elements that need to be updated (0 for x axis, 1 for y and 2 for z)
        if(xDistanceToWaypoint.equals(maxDistance)) updateElements.add(0);
        if(yDistanceToWaypoint.equals(maxDistance)) updateElements.add(1);
        if(zDistanceToWaypoint.equals(maxDistance)) updateElements.add(2);

        for(int i : updateElements){
            if(currentPosition[i] < waypoints.peekFirst()[i]){
                currentPosition[i]++;

                // If drone is moving vertically upwards, there is an added battery cost.
                if(i==2){
                    battery -= 0.5;
                }
            }
            else{
                currentPosition[i]--;
            }
        }

        // General battery cost of any movement
        battery -= 0.5;

        if(battery <= 0){
            throw new OutOfBatteryException();
        }

        // return true if drone is still on journey.
        return true;
    }

    public void printStats(){
        if(false){
        System.out.println("Drone Status: " + (Arrays.equals(currentPosition, destinationPosition) ? "Journey Complete" : "CRASHED"));
        System.out.println("Current Position: " + ArrayPrinter.print(currentPosition));
        System.out.println("Destination Position: " + ArrayPrinter.print(destinationPosition));
        int i = 1;
        System.out.println("Remaining Waypoints: ");
        for(Integer[] waypoint : waypoints){
            System.out.print("Waypoint " + i + ": ");
            System.out.println(ArrayPrinter.print(waypoint));
            i++;
        }
        System.out.println("Start time: " + startTime);
        System.out.println("End time: " + endTime);
        System.out.println("Remaining Battery Life: " + getBatteryLife() + "%");
        System.out.println();
        }
    }


    // Adds a waypoint that is vertically up from current location by upByMetres
    protected void waypointUp(int upByMetres) {
        Integer[] newWaypoint = waypoints.peekLast().clone();
        newWaypoint[2] += upByMetres;
        if (newWaypoint[2] > worldHeight){
            System.out.println("Waypoints cannot be set outside of the world boundaries");
            newWaypoint[2] = worldHeight;
        }
        waypoints.addLast(newWaypoint);
    }


    // Adds a waypoint that is vertically down from current location by upByMetres
    protected void waypointDown(int downByMetres) {
        Integer[] newWaypoint = waypoints.peekLast().clone();
        newWaypoint[2] -= downByMetres;
        if (newWaypoint[2] < 0) {
            System.out.println("Waypoints cannot be set outside of the world boundaries");
            newWaypoint[2] = 0;
        }
        waypoints.addLast(newWaypoint);
    }


    // Adds a waypoint that is at the x and y arguments but on the same altitude/z axis.
    protected void waypointGoTo(int x, int y){
        Integer[] newWaypoint = waypoints.peekLast().clone();
        newWaypoint[0] = x;
        newWaypoint[1] = y;
        if(newWaypoint[0] > this.worldDiameter){
            newWaypoint[0] = this.worldDiameter;
        }
        if(newWaypoint[1] > this.worldDiameter){
            newWaypoint[1] = this.worldDiameter;
        }
        waypoints.addLast(newWaypoint);
    }

    protected void waypointLand(){
        Integer[] newWaypoint = waypoints.peekLast().clone();
        newWaypoint[2] = 0;

        waypoints.addLast(newWaypoint);
    }


    protected void addWaypoint(Integer[] waypoint){
        waypoints.addLast(waypoint);
    }


    protected Boolean checkWaypointsAreValid(){
        for(Integer[] waypoint : waypoints){
            if(waypoint[0] > worldDiameter || waypoint[1] > worldDiameter || waypoint[2] > worldHeight){
                System.out.println("Invalid waypoint!");
                return false;
            }
        }
        return true;
    }
}