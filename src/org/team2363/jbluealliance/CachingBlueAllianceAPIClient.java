package org.team2363.jbluealliance;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CachingBlueAllianceAPIClient extends BlueAllianceAPIClient
{
	private String cachingDir;

	private static final DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");

	public CachingBlueAllianceAPIClient(String appId, String cachingDir)
	{
		super(appId);
		this.cachingDir = cachingDir;
		if(!cachingDir.endsWith("/")) this.cachingDir += "/";
	}

	@Override
	public String getHTML(String url)
	{
		String requestCode = getRequestCode(url);
		String cache = readCache(requestCode);
		String localLastModified = dateFormat.format(0);
		System.out.println("Getting URL request: " + url);

		if(cache != null) {
			JSONObject cacheJSON = new JSONObject(cache);
			localLastModified = cacheJSON.get("last_modified").toString();
			JSONArray arr = cacheJSON.optJSONArray("cache_array");
			if(arr != null) cache = arr.toString();
		}

		URL oracle;
		HttpURLConnection conn;
		String line;
		BufferedReader rd;
		StringBuilder result = new StringBuilder();

		try {
			oracle = new URL(url);
			conn = (HttpURLConnection) oracle.openConnection();
			conn.setRequestMethod("GET");
			conn.addRequestProperty("X-TBA-App-Id", getAppId());
			conn.addRequestProperty("If-Modified-Since ", localLastModified);

			int responseCode = conn.getResponseCode();

			if(cache != null && responseCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
				//System.out.println("Server returned 304. Using cache...");
				return cache;
			}
			else if (responseCode == HttpURLConnection.HTTP_OK)
			{
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = rd.readLine()) != null) {
					result.append(line).append("\n");
				}
				rd.close();
				writeCache(result.toString(), requestCode);
				return result.toString();
			} else System.out.println(responseCode);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(cache != null) return cache;
		else return null;
	}

	private String getRequestCode(String url)
	{
		String code = url.replace("http://www.thebluealliance.com/api/v2/", "");
		code = code.replace("/", "_");
		return code;
	}

	private void writeCache (final String text, final String requestCode)
	{
		new Thread(new Runnable() {

			@Override
			public void run()
			{
				if (text.startsWith("{")) {
					JSONObject cache = new JSONObject(text);
					cache.append("last_modified", dateFormat.format(System.currentTimeMillis()));

					try (BufferedWriter out = new BufferedWriter(new FileWriter(cachingDir + requestCode + ".json"))) {
						cache.write(out);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (text.startsWith("[")) {
					JSONArray cache = new JSONArray(text);
					JSONObject cacheObject = new JSONObject();
					cacheObject.put("last_modified", dateFormat.format(System.currentTimeMillis()));
					cacheObject.put("cache_array", cache);

					try (BufferedWriter out = new BufferedWriter(new FileWriter(cachingDir + requestCode + ".json"))) {
						cacheObject.write(out);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
	}

	private String readCache (String requestCode)
	{
		try (BufferedReader r = new BufferedReader(new FileReader(cachingDir + requestCode + ".json"))) {
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
}
