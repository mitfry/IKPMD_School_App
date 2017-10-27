package s1080488.ikpmd_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import layout.dinoDataFragment;
import layout.editDataFragment;
import layout.serversFragment;
import s1080488.ikpmd_app.Threads.fetchServerData;


public class MainNavigation extends AppCompatActivity implements fetchServerData.AsyncResponse {
    Fragment bottomNavFragment;
    public static String serverData;
    public static ArrayList<String> serversData;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportActionBar().setTitle(R.string.title_servers);
                    bottomNavFragment = new serversFragment();
                    changeFragment();
                    return true;
                case R.id.navigation_dashboard:
                    getSupportActionBar().setTitle(R.string.title_database_info);
                    bottomNavFragment = new dinoDataFragment();
                    changeFragment();
                    return true;
                case R.id.navigation_notifications:
                    getSupportActionBar().setTitle(R.string.title_edit_database);
                    bottomNavFragment = new editDataFragment();
                    changeFragment();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Prepare server data for local use
        loadServerData(serversFragment.json_urls);

        //Prepare dino types for auto complete
        fetchAvailableDinoNames();

        //Set default title for this fragment
        getSupportActionBar().setTitle(R.string.title_servers);

        //Retrieve all user data after successful login using intents.
        //Will later be used for editing account details!
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String firstname = intent.getStringExtra("firstname");
        String lastname = intent.getStringExtra("lastname");
        String ign = intent.getStringExtra("ign");
        String u_password = intent.getStringExtra("u_password");

        //Add capital letter to username
        username = username.substring(0, 1).toUpperCase() + username.substring(1);
        Toast.makeText(this.getApplicationContext(), "Welcome " + username + "!", Toast.LENGTH_LONG).show();
    }

    //This overrides the implemented method from AsyncResponse
    @Override
    public void processFinished() {
        //This code is run when the asyncTask completes
        //show the results loaded so far
        setServerData();
    }

    //Switch between fragments
    public void changeFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out).show(bottomNavFragment);
        ft.replace(R.id.mainNavFragment, bottomNavFragment);
        ft.commit();
    }

    //Clear serversData list and reload it from internet
    public void loadServerData(String[] urls) {
        serversData = new ArrayList<>();
        for (String url : urls) {
            fetchServerData process = new fetchServerData(url, this);
            process.execute();
        }
    }

    //Show results in serverFragment
    public void setServerData() {
        for (int i = 0; i < serversData.size(); i++) {
            if (serversData.get(i).contains("Island")) {
                serversFragment.tvServerData1.setText(serversData.get(i));
            } else if (serversData.get(i).contains("Scorched")) {
                serversFragment.tvServerData2.setText(serversData.get(i));
            } else if (serversData.get(i).contains("Center")) {
                serversFragment.tvServerData3.setText(serversData.get(i));
            } else if (serversData.get(i).contains("Ragnarok")) {
                serversFragment.tvServerData4.setText(serversData.get(i));
            }
        }
    }

    public void fetchAvailableDinoNames() {
        RequestQueue mRequestQueue;

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

        // Start the queue
        mRequestQueue.start();

        String url = "https://www.dinodeluxe.eu/DinoDeluxe_App/serverDinoTypes.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                editDataFragment.availableDinoTypes.add(response.getString(i));

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                editDataFragment.availableDinoTypes.add(response.getString(i));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        mRequestQueue.add(jsonArrayRequest);
    }
}
