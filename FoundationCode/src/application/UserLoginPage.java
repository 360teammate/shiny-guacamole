package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;

/**
 * The UserLoginPage class provides a login interface for users to access their accounts.
 * It validates the user's credentials and navigates to the appropriate page upon successful login.
 */
public class UserLoginPage {

    private final DatabaseHelper databaseHelper;

    public UserLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Username");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button loginButton = new Button("Login");

        loginButton.setOnAction(a -> {
            String userName = userNameField.getText();
            String password = passwordField.getText();

            // If the assignment wants us to re-check the userName format on login:
            String userNameError = UserNameRecognizer.checkForValidUserName(userName);
            if (!userNameError.isEmpty()) {
                errorLabel.setText(userNameError);
                return;
            }

            // If the assignment wants us to re-check password rules on login:
            String passwordErr = PasswordEvaluator.evaluatePassword(password);
            if (!passwordErr.isEmpty()) {
                errorLabel.setText(passwordErr);
                return;
            }

            try {
                String role = databaseHelper.getUserRole(userName);
                if (role != null) {
                    User user = new User(userName, password, role);
                    if (databaseHelper.login(user)) {
                        new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
                    } else {
                        errorLabel.setText("*** ERROR *** Login failed. Incorrect username or password.");
                    }
                } else {
                    errorLabel.setText("*** ERROR *** No account found with that username.");
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
                errorLabel.setText("*** ERROR *** Database error: " + e.getMessage());
            }
        });

        VBox layout = new VBox(10, userNameField, passwordField, loginButton, errorLabel);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
}
