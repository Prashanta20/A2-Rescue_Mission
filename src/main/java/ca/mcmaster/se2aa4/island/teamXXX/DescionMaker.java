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

    public DescionMaker(DroneStats drone){
        this.drone = drone;
    }
    
    public JSONObject chooseAction(){
        if (currentDesicion == null){
            currentDesicion = new JSONObject();
            currentDesicion.put("action", "scan");
        }
        else if (drone.getBatterylevel() > drone.getBatteryCapacity()/2 ){
            JSONObject extra = response.getJSONObject("extras");
            if (currentDesicion.get("action").equals("scan")){
                if (!extra.get("biomes").equals("OCEAN")){
                    currentDesicion.put("action", "stop");
                    return currentDesicion;
                }
                else{
                    currentDesicion.put("action", "echo");
                    JSONObject parameters = new JSONObject();
                    parameters.put("direction", "S"); 
                    currentDesicion.put("parameters", parameters);
                }
            }
            else if (currentDesicion.get("action").equals("fly") || currentDesicion.get("action").equals("heading")) { 
                currentDesicion.put("action", "scan");
            }

            else if(currentDesicion.get("action").equals("echo")){
                if (extra.get("found").equals("OUT_OF_RANGE")){
                    currentDesicion.put("action", "fly");
                }
                //Ground was found in some range
                else{
                    if (drone.getDirection().equals("S")){
                        currentDesicion.put("action", "fly");
                    }
                    else{
                        currentDesicion.put("action", "heading");
                        JSONObject parameters = new JSONObject();
                        parameters.put("direction", "S"); 
                        currentDesicion.put("parameters", parameters);
                        drone.changeDirection("S");    
                    }
                }
            }
        }
        return currentDesicion;
    }

    public void setResponse(JSONObject response){
        this.response = response;
        changeDroneStats();
    }

    private void changeDroneStats(){
        drone.decreaseBatteryLevel(response.getInt("cost"));
    }
}
