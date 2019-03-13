package org.team2363.jbluealliance;

import org.json.JSONObject;

public class Team implements Comparable<Team>
{
	private final String website;
	private final String name;
	private final String long_name;
	private final String locality;
	private final String state;
	private final String country;
	private final String location;
	private final String key;
	private final int teamNum;
	private final int rookie_year;

	public Team(JSONObject source)
	{
		website = source.get("website").toString();
		name = source.getString("nickname");
		long_name = source.getString("name");
		locality = source.getString("locality");
		state = source.getString("region");
		country = source.getString("country_name");
		location = source.getString("location");
		teamNum = source.getInt("team_number");
		key = source.getString("key");
		rookie_year = Integer.valueOf(source.getString("rookie_year"));
	}

	@Override
	public String toString()
	{
		return getTeamNum() + ": " + getName();
	}

	@Override
	public int compareTo(Team o)
	{
		return getTeamNum() - o.getTeamNum();
	}

	public String getWebsite()
	{
		return website;
	}

	public String getName()
	{
		return name;
	}

	public String getLong_name()
	{
		return long_name;
	}

	public String getLocality()
	{
		return locality;
	}

	public String getState()
	{
		return state;
	}

	public String getCountry()
	{
		return country;
	}

	public String getLocation()
	{
		return location;
	}

	public String getKey()
	{
		return key;
	}

	public int getTeamNum()
	{
		return teamNum;
	}

	public int getRookie_year()
	{
		return rookie_year;
	}
}
