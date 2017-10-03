package s1080488.ikpmd_app.Threads;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import layout.serversFragment;
import s1080488.ikpmd_app.MainActivity;

/**
 * Created by Mitchell on 27-9-2017.
 */

public class fetchServerData extends AsyncTask<Void, Void, Void> {
    private String rawStreamData = "";
    private String parsedSteamData = "";
    private String allParsedStreamData = "";

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL("https://ark-servers.net/api/?object=servers&element=detail&key=KGj7i6Jy3iXCtHyb9SdF3fMPRCWZfPnIigG");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream dataStream = urlConnection.getInputStream();
            BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataStream));
            String dataLine = "";

            //String buffer might be better?
            while (dataLine != null) {
                dataLine = dataReader.readLine();
                rawStreamData = rawStreamData + dataLine;
            }

            //We krijgen geen JSON array maar een JSON object
//            JSONArray jsonDataArray = new JSONArray(rawStreamData);
//
//            //TO-DO Show using listview?
//            for (int i = 0; i > jsonDataArray.length(); i++) {
//                JSONObject jsonDataObject = (JSONObject) jsonDataArray.get(i);
//                parsedSteamData = "Name: " + jsonDataObject.get("name") + "\n" +
//                        "Address: " + jsonDataObject.get("address") + "\n" +
//                        "Map name: " + jsonDataObject.get("map") + "\n" +
//                        "Location: " + jsonDataObject.get("location") + "\n";
//
//                allParsedStreamData = allParsedStreamData + parsedSteamData;
//            }

            JSONObject jsonDataObject = new JSONObject(rawStreamData);

            // Display selected elements
            allParsedStreamData = "Name: " + jsonDataObject.getString("name") + "\n" +
                    "Address: " + jsonDataObject.getString("address") + "\n" +
                    "Map name: " + jsonDataObject.getString("map") + "\n" +
                    "Location: " + jsonDataObject.getString("location") + "\n" +
                    "Data from: " + jsonDataObject.getString("last_check") + "\n";

            // Loop through all values
//            Iterator<String> jsonKeys = jsonDataObject.keys();
//            while (jsonKeys.hasNext()) {
//                String keyValue = jsonKeys.next();
//                parsedSteamData = jsonDataObject.getString(keyValue) + " \n";
//                allParsedStreamData = allParsedStreamData + parsedSteamData;
//                Log.d("jsDataArray: ", "" + parsedSteamData);
//            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        serversFragment.tvServerData.setText(this.allParsedStreamData);
    }
}
