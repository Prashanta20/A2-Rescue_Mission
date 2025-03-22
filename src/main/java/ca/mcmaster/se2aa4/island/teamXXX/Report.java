package ca.mcmaster.se2aa4.island.teamXXX;

public class Report {
    private String creekID;
    private String emergencyID;

    public Report() {

    }

    public void setCreekID(String creekID) { // update creek id
        this.creekID = creekID;
    }

    public void setEmergencyID(String emergencyID) { // update emergency site id
        this.emergencyID = emergencyID;
    }

    @Override
    public String toString() { // method to output creek and emergency site id
        String output = "CreekID: " + creekID + "\nEmergency Site ID: " + emergencyID;
        return output;
    }
}
