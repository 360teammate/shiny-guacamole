package UIPages;

import Application.*;
import Database.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import UIComponents.CustomButton;

import java.sql.SQLException;
import java.util.ArrayList;

public class ReviewRequestPage {

    public void show(Stage primaryStage) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        Label title = new Label("Pending Reviewer Role Requests");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox requestList = new VBox(15);
        requestList.setPadding(new Insets(10));

        try {
            ArrayList<User> pendingUsers = StartCSE360.databaseHelper.getPendingRequests();

            if (pendingUsers.isEmpty()) {
                requestList.getChildren().add(new Label("No pending requests."));
            } else {
                for (User user : pendingUsers) {
                    HBox row = new HBox(10);
                    row.setAlignment(Pos.CENTER_LEFT);

                    Label userLabel = new Label(user.getUserName());
                    userLabel.setStyle("-fx-font-size: 16px;");

                    CustomButton approveBtn = new CustomButton("Approve", CustomButton.ColorPreset.GREEN, e -> {
                        try {
                            ArrayList<UserRole> roles = user.getRole();
                            if (!roles.contains(UserRole.REVIEWER)) {
                                roles.add(UserRole.REVIEWER);
                                StartCSE360.databaseHelper.updateUserRoles(user.getUserName(), roles);

                                // Remove the request after approval
                                StartCSE360.databaseHelper.removeRoleRequest(user.getUserName());

                                show(primaryStage); // reload the page
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    });


                    CustomButton denyBtn = new CustomButton("Deny", CustomButton.ColorPreset.RED, e -> {
                        try {
                            StartCSE360.databaseHelper.removeRoleRequest(user.getUserName());
                            requestList.getChildren().remove(row); // visually remove after DB update
                            System.out.println("Denied request for: " + user.getUserName());
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            System.out.println("Failed to deny request for: " + user.getUserName());
                        }
                    });


                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    row.getChildren().addAll(userLabel, spacer, approveBtn, denyBtn);
                    requestList.getChildren().add(row);
                }
            }
        } catch (SQLException e) {
            requestList.getChildren().add(new Label("Error fetching pending requests."));
            e.printStackTrace();
        }

        scrollPane.setContent(requestList);

        CustomButton backButton = new CustomButton("← Back", CustomButton.ColorPreset.GREY, e -> {
            new AdminHomePage(StartCSE360.databaseHelper).show(primaryStage);
        });

        layout.getChildren().addAll(title, scrollPane, backButton);

        Scene scene = new Scene(layout, StartCSE360.WIDTH, StartCSE360.HEIGHT);
        primaryStage.setTitle("Reviewer Requests");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}