package ca.mcmaster.se2aa4.island.teamXXX;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.json.JsonConfiguration;
import org.json.JSONObject;

import netscape.javascript.JSObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DescionMaker {
    private final Logger logger = LogManager.getLogger();
    private JSONObject response;
    private JSONObject currentDesicion;
    DroneStats drone;

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
        logger.info("**BATTERY CAp: {}" + drone.getBatteryCapacity());
        boolean check = drone.getBatterylevel() > (drone.getBatteryCapacity() / 2);
        logger.info("**BATTERY CHECK" + check);
        if (drone.getBatterylevel() > (drone.getBatteryCapacity() / 2)) {
            // if we have more than half the battery, continue exploring
            exploring();
        } else {
            // stop right here so we can make it back
            stop();
        }

        return currentDesicion;

    }

    // exploring helper
    private void exploring() {
        // Depending on previous move, we make move
        String prevMove = currentDesicion.getString("action");
        logger.info("**MOVE: {}" + prevMove);

        if (prevMove.equals("scan")) {
            // previous was scan
            logger.info("**Is OCeaon: {}" + response.getJSONObject("extras").get("biomes"));
            if (response.getJSONObject("extras").getJSONArray("biomes").getString(0).equals("OCEAN")) {
                echo();
            } else {
                stop();
            }

        } else if (prevMove.equals("echo")) {
            // previous was echo
            if (response.getJSONObject("extras").get("found").equals("OUT_OF_RANGE")) {
                fly();
            } else {
                if (drone.getDirection().equals("S")) {
                    fly();
                } else {
                    heading();
                }
            }

        } else if (prevMove.equals("fly")) {
            // was fly
            scan();
        } else if (prevMove.equals("heading")) {
            // was heading
            fly();
        } else {
            // any other move
        }

    }

    // Actions to perform
    private void echo() {
        currentDesicion.put("action", "echo");
        JSONObject parameters = new JSONObject();
        parameters.put("direction", "S");
        currentDesicion.put("parameters", parameters);
    }

    private void scan() {
        currentDesicion.put("action", "scan");
    }

    private void fly() {
        currentDesicion.put("action", "fly");
    }

    private void heading() {
        currentDesicion.put("action", "heading");
        JSONObject parameters = new JSONObject();
        parameters.put("direction", "S");
        currentDesicion.put("parameters", parameters);
        drone.changeDirection("S");
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
