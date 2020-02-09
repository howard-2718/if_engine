import objects.*;
import common.Common;

import java.net.URL;
import java.util.ArrayList;

import java.io.*;

public class Main {

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

    private static ArrayList<Room> all_room;

    // The game loop
    private static void exec_game(){
        // Ask for filename
        // Need to check for the existence of the file
        String name = Common.input("Please enter the filename (without the extension!).\n\n> ");
        URL url = Main.class.getResource(name + ".txt");
        File game_file = new File(url.getPath());

        // Parse game file
        try {
            all_room = Common.parse_game_file(game_file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Give values to all those variables that were null
        Room player_room = all_room.get(0);
        ArrayList<Obj> player_inv = new ArrayList<>();

        // Print the description of the first room the player finds themselves in
        System.out.println(player_room.fullDesc());

        new Game(all_room, player_room, player_inv);
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
}

/*
    EVENT SYSTEM

    so obviously things are going to happen and properties will change so note to self: something like

    Event(1, param1, param2)

   where the first integer is like the type of event and then the params are whats involved
   so like Event(1, obj, desc) could be like changing the description of an object)
*/