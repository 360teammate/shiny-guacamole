package UIComponents;

import java.util.ArrayList;

import Application.StartCSE360;
import Application.UserRole;
import UIPages.AdminHomePage;
import UIPages.InstructorHomePage;
import UIPages.ReviewerHomePage;
import UIPages.SetupLoginSelectionPage;
import UIPages.StaffHomePage;
import UIPages.UserHomePage;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class NavigationCard extends Card {
	public NavigationCard(Stage primaryStage) {
		super();
		
        CustomButton quit = new CustomButton("Quit to Login", CustomButton.ColorPreset.RED);
        quit.setOnAction(event -> {
            new SetupLoginSelectionPage(StartCSE360.databaseHelper).show(primaryStage);
        });
        CustomButton  home = new CustomButton("Return Home", CustomButton.ColorPreset.GREY);
        ArrayList<UserRole> roles = StartCSE360.loggedInUser.getRole();
        home.setOnAction(a -> {
            if (roles.contains(UserRole.ADMIN)) {
                new AdminHomePage(StartCSE360.databaseHelper).show(primaryStage);
            } else if(roles.contains(UserRole.INSTRUCTOR)) {
            	new InstructorHomePage(StartCSE360.databaseHelper).show(primaryStage);
            } else if(roles.contains(UserRole.STAFF)) {
            	new StaffHomePage(StartCSE360.databaseHelper).show(primaryStage);
            } else if(roles.contains(UserRole.REVIEWER)) {
            	new ReviewerHomePage(StartCSE360.databaseHelper).show(primaryStage);
            } else if (roles.contains(UserRole.STUDENT)) {
                new UserHomePage(StartCSE360.databaseHelper).show(primaryStage);
            }
        });
        
        HBox box = new HBox(10, quit, home);
        this.getChildren().addAll(box);
	}
}
