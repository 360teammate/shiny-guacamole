package UIPages;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

import Application.RoleRequest;
import Application.StartCSE360;
import Application.User;
import Application.UserRole;
import Database.DatabaseHelper;

public class RoleRequestPage {
	
	private final DatabaseHelper databaseHelper;
	
	public RoleRequestPage (DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}
	
	public void show(Stage primaryStage) {
		// Input field for the user's userName, password

        TextField requestField = new TextField();
        requestField.setPromptText("Input application reasoning");
        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");


        Button reviewerRoleRequestButton = new Button("Submit Reviewer Role Request");
        reviewerRoleRequestButton.setOnAction(a -> {
        	// Retrieve user inputs
            String requestText = requestField.getText();
            String userName = StartCSE360.loggedInUser.getUserName();
            
            try {
				databaseHelper.insertRoleRequest(userName, requestText, UserRole.REVIEWER);
				requestField.setText("Request Sent");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				requestField.setText("Request failed");
				e.printStackTrace();
			}
        });
        
        Button backButton = new Button("Back");
        backButton.setOnAction(a -> {
        	new UserHomePage(databaseHelper).show(primaryStage);
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(requestField, reviewerRoleRequestButton, backButton, errorLabel);

        primaryStage.setScene(new Scene(layout, StartCSE360.WIDTH, StartCSE360.HEIGHT));
        primaryStage.setTitle("Request Page");
        primaryStage.show();
    }
	/**
     * Helper method to create styled buttons for consistency.
     */
    private Button createStyledButton(String text) {
        return createStyledButton(text, false);
    }

    private Button createStyledButton(String text, boolean isQuitButton) {
        Button button = new Button(text);

        final String defaultStyle;
        final String hoverStyle;

        if (isQuitButton) {
            defaultStyle = "-fx-font-size: 16px; -fx-padding: 12px 20px; " +
                           "-fx-background-color: #ff4d4d; -fx-text-fill: white; " +
                           "-fx-border-radius: 5; -fx-background-radius: 5;";
            hoverStyle = "-fx-font-size: 16px; -fx-padding: 12px 20px; " +
                         "-fx-background-color: #cc0000; -fx-text-fill: white; " +
                         "-fx-border-radius: 5; -fx-background-radius: 5;";
        } else {
            defaultStyle = "-fx-font-size: 16px; -fx-padding: 12px 20px; " +
                           "-fx-background-color: #0078D7; -fx-text-fill: white; " +
                           "-fx-border-radius: 5; -fx-background-radius: 5;";
            hoverStyle = "-fx-font-size: 16px; -fx-padding: 12px 20px; " +
                         "-fx-background-color: #005a9e; -fx-text-fill: white; " +
                         "-fx-border-radius: 5; -fx-background-radius: 5;";
        }

        button.setStyle(defaultStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));

        return button;
    }
}
