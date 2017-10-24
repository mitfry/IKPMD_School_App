package s1080488.ikpmd_app.Threads;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import layout.dinoDataFragment;


/**
 * Created by Mitchell on 19-10-2017.
 */

public class fetchDinoData extends AsyncTask<Void, Void, Void> {
    private List<List<String>> allParsedStreamData = new ArrayList<>();
    private String dinoName = "";
    private AsyncResponse delegate = null;

    public fetchDinoData(String jsonDinoName, AsyncResponse delegate) {
        super();
        dinoName = jsonDinoName;
        this.delegate = delegate;
    }

    public interface AsyncResponse {
        void processFinished();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            //URL for API to send the right dino data back to App.
            URL url = new URL("https://www.dinodeluxe.eu/DinoDeluxe_App/serverDinoData.php");

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("dinoName", dinoName);
            //Log.d("params", postDataParams.toString());

            //Default reply encoded in API because code below doesn't work yet.
            //Post data seems incorrect
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            //urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            //Column counter for later use
            int columnsNeeded = 0;

            //Read data retrieved from URL
            BufferedReader dataReader = new BufferedReader(new
                    InputStreamReader(urlConnection.getInputStream()));

            //Loop through retrieved data and add to StringBuffer
            String line;
            StringBuffer sb = new StringBuffer("");
            while ((line = dataReader.readLine()) != null) {
                sb.append(line);
            }

            //Create jsonArrays from data in StringBuffer
            JSONArray jsonArray = new JSONArray(String.valueOf(sb));

            //Loop through each jsonArray
            for (int i = 0; i < jsonArray.length(); i++) {
                columnsNeeded = 0;
                String lastKey = "";
                List<String> tempData = new ArrayList<>();
                JSONObject row = jsonArray.getJSONObject(i);

                //Iterate over each row inside the jsonArrays
                for (Iterator<String> iter = row.keys(); iter.hasNext(); ) {
                    String key = String.valueOf(iter.next());
                    String value = String.valueOf(row.get(key));

                    //Check if gender data is present, if not add place-holding
                    //increase total required colums if data was added during iteration.
                    if (lastKey.equals("colorSetIndices") && key.equals("id")) {
                        tempData.add("false");
                        columnsNeeded++;
                    }

                    //Workaround to count the amount of rows with multiple values
                    //so we can iterate these as jsonObjects
                    int charCount = 0;
                    for (char ch : value.toCharArray()) {
                        if (ch == ':') {
                            charCount++;
                        }
                    }

                    //If multiple rows are found we're going to iterate on these
                    //and add the individual values to the complete array.
                    if (charCount != 0) {
                        JSONObject innerRow = new JSONObject(value);

                        //Prepare all Integer and String variables that are used
                        //for checking if the correct data is present or not.
                        int rightStats = 0;
                        int cycles = 0;
                        int addedEmptyField = 0;

                        String lastStat = "";
                        String innerValue = "";
                        String innerKey = "";

                        //Iterator for rows with multiple values. MUST iterate at least 7 times to
                        //always add 7 stats for each dino.
                        for (Iterator<String> innerIter = innerRow.keys(); innerIter.hasNext() || cycles != 7; ) {

                            //Check if last iteration added a field for a missing value.
                            //Check for nextValue to prevent crash with non-existing index.
                            if (addedEmptyField == 0 && innerIter.hasNext()) {
                                innerKey = innerIter.next();
                                innerValue = String.valueOf(innerRow.get(innerKey));
                            }

                            //Reset field for next iteration
                            addedEmptyField = 0;

                            //The almighty checks to ensure all 7 stats are present, checked and
                            //in the right order at the right moment. Checks previous checked stat
                            //and counts the amount of right stats so far.
                            if (key.equals("wildLevels")) {
                                if (lastStat.equals("") && innerKey.equals("health") && rightStats == 0) {
                                    lastStat = "health";
                                    rightStats++;
                                } else if (lastStat.equals(lastStat) && innerKey.equals("stamina") && rightStats == 1) {
                                    lastStat = "stamina";
                                    rightStats++;
                                } else if (lastStat.equals(lastStat) && innerKey.equals("oxygen") && rightStats == 2) {
                                    lastStat = "oxygen";
                                    rightStats++;
                                } else if (lastStat.equals(lastStat) && innerKey.equals("food") && rightStats == 3) {
                                    lastStat = "food";
                                    rightStats++;
                                } else if (lastStat.equals(lastStat) && innerKey.equals("weight") && rightStats == 4) {
                                    lastStat = "weight";
                                    rightStats++;
                                } else if (lastStat.equals(lastStat) && innerKey.equals("melee") && rightStats == 5) {
                                    lastStat = "melee";
                                    rightStats++;
                                } else if (lastStat.equals(lastStat) && innerKey.equals("speed") && rightStats == 6) {
                                    lastStat = "speed";
                                    rightStats++;
                                }

                                //Increase the amount of cycles for each inner row.
                                cycles++;

                                //Correct missing values if not enough correct stats were found
                                //for the amount of cycles ran.
                                if (cycles != rightStats) {
                                    tempData.add("0");
                                    addedEmptyField = 1;
                                    columnsNeeded++;

                                    //Switch to set the last check stat if it would'nt have
                                    //been missing and filled up with an empty value.
                                    switch (cycles) {
                                        case 1:
                                            lastStat = "health";
                                            break;
                                        case 2:
                                            lastStat = "stamina";
                                            break;
                                        case 3:
                                            lastStat = "oxygen";
                                            break;
                                        case 4:
                                            lastStat = "food";
                                            break;
                                        case 5:
                                            lastStat = "weight";
                                            break;
                                        case 6:
                                            lastStat = "melee";
                                            break;
                                        case 7:
                                            lastStat = "speed";
                                            break;
                                        default:
                                            lastStat = "";
                                    }

                                    //Since a correcting value has been added, we can set
                                    //the amount of successfully found stats to 1 per cycle.
                                    rightStats = cycles;
                                }

                            }
                            //Set amount of cycles to 7 if multiple rows are not found.
                            //This allows for new iteration to occur.
                            else {
                                cycles = 7;
                            }

                            //Add current keyValue only if no place-holding was already added.
                            if (addedEmptyField != 1) {
                                tempData.add(innerValue);

                                //increase total required columns if data was added during iteration.
                                columnsNeeded++;

                                //Reset value since correction have been performed.
                                addedEmptyField = 0;
                            }
                        }

                    } else {
                        //Collect data to later add to allParsedStreamData.
                        tempData.add(value);

                        //increase total required columns if data was added during iteration.
                        columnsNeeded++;
                    }

                    //Set last iterated key to determine if gender stat is present.
                    lastKey = key;
                }

                //Add ready for use in tableLayout collected data
                //during iteration to total collection of data.
                allParsedStreamData.add(tempData);
            }

            //Horizontal scroll is impossible on GridView Fuck
            //Prepare column width

            dinoDataFragment.columns = columnsNeeded;
            Log.d("columnsNeeded", " " + columnsNeeded);

        } catch (
                Exception e)

        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        //Renew arrayList that collects dino data.
        dinoDataFragment.dinoData = new ArrayList<>();

        //Prepare custom comparator to sort output based on dino level (high to low).
        final Comparator<List<String>> comparator = new Comparator<List<String>>() {
            public int compare(List<String> o2, List<String> o1) {
                Integer firstInteger = Integer.parseInt(o1.get(0));
                Integer secondInteger = Integer.parseInt(o2.get(0));

                return firstInteger.compareTo(secondInteger);
            }
        };

        //Execute custom comparator to sort dino levels.
        Collections.sort(allParsedStreamData, comparator);

        //Add all collected data to variable in fragment.
        for (int i = 0; i < allParsedStreamData.size(); i++) {
            dinoDataFragment.dinoData.addAll(allParsedStreamData.get(i));
        }

        //Let fragment know thread has finished preparing data.
        delegate.processFinished();
    }

    //Might be useless after new POST code
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
