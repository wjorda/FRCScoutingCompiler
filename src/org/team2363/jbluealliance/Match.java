package org.team2363.jbluealliance;

import org.json.JSONObject;

public class Match implements Comparable<Match>
{
	private final String key;
	private final int set_number;
	private final int match_number;
	private final String event_key;
	private final String time_string;
	private final MatchCompetitionLevel level;
	private final Alliance redAlliance;
	private final Alliance blueAlliance;
	private final long timestamp;

	public Match(JSONObject json)
	{
		this.key = json.getString("key");
		this.set_number = json.getInt("set_number");
		this.match_number = json.getInt("match_number");
		this.event_key = json.getString("event_key");
		this.time_string = json.optString("time_string");
		this.level = MatchCompetitionLevel.getInstance(json.getString("comp_level"));
		this.timestamp = json.getLong("time");

		JSONObject alliances = json.getJSONObject("alliances");
		redAlliance = new Alliance(Alliance.RED, false, alliances.getJSONObject("red"));
		blueAlliance = new Alliance(Alliance.BLUE, false, alliances.getJSONObject("blue"));
	}

	public String getKey()
	{
		return key;
	}

	public int getSetNumber()
	{
		return set_number;
	}

	public int getMatchNumber()
	{
		return match_number;
	}

	public String getEvent_key()
	{
		return event_key;
	}

	public String getTime_string()
	{
		return time_string;
	}

	public MatchCompetitionLevel getLevel()
	{
		return level;
	}

	public Alliance getTeamAlliance(int team)
	{
		if (redAlliance.hasTeam(team)) return redAlliance;
		if (blueAlliance.hasTeam(team)) return blueAlliance;
		else return null;
	}

	public Alliance getRedAlliance()
	{
		return redAlliance;
	}

	public Alliance getBlueAlliance()
	{
		return blueAlliance;
	}

	public int[] getTeams()
	{
		return new int[]
				{
					redAlliance.getTeam(1),
					redAlliance.getTeam(2),
					redAlliance.getTeam(3),
					blueAlliance.getTeam(1),
					blueAlliance.getTeam(2),
					blueAlliance.getTeam(3),
				};
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	@Override
	public int compareTo(Match that)
	{
		return this.match_number - that.match_number;
	}

	public enum MatchCompetitionLevel
	{
		QUALIFICATION("qm"),
		EINSTEIN("ef"),
		QUARTERFINALS("qf"),
		SEMIFINALS("sf"),
		FINALS("f");

		private final String tag;

		private MatchCompetitionLevel(String tag)
		{
			this.tag = tag;
		}

		public static MatchCompetitionLevel getInstance(String tag)
		{
			for (MatchCompetitionLevel l : values())
				if (l.tag.equals(tag)) return l;

			return QUALIFICATION;
		}

		public String getTag()
		{
			return tag;
		}
	}
}
