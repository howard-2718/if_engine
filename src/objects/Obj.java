package objects;

// The first parameter of an object is its type; that then determines the rest of its parameters

public class Obj {
    private String type;
    private String name;
    private String[] alias;
    private String desc;
    private String room_desc;
    private Room taken_from;

    public Obj(String name, String[] alias, String desc, String room_desc) {
        // All objects, regardless of type, will have these 4 parameters
        this.type = "Object";
        this.name = name;
        this.alias = alias;
        this.desc = desc;
        this.room_desc = room_desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getAlias() {
        return alias;
    }

    public void setAlias(String[] alias) {
        this.alias = alias;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRoom_desc() {
        return room_desc;
    }

    public void setRoom_desc(String room_desc) {
        this.room_desc = room_desc;
    }

    public Room getTaken_from() {
        return taken_from;
    }

    public void setTaken_from(Room taken_from) {
        this.taken_from = taken_from;
    }
}
