package org.team2363.scouting.compiler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.team2363.jbluealliance.CachingBlueAllianceAPIClient;
import org.team2363.scouting.compiler.modals.BlueAllianceBox;

/**
 * The main information screen of the application.
 */
public class ManagementScreen implements ScheduleLoader.OnScheduleLoadedListener
{

	public static final int RED1 = 0, RED2 = 1, RED3 = 2, BLUE1 = 3, BLUE2 = 4, BLUE3 = 5;
	private static final FileChooser.ExtensionFilter scoutFilter = new FileChooser.ExtensionFilter("Scouting Data Files (*.scout)", "*.scout");
	private static final FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("Scouting Scoresheet Files (*.xml)", "*.xml");
	private static final FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("Comma Separated Values Files (*.csv)", "*.csv");
	private static final FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
	public static CachingBlueAllianceAPIClient blueAlliance;
	private static ManagementScreen instance = null;
	private final UpdateFiles deviceLoader;
	//FXML Guff
	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private VBox vbox_detail_content;
	@FXML
	private VBox vbox_match_list;
	@FXML
	private Button btn_show_history;
	@FXML
	private MenuItem mitem_new;
	@FXML
	private MenuItem mitem_open;
	@FXML
	private Label lbl_detail_header;
	private Parent view;
	private ArrayList<MatchRow> matchRows = new ArrayList<MatchRow>();
	private File out, scoresheet;
	private MatchData[][] matchData;
	private List<Field> fields = new ArrayList<Field>();

	/**
	 * Create BlueAlliance Client caching directory
	 */
	static {
		try {
			blueAlliance = new CachingBlueAllianceAPIClient("frc2363:scouting-manager:v1", Files.createTempDirectory("bluealliance").toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	ManagementScreen()
	{
		//Start info loader worker thread
		deviceLoader = new UpdateFiles();
		deviceLoader.start();
	}

	/**
	 * Retrieves the global instance of this screen.
	 * @return The global instance of this screen.
	 */
	public static ManagementScreen getInstance()
	{
		if (instance == null) try {
			FXMLLoader loader = new FXMLLoader(MatchRow.class.getResource("management.fxml"));
			Parent root = loader.load();
			instance = loader.getController();
			instance.view = root;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return instance;
	}

	/**
	 * Gets the scouted match data for the given team stored inside of a <link>MatchData</link> object.
	 * @param matchNum The official number of the match to give the data from (i.e, if the match was Q22, you would use 22).
	 * @param team The team number to get the data from.
	 * @param flag
	 * @return A <link>MatchData</link> object containing the data scouted from that particular match, if it exists;
	 * otherwise, null.
	 */
	public MatchData getMatchData(int matchNum, int team, boolean flag)
	{
		MatchData[] row = matchData[matchNum - 1];
		for (MatchData data : row) if (data.getTeamNum() == team) return data;
		return null;
	}

	/**
	 * Gets scouted match data from the team playing in the given match at the given position formatted in a
	 * <link>MatchData</link> object.
	 * @param matchNum The official number of the match to give the data from (i.e, if the match was Q22, you would use 22).
	 * @param slot The position to get team match data from. Should be an integer from 0 (Red 1) to 5 (Blue 3).
	 * @return A <link>MatchData</link> object containing the data scouted from that particular match, if it exists;
	 * otherwise, null.
	 */
	public MatchData getMatchData(int matchNum, int slot)
	{
		return matchData[matchNum - 1][slot];
	}

	/**
	 * Retrieves an <link>javafx.collections.ObservableList</link> containing all of the scouted match data for the given team,
	 * provided it exists.
	 * @param team The team number to query data for.
	 * @param showOnlyWithData Only add matches which have been scouted (i.e. data exists) to the list if true.
	 * @return an <link>javafx.collections.ObservableList</link> containing all of the scouted match data for the given team,
	 * provided it exists.
	 */
	public ObservableList<MatchData> getMatchData (int team, boolean showOnlyWithData)
	{
		ObservableList<MatchData> result = FXCollections.observableArrayList();

		for (MatchData[] datas : matchData)
			for (MatchData data : datas) {
				if (data.getTeamNum() != team) continue;
				if (!showOnlyWithData || data.hasData()) result.add(data);
			}

		return result;
	}

	/**
	 * @return A list of all of the data fields used to scout team match information.
	 */
	public List<Field> getFields() {
		return fields;
	}

	/**
	 * Updates the list of teams displayed in the main match list.
	 * @param resetRows If match data should be deleted and reloaded.
	 */
	void updateRows(boolean resetRows)
	{
		if (resetRows) {
			matchRows.clear();
			vbox_match_list.getChildren().clear();

			for (MatchData[] matchDatas : matchData)
				try {
					MatchRow row = MatchRow.getInstance(matchDatas[0].getMatchNum());
					matchRows.add(row);
					vbox_match_list.getChildren().add(row.getView());
					//System.out.println(Arrays.toString(matchDatas));
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		else for (MatchRow matchRow : matchRows) matchRow.updateButtons();

		System.out.println("Schedule finished loading!");
		System.out.println(vbox_match_list.getChildren().size());
		writeJSONOutput();
	}

	/**
	 * Called when the user selects one of the match buttons from the list.
	 * @param match Which match data to display in the detail display.
	 */
	void changeSelectedTeam(MatchData match)
	{
		vbox_detail_content.getChildren().clear();
		lbl_detail_header.setText("Team " + match.getTeamNum() + " - Match Q" + match.getMatchNum());
		vbox_detail_content.getChildren().add(getDisplayLayout(match));
	}

	@FXML
	void getSelectedTeamHistory(MouseEvent event) throws IOException
	{
		MatchRow row = MatchRow.getInstance(2);
		matchRows.add(row);
		vbox_match_list.getChildren().add(row.getView());
		System.out.println(vbox_match_list.getChildren().size());
	}

	public Parent getView()
	{
		return view;
	}

	/**
	 * Creates the layout of the match detail display.
	 * @param data The scouted data to insert into the layout.
	 * @return The layout of the match detail display.
	 */
	public Pane getDisplayLayout(MatchData data)
	{
		VBox pane = new VBox(4);
		Collections.sort(fields);

		for (Field field : fields) {
			JSONObject item = data.getData(field.getString("id"));
			switch (field.getString("type")) {
				case "checkbox":
					CheckBox box = new CheckBox(field.getString("id"));
					box.setSelected(item.getBoolean("checkbox"));
					//box.disableProperty().setValue(true);
					pane.getChildren().add(box);
					break;
				case "counter":
					String txt = field.getString("id") + item.getInt("counter");
					pane.getChildren().add(new Label(txt));
					break;
				case "slider":
					String txt2 = field.getString("id") + item.getDouble("slider");
					pane.getChildren().add(new Label(txt2));
					break;
				case "notes":
					TextArea textArea = new TextArea(item.getString("notes"));
					textArea.editableProperty().setValue(false);
					pane.getChildren().add(textArea);
					break;
				case "rating":
					String txt3 = field.getString("id") + item.getString("rating");
					pane.getChildren().add(new Label(txt3));
					break;
				case "toteStacker":
					pane.getChildren().add(new Label(ToteStackGraphFactory.create(item)));
					break;
			}
		}

		return pane;
	}

	@FXML
	void onclick_mitemNew(ActionEvent event) {
		if(out != null && matchData != null) writeJSONOutput();

		FileChooser fileChooser = new FileChooser();
		if(out != null) fileChooser.setInitialDirectory(out);
		fileChooser.getExtensionFilters().add(scoutFilter);
		fileChooser.titleProperty().setValue("Save Scouting Data to...");
		fileChooser.initialFileNameProperty().setValue("NewScoutingFile.scout");
		out = fileChooser.showSaveDialog(Main.getStage());
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(xmlFilter);
		fileChooser.titleProperty().setValue("Load Scoresheet Data from...");
		if(out != null) fileChooser.setInitialDirectory(out.getParentFile());
		scoresheet = fileChooser.showOpenDialog(Main.getStage());
		loadScoresheet(scoresheet);
		BlueAllianceBox.show((int year, String eventCode, boolean schedules) -> {
			if(schedules) createNewSchedule(new ScheduleLoader(year, eventCode, out.getParentFile()));
			else createNewSchedule(new ScheduleLoader(year, eventCode));
		});

	}

	@FXML
	void onclick_mitemOpen(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(scoutFilter);
		fileChooser.titleProperty().setValue("Load Scouting Data from...");
		out = fileChooser.showOpenDialog(Main.getStage());
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(xmlFilter);
		fileChooser.titleProperty().setValue("Load Scoresheet Data from...");
		scoresheet = fileChooser.showOpenDialog(Main.getStage());
		loadScoresheet(scoresheet);
		loadFromFile(out);
	}

	@FXML
	void onclick_mitemGetPitScouting(ActionEvent event) {

	}

	@FXML
	void onclick_mitemPitScoutingFromFile(ActionEvent event) {

	}

	@FXML
	void onclick_mitemPitScoutingAndroid(ActionEvent event) {

	}

	@FXML
	void onclick_mItemToCSV(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		//chooser.setInitialDirectory(out);
		chooser.setTitle("Export to CSV...");
		chooser.getExtensionFilters().addAll(csvFilter, txtFilter);
		File csvOut = chooser.showSaveDialog(Main.getStage());
		JSONArray dataOut = new JSONArray();
		JSONArray stackOut = new JSONArray();

		for (MatchData[] dataRow : matchData)
			for (MatchData data : dataRow) {
				if(!data.hasData()) continue;
				JSONObject prototype = new JSONObject();

				prototype.put("teamNum", data.getTeamNum());
				prototype.put("matchNum", data.getMatchNum());

				for (Field field : fields) {
					String type = field.getString("type"), id = field.getString("id");
					prototype.put(id, data.getData(id).get(MatchHistoryViewer.getFieldName(type)).toString());
				}

				dataOut.put(prototype);

				JSONObject stackPrototype = new JSONObject();
				stackPrototype.put("teamNum", data.getTeamNum());
				stackPrototype.put("matchNum", data.getMatchNum());

				String type = "toteStacker", id = "totes";
				stackPrototype.put(id, CDL.toString(data.getData(id).getJSONArray(MatchHistoryViewer.getFieldName(type))));
				stackOut.put(stackPrototype);
			}

		try {
			String out = CDL.toString(dataOut);
			BufferedWriter writer = new BufferedWriter(new FileWriter(csvOut));
			writer.write(out);
			writer.flush();
			writer.close();

			out = CDL.toString(stackOut);
			System.out.println(out);
			BufferedWriter writer2 = new BufferedWriter(new FileWriter(csvOut.getParent() + File.separator + "stacks.csv"));
			writer2.write(out);
			writer2.flush();
			writer2.close();

			System.out.println("Data successfully written to " + csvOut.getParent() + File.separator + "stacks.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onScheduleLoaded(MatchData[][] matches)
	{
		matchData = matches;
		Platform.runLater(() -> updateRows(true));
	}

	/**
	 * Creates a blank list of team matches from a ScheduleLoader, and thereby deletes all existing match data after saving.
	 * @param loader A ScheduleLoader to load the match schedules from.
	 */
	public void createNewSchedule(ScheduleLoader loader)
	{
		loader.start(this);
		matchRows.clear();
	}

	/**
	 * Loads a scouting scoresheet XML from a <link>java.io.File</link>.
	 * @param scoresheet A file containing the location of the XML-based scoresheet file.
	 */
	public void loadScoresheet(File scoresheet)
	{
		String xml = AccessFiles.readFile(scoresheet);
		JSONObject fields = XML.toJSONObject(xml).getJSONObject("scouting");
		this.fields.clear();

		Iterator<String> jsonItr = fields.keys();
		while(jsonItr.hasNext()) {
			String key = jsonItr.next();
			if(key.equals("group")) continue;

			Object mystery = fields.get(key);
			//System.out.println(mystery.getClass().getCanonicalName());

			if(mystery instanceof JSONObject) {
				Field f = new Field(mystery.toString());
				f.put("type", key);
				this.fields.add(f);
			}
			if(mystery instanceof JSONArray) {
				JSONArray jArray = (JSONArray) mystery;
				for (int i = 0; i < jArray.length(); i++) {
					Field j = new Field(jArray.getJSONObject(i).toString());
					j.put("type", key);
					this.fields.add(j);
				}
			}

		}

		for (Field field : this.fields) {
			System.out.println(field.toString(3));
		}

		Collections.sort(this.fields);
	}

	/**
	 * Saves all match data to file.
	 */
	public void writeJSONOutput()
	{
		if(out == null) return;
		JSONArray dataOut = new JSONArray();
		//dataOut.put(this.out.getAbsolutePath());

		for (MatchData[] dataRow : matchData)
			for (MatchData data : dataRow)
				dataOut.put(data.getData());

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(out))) {
			writer.write(dataOut.toString(5));
			System.out.println("Data written successfully to " + out.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads all scouting data from a file.
	 * @param out The location of the .scout file to load scouting data from.
	 */
	public void loadFromFile(File out)
	{
		this.out = out;
		JSONArray json;

		try {
			json = new JSONArray(AccessFiles.readFile(out));
			//System.out.println(json.toString(3));
		} catch (NullPointerException e) {
			return;
		}

		matchData = new MatchData[json.length() / 6][6];
		//scoresheet = new File(json.getString(0));

		int r = 0;

		for (int i = 0; i < matchData.length; i++)
			for (int j = 0; j < matchData[i].length; j++) {
				JSONObject n = json.getJSONObject(r);
				matchData[i][j] = new MatchData(json.getJSONObject(r));
				r++;
			}

		Platform.runLater(() -> updateRows(true));
	}

	/**
	 * Requests the schedule loader worker thread to finish.
	 */
	public void requestFinish()
	{
		deviceLoader.requestFinish();
	}

	/**
	 * A worker thread which searches through all connected drives to find usable scouting data.
	 */
	private class UpdateFiles extends Thread
	{
		private boolean requestFinish = false;

		@Override
		public void run()
		{
			while (!requestFinish) {
				try {
					//Query drives once every second.
					if(matchData != null) AccessFiles.scan();
					sleep(1000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}

		/**
		 * Requests this thread to finish.
		 */
		public synchronized void requestFinish()
		{
			requestFinish = true;
		}

	}
}