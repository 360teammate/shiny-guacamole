package UIPages;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Application.Answer;
import Application.Question;
import Application.StartCSE360;
import Application.User;
import Application.UserRole;
import UIComponents.QuestionCard;
import UIComponents.ReplyThreadCard;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PostPage {
    private Question question;
    private VBox replyContainer;

    public PostPage(Question question) {
        this.question = question;
    }

    public void show(Stage primaryStage) {
        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(10));

        replyContainer = new VBox(10);
        loadReplies();

        QuestionCard questionCard = new QuestionCard(question, primaryStage, this::loadReplies);

        contentBox.getChildren().addAll(questionCard, replyContainer);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        Scene scene = new Scene(scrollPane, StartCSE360.WIDTH, StartCSE360.HEIGHT);
        primaryStage.setTitle(question.getTitle());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadReplies() {
        if (replyContainer == null) return;
        replyContainer.getChildren().clear();

        // Separate replies into staff and non-staff
        List<UUID> staffReplies = new ArrayList<>();
        List<UUID> regularReplies = new ArrayList<>();

        for (UUID answerID : question.getChildren()) {
            Answer answer = StartCSE360.answers.getAnswer(answerID);
            User author = StartCSE360.databaseHelper.getUser(answer.getAuthor());
            if (author != null && author.getRole().contains(UserRole.STAFF)) {
                staffReplies.add(answerID);
            } else {
                regularReplies.add(answerID);
            }
        }

        // Add staff replies first (highlighted)
        for (UUID answerID : staffReplies) {
            var card = new ReplyThreadCard(StartCSE360.answers.getAnswer(answerID), question, this::loadReplies);
            card.setStyle("-fx-background-color: #fff4d6; -fx-border-color: #e0b400; -fx-border-width: 2; -fx-border-radius: 8px; ");  // yellowish highlight
            replyContainer.getChildren().add(card);
        }

        // Add regular replies after
        for (UUID answerID : regularReplies) {
            replyContainer.getChildren().add(new ReplyThreadCard(StartCSE360.answers.getAnswer(answerID), question, this::loadReplies));
        }
    }

}
