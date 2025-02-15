package application;

/*******
 * <p> Title: PasswordEvaluationTestingAutomation Class. </p>
 *
 * <p> Description: A Java demonstration for semi-automated tests of PasswordEvaluator </p>
 */
public class PasswordEvaluationTestingAutomation {

    static int numPassed = 0; // # of passed tests
    static int numFailed = 0; // # of failed tests

    public static void main(String[] args) {
        System.out.println("______________________________________");
        System.out.println("\nTesting Automation");

        // Start of the test cases
        performTestCase(1, "Aa!15678", true);
        performTestCase(2, "A!", false);
        performTestCase(3, "Aa!15678", false);
        performTestCase(4, "A!", true);
        performTestCase(5, "", true);

        System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    private static void performTestCase(int testCase, String inputText, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
        System.out.println("Input: \"" + inputText + "\"");
        System.out.println("______________");
        System.out.println("\nFinite state machine execution trace:");

        String resultText = PasswordEvaluator.evaluatePassword(inputText);
        System.out.println();

        if (!resultText.isEmpty()) {
            // The password was rejected
            if (expectedPass) {
                System.out.println("***Failure*** The password <" + inputText + "> is invalid." +
                        "\nBut it was supposed to be valid, so this is a failure!\n");
                System.out.println("Error message: " + resultText);
                numFailed++;
            } else {
                System.out.println("***Success*** The password <" + inputText + "> is invalid." +
                        "\nBut it was supposed to be invalid, so this is a pass!\n");
                System.out.println("Error message: " + resultText);
                numPassed++;
            }
        } else {
            // The password was accepted
            if (expectedPass) {
                System.out.println("***Success*** The password <" + inputText + "> is valid, so this is a pass!");
                numPassed++;
            } else {
                System.out.println("***Failure*** The password <" + inputText + "> was judged as valid" +
                        "\nBut it was supposed to be invalid, so this is a failure!");
                numFailed++;
            }
        }

        displayEvaluation();
    }

    private static void displayEvaluation() {
        if (PasswordEvaluator.foundUpperCase)
            System.out.println("At least one upper case letter - Satisfied");
        else
            System.out.println("At least one upper case letter - Not Satisfied");

        if (PasswordEvaluator.foundLowerCase)
            System.out.println("At least one lower case letter - Satisfied");
        else
            System.out.println("At least one lower case letter - Not Satisfied");

        if (PasswordEvaluator.foundNumericDigit)
            System.out.println("At least one digit - Satisfied");
        else
            System.out.println("At least one digit - Not Satisfied");

        if (PasswordEvaluator.foundSpecialChar)
            System.out.println("At least one special character - Satisfied");
        else
            System.out.println("At least one special character - Not Satisfied");

        if (PasswordEvaluator.foundLongEnough)
            System.out.println("At least 8 characters - Satisfied");
        else
            System.out.println("At least 8 characters - Not Satisfied");
    }
}
