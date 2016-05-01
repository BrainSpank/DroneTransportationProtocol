package world;

import com.company.Main;
import drones.Drone;
import drones.DroneFactory;
import exceptions.DroneCrashException;
import exceptions.OutOfBatteryException;
import helpers.CityData;
import helpers.DroneData;
import helpers.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class World {

    // Constants
    public final static int cellSize = 5;
    public final double secondsPerTick = 0.5;

    public static int time = 0;
    public final int worldDiameterMetres;
    public final int worldDiameterCells;
    public final double[] coordinates00 = new double[2];
    // 500 feet (the max flying height for drones) = 152 metres
    public final int worldHeightMetres = 152;
    public final int worldHeightCells = worldHeightMetres/cellSize;

    private ArrayList<Drone> drones = new ArrayList<>();
    private ArrayList<Drone> completedJourneyDrones = new ArrayList<>();
    private int maxConcurrentDrones = 0;
    private ArrayList<Hub> hubs = new ArrayList<>();
    private final int delayBetweenDroneReleases = 2;


    public World(CityData cd) {
        worldDiameterMetres = cd.cityDiameter;
        // cellSize = cd.cellSize; // Configurable Cell Size has been depricated.  It is now a constant value.
        worldDiameterCells = worldDiameterMetres /cellSize;
        coordinates00[0] = cd.xCoordinateOf00;
        coordinates00[1] = cd.yCoordinateOf00;
    }


    public Boolean tick() throws DroneCrashException, OutOfBatteryException{
        Boolean requireNextTick = moveAllDrones();

        Boolean requireNextTick2 = true;

        if(time%delayBetweenDroneReleases == 0) {
            requireNextTick2 = hubsReleaseDrones();
        }

        ArrayList<Drone> requireAvoid = dronesSense();

        return requireNextTick || requireNextTick2;
    }


    public void addDrones(ArrayList<DroneData> droneDataList){
        DroneFactory factory = new DroneFactory();
        for(DroneData dd : droneDataList) addDrone(dd, factory);
        if(drones.size() > maxConcurrentDrones) maxConcurrentDrones = drones.size();
    }


    public void addHubs(ArrayList<Hub> inHubs){
        hubs.addAll(inHubs);
    }


    public void printWorldStats(){
        Main.logger.log("# of Completed journeys: " + completedJourneyDrones.size());
        Main.logger.log("Maximum Drone density: " + maxConcurrentDrones);
        int averageJourneyTime = 0;
        for(Drone drone : completedJourneyDrones){
            averageJourneyTime += drone.endTime - drone.startTime;
        }
        averageJourneyTime = averageJourneyTime/completedJourneyDrones.size();
        Main.logger.log("Average Journey Time: " + averageJourneyTime);
    }


    private Boolean hubsReleaseDrones(){
        Boolean remainingUnreleasedDrones = false;
        for(Hub hub : hubs){
            Drone d = hub.releaseDrone();
            if(d != null) {
                drones.add(hub.releaseDrone());
                remainingUnreleasedDrones = true;
            }
        }
        return remainingUnreleasedDrones;
    }


    private void addDrone(DroneData dd, DroneFactory inFactory) {
        // instantiate drone (using factory)
        DroneFactory factory = inFactory;
        Drone drone = factory.getDrone(dd, worldDiameterCells, worldHeightCells);

        // add drone to drones List
        drones.add(drone);
    }


    private ArrayList<Drone> dronesSense(){
        // TODO:  Add drone detecting method.  This will be used later to assess different avoidance techniques.
        return null;
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
            Main.logger.log("A crash has occured between 2 or more drones!");
            int i = 1;
            for(Drone d : crashedDrones){
                d.endTime = World.time;
                Main.logger.log("Drone " + i + ": ");
                d.printStats();
                drones.remove(d);
                i++;
            }

            throw new DroneCrashException();
        }
    }


    private HashSet<Drone> crashOccured(){
        HashSet<Drone> returnDrones = new HashSet<>();
        HashMap<Position, Drone> dronePositions = new HashMap<>();
        for(Drone drone : drones){
            Position position = new Position(drone.currentPosition);
            if(!dronePositions.containsKey(position)){
                dronePositions.put(position, drone);
            }
            else{
                returnDrones.add(drone);
                returnDrones.add(dronePositions.get(position));
            }
        }
        return returnDrones.isEmpty() ? null : returnDrones;
    }
}
