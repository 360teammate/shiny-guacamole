package UIPages;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import Application.PasswordEvaluator;
import Application.StartCSE360;
import Application.User;
import Application.UserNameRecognizer;
import Application.UserRole;
import Database.*;

/**
 * The SetupAdmin class handles the setup process for creating an administrator account.
 * This is intended to be used by the first user to initialize the system with admin credentials.
 */
public class AdminSetupPage {
	
    private final DatabaseHelper databaseHelper;

    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	// Input fields for userName and password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);

        Button setupButton = new Button("Setup");
        
        // Label to display error messages for invalid input or registration issues
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        setupButton.setOnAction(a -> {
        	// Retrieve user input
            String userName = userNameField.getText();
            String userNameErrorMessage = UserNameRecognizer.checkForValidUserName(userName);
            String password = passwordField.getText();
            String passwordErrorMessage = PasswordEvaluator.evaluatePassword(password);
            try {
            	if (!userNameErrorMessage.equals("")) {
            		errorLabel.setText(userNameErrorMessage);
            		return;
            	}
            	if (!passwordErrorMessage.equals("")) {
            		errorLabel.setText(passwordErrorMessage);
            		return;
            	}
            	
            	// Create a new User object with admin role and register in the database
            	User user=new User(userName, password, UserRole.ADMIN);
                databaseHelper.register(user);
                System.out.println("Administrator setup completed.");
                
                // Navigate to the Welcome Login Page
                new WelcomeLoginPage(databaseHelper).show(primaryStage,user);
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        VBox layout = new VBox(10, userNameField, passwordField, setupButton, errorLabel);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, StartCSE360.WIDTH, StartCSE360.HEIGHT));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}
