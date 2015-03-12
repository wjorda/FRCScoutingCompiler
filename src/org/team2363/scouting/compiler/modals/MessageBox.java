package org.team2363.scouting.compiler.modals;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Displays a modal dialog box for messages.
 * @author Bryce Davis
 */
public class MessageBox {
    /**
     * Show a <code>MessageBox</code>.
     * Display a modal dialog box with a message and a title.
     * @param msg The message to display
     * @param title The title of the message box
     */
    public static void show(String msg, String title)
    {
        // Create a new stage, or window, to display. Since we want a modal
        // dialog, we set the modality as APPLICATION_MODAL. We also set the
        // minimum width the box can be.
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(250);

        // This label will display the message
        Label lbl = new Label();
        lbl.setText(msg);

        // The button to close the dialog
        Button btn = new Button();
        btn.setText("OK");
        btn.setOnAction(e -> stage.close());

        // Position all of the elements vertically
        VBox pane = new VBox(20);
        pane.getChildren().addAll(lbl, btn);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(20));

        // Add everything to the scene and display the box
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();
    }
}

