package tests;

import exceptions.DroneCrashException;

public class CrashTest extends Test{

    public CrashTest(){
        super();
    }


    public Boolean run(){
        droneData = readInDrones("crashDrones.csv");
        // testing
        // for drones to crash
        try {
            run(droneData);
        } catch (DroneCrashException e){
            // As this tests for drones crashing, catching this exception counts as a success.
            //e.printStackTrace();
            return true;
        } finally {
            outputFlightDataInGraphableFormat("crashFlightData.m");
        }

        // if test completes without catching an exception the it has failed so return false
        return false;
    }
}
