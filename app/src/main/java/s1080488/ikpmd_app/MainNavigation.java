package s1080488.ikpmd_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;

import layout.dinoDataFragment;
import layout.editDataFragment;
import layout.serversFragment;
import s1080488.ikpmd_app.Threads.fetchServerData;


public class MainNavigation extends AppCompatActivity {
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

        //prepare server data
        loadServerData(serversFragment.json_urls);

        //Set default title for this fragment
        getSupportActionBar().setTitle(R.string.title_servers);
    }

    public void changeFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out).show(bottomNavFragment);
        ft.replace(R.id.mainNavFragment, bottomNavFragment);
        ft.commit();
    }

    public void loadServerData(String[] urls) {
        //Clear serversData list
        serversData = new ArrayList<>();
        for (String url : urls) {
            fetchServerData process = new fetchServerData(url);
            process.execute();
        }
    }

    public void logServerData(){
        Log.d("ServersData ->"," "+serversData);
    }


    public void setServerData() {
        //Set results in fragment
        if (serversData.get(0).contains("Island")) {
            serversFragment.tvServerData1.setText(serversData.get(0));
        } else if (serversData.get(1).contains("Scorched Earth")) {
            serversFragment.tvServerData2.setText(serversData.get(1));
        } else if (serversData.get(2).contains("The Center")) {
            serversFragment.tvServerData3.setText(serversData.get(2));
        } else if (serversData.get(3).contains("Ragnarok")) {
            serversFragment.tvServerData4.setText(serversData.get(3));
        }
    }
}
