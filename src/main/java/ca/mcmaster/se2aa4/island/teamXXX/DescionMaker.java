package ca.mcmaster.se2aa4.island.teamXXX;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.json.JsonConfiguration;
import org.json.JSONObject;

import main.java.ca.mcmaster.se2aa4.island.teamXXX.GridSearch;
import main.java.ca.mcmaster.se2aa4.island.teamXXX.SearchType;
import netscape.javascript.JSObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DescionMaker {
    private final Logger logger = LogManager.getLogger();
    private JSONObject response;
    private JSONObject currentDesicion;
    DroneStats drone;
    public static boolean landFound = false;

    public DescionMaker(DroneStats drone) {
        this.drone = drone;
    }

    public JSONObject chooseAction() {
        if (currentDesicion == null) {
            currentDesicion = new JSONObject();
            currentDesicion.put("action", "scan");
            return currentDesicion;
        }
        logger.info("**BATTERY LEVEL: {}" + drone.getBatterylevel());
        logger.info("**BATTERY Cap: {}" + drone.getBatteryCapacity());
        boolean check = drone.getBatterylevel() > (drone.getBatteryCapacity() / 2);
        logger.info("**BATTERY CHECK" + check);
        if (drone.getBatterylevel() > (drone.getBatteryCapacity() / 2)) {
            SearchType search;
            if (landFound){
                //Gridsearching method
                search = new GridSearch(response, currentDesicion, drone);
            }
            else{
                // if we have more than half the battery, continue exploring
                search = new IslandFinder(response, currentDesicion, drone);
            }
            search.makeMove();
            currentDesicion = search.getDesicion();
        } 
        else {
            // stop right here so we can make it back
            stop();
        }

        return currentDesicion;
    }

    private void stop() {
        currentDesicion.put("action", "stop");
    }

    // Getters and Setters
    public void setResponse(JSONObject response) {
        this.response = response;
        changeDroneStats();
    }

    private void changeDroneStats() {
        drone.decreaseBatteryLevel(response.getInt("cost"));
    }
}
