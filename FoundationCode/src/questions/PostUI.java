package questions;

import java.util.UUID;
import java.sql.SQLException;
import java.util.ArrayList;

import application.StartCSE360;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PostUI {
    private Question question;
    private VBox replyContainer; // Holds all replies
    private VBox layout; // Main layout

    public PostUI(Question question) {
        this.question = question;
    }

    public void show(Stage primaryStage) {
        // ** Back Button **
        Button backButton = new Button("← Back");
        backButton.setStyle("-fx-font-size: 14px; -fx-background-color: #ddd; -fx-border-radius: 5px; -fx-cursor: hand;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-font-size: 14px; -fx-background-color: #ccc; -fx-border-radius: 5px; -fx-cursor: hand;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-font-size: 14px; -fx-background-color: #ddd; -fx-border-radius: 5px; -fx-cursor: hand;"));
        backButton.setOnAction(e -> new PostListUI(StartCSE360.questions).show(primaryStage));

        // ** Title Label **
        Label titleLabel = new Label(question.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setWrapText(true);

        // ** Author and Date Label **
        Label authorDateLabel = new Label("Posted by " + question.getAuthor() + " on " + question.getDateAsString());
        authorDateLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        authorDateLabel.setStyle("-fx-font-style: italic; -fx-text-fill: gray;");

        // Spacer to align title and author info
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox titleRow = new HBox(titleLabel, spacer, authorDateLabel);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        titleRow.setPadding(new Insets(5, 0, 10, 0));

        // ** Body Text **
        Label bodyLabel = new Label(question.getBodyText());
        bodyLabel.setFont(Font.font("Arial", 14));
        bodyLabel.setWrapText(true);

        // ** Reply Button **
        Button replyButton = new Button("Reply");
        replyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5px;");
        replyButton.setOnMouseEntered(e -> replyButton.setStyle("-fx-background-color: #45A049; -fx-text-fill: white; -fx-border-radius: 5px;"));
        replyButton.setOnMouseExited(e -> replyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5px;"));

        // Reply input card (initially hidden)
        VBox replyInputCard = createReplyInputCard(primaryStage);

        // Show reply input card when clicking reply button
        replyButton.setOnAction(e -> {
            if (layout.getChildren().contains(replyInputCard)) {
                layout.getChildren().remove(replyInputCard); // Hide when clicked again
            } else {
                layout.getChildren().add(4, replyInputCard); // Show it right after the reply button
            }
        });

        // ** Reply Container (Holds all Replies) **
        replyContainer = new VBox();
        replyContainer.setSpacing(10);
        replyContainer.setPadding(new Insets(10, 0, 0, 0));

        // ** Load Replies **
        loadReplies();

        // ** Layout **
        layout = new VBox(10, backButton, titleRow, bodyLabel, replyButton, replyContainer);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #d3d3d3; -fx-border-width: 1px; -fx-border-radius: 5px;");

        // ** ScrollPane for long posts **
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        // ** Scene Setup **
        Scene scene = new Scene(scrollPane, 800, 400);
        primaryStage.setTitle(question.getTitle());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ** Creates the Reply Input Card **
    private VBox createReplyInputCard(Stage primaryStage) {
        Label replyLabel = new Label("Write a Reply:");
        replyLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextArea replyField = new TextArea();
        replyField.setPromptText("Enter your reply...");
        replyField.setWrapText(true);
        replyField.setPrefRowCount(3);
        replyField.setMaxWidth(Double.MAX_VALUE);

        Button commentButton = new Button("Comment");
        commentButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-border-radius: 5px;");
        commentButton.setOnMouseEntered(e -> commentButton.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-border-radius: 5px;"));
        commentButton.setOnMouseExited(e -> commentButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-border-radius: 5px;"));
        
        VBox replyCard = new VBox(10, replyLabel, replyField, commentButton);
        replyCard.setPadding(new Insets(10));
        replyCard.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 15px;");
        replyCard.setMaxWidth(Double.MAX_VALUE);

        commentButton.setOnAction(e -> {
            String replyText = replyField.getText().trim();
            if (!replyText.isEmpty()) {
                // Generate a new Answer object (assuming Answer class exists)
                UUID answerID = StartCSE360.answers.newAnswer(question, replyText, "User123");
                question.addChild(answerID);
                layout.getChildren().remove(replyCard);
                try {
					StartCSE360.databaseHelper.updateQuestion(question);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

                // Refresh replies and clear input field
                loadReplies();
                replyField.clear();
            }
        });


        return replyCard;
    }

    // ** Load Replies into the Reply Container **
    private void loadReplies() {
        replyContainer.getChildren().clear();
        ArrayList<UUID> children = question.getChildren();

        for (int i = 0; i < children.size(); i++) {
            UUID answerID = children.get(i);
            Answer answer = StartCSE360.answers.getAnswer(answerID);

            VBox replyCard = createReplyCard(answer);

            replyContainer.getChildren().add(replyCard);

            // Add a separator after each reply, except the last one
            if (i < children.size() - 1) {
                replyContainer.getChildren().add(new Separator());
            }
        }
    }

    // ** Creates a Reply Card **
    private VBox createReplyCard(Answer answer) {
        Label authorLabel = new Label("Reply by " + answer.getAuthor() + " on " + answer.getDateAsString());
        authorLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray; -fx-font-style: italic;");

        Label bodyLabel = new Label(answer.getBodyText());
        bodyLabel.setWrapText(true);
        bodyLabel.setFont(Font.font("Arial", 14));

        VBox replyCard = new VBox(5, authorLabel, bodyLabel);
        replyCard.setPadding(new Insets(10));
        replyCard.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 15px;");
        replyCard.setMaxWidth(Double.MAX_VALUE);

        return replyCard;
    }
}
