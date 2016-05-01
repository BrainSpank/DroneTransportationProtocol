package world;

import drones.Drone;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Hub {

    private Integer[] location;
    private ArrayDeque<Drone> dronesInHub = new ArrayDeque<>();

    public Hub(Integer[] inLocation){

        // set location
        location = inLocation;
    }


    public Integer[] getLocation(){
        return location;
    }


    public ArrayDeque<Drone> getDrones(){
        return dronesInHub;
    }


    public void addDrone(Drone drone){
        dronesInHub.add(drone);
    }


    public void addDrones(ArrayList<Drone> drones){
//        for(Drone drone: drones) {
//            addDrone(drone);
//        }
        dronesInHub.addAll(drones);
    }


    public Drone releaseDrone(){
        Drone currentDrone = dronesInHub.poll();
        if (currentDrone == null) {
            return null;
        }

        //Set drone position to one cell above the Hub
        Integer[] pos = new Integer[]{location[0], location[1], 1};
        currentDrone.currentPosition = pos;



        return currentDrone;
    }
}
