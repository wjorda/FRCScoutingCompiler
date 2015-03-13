package org.team2363.scouting.compiler;

import org.json.JSONObject;

/**
 * A JSONObject wrapper used to store information about the fields used to scout data.
 */
public class Field extends JSONObject implements Comparable<Field>
{
	private final int index;

	private Field()
	{
		index = 0;
	}

	/**
	 * Creates a new Field from JSON-formatted string.
	 * @param json
	 */
	public Field(String json)
	{
		super(json);
		this.index = getInt("index");
	}


	public int getIndex()
	{
		return index;
	}

	@Override
	public int compareTo(Field that)
	{
		return this.index - that.index;
	}
}
