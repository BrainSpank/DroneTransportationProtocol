package tests;

import exceptions.DroneCrashException;

public class CrashTest extends Test{

    public CrashTest(){
        super();
    }


    public Boolean run(){
        droneData = readInDrones("crashDrones.csv");

        try {
            run(droneData);
        } catch (DroneCrashException e){
            // As this tests for drones crashing, catching this exception counts as a success.
            return true;
        } finally {
            outputFlightDataInGraphableFormat("crashFlightData.m");
        }

        // if test completes without catching an exception then it has failed so return false
        return false;
    }
}
