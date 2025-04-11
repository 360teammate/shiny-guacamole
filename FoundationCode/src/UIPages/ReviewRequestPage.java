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
            ArrayList<RoleRequest> pendingRequests = StartCSE360.databaseHelper.getRoleRequests();

            if (pendingRequests.isEmpty()) {
                requestList.getChildren().add(new Label("No pending requests."));
            } else {
            	for (RoleRequest request : pendingRequests) {
            	    HBox row = new HBox(10);
            	    row.setAlignment(Pos.CENTER_LEFT);
            	    row.setPadding(new Insets(10));
            	    row.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5;");

            	    // User info VBox (username + request text)
            	    VBox userInfo = new VBox(5);
            	    Label userLabel = new Label(request.getAuthor());
            	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            	    Label requestText = new Label(request.getText());
            	    requestText.setWrapText(true);
            	    requestText.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");

            	    userInfo.getChildren().addAll(userLabel, requestText);

            	    CustomButton approveBtn = new CustomButton("Approve", CustomButton.ColorPreset.GREEN, e -> {
            	        try {
            	            ArrayList<UserRole> roles = StartCSE360.databaseHelper.getUserRole(request.getAuthor());
            	            if (!roles.contains(UserRole.REVIEWER)) {
            	                roles.add(UserRole.REVIEWER);
            	                StartCSE360.databaseHelper.updateUserRoles(request.getAuthor(), roles);
            	                StartCSE360.databaseHelper.removeRoleRequest(request.getAuthor());
            	                show(primaryStage); // reload page
            	            }
            	        } catch (SQLException ex) {
            	            ex.printStackTrace();
            	        }
            	    });

            	    CustomButton denyBtn = new CustomButton("Deny", CustomButton.ColorPreset.RED, e -> {
            	        try {
            	            StartCSE360.databaseHelper.removeRoleRequest(request.getAuthor());
            	            requestList.getChildren().remove(row);
            	            System.out.println("Denied request for: " + request.getAuthor());
            	        } catch (SQLException ex) {
            	            ex.printStackTrace();
            	            System.out.println("Failed to deny request for: " + request.getAuthor());
            	        }
            	    });

            	    Region spacer = new Region();
            	    HBox.setHgrow(spacer, Priority.ALWAYS);

            	    row.getChildren().addAll(userInfo, spacer, approveBtn, denyBtn);
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