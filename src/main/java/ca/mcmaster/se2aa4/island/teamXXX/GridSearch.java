package ca.mcmaster.se2aa4.island.teamXXX;

import ca.mcmaster.se2aa4.island.teamXXX.DroneStats;
import org.json.JSONObject;

public class GridSearch extends SearchType{
    public boolean creekFound = false;
    public boolean emergencySiteFound = false;

    public GridSearch(JSONObject reponse, JSONObject currentDesicion, DroneStats drone){
        this.response = reponse;
        this.currentDesicion = currentDesicion;
        this.drone = drone;
    }

    @Override
    public void makeMove(){
    
    }
}
