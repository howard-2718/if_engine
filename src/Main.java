import objects.*;
import common.Common;

import java.net.URL;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.io.*;

public class Main {

    private static URL url;
    private static File game_file;

    private static ParseOutput temp;

    // all_obj is not used currently, may be able to just completely phase it out
    private static ArrayList<Obj> all_obj;
    private static ArrayList<Room> all_room;

    private static Room player_room;
    private static ArrayList<Obj> player_inv;

    public static void main(String[] args){

        // Upon starting, ask the player if they would like to play or edit.
        boolean decision = false;

        // Keep looping the choice until a valid option is selected.
        while(!decision) {
            String play_or_edit = Common.input("Welcome to the IF engine interface.\nWould you like to PLAY or EDIT?" +
                    "\n\n1. Play\n2. Edit (Will open another window)\n\n> ").toLowerCase();

            switch (play_or_edit) {
                case "1":
                case "play":
                    decision = true;
                    exec_game();
                    break;
                case "2":
                case "edit":
                    decision = true;
                    exec_editor();
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

/////////////////////////////////////////////////////////////////
//*************************************************************//
//*********************** THE PLAYER **************************//
//*************************************************************//
/////////////////////////////////////////////////////////////////

    // The game loop
    private static void exec_game(){
        // Ask for filename
        String name = Common.input("Please enter the filename (without the extension!).\n\n> ");
        url = Main.class.getResource(name + ".txt");
        game_file = new File(url.getPath());

        // Parse game file
        try {
            temp = parse_game_file(game_file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Give values to all those variables that were null
        all_obj = temp.getObj_list();
        all_room = temp.getRoom_list();

        player_room = all_room.get(0);
        player_inv = new ArrayList<>();

        // Print the description of the first room the player finds themselves in
        System.out.println(player_room.fullDesc());

        int cont = game();

        // Keep calling game() until it returns a 1, indicating that the game has been exited.
        while(cont != 1){
            cont = game();
        }
    }

    // It'd probably be best to move this into another file
    private static int game(){
        String choice = Common.input("\n> ").toLowerCase();

        String[] c = choice.split("\\s+");

        switch (c[0]){
            case "examine":
            case "e":
                if(c.length < 2) {
                    System.out.println("What would you like to examine?");
                }
                else {
                    switch (c[1]) {
                        case "room":
                            // 'examine room' prints out the room description
                            System.out.println(player_room.fullDesc());
                            break;
                        case "self":
                            System.out.println("I'll leave that up to your imagination.");
                            break;
                        default:
                            boolean match = false;

                            // Remove 'examine' and concatenate the rest of the input
                            String str = String.join(" ", Arrays.copyOfRange(c, 1, c.length));

                            if (obj_in_room(str, player_room) != null) {
                                System.out.println(Objects.requireNonNull(obj_in_room(str, player_room)).getDesc());
                                match = true;
                            }

                            // If the player's input doesn't correspond to any object
                            if (!match) {
                                System.out.println("There is no such thing in this room!");
                            }
                            break;
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

                    for(String dir : player_room.getConnections().keySet()){
                        if(inp.equals(dir.toLowerCase())){

                            String dest = player_room.getConnections().get(dir).toLowerCase();

                            for(Room room : all_room){
                                if(room.getName().toLowerCase().equals(dest)){
                                    // This will probably break if there are two rooms of the same name
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
        for(Obj obj : room.getObjects()){
            // Does the object refer to an existing object?
            if(str.toLowerCase().equals(obj.getName().toLowerCase()) || Arrays.asList(obj.getAlias()).contains(str)) {
                // Is it in the room?
                if(room.getObjects().contains(obj)) {
                    // It is! Return the object.
                    return obj;
                }
            }
        }
        // It isn't! Return null.
        return null;
    }

/////////////////////////////////////////////////////////////////
//*************************************************************//
//*********************** THE EDITOR **************************//
//*************************************************************//
/////////////////////////////////////////////////////////////////

    private static void exec_editor(){
        // Create an instance of the Editor class
        new Editor();
    }

/////////////////////////////////////////////////////////////////
//*************************************************************//
//********************** MISCELLANEOUS ************************//
//*************************************************************//
/////////////////////////////////////////////////////////////////

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
            if(st.trim().substring(0,4).equals("Room")){
                ++current_room;

                // Splitting the connections
                String[] split = st.split(" @@@ ");
                String[] dir_split = split[2].split(" @@* ");

                HashMap<String, String> connections = new HashMap<>();

                // Appending the connections to the HashMap 'connections'
                for(String dir : dir_split){
                    String[] parts = dir.split(";");
                    connections.put(parts[0].trim(), parts[1].substring(0, parts[1].length() - 1).trim());
                }

                all_room.add(new Room(split[0].substring(5, split[0].length()), split[1].replace("\\n", "n"), new ArrayList<>(), connections));

            } else if(st.trim().substring(0,6).equals("Object")){
                ++current_obj;

                String[] split = st.split(" @@@ ");
                all_obj.add(new Obj(split[0].substring(7, split[0].length()), split[1].split("; "), split[2].replace("\\n", "n"), split[3].substring(0, split[3].length() - 1)));
                all_room.get(current_room).appendObject(all_obj.get(current_obj));
            } else if(st.trim().substring(0,9).equals("Character")){
                ++current_obj;

                String[] split = st.split(" @@@ ");
                all_obj.add(new Obj(split[0].substring(7, split[0].length()), split[1].split("; "), split[2].replace("\\n", "n"), split[3].substring(0, split[3].length() - 1), new Dialogue[] {}));
                all_room.get(current_room).appendObject(all_obj.get(current_obj));
            }
        }

        return new ParseOutput(all_room, all_obj);
    }

}

// Only used in parse_game_file() so two ArrayLists of different classes can be returned
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

/*
    EVENT SYSTEM

    so obviously things are going to happen and properties will change so note to self: something like

    Event(1, param1, param2)

   where the first integer is like the type of event and then the params are whats involved
   so like Event(1, obj, desc) could be like changing the description of an object)
*/