package s1080488.ikpmd_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.view.Gravity.CENTER;

public class MainActivity extends AppCompatActivity {
    Button btnLogin;
    EditText txtUsername, txtPassword;
    int counter = 3;

    @Override       // Because : Inheritance
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     // Check with Parent
        setContentView(R.layout.activity_main); // Load view

        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        //Snel inloggen
        txtUsername.setText("admin");
        txtPassword.setText("admin");

        Log.d("Hieperdepiep", " Hoera!"); // System.out.println
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Hide the actionbar title
        getSupportActionBar().setTitle(null);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            //Change text color for feedback
            private void showColorOnTextView(final EditText e, String newColor) {
                final int oldColor = e.getCurrentHintTextColor();

                e.setHintTextColor(Color.parseColor(newColor));
                e.setTextColor(Color.parseColor(newColor));

                Handler colorHandler = new Handler();
                colorHandler.postDelayed(new Runnable() {
                    public void run() {
                        e.setHintTextColor(oldColor);
                        e.setTextColor(oldColor);
                    }
                }, 2000);
            }

            @Override
            public void onClick(View v) {
                Log.d("Login knop ingedrukt", "Start LoginActivity");

                if (txtUsername.getText().toString().equals("admin") &&
                        txtPassword.getText().toString().equals("admin")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Redirecting...", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | CENTER, 0, 15);
                    toast.show();

                    showColorOnTextView(txtUsername, "GREEN");
                    showColorOnTextView(txtPassword, "GREEN");

                    Handler feedbackHandler = new Handler();
                    feedbackHandler.postDelayed(new Runnable() {
                        public void run() {
                            //Data transfer to login activity using intent
                            Intent intentLoginActivity = new Intent(getApplicationContext(),
                                    MainNavigation.class);

                            intentLoginActivity.putExtra("intentUsername",
                                    txtUsername.getText().toString());

                            startActivity(intentLoginActivity);
                        }
                    }, 100);

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Wrong credentials.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | CENTER, 0, 15);
                    toast.show();

                    showColorOnTextView(txtUsername, "RED");
                    showColorOnTextView(txtPassword, "RED");
                    counter--;

                    if (counter == 0) {
                        btnLogin.setEnabled(false);
                        btnLogin.setBackgroundColor(Color.parseColor("GREY"));
                        showColorOnTextView(txtUsername, "RED");
                        showColorOnTextView(txtPassword, "RED");
                    }
                }
            }
        });

        // INLINE METHOD WRITING –
        // SINCE WE WILL ONLY USE THIS METHOD ONCE
        // AND WE CAN ONLY USE THIS METHOD FOR THIS CASE
        // GET USED TO IT ..
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // To give myself feedback and help to test.
    public static void toastMessage(Context context, String tMessage) {
        Toast.makeText(context, tMessage, Toast.LENGTH_SHORT).show();
    }
}
