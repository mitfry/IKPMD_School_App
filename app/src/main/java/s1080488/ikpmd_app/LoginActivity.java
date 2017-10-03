package s1080488.ikpmd_app;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import s1080488.ikpmd_app.Databases.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    Button btBackToMain;
    DatabaseHelper dbHelper;
    SQLiteDatabase ddDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Retrieve username intent from mainActivity
        String intentUsername = getIntent().getStringExtra("intentUsername");
        //Add capital letter to username
        intentUsername = intentUsername.substring(0, 1).toUpperCase() + intentUsername.substring(1);
        TextView txtIntentUsername = (TextView) findViewById(R.id.txtIntentUsername);

        txtIntentUsername.setText("Logged in as: " + intentUsername);

        //SQLite is Thread safe by default
        dbHelper = new DatabaseHelper(this);
        ddDatabase = dbHelper.getWritableDatabase();

        btBackToMain = (Button) findViewById(R.id.btBackToMain);
        //Back to login/start screen
        btBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Back knop ingedrukt", "Ga terug naar main");
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }
}
