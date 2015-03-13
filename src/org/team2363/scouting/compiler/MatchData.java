package org.team2363.scouting.compiler;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.json.JSONObject;

import java.util.Objects;

/**
 * JSONObject wrapper containing scouted team and match data.
 */
public class MatchData
{
	private JSONObject data;
	private final int matchNum, teamNum;

	/**
	 * Creates a blank MatchData object with no match data
	 * @param matchNum
	 * @param teamNum
	 */
	public MatchData(int matchNum, int teamNum)
	{
		data = null;
		this.teamNum = teamNum;
		this.matchNum = matchNum;
	}

	/**
	 * Creates a MatchData object from a JSONObject.
	 * @param json JSONObject containing team match data.
	 */
	public MatchData(JSONObject json)
	{
		if(json.length() > 2) data = json;
		else data = null;

		matchNum = json.getInt("match_number");
		teamNum = json.getInt("team_number");
	}

	/**
	 * Creates a MatchData object from JSON-formatted text.
	 * @param json Stirng containing json-formatted scouting data.
	 */
	public MatchData(String json)
	{
		this(new JSONObject(json));
	}

	/**
	 * @return The official number of the match.
	 */
	public int getMatchNum()
	{
		return matchNum;
	}

	public IntegerProperty matchNumProperty() { return new SimpleIntegerProperty(matchNum); }

	/**
	 * @return the number of the team playing in this match.
	 */
	public int getTeamNum()
	{
		return teamNum;
	}

	//public int teamNumProperty() { return teamNum; }

	/**
	 * @return true if this match has data, false otherwise.
	 */
	public boolean hasData()
	{
		return (data != null);
	}

	/**
	 * Gets a value from a this match's data.
	 * @param id The json key for this value.
	 * @return The JSON object that corresponds to the id given, null if it does not exist.
	 */
	public JSONObject getData(String id)
	{
		return data.optJSONObject(id);
	}

	/**
	 * Sets the match data.
	 * @param json JSO object containing the match data.
	 */
	public void setData(JSONObject json)
	{
		data = json;
	}

	@Override
	public String toString()
	{
		return String.valueOf(teamNum);
	}

	/**
	 * Get the JSONObject that contains all of the data, including team num and match num.
	 * @return
	 */
	public JSONObject getData()
	{
		if(data != null) return data;

		JSONObject json = new JSONObject();
		json.put("match_number", matchNum);
		json.put("team_number", teamNum);
		return json;
	}

	public static class MatchDataCallback implements Callback<TableColumn.CellDataFeatures<MatchData, String>, ObservableValue<String>>
	{
		private final String fieldKey, keyValue;

		public MatchDataCallback(String fieldKey, String keyValue)
		{
			this.fieldKey = fieldKey;
			this.keyValue = keyValue;
		}
		/**
		 * The <code>call</code> method is called when required, and is given a
		 * single argument of type P, with a requirement that an object of type R
		 * is returned.
		 *
		 * @param param The single argument upon which the returned value should be
		 *              determined.
		 * @return An object of type R that may be determined based on the provided
		 * parameter value.
		 */
		@Override
		public ObservableValue<String> call(TableColumn.CellDataFeatures<MatchData, String> param)
		{
			return new SimpleStringProperty(param.getValue().getData(fieldKey).get(keyValue).toString());
		}
	}

}
