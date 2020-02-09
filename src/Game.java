import common.Common;
import objects.Obj;
import objects.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

// The game loop

class Game {
    private ArrayList<Room> all_room;
    private Room player_room;
    private ArrayList<Obj> player_inv;

    Game(ArrayList<Room> all_room, Room player_room, ArrayList<Obj> player_inv) {
        this.all_room = all_room;
        this.player_room = player_room;
        this.player_inv = player_inv;

        int cont = game_loop();

        while(cont != 1){
            cont = game_loop();
        }
    }

    private int game_loop(){
        String choice = Common.input("\n> ").toLowerCase();

        String[] c = choice.split("\\s+");

        switch (c[0]){
            case "examine":
            case "e":
                examine(c);
                return(0);

            case "take":
                take(c);
                return(0);

            case "inventory":
            case "inv":
                inventory();
                return(0);

            case "move":
            case "walk":
                move(c);
                return(0);

            case "use":
                use(c);
                return(0);

            case "stop":
            case "quit":
                System.out.println("Game stopped.");
                return(1);

            default:
                System.out.println("Invalid action!");
                return(0);
        }
    }

    private void examine(String[] c){
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

                    if (obj_in_room(str, player_room)) {
                        System.out.println(Objects.requireNonNull(obj_from_string(str, player_room)).getDesc());
                        match = true;
                    }

                    // If the player's input doesn't correspond to any object
                    if (!match) {
                        System.out.println("There is no such thing in this room!");
                    }
                    break;
            }
        }
    }

    private void take(String[] c){
        if(c.length < 2) {
            System.out.println("What would you like to take?");
        } else {
            boolean match = false;

            // Remove 'examine' and concatenate the rest of the input
            String str = String.join(" ", Arrays.copyOfRange(c, 1, c.length));

            if(obj_in_room(str, player_room)){

                Obj obj = Objects.requireNonNull(obj_from_string(str, player_room));

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
    }

    private void inventory(){
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
            System.out.println("  " + obj.getName() + spaces.toString() + " - taken from " + obj.getTaken_from().getName());
        }
    }

    private void move(String[] c){
        if(c.length < 2) {
            System.out.println("Which direction would you like to move in?");
        } else {
            boolean match = false;
            String inp = c[1];

            // For each existing connection the room has, get the direction
            for(String dir : player_room.getConnections().keySet()){
                if(inp.equals(dir.toLowerCase())){

                    String dest = player_room.getConnections().get(dir).toLowerCase();

                    // Door processing
                    for(Obj obj : player_room.getObjects()){
                        // Is there a door?
                        if(obj.getType().equals("Door")){
                            // Is the door in the way?
                            if(obj.getDir().equals(dir)){
                                // Is said door open?
                                if(obj.isIs_open()){
                                    // Successfully made it past door processing
                                    for(Room room : all_room){
                                        if(room.getName().toLowerCase().equals(dest)){
                                            // This will probably break if there are two rooms of the same name
                                            match = true;
                                            player_room = room;
                                            System.out.println(player_room.fullDesc());
                                        }
                                    }
                                } else {
                                    // Didn't make it through door processing
                                    match = true;
                                    System.out.println("You can't; something is in the way.");
                                }
                            }
                        }
                    }


                }
            }

            if(!match){
                System.out.println("That is not a valid movement direction!");
            }
        }
    }

    private void use(String[] c){
        if(c.length < 2) {
            System.out.println("What would you like to use?");
        } else if(c.length < 4) {
            System.out.println("What would you like to use that item on?");
        } else {
            // Check to see if c[1] is in the player's inventory, and if c[3] is in the room
            if((obj_in_inv(c[1])) && (obj_in_room(c[3], player_room))){
                // It is!
                Obj door = Objects.requireNonNull(obj_from_string(c[3], player_room));
                Obj item = Objects.requireNonNull(obj_from_inv(c[1]));

                if(item.getName().toLowerCase().equals(door.getKey().toLowerCase())){
                    door.setIs_open(true);
                    player_inv.remove(item);
                    System.out.println("the door is open");
                } else {
                    System.out.println("Nothing happened.");
                }

            }
        }
    }

    // Takes a string and a room, and then determines if the object with that string as a name/alias is in said room
    private boolean obj_in_room(String str, Room room){
        for(Obj obj : room.getObjects()){
            // Does the input refer to an existing object?
            if(str.toLowerCase().equals(obj.getName().toLowerCase()) || Arrays.asList(obj.getAlias()).contains(str)) {
                return true;
            }
        }

        // It isn't! Return false.
        return false;
    }

    private Obj obj_from_string(String str, Room room){
        for(Obj obj : room.getObjects()){
            // Does the input refer to an existing object?
            if(str.toLowerCase().equals(obj.getName().toLowerCase()) || Arrays.asList(obj.getAlias()).contains(str)) {
                return obj;
            }
        }
        return null;
    }

    private boolean obj_in_inv(String str){
        for(Obj obj : player_inv){
            // Does the input refer to an existing object?
            if(str.toLowerCase().equals(obj.getName().toLowerCase()) || Arrays.asList(obj.getAlias()).contains(str)) {
                return true;
            }
        }
        return false;
    }

    private Obj obj_from_inv(String str){
        for(Obj obj : player_inv){
            // Does the input refer to an existing object?
            if(str.toLowerCase().equals(obj.getName().toLowerCase()) || Arrays.asList(obj.getAlias()).contains(str)) {
                return obj;
            }
        }
        return null;
    }
}
