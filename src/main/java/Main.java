import Models.Airport;
import Utils.ConsoleExecutor;

public class Main {
    public static void main(String[] args) {

        ConsoleExecutor.sayHello();

        Airport airport = new Airport();
        if (!airport.getConnected()) {
            ConsoleExecutor.sayBye();
            System.exit(0);
        }

        ConsoleExecutor.runDialog(airport);
    }
}