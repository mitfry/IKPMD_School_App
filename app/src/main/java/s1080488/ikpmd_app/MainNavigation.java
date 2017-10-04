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

import java.util.Arrays;

import layout.dinoDataFragment;
import layout.editDataFragment;
import layout.serversFragment;
import s1080488.ikpmd_app.Threads.fetchServerData;


public class MainNavigation extends AppCompatActivity {
    Fragment bottomNavFragment;
    public static String serverData;

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
        for (String url : urls) {
            //fetchServerData.json_url = url;
            fetchServerData process = new fetchServerData(url);
            process.execute();
        }
    }


    public void setServerData() {
        //Set results in fragment
        if (Arrays.asList(serverData).contains("Island")) {
            serversFragment.tvServerData1.setText(serverData);
        } else if (Arrays.asList(serverData).contains("Scorched Earth")) {
            serversFragment.tvServerData2.setText(serverData);
        } else if (Arrays.asList(serverData).contains("The Center")) {
            serversFragment.tvServerData3.setText(serverData);
        } else if (Arrays.asList(serverData).contains("Ragnarok")) {
            serversFragment.tvServerData4.setText(serverData);
        }
    }
}
