package s1080488.ikpmd_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import s1080488.ikpmd_app.Models.LoginRequest;

import static android.view.Gravity.CENTER;

public class MainActivity extends AppCompatActivity {
    Button btnLogin;
    EditText txtUsername, txtPassword;
    int counter = 3;

    String username;
    String email;
    String firstname;
    String lastname;
    String ign;
    String u_password;

    @Override       // Because : Inheritance
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     // Check with Parent
        setContentView(R.layout.activity_main); // Load view

        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        //Snel inloggen
        txtUsername.setText("test");
        txtPassword.setText("test");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Add register button
        final TextView tvRegister = (TextView) findViewById(R.id.lblRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(registerIntent);
            }
        });

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
                final String i_username = String.valueOf(txtUsername.getText());
                final String i_password = String.valueOf(txtPassword.getText());

                if (!i_username.equals("") && !i_password.equals("")) {

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                final JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (txtUsername.getText().toString().equals("test") &&
                                        txtPassword.getText().toString().equals("test")) {
                                    counter = 3;
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Redirecting...", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.BOTTOM | CENTER, 0, 15);
                                    toast.show();

                                    showColorOnTextView(txtUsername, "GREEN");
                                    showColorOnTextView(txtPassword, "GREEN");

                                    Handler feedbackHandler = new Handler();
                                    feedbackHandler.postDelayed(new Runnable() {
                                        public void run() {

                                            //Test Data transfer to login activity using intent
                                            Intent intent = new Intent(getApplicationContext(),
                                                    MainNavigation.class);

                                            intent.putExtra("username",
                                                    txtUsername.getText().toString());

                                            startActivity(intent);
                                        }
                                    }, 100);

                                } else if (success) {
                                    counter = 3;
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Redirecting...", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.BOTTOM | CENTER, 0, 15);
                                    toast.show();

                                    showColorOnTextView(txtUsername, "GREEN");
                                    showColorOnTextView(txtPassword, "GREEN");

                                    Handler feedbackHandler = new Handler();
                                    feedbackHandler.postDelayed(new Runnable() {
                                        public void run() {
                                            try {
                                                //Data transfer to MainNavigation activity using intent
                                                username = jsonResponse.getString("username");
                                                email = jsonResponse.getString("email");
                                                firstname = jsonResponse.getString("firstname");
                                                lastname = jsonResponse.getString("lastname");
                                                ign = jsonResponse.getString("ign");
                                                u_password = jsonResponse.getString("u_password");

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            Intent intent = new Intent(MainActivity.this, MainNavigation.class);
                                            intent.putExtra("username", username);
                                            intent.putExtra("email", email);
                                            intent.putExtra("firstname", firstname);
                                            intent.putExtra("lastname", lastname);
                                            intent.putExtra("ign", ign);
                                            intent.putExtra("u_password", u_password);

                                            MainActivity.this.startActivity(intent);
                                        }
                                    }, 1000);


                                } else {
                                    //AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    //builder.setMessage("Login failed!").setNegativeButton("Retry", null).create().show();

                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Wrong credentials.", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.BOTTOM | CENTER, 0, 15);
                                    toast.show();

                                    showColorOnTextView(txtUsername, "RED");
                                    showColorOnTextView(txtPassword, "RED");
                                    counter--;

                                    //Disable login button after 3 failed attempts
                                    if (counter == 0) {
                                        btnLogin.setEnabled(false);
                                        btnLogin.setBackgroundColor(Color.parseColor("GREY"));
                                        showColorOnTextView(txtUsername, "RED");
                                        showColorOnTextView(txtPassword, "RED");
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(i_username, i_password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(loginRequest);
                } else {
                    toastMessage(MainActivity.this, "Please enter a username and password.");
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
