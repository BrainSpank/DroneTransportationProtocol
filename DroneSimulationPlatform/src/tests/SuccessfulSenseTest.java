package tests;

import exceptions.DroneCrashException;

public class SuccessfulSenseTest extends Test{

    public SuccessfulSenseTest(){
        super();
    }

    public Boolean run(){
        droneData = readInDrones("droneSense.csv");
        try{
            run(droneData);
        } catch (DroneCrashException e){
            return false;
        }

        outputFlightDataInGraphableFormat("successSenseData.m");

        return true;
    }
}


