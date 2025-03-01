package ca.mcmaster.se2aa4.island.teamXXX;

public class DroneStats {
    private Integer batterylevel;
    private String direction;
    private final Integer batteryCapacity;

    public DroneStats(String direction, Integer batterylevel){
        this.batteryCapacity = batterylevel;
        this.batterylevel = batterylevel;
        this.direction = direction; 
    }
 
    public Integer getBatterylevel(){
        return batterylevel;
    }
    public String getDirection(){
        return direction;
    }
    public Integer getBatteryCapacity(){
        return batteryCapacity;
    }
    public void decreaseBatteryLevel(int decrement){
        batterylevel = batterylevel - decrement;
    }
    public void changeDirection(String new_direction){
        direction = new_direction;
    }
}
