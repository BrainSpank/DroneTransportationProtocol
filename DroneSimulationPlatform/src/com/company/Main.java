package com.company;

import helpers.Logger;
import tests.*;

public class Main {
    public static Boolean verbose = true;
    public static Logger logger = new Logger("testLog.txt", verbose);

    public static void main(String[] args) {

        Test crashTest = new CrashTest();
        if(crashTest.run()) {
            logger.log("Crash Test has passed!");
        }
        else {
            System.out.println("CRASH TEST FAILED");
        }
        System.out.println();


        Test successTest = new SuccessfulFlightsTest();
        if(successTest.run()){
            System.out.println("Success Test has passed!");
        }
        else{
            System.out.println("SUCCESS TEST FAILED");
        }
        System.out.println();


/*
        Test maxConcurrentBasicTest = new MaxConcurrentTest("BASICDRONE");
        if(maxConcurrentBasicTest.run()){
            System.out.println("Success Test has passed!");
        }
        else{
            System.out.println("MAX CONCURRENT TEST FAILED");
        }
        System.out.println();


        Test maxConcurrentRandomHeightTest = new MaxConcurrentTest("RANDOMHEIGHTDRONE");
        if(maxConcurrentRandomHeightTest.run()){
            System.out.println("Success Test has passed!");
        }
        else{
            System.out.println("MAX CONCURRENT TEST FAILED");
        }
        System.out.println();

        /*
        Test hubTest = new HubTest();
        if(hubTest.run()){
            System.out.println("Success Test has passed!");
        }
        else{
            System.out.println("MAX CONCURRENT TEST FAILED");
        }
        System.out.println();
        */


        logger.close();
        System.exit(0);
    }
}
