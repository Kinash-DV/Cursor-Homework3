package Models;

import java.util.HashMap;
import java.util.Map;

public class Plane {
    private Integer id;
    private String serialNumber;
    private ModelPlane model;

    private static Map<Integer, Plane> instances = new HashMap<>();

    public Plane(Integer id, String serialNumber, ModelPlane model) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.model = model;
    }

    @Override
    public String toString() {
        return model.toString() + " s.n." + serialNumber + ", id=" + id;
    }

    public Integer getId() {
        return id;
    }

    public ModelPlane getModel() {
        return model;
    }

    public static void saveInstance(Integer id, Plane plane){
        instances.put(id, plane);
    }

    public static Plane getInstance(Integer id){
        return instances.get(id);
    }
}
