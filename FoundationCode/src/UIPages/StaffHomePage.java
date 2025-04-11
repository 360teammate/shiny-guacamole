package UIPages;

import Application.StartCSE360;
import Database.DatabaseHelper;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StaffHomePage {

	private final DatabaseHelper databaseHelper;

	public StaffHomePage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}

	public void show(Stage primaryStage) {

		VBox layout = new VBox(20);
		layout.setAlignment(Pos.CENTER);
		layout.setStyle("-fx-padding: 30; -fx-background-color: #f4f4f4;");

		Label title = new Label("Staff Dashboard");
		title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

		Label subtitle = new Label("Manage users and posts");
		subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

		Button postListButton = createStyledButton("📜 View Posts");
		postListButton.setOnAction(e -> new PostsBrowsePage(StartCSE360.questions).show(primaryStage));

		Button privateMessagesButton = createStyledButton("✉️ Private Messages");
		privateMessagesButton.setOnAction(e -> new PrivateMessagesPage().show(primaryStage));

		Button roleRequestButton = createStyledButton("Manage Role Requests");
		roleRequestButton.setOnAction(e -> new ReviewRequestPage().show(primaryStage));

		Button createAnnouncementButton = createStyledButton("📢 Create Announcement");
		createAnnouncementButton.setOnAction(e -> showCreateAnnouncementPopup());

		Button quitButton = createStyledButton("❌ Quit", true);
		quitButton.setOnAction(a -> {
			StartCSE360.databaseHelper.closeConnection();
			Platform.exit();
		});

		layout.getChildren().addAll(
			title,
			subtitle,
			postListButton,
			roleRequestButton,
			privateMessagesButton,
			createAnnouncementButton,
			quitButton
		);

		Scene instructorScene = new Scene(layout, StartCSE360.WIDTH, StartCSE360.HEIGHT);
		primaryStage.setScene(instructorScene);
		primaryStage.setTitle("Instructor Dashboard");
	}

	private void showCreateAnnouncementPopup() {
		Stage popup = new Stage();
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.setTitle("Create Announcement");

		VBox box = new VBox(15);
		box.setPadding(new Insets(20));
		box.setAlignment(Pos.CENTER);

		Label prompt = new Label("Write your announcement:");
		TextArea textArea = new TextArea();
		textArea.setPromptText("Enter announcement...");
		textArea.setWrapText(true);
		textArea.setPrefRowCount(6);

		Label confirmation = new Label();
		confirmation.setStyle("-fx-text-fill: green;");

		Button submit = new Button("Submit");
		submit.setOnAction(e -> {
			String announcement = textArea.getText().trim();
			if (!announcement.isEmpty()) {
				databaseHelper.setLatestAnnouncement(announcement);
				confirmation.setText("Announcement saved.");
			} else {
				confirmation.setText("Please enter something first.");
				confirmation.setStyle("-fx-text-fill: red;");
			}
		});

		Button close = new Button("Close");
		close.setOnAction(e -> popup.close());

		HBox buttons = new HBox(10, submit, close);
		buttons.setAlignment(Pos.CENTER);

		box.getChildren().addAll(prompt, textArea, buttons, confirmation);

		Scene scene = new Scene(box, 400, 300);
		popup.setScene(scene);
		popup.showAndWait();
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
