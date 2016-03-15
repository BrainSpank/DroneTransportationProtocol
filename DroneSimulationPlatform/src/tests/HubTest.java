package tests;

import exceptions.DroneCrashException;
import helpers.DroneData;

import java.util.ArrayList;
import java.util.Random;

public class HubTest extends Test{

    private ArrayList<Integer[]> hubs = new ArrayList<>();

    public HubTest(){
        super();
    }

    public Boolean run(){
        setupWorld();
        generateHubs();

        int sumOfDronesBeforeCrash = 0;
        int numOfIterations = 100;
        for(int i = 0; i < numOfIterations; i++){
            setupWorld();
            int numOfDrones = 0;
            Boolean noCrashes = true;
            while (noCrashes) {
                // set up drones
                numOfDrones = numOfDrones + 10;
                droneData = generateRandomDrones(numOfDrones, "HUBDRONE");

                try{
                    run(droneData);
                } catch (DroneCrashException e){
                    System.out.println("Crash occurred with: " + numOfDrones + " concurrent drones");
                    sumOfDronesBeforeCrash += numOfDrones;
                    noCrashes= false;
                }
            }
        }

        int averageDronesBeforeCrash = sumOfDronesBeforeCrash / numOfIterations;

        System.out.println("\nThe Average number of concurrent drones before a crash occurred was: " + averageDronesBeforeCrash);
        System.out.println("Over: " + numOfIterations + " iterations\n");

        return true;
    }


    private void generateHubs(){
        int quarterDistance = world.worldDiameterCells/4;
        int halfDistance = world.worldDiameterCells/2;
        int threeQuarterDistance = (world.worldDiameterCells*3)/4;

        hubs.add(new Integer[]{quarterDistance, quarterDistance, 0});
        hubs.add(new Integer[]{quarterDistance, threeQuarterDistance, 0});
        hubs.add(new Integer[]{halfDistance, halfDistance, 0});
        hubs.add(new Integer[]{threeQuarterDistance, quarterDistance, 0});
        hubs.add(new Integer[]{threeQuarterDistance, threeQuarterDistance, 0});
    }


    private ArrayList<DroneData> generateRandomDrones(int numberOfDrones, String type){
        ArrayList<DroneData> drones = new ArrayList<>();
        Random rand = new Random();

        for(int i = 0; i < numberOfDrones; i++){
            int index = rand.nextInt(hubs.size());
            Integer[] startHub = hubs.get(index);
            int xEnd = rand.nextInt(world.worldDiameterCells);
            int yEnd = rand.nextInt(world.worldDiameterCells);
            DroneData d = new DroneData(type, startHub[0], startHub[1], xEnd, yEnd);
            drones.add(d);
        }
        return drones;
    }

}
