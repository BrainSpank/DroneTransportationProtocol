package tests;

import com.company.Main;
import exceptions.DroneCrashException;
import helpers.DroneData;

import java.util.ArrayList;
import java.util.Random;

public class MaxConcurrentTest extends Test{

    private String droneType;

    public MaxConcurrentTest(String inDroneType){
        super();
        droneType = inDroneType;
    }


    public Boolean run(){
        int sumOfDronesBeforeCrash = 0;
        int numOfIterations = 50;
        for(int i = 0; i < numOfIterations; i++){
            setupWorld();
            int numOfDrones = 0;
            Boolean noCrashes = true;
            while (noCrashes) {
                // set up drones
                numOfDrones = numOfDrones + 5;
                droneData = generateRandomDrones(numOfDrones, droneType);

                try{
                    run(droneData);
                    outputFlightDataInGraphableFormat("maxConcurrentFlightData.m");
                } catch (DroneCrashException e){
                    Main.logger.log("Crash occurred with: " + numOfDrones + " concurrent drones");
                    sumOfDronesBeforeCrash += numOfDrones;
                    noCrashes= false;
                }
            }
        }

        int averageDronesBeforeCrash = sumOfDronesBeforeCrash / numOfIterations;

        Main.logger.log("\nThe Average number of concurrent drones before a crash occurred was: " + averageDronesBeforeCrash);
        Main.logger.log("Over: " + numOfIterations + " iterations\n");

        return true;
    }


    private ArrayList<DroneData> generateRandomDrones(int numberOfDrones, String type){
        ArrayList<DroneData> drones = new ArrayList<>();
        Random rand = new Random();

        for(int i = 0; i < numberOfDrones; i++){
            int xStart = rand.nextInt(world.worldDiameterCells);
            int yStart = rand.nextInt(world.worldDiameterCells);
            int xEnd = rand.nextInt(world.worldDiameterCells);
            int yEnd = rand.nextInt(world.worldDiameterCells);
            DroneData d = new DroneData(type, xStart, yStart, xEnd, yEnd);
            drones.add(d);
        }
        return drones;
    }
}
