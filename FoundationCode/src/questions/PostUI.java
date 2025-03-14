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

    public PostUI(Question question) {
        this.question = question;
    }

    public void show(Stage primaryStage) {
    	VBox contentBox = new VBox();
    	contentBox.setSpacing(10);
    	
        layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: white; -fx-border-color: #d3d3d3; -fx-border-width: 1px; -fx-border-radius: 5px;");

        HBox editBack = createEditBackButtons(primaryStage);
        HBox titleRow = createTitleRow();
        bodyLabel = createBodyLabel();
        replyContainer = new VBox(10);
        replyContainer.setPadding(new Insets(10, 0, 0, 0));

        loadReplies();
        layout.getChildren().addAll(editBack, titleRow, bodyLabel, createReplyInputCard());
        
        contentBox.getChildren().addAll(layout, replyContainer);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        Scene scene = new Scene(scrollPane, StartCSE360.WIDTH, StartCSE360.HEIGHT);
        primaryStage.setTitle(question.getTitle());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createEditBackButtons(Stage primaryStage) {
        Button backButton = createStyledButton("← Back", "#ddd", e -> new PostListUI(StartCSE360.questions).show(primaryStage));
        Button editButton = createStyledButton("Edit", "#ddd", e -> edit());
        return new HBox(10, backButton, editButton);
    }

    private Label createBodyLabel() {
        Label label = new Label(question.getBodyText());
        label.setFont(Font.font("Arial", 14));
        label.setWrapText(true);
        return label;
    }

    private HBox createTitleRow() {
        titleLabel = new Label(question.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setWrapText(true);

        Label authorDateLabel = new Label("Posted by " + question.getAuthor() + " on " + question.getDateAsString());
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
    	Label authorLabel = new Label("Edited by " + answer.getAuthor() + " on " + answer.getEditedDateAsString());
    	if (answer.getDateAsString().equals(answer.getEditedDateAsString())) {
    		authorLabel.setText("Reply by " + answer.getAuthor() + " on " + answer.getDateAsString());
    	}
        authorLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray; -fx-font-style: italic;");

        Label bodyLabel = new Label(answer.getBodyText());
        bodyLabel.setWrapText(true);
        bodyLabel.setFont(Font.font("Arial", 14));
        
        TextArea editField = new TextArea(answer.getBodyText());
        editField.setWrapText(true);
        
        Button saveButton = createStyledButton("Save", "#4169E1", "white", e -> {
            answer.editBodyText(editField.getText().trim());
            try {
                StartCSE360.databaseHelper.updateAnswer(answer);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            loadReplies();
        });
        
        VBox replyCard = new VBox(5, authorLabel, bodyLabel);
        replyCard.setPadding(new Insets(10));
        replyCard.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 15px;");
        
        
        Button editAnswer = createStyledButton("Edit", "#ddd", e -> {
            replyCard.getChildren().setAll(authorLabel, editField, saveButton);
        });
        
        TextArea reply = new TextArea();
        Button saveReply = createStyledButton("Save", "#ddd", e -> {});
        saveReply.setOnAction(e -> {
        	UUID answerID = StartCSE360.answers.newAnswer(answer, reply.getText(), "User123");
            answer.addChild(answerID);
            replyCard.getChildren().removeAll(reply, saveReply);
            show((Stage) layout.getScene().getWindow());
        });
        
        Button replyToAnswer = createStyledButton("Reply", "#ddd", e -> {
        	if (!replyCard.getChildren().contains(reply)) {
        		replyCard.getChildren().addAll(reply, saveReply);
        	}
        });
        
        HBox interactions = new HBox(10, replyToAnswer, editAnswer);
        
        if (answer.getUUID().equals(question.getResolvingChild())) {
            Label checkmark = new Label("✓ Verified Answer");
            checkmark.setStyle("-fx-font-size: 18px; -fx-text-fill: green; -fx-font-weight: bold;");
            interactions.getChildren().add(checkmark);
        } else {
            Button resolvesQuestion = createStyledButton("Mark as Solution", "#4169E1", "white", e -> {
                question.resolveQuestion(answer.getUUID());
                try {
                    StartCSE360.databaseHelper.updateQuestion(question);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                loadReplies();
            });
            interactions.getChildren().add(resolvesQuestion);
        }
        
        replyCard.getChildren().add(interactions);
        
        for (UUID grandchild : answer.getChildren()) {
        	replyCard.getChildren().addAll(new Region(), createReplyCard(StartCSE360.answers.getAnswer(grandchild)));
        }
        return replyCard;
    }
    
    private void edit() {
        layout.getChildren().clear();
        TextField titleField = new TextField(question.getTitle());
        TextArea bodyField = new TextArea(question.getBodyText());
        bodyField.setWrapText(true);

        Button saveButton = createStyledButton("Save", "#4169E1", "white", e -> {
            question.editTitle(titleField.getText().trim());
            question.editBodyText(bodyField.getText().trim());
            try {
                StartCSE360.databaseHelper.updateQuestion(question);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            show((Stage) layout.getScene().getWindow());
        });

        layout.getChildren().addAll(titleField, bodyField, saveButton);
    }

    private VBox createReplyInputCard() {
        Label replyLabel = new Label("Write a Reply:");
        replyLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextArea replyField = new TextArea();
        replyField.setPromptText("Enter your reply...");
        replyField.setWrapText(true);
        replyField.setPrefRowCount(3);

        Button commentButton = createStyledButton("Comment", "#ddd", e -> {
            String replyText = replyField.getText().trim();
            if (!replyText.isEmpty()) {
                UUID answerID = StartCSE360.answers.newAnswer(question, replyText, "User123");
                question.addChild(answerID);
                try {
                    StartCSE360.databaseHelper.updateQuestion(question);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                loadReplies();
                replyField.clear();
            }
        });

        return new VBox(10, replyLabel, replyField, commentButton);
    }

    private Button createStyledButton(String text, String color, javafx.event.EventHandler<javafx.event.ActionEvent> event) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: black; -fx-border-radius: 5px;");
        button.setOnAction(event);
        return button;
    }
    
    private Button createStyledButton(String text, String color, String textColor, javafx.event.EventHandler<javafx.event.ActionEvent> event) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: " + textColor + "; -fx-border-radius: 5px;");
        button.setOnAction(event);
        return button;
    }
}
