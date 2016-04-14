package tests;

import drones.Drone;
import drones.DroneFactory;
import exceptions.DroneCrashException;
import helpers.DroneData;
import world.Hub;

import java.util.ArrayList;
import java.util.Random;

public class HubTest extends Test{

    private ArrayList<Hub> hubs = new ArrayList<>();

    public HubTest(){
        super();
    }

    public Boolean run(){
        generateHubs();

        addDronesToHubs();

        world.addHubs(hubs);

        try {
            run(null);
            return true;
        }
        catch(DroneCrashException e){
            e.printStackTrace();
            return false;
        }
    }


    private void generateHubs(){
        int quarterDistance = world.worldDiameterCells/4;
        int halfDistance = world.worldDiameterCells/2;
        int threeQuarterDistance = (world.worldDiameterCells*3)/4;

        hubs.add(new Hub(new Integer[]{quarterDistance, quarterDistance, 0}));
        hubs.add(new Hub(new Integer[]{quarterDistance, threeQuarterDistance, 0}));
        hubs.add(new Hub(new Integer[]{halfDistance, halfDistance, 0}));
        hubs.add(new Hub(new Integer[]{threeQuarterDistance, quarterDistance, 0}));
        hubs.add(new Hub(new Integer[]{threeQuarterDistance, threeQuarterDistance, 0}));
    }


    private void addDronesToHubs(){
        for(Hub hub : hubs){
            ArrayList<Drone> drones = generateRandomDrones(50, hub);
            hub.addDrones(drones);
        }
    }


    private ArrayList<Drone> generateRandomDrones(int numberOfDrones, Hub startHub){
        ArrayList<Drone> drones = new ArrayList<>();
        Random rand = new Random();
        DroneFactory droneFactory = new DroneFactory();

        for(int i = 0; i < numberOfDrones; i++){
            int xEnd = rand.nextInt(world.worldDiameterCells);
            int yEnd = rand.nextInt(world.worldDiameterCells);
            DroneData dd = new DroneData("HUBDRONE", startHub.getLocation()[0], startHub.getLocation()[1], xEnd, yEnd);
            Drone drone = droneFactory.getDrone(dd, world.worldDiameterCells, world.worldHeightCells);
            drones.add(drone);
        }
        return drones;
    }

}
