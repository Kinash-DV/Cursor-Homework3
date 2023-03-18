package Models;

import java.util.HashMap;
import java.util.Map;

public class Flight {
    private Integer id;
    private Plane plane;
    private Pilot pilot1;
    private Pilot pilot2;
    private Boolean finished = false;

    private static Map<Integer, Flight> instances = new HashMap<>();

    public Flight(Integer id, Plane plane, Pilot pilot1, Pilot pilot2) {
        this.id = id;
        this.plane = plane;
        this.pilot1 = pilot1;
        this.pilot2 = pilot2;
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", plane=" + plane +
                ", pilot1=" + pilot1.getName() +
                ", pilot2=" + pilot2.getName() ;
    }

    public Integer getId() {
        return id;
    }

    public Plane getPlane() {
        return plane;
    }

    public Pilot getPilot1() {
        return pilot1;
    }

    public Pilot getPilot2() {
        return pilot2;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public static void saveInstance(Integer id, Flight flight){
        instances.put(id, flight);
    }

    public static Flight getInstance(Integer id){
        return instances.get(id);
    }
}
