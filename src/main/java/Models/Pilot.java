package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pilot {
    private Integer id;
    private String name;
    private Integer age;
    private List<ModelPlane> modelPlanes;

    private static Map<Integer, Pilot> instances = new HashMap<>();

    public Pilot(Integer id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
        modelPlanes = new ArrayList<>();
    }

    @Override
    public String toString() {
        return name + ", id=" + id.toString();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public List<ModelPlane> getModelPlanes() {
        return modelPlanes;
    }

    public void addModelPlane(ModelPlane modelPlane){
        if (! modelPlanes.contains(modelPlane))
            modelPlanes.add(modelPlane);
    }

    public static String getInfoById(Integer id){
        Pilot pilot = getInstance(id);
        if (pilot == null){
            return "NOT FOUND";
        } else {
            return "name: " + pilot.getName() + "\n" +
                    "age: " + pilot.getAge() + "\n" +
                    "can fly following models: " + pilot.getModelPlanes();
        }
    }

    public static void saveInstance(Integer id, Pilot pilot){
        instances.put(id, pilot);
    }

    public static Pilot getInstance(Integer id){
        return instances.get(id);
    }
}
