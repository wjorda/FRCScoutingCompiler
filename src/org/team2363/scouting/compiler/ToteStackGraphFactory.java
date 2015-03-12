package org.team2363.scouting.compiler;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TableColumn;
import org.json.JSONArray;
import org.json.JSONObject;

public class ToteStackGraphFactory
{
	public static String create (JSONObject entry)
	{
		JSONArray stax = entry.getJSONArray("stacks");
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<stax.length(); i++) builder.append(stax.getJSONObject(i)).append("\n");
		return builder.toString();
	}

	private static String toString(JSONObject stack)
	{
		StringBuilder builder = new StringBuilder("|");
		for (int i = 0; i < stack.getInt("height"); i++) {
			builder.append("[]");
		}
		if(stack.getBoolean("can")) builder.append("[  }");
		if(stack.getBoolean("noodle")) builder.append("=");
		return builder.toString();
	}
}
