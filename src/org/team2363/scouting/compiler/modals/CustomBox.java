package org.team2363.scouting.compiler.modals;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomBox {
    private static Stage stage;

    public static void show(Scene scene, String title) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.showAndWait();
    }
}
