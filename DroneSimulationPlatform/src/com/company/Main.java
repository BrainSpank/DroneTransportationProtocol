package com.company;

import helpers.Logger;
import tests.*;

public class Main {
    public static Boolean verbose = true;
    public static Logger logger = new Logger("testLog.txt", verbose);

    public static void main(String[] args) {

        Test crashTest = new CrashTest();
        if(crashTest.run()) {
            Main.logger.log("Crash Test has passed!");
        }
        else {
            Main.logger.log("CRASH TEST FAILED");
        }
        Main.logger.log("");


        Test successTest = new SuccessfulFlightsTest();
        if(successTest.run()){
            Main.logger.log("Success Test has passed!");
        }
        else{
            Main.logger.log("SUCCESS TEST FAILED");
        }
        Main.logger.log("");


/*
        Test maxConcurrentBasicTest = new MaxConcurrentTest("BASICDRONE");
        if(maxConcurrentBasicTest.run()){
            Main.logger.log("Success Test has passed!");
        }
        else{
            Main.logger.log("MAX CONCURRENT TEST FAILED");
        }
        Main.logger.log("");
*/

        Test maxConcurrentRandomHeightTest = new MaxConcurrentTest("RANDOMHEIGHTDRONE");
        if(maxConcurrentRandomHeightTest.run()){
            Main.logger.log("Success Test has passed!");
        }
        else{
            Main.logger.log("MAX CONCURRENT TEST FAILED");
        }
        Main.logger.log("");

/*
        Test hubTest = new HubTest();
        if(hubTest.run()){
            Main.logger.log("Success Test has passed!");
        }
        else{
            Main.logger.log("HUB TEST FAILED");
        }
        Main.logger.log("");

        */

        logger.close();
        System.exit(0);
    }
}
