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
import java.util.Map;

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
    public ArrayList<Drone> completedJourneyDrones = new ArrayList<>();

    private ArrayList<Drone> drones = new ArrayList<>();
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

        Boolean requireNextTick2 = false;
        if(!hubs.isEmpty()){
            requireNextTick2 = true;
            if(time%delayBetweenDroneReleases == 0) {
                requireNextTick2 = hubsReleaseDrones();
            }
        }

        HashMap<Position, ArrayList<Drone>> requireAvoid = dronesSense();
        if(!requireAvoid.isEmpty()) {
            for(Map.Entry<Position, ArrayList<Drone>> entry : requireAvoid.entrySet()){
                for(Drone drone : entry.getValue()){
                    drone.avoid();
                }
            }
        }

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


    private HashMap<Position, ArrayList<Drone>> dronesSense(){
        HashMap<Position, ArrayList<Drone>> closeProximityDrones = new HashMap<>();

        for(Drone drone : drones){
            Integer[] currentPosition = drone.currentPosition;
            // Floor current coordinates to the nearest 100 metres (equivilent to 20 cells because each cell = 5m)
            Integer[] currentSector = new Integer[3];
            currentSector[0] = currentPosition[0] - currentPosition[0]%20;
            currentSector[1] = currentPosition[1] - currentPosition[1]%20;
            currentSector[2] = currentPosition[2] - currentPosition[2]%20;
            Position position = new Position(currentSector);

            if(!closeProximityDrones.containsKey(position)){
                closeProximityDrones.put(position, new ArrayList<Drone>());
                closeProximityDrones.get(position).add(drone);
            }
            else{
                closeProximityDrones.get(position).add(drone);
            }
        }

        HashMap<Position, ArrayList<Drone>> returnDrones = new HashMap<>();

        for(Map.Entry<Position, ArrayList<Drone>> entry : closeProximityDrones.entrySet()){
            if(entry.getValue().size() > 1){
                returnDrones.put(entry.getKey(), entry.getValue());
                //closeProximityDrones.remove(entry.getKey());
            }
        }

        return returnDrones;
    }


    // Moves every drone by one step.  Detects if a crash has occurred.
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
        if(!crashedDrones.isEmpty()){
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
        return returnDrones;
    }
}
