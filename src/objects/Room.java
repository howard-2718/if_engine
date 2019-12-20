package objects;

import java.util.ArrayList;

public class Room {
    private String name;
    private String desc;
    private ArrayList<Obj> objects;
    private String[][] connections;

    public Room(String name, String desc, ArrayList<Obj> objects, String[][] connections) {
        this.name = name;
        this.desc = desc;
        this.objects = objects;
        this.connections = connections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<Obj> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<Obj> objects) {
        this.objects = objects;
    }

    public String[][] getConnections() {
        return connections;
    }

    public void setConnections(String[][] connections) {
        this.connections = connections;
    }

    public String fullDesc() {
        return name + "\n\n" + desc;
    }
}
