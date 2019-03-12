package org.team2363.jbluealliance;

import org.json.JSONObject;

public class TeamMedia
{
	private final String foreign_key;
	private final JSONObject details;
	private final MediaType type;

	public TeamMedia (JSONObject json)
	{
		foreign_key = json.getString("foreign_key");
		type = MediaType.getInstance(json.getString("type"));
		details = json.getJSONObject("details");
	}

	public String getMediaUrl ()
	{
		switch (type)
		{
			case CHIEF_DELPHI_PHOTO:
				return "http://www.chiefdelphi.com/media/img/"
						+ details.getString("image_partial");

			case YOUTUBE:
				return "http://www.youtube.com/embed/"
						+ foreign_key;
		}

		return null;
	}

	public enum MediaType
	{
		CHIEF_DELPHI_PHOTO("cdphotothread"),
		YOUTUBE("youtube");

		private final String tag;

		public static MediaType getInstance(String tag)
		{
			for(MediaType value : values())
				if(value.tag.equals(tag)) return value;

			return null;
		}

		private MediaType(String tag)
		{
			this.tag = tag;
		}

		public String getTag()
		{
			return tag;
		}
	}

}
