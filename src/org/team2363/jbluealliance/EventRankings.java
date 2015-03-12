package org.team2363.jbluealliance;

import org.json.JSONArray;

public class EventRankings
{
	private final String[][] rankings;

	public EventRankings(JSONArray jsonArray)
	{
		rankings = new String[jsonArray.length()][jsonArray.getJSONArray(0).length()];
		for(int i=0; i<rankings.length; i++) {
			JSONArray rowJson = jsonArray.getJSONArray(i);
			for (int j=0; j<rankings[i].length; j++) rankings[i][j] = rowJson.getString(j);
		}
	}

	public String[][] getRankings() { return rankings; }

	public String[] getTeamRanking (int team)
	{
		for (String[] row : rankings) if (row[1] == String.valueOf(team)) return row;
		return null;
	}
}
