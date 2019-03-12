package org.team2363.scouting.compiler;

import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class AccessFiles
{
	public static void scan()
	{
		String os = System.getProperty("os.name");
		//System.out.println("OS: " + os);
		if(os.startsWith("Windows")) scanWindows();
		else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0) scanUnix();
	}

	private static void scanUnix()
	{
		File mediaDir = new File(("/media/" + System.getProperty("user.name")));
		//System.out.println(mediaDir.getPath());
		//if(!mediaDir.exists() || !mediaDir.isDirectory()) return;
		File[] files = mediaDir.listFiles();
		for (File drive : files) {
			File scoutingDir = new File(drive.getAbsolutePath() + File.separator + "Scouting");
			for(File file : scoutingDir.listFiles(pathname -> pathname.getPath().endsWith(".json"))) {
				if(file.exists() && file.canRead()) collectMatches(file);
			}
		}
	}

	private static void collectMatches(File file)
	{
		ManagementScreen screen = ManagementScreen.getInstance();
		boolean hasChanged = false;

		try {
			JSONArray jsonArray = new JSONArray(readFile(file));
			System.out.println(jsonArray.length());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject match = jsonArray.getJSONObject(i);
				int matchNum = match.getInt("match_number");
				int teamNum = match.getInt("team_number");
				MatchData m = screen.getMatchData(matchNum, teamNum, true);
				if (m != null) {
					m.setData(match);
					System.out.println("Added new match: (" + matchNum + ", " + teamNum + ")" );
					hasChanged = true;
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		if(hasChanged) {
			Platform.runLater(() -> ManagementScreen.getInstance().updateRows(false));
		}
	}

	public static String readFile(File file)
	{
		try (BufferedReader r = new BufferedReader(new FileReader(file))) {
			String line;
			StringBuilder text = new StringBuilder();
			while ((line = r.readLine()) != null) text.append(line).append("\n");
			return text.toString();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void scanWindows()
	{

	}
}
