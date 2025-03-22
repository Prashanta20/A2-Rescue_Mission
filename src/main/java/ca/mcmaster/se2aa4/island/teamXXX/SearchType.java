package ca.mcmaster.se2aa4.island.teamXXX;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public abstract class SearchType {
    protected final Logger logger = LogManager.getLogger();
    protected JSONObject response;
    protected JSONObject currentDecision;
    protected DroneStats drone;

    public abstract void makeMove();

    public JSONObject getdecision() {
        return currentDecision;
    }

    protected void echo(String direction) { // method for echo function
        drone.setEchoDirection(direction);
        currentDecision.put("action", "echo");
        JSONObject parameters = new JSONObject();
        parameters.put("direction", direction);
        currentDecision.put("parameters", parameters);
    }

    protected void scan() { // method for scan function
        currentDecision.put("action", "scan");
    }

    protected void fly() { // method for fly function
        drone.setPrevDirection(drone.getDirection());
        currentDecision.put("action", "fly");
    }

    protected void heading(String direction) { // method for changing direction
        drone.setPrevDirection(drone.getDirection());
        currentDecision.put("action", "heading");
        JSONObject parameters = new JSONObject();
        parameters.put("direction", direction);
        currentDecision.put("parameters", parameters);
        drone.changeDirection(direction);
    }

    protected void stop() { // method for returning home
        currentDecision.put("action", "stop");
    }
}
