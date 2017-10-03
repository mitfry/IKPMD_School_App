package s1080488.ikpmd_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import layout.dinoDataFragment;
import layout.editDataFragment;
import layout.serversFragment;
import s1080488.ikpmd_app.Databases.DatabaseHelper;


public class MainNavigation extends AppCompatActivity {
    Fragment bottomNavFragment;

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
}
