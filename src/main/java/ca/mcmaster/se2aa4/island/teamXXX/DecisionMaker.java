package ca.mcmaster.se2aa4.island.teamXXX;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class DecisionMaker {
    private static DecisionMaker instance;
    private final Logger logger = LogManager.getLogger();
    private JSONObject response;
    private JSONObject currentDecision;
    private DroneStats drone;
    private boolean landFound = false;
    private boolean creekFound = false;
    private boolean emergencySiteFound = false;
    private boolean turnAround = false;
    private Report report = new Report();
    private int firstWTurn = 0;

    private DecisionMaker(DroneStats drone) {
        this.drone = drone;
    }

    public static DecisionMaker getInstance(DroneStats drone) { // Singleton
        if (instance == null) {
            instance = new DecisionMaker(drone);
        }
        return instance;
    }

    public JSONObject chooseAction() {
        if (currentDecision == null) {
            currentDecision = new JSONObject();
            currentDecision.put("action", "scan");
            return currentDecision;
        }

        if (drone.getBatterylevel() > (drone.getBatteryCapacity() / 5)) {
            SearchType search;
            if (landFound) {
                // Gridsearching method
                search = new GridSearch(response, currentDecision, drone, this);
            } else {
                // if we have sufficient battery, continue exploring
                search = new IslandFinder(response, currentDecision, drone, this);
            }
            search.makeMove();
            currentDecision = search.getdecision();
        } else {
            // stop right here so we can make it back
            logger.info("___________No More Power_______");
            stop();
        }

        return currentDecision;
    }

    // Helper
    private void stop() {
        currentDecision.put("action", "stop");
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
