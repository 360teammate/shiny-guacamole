package UIPages;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import Application.Answer;
import Application.AnswerList;
import Application.RoleRequest;
import Application.StartCSE360;
import Application.User;
import Application.UserRole;
import Database.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class RequestsPage {
    private final DatabaseHelper databaseHelper;
    private ArrayList<RoleRequest> requests = new ArrayList<>();

    public RequestsPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        try {
			loadUserData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void loadUserData() throws SQLException{
    	requests = databaseHelper.getRoleRequests();
    }

    public void show(Stage primaryStage) {
    	
    	ArrayList<User> test;
		try {
			test = databaseHelper.getPendingRequests();
	    	for (User author : test) {
	    		System.out.println(author.getUserName());
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 30; -fx-background-color: #f4f4f4;");

        Label title = new Label("Reviewer List");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label subtitle = new Label("Erm wat da sigma???");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");
        
        // Back button
        Button backButton = createStyledButton("🔙 Back");
        if (StartCSE360.loggedInUser.getRole().contains(UserRole.ADMIN)) {
        	backButton.setOnAction(e -> new AdminHomePage(databaseHelper).show(primaryStage));
        } else if (StartCSE360.loggedInUser.getRole().contains(UserRole.REVIEWER)) {
        	backButton.setOnAction(e -> new ReviewerHomePage(databaseHelper).show(primaryStage));
        } else {
        	backButton.setOnAction(e -> new UserHomePage(databaseHelper).show(primaryStage));
        }

        VBox userListBox = new VBox(10);
        userListBox.setAlignment(Pos.CENTER);

        for (RoleRequest request : requests) {
        	
            String user = request.getAuthor();

            Label userLabel = new Label("👤 " + user);
            userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            
            Label requestText = new Label("Request: " + request.getText());
            
            // Error label for validation messages
            Label errorLabel = new Label();
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            
            

            VBox userBox = new VBox(5, userLabel, requestText, backButton, errorLabel);
            
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
