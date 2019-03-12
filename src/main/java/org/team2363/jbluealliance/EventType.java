package org.team2363.jbluealliance;

public enum EventType
{
	REGIONAL("Regional", 0),
	DISTRICT("District", 1),
	DISTRICT_CMP("District Championships", 2),
	CMP_DIVISION("Championship Division", 3),
	CMP_FINALS("Championship Finals", 4),
	OFFSEASON("Offseason", 99),
	PRESEASON("Preseason", 100),
	UNLABELED("--", -1);

	private final String name;
	private final int value;

	public static EventType getInstance(int constant)
	{
		for(EventType e : values()) {
			if(e.value == constant) return e;
		}

		return UNLABELED;
	}

	private EventType (String name, int value)
	{
		this.name = name;
		this.value = value;
	}

	public String getName()
	{
		return name;
	}

	public int getValue()
	{
		return value;
	}
}
