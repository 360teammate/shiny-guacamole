module FoundationCode {
	requires javafx.controls;
	requires java.sql;
	requires javafx.graphics;
	requires junit;
	
	opens Application to javafx.graphics, javafx.fxml;
}