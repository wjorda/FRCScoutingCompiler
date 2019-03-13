package org.team2363.scouting.compiler.modals;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Displays an OK/Cancel confirmation box.
 * Used if the user needs to confirm or deny an action.
 * @author Bryce Davis
 */
public class ConfirmationBox {
    // These are static because these dialog boxes are considered "singleton"
    // objects, or objects that only have one instance. Normally this instance
    // is created via a static class method, and is the case here.
    static Stage stage;
    static boolean confirm;

    /**
     * Display a confirmation box.
     * @param msg The message to display
     * @param title The title of the box
     * @param yesText The text for the confirm button
     * @param noText The text for the deny button
     * @return <code>true</code> if the user confirms, <code>false</code> otherwise
     */
    public static boolean show(String msg, String title,
                               String yesText, String noText) {
        // We will use this boolean to return the user's choice
        confirm = false;

        // Again, we are initializing a modal stage
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(250);

        // Create our various GUI elements
        Label lbl = new Label();
        lbl.setText(msg);

        Button confirmBtn = new Button();
        confirmBtn.setText(yesText);
        confirmBtn.setOnAction(e -> confirmBtn_Click());

        Button denyBtn = new Button();
        denyBtn.setText(noText);
        denyBtn.setOnAction(e -> denyBtn_Click());

        // Here is an example of using multiple layout panes to organize our
        // elements. The buttons are arranged horizontally in an HBox, but the
        // Label and this HBox are arranged vertically in a VBox.
        HBox buttonsPane = new HBox(20);
        buttonsPane.getChildren().addAll(confirmBtn, denyBtn);

        VBox pane = new VBox(20);
        pane.getChildren().addAll(lbl, buttonsPane);
        pane.setAlignment(Pos.CENTER);

        // Let's set the scene!
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();

        return confirm;
    }

    /**
     * Event handler for the confirmation button.
     * Closes the stage and sets that the user confirmed.
     */
    private static void confirmBtn_Click()
    {
        stage.close();
        confirm = true;
    }

    /**
     * Event handler for the deny button.
     * Closes the stage and sets that the user denied.
     */
    private static void denyBtn_Click()
    {
        stage.close();
        confirm = false;
    }
}
