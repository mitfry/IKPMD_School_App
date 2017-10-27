package s1080488.ikpmd_app.Threads;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import s1080488.ikpmd_app.MainNavigation;


/**
 * Created by Mitchell on 27-9-2017.
 */

public class fetchServerData extends AsyncTask<Void, Void, Void> {
    private String rawStreamData = "";
    private String allParsedStreamData = "";
    private String json_url = "";
    private AsyncResponse delegate = null;

    public fetchServerData(String url, AsyncResponse delegate) {
        super();
        json_url = url;
        this.delegate = delegate;
    }

    public interface AsyncResponse {
        void processFinished();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL(json_url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream dataStream = urlConnection.getInputStream();
            BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataStream));
            String dataLine = "";

            //String buffer might be better?
            while (dataLine != null) {
                dataLine = dataReader.readLine();
                rawStreamData = rawStreamData + dataLine;
            }

            JSONObject jsonDataObject = new JSONObject(rawStreamData);

            //Make data more easily readable
            allParsedStreamData = "Name: " + jsonDataObject.getString("hostname").substring(0, 45) + "\n" +
                    "Address: " + jsonDataObject.getString("address") + "\n" +
                    "Map name: " + jsonDataObject.getString("map") + "\n" +
                    "Location: " + jsonDataObject.getString("location") + "\n" +
                    "Online Players: " + jsonDataObject.getString("players") + "\n" +
                    "Data from: " + jsonDataObject.getString("last_check") + "\n";

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        MainNavigation.serverData = null;
        MainNavigation.serverData = allParsedStreamData;
        MainNavigation.serversData.add(allParsedStreamData);
        delegate.processFinished();
    }
}
