package UIPages;

import java.util.ArrayList;

import Application.StartCSE360;
import Application.UserRole;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class UserListUI {
    private ArrayList<String> userNames = new ArrayList<>();
    private ArrayList<ArrayList<String>> roles = new ArrayList<>();

    public UserListUI() {
        userNames = StartCSE360.databaseHelper.getAllUsernames();

        for (String user : userNames) { // Iterate through each user
            ArrayList<UserRole> userRoles = StartCSE360.databaseHelper.getUserRole(user);
            ArrayList<String> roleNames = new ArrayList<>();

            for (UserRole role : userRoles) {
                roleNames.add(role.toString()); // Store role names as Strings
            }

            roles.add(roleNames); // Add the user's role list to roles
        }
    }

    public void show(Stage primaryStage) {
        VBox userListBox = new VBox(15);
        userListBox.setPadding(new Insets(20));
        userListBox.setAlignment(Pos.TOP_CENTER);

        // Back button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        backButton.setOnAction(e -> {
            new AdminHomePage().show(primaryStage); // Return to Admin Page
        });

        // Title
        Label title = new Label("User List");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Layout for title and back button
        VBox titleBox = new VBox(10, backButton, title);
        titleBox.setAlignment(Pos.CENTER);
        userListBox.getChildren().add(titleBox);

        // Populate the list with styled user entries
        for (int i = 0; i < userNames.size(); i++) {
            String user = userNames.get(i);
            String roleList = String.join(", ", roles.get(i)); // Format roles as comma-separated

            // User name label
            Label userLabel = new Label(user);
            userLabel.setFont(Font.font("Arial", 16));
            userLabel.setTextFill(Color.BLACK);

            // Role label
            Label roleLabel = new Label("Roles: " + roleList);
            roleLabel.setFont(Font.font("Arial", 14));
            roleLabel.setTextFill(Color.DARKGRAY);

            // User box styling
            VBox userBox = new VBox(5, userLabel, roleLabel);
            userBox.setPadding(new Insets(10));
            userBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #f9f9f9; -fx-border-radius: 5;");
            userBox.setMaxWidth(600);

            userListBox.getChildren().add(userBox);
        }

        // Wrap in a scrollable pane
        ScrollPane scrollPane = new ScrollPane(userListBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        Scene scene = new Scene(scrollPane, 800, 400); // Same size as other pages
        primaryStage.setTitle("User List");
        primaryStage.setScene(scene);
    }
}
