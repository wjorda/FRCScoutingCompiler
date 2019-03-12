package org.team2363.jbluealliance;

import org.json.JSONArray;
import org.json.JSONObject;
import org.team2363.jbluealliance.Match.MatchCompetitionLevel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlueAllianceAPIClient
{
	private static final String TBA_SERVER = "http://www.thebluealliance.com/api/v3/";
	private final String appId;

	public BlueAllianceAPIClient (String appId)
	{
		this.appId = appId;
	}

	public Team teamRequest (int number)
	{
		String url = TBA_SERVER + "team/frc" + number,
				code = "team_"+number;

		JSONObject jsonTeam = new JSONObject(getHTML(url));
		return new Team(jsonTeam);
	}

	public Event[] teamEventRequest (int team, int year)
	{
		String url = TBA_SERVER + "team/frc" + team + "/" + year + "/events";
		JSONArray jsonEvents = new JSONArray(getHTML(url));
		Event[] events = new Event[jsonEvents.length()];
		for (int i = 0; i < events.length; i++)
			events[i] = new Event(jsonEvents.getJSONObject(i), false);

		return events;
	}

	public Award[] teamEventAwardRequest (int team, int year, String eventCode)
	{
		String url = TBA_SERVER + "team/frc" + team + "/event/" + year + eventCode + "/awards";
		JSONArray jsonAwards = new JSONArray(getHTML(url));
		Award[] awards = new Award[jsonAwards.length()];

		for(int i=0; i<awards.length; i++) awards[i] = new Award(jsonAwards.getJSONObject(i));

		return awards;
	}

	public Match[] teamEventMatchRequest (int team, int year, String eventCode)
	{
		String url = TBA_SERVER + "team/frc" + team + "/event/" + year + eventCode + "/matches";
		JSONArray jsonMatches = new JSONArray(getHTML(url));
		Match[] matches = new Match[jsonMatches.length()];

		for(int i=0; i<matches.length; i++) matches[i] = new Match(jsonMatches.getJSONObject(i));

		return matches;
	}

	public int[] teamYearsParticipatedRequest (int team)
	{
		String url = TBA_SERVER + "team/frc" + team + "/years_participated";
		JSONArray jsonYears = new JSONArray(getHTML(url));
		int[] years = new int[jsonYears.length()];
		for(int i=0; i<years.length; i++) years[i] = jsonYears.getInt(i);
		return years;
	}

	public TeamMedia[] teamMediaRequest (int team, int year)
	{
		String url = TBA_SERVER + "team/frc" + team + "/" + year + "/media";
		JSONArray jsonMedia = new JSONArray(getHTML(url));
		TeamMedia[] media = new TeamMedia[jsonMedia.length()];
		for(int i=0; i<media.length; i++) media[i] = new TeamMedia(jsonMedia.getJSONObject(i));
		return media;
	}

	public Event[] eventListRequest (int year)
	{
		String url = TBA_SERVER + "events/" + year;
		JSONArray jsonEvents = new JSONArray(getHTML(url));
		Event[] events = new Event[jsonEvents.length()];
		for(int i=0; i<events.length; i++) events[i] = new Event(jsonEvents.getJSONObject(i), true);
		return events;
	}

	public Event eventRequest (int year, String code)
	{
		String url = TBA_SERVER + "event/" + year + code;
		return new Event(new JSONObject(getHTML(url)), true);
	}

	public Team[] eventTeamRequest (int year, String code)
	{
		String url = TBA_SERVER + "event/" + year + code + "/teams";
		JSONArray jsonTeams = new JSONArray(getHTML(url));
		Team[] teams = new Team[jsonTeams.length()];
		for(int i=0; i<teams.length; i++) teams[i] = new Team(jsonTeams.getJSONObject(i));
		return teams;
	}

	public Match[] eventMatchRequest (int year, String code)
	{
		String url = TBA_SERVER + "event/" + year + code + "/matches";
		JSONArray jsonMatches = new JSONArray(getHTML(url));
		Match[] matches = new Match[jsonMatches.length()];
		for(int i=0; i<matches.length; i++) matches[i] = new Match(jsonMatches.getJSONObject(i));
		Arrays.sort(matches, (m1, m2) -> m1.getMatchNumber() - m2.getMatchNumber());
		return matches;
	}

	public List<Match> eventMatchRequest (int year, String code, MatchCompetitionLevel level)
	{
		String url = TBA_SERVER + "event/" + year + code + "/matches";
		JSONArray jsonMatches = new JSONArray(getHTML(url));
		List<Match> matches = new ArrayList<Match>();
		for(int i=0; i<jsonMatches.length(); i++) {
			Match m = new Match(jsonMatches.getJSONObject(i));
			if(m.getLevel() == MatchCompetitionLevel.QUALIFICATION) matches.add(m);
		}
		matches.sort((m1, m2) -> m1.getMatchNumber() - m2.getMatchNumber());
		return matches;
	}

	public EventStats eventStatsRequest (int year, String code)
	{
		String url = TBA_SERVER + "event/" + year + code + "/stats";
		return new EventStats(new JSONObject(getHTML(url)));
	}

	public EventRankings eventRankingsRequest (int year, String code)
	{
		String url = TBA_SERVER + "event/" + year + code + "/rankings";
		return new EventRankings(new JSONArray(getHTML(url)));
	}
	
	public Award[] eventRewardRequest (int year, String code)
	{
		String url = TBA_SERVER + "event/" + year + code + "/awards";
		JSONArray jsonAwards = new JSONArray(getHTML(url));
		Award[] awards = new Award[jsonAwards.length()];
		for(int i=0; i<awards.length; i++) awards[i] = new Award(jsonAwards.getJSONObject(i));
		return awards;
	}

	public Match singleMatchRequest (int year, String code, MatchCompetitionLevel level, int levelNum, int setNumber)
	{
		String set = year + code + "_" + level.getTag() + levelNum + "m" + setNumber;
		String url = TBA_SERVER + "match/" + set;
		return new Match(new JSONObject(getHTML(url)));
	}

	public String getHTML(String url)
	{
		URL oracle;
		HttpURLConnection conn;
		BufferedReader rd = null;
		String line;
		StringBuilder result = new StringBuilder();
		try {
			oracle = new URL(url);
			conn = (HttpURLConnection) oracle.openConnection();
			conn.setRequestMethod("GET");
			conn.addRequestProperty("X-TBA-Auth-Key", appId);
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rd != null) {
				try {
					rd.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String res = result.toString();
		System.out.println("Server responded with: " + res);
		return res;
	}

	public String getAppId()
	{
		return appId;
	}
}
