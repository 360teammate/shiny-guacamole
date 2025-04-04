package UIPages;

import java.sql.SQLException;
import java.util.ArrayList;
import Application.StartCSE360;
import Application.UserRole;
import Database.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * UserListUI class represents the user list page with updated UI.
 */
public class UserListPage {
    private final DatabaseHelper databaseHelper;
    private ArrayList<String> userNames = new ArrayList<>();
    private ArrayList<ArrayList<String>> roles = new ArrayList<>();

    public UserListPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        loadUserData();
    }

    private void loadUserData() {
        userNames = StartCSE360.databaseHelper.getAllUsernames();
        roles.clear();

        for (String user : userNames) {
            ArrayList<UserRole> userRoles = StartCSE360.databaseHelper.getUserRole(user);
            ArrayList<String> roleNames = new ArrayList<>();

            for (UserRole role : userRoles) {
                roleNames.add(role.toString());
            }

            roles.add(roleNames);
        }
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 30; -fx-background-color: #f4f4f4;");

        Label title = new Label("User List");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label subtitle = new Label("Manage user roles and details");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

        // Back button
        Button backButton = createStyledButton("🔙 Back");
        backButton.setOnAction(e -> new AdminHomePage(databaseHelper).show(primaryStage));

        VBox userListBox = new VBox(10);
        userListBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < userNames.size(); i++) {
            final int index = i;  // Create a final variable to capture the index
            String user = userNames.get(i);
            String roleList = String.join(", ", roles.get(i));

            Label userLabel = new Label("👤 " + user);
            userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label roleLabel = new Label("Roles: " + roleList);
            roleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

            // Error label for validation messages
            Label errorLabel = new Label();
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

            // Check if the user is an admin, if so, do not show checkboxes
            boolean isAdmin = roles.get(i).contains("Admin");

            HBox checkBoxContainer = new HBox(10);
            Button assignButton;

            if (!isAdmin) {
                // Checkboxes for roles (excluding Admin)
                CheckBox instructorCheckBox = new CheckBox("Instructor");
                CheckBox studentCheckBox = new CheckBox("Student");
                CheckBox reviewerCheckBox = new CheckBox("Reviewer");

                // Set initial values based on user's existing roles
                instructorCheckBox.setSelected(roles.get(i).contains("INSTRUCTOR"));
                studentCheckBox.setSelected(roles.get(i).contains("STUDENT"));
                reviewerCheckBox.setSelected(roles.get(i).contains("REVIEWER"));

                // Ensure mutual exclusivity of Instructor and Student
                instructorCheckBox.setOnAction(e -> {
                    if (instructorCheckBox.isSelected()) {
                        studentCheckBox.setSelected(false);
                    }
                });

                studentCheckBox.setOnAction(e -> {
                    if (studentCheckBox.isSelected()) {
                        instructorCheckBox.setSelected(false);
                    }
                });

                // Add checkboxes to the container
                checkBoxContainer.getChildren().addAll(instructorCheckBox, studentCheckBox, reviewerCheckBox);

                // Assign Button
                assignButton = createStyledButton("Assign Roles");
                assignButton.setOnAction(e -> {
                    ArrayList<UserRole> selectedRoles = new ArrayList<>();

                    // Ensure the admin role is always retained
                    if (roles.get(index).contains("ADMIN")) {
                        selectedRoles.add(UserRole.ADMIN);
                    }

                    // Add roles based on user input
                    if (instructorCheckBox.isSelected()) selectedRoles.add(UserRole.INSTRUCTOR);
                    if (studentCheckBox.isSelected()) selectedRoles.add(UserRole.STUDENT);
                    if (reviewerCheckBox.isSelected()) selectedRoles.add(UserRole.REVIEWER);

                    // If no roles selected, keep existing roles
                    if (selectedRoles.isEmpty()) {
                        errorLabel.setText("No roles selected. User roles remain unchanged.");
                        return;
                    }

                    // Make sure the admin role is preserved (no matter what other roles are selected)
                    if (!selectedRoles.contains(UserRole.ADMIN) && roles.get(index).contains("ADMIN")) {
                        selectedRoles.add(UserRole.ADMIN);  // Add the admin role if it was removed
                    }

                    assignRole(user, selectedRoles, errorLabel);
                    loadUserData();  // Refresh data after assignment
                    show(primaryStage);  // Refresh UI
                });
            } else {
                // If the user is an Admin, do not show checkboxes and disable the Assign button
                assignButton = createStyledButton("Admin cannot have roles adjusted");
                assignButton.setDisable(true);
                checkBoxContainer.setVisible(false);  // Hide checkboxes for admin
            }

            VBox userBox = new VBox(5, userLabel, roleLabel, checkBoxContainer, assignButton, errorLabel);
            userBox.setPadding(new Insets(10));
            userBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #ffffff; -fx-border-radius: 5;");
            userBox.setMaxWidth(400);

            userListBox.getChildren().add(userBox);
        }

        // Create a scrollable pane for the user list
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(userListBox);
        scrollPane.setFitToWidth(true);  // Ensure it resizes to fit the width of the screen
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);  // Vertical scrollbar only when needed
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);  // No horizontal scrollbar

        layout.getChildren().addAll(title, subtitle, scrollPane, backButton);

        Scene scene = new Scene(layout, StartCSE360.WIDTH, StartCSE360.HEIGHT);
        primaryStage.setTitle("User List");
        primaryStage.setScene(scene);
    }

    public void assignRole(String targetUserName, ArrayList<UserRole> newRoles, Label errorLabel) {
        if (!databaseHelper.doesUserExist(targetUserName)) {
            errorLabel.setText("Error: User does not exist.");
            return;
        }

        try {
            databaseHelper.updateUserRoles(targetUserName, newRoles);
            errorLabel.setText("Roles successfully updated.");
        } catch (SQLException e) {
            errorLabel.setText("Database error: Unable to update roles.");
            e.printStackTrace();
        }
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        String defaultStyle = "-fx-font-size: 16px; -fx-padding: 12px 20px; " +
                              "-fx-background-color: #0078D7; -fx-text-fill: white; " +
                              "-fx-border-radius: 5; -fx-background-radius: 5;";
        String hoverStyle = "-fx-font-size: 16px; -fx-padding: 12px 20px; " +
                            "-fx-background-color: #005a9e; -fx-text-fill: white; " +
                            "-fx-border-radius: 5; -fx-background-radius: 5;";

        button.setStyle(defaultStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));

        return button;
    }
}
