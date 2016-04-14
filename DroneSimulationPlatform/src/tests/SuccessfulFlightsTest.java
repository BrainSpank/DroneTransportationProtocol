package tests;

import exceptions.DroneCrashException;

public class SuccessfulFlightsTest extends Test{
    public SuccessfulFlightsTest(){
        super();
    }

    public Boolean run(){
        droneData = readInDrones("successfulFlightDrones.csv");
        try{
            run(droneData);
        } catch (DroneCrashException e){
            return false;
        }

        return true;
    }
}
