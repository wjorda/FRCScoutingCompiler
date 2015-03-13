package org.team2363.scouting.compiler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

/**
 * Main launcher class.
 */
public class Main extends Application {

	private static Stage primaryStage;

	private ManagementScreen window;

	public static Stage getStage()
	{
		return primaryStage;
	}

	@Override
    public void start(Stage primaryStage) throws Exception {
	    window = ManagementScreen.getInstance();
	    this.primaryStage = primaryStage;
        primaryStage.setTitle("Triple Helix Scouting Manager");
        primaryStage.setScene(new Scene(window.getView(), 900, 650));
        primaryStage.show();
		primaryStage.onCloseRequestProperty().setValue(event -> window.requestFinish());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
