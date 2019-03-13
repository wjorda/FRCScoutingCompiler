module compiler {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    opens org.team2363.scouting.compiler to javafx.fxml;
    exports org.team2363.scouting.compiler;
    exports org.team2363.jbluealliance;
}