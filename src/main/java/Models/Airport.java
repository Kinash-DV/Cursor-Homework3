package Models;

import JDBC.AirportData;
import Utils.MsSqlConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Airport {

    private List<ModelPlane> modelPlanes = new ArrayList<>();
    private List<Plane> planes = new ArrayList<>();
    private List<Pilot> pilots = new ArrayList<>();
    private List<Flight> currentFlights = new ArrayList<>();

    private Boolean isConnected = false;

    public Boolean getConnected() {
        return isConnected;
    }

    public Airport() {
        try (Connection connection = MsSqlConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM master.sys.databases WHERE name = 'AirportBase'")) {

            if (resultSet.next()) {
                isConnected = true;
            } else {
                throw new Exception("Database not found");
            }

            reloadData();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Information about the airport:");

        builder.append("\n - list of planes:");
        if (planes.size() == 0) {
            builder.append("\n    (none)");
        } else {
            for (Plane plane: planes) {
                builder.append("\n    " + plane);
            }
        }

        builder.append("\n - list of pilots:");
        if (pilots.size() == 0) {
            builder.append("\n    (none)");
        } else {
            for (Pilot pilot: pilots) {
                builder.append("\n    " + pilot);
            }
        }

        builder.append("\n - list of current flights:");
        if (currentFlights.size() == 0) {
            builder.append("\n    (none)");
        } else {
            for (Flight flight: currentFlights) {
                builder.append("\n    " + flight);
            }
        }

        return builder.toString();
    }

    public String checkPlaneAvailability(Plane plane) {
        for (Flight flight: currentFlights)
            if (flight.getPlane() == plane)
                return "The plane is now flying";
        return null;
    }

    public String checkPilotAvailability(Pilot pilot, Plane plane) {
        if (!pilot.getModelPlanes().contains(plane.getModel()))
            return "The pilot cannot fly " + plane.getModel();
        for (Flight flight: currentFlights)
            if (flight.getPilot1() == pilot || flight.getPilot2() == pilot)
                return "The pilot is now flying";
        return null;
    }

    public void setModelPlanes(List<ModelPlane> modelPlanes) {
        this.modelPlanes = modelPlanes;
    }

    public void setPlanes(List<Plane> planes) {
        this.planes = planes;
    }

    public void setPilots(List<Pilot> pilots) {
        this.pilots = pilots;
    }

    public void setCurrentFlights(List<Flight> currentFlights) {
        this.currentFlights = currentFlights;
    }

    public void addCurrentFlight(Flight flight) {
        if (! currentFlights.contains(flight))
            currentFlights.add(flight);
    }

    public void deleteCurrentFlight(Flight flight){
        currentFlights.remove(flight);
    }

    private void reloadData(){
        if (!this.isConnected) return;
        try{
            AirportData.LoadAirport(this);
        } catch (Exception e) {
            this.isConnected = false;
            e.printStackTrace();
        }
    }

    public void createNewFlight(Plane plane, Pilot firstPilot, Pilot secondPilot) throws SQLException {
        AirportData.createFlight(plane, firstPilot, secondPilot, this);
    }

    public void finishFlight(Flight flight) throws SQLException {
        AirportData.finishFlight(flight, this);
    }

    public void cancelFlight(Flight flight) throws SQLException {
        AirportData.cancelFlight(flight, this);
    }
}
