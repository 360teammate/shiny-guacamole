package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;
import java.sql.SQLException;
import java.util.List;

/**
 * AdminHomePage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin and provides
 * administrative options (Reset Password, Delete User, View Users, Update User Role).
 */
public class AdminHomePage {
    private final DatabaseHelper databaseHelper;

    public AdminHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label adminLabel = new Label("Hello, Admin!");
        adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Button to invite a user
        Button inviteButton = new Button("Invite User");
        inviteButton.setOnAction(e -> new InvitationPage().show(databaseHelper, primaryStage));

        // Reset password button
        Button resetPasswordButton = new Button("Reset User Password");
        resetPasswordButton.setOnAction(e -> resetUserPassword());

        // Delete user button
        Button deleteUserButton = new Button("Delete User");
        deleteUserButton.setOnAction(e -> deleteUser());

        // View users button
        Button viewUsersButton = new Button("View Users");
        viewUsersButton.setOnAction(e -> viewUsers());

        // Update user role button
        Button updateUserRoleButton = new Button("Update User Role");
        updateUserRoleButton.setOnAction(e -> updateUserRole());

        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> new SetupLoginSelectionPage(databaseHelper).show(primaryStage));

        layout.getChildren().addAll(adminLabel, inviteButton, resetPasswordButton,
                deleteUserButton, viewUsersButton, updateUserRoleButton, logoutButton);

        Scene adminScene = new Scene(layout, 800, 400);
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin Page");
        primaryStage.show();
    }

    /**
     * Prompts the admin for a username and a new password, then resets the user's password.
     */
    private void resetUserPassword() {
        // Prompt for username
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setTitle("Reset Password");
        usernameDialog.setHeaderText("Enter the username to reset");
        usernameDialog.setContentText("Username:");
        usernameDialog.showAndWait().ifPresent(username -> {
            // Prompt for new password
            TextInputDialog passDialog = new TextInputDialog();
            passDialog.setTitle("New Password");
            passDialog.setHeaderText("Enter the new password for " + username);
            passDialog.setContentText("New Password:");
            passDialog.showAndWait().ifPresent(newPass -> {
                try {
                    databaseHelper.resetUserPassword(username, newPass);
                    showAlert(Alert.AlertType.INFORMATION,
                            "Success", "Password reset successfully for user: " + username);
                } catch (SQLException ex) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", ex.getMessage());
                }
            });
        });
    }

    /**
     * Prompts the admin for a username to delete from the database.
     */
    private void deleteUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete User");
        dialog.setHeaderText("Enter Username to Delete");
        dialog.setContentText("Username:");
        dialog.showAndWait().ifPresent(username -> {
            try {
                databaseHelper.deleteUser(username);
                showAlert(Alert.AlertType.INFORMATION,
                        "Success", "User \"" + username + "\" deleted successfully.");
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", ex.getMessage());
            }
        });
    }

    /**
     * Retrieves all users from the database and displays them in a TableView.
     */
    private void viewUsers() {
        Stage userStage = new Stage();
        TableView<User> userTable = new TableView<>();

        // Table columns
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getUserName()));

        TableColumn<User, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPassword()));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRole()));

        userTable.getColumns().addAll(usernameCol, passwordCol, roleCol);

        try {
            List<User> users = databaseHelper.getAllUsers();
            userTable.getItems().addAll(users);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }

        VBox layout = new VBox(userTable);
        Scene scene = new Scene(layout, 600, 400);
        userStage.setScene(scene);
        userStage.setTitle("All Users");
        userStage.show();
    }

    /**
     * Prompts the admin for a username and then a new role, and updates the user's role.
     */
    private void updateUserRole() {
        TextInputDialog userDialog = new TextInputDialog();
        userDialog.setTitle("Update User Role");
        userDialog.setHeaderText("Enter Username to Update Role");
        userDialog.setContentText("Username:");
        userDialog.showAndWait().ifPresent(username -> {
            ChoiceDialog<String> roleDialog = new ChoiceDialog<>("user", "admin", "student", "instructor", "staff");
            roleDialog.setTitle("Select New Role");
            roleDialog.setHeaderText("Choose a New Role for \"" + username + "\"");
            roleDialog.setContentText("Role:");
            roleDialog.showAndWait().ifPresent(newRole -> {
                try {
                    databaseHelper.updateUserRole(username, newRole);
                    showAlert(Alert.AlertType.INFORMATION,
                            "Success", "Role updated to \"" + newRole + "\" for user: " + username);
                } catch (SQLException ex) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", ex.getMessage());
                }
            });
        });
    }

    /**
     * Utility method to show alerts.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
