	package UIComponents;

import javafx.scene.control.Button;

public class CustomButton extends Button {

	public enum ColorPreset {
		GREY("#9E9E9E", "#757575"),
		BLUE("#2196F3", "#1976D2"),
		GREEN("#4CAF50", "#45A049"),
		RED("#F44336", "#D32F2F");

		public final String baseColor;
		public final String hoverColor;

		ColorPreset(String baseColor, String hoverColor) {
			this.baseColor = baseColor;
			this.hoverColor = hoverColor;
		}
	}

	private ColorPreset preset;

	public CustomButton(String text) {
		super(text);
		this.preset = ColorPreset.GREEN; // Default to green
		applyStyle(preset.baseColor, preset.hoverColor);
	}

	public CustomButton(String text, ColorPreset preset) {
		super(text);
		this.preset = preset;
		applyStyle(preset.baseColor, preset.hoverColor);
	}
	
	public CustomButton(String text, ColorPreset preset, javafx.event.EventHandler<javafx.event.ActionEvent> event) {
		super(text);
		this.preset = preset;
		applyStyle(preset.baseColor, preset.hoverColor);
		this.setOnAction(event);
	}

	private void applyStyle(String baseColor, String hoverColor) {
		String baseStyle = getStyleString(baseColor);
		String hoverStyle = getStyleString(hoverColor);

		this.setStyle(baseStyle);
		this.setOnMouseEntered(e -> this.setStyle(hoverStyle));
		this.setOnMouseExited(e -> this.setStyle(baseStyle));
	}

	private String getStyleString(String bgColor) {
		return "-fx-background-color: " + bgColor + "; " +
		       "-fx-text-fill: white; " +
		       "-fx-border-radius: 5px; " +
		       "-fx-background-radius: 5px;";
	}
}
