package org.team2363.jbluealliance;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

public class Event
{
	private final String key;
	private final String name;
	private final String short_name;
	private final String event_code;
	private final String location;
	private final String venue_address;
	private final String website;
	private final int year;
	private final boolean official, withTeams;
	private final EventType eventType;
	private final DistrictType districtType;

	private LinkedList<Alliance> alliances;

	public Event(JSONObject source, boolean withTeams)
	{
		this.withTeams = withTeams;

		key = source.getString("key");
		name = source.getString("name");
		short_name = source.getString("short_name");
		event_code = source.getString("event_code");
		location = source.getString("location");
		venue_address = source.get("venue_address").toString();
		website = source.getString("website");
		year = source.getInt("year");
		official = source.getBoolean("official");
		eventType = EventType.getInstance(source.getInt("event_type"));
		districtType = DistrictType.getInstance(source.getInt("event_district"));

		if (withTeams) {
			alliances = new LinkedList<Alliance>();
			JSONArray alliancesJSON = source.getJSONArray("alliances");
			for (int i = 0; i < alliancesJSON.length(); i++)
				alliances.add(new Alliance(true, true, alliancesJSON.getJSONObject(i)));
		}
	}

	public String getKey()
	{
		return key;
	}

	public String getName()
	{
		return name;
	}

	public String getShort_name()
	{
		return short_name;
	}

	public String getEvent_code()
	{
		return event_code;
	}

	public String getLocation()
	{
		return location;
	}

	public String getVenue_address()
	{
		return venue_address;
	}

	public String getWebsite()
	{
		return website;
	}

	public int getYear()
	{
		return year;
	}

	public boolean isOfficial()
	{
		return official;
	}

	public EventType getEventType()
	{
		return eventType;
	}

	public DistrictType getDistrictType()
	{
		return districtType;
	}

	public boolean isWithTeams()
	{
		return withTeams;
	}
}
