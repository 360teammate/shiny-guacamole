package application;

/***
 * Title: UserNameRecognizer Class
 *
 * Description: A Finite State Machine that:
 *   1) Requires the first character be alphabetic (A–Z, a–z).
 *   2) Subsequent characters can be alphanumeric, underscore (_), minus (-), or period (.).
 *   3) If underscore, minus, or period is used, the next character MUST be alphanumeric
 *      (i.e., no consecutive underscores or special symbols, and it can't be at the end).
 *   4) The total length must be between 4 and 16 inclusive.
 *
 * This code rejects inputs like "abc__def", because consecutive underscores mean the first
 * underscore is not directly followed by an alphanumeric character.
 *
 * @version 2025-01-30
 */
public class UserNameRecognizer {

    public static String userNameRecognizerErrorMessage = "";
    public static String userNameRecognizerInput = "";
    public static int userNameRecognizerIndexofError = -1;

    private static int state = 0;
    private static int nextState = 0;
    private static boolean running;
    private static String inputLine;
    private static char currentChar;
    private static int currentCharNdx;
    private static int userNameSize;

    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 16;

    public static String checkForValidUserName(String input) {

        if (input.isEmpty()) {
            userNameRecognizerIndexofError = 0;
            return "* ERROR * The userName is empty. Must start with alpha and be 4–16 chars.";
        }

        state = 0;
        nextState = -1;
        running = true;
        userNameSize = 0;

        inputLine = input;
        userNameRecognizerInput = input;
        userNameRecognizerIndexofError = -1;

        currentCharNdx = 0;
        currentChar = input.charAt(0);

        while (running) {
            switch (state) {
                case 0:
                    if (Character.isLetter(currentChar)) {
                        nextState = 1;
                        userNameSize++;
                    } else {
                        running = false;
                    }
                    break;

                case 1:
                    if (Character.isLetterOrDigit(currentChar)) {
                        nextState = 1;
                        userNameSize++;
                    }
                    else if (currentChar == '_' || currentChar == '-' || currentChar == '.') {
                        nextState = 2;
                        userNameSize++;
                    }
                    else {
                        running = false;
                    }
                    if (userNameSize > MAX_LENGTH) {
                        running = false;
                    }
                    break;

                case 2:
                    if (Character.isLetterOrDigit(currentChar)) {
                        nextState = 1;
                        userNameSize++;
                    } else {
                        running = false;
                    }
                    if (userNameSize > MAX_LENGTH) {
                        running = false;
                    }
                    break;
            }

            if (running) {
                moveToNextChar();
                state = nextState;
            }
        }

        userNameRecognizerIndexofError = currentCharNdx;

        if (state == 0) {
            return buildErr("First character must be alphabetic (A–Z/a–z).");
        } else if (state == 1 || state == 2) {
            if (currentCharNdx < inputLine.length()) {
                return buildErr("Invalid char encountered: '" + currentChar + "'");
            }
            if (userNameSize < MIN_LENGTH) {
                return buildErr("UserName too short! Must be >= " + MIN_LENGTH + " chars.");
            } else if (userNameSize > MAX_LENGTH) {
                return buildErr("UserName too long! Must be <= " + MAX_LENGTH + " chars.");
            }
            return "";
        }
        return buildErr("Invalid userName format encountered.");
    }

    private static void moveToNextChar() {
        currentCharNdx++;
        if (currentCharNdx < inputLine.length()) {
            currentChar = inputLine.charAt(currentCharNdx);
        } else {
            currentChar = ' ';
            running = false;
        }
    }

    private static String buildErr(String msg) {
        userNameRecognizerErrorMessage = "* ERROR * " + msg;
        return userNameRecognizerErrorMessage;
    }
}
