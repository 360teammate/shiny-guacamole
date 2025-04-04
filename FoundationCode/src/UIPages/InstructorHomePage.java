package UIPages;

import Application.StartCSE360;
import Database.DatabaseHelper;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InstructorHomePage {

	//TODO: Unneeded right now, but probably will be later
	//		so I left it
	private final DatabaseHelper databaseHelper;

    public InstructorHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	
    public void show(Stage primaryStage) {

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 30; -fx-background-color: #f4f4f4;");

        // Title
        Label title = new Label("Instructor Dashboard");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label subtitle = new Label("Manage users and posts");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

        // Buttons
        Button postListButton = createStyledButton("📜 View Posts");
        postListButton.setOnAction(e -> new PostsBrowsePage(StartCSE360.questions).show(primaryStage));

        //TODO: Unsure if Instructor should have userlist. Uncomment if they should
        //Button userListButton = createStyledButton("👥 User List");
        //userListButton.setOnAction(e -> new UserListPage(databaseHelper).show(primaryStage));

        Button privateMessagesButton = createStyledButton("✉️ Private Messages");
        privateMessagesButton.setOnAction(e -> new PrivateMessagesPage().show(primaryStage));

        Button roleRequestButton = createStyledButton("Manage Role Requests");
        roleRequestButton.setOnAction(e -> new RequestsPage(StartCSE360.databaseHelper).show(primaryStage));

        Button quitButton = createStyledButton("❌ Quit", true);
        quitButton.setOnAction(a -> {
            StartCSE360.databaseHelper.closeConnection();
            Platform.exit();
        });

        layout.getChildren().addAll(title, subtitle, postListButton, roleRequestButton, privateMessagesButton, quitButton);

        Scene instructorScene = new Scene(layout, StartCSE360.WIDTH, StartCSE360.HEIGHT);
        primaryStage.setScene(instructorScene);
        primaryStage.setTitle("Instructor Dashboard");
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
