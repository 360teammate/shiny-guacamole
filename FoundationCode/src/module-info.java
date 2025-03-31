module FoundationCode {
	requires javafx.controls;
	requires java.sql;
	requires javafx.graphics;
	
	opens Application to javafx.graphics, javafx.fxml;
}
