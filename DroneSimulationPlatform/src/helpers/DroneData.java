package helpers;

import drones.DroneType;

public class DroneData {
    public DroneType droneType;
    public int startX;
    public int startY;
    public int endX;
    public int endY;

    public DroneData(String inDroneType, int inStartX, int inStartY, int inEndX, int inEndY){
        this.droneType = DroneType.valueOf(inDroneType);
        this.startX = inStartX;
        this.startY = inStartY;
        this.endX = inEndX;
        this.endY = inEndY;
    }
}
