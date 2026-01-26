package ra.validation;

import java.util.Scanner;

public class Validator {
    public static String getString(Scanner sc, String s){
        String input = "";
        try{
            System.out.println(s);
            input = sc.nextLine();
            if (input.isEmpty()){
                System.out.println("Không được để trống");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return input;
    }
}
