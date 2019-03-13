package org.team2363.scouting.compiler.modals;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BlueAllianceBox extends CustomBox
{
	private static Stage stage;
	private static TextField txt_event, txt_year;
	private static OnFinishListener listener;
	private static CheckBox checkBox;

	public static void show(OnFinishListener listener)
	{
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		BlueAllianceBox.listener = listener;

		HBox row1 = new HBox(new Label("Event Code: "));
		txt_event = new TextField();
		txt_event.setPromptText("VARI");
		row1.getChildren().add(txt_event);

		HBox row2 = new HBox(new Label("Event Year: "));
		txt_year = new TextField();
		txt_year.setPromptText("2014");
		row2.getChildren().add(txt_year);

		checkBox = new CheckBox("Generate Device Schedules");

		Button btn_confirm = new Button("OK");
		btn_confirm.onMouseClickedProperty().setValue(event -> confirm());

		stage.setScene(new Scene(new VBox(row1, row2, checkBox, btn_confirm), 400, 230));
		stage.setTitle("Select Event...");
		stage.showAndWait();
	}

	private static void confirm()
	{
		listener.onFinish(Integer.parseInt(txt_year.getText()), txt_event.getText(), checkBox.isSelected());
		stage.close();
	}

	public static interface OnFinishListener
	{
		public void onFinish(int year, String eventCode, boolean generateDeviceSchedules);
	}
}
