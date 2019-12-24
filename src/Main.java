import objects.*;
import common.Common;

import java.net.URL;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Objects;
import java.io.*;

public class Main {

    private static URL url = Main.class.getResource("game.txt");
    private static File game_file = new File(url.getPath());

    private static ParseOutput temp;

    static {
        try {
            temp = parse_game_file(game_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Obj> all_obj = temp.getObj_list();
    private static ArrayList<Room> all_room = temp.getRoom_list();

    private static Room player_room = all_room.get(0);
    private static ArrayList<Obj> player_inv = new ArrayList<>();

    public static void main(String[] args){

        System.out.println(player_room.fullDesc());

        int cont = game();

        while(cont != 1){
            cont = game();
        }
    }

    private static int game(){
        String choice = Common.input().toLowerCase();

        String[] c = choice.split("\\s+");

        switch (c[0]){
            case "examine":
            case "e":
                if(c.length < 2) {
                    System.out.println("What would you like to examine?");
                }
                else {
                    if(c[1].equals("room")) {
                        // 'examine room' prints out the room description
                        System.out.println(player_room.fullDesc());
                    } else {
                        boolean match = false;

                        // Remove 'examine' and concatenate the rest of the input
                        String str = String.join(" ", Arrays.copyOfRange(c, 1, c.length));

                        if(obj_in_room(str, player_room) != null){
                            System.out.println(Objects.requireNonNull(obj_in_room(str, player_room)).getDesc());
                            match = true;
                        }

                        // If the player's input doesn't correspond to any object
                        if(!match){
                            System.out.println("There is no such thing in this room!");
                        }
                    }
                }
                return(0);

            case "take":
                if(c.length < 2) {
                    System.out.println("What would you like to take?");
                } else {
                    boolean match = false;

                    // Remove 'examine' and concatenate the rest of the input
                    String str = String.join(" ", Arrays.copyOfRange(c, 1, c.length));

                    if(obj_in_room(str, player_room) != null){

                        Obj obj = Objects.requireNonNull(obj_in_room(str, player_room));

                        System.out.println("Took the " + obj.getName().toLowerCase() + ".");

                        // Set the room the object was taken from
                        obj.setTaken_from(player_room);

                        // Update the contents of the room
                        ArrayList<Obj> new_objects = player_room.getObjects();
                        new_objects.remove(obj);
                        player_room.setObjects(new_objects);

                        // Add to player's inventory
                        player_inv.add(obj);

                        match = true;
                    }

                    if(!match){
                        System.out.println("There is no such thing in this room!");
                    }
                }
                return(0);

            case "inventory":
            case "inv":
                // Ahh, nice and simple.
                System.out.println("Inventory:");

                int max_char = 0;

                // Find the longest item name-wise in the inventory
                for(Obj obj : player_inv){
                    if(obj.getName().length() > max_char){ max_char = obj.getName().length(); }
                }

                // Space out the other items such that it looks nice
                for(Obj obj : player_inv){
                    StringBuilder spaces = new StringBuilder();
                    for(int i = obj.getName().length(); i < max_char; ++i){
                        spaces.append(" ");
                    }
                    System.out.println("  " + obj.getName() + spaces.toString() + "   - taken from " + obj.getTaken_from().getName());
                }
                return(0);

            case "move":
            case "walk":
                if(c.length < 2) {
                    System.out.println("Which direction would you like to move in?");
                } else {
                    boolean match = false;
                    String inp = c[1].toLowerCase();

                    // Genius coding
                    for(String[] dir : player_room.getConnections()){
                        if(inp.equals(dir[0])){
                            for(Room room : all_room){
                                if(room.getName().equals(dir[1])){
                                    match = true;
                                    player_room = room;
                                    System.out.println(player_room.fullDesc());
                                }
                            }
                        }
                    }

                    if(!match){
                        System.out.println("That is not a valid movement direction!");
                    }
                }
                return(0);

            case "stop":
            case "quit":
                System.out.println("Game stopped.");
                return(1);

            default:
                System.out.println("Invalid action!");
                return(1);
        }
    }

    // Takes a string and a room, and then determines if the object with that string as a name/alias is in said room
    private static Obj obj_in_room(String str, Room room){
        for(Obj obj : all_obj){
            // Does the object refer to an existing object?
            if(str.toLowerCase().equals(obj.getName().toLowerCase()) || Arrays.asList(obj.getAlias()).contains(str)) {
                // Is it in the room?
                if(room.getObjects().contains(obj)) {
                    // It is! Return the object.
                    return obj;
                }
            }
        }
        return null;
    }

    /*
    // Creates all_obj
    private static ArrayList<Obj> all_obj(){
        return new ArrayList<>(Arrays.asList(
                new Obj("Wooden table", new String[] {"table", "wood table"}, "It's a plain, wooden table. Looks like it's made out of oak.\nOr maybe spruce?", "a table"),
                new Obj("Painting", new String[] {}, "You examine the painting.\nIt's some abstract post-modernist art that you don't really get, but it looks nice.", "a painting on the wall"),
                new Obj("Bottle of expensive-looking wine", new String[] {"bottle", "wine bottle", "booze", "bottle of wine"},"it's a bottle of wine, what more do i need to say", "a bottle of expensive-looking wine")
        ));
    }

    // Creates all_room
    private static ArrayList<Room> all_room(){
        return new ArrayList<>(Arrays.asList(
                new Room("Living Room", "The living room is poorly furnished; in fact, the only thing here to see is ", new ArrayList<>(Arrays.asList(all_obj.get(0), all_obj.get(1))), new String[][] {{"left", "Kitchen"}}),
                new Room("Kitchen", "The kitchen isn't a big step up in looks either. There's a fridge, a stove, some cupboards and an island.", new ArrayList<>(), new String[][] {{"right", "Living Room"}})
        ));
    }*/

    private static ParseOutput parse_game_file(File file) throws IOException {
        // Initialize ArrayLists
        ArrayList<Room> all_room = new ArrayList<>();
        ArrayList<Obj> all_obj = new ArrayList<>();

        // File reading shenanigans

        BufferedReader br = new BufferedReader(new FileReader(file));

        int current_room = -1;
        int current_obj = -1;

        String st;
        while ((st = br.readLine()) != null){
            // If the line begins with "- Room"
            // " @@@ " is the delimiter
            if(st.trim().substring(0,6).equals("- Room")){
                ++current_room;

                String[] split = st.split(" @@@ ");
                all_room.add(new Room(split[0].substring(7, split[0].length()), split[1], new ArrayList<>(), new String[][] {{"left", "Kitchen"}}));

            } else if(st.trim().substring(0,8).equals("- Object")){
                ++current_obj;

                String[] split = st.split(" @@@ ");
                all_obj.add(new Obj(split[0].substring(9, split[0].length()), split[1].split("; "), split[2], split[3].substring(0, split[3].length() - 1)));
                all_room.get(current_room).appendObject(all_obj.get(current_obj));
            }
        }

        return new ParseOutput(all_room, all_obj);
    }

}

// Only used in parse_game_file() so two ArrayLists of different classes could be returned
final class ParseOutput {
    private final ArrayList<Room> room_list;
    private final ArrayList<Obj> obj_list;

    ParseOutput(ArrayList<Room> room_list, ArrayList<Obj> obj_list){
        this.room_list = room_list;
        this.obj_list = obj_list;
    }

    ArrayList<Room> getRoom_list() {
        return room_list;
    }

    ArrayList<Obj> getObj_list() {
        return obj_list;
    }
}
