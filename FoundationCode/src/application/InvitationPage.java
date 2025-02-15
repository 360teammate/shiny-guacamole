package application;

import databasePart1.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * InvitePage class represents the page where an admin can generate an invitation code.
 * The invitation code is displayed upon clicking a button.
 */
public class InvitationPage {

    public void show(DatabaseHelper databaseHelper, Stage primaryStage) {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label userLabel = new Label("Invite ");
        userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button showCodeButton = new Button("Generate Invitation Code");

        Label inviteCodeLabel = new Label("");
        inviteCodeLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");

        showCodeButton.setOnAction(a -> {
            String invitationCode = databaseHelper.generateInvitationCode();
            inviteCodeLabel.setText(invitationCode);
        });

        layout.getChildren().addAll(userLabel, showCodeButton, inviteCodeLabel);
        Scene inviteScene = new Scene(layout, 800, 400);

        primaryStage.setScene(inviteScene);
        primaryStage.setTitle("Invite Page");
    }
}
