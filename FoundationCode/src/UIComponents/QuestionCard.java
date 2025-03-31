package UIComponents;

import UIPages.PostsBrowsePage;
import Application.Question;
import Application.StartCSE360;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.UUID;

public class QuestionCard extends Card {

    public QuestionCard(Question question, Stage primaryStage, Runnable reloadReplies) {
        super();

        // NAV BAR
        CustomButton back = new CustomButton("← Back", CustomButton.ColorPreset.GREY,
                e -> new PostsBrowsePage(StartCSE360.questions).show(primaryStage));

        CustomButton edit = new CustomButton("Edit", CustomButton.ColorPreset.GREY, e -> showEditForm(question, primaryStage));

        HBox navBar = new HBox(10, back, edit);

        // TITLE & META
        Label titleLabel = new Label(question.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setWrapText(true);

        Label authorDateLabel = new Label("Posted by " + question.getAuthor() + " on " + question.getDateAsString());
        authorDateLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        authorDateLabel.setStyle("-fx-font-style: italic; -fx-text-fill: gray;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox titleRow = new HBox(titleLabel, spacer, authorDateLabel);
        titleRow.setAlignment(Pos.TOP_LEFT);

        // BODY
        Label bodyLabel = new Label(question.getBodyText());
        bodyLabel.setFont(Font.font("Arial", 14));
        bodyLabel.setWrapText(true);

        // === REPLY INPUT ===
        Label replyLabel = new Label("Write a Reply:");
        replyLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextArea replyField = new TextArea();
        replyField.setPromptText("Enter your reply...");
        replyField.setWrapText(true);
        replyField.setPrefRowCount(3);

        CustomButton commentButton = new CustomButton("Comment", CustomButton.ColorPreset.GREY, e -> {
            String text = replyField.getText().trim();
            if (!text.isEmpty()) {
                UUID newID = StartCSE360.answers.newAnswer(question, text, StartCSE360.loggedInUser.getUserName());
                question.addChild(newID);
                try {
                    StartCSE360.databaseHelper.updateQuestion(question);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                replyField.clear();
                reloadReplies.run();
            }
        });

        VBox replyInput = new VBox(10, replyLabel, replyField, commentButton);

        this.getChildren().addAll(navBar, titleRow, bodyLabel, replyInput);
    }

    private void showEditForm(Question question, Stage primaryStage) {
        TextField titleField = new TextField(question.getTitle());
        TextArea bodyField = new TextArea(question.getBodyText());
        bodyField.setWrapText(true);

        CustomButton save = new CustomButton("Save", CustomButton.ColorPreset.BLUE, e -> {
            question.editTitle(titleField.getText().trim());
            question.editBodyText(bodyField.getText().trim());
            try {
                StartCSE360.databaseHelper.updateQuestion(question);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            new UIPages.PostPage(question).show(primaryStage);
        });

        VBox editForm = new VBox(10, titleField, bodyField, save);
        editForm.setPadding(new Insets(10));
        primaryStage.setScene(new Scene(editForm, StartCSE360.WIDTH, StartCSE360.HEIGHT));
    }
}
