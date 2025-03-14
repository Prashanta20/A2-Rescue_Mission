package ca.mcmaster.se2aa4.island.teamXXX;

import ca.mcmaster.se2aa4.island.teamXXX.DroneStats;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class GridSearch extends SearchType {
    private final Logger logger = LogManager.getLogger();
    DescionMaker decsicion;

    public GridSearch(JSONObject reponse, JSONObject currentDesicion, DroneStats drone, DescionMaker decsicion) {
        this.response = reponse;
        this.currentDesicion = currentDesicion;
        this.drone = drone;
        this.decsicion = decsicion;
    }

    @Override
    public void makeMove() {
        String prevMove = currentDesicion.getString("action");
        logger.info("==========GRID SEARCHING ==========");

        if (prevMove.equals("scan")) {
            // we just scanned, we can check if we are above ground or water

            // TURNING
            if (isOcean()) {
                // flying over ocean
                // turn right
                String facing = drone.getDirection();
                if (facing.equals("S") || facing.equals("N")) {
                    // Facing east and over water, turn to south
                    heading("E");
                } else if (facing.equals("E")) {
                    // if are currently going east
                    // middle of the turn
                    if (drone.getPrevDirection().equals("N")) {
                        // if we were north before
                        heading("S");
                    } else {
                        heading("N");
                    }
                }

            } else {
                fly();
            }
        } else {
            scan();
        }
        stop(); // remove this line
    }

    private boolean isOcean() {
        return response.getJSONObject("extras").getJSONArray("biomes").getString(0).equals("OCEAN");
    }
}
