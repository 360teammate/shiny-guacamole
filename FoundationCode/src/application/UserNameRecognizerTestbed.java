package application;

import java.util.Scanner;

/**
 * A console-based demonstration for the new UserNameRecognizer FSM.
 */
public class UserNameRecognizerTestbed {

    public static void main(String[] args) {
        System.out.println("Welcome to the New UserName Recognizer Testbed\n");
        System.out.println("Please enter a UserName or an empty line to stop.");

        Scanner keyboard = new Scanner(System.in);

        while (true) {
            String inputLine = keyboard.nextLine();
            if (inputLine.isEmpty()) {
                System.out.println("\n*** Empty input line detected, stopping.");
                keyboard.close();
                System.exit(0);
            }
            String errMessage = UserNameRecognizer.checkForValidUserName(inputLine);
            if (!errMessage.isEmpty()) {
                System.out.println(errMessage);
                if (UserNameRecognizer.userNameRecognizerIndexofError >= 0
                        && UserNameRecognizer.userNameRecognizerIndexofError < inputLine.length()) {
                    System.out.println(inputLine);
                    System.out.println(inputLine.substring(0, UserNameRecognizer.userNameRecognizerIndexofError) + "\u21EB");
                }
            } else {
                System.out.println("Success! The UserName \"" + inputLine + "\" is valid.");
            }
            System.out.println("\nEnter another UserName or an empty line to quit:");
        }
    }
}
