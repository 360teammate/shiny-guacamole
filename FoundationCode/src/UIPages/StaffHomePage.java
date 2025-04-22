package UIPages;

import Application.StartCSE360;
import Database.DatabaseHelper;
import UIPages.PostsBrowsePage;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StaffHomePage {

    private DatabaseHelper databaseHelper;

    public StaffHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage stage) {
        // Layout setup
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 30; -fx-background-color: #f4f4f4;");

        // Title
        Label title = new Label("Staff Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button announcementButton = createStyledButton("📢 Post Announcement");
        announcementButton.setOnAction(e -> new StaffAnnouncementPage().show(stage));

        Button reviewerButton = createStyledButton("🔍 Review Role Requests");
        reviewerButton.setOnAction(e -> new ReviewRequestPage().show(stage));

        Button viewpostButton = createStyledButton("📄 View Posts");
        viewpostButton.setOnAction(e -> new PostsBrowsePage(StartCSE360.questions).show(stage));

        Button quitButton = createStyledButton("❌ Quit", true);
		quitButton.setOnAction(a -> {
			StartCSE360.databaseHelper.closeConnection();
			Platform.exit();
		});

        // Add everything to layout
        layout.getChildren().addAll(title, viewpostButton, announcementButton, reviewerButton, quitButton);

        // Finalize and show scene
        Scene staffScene = new Scene(layout, StartCSE360.WIDTH, StartCSE360.HEIGHT);
        stage.setScene(staffScene);
        stage.setTitle("Staff Dashboard");
        stage.show();
    }

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
