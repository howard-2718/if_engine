package common;

// Common methods

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

}
