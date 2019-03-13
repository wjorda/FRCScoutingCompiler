package org.team2363.scouting.compiler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MatchRow
{
	private HBox view;

	//Java FXML initialization guff
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private MenuButton mbtn_match_num;

	@FXML
	private Button btn_red1;

	@FXML
	private Button btn_blue1;

	@FXML
	private Button btn_blue2;

	@FXML
	private Button btn_blue3;

	@FXML
	private MenuItem mitem_scouting_report;

	@FXML
	private Button btn_red3;

	@FXML
	private Button btn_red2;

	@FXML
	void initialize() {
		assert btn_red1 != null : "fx:id=\"btn_red1\" was not injected: check your FXML file 'match_row.fxml'.";
		assert btn_blue1 != null : "fx:id=\"btn_blue1\" was not injected: check your FXML file 'match_row.fxml'.";
		assert btn_blue2 != null : "fx:id=\"btn_blue2\" was not injected: check your FXML file 'match_row.fxml'.";
		assert btn_blue3 != null : "fx:id=\"btn_blue3\" was not injected: check your FXML file 'match_row.fxml'.";
		assert mitem_scouting_report != null : "fx:id=\"mitem_scouting_report\" was not injected: check your FXML file 'match_row.fxml'.";
		assert btn_red3 != null : "fx:id=\"btn_red3\" was not injected: check your FXML file 'match_row.fxml'.";
		assert btn_red2 != null : "fx:id=\"btn_red2\" was not injected: check your FXML file 'match_row.fxml'.";
	}
	
	private int num, red1, red2, red3, blue1, blue2, blue3;
	
	public static MatchRow getInstance(int matchNum) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(MatchRow.class.getResource("/match_row.fxml"));
		ManagementScreen mgmt = ManagementScreen.getInstance();
		HBox root = loader.load();
		MatchRow instance = loader.getController();
		
		instance.view = root;
		instance.num = matchNum;
		instance.red1 = mgmt.getMatchData(matchNum, ManagementScreen.RED1).getTeamNum();
		instance.red2 = mgmt.getMatchData(matchNum, ManagementScreen.RED2).getTeamNum();
		instance.red3 = mgmt.getMatchData(matchNum, ManagementScreen.RED3).getTeamNum();
		instance.blue1 = mgmt.getMatchData(matchNum, ManagementScreen.BLUE1).getTeamNum();
		instance.blue2 = mgmt.getMatchData(matchNum, ManagementScreen.BLUE2).getTeamNum();
		instance.blue3 = mgmt.getMatchData(matchNum, ManagementScreen.BLUE3).getTeamNum();
		
		instance.mbtn_match_num.setText("Q" + matchNum);
		instance.btn_red1.setText(Integer.toString(instance.red1));
		instance.btn_red2.setText(Integer.toString(instance.red2));
		instance.btn_red3.setText(Integer.toString(instance.red3));
		instance.btn_blue1.setText(Integer.toString(instance.blue1));
		instance.btn_blue2.setText(Integer.toString(instance.blue2));
		instance.btn_blue3.setText(Integer.toString(instance.blue3));

		instance.updateButtons();
		
		return instance;
	}

	public HBox getView()
	{
		return view;
	}

	public void updateButtons()
	{
		ManagementScreen mgmt = ManagementScreen.getInstance();

		btn_red1.setDisable(!mgmt.getMatchData(num, ManagementScreen.RED1).hasData());
		btn_red2.setDisable(!mgmt.getMatchData(num, ManagementScreen.RED2).hasData());
		btn_red3.setDisable(!mgmt.getMatchData(num, ManagementScreen.RED3).hasData());
		btn_blue1.setDisable(!mgmt.getMatchData(num, ManagementScreen.BLUE1).hasData());
		btn_blue2.setDisable(!mgmt.getMatchData(num, ManagementScreen.BLUE2).hasData());
		btn_blue3.setDisable(!mgmt.getMatchData(num, ManagementScreen.BLUE3).hasData());
	}

	@FXML
	void onclick_red1(MouseEvent event) {
		System.out.println(num + " " + red1);
		ManagementScreen.getInstance().changeSelectedTeam(ManagementScreen.getInstance().getMatchData(num, ManagementScreen.RED1));
	}

	@FXML
	void onclick_red2(MouseEvent event) {
		System.out.println(num + " " + red2);
		ManagementScreen.getInstance().changeSelectedTeam(ManagementScreen.getInstance().getMatchData(num, ManagementScreen.RED2));
	}

	@FXML
	void onclick_red3(MouseEvent event) {
		System.out.println(num + " " + red3);
		ManagementScreen.getInstance().changeSelectedTeam(ManagementScreen.getInstance().getMatchData(num, ManagementScreen.RED3));
	}

	@FXML
	void onclick_blue1(MouseEvent event) {
		System.out.println(num + " " + blue1);
		ManagementScreen.getInstance().changeSelectedTeam(ManagementScreen.getInstance().getMatchData(num, ManagementScreen.BLUE1));
	}

	@FXML
	void onclick_blue2(MouseEvent event) {
		System.out.println(num + " " + blue2);
		ManagementScreen.getInstance().changeSelectedTeam(ManagementScreen.getInstance().getMatchData(num, ManagementScreen.BLUE2));
	}

	@FXML
	void onclick_blue3(MouseEvent event) {
		System.out.println(num + " " + blue3);
		ManagementScreen.getInstance().changeSelectedTeam(ManagementScreen.getInstance().getMatchData(num, ManagementScreen.BLUE3));
	}

	@FXML
	void onclick_mItemScoutingReport(ActionEvent event) {
		MatchHistoryViewer.showMultipleInstances(num, red1, red2, red3, blue1, blue2, blue3);
	}
}
