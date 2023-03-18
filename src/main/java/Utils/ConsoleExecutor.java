package Utils;

import Models.Airport;
import Models.Flight;
import Models.Pilot;
import Models.Plane;

import java.sql.SQLException;
import java.util.Scanner;

public class ConsoleExecutor {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static void sayHello(){
        System.out.println(ANSI_GREEN + "Welcome to the Airport Management System!" + ANSI_RESET);
        System.out.println("You can always get help on usage by typing " +
                ANSI_YELLOW + "/help" + ANSI_RESET);
    }

    public static void sayBye(){
        System.out.println(ANSI_RED + "Sorry. The system is off" + ANSI_RESET);
    }

    public static void runDialog(Airport airport){

        try (Scanner scanner = new Scanner(System.in)){

            while (true){

                switch ( scanner.nextLine() ) {

                    case "/exit" :
                        System.exit(0);
                        break;

                    case "/help":
                        System.out.println("You can type the following commands:" + "\n" +
                                ANSI_YELLOW + "/help" + ANSI_RESET +
                                " - print out of this help\n" +
                                ANSI_YELLOW + "/exit" + ANSI_RESET +
                                " - terminate this program\n"+
                                ANSI_YELLOW + "/airport" + ANSI_RESET +
                                " - get info about the airport\n"+
                                ANSI_YELLOW + "/pilot" + ANSI_RESET +
                                " - get info about the pilot\n"+
                                ANSI_YELLOW + "/new" + ANSI_RESET +
                                " - create a new flight\n"+
                                ANSI_YELLOW + "/finish" + ANSI_RESET +
                                " - complete the flight\n"+
                                ANSI_YELLOW + "/cancel" + ANSI_RESET +
                                " - cancel the flight");
                        break;

                    case "/airport":
                        System.out.println(airport.toString());
                        break;

                    case "/pilot":
                        System.out.print("Enter Pilot ID: ");
                        Integer pilotID = scanner.nextInt();
                        System.out.println(Pilot.getInfoById(pilotID));
                        scanner.nextLine();
                        break;

                    case "/new":
                        System.out.println(ANSI_GREEN + "Starting to create a new flight!" + ANSI_RESET);

                        System.out.print("Type plane ID: ");
                        Integer planeID = scanner.nextInt();
                        Plane plane = Plane.getInstance(planeID);
                        if (plane == null) {
                            System.out.println(ANSI_RED + "Not found plane with ID=" + planeID + ANSI_RESET);
                            scanner.nextLine();
                            break;
                        }
                        String planeWarning = airport.checkPlaneAvailability(plane);
                        if (planeWarning != null) {
                            System.out.println(ANSI_RED + planeWarning + ANSI_RESET);
                            scanner.nextLine();
                            break;
                        }

                        System.out.print("Enter First Pilot ID: ");
                        Integer firstPilotID = scanner.nextInt();
                        Pilot firstPilot = Pilot.getInstance(firstPilotID);
                        if (firstPilot == null) {
                            System.out.println(ANSI_RED + "Not found pilot with ID=" + firstPilotID + ANSI_RESET);
                            scanner.nextLine();
                            break;
                        }
                        String firstPilotWarning = airport.checkPilotAvailability(firstPilot, plane);
                        if (firstPilotWarning != null) {
                            System.out.println(ANSI_RED + firstPilotWarning + ANSI_RESET);
                            scanner.nextLine();
                            break;
                        }

                        System.out.print("Enter Second Pilot ID: ");
                        Integer secondPilotID = scanner.nextInt();
                        Pilot secondPilot = Pilot.getInstance(secondPilotID);
                        if (secondPilot == null) {
                            System.out.println(ANSI_RED + "Not found pilot with ID=" + secondPilotID + ANSI_RESET);
                            scanner.nextLine();
                            break;
                        } else if (secondPilot == firstPilot) {
                            System.out.println(ANSI_RED + "The pilot was already specified as the First" + ANSI_RESET);
                            scanner.nextLine();
                            break;
                        }
                        String secondPilotWarning = airport.checkPilotAvailability(secondPilot, plane);
                        if (secondPilotWarning != null) {
                            System.out.println(ANSI_RED + secondPilotWarning + ANSI_RESET);
                            scanner.nextLine();
                            break;
                        }

                        airport.createNewFlight(plane, firstPilot, secondPilot);

                        scanner.nextLine();
                        break;

                    case "/finish":
                        System.out.print("Enter flight number to finish: ");
                        Integer flightID = scanner.nextInt();
                        Flight flight = Flight.getInstance(flightID);
                        if (flight == null) {
                            System.out.println(ANSI_RED + "Not found flight with ID=" + flightID + ANSI_RESET);
                            scanner.nextLine();
                            break;
                        } else if (flight.getFinished()) {
                            System.out.println(ANSI_RED + "The pilot was already finished" + ANSI_RESET);
                            scanner.nextLine();
                            break;
                        }

                        airport.finishFlight(flight);

                        scanner.nextLine();
                        break;

                    case "/cancel":
                        System.out.print("Enter flight number to cancel: ");
                        Integer canceledFlightID = scanner.nextInt();
                        Flight canceledFlight = Flight.getInstance(canceledFlightID);
                        if (canceledFlight == null) {
                            System.out.println(ANSI_RED + "Not found flight with ID=" + canceledFlightID + ANSI_RESET);
                            scanner.nextLine();
                            break;
                        } else if (canceledFlight.getFinished()) {
                            System.out.println(ANSI_RED + "The pilot was already finished" + ANSI_RESET);
                            scanner.nextLine();
                            break;
                        }

                        airport.cancelFlight(canceledFlight);

                        scanner.nextLine();
                        break;

                    default:
                        System.out.println(ANSI_RED + "Unknown command. " + ANSI_RESET +
                                "Use " + ANSI_YELLOW + "/help" + ANSI_RESET);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
