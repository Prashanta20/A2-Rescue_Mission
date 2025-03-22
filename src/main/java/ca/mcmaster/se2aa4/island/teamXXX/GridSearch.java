package ca.mcmaster.se2aa4.island.teamXXX;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class GridSearch extends SearchType {
    private final Logger logger = LogManager.getLogger();
    DecisionMaker decsicion;

    public GridSearch(JSONObject reponse, JSONObject currentDecision, DroneStats drone, DecisionMaker decsicion) {
        this.response = reponse;
        this.currentDecision = currentDecision;
        this.drone = drone;
        this.decsicion = decsicion;
    }

    @Override
    public void makeMove() {
        logger.info("==========GRID SEARCHING ==========");

        DecisionMaker decisionMaker = DecisionMaker.getInstance(drone);

        if ((decisionMaker.isCreekFound()) && (decisionMaker.isEmergencySiteFound())) {
            // we found the creek and emergency site so we stop
            stop();
        } else {
            // continue the search
            search();
        }
    }

    private void search() {
        String prevMove = currentDecision.getString("action"); // the move we made

        if (prevMove.equals("scan")) {
            // check for creek and emergency site
            if (isCreek()) {
                // found a creek
                logger.info("==========FOUND CREEK ==========");
                decsicion.setCreekFound(true);
                decsicion.getReport().setCreekID(response.getJSONObject("extras").getJSONArray("creeks").getString(0)); // obtain
                                                                                                                        // creek
                                                                                                                        // id
            }
            if (isEmergencySite()) {
                // found a creek
                logger.info("==========FOUND EMERGENCY ==========");
                decsicion.setEmergencySiteFound(true);
                decsicion.getReport()
                        .setEmergencyID((response.getJSONObject("extras").getJSONArray("sites").getString(0))); // obtain
                                                                                                                // emergency
                                                                                                                // site
                                                                                                                // id
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
            }
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
