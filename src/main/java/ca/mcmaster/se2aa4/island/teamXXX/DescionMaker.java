package ca.mcmaster.se2aa4.island.teamXXX;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.json.JsonConfiguration;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.GridSearch;
import ca.mcmaster.se2aa4.island.teamXXX.IslandFinder;

import netscape.javascript.JSObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DescionMaker {
    private final Logger logger = LogManager.getLogger();
    private JSONObject response;
    private JSONObject currentDesicion;
    private DroneStats drone;
    private boolean landFound = false;
    private boolean creekFound = false;
    private boolean emergencySiteFound = false;
    private boolean turnAround = false;
    private Report report = new Report();
    private int firstWTurn = 0;

    public DescionMaker(DroneStats drone) {
        this.drone = drone;
    }

    public JSONObject chooseAction() {
        if (currentDesicion == null) {
            currentDesicion = new JSONObject();
            currentDesicion.put("action", "scan");
            return currentDesicion;
        }

        if (drone.getBatterylevel() > (drone.getBatteryCapacity() / 5)) {
            SearchType search;
            if (landFound) {
                // Gridsearching method
                search = new GridSearch(response, currentDesicion, drone, this);
            } else {
                // if we have more than half the battery, continue exploring
                search = new IslandFinder(response, currentDesicion, drone, this);
            }
            search.makeMove();
            currentDesicion = search.getDesicion();
        } else {
            // stop right here so we can make it back
            logger.info("___________No More Power_______");
            stop();
        }

        return currentDesicion;
    }

    // Helper
    private void stop() {
        currentDesicion.put("action", "stop");
    }

    private void changeDroneStats() {
        drone.decreaseBatteryLevel(response.getInt("cost"));
    }

    // Getters and Setters
    public void setResponse(JSONObject response) {
        this.response = response;
        changeDroneStats();
    }

    public void setLandFound(boolean landFound) {
        this.landFound = landFound;
    }

    public boolean isCreekFound() {
        return creekFound;
    }

    public void setCreekFound(boolean creekFound) {
        this.creekFound = creekFound;
    }

    public boolean isEmergencySiteFound() {
        return emergencySiteFound;
    }

    public void setEmergencySiteFound(boolean emergencySiteFound) {
        this.emergencySiteFound = emergencySiteFound;
    }

    public boolean isTurnAround() {
        return turnAround;
    }

    public void setTurnAround(boolean turnAround) {
        this.turnAround = turnAround;
    }

    public Report getReport() {
        return report;
    }

    public int isFirstWTurn() {
        return firstWTurn;
    }

    public void setFirstWTurn(int firstWTurn) {
        this.firstWTurn = firstWTurn;
    }

}
