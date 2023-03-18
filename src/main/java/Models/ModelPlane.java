package Models;

import java.util.HashMap;
import java.util.Map;

public class ModelPlane {
    private String id;
    private String name;
    private Integer seats;

    private static Map<String, ModelPlane> instances = new HashMap<>();

    public ModelPlane(String id, String name, Integer seats) {
        this.id = id;
        this.name = name;
        this.seats = seats;
    }
    @Override
    public String toString() {
        return name + " (" + seats + ')';
    }

    public String getId() {
        return id;
    }

    public static void saveInstance(String id, ModelPlane model){
        instances.put(id, model);
    }

    public static ModelPlane getInstance(String id){
        return instances.get(id);
    }
}
