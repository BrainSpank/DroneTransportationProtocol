package com.company;

import helpers.Logger;
import tests.*;

public class Main {
    public static Boolean verbose = true;
    public static Logger logger = new Logger("log.txt", verbose);

    public static void main(String[] args) {

        // CRASH TEST
        if (true) {
            Test crashTest = new CrashTest();
            if (crashTest.run()) {
                Main.logger.log("Crash Test has passed!");
            } else {
                Main.logger.log("CRASH TEST FAILED");
            }
            Main.logger.log("");
        }


        // SUCCESSFUL FLIGHT TEST
        if(true) {
            Test successTest = new SuccessfulFlightsTest();
            if (successTest.run()) {
                Main.logger.log("Success Test has passed!");
            } else {
                Main.logger.log("SUCCESS TEST FAILED");
            }
            Main.logger.log("");
        }


        // SENSE TEST
        if(true) {
            Test successfulSenseTest = new SuccessfulSenseTest();
            if (successfulSenseTest.run()) {
                Main.logger.log("Successful Sense Test has passed!");
            } else {
                Main.logger.log("SENSE TEST FAILED");
            }
            Main.logger.log("");
        }


        // BASIC DRONE MAX CONCURRENT TEST
        if(true) {
            Test maxConcurrentBasicTest = new MaxConcurrentTest("BASICDRONE");
            if (maxConcurrentBasicTest.run()) {
                Main.logger.log("Max Concurrent Test has passed!");
            } else {
                Main.logger.log("MAX CONCURRENT TEST FAILED");
            }
            Main.logger.log("");
        }


        // RANDOM HEIGHT DRONE TEST
        if (true) {
            Test maxConcurrentRandomHeightTest = new MaxConcurrentTest("RANDOMHEIGHTDRONE");
            if (maxConcurrentRandomHeightTest.run()) {
                Main.logger.log("Random Height Test has passed!");
            } else {
                Main.logger.log("RANDOM HEIGHT TEST FAILED");
            }
            Main.logger.log("");
        }


        logger.close();
        System.exit(0);
    }
}
