package org.team2363.scouting.compiler;

import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MatchHistoryViewer
{

	//FXML initialization guff

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TableView<MatchData> tbl_match_data;
	private Parent view;

	private int team;
	private ObservableList<MatchData> teamData;

	public static MatchHistoryViewer getInstance(int team)
	{
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(MatchHistoryViewer.class.getResource("../../../../../resources/match_history.fxml"));
			Parent root = fxmlLoader.load();
			MatchHistoryViewer instance = fxmlLoader.getController();
			instance.team = team;
			instance.view = root;
			instance.teamData = ManagementScreen.getInstance().getMatchData(team, true);
			instance.initTable();
			return instance;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void initTable()
	{
		List<Field> fields = ManagementScreen.getInstance().getFields();
		TableColumn<MatchData, IntegerProperty> numCol = new TableColumn<>("Match #");
		numCol.setCellValueFactory(new PropertyValueFactory<>("matchNum"));
		numCol.setSortType(TableColumn.SortType.ASCENDING);
		tbl_match_data.getSortOrder().add(numCol);
		tbl_match_data.getColumns().add(numCol);

		for (Field field : fields) {
			String name = field.optString("name");
			if(name.equals("") || name.equals(null)) name = field.getString("id");
			TableColumn<MatchData, String> tableColumn = new TableColumn<>(name);
			tableColumn.setCellValueFactory(new MatchData.MatchDataCallback(field.getString("id"), getFieldName(field.getString("type"))));
			tableColumn.setSortType(TableColumn.SortType.ASCENDING);
			tbl_match_data.getSortOrder().add(tableColumn);
			tbl_match_data.getColumns().add(tableColumn);
		}

		tbl_match_data.setItems(teamData);
		tbl_match_data.setPrefHeight(100+30 * teamData.size());
		tbl_match_data.setPrefWidth(1600);
	}

	public void show()
	{
		Stage stage = new Stage();
		stage.setTitle("Team " + team + " Match History");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(new Scene(view, 300, 200));
		stage.show();
	}

	public Parent getView()
	{
		return view;
	}

	public static MatchHistoryViewer[] showMultipleInstances(int match, int... teams)
	{
		GridPane pane = new GridPane();
		MatchHistoryViewer[] viewers = new MatchHistoryViewer[teams.length];

		for (int i = 0; i < teams.length; i++) {
			viewers[i] = MatchHistoryViewer.getInstance(teams[i]);
			Parent p = viewers[i].getView();
			VBox wrapper = new VBox(4, new Label("Team " + teams[i]), p);
			wrapper.setPadding(new Insets(4));
			GridPane.setRowIndex(wrapper, i);
			GridPane.setColumnIndex(wrapper, 0);
			pane.getChildren().add(wrapper);
		}

		pane.setPadding(new Insets(4));

		Stage stage = new Stage();
		stage.setTitle("Alliance Scouting Report: Match Q" + match);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(new Scene(pane, Pane.USE_COMPUTED_SIZE, Pane.USE_COMPUTED_SIZE));
		stage.show();

		return viewers;
	}

	public static String getFieldName(String field)
	{
		switch(field)
		{
			case "toteStacker": return "stacks";
			default: return field;
		}
	}
}
