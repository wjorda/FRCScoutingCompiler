package org.team2363.scouting.compiler;

import org.team2363.jbluealliance.Match.MatchCompetitionLevel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ScheduleLoader extends Thread
{
	private final String eventKey;
	private final int year;
	private final File scheduleOutput;
	private OnScheduleLoadedListener listener;
	private boolean started = false;

	public ScheduleLoader(int year, String eventKey)
	{
		this.year = year;
		this.eventKey = eventKey;
		scheduleOutput = null;
	}

	public ScheduleLoader(int year, String eventKey, File outputDeviceSchedules)
	{
		this.year = year;
		this.eventKey = eventKey;
		this.scheduleOutput = outputDeviceSchedules;
		System.out.println(outputDeviceSchedules);
	}

	public void start(OnScheduleLoadedListener listener)
	{
		started = true;
		this.listener = listener;
		start();
	}

	@Override
	public void run()
	{
		MatchData[][] matches = null;

		try {
			List<org.team2363.jbluealliance.Match> bluMatches = ManagementScreen.blueAlliance.eventMatchRequest(year, eventKey, MatchCompetitionLevel.QUALIFICATION);
			matches = new MatchData[bluMatches.size()][6];
			for (int i = 0; i < matches.length; i++) {
				int[] teams = bluMatches.get(i).getTeams();
				for (int j = 0; j < teams.length; j++) {
					matches[i][j] = new MatchData(i+1, teams[j]);
				}
			}

			if(scheduleOutput != null && scheduleOutput.isDirectory()) try {
				String sPath = scheduleOutput.getPath() + File.separator;
				BufferedWriter[] writers = new BufferedWriter[]
						{
								new BufferedWriter(new FileWriter(sPath + "red1.csv")),
								new BufferedWriter(new FileWriter(sPath + "red2.csv")),
								new BufferedWriter(new FileWriter(sPath + "red3.csv")),
								new BufferedWriter(new FileWriter(sPath + "blue1.csv")),
								new BufferedWriter(new FileWriter(sPath + "blue2.csv")),
								new BufferedWriter(new FileWriter(sPath + "blue3.csv")),
						};
				for (int i = 0; i < matches.length; i++) {
					for (int j = 0; j < matches[i].length; j++) {
						writers[j].write((i+1) + ", " + matches[i][j].getTeamNum() + "\n");
					}
				}

				for (BufferedWriter writer : writers) {
					writer.flush();
					writer.close();
				}

			} catch (IOException io)
			{
				io.printStackTrace();
			}



		} finally {
			if(listener != null) listener.onScheduleLoaded(matches);
		}
	}

	public boolean hasStarted()
	{
		return started;
	}


	public interface OnScheduleLoadedListener
	{
		public void onScheduleLoaded(MatchData[][] matches);
	}
}
