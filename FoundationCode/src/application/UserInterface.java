package application;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * A JavaFX-based user interface testbed to develop and test password UI ideas.
 */
public class UserInterface {

    // For the password test GUI
    private Label label_ApplicationTitle = new Label("Password Evaluation Testbed");
    private Label label_Password = new Label("Enter the password here");
    private TextField text_Password = new TextField();
    private Label label_errPassword = new Label("");
    private Label noInputFound = new Label("");
    private TextFlow errPassword;
    private Text errPasswordPart1 = new Text();
    private Text errPasswordPart2 = new Text();
    private Label errPasswordPart3 = new Label("");
    private Label validPassword = new Label("");

    private Label label_Requirements = new Label("A valid password must satisfy the following requirements:");
    private Label label_UpperCase = new Label("At least one upper case letter");
    private Label label_LowerCase = new Label("At least one lower case letter");
    private Label label_NumericDigit = new Label("At least one numeric digit");
    private Label label_SpecialChar = new Label("At least one special character");
    private Label label_LongEnough = new Label("At least eight characters");

    public UserInterface(Pane theRoot) {
        setupLabelUI(label_ApplicationTitle, "Arial", 24,
                PasswordEvaluationGUITestbed.WINDOW_WIDTH, Pos.CENTER, 0, 10);

        setupLabelUI(label_Password, "Arial", 14,
                PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 10, 50);

        setupTextUI(text_Password, "Arial", 18,
                PasswordEvaluationGUITestbed.WINDOW_WIDTH - 20, Pos.BASELINE_LEFT, 10, 70, true);

        text_Password.textProperty().addListener((observable, oldValue, newValue) -> {
            setPassword();
        });

        noInputFound.setTextFill(Color.RED);
        noInputFound.setAlignment(Pos.BASELINE_LEFT);
        setupLabelUI(noInputFound, "Arial", 14,
                PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 10, 110);

        label_errPassword.setTextFill(Color.RED);
        label_errPassword.setAlignment(Pos.BASELINE_RIGHT);
        setupLabelUI(label_errPassword, "Arial", 14,
                PasswordEvaluationGUITestbed.WINDOW_WIDTH - 160, Pos.BASELINE_LEFT, 22, 126);

        errPasswordPart1.setFill(Color.BLACK);
        errPasswordPart1.setFont(Font.font("Arial", FontPosture.REGULAR, 18));
        errPasswordPart2.setFill(Color.RED);
        errPasswordPart2.setFont(Font.font("Arial", FontPosture.REGULAR, 24));
        errPassword = new TextFlow(errPasswordPart1, errPasswordPart2);
        errPassword.setMinWidth(PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10);
        errPassword.setLayoutX(22);
        errPassword.setLayoutY(100);

        setupLabelUI(errPasswordPart3, "Arial", 14, 200, Pos.BASELINE_LEFT, 20, 125);

        setupLabelUI(label_Requirements, "Arial", 16,
                PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 10, 200);

        setupLabelUI(label_UpperCase, "Arial", 14,
                PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 30, 230);
        label_UpperCase.setTextFill(Color.RED);

        setupLabelUI(label_LowerCase, "Arial", 14,
                PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 30, 260);
        label_LowerCase.setTextFill(Color.RED);

        setupLabelUI(label_NumericDigit, "Arial", 14,
                PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 30, 290);
        label_NumericDigit.setTextFill(Color.RED);

        setupLabelUI(label_SpecialChar, "Arial", 14,
                PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 30, 320);
        label_SpecialChar.setTextFill(Color.RED);

        setupLabelUI(label_LongEnough, "Arial", 14,
                PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 30, 350);
        label_LongEnough.setTextFill(Color.RED);

        resetAssessments();

        validPassword.setTextFill(Color.GREEN);
        validPassword.setAlignment(Pos.BASELINE_RIGHT);
        setupLabelUI(validPassword, "Arial", 18,
                PasswordEvaluationGUITestbed.WINDOW_WIDTH - 160, Pos.BASELINE_LEFT, 10, 380);

        theRoot.getChildren().addAll(label_ApplicationTitle, label_Password, text_Password,
                noInputFound, label_errPassword, errPassword, errPasswordPart3, validPassword,
                label_Requirements, label_UpperCase, label_LowerCase, label_NumericDigit,
                label_SpecialChar, label_LongEnough);
    }

    private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) {
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);
    }

    private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e) {
        t.setFont(Font.font(ff, f));
        t.setMinWidth(w);
        t.setMaxWidth(w);
        t.setAlignment(p);
        t.setLayoutX(x);
        t.setLayoutY(y);
        t.setEditable(e);
    }

    private void setPassword() {
        label_errPassword.setText("");
        noInputFound.setText("");
        errPasswordPart1.setText("");
        errPasswordPart2.setText("");
        validPassword.setText("");
        resetAssessments();
        performEvaluation();
    }

    private void performEvaluation() {
        String inputText = text_Password.getText();
        if (inputText.isEmpty()) {
            noInputFound.setText("No input text found!");
        } else {
            String errMessage = PasswordEvaluator.evaluatePassword(inputText);
            updateFlags();
            if (!errMessage.isEmpty()) {
                label_errPassword.setText(PasswordEvaluator.passwordErrorMessage);
                if (PasswordEvaluator.passwordIndexofError <= -1) return;
                String input = PasswordEvaluator.passwordInput;

                // If index is within range
                if (PasswordEvaluator.passwordIndexofError <= input.length()) {
                    errPasswordPart1.setText(input.substring(0, PasswordEvaluator.passwordIndexofError));
                }
                errPasswordPart2.setText("\u21EB");
                validPassword.setTextFill(Color.RED);
                errPasswordPart3.setText("The red arrow points at the character causing the error!");
                validPassword.setText("Failure! The password is not valid.");
            } else if (PasswordEvaluator.foundUpperCase && PasswordEvaluator.foundLowerCase &&
                    PasswordEvaluator.foundNumericDigit && PasswordEvaluator.foundSpecialChar &&
                    PasswordEvaluator.foundLongEnough) {

                validPassword.setTextFill(Color.GREEN);
                validPassword.setText("Success! The password satisfies the requirements.");
            } else {
                validPassword.setTextFill(Color.RED);
                validPassword.setText("The password as currently entered is not yet valid.");
            }
        }
    }

    protected void resetAssessments() {
        label_UpperCase.setText("At least one upper case letter - Not yet satisfied");
        label_UpperCase.setTextFill(Color.RED);

        label_LowerCase.setText("At least one lower case letter - Not yet satisfied");
        label_LowerCase.setTextFill(Color.RED);

        label_NumericDigit.setText("At least one numeric digit - Not yet satisfied");
        label_NumericDigit.setTextFill(Color.RED);

        label_SpecialChar.setText("At least one special character - Not yet satisfied");
        label_SpecialChar.setTextFill(Color.RED);

        label_LongEnough.setText("At least eight characters - Not yet satisfied");
        label_LongEnough.setTextFill(Color.RED);

        errPasswordPart3.setText("");
    }

    private void updateFlags() {
        if (PasswordEvaluator.foundUpperCase) {
            label_UpperCase.setText("At least one upper case letter - Satisfied");
            label_UpperCase.setTextFill(Color.GREEN);
        }
        if (PasswordEvaluator.foundLowerCase) {
            label_LowerCase.setText("At least one lower case letter - Satisfied");
            label_LowerCase.setTextFill(Color.GREEN);
        }
        if (PasswordEvaluator.foundNumericDigit) {
            label_NumericDigit.setText("At least one numeric digit - Satisfied");
            label_NumericDigit.setTextFill(Color.GREEN);
        }
        if (PasswordEvaluator.foundSpecialChar) {
            label_SpecialChar.setText("At least one special character - Satisfied");
            label_SpecialChar.setTextFill(Color.GREEN);
        }
        if (PasswordEvaluator.foundLongEnough) {
            label_LongEnough.setText("At least eight characters - Satisfied");
            label_LongEnough.setTextFill(Color.GREEN);
        }
    }
}
