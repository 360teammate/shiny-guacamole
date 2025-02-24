package application;

import databasePart1.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FirstPage class represents the initial screen for the first user.
 * It prompts the user to set up administrator access and navigate to the setup screen.
 */
public class FirstPage {
    private final DatabaseHelper databaseHelper;

    public FirstPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(5);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label userLabel = new Label("Hello..You are the first person here.\nPlease select continue to setup administrator access");
        userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button continueButton = new Button("Continue");
        continueButton.setOnAction(a -> new AdminSetupPage(databaseHelper).show(primaryStage));

        layout.getChildren().addAll(userLabel, continueButton);
        Scene firstPageScene = new Scene(layout, 800, 400);

        primaryStage.setScene(firstPageScene);
        primaryStage.setTitle("First Page");
        primaryStage.show();
    }
}
