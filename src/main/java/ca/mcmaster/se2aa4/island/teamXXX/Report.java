package ca.mcmaster.se2aa4.island.teamXXX;

public class Report {
    private String creekID;
    private String emergencyID;

    public Report() {

    }

    public void setCreekID(String creekID) {
        this.creekID = creekID;
    }

    public void setEmergencyID(String emergencyID) {
        this.emergencyID = emergencyID;
    }

    @Override
    public String toString() {
        String output = "CreekID: " + creekID + "\nEmergency Site ID: " + emergencyID;
        return output;
    }
}
