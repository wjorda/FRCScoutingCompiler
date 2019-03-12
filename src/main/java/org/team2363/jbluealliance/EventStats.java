package org.team2363.jbluealliance;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EventStats
{
	private final Map<Integer, Double> oprs, ccwms, dprs;
	
	public EventStats (JSONObject json)
	{
		JSONObject oprJson = json.getJSONObject("oprs");
		oprs = new HashMap<>();
		Iterator<String> oprKeys = oprJson.keys();
		while(oprKeys.hasNext()) {
			String key = oprKeys.next();
			oprs.put(Integer.valueOf(key), oprJson.getDouble(key));
		}

		JSONObject ccwmJson = json.getJSONObject("ccwms");
		ccwms = new HashMap<>();
		Iterator<String> ccwmKeys = ccwmJson.keys();
		while(ccwmKeys.hasNext()) {
			String key = ccwmKeys.next();
			ccwms.put(Integer.valueOf(key), ccwmJson.getDouble(key));
		}

		JSONObject dprJson = json.getJSONObject("dprs");
		dprs = new HashMap<>();
		Iterator<String> dprKeys = dprJson.keys();
		while(dprKeys.hasNext()) {
			String key = dprKeys.next();
			dprs.put(Integer.valueOf(key), dprJson.getDouble(key));
		}
	}
	
	public double getOPR (int team)
	{
		return oprs.get(team);
	}

	public double getCCWM (int team)
	{
		return ccwms.get(team);
	}

	public double getDPR (int team)
	{
		return dprs.get(team);
	}
	
}
