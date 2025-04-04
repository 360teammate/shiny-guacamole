package UIComponents;

import Application.Answer;
import Application.Question;
import Application.StartCSE360;
import Application.UserRole;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.sql.SQLException;
import java.util.UUID;

public class ReplyThreadCard extends Card {

    public ReplyThreadCard(Answer answer, Question question, Runnable reloadReplies) {
        super();

        // Author + Date
        Label authorLabel = new Label("Reply by " + answer.getAuthor() + " on " + answer.getDateAsString());
        if (!answer.getDateAsString().equals(answer.getEditedDateAsString())) {
            authorLabel.setText("Edited by " + answer.getAuthor() + " on " + answer.getEditedDateAsString());
        }
        authorLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray; -fx-font-style: italic;");

        // Body Text (default view)
        Label bodyLabel = new Label(answer.getBodyText());
        bodyLabel.setFont(Font.font("Arial", 14));
        bodyLabel.setWrapText(true);

        // Edit Field + Save
        TextArea editField = new TextArea(answer.getBodyText());
        editField.setWrapText(true);

        CustomButton saveEditButton = new CustomButton("Save", CustomButton.ColorPreset.BLUE, e -> {
            answer.editBodyText(editField.getText().trim());
            try {
                StartCSE360.databaseHelper.updateAnswer(answer);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            reloadReplies.run();
        });

        // Reply input
        TextArea replyField = new TextArea();
        replyField.setWrapText(true);
        replyField.setPromptText("Enter your reply...");

        CustomButton saveReplyButton = new CustomButton("Save", CustomButton.ColorPreset.GREY, e -> {
            UUID replyID = StartCSE360.answers.newAnswer(answer, replyField.getText().trim(), StartCSE360.loggedInUser.getUserName());
            answer.addChild(replyID);
            try {
				StartCSE360.databaseHelper.updateAnswer(answer);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            reloadReplies.run();
        });

        // Edit & Reply buttons
        CustomButton replyButton = new CustomButton("Reply", CustomButton.ColorPreset.GREY, e -> {
            if (!this.getChildren().contains(replyField)) {
                this.getChildren().addAll(replyField, saveReplyButton);
            }
        });

        HBox interactions = new HBox(10, replyButton);

        // Only show Edit if current user is the author
        if (StartCSE360.loggedInUser.getUserName().equals(answer.getAuthor())) {
            CustomButton editButton = new CustomButton("Edit", CustomButton.ColorPreset.GREY);
            editButton.setOnAction(e -> {
                this.getChildren().setAll(authorLabel, editField, saveEditButton, interactions);
            });
            interactions.getChildren().add(editButton);
        }

        if (answer.getUUID().equals(question.getResolvingChild())) {
            Label checkmark = new Label("✓ Verified Answer");
            checkmark.setStyle("-fx-font-size: 18px; -fx-text-fill: green; -fx-font-weight: bold;");
            interactions.getChildren().add(checkmark);
        } else {
            String currentUser = StartCSE360.loggedInUser.getUserName();
            boolean isAuthor = currentUser.equals(question.getAuthor());

            boolean hasPermission = StartCSE360.loggedInUser.getRole().stream().anyMatch(role ->
                role == UserRole.ADMIN || role == UserRole.REVIEWER || role == UserRole.INSTRUCTOR
            );

            if (isAuthor || hasPermission) {
                CustomButton markAsSolution = new CustomButton("Mark as Solution", CustomButton.ColorPreset.BLUE, e -> {
                    question.resolveQuestion(answer.getUUID());
                    try {
                        StartCSE360.databaseHelper.updateQuestion(question);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    reloadReplies.run();
                });
                interactions.getChildren().add(markAsSolution);
            }
            
            // Show the "Delete Solution" button if the user is a Reviewer and it's an Expert Solution
            if (!StartCSE360.loggedInUser.getRole().contains(UserRole.REVIEWER)) {
            	CustomButton deleteSolution = new CustomButton(
            		    answer.getBodyText().startsWith("Reviewer Solution:") ? "Delete Solution" : "Delete Reply", 
            		    CustomButton.ColorPreset.GREY, e -> {
            		        // Remove the answer from the list
            		        StartCSE360.answers.deleteAnswer(answer.getUUID());

            		        // (Might not be needed) If it's a resolved answer for the question, remove it from the question
            		        if (question.getResolvingChild().equals(answer.getUUID())) {
            		            question.removeResolvingChild(); // Use the new method to remove the question
            		        }

            		        // Check if the question has no more answers
            		        if (question.getChildren().isEmpty()) {
            		            StartCSE360.questions.deleteQuestion(question.getUUID());
            		        }

            		        reloadReplies.run();
            		    });
            		interactions.getChildren().add(deleteSolution);

            }
        }
        
        this.getChildren().addAll(authorLabel, bodyLabel, interactions);

        // Recursively add children
        System.out.printf("Created reply for: '%s', with children '%d'", answer.getBodyText(), answer.getChildren().size());
        for (UUID grandchild : answer.getChildren()) {
            this.getChildren().add(new ReplyThreadCard(StartCSE360.answers.getAnswer(grandchild), question, reloadReplies));
        }
    }
}
