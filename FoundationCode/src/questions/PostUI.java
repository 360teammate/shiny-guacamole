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
        layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #d3d3d3; -fx-border-width: 1px; -fx-border-radius: 5px;");

        HBox editBack = createEditBackButtons(primaryStage);
        HBox titleRow = createTitleRow();
        bodyLabel = createBodyLabel();
        replyContainer = new VBox(10);
        replyContainer.setPadding(new Insets(10, 0, 0, 0));

        loadReplies();

        layout.getChildren().addAll(editBack, titleRow, bodyLabel, createReplyInputCard(), replyContainer);

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
        Label authorLabel = new Label("Reply by " + answer.getAuthor() + " on " + answer.getDateAsString());
        authorLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray; -fx-font-style: italic;");

        Label bodyLabel = new Label(answer.getBodyText());
        bodyLabel.setWrapText(true);
        bodyLabel.setFont(Font.font("Arial", 14));

        VBox replyCard = new VBox(5, authorLabel, bodyLabel);
        replyCard.setPadding(new Insets(10));
        replyCard.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 15px;");

        if (answer.getUUID().equals(question.getResolvingChild())) {
            Label checkmark = new Label("✓ Verified Answer");
            checkmark.setStyle("-fx-font-size: 18px; -fx-text-fill: green; -fx-font-weight: bold;");
            replyCard.getChildren().add(checkmark);
        } else {
            Button resolvesQuestion = createStyledButton("Mark as Solution", e -> {
                question.resolveQuestion(answer.getUUID());
                try {
                    StartCSE360.databaseHelper.updateQuestion(question);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                loadReplies();
            });
            replyCard.getChildren().add(resolvesQuestion);
        }

        return replyCard;
    }

    private VBox createReplyInputCard() {
        Label replyLabel = new Label("Write a Reply:");
        replyLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextArea replyField = new TextArea();
        replyField.setPromptText("Enter your reply...");
        replyField.setWrapText(true);
        replyField.setPrefRowCount(3);

        Button commentButton = createStyledButton("Comment", e -> {
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

    private void edit() {
        layout.getChildren().clear();
        TextField titleField = new TextField(question.getTitle());
        TextArea bodyField = new TextArea(question.getBodyText());
        bodyField.setWrapText(true);

        Button saveButton = createStyledButton("Save", e -> {
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

    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> event) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #ccc; -fx-text-fill: black; -fx-border-radius: 5px;");
        button.setOnAction(event);
        return button;
    }
}
