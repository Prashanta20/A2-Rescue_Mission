package ca.mcmaster.se2aa4.island.teamXXX;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.json.JsonConfiguration;
import org.json.JSONObject;
import ca.mcmaster.se2aa4.island.teamXXX.DroneStats;

public abstract class SearchType {
    protected final Logger logger = LogManager.getLogger();
    protected JSONObject response;
    protected JSONObject currentDesicion;
    protected DroneStats drone;
    
    public abstract void makeMove();

    public JSONObject getDesicion(){
        return currentDesicion;
    }

    protected void echo() {
        currentDesicion.put("action", "echo");
        JSONObject parameters = new JSONObject();
        parameters.put("direction", "S");
        currentDesicion.put("parameters", parameters);
    }

    protected void scan() {
        currentDesicion.put("action", "scan");
    }

    protected void fly() {
        currentDesicion.put("action", "fly");
    }

    protected void heading() {
        currentDesicion.put("action", "heading");
        JSONObject parameters = new JSONObject();
        parameters.put("direction", "S");
        currentDesicion.put("parameters", parameters);
        drone.changeDirection("S");
    }

    protected void stop() {
        currentDesicion.put("action", "stop");
    }
}
