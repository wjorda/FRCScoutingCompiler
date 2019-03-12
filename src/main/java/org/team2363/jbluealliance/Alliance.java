package org.team2363.jbluealliance;

import org.json.JSONArray;
import org.json.JSONObject;

public class Alliance
{
	public static final boolean RED = true;
	public static final boolean BLUE = false;

	private final boolean color, isElimination;
	private final int score;
	private final String[] teams;
	private final String[] declines;

	public Alliance (boolean color, boolean isElimination, JSONObject json)
	{
		this.color = color;
		this.isElimination = isElimination;

		if(isElimination) {
			JSONArray teamsJSON = json.getJSONArray("picks");
			teams = new String[teamsJSON.length()];
			for (int i = 0; i < teamsJSON.length(); i++)
				teams[i] = teamsJSON.getString(i);
			JSONArray declinesJSON = json.getJSONArray("declines");
			declines = new String[declinesJSON.length()];
			for (int i = 0; i < declinesJSON.length(); i++)
				declines[i] = declinesJSON.getString(i);
			this.score = 0;
		} else {
			JSONArray teamsJSON = json.getJSONArray("team_keys");
			teams = new String[teamsJSON.length()];
			for (int i = 0; i < teamsJSON.length(); i++)
				teams[i] = teamsJSON.getString(i);
			declines = null;
			this.score = json.getInt("score");
		}
	}

	public boolean hasTeam(int team)
	{
		for(String s : teams) if (s.equals("frc" + team)) return true;
		return false;
	}
	
	public int getTeam(int pos)
	{
		return Integer.parseInt(teams[pos-1].replace("frc", ""));
	}

	public boolean isColor()
	{
		return color;
	}

	public int getScore()
	{
		return score;
	}

	public String[] getTeams()
	{
		return teams;
	}

	public boolean isElimination()
	{
		return isElimination;
	}
}
