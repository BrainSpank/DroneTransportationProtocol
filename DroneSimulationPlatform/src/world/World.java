package world;

import drones.Drone;
import drones.DroneFactory;
import exceptions.DroneCrashException;
import exceptions.OutOfBatteryException;
import helpers.CityData;
import helpers.DroneData;
import helpers.Key;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class World {

    // Constants
    public final int cellSize = 5;
    public final double secondsPerTick = 0.5;

    public static int time = 0;
    public final int worldDiameterMetres;
    public final int worldDiameterCells;
    public final double[] coordinates00 = new double[2];
    private ArrayList<Drone> drones = new ArrayList<>();
    private ArrayList<Drone> completedJourneyDrones = new ArrayList<>();
    // 500 feet (the max flying height for drones) = 152 metres
    private final int worldHeightMetres = 152;
    private final int worldHeightCells;
    private int maxConcurrentDrones = 0;


    public World(CityData cd) {
        worldDiameterMetres = cd.cityDiameter;
        // cellSize = cd.cellSize; // Configurable Cell Size has been depricated.  It is now a constant value.
        worldDiameterCells = worldDiameterMetres /cellSize;
        worldHeightCells = worldHeightMetres/cellSize;
        coordinates00[0] = cd.xCoordinateOf00;
        coordinates00[1] = cd.yCoordinateOf00;
    }


    public Boolean tick() throws DroneCrashException, OutOfBatteryException{
        Boolean requireNextTick = moveAllDrones();

        Boolean requireAvoid = dronesSense();

        return requireNextTick;
    }


    public void addDrones(ArrayList<DroneData> droneDataList){
        DroneFactory factory = new DroneFactory();
        for(DroneData dd : droneDataList) addDrone(dd, factory);
        if(drones.size() > maxConcurrentDrones) maxConcurrentDrones = drones.size();
    }


    public void printWorldStats(){
        System.out.println("# of Completed journeys: " + completedJourneyDrones.size());
        System.out.println("Maximum Drone density: " + maxConcurrentDrones);
        int averageJourneyTime = 0;
        for(Drone drone : completedJourneyDrones){
            averageJourneyTime += drone.endTime - drone.startTime;
        }
        averageJourneyTime = averageJourneyTime/completedJourneyDrones.size();
        System.out.println("Average Journey Time: " + averageJourneyTime);
    }


    private void addDrone(DroneData dd, DroneFactory inFactory) {
        // instantiate drone (using factory)
        DroneFactory factory = inFactory;
        Drone drone = factory.getDrone(dd, worldDiameterCells, worldHeightCells, cellSize);

        // add drone to drones List
        drones.add(drone);
    }


    private Boolean dronesSense(){
        // TODO:  Add drone detecting method.  This will be used later to assess differnt avoidance techniques.
        /*
        for(Drone drone : drones){
            if(drone.detect()){
                drone.avoid();
            }
        }
        */
        return false;
    }


    // Moves every drone by one step.  Detects if a crash has occured.
    private Boolean moveAllDrones() throws DroneCrashException, OutOfBatteryException{
        Boolean ongoingJourneys = false;

        ArrayList<Drone> tempCompletedJourney = new ArrayList<>();

        for(Drone drone : drones){
            if(drone.move()) {
                ongoingJourneys = true;
            }
            else{
                drone.printStats();
                // remove drones that have completed their journey from the list
                tempCompletedJourney.add(drone);
            }

            checkForCrashes();
        }
        completedJourneyDrones.addAll(tempCompletedJourney);
        drones.removeAll(tempCompletedJourney);

        return ongoingJourneys;
    }


    private void checkForCrashes() throws DroneCrashException{
        HashSet<Drone> crashedDrones = crashOccured();
        if(crashedDrones != null){
            System.out.println("A crash has occured between 2 or more drones!");
            int i = 1;
            for(Drone d : crashedDrones){
                d.endTime = World.time;
                System.out.print("Drone " + i + ": ");
                d.printStats();
                drones.remove(d);
                i++;
            }

            throw new DroneCrashException();
        }
    }


    private HashSet<Drone> crashOccured(){
        HashSet<Drone> returnDrones = new HashSet<>();
        HashMap<Key, Drone> dronePositions = new HashMap<>();
        for(Drone drone : drones){
            Key key = new Key(drone.currentPosition);
            if(!dronePositions.containsKey(key)){
                dronePositions.put(key, drone);
            }
            else{
                returnDrones.add(drone);
                returnDrones.add(dronePositions.get(key));
            }
        }
        return returnDrones.isEmpty() ? null : returnDrones;
    }
}
