package org.team2363.scouting.compiler;

import org.team2363.jbluealliance.Match.MatchCompetitionLevel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Worker thread for loading event schedules from BlueAlliance in the background.
 */
public class ScheduleLoader extends Thread
{
	private final String eventKey;
	private final int year;
	private final File scheduleOutput;
	private OnScheduleLoadedListener listener;
	private boolean started = false;

	/**
	 * Creates a new ScheduleLoader given a year and event key.
	 * @param year The year in which the event takes place.
	 * @param eventKey The string key of the event. See <link></link> for information.
	 */
	public ScheduleLoader(int year, String eventKey)
	{
		this.year = year;
		this.eventKey = eventKey;
		scheduleOutput = null;
	}

	/**
	 * Creates a new ScheduleLoader, given a year and a event key, as well as a directory to output device schedules.
	 * @param year The year in which the event takes place.
	 * @param eventKey The string key of the event. See <link></link> for information.
	 * @param outputDeviceSchedules The directory to put CSV formatted device schedules, for use within the FRCScouting Android app.
	 */
	public ScheduleLoader(int year, String eventKey, File outputDeviceSchedules)
	{
		this.year = year;
		this.eventKey = eventKey;
		this.scheduleOutput = outputDeviceSchedules;
	}

	/**
	 * Starts the thread with an OnScheduleLoadedListener attached, to be run at the end of execution.
	 * @param listener An OnScheduleLoadedListener to run at the end of execution.
	 */
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
				String sPath = scheduleOutput.getPath() + File.pathSeparator;
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

	/**
	 * Callback interface used once the schedule is finished loading.
	 */
	public interface OnScheduleLoadedListener
	{
		/**
		 * Called when the schedule has finished loading (on background thread)
		 * @param matches The loaded schedule.
		 */
		public void onScheduleLoaded(MatchData[][] matches);
	}
}
