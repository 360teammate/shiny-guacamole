package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The SetupLoginSelectionPage class allows users to choose between setting up
 * a new account or logging into an existing account.
 */
public class SetupLoginSelectionPage {

    private final DatabaseHelper databaseHelper;

    public SetupLoginSelectionPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        Button setupButton = new Button("SetUp");
        Button loginButton = new Button("Login");

        setupButton.setOnAction(a -> {
            new SetupAccountPage(databaseHelper).show(primaryStage);
        });
        loginButton.setOnAction(a -> {
            new UserLoginPage(databaseHelper).show(primaryStage);
        });

        VBox layout = new VBox(10, setupButton, loginButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Setup or Login");
        primaryStage.show();
    }
}
