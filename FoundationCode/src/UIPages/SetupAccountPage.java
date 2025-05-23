package UIPages;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Application.PasswordEvaluator;
import Application.StartCSE360;
import Application.User;
import Application.UserNameRecognizer;
import Application.UserRole;
import Database.*;

/**
 * SetupAccountPage class handles the account setup process for new users.
 * Users provide their userName, password, and a valid invitation code to register.
 */
public class SetupAccountPage {
	
    private final DatabaseHelper databaseHelper;
    // DatabaseHelper to handle database operations.
    public SetupAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the Setup Account page in the provided stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
    	// Input fields for userName, password, and invitation code
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        TextField inviteCodeField = new TextField();
        inviteCodeField.setPromptText("Enter InvitationCode");
        inviteCodeField.setMaxWidth(250);
        
        // Label to display error messages for invalid input or registration issues
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        

        Button setupButton = new Button("Setup");
        setupButton.setOnAction(a -> {
        	// Retrieve user input
            String userName = userNameField.getText();
            String userNameErrorMessage = UserNameRecognizer.checkForValidUserName(userName);
            String password = passwordField.getText();
            String passwordErrorMessage = PasswordEvaluator.evaluatePassword(password);
            String code = inviteCodeField.getText();
            
            try {
            	if (!userNameErrorMessage.equals("")) {
            		errorLabel.setText(userNameErrorMessage);
            		return;
            	}
            	if (!passwordErrorMessage.equals("")) {
            		errorLabel.setText(passwordErrorMessage);
            		return;
            	}
            	
            	
            	// Check if the user already exists
            	if(!databaseHelper.doesUserExist(userName)) {
            		
            		// Validate the invitation code
            		if(databaseHelper.validateInvitationCode(code)) {
            			
            			// Create a new user and register them in the database
		            	User user=new User(userName, password, new ArrayList<UserRole>(List.of(UserRole.STUDENT)));
		                databaseHelper.register(user);
		                
		             // Navigate to the Welcome Login Page
		                new WelcomeLoginPage(databaseHelper).show(primaryStage,user);
            		}
            		else {
            			errorLabel.setText("Please enter a valid invitation code");
            		}
            	}
            	else {
            		errorLabel.setText("This useruserName is taken!!.. Please use another to setup an account");
            	}
            	
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });
        
        Button backButton = new Button("Back");
        backButton.setOnAction(a -> {
        	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField,inviteCodeField, setupButton, backButton, errorLabel);

        primaryStage.setScene(new Scene(layout, StartCSE360.WIDTH, StartCSE360.HEIGHT));
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
}
