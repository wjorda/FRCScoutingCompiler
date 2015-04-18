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

public class MatchData
{
	private JSONObject data;
	private final int matchNum, teamNum;

	public MatchData(int matchNum, int teamNum)
	{
		data = null;
		this.teamNum = teamNum;
		this.matchNum = matchNum;
	}

	public MatchData(JSONObject json)
	{
		if(json.length() > 2) data = json;
		else data = null;

		matchNum = json.getInt("match_number");
		teamNum = json.getInt("team_number");
	}

	public MatchData(String json)
	{
		this(new JSONObject(json));
	}

	public int getMatchNum()
	{
		return matchNum;
	}

	public IntegerProperty matchNumProperty() { return new SimpleIntegerProperty(matchNum); }

	public int getTeamNum()
	{
		return teamNum;
	}

	//public int teamNumProperty() { return teamNum; }

	public boolean hasData()
	{
		return (data != null);
	}

	public JSONObject getData(String id)
	{
		return data.optJSONObject(id);
	}

	public void setData(JSONObject json)
	{
		data = json;
	}

	@Override
	public String toString()
	{
		return String.valueOf(teamNum);
	}

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
