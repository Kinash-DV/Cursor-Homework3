package JDBC;

import Models.*;
import Utils.MsSqlConnection;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AirportData {
    private AirportData(){
    }

    public static void LoadAirport(Airport airport){
        List<ModelPlane> modelPlanes = new ArrayList<>();
        List<Plane> planes = new ArrayList<>();
        List<Pilot> pilots = new ArrayList<>();
        List<Flight> currentFlights = new ArrayList<>();

        try (Connection connection = MsSqlConnection.getConnection();
            Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("SELECT Id, Name, Seats FROM ModelPlanes");
            while (resultSet.next()){
                ModelPlane modelPlane = new ModelPlane(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3)
                );
                ModelPlane.saveInstance(modelPlane.getId(), modelPlane);
                modelPlanes.add(modelPlane);
            }
            resultSet.close();

            resultSet = statement.executeQuery("SELECT Id, SerialNumber, Model FROM Planes");
            while (resultSet.next()){
                Plane plane = new Plane(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        ModelPlane.getInstance( resultSet.getString(3))
                );
                Plane.saveInstance(plane.getId(), plane);
                planes.add(plane);
            }
            resultSet.close();

            resultSet = statement.executeQuery("SELECT Id, Name, Age FROM Pilots");
            while (resultSet.next()){
                Pilot pilot = new Pilot(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3)
                );
                Pilot.saveInstance(pilot.getId(), pilot);
                pilots.add(pilot);
            }
            resultSet.close();

            resultSet = statement.executeQuery("SELECT Model, Pilot FROM ModelPlanesPilots");
            while (resultSet.next()){
                ModelPlane modelPlane = ModelPlane.getInstance(resultSet.getString(1));
                Pilot pilot = Pilot.getInstance(resultSet.getInt(2));
                pilot.addModelPlane(modelPlane);
            }
            resultSet.close();

            resultSet = statement.executeQuery(
                    "SELECT Id, Plane, Pilot1, Pilot2 FROM FlightHistory WHERE Finished=0");
            while (resultSet.next()){
                Flight flight = new Flight(
                        resultSet.getInt(1),
                        Plane.getInstance(resultSet.getInt(2)),
                        Pilot.getInstance(resultSet.getInt(3)),
                        Pilot.getInstance(resultSet.getInt(4))
                );
                Flight.saveInstance(flight.getId(), flight);
                currentFlights.add(flight);
            }
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        airport.setModelPlanes(modelPlanes);
        airport.setPlanes(planes);
        airport.setPilots(pilots);
        airport.setCurrentFlights(currentFlights);
    }

    public static Flight createFlight(Plane plane, Pilot firstPilot, Pilot secondPilot, Airport airport) throws SQLException {

        try (Connection connection = MsSqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO FlightHistory (Plane, Pilot1, Pilot2, Finished) " +
                        "VALUES (?, ?, ?, 0)",
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, plane.getId());
            statement.setInt(2, firstPilot.getId());
            statement.setInt(3, secondPilot.getId());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (! resultSet.next()){
                throw new RuntimeException("The record was not written in the database");
            }

            Integer newID = resultSet.getInt(1);

            Flight newFlight = new Flight(newID, plane, firstPilot, secondPilot);
            Flight.saveInstance(newID, newFlight);
            airport.addCurrentFlight(newFlight);

            return newFlight;

        } catch (SQLException e) {
            throw e;
        }

    }

    public static void finishFlight(Flight flight, Airport airport) throws SQLException {

        try (Connection connection = MsSqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE FlightHistory SET Finished = 1 WHERE id = ?" )) {

            statement.setInt(1, flight.getId());
            if (statement.executeUpdate() == 0){
                throw new RuntimeException("Flight record was not found");
            }

            flight.setFinished(true);
            airport.deleteCurrentFlight(flight);

        } catch (SQLException e) {
            throw e;
        }
    }

    public static void cancelFlight(Flight flight, Airport airport) throws SQLException {

        try (Connection connection = MsSqlConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM FlightHistory WHERE id = ?" )) {

            statement.setInt(1, flight.getId());
            if (statement.executeUpdate() == 0){
                throw new RuntimeException("Flight record was not found");
            }

            Flight.saveInstance(flight.getId(), null);
            airport.deleteCurrentFlight(flight);

        } catch (SQLException e) {
            throw e;
        }
    }
}
