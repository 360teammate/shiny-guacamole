package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/*******
 * A JavaFX demonstration application for testing password evaluation UI.
 */
public class PasswordEvaluationGUITestbed extends Application {

    public final static double WINDOW_WIDTH = 500;
    public final static double WINDOW_HEIGHT = 430;

    public UserInterface theGUI;

    @Override
    public void start(Stage theStage) throws Exception {
        theStage.setTitle("Password Evaluator Demo");

        Pane theRoot = new Pane();
        theGUI = new UserInterface(theRoot);

        Scene theScene = new Scene(theRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
        theStage.setScene(theScene);
        theStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
