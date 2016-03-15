package helpers;

public class CityData {
    public String cityName;
    public int cityDiameter;
    public int cellSize;
    public double xCoordinateOf00;
    public double yCoordinateOf00;

    public CityData(String inCityName, String inCityDiamter, String inCellSize, String inXCoord, String inYCoord){
        cityName = inCityName;
        cityDiameter = Integer.parseInt(inCityDiamter);
        cellSize = Integer.parseInt(inCellSize);
        xCoordinateOf00 = Double.parseDouble(inXCoord);
        yCoordinateOf00 = Double.parseDouble(inYCoord);
    }
}
