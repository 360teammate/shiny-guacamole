package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;

/**
 * SetupAccountPage class handles the account setup process for new users.
 * Users provide their userName, password, and a valid invitation code to register.
 */
public class SetupAccountPage {

    private final DatabaseHelper databaseHelper;

    public SetupAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Username");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);

        TextField inviteCodeField = new TextField();
        inviteCodeField.setPromptText("Enter Invitation Code");
        inviteCodeField.setMaxWidth(250);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button setupButton = new Button("Setup");

        setupButton.setOnAction(a -> {
            String userName = userNameField.getText();
            String password = passwordField.getText();
            String inviteCode = inviteCodeField.getText();

            // Validate userName
            String userNameError = UserNameRecognizer.checkForValidUserName(userName);
            if (!userNameError.isEmpty()) {
                errorLabel.setText(userNameError);
                return;
            }

            // Validate password
            String passwordError = PasswordEvaluator.evaluatePassword(password);
            if (!passwordError.isEmpty()) {
                errorLabel.setText(passwordError);
                return;
            }

            try {
                if (!databaseHelper.validateInvitationCode(inviteCode)) {
                    errorLabel.setText("*** ERROR *** Invalid invitation code. Please try again.");
                    return;
                }

                if (databaseHelper.doesUserExist(userName)) {
                    errorLabel.setText("*** ERROR *** That username already exists. Choose a different username.");
                    return;
                }

                // Register new user
                User newUser = new User(userName, password, "user");
                databaseHelper.register(newUser);

                // Navigate to welcome page
                new WelcomeLoginPage(databaseHelper).show(primaryStage, newUser);

            } catch (SQLException e) {
                errorLabel.setText("*** ERROR *** Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        VBox layout = new VBox(10, userNameField, passwordField, inviteCodeField, setupButton, errorLabel);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
}
