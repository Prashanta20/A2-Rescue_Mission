package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONObject;

public class IslandFinder extends SearchType {
    DecisionMaker decision;

    public IslandFinder(JSONObject reponse, JSONObject currentdecision, DroneStats drone, DecisionMaker decision) {
        this.response = reponse;
        this.currentDecision = currentdecision;
        this.drone = drone;
        this.decision = decision;
    }

    @Override
    public void makeMove() {
        // Depending on previous move, we make move
        String prevMove = currentDecision.getString("action");
        logger.info("**MOVE: {}" + prevMove);

        if (prevMove.equals("scan")) {
            // previous was scan
            logger.info("**Is OCeaon: {}" + response.getJSONObject("extras").get("biomes"));
            if (response.getJSONObject("extras").getJSONArray("biomes").getString(0).equals("OCEAN")) {
                echo("S");
            } else {
                // stop();
                decision.setLandFound(true);
            }

        } else if (prevMove.equals("echo")) {
            // previous was echo
            if (response.getJSONObject("extras").get("found").equals("OUT_OF_RANGE")) {
                fly();
            } else {
                if (drone.getDirection().equals("S")) {
                    fly();
                } else {
                    heading("S");
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
}
