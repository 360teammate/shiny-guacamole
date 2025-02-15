package application;

/*******
 * <p> Title: PasswordEvaluation Class. </p>
 *
 * <p> Description: A demonstration of the mechanical translation of Directed Graph
 * diagram into an executable Java program using the Password Evaluator Directed Graph. </p>
 *
 * <p> Updated to limit passwords to 12 characters. Error messages are more descriptive. </p>
 *
 */
public class PasswordEvaluator {

    public static String passwordErrorMessage = "";    // The error message text
    public static String passwordInput = "";           // The input being processed
    public static int passwordIndexofError = -1;       // The index where the error was located

    public static boolean foundUpperCase = false;
    public static boolean foundLowerCase = false;
    public static boolean foundNumericDigit = false;
    public static boolean foundSpecialChar = false;
    public static boolean foundLongEnough = false;
    public static boolean withinMaxLength = true;      

    private static String inputLine = "";
    private static char currentChar;
    private static int currentCharNdx;
    private static boolean running;

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 12;  // Example limit from your snippet
    private static final String SPECIAL_CHARS = "~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/";

    public static String evaluatePassword(String input) {
        // Initialize
        passwordErrorMessage = "";
        passwordIndexofError = 0;
        inputLine = input;
        currentCharNdx = 0;
        passwordInput = input;

        foundUpperCase = false;
        foundLowerCase = false;
        foundNumericDigit = false;
        foundSpecialChar = false;
        foundLongEnough = false;
        withinMaxLength = true;
        running = true;

        // 1) Check empty
        if (input.length() == 0) {
            return "*** ERROR *** The password is empty! Please enter a non-empty password.";
        }

        // 2) Check length > 12
        if (input.length() > MAX_LENGTH) {
            withinMaxLength = false;
            return "*** ERROR *** The password exceeds " + MAX_LENGTH + " characters! Please shorten it.";
        }

        currentChar = inputLine.charAt(0);

        // FSM simulation loop
        while (running) {
            if (currentChar >= 'A' && currentChar <= 'Z') {
                foundUpperCase = true;
            } else if (currentChar >= 'a' && currentChar <= 'z') {
                foundLowerCase = true;
            } else if (currentChar >= '0' && currentChar <= '9') {
                foundNumericDigit = true;
            } else if (SPECIAL_CHARS.indexOf(currentChar) >= 0) {
                foundSpecialChar = true;
            } else {
                passwordIndexofError = currentCharNdx;
                return "*** ERROR *** An invalid character ['" + currentChar + "'] was found in the password.";
            }

            if (currentCharNdx >= 7) {
                foundLongEnough = true;
            }

            // Advance to next character
            currentCharNdx++;
            if (currentCharNdx >= inputLine.length()) {
                running = false;
            } else {
                currentChar = inputLine.charAt(currentCharNdx);
            }
        }

        // Validate final conditions
        StringBuilder errorMessage = new StringBuilder();
        if (!foundUpperCase)    errorMessage.append(" Missing uppercase;");
        if (!foundLowerCase)    errorMessage.append(" Missing lowercase;");
        if (!foundNumericDigit) errorMessage.append(" Missing digit;");
        if (!foundSpecialChar)  errorMessage.append(" Missing special char;");
        if (!foundLongEnough)   errorMessage.append(" Must be at least 8 chars long;");

        if (errorMessage.length() == 0) {
            return ""; // success
        } else {
            passwordIndexofError = currentCharNdx;
            return "*** ERROR *** Password not valid:" + errorMessage.toString();
        }
    }
}
