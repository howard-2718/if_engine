package objects;

// The first parameter of an object is its type; that then determines the rest of its parameters

public class Obj {
    private String type;
    private String name;
    private String[] alias;
    private String desc;
    private String room_desc;
    private Room taken_from;
    private Dialogue[] dialogue;
    private boolean is_open;
    private String dir;
    private String key;

    public Obj(String name, String[] alias, String desc, String room_desc) {
        // All objects, regardless of type, will have these 4 parameters
        this.type = "Object";
        this.name = name;
        this.alias = alias;
        this.desc = desc;
        this.room_desc = room_desc;
    }

    // Doors are kind of funky because they technically don't exist in a room but rather bridge two rooms together
    public Obj(String name, String[] alias, String desc, String room_desc, String is_open, String dir, String key) {
        this.type = "Door";
        this.name = name;
        this.alias = alias;
        this.desc = desc;
        this.room_desc = room_desc;
        this.is_open = is_open.equals("t");
        this.dir = dir;
        this.key = key;
    }

    public Obj(String name, String[] alias, String desc, String room_desc, Dialogue[] dialogue) {
        this.type = "Character";
        this.name = name;
        this.alias = alias;
        this.desc = desc;
        this.room_desc = room_desc;
        this.dialogue = dialogue;
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

    public boolean isIs_open() {
        return is_open;
    }

    public void setIs_open(boolean is_open) {
        this.is_open = is_open;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Room getTaken_from() {
        return taken_from;
    }

    public void setTaken_from(Room taken_from) {
        this.taken_from = taken_from;
    }

    public Dialogue[] getDialogue() {
        return dialogue;
    }

    public void setDialogue(Dialogue[] dialogue) {
        this.dialogue = dialogue;
    }
}
