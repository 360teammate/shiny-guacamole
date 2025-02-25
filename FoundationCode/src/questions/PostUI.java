package questions;

import java.util.UUID;
import java.sql.SQLException;
import application.StartCSE360;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PostUI {
    private Question question;
    private VBox replyContainer;
    private VBox layout;
    private Label titleLabel;
    private Label bodyLabel;
    private Label authorDateLabel;
    private VBox newPost;
    private HBox editBack;
    private HBox titleRow;
    private Button replyButton;
    private VBox replyInputCard;

    public PostUI(Question question) {
        this.question = question;
        this.replyInputCard = createReplyInputCard();
    }

    public void show(Stage primaryStage) {
        layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #d3d3d3; -fx-border-width: 1px; -fx-border-radius: 5px;");

        editBack = createEditBackButtons(primaryStage);
        titleRow = createTitleRow();
        bodyLabel = createBodyLabel();
        replyButton = createReplyButton(primaryStage);
        replyContainer = new VBox(10);
        replyContainer.setPadding(new Insets(10, 0, 0, 0));

        loadReplies();

        layout.getChildren().addAll(editBack, titleRow, bodyLabel, replyButton, replyContainer);

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        Scene scene = new Scene(scrollPane, 800, 400);
        primaryStage.setTitle(question.getTitle());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createEditBackButtons(Stage primaryStage) {
        Button backButton = createStyledButton("← Back", e -> new PostListUI(StartCSE360.questions).show(primaryStage));
        Button editButton = createStyledButton("Edit", e -> edit());
        return new HBox(10, backButton, editButton);
    }

    private Label createBodyLabel() {
        Label label = new Label(question.getBodyText());
        label.setFont(Font.font("Arial", 14));
        label.setWrapText(true);
        return label;
    }

    private Button createReplyButton(Stage primaryStage) {
        Button button = createStyledButton("Reply", e -> toggleReplyInput());
        return button;
    }

    private void toggleReplyInput() {
        if (layout.getChildren().contains(replyInputCard)) {
            layout.getChildren().remove(replyInputCard);
        } else {
            layout.getChildren().add(4, replyInputCard);
        }
    }

    private HBox createTitleRow() {
        titleLabel = new Label(question.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setWrapText(true);
        
        if (question.getDateAsString().equals(question.getEditedDateAsString())) {
        	authorDateLabel = new Label("Posted by " + question.getAuthor() + " on " + question.getDateAsString());
        } else {
        	authorDateLabel = new Label("Edited by " + question.getAuthor() + " on " + question.getEditedDateAsString());
        }
        authorDateLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        authorDateLabel.setStyle("-fx-font-style: italic; -fx-text-fill: gray;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        return new HBox(titleLabel, spacer, authorDateLabel);
    }

    private void loadReplies() {
        replyContainer.getChildren().clear();
        for (UUID answerID : question.getChildren()) {
            replyContainer.getChildren().add(createReplyCard(StartCSE360.answers.getAnswer(answerID)));
            replyContainer.getChildren().add(new Separator());
        }
    }

    private VBox createReplyCard(Answer answer) {
        Label authorLabel = new Label("Reply by " + answer.getAuthor() + " on " + answer.getDateAsString());
        authorLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray; -fx-font-style: italic;");

        Label bodyLabel = new Label(answer.getBodyText());
        bodyLabel.setWrapText(true);
        bodyLabel.setFont(Font.font("Arial", 14));

        VBox replyCard = new VBox(5, authorLabel, bodyLabel);
        replyCard.setPadding(new Insets(10));
        replyCard.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 15px;");
        return replyCard;
    }

    private VBox createReplyInputCard() {
        Label replyLabel = new Label("Write a Reply:");
        replyLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextArea replyField = new TextArea();
        replyField.setPromptText("Enter your reply...");
        replyField.setWrapText(true);
        replyField.setPrefRowCount(3);
        replyField.setMaxWidth(Double.MAX_VALUE);

        Button commentButton = createStyledButton("Comment", e -> {
            String replyText = replyField.getText().trim();
            if (!replyText.isEmpty()) {
                UUID answerID = StartCSE360.answers.newAnswer(question, replyText, "User123");
                question.addChild(answerID);
                layout.getChildren().remove(replyInputCard);
                try {
                    StartCSE360.databaseHelper.updateQuestion(question);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                loadReplies();
                replyField.clear();
            }
        });

        return new VBox(10, replyLabel, replyField, commentButton);
    }
    
    private void edit() {
        layout.getChildren().clear();
        newPost = createNewPostCard();
        layout.getChildren().add(newPost);
    }

    private void save() {
        layout.getChildren().clear();
        layout.getChildren().addAll(editBack, titleRow, bodyLabel, replyButton, replyContainer);
        reload();
        try {
			StartCSE360.databaseHelper.updateQuestion(question);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void reload() {
        titleLabel.setText(question.getTitle());
        bodyLabel.setText(question.getBodyText());
        authorDateLabel.setText("Edited by " + question.getAuthor() + " on " + question.getEditedDateAsString());
        
    }

    private VBox createNewPostCard() {
        TextField titleField = new TextField(question.getTitle());
        TextArea bodyField = new TextArea(question.getBodyText());
        bodyField.setWrapText(true);

        Button saveButton = createStyledButton("Save", e -> {
            question.editTitle(titleField.getText().trim());
            question.editBodyText(bodyField.getText().trim());
            save();
        });

        return new VBox(10, titleField, bodyField, saveButton);
    }

    
    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> event) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #ddd; -fx-text-fill: black; -fx-border-radius: 5px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #ccc; -fx-text-fill: black; -fx-border-radius: 5px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #ddd; -fx-text-fill: black; -fx-border-radius: 5px;"));
        button.setOnAction(event);
        return button;
    }
}
