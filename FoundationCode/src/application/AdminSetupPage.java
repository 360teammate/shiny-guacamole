package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import databasePart1.*;

/**
 * The AdminSetupPage class handles the setup process for creating an administrator account.
 */
public class AdminSetupPage {
    private final DatabaseHelper databaseHelper;

    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin Username");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button setupButton = new Button("Setup");

        setupButton.setOnAction(a -> {
            String userName = userNameField.getText();
            String password = passwordField.getText();
            
            String userNameError = UserNameRecognizer.checkForValidUserName(userName);
            if (!userNameError.isEmpty()) {
                errorLabel.setText(userNameError);
                return;
            }

            String passwordError = PasswordEvaluator.evaluatePassword(password);
            if (!passwordError.isEmpty()) {
                errorLabel.setText(passwordError);
                return;
            }

            try {
                User user = new User(userName, password, "admin");
                databaseHelper.register(user);
                System.out.println("Administrator setup completed. Please log in again.");
                new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
            } catch (SQLException e) {
                errorLabel.setText("Database error: " + e.getMessage());
            }
        });

        VBox layout = new VBox(10, userNameField, passwordField, setupButton, errorLabel);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}
