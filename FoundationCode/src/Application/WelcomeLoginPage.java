package Application;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

import Database.*;
import UIPages.AdminHomePage;
import UIPages.UserHomePage;

/**
 * The WelcomeLoginPage class displays a welcome screen for authenticated users.
 * It allows users to navigate to their respective pages based on their role or quit the application.
 */
public class WelcomeLoginPage {

    private final DatabaseHelper databaseHelper;

    public WelcomeLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, User user) {
    	try {
			StartCSE360.loggedInUser = user;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 30; -fx-background-color: #f4f4f4;");

        // Title
        Label welcomeLabel = new Label("Welcome to the Portal!");
        welcomeLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label subtitle = new Label("Navigate to your respective dashboard");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

        // Buttons
        Button continueButton = createStyledButton("➡ Continue to Dashboard");

        final ArrayList<UserRole> roles = user.getRole();  
        continueButton.setOnAction(a -> {
            System.out.println(roles);

            if (roles.contains(UserRole.ADMIN)) {
                new AdminHomePage().show(primaryStage);
            } else if (roles.contains(UserRole.STUDENT)) {
                new UserHomePage().show(primaryStage);
            }
        });

        Button quitButton = createStyledButton("❌ Quit", true);
        quitButton.setOnAction(a -> {
            databaseHelper.closeConnection();
            Platform.exit(); // Exit the JavaFX application
        });

        layout.getChildren().addAll(welcomeLabel, subtitle, continueButton, quitButton);
        Scene welcomeScene = new Scene(layout, StartCSE360.WIDTH, StartCSE360.HEIGHT);

        // Set the scene to primary stage
        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Welcome Page");
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
