package UIPages;

import Application.Announcement;
import Application.StartCSE360;
import Application.UserRole;
import Database.DatabaseHelper;
import UIComponents.CustomButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

public class StaffAnnouncementPage {

    public void show(Stage primaryStage) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #f4f4f4;");

        Label title = new Label("📢 Staff Announcements");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label postStatus = new Label();
        postStatus.setStyle("-fx-text-fill: green;");

        // Only STAFF, ADMIN, INSTRUCTOR can post
        if (StartCSE360.loggedInUser.getRole().stream().anyMatch(role ->
                role == UserRole.STAFF || role == UserRole.ADMIN || role == UserRole.INSTRUCTOR)) {

            TextArea announcementArea = new TextArea();
            announcementArea.setPromptText("Enter your announcement here...");
            announcementArea.setWrapText(true);
            announcementArea.setPrefHeight(100);

            Button postButton = new Button("Post");

            String defaultStyle = "-fx-font-size: 16px; -fx-padding: 12px 20px; " +
                                  "-fx-background-color: #0078D7; -fx-text-fill: white; " +
                                  "-fx-border-radius: 5; -fx-background-radius: 5;";
            String hoverStyle = "-fx-font-size: 16px; -fx-padding: 12px 20px; " +
                                "-fx-background-color: #005a9e; -fx-text-fill: white; " +
                                "-fx-border-radius: 5; -fx-background-radius: 5;";

            postButton.setStyle(defaultStyle);
            postButton.setOnMouseEntered(e -> postButton.setStyle(hoverStyle));
            postButton.setOnMouseExited(e -> postButton.setStyle(defaultStyle));

            postButton.setOnAction(e -> {
                String content = announcementArea.getText().trim();
                if (!content.isEmpty()) {
                    try {
                        StartCSE360.databaseHelper.insertAnnouncement(StartCSE360.loggedInUser.getUserName(), content);
                        postStatus.setText("✅ Announcement posted!");
                        announcementArea.clear();
                        show(primaryStage); // reload to update list
                    } catch (SQLException ex) {
                        postStatus.setText("❌ Failed to post announcement.");
                        ex.printStackTrace();
                    }
                } else {
                    postStatus.setText("❗ Cannot post empty announcement.");
                }
            });

            layout.getChildren().addAll(title, announcementArea, postButton, postStatus);
        } else {
            layout.getChildren().add(title); // Add title only for non-posting roles
        }

        // Announcement list section
        VBox announcementList = new VBox(10);
        try {
            ArrayList<Announcement> announcements = StartCSE360.databaseHelper.getAllAnnouncements();
            for (Announcement a : announcements) {
                HBox annBox = new HBox(10);
                annBox.setAlignment(Pos.CENTER_LEFT);
                annBox.setStyle("-fx-background-color: #e6f0ff; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5;");

                Label label = new Label("[" + a.getTimestamp() + "] " + a.getAuthor() + ": " + a.getContent());
                label.setWrapText(true);
                label.setMaxWidth(500);  // Optional for better wrapping

                annBox.getChildren().add(label);

                // Show delete button only if the logged-in user is Admin/Instructor/Staff
                if (StartCSE360.loggedInUser.getRole().stream().anyMatch(role ->
                        role == UserRole.ADMIN || role == UserRole.INSTRUCTOR || role == UserRole.STAFF)) {

                    Button deleteBtn = new Button("🗑 Delete");
                    deleteBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-weight: bold;");
                    deleteBtn.setOnAction(e -> {
                        try {
                            StartCSE360.databaseHelper.deleteAnnouncement(a.getId());
                            show(primaryStage); // refresh the page
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            new Alert(Alert.AlertType.ERROR, "❌ Failed to delete announcement.").showAndWait();
                        }
                    });

                    annBox.getChildren().add(deleteBtn);
                }

                announcementList.getChildren().add(annBox);
            }
        } catch (SQLException ex) {
            announcementList.getChildren().add(new Label("Failed to load announcements."));
            ex.printStackTrace();
        }

        ScrollPane scrollPane = new ScrollPane(announcementList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(250);

        CustomButton backButton = new CustomButton("← Back", CustomButton.ColorPreset.GREY, e -> {
            var roles = StartCSE360.loggedInUser.getRole();

            if (roles.contains(UserRole.ADMIN)) {
                new AdminHomePage(StartCSE360.databaseHelper).show(primaryStage);
            } else if (roles.contains(UserRole.INSTRUCTOR)) {
                new InstructorHomePage(StartCSE360.databaseHelper).show(primaryStage);
            } else if (roles.contains(UserRole.STAFF)) {
                new StaffHomePage(StartCSE360.databaseHelper).show(primaryStage);
            } else {
                new UserHomePage(StartCSE360.databaseHelper).show(primaryStage);  // fallback
            }
        });


        layout.getChildren().addAll(scrollPane, backButton);

        Scene scene = new Scene(layout, StartCSE360.WIDTH, StartCSE360.HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff Announcements");
    }
}
