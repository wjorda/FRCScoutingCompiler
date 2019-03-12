package org.team2363.jbluealliance;

import org.json.JSONArray;
import org.json.JSONObject;

public class Award
{
	private final String name;
	private final String event_key;
	private final int year;
	private final AwardType type;
	private final Awardee[] winners;

	public Award(JSONObject json)
	{
		name = json.getString("name");
		event_key = json.getString("event_key");
		year = json.getInt("year");
		type = AwardType.getInstance(json.getInt("award_type"));

		JSONArray winnersJSON = json.getJSONArray("recipient_list");
		winners = new Awardee[winnersJSON.length()];
		for(int i=0; i<winnersJSON.length(); i++)
			winners[i] = new Awardee(winnersJSON.getJSONObject(i));
	}

	public class Awardee
	{
		private final String team_key;
		private final String awardee;

		public Awardee(JSONObject json)
		{
			team_key = json.get("team_number").toString();
			awardee = json.get("awardee").toString();
		}

		public String getTeamKey()
		{
			return team_key;
		}

		public String getAwardee()
		{
			return awardee;
		}
	}

	public String getName()
	{
		return name;
	}

	public String getEvent_key()
	{
		return event_key;
	}

	public int getYear()
	{
		return year;
	}

	public AwardType getType()
	{
		return type;
	}

	public Awardee[] getWinners()
	{
		return winners;
	}

	public enum AwardType
	{
		CHAIRMANS,
		WINNER,
		FINALIST,

		WOODIE_FLOWERS,
		DEANS_LIST,
		VOLUNTEER,
		FOUNDERS,
		BART_KAMEN_MEMORIAL,
		MAKE_IT_LOUD,

		ENGINEERING_INSPIRATION,
		ROOKIE_ALL_STAR,
		GRACIOUS_PROFESSIONALISM,
		COOPERTITION,
		JUDGES,
		HIGHEST_ROOKIE_SEED,
		ROOKIE_INSPIRATION,
		INDUSTRIAL_DEESIGN,
		QUALITY,
		SAFETY,
		SPORTSMANSHIP,
		CREATIVITY,
		ENGINEERING_EXCELLENCE,
		ENTREPRENEURSHIP,
		EXCELLENCE_IN_DESIGN,
		EXCELLENCE_IN_DESIGN_CAD,
		EXCELLENCE_IN_DESIGN_ANIMATION,
		DRIVING_TOMORROWS_TECHNOLOGY,
		IMAGERY,
		MEDIA_AND_TECHNOLOGY,
		INNOVATION_IN_CONTROL,
		SPIRIT,
		WEBSITE,
		VISUALIZATION,
		AUTODESK_INVENTOR,
		FUTURE_INNOVATOR,
		RECOGNITION_OF_EXTRAORDINARY_SERVICE,
		OUTSTANDING_CART,
		WSU_AIM_HIGHER,
		LEADERSHIP_IN_CONTROL,
		NUM_1_SEED,
		INCREDIBLE_PLAY,
		PEOPLES_CHOICE_ANIMATION,
		VISUALIZATION_RISING_STAR,
		BEST_OFFENSIVE_ROUND,
		BEST_PLAY_OF_THE_DAY,
		FEATHERWEIGHT_IN_THE_FINALS,
		MOST_PHOTOGENIC,
		OUTSTANDING_DEFENSE,
		POWER_TO_SIMPLIFY,
		AGAINST_ALL_ODDS,
		RISING_STAR,
		CHAIRMANS_HONORABLE_MENTION,
		CONTENT_COMMUNICATION_HONORABLE_MENTION,
		TECHNICAL_EXECUTION_HONORABLE_MENTION,
		REALIZATION,
		REALIZATION_HONORABLE_MENTION,
		DESIGN_YOUR_FUTURE,
		DESIGN_YOUR_FUTURE_HONORABLE_MENTION,
		SPECIAL_RECOGNITION_CHARACTER_ANIMATION,
		HIGH_SCORE,
		TEACHER_PIONEER,
		BEST_CRAFTSMANSHIP,
		BEST_DEFENSIVE_MATCH,
		PLAY_OF_THE_DAY,
		PROGRAMMING,
		PROFESSIONALISM,
		GOLDEN_CORNDOG;

		public static AwardType getInstance(int constant)
		{
			return values()[constant];
		}
	}
}
