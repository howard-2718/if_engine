package objects;

import java.util.ArrayList;
import java.util.HashMap;

public class Room {
    private String name;
    private String desc;
    private ArrayList<Obj> objects;
    private HashMap<String, String> connections;

    public Room(String name, String desc, ArrayList<Obj> objects, HashMap<String, String> connections) {
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

    public void appendObject(Obj obj) {
        this.objects.add(obj);
    }

    public HashMap<String, String> getConnections() {
        return connections;
    }

    public void setConnections(HashMap<String, String> connections) {
        this.connections = connections;
    }

    // The end of desc should lead into the listing of the room's objects.
    public String fullDesc() {
        StringBuilder output = new StringBuilder(name + "\n\n" + desc);
        Integer i = 1; // Just keeps count

        if(this.objects.size() == 0){
            output.append("nothing.");
        } else {
            for(Obj obj : this.getObjects()) {
                output.append(obj.getRoom_desc());

                if (this.objects.size() - i == 0) {
                    // There is a table.
                    output.append(".");
                } else if (this.objects.size() - i == 1) {
                    if (this.objects.size() == 2) {
                        // There is a table and a painting.
                        output.append(" and ");
                    } else {
                        // There is a table, a painting, and a chair.
                        output.append(", and ");
                    }
                } else {
                    // There is a table, a painting, a chair, and a bottle of wine.
                    output.append(", ");
                }
                ++i;
            }
        }
        return output.toString();
    }
}
