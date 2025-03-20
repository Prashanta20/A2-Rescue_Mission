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
        /*
         * String prevMove = currentDesicion.getString("action");
         * logger.info("==========GRID SEARCHING ==========");
         * 
         * if ((decsicion.isCreekFound() == true) && (decsicion.isEmergencySiteFound()
         * == true)) {
         * // if we have found a creek and emergency site
         * stop();
         * } else {
         * if (prevMove.equals("scan")) {
         * // we just scanned, we can check if we are above ground or water
         * if (isCreek()) {
         * // found a creek
         * logger.info("==========FOUND CREEK ==========");
         * decsicion.setCreekFound(true);
         * }
         * if (isEmergencySite()) {
         * // found a creek
         * logger.info("==========FOUND EMERGENCY ==========");
         * decsicion.setEmergencySiteFound(true);
         * }
         * 
         * // TURNING
         * if (isOcean()) {
         * // flying over ocean
         * // turn right
         * String facing = drone.getDirection();
         * 
         * if (facing.equals("S") || facing.equals("N")) {
         * // Facing east and over water, turn to south
         * // look to see if there is land on right side still
         * echo("E");
         * // heading("E");
         * } else if (facing.equals("E")) {
         * // if are currently going east
         * // middle of the turn
         * if (drone.getPrevDirection().equals("N")) {
         * // if we were north before
         * heading("S");
         * } else if (drone.getPrevDirection().equals("S")) {
         * heading("N");
         * }
         * } else {
         * // we are facing west
         * stop();
         * logger.info("************** STOPPING ***********");
         * }
         * 
         * } else {
         * // not over the ocean so fly
         * fly();
         * }
         * } else if (prevMove.equals("echo")) {
         * // previous move was echo
         * // do something with echo where if we dont see any more island on the east
         * side,
         * // we need to turn around
         * if (isOutOfRange()) {
         * // no more island to East side
         * heading("W");
         * } else {
         * // still more island left
         * heading("E");
         * }
         * } else {
         * // previous move is not scan so we scan
         * scan();
         * }
         * }
         * 
         * // stop(); // remove this line
         */

        // String prevMove = currentDesicion.getString("action");
        logger.info("==========GRID SEARCHING ==========");

        if ((decsicion.isCreekFound()) && (decsicion.isEmergencySiteFound())) {
            // we found the creek and emergency site so we stop
            stop();
        } else {
            // continue the search
            search();
        }

    }

    private void search() {
        String prevMove = currentDesicion.getString("action"); // the move we made

        if (prevMove.equals("scan")) {
            // do something

            // check for creek and emergency site
            if (isCreek()) {
                // found a creek
                logger.info("==========FOUND CREEK ==========");
                decsicion.setCreekFound(true);
                decsicion.getReport().setCreekID(response.getJSONObject("extras").getJSONArray("creeks").getString(0));
            }
            if (isEmergencySite()) {
                // found a creek
                logger.info("==========FOUND EMERGENCY ==========");
                decsicion.setEmergencySiteFound(true);
                decsicion.getReport()
                        .setEmergencyID((response.getJSONObject("extras").getJSONArray("sites").getString(0)));
            }

            // if we are over the ocean now
            if (isOcean()) {
                // we echo to make sure there is no more island
                echo(drone.getDirection());
            } else {
                // still over land, just fly
                fly();
            }

        } else if (prevMove.equals("echo")) {
            // do something
            logger.info("************** HERE ***********");
            if (drone.getEchoDirection().equals("N") || drone.getEchoDirection().equals("S")) {
                // north or south echo
                if (isOutOfRange()) {

                    // there is no more land left
                    if (drone.getPrevDirection().equals("N") || drone.getPrevDirection().equals("S")) {
                        // could have mor island on the east or west
                        // pass
                    } else {
                        // previous direction was east or west so we are come from a turn
                        if (decsicion.isTurnAround()) {
                            // now want to stop
                            logger.info("************** STOPPING ***********");
                            stop();
                            return;
                        } else {
                            // now we want set to be turning
                            decsicion.setTurnAround(true);
                        }

                    }

                    if (decsicion.isTurnAround()) {
                        // we go west
                        heading("W");
                    } else {
                        // we go east
                        heading("E");
                    }
                } else {
                    fly();
                }

            } else {
                // pass
                fly();
            }

        } else if (prevMove.equals("fly")) {
            // do something
            if (decsicion.isFirstWTurn() == 1) {
                // special turning
                // heading to either North or South
                heading(drone.getEchoDirection());
                decsicion.setFirstWTurn(2);
            } else {
                // normal
                scan();
            }

        } else if (prevMove.equals("heading")) {
            // do something
            // if are currently going east
            // middle of the turn

            if (drone.getDirection().equals("W")) {
                if (decsicion.isFirstWTurn() == 0) {
                    // this is our first W turn so go fly once
                    fly();
                    decsicion.setFirstWTurn(1);
                    return;
                } // otherwise contiinue normally
            }

            if (drone.getPrevDirection().equals("N")) {
                // if we were north before
                heading("S");
            } else if (drone.getPrevDirection().equals("S")) {
                // logger.info("************** HERE ***********");
                heading("N");
            } else {
                // if we finished our turning
                echo(drone.getDirection());
                // we have just completed the turn
                // we may need to turn to the west now
                // logger.info("************** STOPPING ***********");

                // scan(); // stop For now
            }
        } else {
            // do something
        }
    }

    private boolean isOcean() {
        return response.getJSONObject("extras").getJSONArray("biomes").getString(0).equals("OCEAN");
    }

    private boolean isCreek() {
        // if its not empty then it is a creek
        return response.getJSONObject("extras").getJSONArray("creeks").length() > 0;
    }

    private boolean isEmergencySite() {
        // if its not empty then it is a creek
        return response.getJSONObject("extras").getJSONArray("sites").length() > 0;
    }

    private boolean isOutOfRange() {
        return response.getJSONObject("extras").get("found").equals("OUT_OF_RANGE");
    }

}
