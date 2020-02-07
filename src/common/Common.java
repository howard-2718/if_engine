package common;

// Common methods

import objects.Dialogue;
import objects.Obj;
import objects.Room;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Common {

    // Checks to prevent "...s's" from occurring
    public static String apostrophe(String name){
        if("s".equals(name.substring(name.length() - 1))){
            return("'");
        }

        else {
            return("'s");
        }
    }

    // Basically python
    public static String input(String str){
        Scanner in = new Scanner(System.in);
        System.out.print(str);
        return in.nextLine();
    }

    // Parse game file and use the ParseOutput class to return an ArrayList of all rooms and objects in the file
    public static ArrayList<Room> parse_game_file(File file) throws IOException {
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

            st = st.trim();

            if(st.substring(0,4).equals("Room")){
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

            } else if(st.substring(0,6).equals("Object")){
                ++current_obj;

                String[] split = st.split(" @@@ ");
                all_obj.add(new Obj(split[0].substring(7, split[0].length()), split[1].split("; "), split[2].replace("\\n", "n"), split[3].substring(0, split[3].length() - 1)));
                all_room.get(current_room).appendObject(all_obj.get(current_obj));
            } else if(st.substring(0,4).equals("Door")) {
                ++current_obj;

                String[] split = st.split(" @@@ ");
                all_obj.add(new Obj(split[0].substring(5, split[0].length()), split[1].split("; "), split[2].replace("\\n", "n"), split[3], split[4], split[5], split[6].substring(0, split[6].length() - 1)));
                all_room.get(current_room).appendObject(all_obj.get(current_obj));
            } else if(st.substring(0,9).equals("Character")){
                ++current_obj;

                String[] split = st.split(" @@@ ");
                all_obj.add(new Obj(split[0].substring(7, split[0].length()), split[1].split("; "), split[2].replace("\\n", "n"), split[3].substring(0, split[3].length() - 1), new Dialogue[] {}));
                all_room.get(current_room).appendObject(all_obj.get(current_obj));
            }
        }

        return all_room;
    }

}

// Only used in parse_game_file() so two ArrayLists of different classes can be returned
/*final class ParseOutput {
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
}*/
